/**************************************************************************
 * Copyright 2013 by Trixt0r
 * (https://github.com/Trixt0r, Heinrich Reich, e-mail: trixter16@web.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
***************************************************************************/

package com.brashmonkey.spriter;

import com.brashmonkey.spriter.objects.SpriterAbstractObject;

/**
 * A class which provides methods to calculate Spriter specific issues,
 * like linear interpolation and rotation around a parent object.
 * Other interpolation types are coming with the next releases of Spriter.
 * 
 * @author Trixt0r
 *
 */

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
	public static float calculateInterpolation(float a, float b, float timeA, float timeB, float currentTime) {
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
	public static float calculateAngleInterpolation(float a, float b, float timeA, float timeB, float currentTime) {
		return a + (angleDifference(b, a) * ((currentTime - timeA) / (timeB - timeA)));
	}
	
	/**
	 * Calculates the smallest difference between angle a and b.
	 * @param a first angle (in degrees)
	 * @param b second angle (in degrees)
	 * @return Smallest difference between a and b (between 180° and -180°).
	 */
	public static float angleDifference(float a, float b){
		return ((((a - b) % 360) + 540) % 360) - 180;
	}
	
	/**
	 * Rotates the given child around the given parent.
	 * @param parent
	 * @param child
	 */
	public static void translateRelative(SpriterAbstractObject parent, SpriterAbstractObject child) {
		translateRelative(parent, child.getX(), child.getY(), child);
	}
	
	/**
	 * Rotates the given point around the given parent.
	 * @param parent
	 * @param x
	 * @param y
	 * @param target save new position in
	 */
	public static void translateRelative(SpriterAbstractObject parent, float x, float y, SpriterAbstractObject target) {

		float px = x * (parent.getScaleX());
		float py = y * (parent.getScaleY());

		float s = (float) Math.sin(Math.toRadians(parent.getAngle()));
		float c = (float) Math.cos(Math.toRadians(parent.getAngle()));
		float xnew = (px * c) - (py * s);
		float ynew = (px * s) + (py * c);
		xnew += parent.getX();
		ynew += parent.getY();
		
		target.setX(xnew);
		target.setY(ynew);
	}
	
	public static void reTranslateRelative(SpriterAbstractObject parent, SpriterAbstractObject child){
		reTranslateRelative(parent, child.getX(), child.getY(), child);
	}
	
	public static void reTranslateRelative(SpriterAbstractObject parent, float x, float y, SpriterAbstractObject target){
		target.setAngle(target.getAngle()-parent.getAngle());
		target.setScaleX(target.getScaleX()/parent.getScaleX());
		target.setScaleY(target.getScaleY()/parent.getScaleY());
		float xx = x - parent.getX(), yy = y - parent.getY();
		double angle = Math.toRadians(parent.getAngle()); 
		float cos = (float) Math.cos(angle);
		float sin = (float) Math.sin(angle);
		float newX = yy * sin + xx * cos;
		float newY = yy * cos - xx * sin;
		target.setX(newX/parent.getScaleX()); target.setY(newY/parent.getScaleY());
	}
	
	/**
	 * @param x1 x coordinate of first point.
	 * @param y1 y coordinate of first point.
	 * @param x2 x coordinate of second point.
	 * @param y2 y coordinate of second point.
	 * @return Angle between the two given points.
	 */
	public static float angleBetween(float x1, float y1, float x2, float y2){
	    return (float)Math.toDegrees(Math.atan2(y2-y1,x2-x1));
	}

	/**
	 * @param x1 x coordinate of first point.
	 * @param y1 y coordinate of first point.
	 * @param x2 x coordinate of second point.
	 * @param y2 y coordinate of second point.
	 * @return Distance between the two given points.
	 */
	public static float distanceBetween(float x1, float y1, float x2, float y2){
	    return (float)Math.sqrt((x2-x1)*(x2-x1)+(y2-y1)*(y2-y1));
	}
}
