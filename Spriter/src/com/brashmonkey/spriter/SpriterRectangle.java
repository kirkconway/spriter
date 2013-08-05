package com.brashmonkey.spriter;


public class SpriterRectangle {
	
	public float left, top, right, bottom, width, height;
	
	public SpriterRectangle(float left, float top, float right, float bottom){
		this.set(left, top, right, bottom);
		this.calculateSize();
	}
	
	public SpriterRectangle(SpriterRectangle rect){
		this.set(rect);
	}
	
	public boolean isInisde(float x, float y){
		return x >= this.left && x <= this.right && y <= this.top && y >= this.bottom; 
	}
	
	public void calculateSize(){
		this.width = right-left;
		this.height = top-bottom;
	}
	
	public void set(SpriterRectangle rect){
		if(rect == null) return;
		this.bottom = rect.bottom;
		this.left = rect.left;
		this.right = rect.right;
		this.top = rect.top;
		this.calculateSize();
	}
	
	public void set(float left, float top, float right, float bottom){
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
	}
	
	public static boolean areIntersecting(SpriterRectangle rect1, SpriterRectangle rect2){
		return rect1.isInisde(rect2.left, rect2.top) || rect1.isInisde(rect2.right, rect2.top)
				|| rect1.isInisde(rect2.left, rect2.bottom) || rect1.isInisde(rect2.right, rect2.bottom);
	}

}
