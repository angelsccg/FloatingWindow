package com.angels.world.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.angels.library.utils.AcLogUtil;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;


/**
 * Created by Administrator on 2017/8/17.
 */

public class BaseActivity extends AppCompatActivity {

    protected int widthScreen;
    protected int heightScreen;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager manager = this.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        widthScreen = outMetrics.widthPixels;
        heightScreen = outMetrics.heightPixels;

        //友盟推送
        PushAgent.getInstance(this).onAppStart();
        AcLogUtil.i("友盟-->BaseActivity-->id:"+PushAgent.getInstance(this).getRegistrationId());
    }


    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this); // 基础指标统计，不能遗漏
    }
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
