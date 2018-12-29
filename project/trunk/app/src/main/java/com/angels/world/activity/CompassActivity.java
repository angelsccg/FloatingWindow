package com.angels.world.activity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.angels.library.widget.AcCompassView;
/**
 * 指南针
 *
 * 方向传感器
 *
 *
 * 手机状态                           数值状态            数值范围
 手机水平放置，顶部指向正北方      x、y、z方向值为0           0
 水平逆时针旋转                       x不断减少            360 ~ 0
 水平顺时针旋转                       x不断增大            0 ~ 360
 当手机左侧抬起时                     z不断减少            0 ~ -180
 当手机右侧抬起时                     z不断增大            0 ~ 180
 当手机顶部抬起时                     y不断减少             0 ~ -180
 当手机底部抬起时                      y不断增大           0 ~ 180
 * */
public class CompassActivity extends BaseActivity {

    private SensorManager sensorManager;
    private SensorEventListener sensorEventListener;
    private AcCompassView compassView;
    private float val;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        compassView = new AcCompassView(this);
        setContentView(compassView);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorEventListener = new SensorEventListener() {

            @Override
            public void onSensorChanged(SensorEvent event) {
                val = event.values[0];
                compassView.setVal(val);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };

        sensorManager.registerListener(sensorEventListener,sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        sensorManager.unregisterListener(sensorEventListener);
    }
}
