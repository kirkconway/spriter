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

package com.spriter.objects;

import com.spriter.draw.DrawInstruction;
import com.spriter.file.FileLoader;
import com.spriter.file.Reference;

/**
 * A SpriterObject is an object which holds the transformations for an object which was animated in the Spriter editor.
 * It also holds information about things which will be drawn on the screen, such as sprite, depth and transparency.
 * @author Trixt0r
 */
@SuppressWarnings("rawtypes")
public class SpriterObject extends SpriterAbstractObject implements Comparable<SpriterObject>{
	
	float pivotX, pivotY, alpha;
	int zIndex, spin;
	boolean transientObject = false, visible = true;
	Reference ref;
	FileLoader loader = null;
	
	public void setReference(Reference ref){
		this.ref = ref;
	}
	public Reference getRef(){
		return this.ref;
	}
	public float getPivotX() {
		return pivotX;
	}
	public void setPivotX(float pivotX) {
		this.pivotX = pivotX;
	}
	public float getPivotY() {
		return pivotY;
	}
	public void setPivotY(float pivotY) {
		this.pivotY = pivotY;
	}
	public int getZIndex() {
		return zIndex;
	}
	public void setZIndex(int zIndex) {
		this.zIndex = zIndex;
	}
	public void setAngle(float angle) {
		this.angle = angle;
	}
	public int getSpin() {
		return spin;
	}
	public void setSpin(int spin) {
		this.spin = spin;
	}

	public float getAlpha() {
		return alpha;
	}

	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}

	public boolean isTransientObject() {
		return transientObject;
	}

	public void setTransientObject(boolean transientObject) {
		this.transientObject = transientObject;
	}
	
	/**
	 * Compares the z_index of the given SpriterObject with this.
	 * @param o SpriterObject to compare with.
	 */
	public int compareTo(SpriterObject o) {
		if(this.zIndex < o.zIndex) return -1;
		else if(this.zIndex > o.zIndex) return 1;
		else return 0;
	}
	
	public void setLoader(FileLoader loader){
		this.loader = loader;
	}
	
	public FileLoader getLoader(){
		return this.loader;
	}
	
	public boolean isVisible() {
		return visible;
	}
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	public String toString(){
		return "x: "+this.x+", y: "+this.y+", angle: "+this.alpha;
	}
	
	public void copyValuesTo(SpriterObject object){
		super.copyValuesTo(object);
		object.setAlpha(alpha);
		object.setReference(ref);
		object.setPivotX(pivotX);
		object.setPivotY(pivotY);
		object.setSpin(spin);
		object.setTimeline(timeline);
		object.setTransientObject(transientObject);
		object.setZIndex(zIndex);
		object.setLoader(loader);
		object.setVisible(visible);
	}
	
	public void copyValuesTo(DrawInstruction instruction){
		instruction.x =  this.x;
		instruction.y =  this.y;
		instruction.scaleX =  this.scaleX;
		instruction.scaleY =  this.scaleY;
		instruction.pivotX =  this.pivotX;
		instruction.pivotY = this.pivotY;
		instruction.angle = this.angle;
		instruction.alpha = this.alpha;
		instruction.ref = this.ref;
		instruction.loader = this.loader;
		instruction.obj = this;
	}
	
}
