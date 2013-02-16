package com.spriter.interpolation;

public interface SpriterInterpolator {
	
	float interpolate(float a, float b, float timeA, float timeB, float currentTime);
	
	float interpolateAngle(float a, float b, float timeA, float timeB, float currentTime);
}
