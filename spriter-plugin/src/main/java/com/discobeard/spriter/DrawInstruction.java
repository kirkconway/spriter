package com.discobeard.spriter;

public class DrawInstruction {

	private final Reference REF;
	private final float X;
	private final float Y;
	private final float PIVOT_X;
	private final float PIVOT_Y;
	private final float ANGLE;

	public DrawInstruction(Reference ref, float x, float y, float pivot_x,
			float pivot_y, float angle) {
		REF = ref;
		X = x;
		Y = y;
		PIVOT_X = pivot_x;
		PIVOT_Y = pivot_y;
		ANGLE = angle;
	}

	public Reference getREF() {
		return REF;
	}

	public float getX() {
		return X;
	}

	public float getY() {
		return Y;
	}

	public float getPIVOT_X() {
		return PIVOT_X;
	}

	public float getPIVOT_Y() {
		return PIVOT_Y;
	}

	public float getANGLE() {
		return ANGLE;
	}

}
