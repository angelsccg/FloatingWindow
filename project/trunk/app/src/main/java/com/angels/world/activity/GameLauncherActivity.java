package com.angels.world.activity;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.angels.world.R;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.backends.android.AndroidGraphics;
import com.mygdx.game.MyGdxGame;
import com.umeng.analytics.MobclickAgent;
import com.badlogic.gdx.backends.android.AndroidApplication;
public class GameLauncherActivity extends AndroidApplication {

    FrameLayout flContent;
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        initialize(new MyGdxGame(), config);
//

//		initialize(new MyGdxGame(), config);

//		setContentView(((AndroidGraphics)getGraphics()).getView());
        setContentView(R.layout.activity_game_launcher);
        flContent = findViewById(R.id.fl_content);
        flContent.addView(((AndroidGraphics)getGraphics()).getView());
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
