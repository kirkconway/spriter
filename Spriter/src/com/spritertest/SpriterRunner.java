package com.spritertest;

import java.util.List;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.spriter.Spriter;
import com.spriter.SpriterKeyFrameProvider;
import com.spriter.objects.SpriterBone;
import com.spriter.objects.SpriterKeyFrame;
import com.spriter.objects.SpriterModObject;
import com.spriter.objects.SpriterObject;
import com.spriter.player.SpriterAbstractPlayer;
import com.spriter.player.SpriterPlayer;
import com.spriter.player.SpriterPlayerInterpolator;

public class SpriterRunner implements ApplicationListener{

	private OrthographicCamera camera;
	private SpriteBatch batch;
	private SpriterAbstractPlayer playerInt1, playerInt2, playerInt3;
	private SpriterLoader loader, loader2;
	private Spriter spriter;
	int count;
	float x =0;

	@Override
	public void create() {
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		
		camera = new OrthographicCamera(w,h);
		
		batch = new SpriteBatch();
		
		loader = new SpriterLoader();
		//loader2 = new SpriterLoader();
		
		spriter = GdxSpriter.getSpriter("data/monster/basic.scml", loader);
		//GdxSpriter.getSpriter("data/fn/fatman.scml", loader2);
		List<SpriterKeyFrame[]> keyframes = SpriterKeyFrameProvider.generateKeyFramePool(spriter.getSpriterData());
		
		SpriterDrawer drawer = new SpriterDrawer(loader, batch);
		
		final SpriterAbstractPlayer player1 = new SpriterPlayer(spriter.getSpriterData().getEntity().get(0), drawer, keyframes);
		final SpriterAbstractPlayer player2 = new SpriterPlayer(spriter.getSpriterData().getEntity().get(0), drawer, keyframes);
		final SpriterAbstractPlayer player3 = new SpriterPlayer(spriter.getSpriterData().getEntity().get(0), drawer, keyframes);
		
		((SpriterPlayer) player1).setAnimatioIndex(((SpriterPlayer)player2).getAnimationIndexByName("run"), 1, 1);
		((SpriterPlayer) player2).setAnimatioIndex(((SpriterPlayer)player2).getAnimationIndexByName("run"), 1, 1);
		((SpriterPlayer) player3).setAnimatioIndex(2, 1, 1);
		player1.setFrameSpeed(10);
		player1.update(0, 0);
		player1.setFrame(0);
		
		player2.setFrameSpeed(20);
		player3.setFrameSpeed(10);
		
		playerInt1 = player1;//new SpriterPlayerInterpolator(player1, player2);
		player1.setPivot(0, 0f);
		playerInt2 = new SpriterPlayerInterpolator(player2, player3);
		playerInt3 = new SpriterPlayerInterpolator(player3, playerInt2);
		SpriterBone head = player1.getBoneByName("torso");
		final SpriterObject headObject = player1.findObjectForBone(head, 0);
		final SpriterModObject obj = player1.findModObjectForBone(head, 0);
		final SpriterModObject headObj = player1.findModBoneForBone(head);
		//headObj.setAngle(90f);
		obj.setRef(loader.findReferenceByFileName("mon_torso/torso_0.png"));
		
		//playerInt3.changeRootParent(headObject);
		playerInt3.setZIndex(10);
		playerInt1.addPlayer(playerInt3, headObject);
		
		count = 0;
		
		Gdx.input.setInputProcessor(new InputProcessor(){

			@Override
			public boolean keyDown(int keycode) {
				/*if(keycode == Keys.PLUS) playerInt1.getSecond().setAnimatioIndex(Math.min(player2.getAnimationIndex()+1, player1.getEntity().getAnimation().size()-1), 1, 1);
				else if(keycode == Keys.MINUS) playerInt1.getSecond().setAnimatioIndex(Math.max(player2.getAnimationIndex()-1, 0), 1, 1);
				if(keycode == Keys.UP) playerInt1.getFirst().setAnimatioIndex(Math.min(player1.getAnimationIndex()+1, player1.getEntity().getAnimation().size()-1), 1, 1);
				else if(keycode == Keys.DOWN) playerInt1.getFirst().setAnimatioIndex(Math.max(player1.getAnimationIndex()-1, 0), 1, 1);*/
				if(keycode == Keys.PLUS){
					headObj.setScaleX(headObj.getScaleX()+0.5f);
					headObj.setScaleY(headObj.getScaleY()+0.5f);
				}
				if(keycode == Keys.MINUS){
					headObj.setScaleX(headObj.getScaleX()-0.5f);
					headObj.setScaleY(headObj.getScaleY()-0.5f);
				}
				return false;
			}

			@Override
			public boolean keyUp(int keycode) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean keyTyped(char character) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean touchDown(int screenX, int screenY, int pointer,
					int button) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean touchUp(int screenX, int screenY, int pointer,
					int button) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean touchDragged(int screenX, int screenY, int pointer) {
				Vector3 vec = new Vector3(screenX, screenY, 0);
				camera.unproject(vec);
				Vector2 v = new Vector2(vec.x,vec.y);
				Vector2 pos = new Vector2(headObject.getX(), headObject.getY());
				headObj.setAngle(v.sub(pos).angle());
				return false;
			}

			@Override
			public boolean mouseMoved(int screenX, int screenY) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean scrolled(int amount) {
				count = (loader.getRefs().length+count+amount) %loader.getRefs().length; 
				/*count += amount;
				count = Math.max(count, 0);
				count = Math.min(count, loader.getRefs().length-1);*/
				obj.setRef(loader.getRefs()[count]);
				return false;
			}
			
		});
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor((float)100/255,(float)149/255,(float)237/255, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		this.camera.update();
		this.batch.setProjectionMatrix(camera.combined);
		playerInt3.update(0, 0f);
		playerInt1.update(x, 0f);
		//playerInt1.setAngle((float)Math.sin(Math.toRadians((System.currentTimeMillis()/50)*Math.PI))*360);
		this.batch.begin();
			playerInt1.draw();
			//playerInt3.draw();
		this.batch.end();
		
		if(Gdx.input.isKeyPressed(Keys.RIGHT)){
			if(playerInt1.getFlipX() == -1){
				playerInt1.flipX();
				playerInt3.flipX();
			}
			x+=10f;//((SpriterPlayerInterpolator) playerInt1).setWeight(Math.min(((SpriterPlayerInterpolator) playerInt1).getWeight()+0.025f,1f));
		}
		if(Gdx.input.isKeyPressed(Keys.LEFT)){
			if(playerInt1.getFlipX() == 1){
				playerInt1.flipX();
				playerInt3.flipX();
			}
			x-=10f;//((SpriterPlayerInterpolator) playerInt1).setWeight(Math.max(((SpriterPlayerInterpolator) playerInt1).getWeight()-0.025f,0f));
		}
		/*if(Gdx.input.isKeyPressed(Keys.UP)) playerInt1.setScale(playerInt1.getScale()+0.05f);
		if(Gdx.input.isKeyPressed(Keys.DOWN)) playerInt1.setScale(playerInt1.getScale()-0.05f);*/
		/*if(Gdx.input.isKeyPressed(Keys.UP)) playerInt2.setWeight(Math.min(playerInt2.getWeight()+0.025f,1f));
		if(Gdx.input.isKeyPressed(Keys.DOWN)) playerInt2.setWeight(Math.max(playerInt2.getWeight()-0.025f,0f));
		if(Gdx.input.isKeyPressed(Keys.PLUS)) playerInt3.setWeight(Math.min(playerInt3.getWeight()+0.025f,1f));
		if(Gdx.input.isKeyPressed(Keys.MINUS)) playerInt3.setWeight(Math.max(playerInt3.getWeight()-0.025f,0f));*/
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		batch.dispose();
		loader.dispose();
		if(loader2 != null) loader2.dispose();
	}

}