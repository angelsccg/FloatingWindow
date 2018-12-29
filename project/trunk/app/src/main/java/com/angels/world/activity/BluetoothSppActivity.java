package com.angels.world.activity;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.angels.library.widget.AcCustomTitleLayout;
import com.angels.world.R;
import com.angels.world.constant.KeyConfig;

/**
 * 蓝牙SPP
 * */
public class BluetoothSppActivity extends BaseActivity implements View.OnClickListener{


    private BluetoothDevice device;
    private AcCustomTitleLayout titleLayout;

    private EditText etSendData;
    private Button btnSend;
    private TextView tvData;
    private TextView tvState;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spp);

        device = getIntent().getParcelableExtra(KeyConfig.KEY_DEVICE);

        initView();
        initTitleView();
        initData();
    }

    private void initView() {
        etSendData = findViewById(R.id.et_send_data);
        btnSend = findViewById(R.id.btn_send);
        tvData = findViewById(R.id.tv_data);
        tvState = findViewById(R.id.tv_state);
    }

    private void initTitleView() {
        titleLayout = findViewById(R.id.rl_title);
        titleLayout.setTitle(device.getName()+"(" + device.getAddress() + ")");
        titleLayout.setIvLeft(R.drawable.ic_back_white, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleLayout.setTvRight("清空", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvData.setText("");
                etSendData.setText("");
            }
        });
    }

    private void initData() {
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
