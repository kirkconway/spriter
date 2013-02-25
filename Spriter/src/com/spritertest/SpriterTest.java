package com.spritertest;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.brashmonkey.spriter.Spriter;
import com.brashmonkey.spriter.SpriterCalculator;
import com.brashmonkey.spriter.SpriterKeyFrameProvider;
import com.brashmonkey.spriter.file.FileLoader;
import com.brashmonkey.spriter.objects.SpriterKeyFrame;
import com.brashmonkey.spriter.player.SpriterPlayer;



/**
 * Simple SpriterAnimation test.
 * Note: If you want to run the whole stuff on android, you have to use the GdxSpriter.getSpriter(String,FileLoader<?>) method.
 * Otherwise the application will crash because the way discobeard is reading xml files is not supported on android.
 * @author Trixt0r
 *
 */

public class SpriterTest implements ApplicationListener, InputProcessor {
	private OrthographicCamera camera;
	private SpriteBatch batch,bitmap;
	private Spriter spriter;
	private SpriterPlayer sp;
	private int animationIndex;
	private float x=0f,y=0f,vspeed=0f;
	private int runIndex, idleIndex, jumpIndex, fallIndex;
	private float hspeed;
	private int head;
	private boolean rotateBack = true;
	private ArrayList<SpriterPlayer> players;
	private BitmapFont bf;
	
	@Override
	public void create() {
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		
		camera = new OrthographicCamera(w,h);
		
		batch = new SpriteBatch();
		bitmap = new SpriteBatch();
		
		SpriterLoader loader = new SpriterLoader(true);
		SpriterDrawer drawer = new SpriterDrawer(loader,batch);
		
		//Gdx.input.setInputProcessor(this);

		
		spriter = Spriter.getSpriter("data/monster/basic.scml", loader);
		loader.generatePackedSprites();
		//List<SpriterKeyFrame[]> keyframes = SpriterKeyFrameProvider.generateKeyFramePool(spriter.getSpriterData());
		this.players = new ArrayList<SpriterPlayer>();
		int max = 422;
		for(int i = 0; i < max; i++){
			SpriterPlayer sp = new SpriterPlayer(spriter.getSpriterData().getEntity().get(0), drawer);
			this.players.add(sp);
			sp.setFrameSpeed(10);
		}
		x = (-(this.players.size()/10)/2)*400;
		y = 0;//((this.players.size()/10))*10;
		vspeed = 0;
		this.sp = this.players.get(0);
		this.idleIndex = this.sp.getAnimationIndexByName("idle");
		this.runIndex = this.sp.getAnimationIndexByName("run");
		this.jumpIndex = this.sp.getAnimationIndexByName("jump");
		this.fallIndex = this.sp.getAnimationIndexByName("fall");
		
		this.sp.setFrameSpeed(10);
		this.sp.setAnimatioIndex(this.idleIndex, 0, 0);
		this.sp.update(x, y);
		this.head = this.sp.getBoneIndexByName("head");
		
		this.bf = new BitmapFont();
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
		int k = 0;
		for(int i = 0; k < this.players.size(); i++){
			for(int j =0 ; j < 10 && k < this.players.size(); j++){
				this.players.get(k).update(x+400*j, y-i*400);
				k++;
			}
		}
		
		this.y += vspeed;
		this.x += hspeed;
		if(vspeed > 0) animationIndex=jumpIndex;
		else if(vspeed < 0 && y > 0){
			animationIndex=fallIndex;
			for(SpriterPlayer sp: this.players)
				sp.setFrameSpeed(25);
		}
		else if(this.animationIndex != this.runIndex && hspeed == 0 && vspeed == 0 && y == 0){
			this.animationIndex = this.idleIndex;
			for(SpriterPlayer sp: this.players)
				sp.setFrameSpeed(18);
		}
		if(this.hspeed != 0 && vspeed == 0 && y == 0){
			this.animationIndex = this.runIndex;
			for(SpriterPlayer sp: this.players)
				sp.setFrameSpeed(25);
		}
		for(SpriterPlayer sp: this.players)
			sp.setAnimatioIndex(this.animationIndex,10,120);
		vspeed -= 0.5f;
		if(y < 0){
			y = 0;
			vspeed = 0;
			this.animationIndex = this.idleIndex;
		}
		if(this.rotateBack){
			float diff;
			for(SpriterPlayer sp: this.players){
				diff = SpriterCalculator.angleDifference(0, sp.getModdedBones()[head].getAngle());
				sp.getModdedBones()[head].setAngle(sp.getModdedBones()[head].getAngle()+Math.signum(diff)*Math.min(Math.abs(diff), 10f));
			}
		}
		else this.roatateToMouse(Gdx.input.getX(), Gdx.input.getY());
		
		batch.begin();
		for(SpriterPlayer sp: this.players)
			sp.draw();
		batch.end();
		bitmap.begin();
		this.bf.draw(bitmap, ""+Gdx.graphics.getFramesPerSecond(), 50, 50);
		bitmap.end();
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
		this.rotateBack = Gdx.input.isKeyPressed(Keys.CONTROL_LEFT);
		//this.roatateToMouse(screenX, screenY);
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		this.rotateBack = true;
		return true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		if(Gdx.input.isKeyPressed(Keys.CONTROL_LEFT)){
			this.camera.position.x += Gdx.input.getDeltaX()*this.camera.zoom;
			this.camera.position.y -= Gdx.input.getDeltaY()*this.camera.zoom;
			this.camera.update();
			return true;
		}
		return false;
	}
	
	private void roatateToMouse(int screenX, int screenY){
		//Vector3 vec = new Vector3();
		//this.camera.unproject(vec.set(screenX, screenY, 0));//Translate mouse coordinates to world
		/*for(SpriterPlayer sp: this.players){
			/*Vector2 v = new Vector2(vec.x,vec.y);
			Vector2 p = new Vector2(sp.getRuntimeObjects()[sp.] sp.getBoneX(this.head), sp.getBoneY(this.head));
			v.sub(p);
			sp.setBoneAngle(this.head,(sp.getFlipX() == 1) ? v.angle()*sp.getFlipY() : -(v.angle()*sp.getFlipY())+180);
		}*/
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
