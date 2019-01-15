package com.angels.world.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.angels.world.R;

public class D3Activity extends BaseActivity implements View.OnClickListener {

    public static final String[] btnNames= {"图片3D效果","3D模型（EasyShow3D）"};
    public static final Button[] btns = new Button[btnNames.length];
    private LinearLayout llContent;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common);

        llContent = (LinearLayout) findViewById(R.id.llContent);

        for (int i = 0; i < btnNames.length; i++) {
            Button button = new Button(this);
            button.setText(btnNames[i]);
            button.setTextColor(Color.parseColor("#ffffff"));
            button.setId(i);
            button.setOnClickListener(this);
            button.setBackgroundResource(R.drawable.blue4_button);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(10,10,10,10);
            button.setLayoutParams(params);
            llContent.addView(button);
            btns[i] = button;
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case 0: {
                Intent intent = new Intent(this, D3ImageActivity.class);
                startActivity(intent);
            }
            break;
            case 1: {
                Intent intent = new Intent(this, D3EasyShowActivity.class);
                startActivity(intent);
            }
            break;
        }
    }
}