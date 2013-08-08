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

import com.brashmonkey.spriter.Spriter;
import com.brashmonkey.spriter.SpriterKeyFrameProvider;
import com.brashmonkey.spriter.animation.SpriterAnimation;
import com.brashmonkey.spriter.animation.SpriterKeyFrame;
import com.brashmonkey.spriter.file.FileLoader;
import com.discobeard.spriter.dom.Animation;
import com.discobeard.spriter.dom.Entity;
import com.discobeard.spriter.dom.SpriterData;

import java.util.HashMap;
import java.util.List;

/**
 * A SpriterPlayer is the core of a spriter animation.
 * Here you can get as many information as you need.
 * 
 * SpriterPlayer plays the given data with the method {@link #update(float, float)}. You have to call this method by your own in your main game loop.
 * SpriterPlayer updates the frames by its own. See {@link #setFrameSpeed(int)} for setting the playback speed.
 * 
 * The animations can be drawn by {@link #draw()} which draws all objects with your own implemented Drawer.
 * 
 * Accessing bones and animations via names is also possible. See {@link #getAnimationIndexByName(String)} and {@link #getBoneIndexByName(String)}.
 * You can modify the whole animation or only bones at runtime with some fancy methods provided by this class.
 * Have a look at {@link #setAngle(float)}, {@link #flipX()}, {@link #flipY()}, {@link #setScale(float)} for animation moddification.
 * And see {@link #setBoneAngle(int, float)}, {@link #setBoneScaleX(int, float)}, {@link #setBoneScaleY(int, float)}.
 * 
 * All stuff you set you can also receive by the corresponding getters ;) .
 * 
 * @author Trixt0r
 */

public class SpriterPlayer extends SpriterAbstractPlayer{

	private static HashMap<Entity, SpriterPlayer> loaded = new HashMap<Entity, SpriterPlayer>();
	protected Entity entity;
	private SpriterAnimation animation;
	private int transitionSpeed = 30;
	private int animationIndex = 0;
	private int currentKey = 0;
	SpriterKeyFrame lastRealFrame, firstKeyFrame, secondKeyFrame;
	boolean transitionTempFixed = true;
	private int fixCounter = 0;
	private int fixMaxSteps = 100;
	protected boolean updateObjects = true, updateBones = true;
	
	
	/**
	 * Constructs a new SpriterPlayer object which animates the given Spriter entity.
	 * @param data {@link SpriterData} which provides a method to load all needed data to animate. See {@link Spriter#getSpriter(String, com.spriter.file.FileLoader)} for mor information.
	 * @param entityIndex The entity which should be handled by this player.
	 * @param loader The loader which has loaded all necessary sprites for the scml file.
	 */
	public SpriterPlayer(SpriterData data, Entity entity, FileLoader<?> loader){
		super(loader, null);
		this.entity = entity;
		this.frame = 0;
		if(!alreadyLoaded(entity)){
			this.animations = SpriterKeyFrameProvider.generateKeyFramePool(data, entity);
			loaded.put(entity, this);
		}
		else this.animations = loaded.get(entity).animations;
		this.generateData();
		this.animation = this.animations.get(0);
		this.firstKeyFrame = this.animation.frames.get(0);
		this.update(0, 0);
	}
	
	/**
	 * Constructs a new SpriterPlayer object which animates the given Spriter entity.
	 * @param data {@link SpriterData} which provides a method to load all needed data to animate. See {@link Spriter#getSpriter(String, com.spriter.file.FileLoader)} for mor information.
	 * @param entityIndex The index of the entity which should be handled by this player.
	 * @param loader The loader which has loaded all necessary sprites for the scml file.
	 */
	public SpriterPlayer(SpriterData data, int entityIndex, FileLoader<?> loader){
		this(data, data.getEntity().get(entityIndex), loader);
	}
	
	/**
	 * Constructs a new SpriterPlayer object which animates the given Spriter entity.
	 * @param data {@link Spriter} which provides a method to load all needed data to animate. See {@link Spriter#getSpriter(String, com.spriter.file.FileLoader)} for mor information.
	 * @param entityIndex The index of the entity which should be handled by this player.
	 * @param loader The loader which has loaded all necessary sprites for the scml file.
	 */
	public SpriterPlayer(Spriter spriter, int entityIndex, FileLoader<?> loader){
		this(spriter.getSpriterData(), spriter.getSpriterData().getEntity().get(entityIndex), loader);
	}
	
	@Override
	protected void step(float xOffset, float yOffset){
		//Fetch information
		//SpriterAnimation anim = this.animation;
		List<SpriterKeyFrame> frameList = this.animation.frames;
		if(this.transitionFixed && this.transitionTempFixed){
			//anim = this.animation;
			/*if(this.frameSpeed >= 0){
				firstKeyFrame = frameList.get(this.currentKey);
				secondKeyFrame = frameList.get((this.currentKey+1)%frameList.size());
			}
			else{
				secondKeyFrame = frameList.get(this.currentKey);
				firstKeyFrame = frameList.get((this.currentKey+1)%frameList.size());
			}
			//Update
			this.frame += this.frameSpeed;
			if (this.frame > secondKeyFrame.getTime() && this.frameSpeed >= 0){
				this.currentKey = (this.currentKey+1)%frameList.size();
				this.frame = frameList.get(this.currentKey).getTime();
			}
			else if(this.frame < firstKeyFrame.getTime()){
				this.currentKey = ((this.currentKey-1)+frameList.size())%frameList.size();
				this.frame = frameList.get(this.currentKey).getTime();
			}
			this.currenObjectsToDraw = firstKeyFrame.getObjects().length;
			if(this.updateBones) this.transformBones(firstKeyFrame, secondKeyFrame, xOffset, yOffset);
			if(this.updateObjects) this.transformObjects(firstKeyFrame, secondKeyFrame, xOffset, yOffset);*/
			
			if(this.updateBones) this.transformBones(this.animation,  xOffset, yOffset);
			if(this.updateObjects) this.transformObjects(this.animation,  xOffset, yOffset);
		}
		else{
			//this.transitionAnimation.frames.remove(0);
			//this.transitionAnimation.frames.remove(1);
			firstKeyFrame = frameList.get(0);
			secondKeyFrame = this.lastRealFrame;
			float temp =(float)(this.fixCounter)/(float)this.fixMaxSteps;
			this.frame = this.lastRealFrame.getTime()+(long)(this.fixMaxSteps*temp);
			this.fixCounter= Math.min(this.fixCounter+this.transitionSpeed,this.fixMaxSteps);
			//Update
			if(this.fixCounter == this.fixMaxSteps){
				this.frame = 0;
				this.fixCounter = 0;
				if(this.lastRealFrame.equals(this.lastFrame)) this.transitionFixed = true;
				else this.transitionTempFixed = true;
				firstKeyFrame.setTime(0);
			}
			this.currenObjectsToDraw = firstKeyFrame.getObjects().length;
			if(this.updateBones) this.transformBones(firstKeyFrame, secondKeyFrame,  xOffset, yOffset);
			if(this.updateObjects) this.transformObjects(firstKeyFrame, secondKeyFrame,  xOffset, yOffset);
		}
		//this.currenObjectsToDraw = firstKeyFrame.getObjects().length;
		//Interpolate
		//if(this.updateBones) this.transformBones(anim, xOffset, yOffset);
		//if(this.updateObjects) this.transformObjects(this.animation, xOffset, yOffset);
	}
	
	/**
	 * Sets the animationIndex for this to the given animationIndex.
	 * This method can make sure that the switching between to animations is smooth.
	 * By setting transitionSpeed and transitionSteps to appropriate values, you can have nice transitions between two animations.
	 * Setting transitionSpeed to 1 and transitionSteps to 20 means, that this player will need 20 steps to translate the current animation to the given one.
	 * @param animationIndex Index of animation to set. Get the index with {@link #getAnimationIndexByName(String)}.
	 * @param transitionSpeed Speed for the switch between the current animation and the one which has been set.
	 * @param transitionSteps Steps needed for the transition
	 */
	public void setAnimatioIndex(int animationIndex, int transitionSpeed, int transitionSteps) throws RuntimeException{
		if(animationIndex >= this.entity.getAnimation().size() || animationIndex < 0) throw new RuntimeException("The given animation index does not exist: "+animationIndex+"\n"+
		"Index range goes from 0 to " + (this.entity.getAnimation().size()-1));
		if(this.animationIndex != animationIndex){
			if(this.transitionFixed){
				this.lastRealFrame = this.lastFrame;
				this.transitionFixed = false;
				this.transitionTempFixed = true;
			}
			else{
				this.lastRealFrame = this.lastTempFrame;
				this.transitionTempFixed = false;
				this.transitionFixed = true;
			}
			this.transitionSpeed = transitionSpeed;
			this.fixMaxSteps = transitionSteps;
			this.lastRealFrame.setTime(this.frame+1);
			this.animation = this.animations.get(animationIndex);
			this.animation.frames.get(0).setTime(this.frame+1+this.fixMaxSteps);
			//this.currentKey = 0;
			this.fixCounter = 0;
			this.animationIndex = animationIndex;
		}
	}
	
	public void setAnimationIndex(int animationIndex) throws RuntimeException{
		this.setAnimatioIndex(animationIndex, 1, 1);
	}
	
	public void setAnimation(String animationName, int transitionSpeed, int transitionSteps){
		int index = getAnimationIndexByName(animationName);
		if(index >= this.entity.getAnimation().size() || index < 0) throw new RuntimeException("The animation \""+ animationName+"\" does not exist!");
		this.setAnimatioIndex(index, transitionSpeed, transitionSteps);
	}
	
	/**
	 * Searches for the animation index with the given name and returns the right one
	 * @param name name of the animation.
	 * @return index of the animation if the given name was found, otherwise it returns -1
	 */
	public int getAnimationIndexByName(String name){
		Animation anim = this.getAnimationByName(name);
		if(this.getAnimationByName(name) == null) return -1;
		else return anim.getId();
	}
	
	/**
	 * Searches for the animation with the given name and returns the right one
	 * @param name of the animation.
	 * @return nimation if the given name was found, otherwise it returns null.
	 */
	public Animation getAnimationByName(String name){
		List<Animation> anims = this.entity.getAnimation();
		for(Animation anim: anims)
			if(anim.getName().equals(name)) return anim;
		return null;
	}
	
	/**
	 * @return current animation index, which has same numbering as in the scml file.
	 */
	public int getAnimationIndex(){
		return this.animationIndex;
	}

	/**
	 * @return the entity, which was read from the scml file you loaded before.
	 */
	public Entity getEntity() {
		return entity;
	}

	/**
	 * @param entity the entity to set
	 */
	public void setEntity(Entity entity) {
		this.entity = entity;
	}

	
	/**
	 * @return the current animation with all its raw data which was read from the scml file.
	 */
	public SpriterAnimation getAnimation() {
		return animation;
	}
	
	private static boolean alreadyLoaded(Entity entity){
		return loaded.containsKey(entity);
	}
}
