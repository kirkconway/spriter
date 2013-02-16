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

package com.spriter.draw;

import com.spriter.file.FileLoader;
import com.spriter.file.Reference;
import com.spriter.objects.SpriterObject;

/**
 * A DrawIntruction is an object which holds all information you need to draw the previous transformed objects.
 * @author Trixt0r
 */
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
