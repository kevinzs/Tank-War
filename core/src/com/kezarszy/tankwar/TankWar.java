package com.kezarszy.tankwar;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TankWar extends ApplicationAdapter {

	public static final int WIDTH = 16*60, HEIGHT = 9*60;

	SpriteBatch batch;
	Game game;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		game = new Game();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		game.update();
		game.draw(batch);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
