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
import com.discobeard.spriter.dom.SpriterData;

public class SpriterPlayerIK extends SpriterPlayer {
	
	private boolean resovling;
	private SpriterBone[] bones;
	private SpriterIKObject[] objects;
	private float tolerance;
	private ISpriterIKResolver resolver;

	public SpriterPlayerIK(SpriterData data, Entity entity, AbstractDrawer<?> drawer) {
		super(data, entity, drawer);
		this.resovling = false;
		this.tolerance = 0.5f;
		this.resolver = new SpriterCCDResolver(this);
		super.step(0, 0);
		this.updateObjects = false;
	}
	
	@Override
	protected void step(float xOffset, float yOffset){
		super.step(xOffset, yOffset);
		if(this.resovling) this.resolve();
		this.transformObjects(firstKeyFrame, secondKeyFrame, xOffset, yOffset);
		for(int i = 0; i < this.currenObjectsToDraw; i++)
			this.tempObjects[i].copyValuesTo(this.instructions[i]);
	}
	
	private void resolve(){
		for(int i = 0; i < this.bones.length; i++)
			for(int j = 0; j < this.objects[i].iterations; j++)
				this.resolver.resolve(this.objects[i].getX(), this.objects[i].getY(), this.objects[i].chainLength, this.tempBones[this.bones[i].getId()]);
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
	
	/**
	 * Changes the state of each effector to unactive. The effect results in non animated bodyparts.
	 * @param parents indicates whether parents of the effectors have to be deactivated or not.
	 */
	public void deactivateEffectors(boolean parents){
		for(int i = 0; i < this.bones.length; i++){
			this.moddedBones[this.bones[i].getId()].setActive(false);
			if(!parents) continue;
			SpriterBone par = (SpriterBone) this.bones[i].getParent();
			for(int j = 0; j < this.objects[i].chainLength && par != null; j++){
				this.moddedBones[par.getId()].setActive(false);
				par = (SpriterBone) par.getParent();
			}
		}
	}
	
	public void activateEffectors(){
		for(int i = 0; i < this.bones.length; i++){
			this.moddedBones[this.bones[i].getId()].setActive(true);
			SpriterBone par = (SpriterBone) this.bones[i].getParent();
			for(int j = 0; j < this.objects[i].chainLength && par != null; j++){
				this.moddedBones[par.getId()].setActive(true);
				par = (SpriterBone) par.getParent();
			}
		}
	}

	/**
	 * @return the resolver
	 */
	public ISpriterIKResolver getResolver() {
		return resolver;
	}

	/**
	 * @param resolver the resolver to set
	 */
	public void setResolver(ISpriterIKResolver resolver) {
		this.resolver = resolver;
	}

}
