package com.discobeard.spriter.draw;

import com.discobeard.spriter.file.Reference;

public class DrawInstruction {

	private final Reference ref;
	private final float x;
	private final float y;
	private final float pivot_x;
	private final float pivot_y;
	private final float angle;
	private final float alpha;
	private final float scale_x;
	private final float scale_y;
	
	public DrawInstruction(Reference ref, float x, float y, float pivot_x,
			float pivot_y,float scale_x, float scale_y, float angle,float alpha) {
		this.ref = ref;
		this.x = x;
		this.y = y;
		this.pivot_x = pivot_x;
		this.pivot_y = pivot_y;
		this.angle = angle;
		this.alpha = alpha;
		this.scale_x=scale_x;
		this.scale_y=scale_y;
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
		return pivot_x;
	}

	public float getPivot_y() {
		return pivot_y;
	}

	public float getAngle() {
		return angle;
	}

	public float getAlpha() {
		return alpha;
	}

	public float getScale_x() {
		return scale_x;
	}

	public float getScale_y() {
		return scale_y;
	}

}
