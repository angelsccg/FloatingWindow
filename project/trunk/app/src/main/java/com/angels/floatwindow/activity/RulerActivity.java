package com.angels.floatwindow.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.angels.floatwindow.widget.RulerView;

public class RulerActivity extends  BaseActivity {
    private RulerView rulerView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
