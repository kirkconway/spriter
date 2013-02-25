package com.spritertest;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JFileChooser;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class BezierCurveTest implements ApplicationListener, InputProcessor{
	
	private OrthographicCamera camera;
	private SpriteBatch batch;
	BezierCurveDrawer bz;
	Vector2[] points;
	int count;
	
	private void reset(){
		points = new Vector2[4];
		count = 0;
		Vector2 t = new Vector2(this.camera.position.x,this.camera.position.y);
		for(int i = 0; i< this.points.length; i++)
			points[i] = t;
		this.bz = new BezierCurveDrawer(1,30,points);
	}
	
	@Override
	public void create() {
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		
		camera = new OrthographicCamera(w,h);
		
		batch = new SpriteBatch();
		this.reset();
		
		Gdx.input.setInputProcessor(this);
	}
	
	private void save(){
		String wd = System.getProperty("user.dir");
		JFileChooser fc = new JFileChooser(wd);
		int rc = fc.showDialog(null, "Select Data File");
		if (rc == JFileChooser.APPROVE_OPTION)
			{
			File file = fc.getSelectedFile();
			String filename = file.getAbsolutePath();
			try {
				
				FileWriter outFile = new FileWriter(filename);
				PrintWriter out = new PrintWriter(outFile);
				out.println("points = new Vector2f["+this.points.length+"];");
				for(int i = 0; i < this.points.length; i++)
					out.println("points["+i+"] = new Vector2f("+(this.points[i].x-this.camera.position.x)/100f+"f,"+(this.points[i].y-this.camera.position.y)/100f+"f);");
				
				out.close();
			  } catch (IOException e){
				   e.printStackTrace();
			  }
		}
		else
			System.out.println("File chooser cancel button clicked");
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor((float)100/255,(float)149/255,(float)237/255, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		batch.setProjectionMatrix(camera.combined);
		this.bz.shapeRenderer().setProjectionMatrix(camera.combined);
		
		this.bz.sR.begin(ShapeType.Line);
		this.bz.sR.line(this.camera.position.x, this.camera.position.y+Gdx.graphics.getHeight()/2, this.camera.position.x, this.camera.position.y-Gdx.graphics.getHeight()/2);
		this.bz.sR.line(this.camera.position.x+Gdx.graphics.getWidth()/2, this.camera.position.y, this.camera.position.x-Gdx.graphics.getWidth()/2, this.camera.position.y);
		this.bz.sR.end();
		
		this.bz.draw();
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		this.bz.dispose();
	}

	@Override
	public boolean keyDown(int keycode) {
		if(Gdx.input.isKeyPressed(Keys.CONTROL_LEFT)){
			if(keycode == Keys.S){
				this.save();
				return true;
			}
			else if(keycode == Keys.R){
				this.reset();
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		Vector3 vec = new Vector3(screenX, screenY, 0);
		this.camera.unproject(vec);//Translate mouse coordinates to world
		if(Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)){
			Vector2 n = new Vector2(vec.x,vec.y);
			if(++this.count > this.points.length){
				Vector2[] temp = new Vector2[this.points.length+3];
				System.arraycopy(this.points, 0, temp, 0, this.points.length);
				this.points = temp;
				this.bz.segments++;
			}
			for(int i = this.count-1; i < points.length; i++) points[i] = n;
			this.bz.controlPoints = this.points;
			return true;
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		Vector3 vec = new Vector3(screenX, screenY, 0);
		this.camera.unproject(vec);//Translate mouse coordinates to world
		Vector2 cam = new Vector2(vec.x,vec.y);
		if(Gdx.input.isKeyPressed(Keys.CONTROL_LEFT)){
			float length = 10000000000000f;
			Vector2 found = null;
			for(int i = 0; i < this.points.length; i++){
				Vector2 p = new Vector2(this.points[i]);
				Vector2 v = p.sub(cam);
				length = Math.min(length, v.len());
				if(length == v.len()) found = this.points[i];
			}
			found.x = cam.x;
			found.y = cam.y;
			return true;
		}
		else if(!Gdx.input.isKeyPressed(Keys.ANY_KEY)){
			this.camera.position.x -= Gdx.input.getDeltaX()*this.camera.zoom;
			this.camera.position.y += Gdx.input.getDeltaY()*this.camera.zoom;
			this.camera.update();
			return true;
		}
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

}
