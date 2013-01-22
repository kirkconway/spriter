package com.spriter;

public interface Interpolator {
	
	float interpolate(float a, float b, float timeA, float timeB, long currentTime);
	
	float interpolateAngle(float a, float b, float timeA, float timeB, long currentTime);
}
