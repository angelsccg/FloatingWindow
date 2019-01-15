package com.angels.world.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.angels.world.R;
import com.angels.world.bd.StepDBManager;
import com.angels.world.constant.KeyConfig;

/**
 * 蓝牙
 * */
public class BluetoothActivity extends BaseActivity implements View.OnClickListener{


    private TextView tvBle,tvSpp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_type);

        tvBle = findViewById(R.id.tv_ble);
        tvSpp = findViewById(R.id.tv_spp);

        tvBle.setOnClickListener(this);
        tvSpp.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_ble:{
                Intent intent = new Intent(view.getContext(), BluetoothListActivity.class);
                intent.putExtra(KeyConfig.KEY_BLUETOOTH_TYPE,KeyConfig.KEY_BLUETOOTH_BLE);
                startActivity(intent);
                break;
            }
            case R.id.tv_spp:{
                Intent intent = new Intent(view.getContext(), BluetoothListActivity.class);
                intent.putExtra(KeyConfig.KEY_BLUETOOTH_TYPE,KeyConfig.KEY_BLUETOOTH_SPP);
                startActivity(intent);
                break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
