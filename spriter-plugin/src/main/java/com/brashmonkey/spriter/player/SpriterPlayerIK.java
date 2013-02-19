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


import com.brashmonkey.spriter.draw.AbstractDrawer;
import com.brashmonkey.spriter.objects.SpriterBone;
import com.brashmonkey.spriter.objects.SpriterIKObject;
import com.discobeard.spriter.dom.Entity;

public class SpriterPlayerIK extends SpriterPlayer {
	
	private boolean resovling;
	private SpriterBone[] bones;
	private SpriterIKObject[] objects;
	private float tolerance;

	public SpriterPlayerIK(Entity entity, AbstractDrawer<?> drawer) {
		super(entity, drawer);
		this.resovling = false;
		this.tolerance = 0.5f;
		super.step(0, 0);
	}
	
	@Override
	protected void step(float xOffset, float yOffset){
		//this.frameSpeed = 0;
		super.step(xOffset, yOffset);
		if(this.resovling) this.resolve(xOffset,yOffset);
	}
	
	private void resolve(float xOffset, float yOffset){
		for(int i = 0; i < this.bones.length; i++){
		}
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
/*
		float xx, yy;
		SpriterBone bone = null;
		SpriterBone parent = null;
		SpriterIKObject object = null;
		float angle, angleDiff;
		for(int i = 0; i < this.bones.length; i++){
			bone = this.bones[i];
			object = this.objects[i];
			parent = this.tempBones[bone.getParentId()];
			//System.out.println(SpriterCalculator.distanceBetween(bone.getX(), bone.getY(), object.getX(), object.getY()));
			//while(SpriterCalculator.distanceBetween(bone.getX(), bone.getY(), object.getX(), object.getY()) > 10){
			angle = SpriterCalculator.angleBetween(bone.getX(), bone.getY(), object.getX(), object.getY());
			bone.setAngle(angle);
			for(int j = 0; j < object.chainLength && parent != null; j++){
				xx= bone.getX() + (float)Math.cos(Math.toRadians(bone.getAngle()))*200;
				yy= bone.getY() + (float)Math.sin(Math.toRadians(bone.getAngle()))*200;
				
				angle = SpriterCalculator.angleBetween(parent.getX(), parent.getY(), object.getX(), object.getY());
				angleDiff = SpriterCalculator.angleBetween(parent.getX(), parent.getY(), xx, yy);
				parent.setAngle(parent.getAngle()+SpriterCalculator.angleDifference(angle,angleDiff));
				
				for(SpriterBone b: parent.getChildBones())
					SpriterCalculator.rotatePoint(parent, this.tempBones[b.getId()]);
				parent.setAngle(parent.getAngle()+90);
				if(parent.hasParent()) parent = this.tempBones[bone.getParentId()];
				else parent = null;
				angle = SpriterCalculator.angleBetween(bone.getX(), bone.getY(), object.getX(), object.getY());
				bone.setAngle(angle);
			}
			//}
		}
		//this.resovling = false;*/
