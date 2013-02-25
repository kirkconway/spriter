package com.spritertest;


import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.brashmonkey.spriter.Spriter;
import com.brashmonkey.spriter.objects.*;
import com.brashmonkey.spriter.player.*;

public class SpriterRunner implements ApplicationListener{

	private OrthographicCamera camera;
	private SpriteBatch batch;
	private SpriterAbstractPlayer playerInt1, playerInt2, playerInt3;
	private SpriterLoader loader, loader2;
	private Spriter spriter;
	private ShapeRenderer sR;
	int count;
	float x =0;
	SpriterIKObject headTarget;
	SpriterBone head;
	private BitmapFont bf;
	private ArrayList<SpriterAbstractPlayer> players;
	private ArrayList<Vector2> positions;
	SpriterDrawer drawer;
	Random ran;
	private boolean addAble = true;

	@Override
	public void create() {
		ran = new Random();
		players = new ArrayList<SpriterAbstractPlayer>();
		positions = new ArrayList<Vector2>();
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		
		camera = new OrthographicCamera(w,h);
		
		batch = new SpriteBatch();
		sR = new ShapeRenderer();
		bf = new BitmapFont();
		
		loader = new SpriterLoader(true);
		//loader2 = new SpriterLoader();
		
		spriter = GdxSpriter.getSpriter("data/monster/basic.scml", loader);
		loader.generatePackedSprites();
		//GdxSpriter.getSpriter("data/fn/fatman.scml", loader2);
		//List<SpriterKeyFrame[]> keyframes = SpriterKeyFrameProvider.generateKeyFramePool(spriter.getSpriterData());
		
		drawer = new SpriterDrawer(loader, batch);
		this.addPlayer();
		/*for(int i = 0; i < 1; i++)
			this.players.add(new SpriterPlayer(spriter.getSpriterData().getEntity().get(0), drawer));*/
		final SpriterPlayer player1 = (SpriterPlayer) this.players.get(0);//new SpriterPlayer(spriter.getSpriterData().getEntity().get(0), drawer);
		player1.setAnimatioIndex(player1.getAnimationIndexByName("idle"), 0, 0);
		//final SpriterAbstractPlayer player2 = new SpriterPlayer(spriter.getSpriterData().getEntity().get(0), drawer);
		//final SpriterAbstractPlayer player3 = new SpriterPlayer(spriter.getSpriterData().getEntity().get(0), drawer, keyframes);
		
		//((SpriterPlayer) player1).setAnimatioIndex(((SpriterPlayer)player1).getAnimationIndexByName("Player_Walk"), 1, 1);
		//((SpriterPlayer) player2).setAnimatioIndex(((SpriterPlayer)player2).getAnimationIndexByName("run"), 1, 1);
		//((SpriterPlayer) player3).setAnimatioIndex(2, 1, 1);
		player1.setFrameSpeed(20);
		player1.update(0, 0);
		player1.setFrame(0);
		
		//player2.setFrameSpeed(20);
		//player3.setFrameSpeed(10);
		
		//playerInt1 = player1;//new SpriterPlayerInterpolator(player1, player2);
		//playerInt2 = playerInt new SpriterPlayerInterpolator(player2, player3);
		//playerInt3 = player2; //new SpriterPlayerInterpolator(player3, playerInt2);
		/*head = player1.getBoneByName("head");
		SpriterBone[] bones = new SpriterBone[]{player1.getBoneByName("hand_right")};
		headTarget = new SpriterIKObject(3,10);
		SpriterIKObject[] objs = new SpriterIKObject[]{headTarget, new SpriterIKObject(1,10,0,-100)};*/
		//player1.setBones(bones);
		//player1.setIKObjects(objs);
		//player1.deactivateEffectors(true);
		//player1.setResovling(true);
		
		//final SpriterObject headObject = player1.findObjectForBone(head, 0);
		//final SpriterModObject obj = player1.findModObjectForBone(head, 0);
		//final SpriterModObject headObj = player1.findModBoneForBone(head);
		//headObj.setAngle(90f);
		//obj.setRef(loader.findReferenceByFileName("mon_torso/torso_0.png"));
		
		//playerInt3.changeRootParent(headObject);
		//playerInt3.setZIndex(10);
		//playerInt1.attachPlayer(playerInt3, headObject);
		/*playerInt3.getRootParent().setScaleX(1/head.getScaleX());
		playerInt3.getRootParent().setScaleY(1/head.getScaleY());
		playerInt3.getRootParent().setAngle(-head.getAngle());*/
		/*player1.setBones(bones);
		player1.setIKObjects(objs);
		
		player1.setResovling(true);*/
		count = 0;
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void render() {
		addAble = Gdx.graphics.getFramesPerSecond() >= 60;
		Gdx.gl.glClearColor((float)100/255,(float)149/255,(float)237/255, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		this.camera.update();
		//playerInt3.update(0, 0f);
		for(int i = 0; i < this.players.size(); i++)
			this.players.get(i).update(this.positions.get(i).x, this.positions.get(i).y);
		//((SpriterPlayerIK) playerInt1).resolve();
		//playerInt1.setAngle((float)Math.sin(Math.toRadians((System.currentTimeMillis()/50)*Math.PI))*360);
		this.batch.setProjectionMatrix(camera.combined);
		this.batch.begin();
			for(SpriterAbstractPlayer player: this.players)
				player.draw();
			//playerInt3.draw();
			this.bf.draw(batch, "fps: "+Gdx.graphics.getFramesPerSecond()+" @"+this.players.size()+" instances ("+players.get(0).getRuntimeBones().length+" bones, "+players.get(0).getRuntimeObjects().length+" objects).", -camera.viewportWidth/2+10, camera.viewportHeight/2-20);
		this.batch.end();
		
		if(Gdx.input.isKeyPressed(Keys.RIGHT)){
			if(players.get(0).getFlipX() == -1){
				for(SpriterAbstractPlayer player: this.players)
					player.flipX();
			}
			x+=10f;//((SpriterPlayerInterpolator) playerInt1).setWeight(Math.min(((SpriterPlayerInterpolator) playerInt1).getWeight()+0.025f,1f));
		}
		if(Gdx.input.isKeyPressed(Keys.LEFT)){
			if(players.get(0).getFlipX() == 1){
				for(SpriterAbstractPlayer player: this.players)
					player.flipX();
			}
			x-=10f;//((SpriterPlayerInterpolator) playerInt1).setWeight(Math.max(((SpriterPlayerInterpolator) playerInt1).getWeight()-0.025f,0f));
		}
		if(Gdx.graphics.getFramesPerSecond() >= 60 && addAble) this.addPlayer();
		else addAble  = false;
		
		//this.debugDraw();
	}

	private void debugDraw() {
		this.sR.setProjectionMatrix(this.camera.combined);
		this.sR.begin(ShapeRenderer.ShapeType.Line);
		for(int i = 0; i < this.playerInt1.getRuntimeBones().length; i++){
			switch(i%8){
			case 0: this.sR.setColor(1f, 0f, 0f, 0.5f); break;
			case 1: this.sR.setColor(0f, 1f, 0f, 0.5f); break;
			case 2: this.sR.setColor(0f, 0f, 1f, 0.5f); break;
			case 3: this.sR.setColor(0f, 1f, 1f, 0.5f); break;
			case 4: this.sR.setColor(1f, 0f, 1f, 0.5f); break;
			case 5: this.sR.setColor(1f, 1f, 0f, 0.5f); break;
			case 6: this.sR.setColor(0f, 0f, 0f, 0.5f); break;
			case 7: this.sR.setColor(1f, 1f, 1f, 0.5f); break;
			}
			SpriterBone bone =  this.playerInt1.getRuntimeBones()[i];
			this.sR.line(bone.getX(), bone.getY(), bone.getX()+(float)Math.cos(Math.toRadians(bone.getAngle()))*200*bone.getScaleX(), bone.getY()+(float)Math.sin(Math.toRadians(bone.getAngle()))*200*bone.getScaleX());
		}
		this.sR.setColor(1f, 0.5f, 0.5f, 1f);
		this.sR.line(head.getX()+(float)Math.cos(Math.toRadians(head.getAngle()))*200*head.getScaleX(), head.getY()+(float)Math.sin(Math.toRadians(head.getAngle()))*200*head.getScaleX(), headTarget.getX(), headTarget.getY());
		SpriterAbstractObject o = playerInt1.getRuntimeBones()[head.getParentId()];
		for(int i = 0; i < headTarget.chainLength && o != null; i++){
			this.sR.line(head.getX()+(float)Math.cos(Math.toRadians(head.getAngle()))*200*head.getScaleX(), head.getY()+(float)Math.sin(Math.toRadians(head.getAngle()))*200*head.getScaleX(), o.getX(), o.getY());
			if(o.hasParent()) o = playerInt1.getRuntimeBones()[o.getParentId()];
			else o = null;
		}
		this.sR.end();
		this.sR.begin(ShapeRenderer.ShapeType.FilledCircle);
		for(int i = 0; i < this.playerInt1.getRuntimeBones().length; i++){
			switch(i%8){
			case 0: this.sR.setColor(1f, 0f, 0f, 0.5f); break;
			case 1: this.sR.setColor(0f, 1f, 0f, 0.5f); break;
			case 2: this.sR.setColor(0f, 0f, 1f, 0.5f); break;
			case 3: this.sR.setColor(0f, 1f, 1f, 0.5f); break;
			case 4: this.sR.setColor(1f, 0f, 1f, 0.5f); break;
			case 5: this.sR.setColor(1f, 1f, 0f, 0.5f); break;
			case 6: this.sR.setColor(0f, 0f, 0f, 0.5f); break;
			case 7: this.sR.setColor(1f, 1f, 1f, 0.5f); break;
			}
			SpriterBone bone =  this.playerInt1.getRuntimeBones()[i];
			this.sR.filledCircle(bone.getX(), bone.getY(), 5);
		}
		this.sR.end();
		this.sR.begin(ShapeRenderer.ShapeType.Circle);
		this.sR.circle(headTarget.getX(), headTarget.getY(), 5);
		this.sR.end();
		
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
	
	private void addPlayer(){
		SpriterPlayer player = new SpriterPlayer(spriter.getSpriterData().getEntity().get(0), drawer);
		int anim = ran.nextInt(spriter.getSpriterData().getEntity().get(0).getAnimation().size());
		System.out.println(anim);
		player.setAnimatioIndex(anim, 0, 0);
		player.setAngle(ran.nextFloat() * 360);
		player.setFrameSpeed(ran.nextInt(60));
		System.out.println("angle: "+player.getAngle());
		this.players.add(player);
		float x = camera.position.x+ran.nextInt((int) this.camera.viewportWidth/2)*((ran.nextBoolean()) ? 1:-1);
		float y = camera.position.y+ran.nextInt((int) this.camera.viewportHeight/2)*((ran.nextBoolean()) ? 1:-1);
		this.positions.add(new Vector2(x,y));
	}

}