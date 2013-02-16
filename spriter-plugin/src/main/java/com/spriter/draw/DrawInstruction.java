package com.spriter.draw;

import com.spriter.file.FileLoader;
import com.spriter.file.Reference;
import com.spriter.objects.SpriterObject;

@SuppressWarnings("rawtypes")
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
	public SpriterObject obj = null;
	public FileLoader loader = null;
	
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

	/**
	 * @return the ref
	 */
	public Reference getRef() {
		return ref;
	}

	/**
	 * @return the x
	 */
	public float getX() {
		return x;
	}

	/**
	 * @return the y
	 */
	public float getY() {
		return y;
	}

	/**
	 * @return the pivotX
	 */
	public float getPivotX() {
		return pivotX;
	}

	/**
	 * @return the pivotY
	 */
	public float getPivotY() {
		return pivotY;
	}

	/**
	 * @return the angle
	 */
	public float getAngle() {
		return angle;
	}

	/**
	 * @return the alpha
	 */
	public float getAlpha() {
		return alpha;
	}

	/**
	 * @return the scaleX
	 */
	public float getScaleX() {
		return scaleX;
	}

	/**
	 * @return the scaleY
	 */
	public float getScaleY() {
		return scaleY;
	}

	/**
	 * @return the obj
	 */
	public SpriterObject getObj() {
		return obj;
	}

}
