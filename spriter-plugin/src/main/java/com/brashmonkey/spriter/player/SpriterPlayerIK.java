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

package com.brashmonkey.spriter.player;

import java.util.List;

import com.brashmonkey.spriter.SpriterCalculator;
import com.brashmonkey.spriter.draw.AbstractDrawer;
import com.brashmonkey.spriter.objects.SpriterBone;
import com.brashmonkey.spriter.objects.SpriterIKObject;
import com.brashmonkey.spriter.objects.SpriterKeyFrame;
import com.discobeard.spriter.dom.Entity;

public class SpriterPlayerIK extends SpriterPlayer {
	
	private boolean resovling;
	private SpriterBone[] bones;
	private SpriterBone tempBone;
	private SpriterIKObject[] objects;
	private float tolerance;

	public SpriterPlayerIK(Entity entity, AbstractDrawer<?> drawer,
			List<SpriterKeyFrame[]> keyframes) {
		super(entity, drawer, keyframes);
		this.resovling = false;
		this.tolerance = 0.5f;
		this.tempBone = new SpriterBone();
		super.update(0, 0);
	}
	
	@Override
	public void update(float xOffset, float yOffset){
		this.frameSpeed = 0;
		if(this.resovling) this.resolve(xOffset,yOffset);
		super.update(xOffset, yOffset);
	}
	
	private void resolve(float xOffset, float yOffset){
		SpriterBone bone = null;
		SpriterBone parent = null;
		SpriterIKObject object = null;
		float angle, angleDiff;
		for(int i = 0; i < this.bones.length; i++){
			bone = this.bones[i];
			parent = bone;
			object = this.objects[i];
			System.out.println(SpriterCalculator.distanceBetween(bone.getX(), bone.getY(), object.getX(), object.getY()));
			while(SpriterCalculator.distanceBetween(bone.getX(), bone.getY(), object.getX(), object.getY()) > 10){
				this.tempBone.setX(object.xOffset);
				this.tempBone.setY(object.yOffset);
				angle = SpriterCalculator.angleBetween(bone.getX(), bone.getY(), object.getX(), object.getY());
				this.moddedBones[bone.getId()].setAngle(angle);
				super.update(xOffset, yOffset);
				SpriterCalculator.rotatePoint(bone, this.tempBone);
				for(int j = 0; j < object.chainLength; j++){
					parent = this.tempBones[parent.getParentId()];
					angle = SpriterCalculator.angleBetween(parent.getX(), parent.getY(), object.getX(), object.getY());
					angleDiff = SpriterCalculator.angleBetween(parent.getX(), parent.getY(), this.tempBone.getX(), this.tempBone.getY());
					this.moddedBones[parent.getId()].setAngle(SpriterCalculator.angleDifference(angle, angleDiff));
					super.update(xOffset, yOffset);
				}
			}
		}
		this.resovling = false;
	}

	/**
	 * @return the resovling
	 */
	public boolean isResovling() {
		return resovling;
	}

	/**
	 * @param resovling the resovling to set
	 */
	public void setResovling(boolean resovling) {
		this.resovling = resovling;
	}
	
	/**
	 * @return the bones
	 */
	public SpriterBone[] getBones() {
		return bones;
	}

	/**
	 * @param bones the bones to set
	 */
	public void setBones(SpriterBone[] bones) {
		this.bones = bones;
	}

	/**
	 * @return the objects
	 */
	public SpriterIKObject[] getIKObjects() {
		return objects;
	}

	/**
	 * @param objects the objects to set
	 */
	public void setIKObjects(SpriterIKObject[] objects) {
		this.objects = objects;
	}

	public float getTolerance() {
		return tolerance;
	}

	public void setTolerance(float tolerance) {
		this.tolerance = tolerance;
	}

}
