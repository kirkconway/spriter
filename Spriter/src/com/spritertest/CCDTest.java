package com.spritertest;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.brashmonkey.spriter.SpriterCalculator;

public class CCDTest implements ApplicationListener{

	OrthographicCamera cam;
	ShapeRenderer renderer;
	Bone[] bones = new Bone[20];
	private int resolvingSpeed = 1;
	
	@Override
	public void create() {
		this.cam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		this.renderer = new ShapeRenderer();
		for(int i = 0; i< this.bones.length; i++){
			this.bones[i] = new Bone();
			this.bones[i].angle = 30f;
			if(i != 0) this.bones[i].setParent(this.bones[i-1]);
		}
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor((float)100/255,(float)149/255,(float)237/255, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		this.cam.update();
		
		Vector3 mouse = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0f);
		cam.unproject(mouse);
		this.resolve( mouse.x, mouse.y, this.bones.length-1, this.bones[this.bones.length-1]);
		
		this.renderer.setProjectionMatrix(this.cam.combined);
		this.renderer.begin(ShapeRenderer.ShapeType.Line);
		for(int i = 0; i < this.bones.length; i++){
			this.bones[i].update();
			this.bones[i].draw(this.renderer);
		}
		this.renderer.end();
		this.renderer.begin(ShapeRenderer.ShapeType.Rectangle);
		this.renderer.rect(mouse.x, mouse.y, 5, 5);
		this.renderer.end();
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
	}
	
	private void resolve(float x, float y, int maxLength, Bone effector){
		float xx = effector.x+(float)Math.cos(Math.toRadians(effector.angleDraw))*effector.length,
				yy = effector.y+(float)Math.sin(Math.toRadians(effector.angleDraw))*effector.length;
		effector.angle =  SpriterCalculator.angleBetween(effector.x, effector.y, x, y);
		if(effector.parent != null) effector.angle -= effector.parent.angleDraw;
		Bone parent = effector.parent;
		for(int i = 0; i < maxLength && parent != null; i++){
			parent.angle += Math.min(SpriterCalculator.angleDifference(SpriterCalculator.angleBetween(parent.x, parent.y, x, y),
					SpriterCalculator.angleBetween(parent.x, parent.y, xx, yy)), resolvingSpeed);
			parent.updateRecursively();
			parent = parent.parent;
			xx = effector.x+(float)Math.cos(Math.toRadians(effector.angleDraw))*effector.length;
			yy = effector.y+(float)Math.sin(Math.toRadians(effector.angleDraw))*effector.length;
		}
	}
	
	private class Bone{
		float length = 20, x,y, angle = 0f, angleDraw;
		Bone parent = null;
		List<Bone> children = new LinkedList<Bone>();
		
		public void update(){
			if(this.parent != null){
				x = parent.x+(float)Math.cos(Math.toRadians(parent.angleDraw))*parent.length;
				y = parent.y+(float)Math.sin(Math.toRadians(parent.angleDraw))*parent.length;
				this.angleDraw = parent.angleDraw+this.angle;
			}
			else this.angleDraw = this.angle;
		}
		
		public void updateRecursively(){
			this.update();
			for(Bone child: this.children)
				child.updateRecursively();
		}
		
		public void draw(ShapeRenderer renderer){
			renderer.line(x, y, x+(float)Math.cos(Math.toRadians(angleDraw))*length,
					y+(float)Math.sin(Math.toRadians(angleDraw))*length);
		}
		
		public void setParent(Bone parent){
			this.parent = parent;
			if(parent != null) parent.children.add(this);
		}
	}

}
