package com.discobeard.spriter;

import com.discobeard.spriter.objects.SpriterBone;

public class SpriterCalculator {
	
	/**
	 * Calculates interpolated value for positions and scale.
	 * @param a first value
	 * @param b second value
	 * @param timeA first time
	 * @param timeB second time
	 * @param currentTime
	 * @return interpolated value between a and b.
	 */
	public static float calculateInterpolation(float a, float b, float timeA, float timeB, long currentTime) {
		return a + ((b - a) * ((currentTime - timeA) / (timeB - timeA)));
	}

	/**
	 * Calculates interpolated value for angles.
	 * @param a first angle
	 * @param b second angle
	 * @param timeA first time
	 * @param timeB second time
	 * @param currentTime
	 * @return interpolated angle
	 */
	public static float calculateAngleInterpolation(float a, float b, float timeA, float timeB, long currentTime) {
		return a + (angleDifference(b, a) * ((currentTime - timeA) / (timeB - timeA)));
	}
	
	/**
	 * Calculates the smallest difference between angle a and b.
	 * @param a first angle (in degrees)
	 * @param b second angle (in degrees)
	 * @return Smallest difference between a and b (between 180 and -180).
	 */
	public static float angleDifference(float a, float b){
		return ((((a - b) % 360) + 540) % 360) - 180;
	}
	
	/**
	 * Rotates the given point around the given parent.
	 * @param parent
	 * @param childX
	 * @param childY
	 * @return float array with two elemts, new x and y coordinates.
	 */
	public static float[] rotatePoint(SpriterBone parent, float childX, float childY) {

		float px = childX * (parent.getScaleX());
		float py = childY * (parent.getScaleY());

		float s = (float) Math.sin(Math.toRadians(parent.getAngle()));
		float c = (float) Math.cos(Math.toRadians(parent.getAngle()));
		float xnew = (px * c) - (py * s);
		float ynew = (px * s) + (py * c);
		xnew += parent.getX();
		ynew += parent.getY();

		return new float[] { xnew, ynew };
	}
}
