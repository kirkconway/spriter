package com.discobeard.spriter.test;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.discobeard.spriter.Spriter;
import com.discobeard.spriter.file.FileLoader;

public class LibGDXTest implements ApplicationListener {
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Spriter spriter;
	
	@Override
	public void create() {		
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		
		camera = new OrthographicCamera(w,h);
		///camera.
		batch = new SpriteBatch();
		
		FileLoader<Sprite> loader = new SpriteLoader();
		SpriteDrawer drawer = new SpriteDrawer(loader,batch);
		
		spriter = Spriter.getSpriter("monster/basic.scml", drawer, loader);
		spriter.playAnimation(0, true);
	}

	@Override
	public void dispose() {
		batch.dispose();
	}

	@Override
	public void render() {		
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		batch.setProjectionMatrix(camera.combined);
		
		batch.begin();
		
		spriter.draw(0,-200);

		batch.end();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
