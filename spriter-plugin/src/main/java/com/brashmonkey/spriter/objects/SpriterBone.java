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

import com.brashmonkey.spriter.SpriterCalculator;

/**
 * A SpriterBone is a bone like in the Spriter editor. It can hold children (#SpriterObject and #SpriterBone) which get manipulated relative to this object.
 * @author Trixt0r
 */
public class SpriterBone extends SpriterAbstractObject{
	
	List<SpriterBone> childBones;
	List<SpriterObject> childObjects;
	int spin;
	String name;
	
	public SpriterBone(){
		this.childBones = new LinkedList<SpriterBone>();
		this.childObjects = new LinkedList<SpriterObject>();
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

	public int getSpin() {
		return spin;
	}

	public void setSpin(int spin) {
		this.spin = spin;
	}
	
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void copyValuesTo(SpriterBone bone){
		super.copyValuesTo(bone);
		bone.setName(getName());
		bone.setSpin(getSpin());
		bone.childBones = this.childBones;
		bone.childObjects = this.childObjects;
	}
	
	public void update(){
		if(this.parent != null)
			SpriterCalculator.rotatePoint(parent, this);
	}
	
	public void updateRecursively(){
		this.update();
		for(SpriterBone child: this.childBones)
			child.updateRecursively();
	}
	
}
