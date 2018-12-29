package com.angels.world.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Window;
import android.view.WindowManager;

import com.angels.world.widget.RulerView;

public class RulerActivity extends  BaseActivity {
    private RulerView rulerView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去除状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(rulerView = new RulerView(this));
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (rulerView != null) {
            rulerView.setLineX(savedInstanceState.getFloat("lineX"));
            rulerView.setKedu(savedInstanceState.getInt("kedu"));
        }
    }


    @Override

    protected void onSaveInstanceState(Bundle outState) {
        if (rulerView != null) {
            outState.putFloat("lineX", rulerView.getLineX());
            outState.putInt("kedu", rulerView.getKedu());
        }
        super.onSaveInstanceState(outState);
    }
}
