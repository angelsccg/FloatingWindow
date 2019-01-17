package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.MyGdxGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 720;			// 窗口宽度
		config.height = 480;		// 窗口高度
		config.resizable = true;	// 窗口设置为大小不可改变
		new LwjglApplication(new MyGdxGame(), config);
	}
}
