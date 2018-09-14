package com.angels.world.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.angels.world.R;
import com.angels.world.constant.RequestUrl;
import com.angels.library.utils.AcAppUtil;

/**
 * Name: SplashAdActivity
 * Comment: 启动页
 */

public class SplashActivity extends BaseActivity {
    TextView tvVersion,tvName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        tvVersion = (TextView) findViewById(R.id.tv_version);
        tvVersion.setText("V"+ AcAppUtil.getCurrentVersionName(this));

        tvName = (TextView) findViewById(R.id.tv_name);
        tvName.setText(RequestUrl.APP_ENVI_NAME[RequestUrl.configType]);
        //延时跳转到主页面，splash用来做引导页
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                SplashActivity.this.startActivity(intent);
                SplashActivity.this.finish();
            }
        },1000*2);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
