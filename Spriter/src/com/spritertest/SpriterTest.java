package com.spritertest;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import com.discobeard.spriter.Spriter;
import com.discobeard.spriter.SpriterPlayer;
import com.discobeard.spriter.file.FileLoader;

public class SpriterTest implements ApplicationListener, InputProcessor {
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Spriter spriter;
	private SpriterPlayer sp;
	private int animationIndex;
	private float x=0f,y=0f,vspeed=0f;
	private int runIndex, idleIndex, jumpIndex, fallIndex;
	private float hspeed;
	private int head;
	
	@Override
	public void create() {
		//System.out.println(System.getProperty("user.dir"));
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		
		camera = new OrthographicCamera(w,h);
		
		batch = new SpriteBatch();
		
		FileLoader<Sprite> loader = new SpriteLoader();
		SpriteDrawer drawer = new SpriteDrawer(loader,batch);
		
		Gdx.input.setInputProcessor(this);
		
		spriter = Spriter.getSpriter("monster/basic.scml", drawer, loader);
		
		this.sp = new SpriterPlayer(spriter.getSpriterData(), drawer);
		this.idleIndex = this.sp.getAnimationIndexByName("boned");
		this.runIndex = this.sp.getAnimationIndexByName("idle");
		this.jumpIndex = this.sp.getAnimationIndexByName("posture");
		this.fallIndex = this.sp.getAnimationIndexByName("fall");
		this.sp.setFrameSpeed(10);
		this.sp.setAnimatioIndex(this.idleIndex);
		this.sp.setPivot(0f, 0f);
	}

	@Override
	public void dispose() {
		batch.dispose();
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor((float)100/255,(float)149/255,(float)237/255, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		batch.setProjectionMatrix(camera.combined);
		
		sp.update(x, y);
		
		this.y += vspeed;
		this.x += hspeed;
		if(vspeed > 0) animationIndex=jumpIndex;
		else if(vspeed < 0 && y > 0){
			animationIndex=fallIndex;
			this.sp.setFrameSpeed(25);
		}
		else if(this.animationIndex != this.runIndex){
			this.animationIndex = this.idleIndex;
			this.sp.setFrameSpeed(15);
		}
		if(this.hspeed != 0 && vspeed == 0){
			this.animationIndex = this.runIndex;
			this.sp.setFrameSpeed(15);
		}
		this.sp.setAnimatioIndex(this.animationIndex);
		vspeed -= 0.5f;
		if(y < 0){
			vspeed = 0;
			this.animationIndex = this.idleIndex;
		}
		
		batch.begin();
		
		sp.draw();

		batch.end();
		head = 3;//this.sp.getBoneIndexByName("arm");
		//System.out.println(head);
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

	@Override
	public boolean keyDown(int keycode) {
		if(keycode == Keys.UP)
			this.sp.flipY();
		if(keycode == Keys.LEFT){
			this.hspeed = -10f;
			this.animationIndex = this.runIndex;
			if(this.sp.getFlipX() == 1) this.sp.flipX();
			return true;
		}
		else if(keycode == Keys.RIGHT){
			this.hspeed = 10f;
			this.animationIndex = this.runIndex;
			if(this.sp.getFlipX() == -1) this.sp.flipX();
			return true;
		}
		if(keycode == Keys.A){
			this.vspeed = 30;
			this.sp.setFrameSpeed(25);
			return true;
		}
		if(keycode == Keys.PLUS){
			this.sp.setScale(this.sp.getScale()+0.1f);
		}
		if(keycode == Keys.MINUS){
			this.sp.setScale(this.sp.getScale()-0.1f);
		}
		
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		if(keycode == Keys.LEFT || keycode == Keys.RIGHT){
			this.animationIndex = this.idleIndex;
			this.hspeed = 0f;
			this.sp.setFrameSpeed(10);
		}
		
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		//this.sp.setAngle(-new Vector2(screenX-Gdx.graphics.getWidth()/2-x,screenY-Gdx.graphics.getHeight()/2-y).angle()*this.sp.getFlipX()*this.sp.getFlipY());
		this.sp.setBoneAngle(this.head, -new Vector2(screenX-Gdx.graphics.getWidth()/2-x,screenY-Gdx.graphics.getHeight()/2-y).angle());
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		float zoom = 0.1f;
		if(Gdx.input.isKeyPressed(Keys.SHIFT_LEFT))
			zoom *= 10f;
		this.camera.zoom += amount*zoom;
		this.camera.update();
		return true;
	}
}
