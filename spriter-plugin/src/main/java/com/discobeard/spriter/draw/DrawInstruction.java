package com.discobeard.spriter.draw;

import com.discobeard.spriter.file.Reference;

public class DrawInstruction {

	public Reference ref;
	public float x;
	public float y;
	public float pivotX;
	public float pivotY;
	public float angle;
	public float alpha;
	public float scaleX;
	public float scaleY;
	
	public DrawInstruction(Reference ref, float x, float y, float pivotX,
			float pivotY,float scaleX, float scaleY, float angle,float alpha) {
		this.ref = ref;
		this.x = x;
		this.y = y;
		this.pivotX = pivotX;
		this.pivotY = pivotY;
		this.angle = angle;
		this.alpha = alpha;
		this.scaleX=scaleX;
		this.scaleY=scaleY;
	}

	public Reference getRef() {
		return ref;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getPivot_x() {
		return pivotX;
	}

	public float getPivot_y() {
		return pivotY;
	}

	public float getAngle() {
		return angle;
	}

	public float getAlpha() {
		return alpha;
	}

	public float getScale_x() {
		return scaleX;
	}

	public float getScale_y() {
		return scaleY;
	}

}
