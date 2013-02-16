package com.spriter.interpolation;

import com.spriter.SpriterCalculator;

public class SpriterLinearInterpolator implements SpriterInterpolator {

	public float interpolate(float a, float b, float timeA, float timeB, float currentTime) {
		return SpriterCalculator.calculateInterpolation(a, b, timeA, timeB, currentTime);
	}

	public float interpolateAngle(float a, float b, float timeA, float timeB, float currentTime) {
		return SpriterCalculator.calculateAngleInterpolation(a, b, timeA, timeB, currentTime);
	}
	
	public static final SpriterLinearInterpolator interpolator = new SpriterLinearInterpolator();

}
