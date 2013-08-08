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

import com.brashmonkey.spriter.SpriterCalculator;
import com.brashmonkey.spriter.animation.SpriterKeyFrame;
import com.brashmonkey.spriter.draw.DrawInstruction;
import com.brashmonkey.spriter.objects.SpriterBone;
import com.brashmonkey.spriter.objects.SpriterObject;

/**
 * This class is made to interpolate between two running animations.
 * The idea is, to give an instance of this class two AbstractSpriterPlayer objects which hold and animate the same spriter entity.
 * This will interpolate the runtime transformations of the bones and objects with a weight between 0 and 1.
 * You will be also able to interpolate SpriterPlayerInterpolators with each other, since it extends  #SpriterAbstractPlayer.
 * Note that this #SpriterAbstractPlayer needs 3 times more calculation effort than a normal #SpriterPlayer.
 * 
 * @author Trixt0r
 */
public class SpriterPlayerInterpolator extends SpriterAbstractPlayer{
	
	private SpriterAbstractPlayer first, second;
	private float weight;
	private boolean interpolateSpeed = false;
	private SpriterKeyFrame[] frame;

	/**
	 * Returns an instance of this class, which will manage the interpolation between two #SpriterAbstractPlayer instances.
	 * @param first player to interpolate with the second one.
	 * @param second player to interpolate with the first one.
	 */
	public SpriterPlayerInterpolator(SpriterAbstractPlayer first, SpriterAbstractPlayer second){
		super(first.loader, first.animations);
		this.weight = 0.5f;
		setPlayers(first, second);
		this.frame = new SpriterKeyFrame[1];
		this.generateData();
		this.update(0, 0);
	}
	
	/**
	 * Note: Make sure, that both instances hold the same bone and object structure.
	 * Otherwise you will not get the interpolation you wish.
	 * @param first SpriterPlayer instance to interpolate.
	 * @param second SpriterPlayer instance to interpolate.
	 */
	public void setPlayers(SpriterAbstractPlayer first, SpriterAbstractPlayer second){
		this.first = first;
		this.second = second;
		this.moddedBones = this.first.moddedBones;
		this.moddedObjects = this.first.moddedObjects;
		this.first.setRootParent(this.rootParent);
		this.second.setRootParent(this.rootParent);
	}
	
	/**
	 * @param weight to set. 0 means the animation of the first player will get played back.
	 * 1 means the second player will get played back.
	 */
	public void setWeight(float weight){
		this.weight = weight;
	}
	
	/**
	 * @return The current weight.
	 */
	public float getWeight(){
		return this.weight;
	}
	
	/**
	 * @return The first player.
	 */
	public SpriterAbstractPlayer getFirst(){
		return this.first;
	}
	
	/**
	 * @return The second player.
	 */
	public SpriterAbstractPlayer getSecond(){
		return this.second;
	}
	
	@Override
	protected void step(float xOffset, float yOffset){
		int fristLastSpeed = first.frameSpeed, secondLastSpeed = second.frameSpeed;
		int speed;
		if(this.interpolateSpeed)
			speed = (int)this.interpolate(first.frameSpeed, second.frameSpeed, 0, 1, this.weight);
		else
			speed = this.frameSpeed;
		this.first.frameSpeed = speed;
		this.second.frameSpeed = speed;
		
		this.moddedBones = (this.weight <= 0.5f) ? this.first.moddedBones: this.second.moddedBones;
		this.moddedObjects = (this.weight <= 0.5f) ? this.first.moddedObjects: this.second.moddedObjects;
		SpriterBone[] tempBones = this.tempBones;
		SpriterObject[] tempObjects = this.tempObjects;
		if(this.weight == 0){
			this.tempBones = this.first.tempBones;
			this.tempObjects = this.first.tempObjects;
			this.first.update(xOffset,yOffset);
			this.instructions = this.first.instructions;
			this.currenObjectsToDraw = first.currenObjectsToDraw;
		}
		else if(this.weight == 1){
			this.tempBones = this.second.tempBones;
			this.tempObjects = this.second.tempObjects;
			this.second.update(xOffset,yOffset);
			this.instructions = this.second.instructions;
			this.currenObjectsToDraw = second.currenObjectsToDraw;
		}
		else{
			this.currenObjectsToDraw = first.currenObjectsToDraw;
			this.first.update(0,0);
			this.second.update(0,0);
		
			SpriterKeyFrame key1 = (first.transitionFixed) ? first.lastFrame: first.lastTempFrame;
			this.frame[0] = (second.transitionFixed) ? second.lastFrame: second.lastTempFrame;
			this.transformBones(key1, this.frame[0], xOffset, yOffset);
			this.transformObjects(first.lastFrame, this.frame[0], xOffset, yOffset);
		}
		this.tempBones = tempBones;
		this.tempObjects = tempObjects;
		this.first.frameSpeed = fristLastSpeed;
		this.second.frameSpeed = secondLastSpeed;
	}
	
	@Override
	protected void setInstructionRef(DrawInstruction dI, SpriterObject obj1, SpriterObject obj2){
		dI.ref = (this.weight <= 0.5f || obj2 == null) ? obj1.getRef(): obj2.getRef();
		dI.loader = (this.weight <= 0.5f || obj2 == null) ? obj1.getLoader(): obj2.getLoader();
		dI.obj = (this.weight <= 0.5f || obj2 == null) ? obj1: obj2;
	}
	
	/**
	 * See {@link SpriterCalculator#calculateInterpolation(float, float, float, float, long)}
	 * Can be inherited, to handle other interpolation techniques. Standard is linear interpolation.
	 */
	@Override
	protected float interpolate(float a, float b, float timeA, float timeB, float currentTime){
		return this.interpolator.interpolate(a, b, 0, 1, this.weight);
	}
	
	/**
	 * See {@link SpriterCalculator#calculateInterpolation(float, float, float, float, long)}
	 * Can be inherited, to handle other interpolation techniques. Standard is linear interpolation.
	 */
	@Override
	protected float interpolateAngle(float a, float b, float timeA, float timeB, float currentTime){
		return this.interpolator.interpolateAngle(a, b, 0, 1, this.weight);
	}
	
	/**
	 * @return true if this player also interpolates the speed of both players. false if not.
	 */
	public boolean interpolatesSpeed(){
		return this.interpolateSpeed;
	}
	
	/**
	 * @param inter indicates whether this player has to interpolate the speed of bother players or not.
	 * If it set to false, this player will set for both players the speed which this player has. See {@link #setFrameSpeed(int)}
	 */
	public void setInterpolateSpeed(boolean inter){
		this.interpolateSpeed = inter;
	}
}
