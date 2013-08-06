package com.brashmonkey.spriter;

public class SpriterPoint {
	
	public float x,y;
	
	public SpriterPoint(float x, float y){
		this.set(x, y);
	}
	
	public void translate(float x, float y){
		this.x += x;
		this.y += y;
	}
	
	public void set(float x, float y){
		this.x = x;
		this.y = y;
	}
	
	public void rotate(float degree){
		double angle = Math.toRadians(degree);
		float cos = (float)Math.cos(angle);
		float sin = (float)Math.sin(angle);
		
		float xx = x*cos-y*sin;
		float yy = x*sin+y*cos;
		
		this.x = xx;
		this.y = yy;
	}

}
