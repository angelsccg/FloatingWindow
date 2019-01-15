package com.angels.world.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.widget.TextView;
import android.widget.Toast;

import com.angels.world.R;
import com.angels.world.Sensor.OrientSensor;
import com.angels.world.Sensor.StepSensorAcceleration;
import com.angels.world.Sensor.StepSensorBase;
import com.angels.world.Sensor.StepSensorPedometer;
import com.angels.world.bd.StepDBManager;
import com.angels.world.service.WorldProtectService;
import com.angels.world.service.WorldService;
import com.angels.world.widget.StepView;

/**
 * 计步器
 * */
public class StepActivity extends BaseActivity{


    private TextView tvStep;

    private Handler handler = new Handler();
    private  Runnable runnable = new Runnable(){
        @Override
        public void run() {
            tvStep.setText(""+StepDBManager.query(System.currentTimeMillis()).getStep());
            handler.postDelayed(this, 1000);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        SensorUtil.getInstance().printAllSensor(this); // 打印所有可用传感器
        setContentView(R.layout.activity_step);
        tvStep =  findViewById(R.id.tv_step);

//        // 开启计步监听, 分为加速度传感器、或计步传感器
//        stepSensor = new StepSensorPedometer(this, this);
//        if (!stepSensor.registerStep()) {
//            Toast.makeText(this, "计步传传感器不可用！", Toast.LENGTH_SHORT).show();
//            stepSensor = new StepSensorAcceleration(this, this);
//            if (!stepSensor.registerStep()) {
//                Toast.makeText(this, "加速度传感器不可用！", Toast.LENGTH_SHORT).show();
//            }
//        }
        handler.postDelayed(runnable,1000);


        Intent i1 = new Intent(this, WorldService.class);
        startService(i1);

        Intent i2 = new Intent(this, WorldProtectService.class);
        startService(i2);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        handler.removeCallbacks(runnable);
    }
}
