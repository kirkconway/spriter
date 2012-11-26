package com.spritertest;

import java.util.ArrayList;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import com.discobeard.spriter.Spriter;
import com.discobeard.spriter.SpriterCalculator;
import com.discobeard.spriter.SpriterKeyFrameProvider;
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
	private boolean rotateBack = true;
	private ArrayList<SpriterPlayer> players;
	
	@Override
	public void create() {
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		
		camera = new OrthographicCamera(w,h);
		
		batch = new SpriteBatch();
		
		FileLoader<Sprite> loader = new SpriteLoader();
		SpriteDrawer drawer = new SpriteDrawer(loader,batch);
		
		Gdx.input.setInputProcessor(this);
		
		spriter = Spriter.getSpriter("data/monster/basic.scml", drawer, loader);
		this.players = new ArrayList<SpriterPlayer>();
		for(int i = 0; i < 100; i++){
			SpriterPlayer sp = new SpriterPlayer(spriter.getSpriterData(), drawer, SpriterKeyFrameProvider.generateKeyFramePool(spriter.getSpriterData()));
			this.players.add(sp);
			sp.setFrameSpeed(10);
		}
		x = (-this.players.size()/2)*400;
		this.sp = this.players.get(0);
		this.idleIndex = this.sp.getAnimationIndexByName("idle");
		this.runIndex = this.sp.getAnimationIndexByName("run");
		this.jumpIndex = this.sp.getAnimationIndexByName("jump");
		this.fallIndex = this.sp.getAnimationIndexByName("fall");
		
		this.sp.setFrameSpeed(10);
		this.sp.setAnimatioIndex(this.idleIndex);
		this.sp.setPivot(0f, 0f);
		this.sp.update(x, y);
		this.head = this.sp.getBoneIndexByName("torso");
		int torso = this.sp.getBoneIndexByName("leftLowerLeg");
		this.sp.setBoneScaleX(torso, 1f);
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
		
		for(int i = 0; i < this.players.size(); i++)
			this.players.get(i).update(x+400*i, y);
		
		this.y += vspeed;
		this.x += hspeed;
		if(vspeed > 0) animationIndex=jumpIndex;
		else if(vspeed < 0 && y > 0){
			animationIndex=fallIndex;
			for(SpriterPlayer sp: this.players)
				sp.setFrameSpeed(25);
		}
		else if(this.animationIndex != this.runIndex){
			this.animationIndex = this.idleIndex;
			for(SpriterPlayer sp: this.players)
				sp.setFrameSpeed(15);
		}
		if(this.hspeed != 0 && vspeed == 0){
			this.animationIndex = this.runIndex;
			for(SpriterPlayer sp: this.players)
				sp.setFrameSpeed(15);
		}
		for(SpriterPlayer sp: this.players)
			sp.setAnimatioIndex(this.animationIndex);
		vspeed -= 0.5f;
		if(y < 0){
			vspeed = 0;
			this.animationIndex = this.idleIndex;
		}
		if(this.rotateBack){
			float diff;
			for(SpriterPlayer sp: this.players){
				diff = SpriterCalculator.angleDifference(0, sp.getBoneAngle(this.head));
				sp.setBoneAngle(this.head, sp.getBoneAngle(this.head)+Math.signum(diff)*Math.min(Math.abs(diff), 10f));
			}
		}
		else this.roatateToMouse(Gdx.input.getX(), Gdx.input.getY());
		
		batch.begin();
		for(SpriterPlayer sp: this.players)
			sp.draw();

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

	@Override
	public boolean keyDown(int keycode) {
		if(keycode == Keys.ESCAPE)
			Gdx.app.exit();
		if(keycode == Keys.UP)
			for(SpriterPlayer sp: this.players)
				sp.flipY();
		if(keycode == Keys.LEFT){
			this.hspeed = -10f;
			this.animationIndex = this.runIndex;
			for(SpriterPlayer sp: this.players)
				if(sp.getFlipX() == 1) sp.flipX();
			return true;
		}
		else if(keycode == Keys.RIGHT){
			this.hspeed = 10f;
			this.animationIndex = this.runIndex;
			for(SpriterPlayer sp: this.players)
				if(sp.getFlipX() == -1) sp.flipX();
			return true;
		}
		if(keycode == Keys.A && (this.animationIndex == this.idleIndex || this.animationIndex == this.runIndex)){
			this.vspeed = 30;
			for(SpriterPlayer sp: this.players)
				sp.setFrameSpeed(25);
			return true;
		}
		if(keycode == Keys.PLUS){
			for(SpriterPlayer sp: this.players)
				sp.setScale(this.sp.getScale()+0.1f);
		}
		if(keycode == Keys.MINUS){
			for(SpriterPlayer sp: this.players)
				sp.setScale(sp.getScale()-0.1f);
		}
		
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		if(keycode == Keys.LEFT || keycode == Keys.RIGHT){
			this.animationIndex = this.idleIndex;
			this.hspeed = 0f;
			for(SpriterPlayer sp: this.players)
				sp.setFrameSpeed(10);
		}
		
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		this.rotateBack = false;
		this.roatateToMouse(screenX, screenY);
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		this.rotateBack = true;
		return true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}
	
	private void roatateToMouse(int screenX, int screenY){
		Vector3 vec = new Vector3();
		this.camera.unproject(vec.set(screenX, screenY, 0));//Translate mouse coordinates to world
		for(SpriterPlayer sp: this.players){
			Vector2 v = new Vector2(vec.x,vec.y);
			Vector2 p = new Vector2(sp.getBoneX(this.head), sp.getBoneY(this.head));
			v.sub(p);
			sp.setBoneAngle(this.head,(sp.getFlipX() == 1) ? v.angle()*sp.getFlipY() : -(v.angle()*sp.getFlipY())+180);
		}
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
