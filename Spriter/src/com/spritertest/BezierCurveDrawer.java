package com.spritertest;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;


public class BezierCurveDrawer implements Disposable{
	
	public int segments, evaluate;
	public Vector2[] controlPoints;
	public ShapeRenderer sR;
	
	public BezierCurveDrawer(int segments, int evaluate, Vector2[] controlPoints){
		this.segments = segments;
		this.evaluate = evaluate;
		this.controlPoints = controlPoints;//new Vector2[((this.segments-1) * 3) + 4];
		
		this.sR = new ShapeRenderer();
	}
	
	public void draw(){
		this.sR.begin(ShapeType.Line);
		this.sR.setColor(1f, 1f, 1f, 1f);
		for(int i = 0; i < segments; i++){
			double prevX=0,prevY=0;
			for(int j = 0; j <= this.evaluate; j++){
				double t = j / (double)this.evaluate;
				double x = this.controlPoints[i*3].x*this.B_0(t)+this.controlPoints[(i*3)+1].x*this.B_1(t)+this.controlPoints[(i*3)+2].x*this.B_2(t)+this.controlPoints[(i*3)+3].x*this.B_3(t);
				double y = this.controlPoints[i*3].y*this.B_0(t)+this.controlPoints[(i*3)+1].y*this.B_1(t)+this.controlPoints[(i*3)+2].y*this.B_2(t)+this.controlPoints[(i*3)+3].y*this.B_3(t);
				if(j > 0)	this.sR.line((float)x, (float)y, (float)prevX, (float)prevY);
				prevX = x;
				prevY = y;
				
			}
		}
		this.sR.end();
		this.sR.begin(ShapeType.Rectangle);
		this.sR.setColor(1f, 0f, 0f, 1f);
		for(int i = 0; i< this.controlPoints.length; i++)
			this.sR.rect(this.controlPoints[i].x-2.5f, this.controlPoints[i].y-2.5f, 5f,5f);
		this.sR.end();
	}

	@Override
	public void dispose() {
		this.sR.dispose();
	}
	
	private double B_0(double t){
		return -Math.pow(t, 3) + (3*Math.pow(t, 2))-3*t+1;
	}
	
	private double B_1(double t){
		return 3*Math.pow(t, 3) - (6*Math.pow(t, 2))+3*t;
	}
	
	private double B_2(double t){
		return -3*Math.pow(t, 3) + (3*Math.pow(t, 2));
	}
	
	private double B_3(double t){
		return Math.pow(t, 3);
	}
	
	public ShapeRenderer shapeRenderer(){
		return this.sR;
	}

}
