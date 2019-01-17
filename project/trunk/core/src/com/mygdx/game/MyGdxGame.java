package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MyGdxGame extends ApplicationAdapter {
	// 纹理画布
	private SpriteBatch batch;
	// 纹理
	private Texture img;

	private int screemWidth;
	private int screemHeight;
	@Override
	public void create () {
		batch = new SpriteBatch();
//		System.out.print("========"+Gdx.files.internal("test.jpg").pathWithoutExtension());
//		img = new Texture("test.jpg");
		img = new Texture(Gdx.files.internal("app/src/main/assets/test.jpg"));

		screemWidth = Gdx.app.getGraphics().getWidth();
		screemHeight = Gdx.app.getGraphics().getHeight();

	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(img, screemWidth-img.getWidth(), screemHeight-img.getHeight());
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
