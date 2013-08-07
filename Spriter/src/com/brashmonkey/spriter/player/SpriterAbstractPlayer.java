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
import java.util.LinkedList;

import com.brashmonkey.spriter.SpriterPoint;
import com.brashmonkey.spriter.SpriterCalculator;
import com.brashmonkey.spriter.SpriterKeyFrameProvider;
import com.brashmonkey.spriter.SpriterRectangle;
import com.brashmonkey.spriter.draw.DrawInstruction;
import com.brashmonkey.spriter.file.FileLoader;
import com.brashmonkey.spriter.interpolation.SpriterInterpolator;
import com.brashmonkey.spriter.interpolation.SpriterLinearInterpolator;
import com.brashmonkey.spriter.objects.SpriterAbstractObject;
import com.brashmonkey.spriter.objects.SpriterBone;
import com.brashmonkey.spriter.objects.SpriterKeyFrame;
import com.brashmonkey.spriter.objects.SpriterModObject;
import com.brashmonkey.spriter.objects.SpriterObject;
import com.discobeard.spriter.dom.SpriterData;

/**
 * SpriterAbstractPlayer is meant to be a base for SpriterPlayer.
 * This abstract class has been created, to have the ability to interpolate
 * other running players at runtime. See #SpriterPlayerInterpolator.
 * 
 * @author Trixt0r
 */
public abstract class SpriterAbstractPlayer {	
	
	SpriterKeyFrame lastFrame,lastTempFrame;
	public boolean transitionFixed = true, drawn = false;
	protected SpriterInterpolator interpolator;
	protected int currenObjectsToDraw, flipX = 1, flipY = 1, frameSpeed = 10, zIndex = 0;
	protected DrawInstruction[] instructions;
	protected SpriterModObject[] moddedObjects,moddedBones;
	protected SpriterObject[] tempObjects, tempObjects2, nonTransformedTempObjects;
	protected SpriterBone[] tempBones, tempBones2, nonTransformedTempBones;
	protected List<SpriterKeyFrame[]> keyframes;
	protected SpriterAbstractObject rootParent, tempParent;
	protected float angle = 0f, scale = 1f, pivotX = 0f, pivotY = 0f;
	protected long frame;
	protected List<SpriterAbstractPlayer> players;
	private boolean generated = false;
	SpriterRectangle rect;
	public final FileLoader<?> loader;
	
	/**
	 * Constructs a new SpriterAbstractPlayer object which is able to animate SpriterBone instances and SpriterObject instances.
	 * @param loader {@link FileLoader} which you have to implement on your own.
	 * @param keyframes A list of SpriterKeyFrame arrays. See {@link SpriterKeyFrameProvider#generateKeyFramePool(SpriterData)} to get the list.
	 * Generate these keyframes once to save memory.
	 */
	public SpriterAbstractPlayer(FileLoader<?> loader, List<SpriterKeyFrame[]> keyframes){
		this.loader = loader;
		this.keyframes = keyframes;
		this.rootParent = new SpriterBone();
		this.tempParent = new SpriterBone();
		this.rootParent.setName("playerRoot");
		this.tempParent.setName("playerRoot");
		this.lastFrame = new SpriterKeyFrame();
		this.lastTempFrame = new SpriterKeyFrame();
		this.interpolator = SpriterLinearInterpolator.interpolator;
		this.players = new LinkedList<SpriterAbstractPlayer>();
		rect = new SpriterRectangle(0,0,0,0);
	}
	
	/**
	 * Generates data which is necessary to animate all animations as intended.
	 * This method has to called inside the specific constructor.
	 */
	protected final void generateData(){
		int maxObjects = 0, maxBones = 0;
		for(SpriterKeyFrame[] key: this.keyframes){
			for(SpriterKeyFrame k: key){
				maxBones = Math.max(k.getBones().length, maxBones);
				maxObjects = Math.max(k.getObjects().length, maxObjects);
				for(SpriterObject o: k.getObjects()){
					o.setLoader(this.loader);
					o.setRef(this.loader.findReference(o.getRef()));
				}
			}
		}
		
		this.instructions = new DrawInstruction[maxObjects];
		this.moddedObjects = new SpriterModObject[this.instructions.length];
		this.tempObjects = new SpriterObject[this.instructions.length];
		this.tempObjects2 = new SpriterObject[this.instructions.length];
		this.nonTransformedTempObjects = new SpriterObject[this.instructions.length];
		for(int i = 0; i < this.instructions.length; i++){
			this.instructions[i] = new DrawInstruction(null,0,0,0,0,0,0,0,0);
			this.tempObjects[i] = new SpriterObject();
			this.tempObjects2[i] = new SpriterObject();
			this.nonTransformedTempObjects[i] = new SpriterObject();
			this.nonTransformedTempObjects[i].setId(i);
			this.moddedObjects[i] = new SpriterModObject();
			this.moddedObjects[i].setId(i);
		}
		this.tempBones = new SpriterBone[maxBones];
		this.tempBones2 = new SpriterBone[tempBones.length];
		this.nonTransformedTempBones = new SpriterBone[tempBones.length];
		this.moddedBones = new SpriterModObject[this.tempBones.length];
		for(int i = 0; i < this.tempBones.length; i++){
			this.tempBones[i] = new SpriterBone();
			this.tempBones2[i] = new SpriterBone();
			this.nonTransformedTempBones[i] = new SpriterBone();
			this.nonTransformedTempBones[i].setId(i);
			this.moddedBones[i] = new SpriterModObject();
			this.moddedBones[i].setId(i);
		}
		
		SpriterBone[] tmpBones1 = new SpriterBone[this.tempBones.length], tmpBones2 = new SpriterBone[this.tempBones.length];
		SpriterObject[] tmpObjs1 = new SpriterObject[this.instructions.length], tmpObjs2 = new SpriterObject[this.instructions.length];
		for(int i = 0; i < tmpObjs1.length; i++){
			tmpObjs1[i] = new SpriterObject();
			tmpObjs2[i] = new SpriterObject();
		}
		for(int i = 0; i < tmpBones1.length; i++){
			tmpBones1[i] = new SpriterBone();
			tmpBones2[i] = new SpriterBone();
		}
		this.lastFrame.setBones(tmpBones1);
		this.lastFrame.setObjects(tmpObjs1);
		this.lastTempFrame.setBones(tmpBones2);
		this.lastTempFrame.setObjects(tmpObjs2);
		this.generated = true;
	}
	
	/**
	 * Updates this player and translates the animation to xOffset and yOffset.
	 * Frame is updated by previous set frame speed (See {@link #setFrameSpeed(long)} ).
	 * This method makes sure that the keyframes get played back.
	 * @param xOffset
	 * @param yOffset
	 */
	public final void update(float xOffset, float yOffset){
		if(!this.generated) 
			System.out.println("Warning! You can not update this player, since necessary data has not been initialized!");
		else this.step(xOffset, yOffset);
	}
	
	/**
	 * Has to be implemented by the specific player.
	 * @param xOffset
	 * @param yOffset
	 */
	protected abstract void step(float xOffset, float yOffset);
	
	/**
	 * Interpolates the objects of firstFrame and secondFrame.
	 * @param currentFrame
	 * @param nextFrame
	 * @param xOffset
	 * @param yOffset
	 */
	protected void transformObjects(SpriterKeyFrame currentFrame, SpriterKeyFrame nextFrame, float xOffset, float yOffset) {
		this.rect.set(xOffset, yOffset, xOffset, yOffset);
		this.updateTransformedTempObjects(nextFrame.getObjects(), this.tempObjects2);
		this.updateTempObjects(currentFrame.getObjects(), this.nonTransformedTempObjects);
		
		for (int i = 0; i < this.currenObjectsToDraw; i++) this.tweenObject(this.nonTransformedTempObjects[i], nextFrame, i, currentFrame.getStartTime());
	}

	protected void transformObjects(SpriterKeyFrame currentFrame, SpriterKeyFrame[] nextFrames, float xOffset, float yOffset) {
		this.rect.set(xOffset, yOffset, xOffset, yOffset);
		this.updateTempObjects(currentFrame.getObjects(), this.nonTransformedTempObjects);
		
		for (int i = 0; i < this.currenObjectsToDraw; i++) {
			SpriterKeyFrame nextFrame = null;
			int cnt = 0;
			for(int j = (currentFrame.getId()+(int)Math.signum(frameSpeed) + nextFrames.length)%nextFrames.length; nextFrame == null && cnt < nextFrames.length;
					j = (j+(int)Math.signum(frameSpeed) + nextFrames.length)%nextFrames.length, cnt++){
				for(SpriterObject object: nextFrames[j].getObjects()){
					if(this.nonTransformedTempObjects[i].equals(object)){
						nextFrame = nextFrames[j];
						object.copyValuesTo(this.tempObjects2[object.getId()]);
						if(!this.tempObjects2[object.getId()].hasParent()){
							this.tempObjects2[object.getId()].setX(this.tempObjects2[object.getId()].getX()+this.pivotX);
							this.tempObjects2[object.getId()].setY(this.tempObjects2[object.getId()].getY()+this.pivotY);
						}
						this.translateRelative(this.tempObjects2[object.getId()], (this.tempObjects2[object.getId()].hasParent()) ?
								this.tempBones2[this.tempObjects2[object.getId()].getParentId()]: this.tempParent);
					}
				}
			}
			if(this.nonTransformedTempObjects[i].getName().equals("legUpperRight")) System.out.println("current frame: "+this.frame+", found frame at "+nextFrame.getStartTime()+"\nfor "+this.nonTransformedTempObjects[i]);
			this.tweenObject(this.nonTransformedTempObjects[i], nextFrame, i, currentFrame.getStartTime());
		}
	}
	
	private void tweenObject(SpriterObject obj1, SpriterKeyFrame nextFrame, int i, long startTime){
		DrawInstruction dI = this.instructions[i];
		SpriterObject obj2 = null;
		obj1.copyValuesTo(this.tempObjects[i]);
		SpriterAbstractObject parent = null;
		if(!obj1.isTransientObject()) {
			obj2 = (SpriterObject) this.findTimelineObject(obj1, nextFrame.getObjects());
			this.tempObjects[i].setTimeline((obj2 != null) ? obj1.getTimeline() : -1);
			parent = (obj1.hasParent()) ? this.tempBones[obj1.getParentId()] : this.tempParent;

			if(obj2 != null){
				if(parent != this.tempParent || (obj2.hasParent() && parent == this.tempParent)){
					if(!obj1.getParent().equals(obj2.getParent())){
						obj2 = (SpriterObject) this.findTimelineObject(obj1, this.tempObjects2);
						SpriterCalculator.setRelative(parent, obj2);
					}
				} else if(obj2.hasParent()){
					obj2 = (SpriterObject) this.findTimelineObject(obj1, this.tempObjects2);
					SpriterCalculator.setRelative(parent, obj2);
				}					
				this.interpolateSpriterObject(this.tempObjects[i], obj1, obj2, startTime, nextFrame.getStartTime());
			}
			
			this.moddedObjects[obj1.getId()].modSpriterObject(this.tempObjects[i]);
			
			if(this.transitionFixed) this.tempObjects[i].copyValuesTo(this.lastFrame.getObjects()[i]);
			else this.tempObjects[i].copyValuesTo(this.lastTempFrame.getObjects()[i]);
		}
		else parent = this.tempParent;
		if(!this.tempObjects[i].hasParent()){
			this.tempObjects[i].setX(this.tempObjects[i].getX()+this.pivotX);
			this.tempObjects[i].setY(this.tempObjects[i].getY()+this.pivotY);
		}
		this.translateRelative(this.tempObjects[i], parent);
		if(this.moddedObjects[obj1.getId()].getRef() != null)	this.tempObjects[i].setRef(this.moddedObjects[obj1.getId()].getRef());
		if(this.moddedObjects[obj1.getId()].getLoader() != null) this.tempObjects[i].setLoader(this.moddedObjects[obj1.getId()].getLoader());
		this.tempObjects[i].copyValuesTo(dI);
		
		this.setInstructionRef(dI, this.tempObjects[i], obj2);
	}
	
	protected void setInstructionRef(DrawInstruction dI, SpriterObject obj1, SpriterObject obj2){
		dI.ref = obj1.getRef();
		dI.loader = obj1.getLoader();
		dI.obj = obj1;
	}
	
	
	/**
	 * Interpolates the bones for this animation.
	 * @param currentFrame first keyframe
	 * @param nextFrame second keyframe
	 * @param currentAnimationTime
	 * @param key2StartTime
	 * @return interpolated SpriterBone array
	 */
	protected void transformBones(SpriterKeyFrame currentFrame, SpriterKeyFrame nextFrame, float xOffset, float yOffset){
		if(this.rootParent.getParent() != null) this.translateRoot();
		else{
			this.tempParent.setX(xOffset); this.tempParent.setY(yOffset);
			this.tempParent.setAngle(this.angle);
			this.tempParent.setScaleX(this.flipX);
			this.tempParent.setScaleY(this.flipY);
		}
		this.setScale(this.scale);
		
		this.updateTransformedTempObjects(nextFrame.getBones(), this.tempBones2);
		this.updateTempObjects(currentFrame.getBones(), this.nonTransformedTempBones);
		
		for (int i = 0; i < this.nonTransformedTempBones.length; i++) {
			this.tweenBone(this.nonTransformedTempBones[i], nextFrame, i, currentFrame.getStartTime());
		}
	}
	
	protected void transformBones(SpriterKeyFrame currentFrame, SpriterKeyFrame[] nextFrames, float xOffset, float yOffset){	
		if(this.rootParent.getParent() != null) this.translateRoot();
		else{
			this.tempParent.setX(xOffset); this.tempParent.setY(yOffset);
			this.tempParent.setAngle(this.angle);
			this.tempParent.setScaleX(this.flipX);
			this.tempParent.setScaleY(this.flipY);
		}
		this.setScale(this.scale);
		this.updateTempObjects(currentFrame.getBones(), this.nonTransformedTempBones);	
		for (int i = 0; i < this.nonTransformedTempBones.length; i++) {
			SpriterKeyFrame nextFrame = null;
			int cnt = 0;
			for(int j = (currentFrame.getId()+(int)Math.signum(frameSpeed) + nextFrames.length)%nextFrames.length; nextFrame == null && cnt < nextFrames.length;
					j = (j+(int)Math.signum(frameSpeed) + nextFrames.length)%nextFrames.length, cnt++){
				/*if(this.nonTransformedTempBones[i].getName().equals("leftUpperLeg"))
					System.out.println("searching at: "+j);*/
				for(SpriterBone bone: nextFrames[j].getBones()){
					if(this.nonTransformedTempBones[i].equals(bone)){
						nextFrame = nextFrames[j];
						bone.copyValuesTo(this.tempBones2[bone.getId()]);
						if(!this.tempBones2[bone.getId()].hasParent()){
							this.tempBones2[bone.getId()].setX(this.tempBones2[bone.getId()].getX()+this.pivotX);
							this.tempBones2[bone.getId()].setY(this.tempBones2[bone.getId()].getY()+this.pivotY);
						}
						this.translateRelative(this.tempBones2[bone.getId()], (this.tempBones2[bone.getId()].hasParent()) ?
								this.tempBones2[this.tempBones2[bone.getId()].getParentId()]: this.tempParent);
					}
				}
			}
			this.tweenBone(this.nonTransformedTempBones[i], nextFrame, i, currentFrame.getStartTime());
			//if(this.nonTransformedTempBones[i].getName().equals("leftUpperLeg")) System.out.println("frame: "+this.frame+", found next frame at: "+nextFrame.getStartTime()+", bone: "+this.nonTransformedTempBones[i]);
			/*else{
				System.err.println("Did not find next frame for bone \""+this.nonTransformedTempBones[i].getName()+"\"");
				//throw new RuntimeException("Did not find next frame for bone \""+this.nonTransformedTempBones[i].getName()+"\"");
			}*/
			//System.out.println("Found frame at "+nextFrame.getStartTime()+", for "+this.nonTransformedTempBones[i]);
		}
	}
	
	private void tweenBone(SpriterBone bone1, SpriterKeyFrame nextFrame, int i, long firstTime){
		bone1.copyValuesTo(this.tempBones[i]);
		SpriterBone bone2 = (SpriterBone) this.findTimelineObject(bone1, nextFrame.getBones());
		this.tempBones[i].setTimeline((bone2 != null) ? bone1.getTimeline() : -1);
		SpriterAbstractObject parent = (this.tempBones[i].hasParent()) ?  this.tempBones[this.tempBones[i].getParentId()]: this.tempParent;
		if(bone2 != null){
			if(parent != this.tempParent){
				if(!bone1.getParent().equals(bone2.getParent())){
					bone2 = (SpriterBone) this.findTimelineObject(bone1, this.tempBones2);
					SpriterCalculator.setRelative(parent, bone2);
				}
			} else if(bone2.hasParent()){
				bone2 = (SpriterBone) this.findTimelineObject(bone1, this.tempBones2);
				SpriterCalculator.setRelative(parent, bone2);
			}
			this.interpolateAbstractObject(this.tempBones[i], bone1, bone2, firstTime, nextFrame.getStartTime());
		} /*else
			System.err.println("Could not find second bone to tween");*/
		
		this.moddedBones[bone1.getId()].modSpriterBone(this.tempBones[i]);
		
		if(this.transitionFixed) this.tempBones[i].copyValuesTo(this.lastFrame.getBones()[i]);
		else this.tempBones[i].copyValuesTo(this.lastTempFrame.getBones()[i]);
		if(!this.tempBones[i].hasParent() || !this.moddedBones[bone1.getId()].isActive()){
			this.tempBones[i].setX(this.tempBones[i].getX()+this.pivotX);
			this.tempBones[i].setY(this.tempBones[i].getY()+this.pivotY);
		}
		this.translateRelative(this.tempBones[i], parent);
	}
	
	private void updateTransformedTempObjects(SpriterAbstractObject[] source, SpriterAbstractObject[] target){
		for(int i = 0; i< source.length; i++){
			source[i].copyValuesTo(target[i]);
			if(!target[i].hasParent()){
				target[i].setX(target[i].getX()+this.pivotX);
				target[i].setY(target[i].getY()+this.pivotY);
			}
			this.translateRelative(target[i], (target[i].hasParent()) ?  this.tempBones2[target[i].getParentId()]: this.tempParent);
		}
	}
	
	private void updateTempObjects(SpriterAbstractObject[] source, SpriterAbstractObject[] target){
		for (int i = 0; i < source.length; i++) {
			SpriterAbstractObject object = source[i];
			boolean found = false;
			for(int j = 0; j < target.length && !found; j++){
				if(object.getId() == target[j].getId()){
					object.copyValuesTo(target[j]);
					found = true;
				}
			}
		}
	}
	
	/*float angleLinear(float angleA,float angleB,int spin, float startTime, float endTime)
	{   
		switch(spin){
		case 1: if((angleB-angleA)<0) angleB+=360; break;
		case -1: if((angleB-angleA)>0) angleB-=360; break;   
		default: return angleA;
		}
		return this.interpolate(angleA, angleB, startTime, endTime, this.frame);
	}*/
	private void translateRoot(){
		this.rootParent.copyValuesTo(tempParent);
		this.tempParent.setAngle(this.tempParent.getAngle()*this.flipX*this.flipY + this.rootParent.getParent().getAngle());
		this.tempParent.setScaleX(this.tempParent.getScaleX() * this.rootParent.getParent().getScaleX());
		this.tempParent.setScaleY(this.tempParent.getScaleY() * this.rootParent.getParent().getScaleY());
		SpriterCalculator.translateRelative(this.rootParent.getParent(), this.tempParent);
	}
	
	private SpriterAbstractObject findTimelineObject(SpriterAbstractObject object, SpriterAbstractObject[] objects){
		for(int i = 0; i < objects.length; i++)
			if(objects[i].getTimeline() == object.getTimeline()) return objects[i];
		return null;
	}
	
	private void interpolateAbstractObject(SpriterAbstractObject target, SpriterAbstractObject obj1, SpriterAbstractObject obj2, float startTime, float endTime){
		if(obj2 == null) return;
		target.setX(this.interpolate(obj1.getX(), obj2.getX(), startTime, endTime, this.frame));
		target.setY(this.interpolate(obj1.getY(), obj2.getY(), startTime, endTime, this.frame));
		target.setScaleX(this.interpolate(obj1.getScaleX(), obj2.getScaleX(), startTime, endTime, this.frame));
		target.setScaleY(this.interpolate(obj1.getScaleY(), obj2.getScaleY(), startTime, endTime, this.frame));
		//target.setAngle(this.angleLinear(obj1.getAngle(), obj2.getAngle(), obj2.getSpin(), startTime, endTime));
		target.setAngle(this.interpolateAngle(obj1.getAngle(), obj2.getAngle(), startTime, endTime, this.frame));
	}
	
	private void interpolateSpriterObject(SpriterObject target, SpriterObject obj1, SpriterObject obj2, float startTime, float endTime){
		if(obj2 == null) return;
		this.interpolateAbstractObject(target, obj1, obj2, startTime, endTime);
		target.setPivotX(this.interpolate(obj1.getPivotX(), obj2.getPivotX(), startTime, endTime, this.frame));
		target.setPivotY(this.interpolate(obj1.getPivotY(), obj2.getPivotY(), startTime, endTime, this.frame));
		target.setAlpha(this.interpolateAngle(obj1.getAlpha(), obj2.getAlpha(), startTime, endTime, this.frame));
	}
	
	private void translateRelative(SpriterAbstractObject object, SpriterAbstractObject parent){
		object.setAngle(object.getAngle()*this.flipX*this.flipY + parent.getAngle());
		object.setScaleX(object.getScaleX() * parent.getScaleX());
		object.setScaleY(object.getScaleY() * parent.getScaleY());
		SpriterCalculator.translateRelative(parent, object);
	}

	
	/**
	 * See {@link SpriterCalculator#calculateInterpolation(float, float, float, float, long)}
	 * Can be inherited, to handle other interpolation techniques. Standard is linear interpolation.
	 */
	protected float interpolate(float a, float b, float timeA, float timeB, float currentTime){
		return this.interpolator.interpolate(a, b, timeA, timeB, currentTime);
	}
	
	/**
	 * See {@link SpriterCalculator#calculateInterpolation(float, float, float, float, long)}
	 * Can be inherited, to handle other interpolation techniques. Standard is linear interpolation.
	 */
	protected float interpolateAngle(float a, float b, float timeA, float timeB, float currentTime){
		return this.interpolator.interpolateAngle(a, b, timeA, timeB, currentTime);
	}
	

	
	/**
	 * Calculates the bounding box for the current running animation.
	 * Call this method after updating the spriter player.
	 * @param bone root to start at. Set null, to iterate through all objects.
	 */
	public void calcBoundingBox(SpriterBone bone){
		if(bone == null) this.calcBoundingBoxForAll();
		else{
			bone.boundingBox.set(this.rect);
			for(SpriterObject object: bone.getChildObjects()){
				SpriterPoint[] points = this.tempObjects[object.getId()].getBoundingBox();
				bone.boundingBox.left = Math.min(Math.min(Math.min(Math.min(points[0].x, points[1].x),points[2].x),points[3].x), bone.boundingBox.left);
				bone.boundingBox.right = Math.max(Math.max(Math.max(Math.max(points[0].x, points[1].x),points[2].x),points[3].x), bone.boundingBox.right);
				bone.boundingBox.top = Math.max(Math.max(Math.max(Math.max(points[0].y, points[1].y),points[2].y),points[3].y), bone.boundingBox.top);
				bone.boundingBox.bottom = Math.min(Math.min(Math.min(Math.min(points[0].y, points[1].y),points[2].y),points[3].y), bone.boundingBox.bottom);
			}
			this.rect.set(bone.boundingBox);
			for(SpriterBone child: bone.getChildBones()){
				calcBoundingBox(child);
				bone.boundingBox.set(child.boundingBox);
			}
			this.rect.set(bone.boundingBox);
		}
		this.rect.calculateSize();
	}
	
	private void calcBoundingBoxForAll(){
		for(int i = 0; i < this.currenObjectsToDraw; i++){
			SpriterPoint[] points = this.tempObjects[i].getBoundingBox();
			this.rect.left = Math.min(Math.min(Math.min(Math.min(points[0].x, points[1].x),points[2].x),points[3].x), this.rect.left);
			this.rect.right = Math.max(Math.max(Math.max(Math.max(points[0].x, points[1].x),points[2].x),points[3].x), this.rect.right);
			this.rect.top = Math.max(Math.max(Math.max(Math.max(points[0].y, points[1].y),points[2].y),points[3].y), this.rect.top);
			this.rect.bottom = Math.min(Math.min(Math.min(Math.min(points[0].y, points[1].y),points[2].y),points[3].y), this.rect.bottom);
		}
	}

	
	/**
	 * @return array of moddable objects.
	 */
	public SpriterModObject[] getModdedObjects() {
		return moddedObjects;
	}

	
	/**
	 * @return array of moddable bones.
	 */
	public SpriterModObject[] getModdedBones() {
		return moddedBones;
	}
	
	/**
	 * Searches for the right index for a given bone. Use {@link #getRuntimeObjects()} to acces an object at runtime.
	 * @param bone bone to search at.
	 * @param objectIndex index of the object in the object children list of the given bone. 0 means first object.
	 * @return right index for the object you want access to. -1 if not found.
	 */
	public int findObjectIndexForBone(SpriterBone bone, int objectIndex){
		for(int i = 0; i < this.tempObjects.length && objectIndex >= 0 && objectIndex < bone.getChildObjects().size(); i++)
			if(this.tempObjects[i].equals(bone.getChildObjects().get(objectIndex)))
				return i;
		return -1;
	}
	
	/**
	 * Searches for the right object for a given bone.
	 * @param bone bone to search at.
	 * @param objectIndex index of the object in the object children list of the given bone. 0 means first object.
	 * @return right object you want access to. Null if not found.
	 */
	public SpriterObject findObjectForBone(SpriterBone bone, int objectIndex){
		try{
			return this.tempObjects[this.findObjectIndexForBone(bone, objectIndex)];
		} catch(ArrayIndexOutOfBoundsException e){
			return null;
		}
	}
	
	/**
	 * Searches for the right mod object for a given bone.
	 * @param bone bone to search at.
	 * @param objectIndex index of the object in the object children list of the given bone. 0 means first object.
	 * @return right mod object you want access to. Null if not found.
	 */
	public SpriterModObject findModObjectForBone(SpriterBone bone, int objectIndex){
		try{
			return this.moddedObjects[this.findObjectIndexForBone(bone, objectIndex)];
		} catch(ArrayIndexOutOfBoundsException e){
			return null;
		}
	}
	
	/**
	 * Searches for the right index for a given bone and index.
	 * @param bone bone to search at.
	 * @param boneIndex index of the bone in the object children list of the given bone. 0 means first object.
	 * @return right index for the child bone you want access to. -1 if not found.
	 */
	public int findChildBoneIndexForBone(SpriterBone bone, int boneIndex){
		for(int i = 0; i < this.tempObjects.length && boneIndex >= 0 && boneIndex < bone.getChildBones().size(); i++)
			if(this.tempBones[i].equals(bone.getChildBones().get(boneIndex)))
				return i;
		return -1;
	}
	
	/**
	 * Searches for the right child bone for a given bone and index.
	 * @param bone bone to search at.
	 * @param boneIndex index of the bone in the object children list of the given bone. 0 means first object.
	 * @return right bone you want access to. Null if not found.
	 */
	public SpriterBone findChildBoneForBone(SpriterBone bone, int boneIndex){
		try{
			return this.tempBones[this.findChildBoneIndexForBone(bone, boneIndex)];
		} catch(ArrayIndexOutOfBoundsException e){
			return null;
		}
	}
	
	/**
	 * Searches for the right child bone for a given bone and index.
	 * @param bone bone to search at.
	 * @param boneIndex index of the bone in the object children list of the given bone. 0 means first object.
	 * @return right mod bone you want access to. Null if not found.
	 */
	public SpriterModObject findChildModBoneForBone(SpriterBone bone, int boneIndex){
		try{
			return this.moddedBones[this.findChildBoneIndexForBone(bone, boneIndex)];
		} catch(ArrayIndexOutOfBoundsException e){
			return null;
		}
	}
	
	/**
	 * Searches for the right index for a given bone.
	 * @param bone bone to search at.
	 * @return right index for the mod bone you want access to. -1 if not found.
	 */
	public int findModBoneIndexForBone(SpriterBone bone){
		for(int i = 0; i < this.tempObjects.length; i++)
			if(this.tempBones[i].equals(bone))
				return i;
		return -1;
	}
	
	/**
	 * Searches for the right mod bone for a given bone.
	 * @param bone bone to search at.
	 * @return right mod bone you want access to. Null if not found.
	 */
	public SpriterModObject findModBoneForBone(SpriterBone bone){
		try{
			return this.moddedBones[this.findModBoneIndexForBone(bone)];
		} catch(ArrayIndexOutOfBoundsException e){
			return null;
		}
	}
	
	/**
	 * Changes the current frame to the given one.
	 * @param frame the frame to set
	 */
	public void setFrame(long frame) {
		this.frame = frame;
	}

	/**
	 * @return the current frame
	 */
	public long getFrame() {
		return frame;
	}
	
	/**
	 * @param frameSpeed the frameSpeed to set. Higher value meens playback speed. frameSpeed == 0 means no playback speed.
	 */
	public void setFrameSpeed(int frameSpeed) {
		this.frameSpeed = frameSpeed;
	}
	/**
	 * @return the current frameSpeed
	 */
	public int getFrameSpeed() {
		return frameSpeed;
	}
	
	/**
	 * Flips this around the x-axis.
	 */
	public void flipX(){
		this.flipX *=-1;
		for(SpriterAbstractPlayer player: this.players)
			player.flipX = this.flipX;
	}
	
	/**
	 * @return Indicates whether this is flipped around the x-axis or not. 1 means is not flipped, -1 is flipped.
	 */
	public int getFlipX(){
		return this.flipX;
	}

	
	/**
	 * Flips this around the y-axis.
	 */
	public void flipY(){
		this.flipY *=-1;
		for(SpriterAbstractPlayer player: this.players)
			player.flipY = this.flipY;
	}

	
	/**
	 * @return Indicates whether this is flipped around the y-axis or not. 1 means is not flipped, -1 is flipped.
	 */
	public float getFlipY() {
		return this.flipY;
	}
	
	/**
	 * Changes the angle of this.
	 * @param angle in degrees to rotate all objects , angle = 0 means no rotation.
	 */
	public void setAngle(float angle){
		this.angle = angle;
		this.rootParent.setAngle(this.angle);
	}
	
	/**
	 * @return The current angle in degrees.
	 */
	public float getAngle(){
		return this.angle;
	}

	/**
	 * @return the scale. 1 means not scale. 0.5 means half scale.
	 */
	public float getScale() {
		return scale;
	}

	/**
	 * Scales this to the given value.
	 * @param scale the scale to set, scale = 1.0 normal scale.
	 */
	public void setScale(float scale) {
		this.scale = scale;
		this.rootParent.setScaleX(this.scale*this.flipX);
		this.rootParent.setScaleY(this.scale*this.flipY);
		this.tempParent.setScaleX(this.scale*this.flipX);
		this.tempParent.setScaleY(this.scale*this.flipY);
	}
	
	/**
	 * Sets the center point of this. pivotX = 0, pivotY = 0 means the same rotation point as in the Spriter editor.
	 * @param pivotX
	 * @param pivotY
	 */
	public void setPivot(float pivotX, float pivotY){
		this.pivotX = pivotX;
		this.pivotY = pivotY;
	}
	
	/**
	 * Returns the x center coordinate of this.
	 * @return pivot x
	 */
	public float getPivotX(){
		return this.pivotX;
	}

	
	/**
	 * Returns the y center coordinate of this.
	 * @return pivot y
	 */
	public float getPivotY(){
		return this.pivotY;
	}
	
	/**
	 * @return Returns the current DrawInstruction array
	 */
	public DrawInstruction[] getDrawInstructions(){
		return this.instructions;
	}
	
	/**
	 * Searches for the bone index with the given name and returns the right one
	 * @param name name of the bone.
	 * @return index of the bone if the given name was found, otherwise it returns -1
	 */
	public int getBoneIndexByName(String name){
		for(int i = 0; i < this.tempBones.length; i++)
			if(name.equals(this.tempBones[i].getName())) return i;
		return -1;
	}
	
	/**
	 * Searches for the bone index with the given name and returns the right one
	 * @param name name of the bone.
	 * @return index of the bone if the given name was found, otherwise it returns null
	 */
	public SpriterBone getBoneByName(String name){
		int i = this.getBoneIndexByName(name);
		if(i != -1) return this.tempBones[i];
		else return null;
	}

	/**
	 * @return the current interpolator. See #SpriterInterpolator. You can implement your own one,
	 * which interpolates the animations as you wish.
	 */
	public SpriterInterpolator getInterpolator() {
		return interpolator;
	}

	/**
	 * @param interpolator the interpolator to set. See #SpriterInterpolator. You can implement your own one,
	 * which interpolates the animations as you wish.
	 */
	public void setInterpolator(SpriterInterpolator interpolator) {
		this.interpolator = interpolator;
	}
	
	/**
	 * @return the current bones which where interpolated for the current animation. Bones are not flipped.
	 */
	public final SpriterBone[] getRuntimeBones(){
		return this.tempBones;
	}
	
	/**
	 * @return the current objects which where interpolated for the current animation. Objects are flipped.
	 */
	public final SpriterObject[] getRuntimeObjects(){
		return this.tempObjects;
	}

	/**
	 * @return the rootParent
	 */
	public SpriterAbstractObject getRootParent() {
		return rootParent;
	}
	
	/**
	 * @param rootParent the rootParent to set
	 */
	void setRootParent(SpriterAbstractObject rootParent) {
		this.rootParent = rootParent;
	}

	/**
	 * @param rootParent the rootParent to set
	 */
	void changeRootParent(SpriterAbstractObject rootParent) {
		this.rootParent.setParent(rootParent);
	}
	
	/**
	 * @return the current z-index. This gets relevant if you attach a #SpriterAbstractPlayer to another.
	 */
	public int getZIndex(){
		return this.zIndex;
	}
	
	/**
	 * Meant to change the drawing order if this player is held by another #SpriterAbstractPlayer.
	 * @param zIndex  Higher means that the object will be drawn later.
	 */
	public void setZIndex(int zIndex){
		this.zIndex = zIndex;
	}
	
	/**
	 * Attaches a given player to this.
	 * @param player indicates the player which has to be attached.
	 * @param root indicates the object the attached player has to follow.
	 * Set to {@link #getRootParent()} to attach the player to the same position as this player.
	 */
	public void attachPlayer(SpriterAbstractPlayer player, SpriterAbstractObject root){
		this.players.add(player);
		player.changeRootParent(root);
	}
	
	/**
	 * Removes the given player from this one.
	 * @param player indicates the player which has to be removed.
	 */
	public void removePlayer(SpriterAbstractPlayer player){
		this.players.remove(player);
		player.changeRootParent(null);
	}
	
	public List<SpriterAbstractPlayer> getAttachedPlayers(){
		return this.players;
	}
	
	/**
	 * Indicates whether the given player is attached to this or not.
	 * @param player the player to ask after.
	 * @return true if player is attached or not.
	 */
	public boolean containsPlayer(SpriterAbstractPlayer player){
		return this.players.contains(player);
	}
	
	public void updateBone(SpriterBone bone){
		if(bone.hasParent()){
			//if(this.moddedBones[bone.getId()].isActive()) bone.setAngle(this.lastFrame.getBones()[bone.getId()].getAngle()+this.tempBones[bone.getParentId()].getAngle());
			SpriterCalculator.translateRelative(this.tempBones[bone.getParentId()], this.lastFrame.getBones()[bone.getId()].getX(),
					this.lastFrame.getBones()[bone.getId()].getY(), bone);
		}
	}
	
	public void updateRecursively(SpriterBone bone){
		this.updateBone(bone);
		for(SpriterBone child: bone.getChildBones())
			this.updateRecursively(this.tempBones[child.getId()]);
	}
	
	public SpriterRectangle getBoundingBox(){
		return this.rect;
	}
	
	public int getObjectsToDraw(){
		return this.currenObjectsToDraw;
	}
}
