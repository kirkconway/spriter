package com.spriter.player;

import com.spriter.SpriterCalculator;
import com.spriter.draw.DrawInstruction;
import com.spriter.objects.SpriterBone;
import com.spriter.objects.SpriterKeyFrame;
import com.spriter.objects.SpriterObject;

/**
 * This class is made to interpolate between two running animations.
 * The idea is, to give an instance of this class two AbstractSpriterPlayer objects which hold and animate the same spriter entity.
 * This will interpolate the runtime transformations of the bones and objects with a weight between 0 and 1.
 * You will be also able to interpolate SpriterPlayerInterpolators with each other, since it extends  #SpriterAbstractPlayer.
 * 
 * @author Trixt0r
 */
public class SpriterPlayerInterpolator extends SpriterAbstractPlayer{
	
	private SpriterAbstractPlayer first, second;
	private float weight;
	private boolean interpolateSpeed = false;

	public SpriterPlayerInterpolator(SpriterAbstractPlayer first, SpriterAbstractPlayer second){
		super(first.drawer, first.keyframes);
		this.weight = 0.5f;
		setPlayers(first, second);
	}
	
	/**
	 * Note: Make sure, that both instances hold the
	 * @param first First SpriterPlayer instance to interpolate.
	 * @param second Second SpriterPlayer instance to interpolate.
	 */
	public void setPlayers(SpriterAbstractPlayer first, SpriterAbstractPlayer second){
		this.first = first;
		this.second = second;
		this.moddedBones = this.first.moddedBones;
		this.moddedObjects = this.first.moddedObjects;
		this.first.setRootParent(this.rootParent);
		this.second.setRootParent(this.rootParent);
	}
	
	public void setWeight(float weight){
		this.weight = weight;
	}
	
	public float getWeight(){
		return this.weight;
	}
	
	public SpriterAbstractPlayer getFirst(){
		return this.first;
	}
	
	public SpriterAbstractPlayer getSecond(){
		return this.second;
	}
	
	@Override
	public void update(float xOffset, float yOffset){
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
			this.first.update(xOffset,yOffset);
			this.second.update(xOffset,yOffset);
		
			SpriterKeyFrame key1 = (first.transitionFixed) ? first.lastFrame: first.lastTempFrame;
			SpriterKeyFrame key2 = (second.transitionFixed) ? second.lastFrame: second.lastTempFrame;
		
			this.transformBones(key1, key2, xOffset, yOffset);
			this.transformObjects(first.lastFrame, second.lastFrame, xOffset, yOffset);
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
	protected float interpolate(float a, float b, float timeA, float timeB, float currentTime){
		return this.interpolator.interpolate(a, b, 0, 1, this.weight);
	}
	
	/**
	 * See {@link SpriterCalculator#calculateInterpolation(float, float, float, float, long)}
	 * Can be inherited, to handle other interpolation techniques. Standard is linear interpolation.
	 */
	protected float interpolateAngle(float a, float b, float timeA, float timeB, float currentTime){
		return this.interpolator.interpolateAngle(a, b, 0, 1, this.weight);
	}
	
	public boolean interpolatesSpeed(){
		return this.interpolateSpeed;
	}
	
	public void setInterpolateSpeed(boolean inter){
		this.interpolateSpeed = inter;
	}
}
