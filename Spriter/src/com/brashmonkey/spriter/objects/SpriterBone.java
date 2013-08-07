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

package com.brashmonkey.spriter.objects;

import java.util.LinkedList;
import java.util.List;

import com.brashmonkey.spriter.SpriterPoint;
import com.brashmonkey.spriter.SpriterCalculator;
import com.brashmonkey.spriter.SpriterRectangle;

/**
 * A SpriterBone is a bone like in the Spriter editor. It can hold children (#SpriterObject and #SpriterBone) which get manipulated relative to this object.
 * @author Trixt0r
 */
public class SpriterBone extends SpriterAbstractObject{
	
	List<SpriterBone> childBones;
	List<SpriterObject> childObjects;
	public SpriterRectangle boundingBox;
	
	public SpriterBone(){
		this.childBones = new LinkedList<SpriterBone>();
		this.childObjects = new LinkedList<SpriterObject>();
		this.boundingBox = new SpriterRectangle(0,0,0,0);
	}
	
	public void addChildBone(SpriterBone bone){
		bone.setParent(this);
		childBones.add(bone);
	}
	
	public List<SpriterBone> getChildBones(){
		return childBones;
	}
	
	public void addChildObject(SpriterObject object){
		object.setParent(this);
		childObjects.add(object);
	}
	
	public List<SpriterObject> getChildObjects(){
		return childObjects;
	}
	
	public void copyValuesTo(SpriterAbstractObject bone){
		super.copyValuesTo(bone);
		if(!(bone instanceof SpriterBone)) return;
		((SpriterBone)bone).childBones = this.childBones;
		((SpriterBone)bone).childObjects = this.childObjects;
	}
	
	public void update(){
		if(this.parent != null)
			SpriterCalculator.translateRelative(parent, this);
	}
	
	public void updateRecursively(){
		this.update();
		for(SpriterBone child: this.childBones)
			child.updateRecursively();
	}
	
	public void calcBoundingBox(SpriterRectangle base){
		this.boundingBox.set(base);
		for(SpriterObject object: this.childObjects){
			SpriterPoint[] points = object.getBoundingBox();
			this.boundingBox.left = Math.min(Math.min(Math.min(Math.min(points[0].x, points[1].x),points[2].x),points[3].x), this.boundingBox.left);
			this.boundingBox.right = Math.max(Math.max(Math.max(Math.max(points[0].x, points[1].x),points[2].x),points[3].x), this.boundingBox.right);
			this.boundingBox.top = Math.max(Math.max(Math.max(Math.max(points[0].y, points[1].y),points[2].y),points[3].y), this.boundingBox.top);
			this.boundingBox.bottom = Math.min(Math.min(Math.min(Math.min(points[0].y, points[1].y),points[2].y),points[3].y), this.boundingBox.bottom);
		}
		for(SpriterBone child: this.childBones){
			child.calcBoundingBox(boundingBox);
			this.boundingBox.set(child.boundingBox);
		}
	}
	
}
