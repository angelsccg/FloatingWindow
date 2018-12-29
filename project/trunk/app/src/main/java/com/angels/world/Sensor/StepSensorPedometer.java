package com.angels.world.Sensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.util.Log;

import com.angels.library.utils.AcLogUtil;
import com.angels.world.bd.StepDBManager;

/**
 * 计步传感器
 */
public class StepSensorPedometer extends StepSensorBase {
    private final String TAG = "StepSensorPedometer";
    private int lastStep = -1;
    private int liveStep = 0;
    private int increment = 0;
    private int sensorMode = 0; // 计步传感器类型

    public StepSensorPedometer(Context context, StepCallBack stepCallBack) {
        super(context, stepCallBack);
    }

    @Override
    protected void registerStepListener() {
        Sensor detectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
//        if (sensorManager.registerListener(this, detectorSensor, SensorManager.SENSOR_DELAY_GAME)) {
        if (sensorManager.registerListener(this, detectorSensor, SensorManager.SENSOR_DELAY_NORMAL)) {
            isAvailable = true;
            sensorMode = 0;
            Log.i(TAG, "计步传感器Detector可用！");
//        } else if (sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_GAME)) {
        } else if (sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_NORMAL)) {
            isAvailable = true;
            sensorMode = 1;
            Log.i(TAG, "计步传感器Counter可用！");
        } else {
            isAvailable = false;
            Log.i(TAG, "计步传感器不可用！");
        }
    }

    @Override
    public void unregisterStep() {
        sensorManager.unregisterListener(this);
    }

    /**
     * 每次第一次启动记步服务时是否从系统中获取了已有的步数记录
     */
    private boolean hasRecord = false;
    /**
     * 系统中获取到的已有的步数
     */
    private int hasStepCount = 0;
    /**
     * 上一次的步数
     */
    private int previousStepCount = 0;
    /**
     * 当前步数
     */
    private int nowBuSu = 0;
    @Override
    public void onSensorChanged(SensorEvent event) {
        liveStep = (int) event.values[0];
        if (sensorMode == 0) {
            Log.i(TAG, "Detector步数："+liveStep);
            StepSensorBase.CURRENT_SETP += liveStep;
        } else if (sensorMode == 1) {
            Log.i(TAG, "Counter步数："+liveStep);
//            StepSensorBase.CURRENT_SETP = liveStep;


            //获取当前传感器返回的临时步数
            int tempStep = (int) event.values[0];
            //首次如果没有获取手机系统中已有的步数则获取一次系统中APP还未开始记步的步数
            if (!hasRecord) {
                hasRecord = true;
                hasStepCount = tempStep;
            } else {
                //获取APP打开到现在的总步数=本次系统回调的总步数-APP打开之前已有的步数
                int thisStepCount = tempStep - hasStepCount;
                //本次有效步数=（APP打开后所记录的总步数 - 上一次APP打开后所记录的总步数）
                int thisStep = thisStepCount - previousStepCount;
                //总步数=现有的步数+本次有效步数
                nowBuSu += (thisStep);
                //记录最后一次APP打开到现在的总步数
                previousStepCount = thisStepCount;
                AcLogUtil.i("步数-->" + "系统总步数：" + tempStep + ",本次有效步数:" + thisStep + ",总步数:" + nowBuSu);
                StepSensorBase.CURRENT_SETP = nowBuSu;

                if(thisStep != 0){
                    StepDBManager.save(thisStep);
                }
            }

        }
        stepCallBack.Step(StepSensorBase.CURRENT_SETP);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
