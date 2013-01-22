package com.spriter;

public class LinearInterpolator implements Interpolator {

	public float interpolate(float a, float b, float timeA, float timeB, long currentTime) {
		return SpriterCalculator.calculateInterpolation(a, b, timeA, timeB, currentTime);
	}

	public float interpolateAngle(float a, float b, float timeA, float timeB, long currentTime) {
		return SpriterCalculator.calculateAngleInterpolation(a, b, timeA, timeB, currentTime);
	}

}
