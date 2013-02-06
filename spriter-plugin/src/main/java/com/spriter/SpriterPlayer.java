package com.spriter;

import com.discobeard.spriter.dom.Animation;
import com.discobeard.spriter.dom.Entity;
import com.discobeard.spriter.dom.SpriterData;
import com.spriter.draw.AbstractDrawer;
import com.spriter.draw.DrawInstruction;
import com.spriter.file.Reference;
import com.spriter.objects.SpriterBone;
import com.spriter.objects.SpriterKeyFrame;
import com.spriter.objects.SpriterModObject;
import com.spriter.objects.SpriterObject;

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
 *
 */

public class SpriterPlayer{

	protected Entity entity;
	private Animation animation;
	private long frame = 0;
	private int frameSpeed = 30, transitionSpeed = 30;
	private int animationIndex = 0;
	private int currentKey = 0;
	private DrawInstruction[] instructions;
	private List<SpriterKeyFrame[]> keyframes;
	private SpriterBone[] tempBones;
	protected AbstractDrawer<?> drawer;
	private int currenObjectsToDraw;
	private int flipX = 1, flipY = 1;
	private float angle = 0;
	private float scale = 1f;
	private SpriterBone rootParent;
	private SpriterModObject[] moddedObjects,moddedBones;
	private SpriterKeyFrame lastFrame, lastTempFrame, lastRealFrame;
	private boolean transitionFixed = true;
	private int fixCounter = 0;
	private int fixMaxSteps = 100;
	private Interpolator interpolator;
	
	
	/**
	 * Constructs a new SpriterPlayer object which animates the given SpriterData.
	 * @param entity {@link Spriter} which provides a method to load all needed data to animate. See {@link Spriter#getSpriter(String, com.spriter.file.FileLoader)} for mor information.
	 * @param drawer {@link AbstractDrawer} which you have to implement on your own.
	 * @param keyframes A list of SpriterKeyFrame arrays. See {@link SpriterKeyFrameProvider#generateKeyFramePool(SpriterData)} to get the list.
	 */
	public SpriterPlayer(Entity entity, AbstractDrawer<?> drawer,List<SpriterKeyFrame[]> keyframes){
		this.entity = entity;
		this.keyframes = keyframes;
		this.drawer = drawer;
		this.generateData();
		this.animation = this.entity.getAnimation().get(0);
		this.rootParent = new SpriterBone();
		this.rootParent.setScaleX(this.scale);
		this.rootParent.setScaleY(this.scale);
		this.lastFrame = new SpriterKeyFrame();
		this.lastTempFrame = new SpriterKeyFrame();
		
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
		this.interpolator = new LinearInterpolator();
		this.lastRealFrame = this.lastFrame;
		
	}
	
	/**
	 * Generates data which is necessary to animate all animations as intended.
	 */
	private void generateData(){
		int maxObjects = 0, maxBones = 0;
		for(SpriterKeyFrame[] key: this.keyframes){
			for(SpriterKeyFrame k: key){
				maxBones = Math.max(k.getBones().length, maxBones);
				maxObjects = Math.max(k.getObjects().length, maxObjects);
			}
		}
		this.instructions = new DrawInstruction[maxObjects];
		this.moddedObjects = new SpriterModObject[this.instructions.length];
		for(int i = 0; i < this.instructions.length; i++){
			this.instructions[i] = new DrawInstruction(new Reference(0,0),0,0,0,0,0,0,0,0);
			this.moddedObjects[i] = new SpriterModObject();
		}
		this.tempBones = new SpriterBone[maxBones];
		this.moddedBones = new SpriterModObject[this.tempBones.length];
		for(int i = 0; i < this.tempBones.length; i++){
			this.tempBones[i] = new SpriterBone();
			this.moddedBones[i] = new SpriterModObject();
		}
	}
	
	/**
	 * Draws the current animation with the provided {@link AbstractDrawer}, which is library specific.
	 */
	public void draw(){
		for(int i = 0; i< this.currenObjectsToDraw; i++){
			DrawInstruction dI = this.instructions[i];
			this.drawer.draw(dI);
		}
	}
	
	/**
	 * Updates this player and translates the animation to xOffset and yOffset.
	 * Frame is updated by previous set frame speed (See {@link #setFrameSpeed(long)} ).
	 * This method makes sure that the keyframes get played back.
	 * @param xOffset
	 * @param yOffset
	 */
	public void update(float xOffset, float yOffset){
		//Fetch information
		SpriterKeyFrame[] keyframes = this.keyframes.get(animationIndex);
		SpriterKeyFrame firstKeyFrame; 
		SpriterKeyFrame secondKeyFrame;
		if(this.transitionFixed){
			if(this.frameSpeed >= 0){
				firstKeyFrame = keyframes[this.currentKey];
				secondKeyFrame = keyframes[(this.currentKey+1)%keyframes.length];
			}
			else{
				secondKeyFrame = keyframes[this.currentKey];
				firstKeyFrame = keyframes[((this.currentKey-1)+keyframes.length)%keyframes.length];
			}
			//Update
			this.frame += this.frameSpeed;
			if (this.frame > firstKeyFrame.getEndTime() && this.frameSpeed > 0){
				this.currentKey = (this.currentKey+1)%keyframes.length;
				this.frame = keyframes[this.currentKey].getStartTime();
			}
			else if(this.frame < firstKeyFrame.getStartTime()){
				this.currentKey = ((this.currentKey-1)+keyframes.length)%keyframes.length;
				this.frame = keyframes[this.currentKey].getStartTime();
			}
		}
		else{
			firstKeyFrame = keyframes[0];
			secondKeyFrame = this.lastRealFrame;
			float temp =(float)(this.fixCounter)/(float)this.fixMaxSteps;
			this.frame = this.lastRealFrame.getStartTime()+(long)(this.fixMaxSteps*temp);
			this.fixCounter= Math.min(this.fixCounter+this.transitionSpeed,this.fixMaxSteps);
			//Update
			if(this.fixCounter == this.fixMaxSteps){
				this.frame = 0;
				this.fixCounter = 0;
				this.transitionFixed = true;
				firstKeyFrame.setStartTime(0);
			}
		}
		this.currenObjectsToDraw = firstKeyFrame.getObjects().length;
		//Interpolate
		this.interpolateBones(firstKeyFrame, secondKeyFrame, xOffset, yOffset);		
		this.interpolateObjects(firstKeyFrame, secondKeyFrame, xOffset, yOffset);
	}
	
	/**
	 * Interpolates the objects of firstFrame and secondFrame.
	 * @param firstFrame
	 * @param secondFrame
	 * @param xOffset
	 * @param yOffset
	 */
	private void interpolateObjects(SpriterKeyFrame firstFrame, SpriterKeyFrame secondFrame, float xOffset, float yOffset) {
		float[] newstuff;
		for (int i = 0; i < this.currenObjectsToDraw; i++) {

			SpriterObject obj1 = firstFrame.getObjects()[i];
			DrawInstruction dI = this.instructions[i];

			if (!obj1.isTransientObject()) {
				SpriterObject obj2 = null;
				boolean found = false;
				for(int j = 0; j < secondFrame.getObjects().length && !found; j++){
					found = secondFrame.getObjects()[j].getTimeline() == obj1.getTimeline();
					if(found) obj2 = secondFrame.getObjects()[j];
				}
				float x=obj1.getX(),y=obj1.getY(),scaleX=obj1.getScaleX(),scaleY=obj1.getScaleY(),rotation=obj1.getAngle(),alpha=obj1.getAlpha();
				if(obj2 != null){
					x = this.interpolate(obj1.getX(), obj2.getX(), firstFrame.getStartTime(), secondFrame.getStartTime(), this.frame);
					y = this.interpolate(obj1.getY(), obj2.getY(), firstFrame.getStartTime(), secondFrame.getStartTime(), this.frame);
					scaleX = this.interpolate(obj1.getScaleX(), obj2.getScaleX(), firstFrame.getStartTime(), secondFrame.getStartTime(), this.frame);
					scaleY = this.interpolate(obj1.getScaleY(), obj2.getScaleY(), firstFrame.getStartTime(), secondFrame.getStartTime(), this.frame);
					rotation = this.interpolateAngle(obj1.getAngle(), obj2.getAngle(), firstFrame.getStartTime(), secondFrame.getStartTime(), this.frame);
					alpha = this.interpolate(obj1.getAlpha(), obj2.getAlpha(), firstFrame.getStartTime(), secondFrame.getStartTime(), this.frame);
				}
				if(this.transitionFixed){
					this.lastFrame.getObjects()[i].setX(x);
					this.lastFrame.getObjects()[i].setY(y);
					this.lastFrame.getObjects()[i].setScaleX(scaleX);
					this.lastFrame.getObjects()[i].setScaleY(scaleY);
					this.lastFrame.getObjects()[i].setAngle(rotation);
					this.lastFrame.getObjects()[i].setAlpha(alpha);
					this.lastFrame.getObjects()[i].setId(obj1.getId());
					this.lastFrame.getObjects()[i].setTimeline((found) ? obj1.getTimeline() : -1);
					this.lastFrame.getObjects()[i].setFile(obj1.getFile());
					this.lastFrame.getObjects()[i].setFolder(obj1.getFolder());
					this.lastFrame.getObjects()[i].setPivotX(obj1.getPivotX());
					this.lastFrame.getObjects()[i].setPivotY(obj1.getPivotY());
					this.lastFrame.getObjects()[i].setSpin(obj1.getSpin());
				}
				else{
					this.lastTempFrame.getObjects()[i].setX(x);
					this.lastTempFrame.getObjects()[i].setY(y);
					this.lastTempFrame.getObjects()[i].setScaleX(scaleX);
					this.lastTempFrame.getObjects()[i].setScaleY(scaleY);
					this.lastTempFrame.getObjects()[i].setAngle(rotation);
					this.lastTempFrame.getObjects()[i].setAlpha(alpha);
					this.lastTempFrame.getObjects()[i].setId(obj1.getId());
					this.lastTempFrame.getObjects()[i].setTimeline((found) ? obj1.getTimeline() : -1);
					this.lastTempFrame.getObjects()[i].setFile(obj1.getFile());
					this.lastTempFrame.getObjects()[i].setFolder(obj1.getFolder());
					this.lastTempFrame.getObjects()[i].setPivotX(obj1.getPivotX());
					this.lastTempFrame.getObjects()[i].setPivotY(obj1.getPivotY());
					this.lastTempFrame.getObjects()[i].setSpin(obj1.getSpin());
				}
				
				if (obj1.getParent() != null) {
					rotation += tempBones[obj1.getParent()].getAngle();
					scaleX *= tempBones[obj1.getParent()].getScaleX();
					scaleY *= tempBones[obj1.getParent()].getScaleY();
					newstuff = SpriterCalculator.rotatePoint(tempBones[obj1.getParent()], x, y);
					x = newstuff[0];
					y = newstuff[1];
				}
				else{
					rotation += this.angle;
					scaleX *= this.scale;
					scaleY *= this.scale;
					newstuff = SpriterCalculator.rotatePoint(this.rootParent, x, y);
					x = newstuff[0];
					y = newstuff[1];
				}
				
				dI.ref.folder = obj1.getFolder();
				dI.ref.file = obj1.getFile();
				dI.x =  x*this.flipX+xOffset;
				dI.y =  y*this.flipY+yOffset;
				dI.scaleX =  scaleX*this.flipX;
				dI.scaleY =  scaleY*this.flipY;
				dI.pivotX =  obj1.getPivotX();
				dI.pivotY = obj1.getPivotY();
				dI.angle = rotation*this.flipX*this.flipY;
				dI.alpha = alpha;
					
			}
			else
			{
				dI.ref.folder = obj1.getFolder();
				dI.ref.file = obj1.getFile();
				dI.pivotX = obj1.getPivotX();
				dI.pivotY = obj1.getPivotY();
				dI.scaleX = obj1.getScaleX()*this.scale;
				dI.scaleY = obj1.getScaleY()*this.scale;
				dI.angle = obj1.getAngle()+this.angle;
				dI.alpha = obj1.getAlpha();
				newstuff = SpriterCalculator.rotatePoint(this.rootParent, dI.x, dI.y);
				dI.x = newstuff[0];
				dI.y = newstuff[1];
				dI.x = dI.x*this.flipX+xOffset;
				dI.y = dI.y*this.flipY+yOffset;
			}
		}
	}
	
	/**
	 * Interpolates the bones for this animation.
	 * @param firstFrame first keyframe
	 * @param secondFrame second keyframe
	 * @param currentAnimationTime
	 * @param key2StartTime
	 * @return interpolated SpriterBone array
	 */
	private void interpolateBones(SpriterKeyFrame firstFrame, SpriterKeyFrame secondFrame, float xOffset, float yOffset){
		for (int i = 0; i < firstFrame.getBones().length; i++) {
			SpriterBone bone1 = firstFrame.getBones()[i];
			this.tempBones[i].setName(bone1.getName());
			this.moddedBones[i].setName(bone1.getName());
			SpriterBone bone2 = null;
			boolean found = false;
			for(int j = 0; j < secondFrame.getBones().length && !found; j++){//Get the right bone to interpolate with
				found = secondFrame.getBones()[j].getTimeline() == bone1.getTimeline();
				if(found) bone2 = secondFrame.getBones()[j];
			}
			float x=bone1.getX(),y=bone1.getY(),scaleX=bone1.getScaleX(),scaleY=bone1.getScaleY(),rotation=bone1.getAngle();
			if(bone2 != null){
				x = this.interpolate(bone1.getX(), bone2.getX(), firstFrame.getStartTime(), secondFrame.getStartTime(), this.frame);
				y = this.interpolate(bone1.getY(), bone2.getY(), firstFrame.getStartTime(), secondFrame.getStartTime(), this.frame);
				scaleX = this.interpolate(bone1.getScaleX(), bone2.getScaleX(), firstFrame.getStartTime(), secondFrame.getStartTime(), this.frame);
				scaleY = this.interpolate(bone1.getScaleY(), bone2.getScaleY(), firstFrame.getStartTime(), secondFrame.getStartTime(), this.frame);
				rotation = this.interpolateAngle(bone1.getAngle(), bone2.getAngle(), firstFrame.getStartTime(), secondFrame.getStartTime(), this.frame);
			}
			rotation += this.moddedBones[i].getAngle();
			scaleX *= this.moddedBones[i].getScaleX();
			scaleY *= this.moddedBones[i].getScaleY();
			this.tempBones[i].setAngle(rotation);
			this.tempBones[i].setScaleX(scaleX);
			this.tempBones[i].setScaleY(scaleY);
			this.tempBones[i].setX(x); 
			this.tempBones[i].setY(y);
			this.tempBones[i].setId(bone1.getId());
			this.tempBones[i].setTimeline(bone1.getTimeline());
			this.tempBones[i].setParent(bone1.getParent()); 
			this.tempBones[i].setName(bone1.getName());
			this.tempBones[i].setSpin(bone1.getSpin());
			if(this.transitionFixed){
				this.tempBones[i].copyValuesTo(this.lastFrame.getBones()[i]);
				if(!found) this.tempBones[i].setTimeline(-1);
			}
			else{
				this.tempBones[i].copyValuesTo(this.lastTempFrame.getBones()[i]);
				if(!found) this.tempBones[i].setTimeline(-1);
			}
			if (this.tempBones[i].getParent() != null) {
				this.tempBones[i].setAngle(this.tempBones[i].getAngle() + tempBones[this.tempBones[i].getParent()].getAngle());
				this.tempBones[i].setScaleX(this.tempBones[i].getScaleX() * tempBones[this.tempBones[i].getParent()].getScaleX());
				this.tempBones[i].setScaleY(this.tempBones[i].getScaleY() * tempBones[this.tempBones[i].getParent()].getScaleY());
				float[] newstuff = SpriterCalculator.rotatePoint(tempBones[this.tempBones[i].getParent()], this.tempBones[i].getX(), this.tempBones[i].getY());
				this.tempBones[i].setX(newstuff[0]);
				this.tempBones[i].setY(newstuff[1]);
			}
			else{
				this.tempBones[i].setAngle(this.tempBones[i].getAngle() + this.rootParent.getAngle());
				this.tempBones[i].setScaleX(this.tempBones[i].getScaleX() * this.rootParent.getScaleX());
				this.tempBones[i].setScaleY(this.tempBones[i].getScaleY() * this.rootParent.getScaleY());
				float[] newstuff = SpriterCalculator.rotatePoint(this.rootParent, this.tempBones[i].getX(), this.tempBones[i].getY());
				this.tempBones[i].setX(newstuff[0]);
				this.tempBones[i].setY(newstuff[1]);
			}
			this.moddedBones[i].setX(this.tempBones[i].getX()+xOffset);
			this.moddedBones[i].setY(this.tempBones[i].getY()+yOffset);
		}
	} 
	
	/**
	 * Switches the current animation to the given one, with smooth transition if required.
	 * Note: Smooth transitions can only be accomplished, if the bone and object structure of the previous and next animation are the same.
	 * Otherwise you will get weird effects. So beware!
	 * @param animationIndex new animation
	 * @param transitionSpeed indicates how fast the animations have to switch
	 * @param transitionSteps indicates how many steps are required to switch between the animations. transitionSteps == 0 means no transition.
	 */
	public void setAnimatioIndex(int animationIndex, int transitionSpeed, int transitionSteps){
		if(this.animationIndex != animationIndex){
			if(this.transitionFixed) this.lastRealFrame = this.lastFrame;
			else this.lastRealFrame = this.lastTempFrame;
			this.transitionFixed = false;
			this.transitionSpeed = transitionSpeed;
			this.fixMaxSteps = transitionSteps;
			this.lastRealFrame.setStartTime(this.frame+1);
			this.lastRealFrame.setEndTime(this.frame+this.fixMaxSteps-1);
			this.keyframes.get(animationIndex)[0].setStartTime(this.frame+1+this.fixMaxSteps);
			this.currentKey = 0;
			this.fixCounter = 0;
			this.animationIndex = animationIndex;
			this.animation = this.entity.getAnimation().get(animationIndex);
		}
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
	
	public Animation getAnimationByName(String name){
		List<Animation> anims = this.entity.getAnimation();
		for(Animation anim: anims)
			if(anim.getName().equals(name)) return anim;
		return null;
	}
	
	/**
	 * Searches for the bone index with the given name and returns the right one
	 * @param name name of the bone.
	 * @return index of the bone if the given name was found, otherwise it returns -1
	 */
	public int getBoneIndexByName(String name){
		for(int i = 0; i < this.moddedBones.length; i++)
			if(name.equals(this.moddedBones[i].getName())) return i;
		return -1;
	}
	
	/**
	 * Modifies the bone's angle with the given bone index
	 * @param index index of the bone
	 * @param angle new angle of the given bone, angle = 0 means no moddification
	 */
	public void setBoneAngle(int index, float angle){
		this.moddedBones[index].setAngle(angle);
	}
	
	/**
	 * Modifies the bone's scale x with the given bone index
	 * @param index index of the bone
	 * @param scaleX new scale of the given bone, scaleX = 1 means no moddification
	 */
	public void setBoneScaleX(int index, float scaleX){
		this.moddedBones[index].setScaleX(scaleX);
	}
	
	/**
	 * Modifies the bone's scale y with the given bone index
	 * @param index index of the bone
	 * @param scaleY new scale of the given bone, scaleY = 1 means no moddification
	 */
	public void setBoneScaleY(int index, float scaleY){
		this.moddedBones[index].setScaleX(scaleY);
	}
	
	/**
	 * @param index of the bone (the hierarchy of the bones is the same as in Spriter)
	 * @return angle of the bone at given index (modified angle is included in return statement).
	 */
	public float getBoneAngle(int index){
		return this.moddedBones[index].getAngle();
	}
	
	/**
	 * @param index of the bone (the hierarchy of the bones is the same as in Spriter)
	 * @return  x scale of the bone at given index (modified x scale is included in return statement).
	 */
	public float getBoneScaleX(int index){
		return this.moddedBones[index].getScaleX();
	}

	
	/**
	 * @param index of the bone (the hierarchy of the bones is the same as in Spriter)
	 * @return  y scale of the bone at given index (modified y scale is included in return statement).
	 */
	public float getBoneScaleY(int index){
		return this.moddedBones[index].getScaleY();
	}
	
	/**
	 * @param index of the bone (the hierarchy of the bones is the same as in Spriter)
	 * @return absolutt x position of the bone at given index (xOffset is included)
	 */
	public float getBoneX(int index){
		return this.moddedBones[index].getX();
	}

	/**
	 * @param index of the bone (the hierarchy of the bones is the same as in Spriter)
	 * @return absolute y position of the bone at given index (yOffset is included)
	 */
	public float getBoneY(int index){
		return this.moddedBones[index].getY();
	}
	
	/**
	 * @return current animation index
	 */
	public int getAnimationIndex(){
		return this.animationIndex;
	}
	
	
	/**
	 * Returns the current DrawInstruction array
	 * @param animationIndex
	 * @return
	 */
	public DrawInstruction[] getDrawInstructions(){
		return this.instructions;
	}

	/**
	 * @return the spriterData
	 */
	public Entity getEntity() {
		return entity;
	}

	/**
	 * @param spriterData the spriterData to set
	 */
	public void setSpriterData(Entity entity) {
		this.entity = entity;
	}

	/**
	 * Changes the current frame to the given one. Note: You can't change the frame while this object is switching to another animation.
	 * @param frame the frame to set
	 */
	public void setFrame(long frame) {
		this.frame = frame;
	}

	/**
	 * @return the frame
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
	 * @return the frameSpeed
	 */
	public int getFrameSpeed() {
		return frameSpeed;
	}

	
	/**
	 * @return the anim
	 */
	public Animation getAnimation() {
		return animation;
	}
	
	/**
	 * Flips this around the x-axis.
	 */
	public void flipX(){
		this.flipX *=-1;
	}
	
	/**
	 * @return indicates whether this is flipped around the x-axis or not. 1 means is not flipped.
	 */
	public int getFlipX(){
		return this.flipX;
	}

	
	/**
	 * Flips this around the y-axis.
	 */
	public void flipY(){
		this.flipY *=-1;
	}

	
	/**
	 * @return indicates whether this is flipped around the y-axis or not. 1 means is not flipped.
	 */
	public float getFlipY() {
		return this.flipY;
	}
	
	/**
	 * Changes the angle of this.
	 * @param angle to rotate all objects , angle = 0 means no rotation
	 */
	public void setAngle(float angle){
		this.rootParent.setAngle(this.angle);
		this.angle = angle;
	}
	
	public float getAngle(){
		return this.angle;
	}

	/**
	 * @return the scaleX
	 */
	public float getScale() {
		return scale;
	}

	/**
	 * Scales this to the given value.
	 * @param scale the scale to set, scale = 1.0f normal scale
	 */
	public void setScale(float scale) {
		this.scale = scale;
		this.rootParent.setScaleX(this.scale);
		this.rootParent.setScaleY(this.scale);
	}
	
	/**
	 * Sets the center point of this. pivotX = 0, pivotY = 0 means the same rotation point as in Spriter.
	 * @param pivotX
	 * @param pivotY
	 */
	public void setPivot(float pivotX, float pivotY){
		this.rootParent.setX(pivotX);
		this.rootParent.setY(pivotY);
	}
	
	/**
	 * Returns the x center coordinate of this.
	 * @return pivot x
	 */
	public float getPivotX(){
		return this.rootParent.getX();
	}

	
	/**
	 * Returns the y center coordinate of this.
	 * @return pivot y
	 */
	public float getPivotY(){
		return this.rootParent.getY();
	}
	
	public void setInterpolator(Interpolator interpolator){
		this.interpolator = interpolator;
	}
	
	public Interpolator getInterpolator(){
		return this.interpolator;
	}
	
	/**
	 * See {@link SpriterCalculator#calculateInterpolation(float, float, float, float, long)}
	 * Can be inherited, to handle other interpolation techniques. Standard is linear interpolation.
	 */
	private float interpolate(float a, float b, float timeA, float timeB, long currentTime){
		return this.interpolator.interpolate(a, b, timeA, timeB, currentTime);
	}
	
	/**
	 * See {@link SpriterCalculator#calculateInterpolation(float, float, float, float, long)}
	 * Can be inherited, to handle other interpolation techniques. Standard is linear interpolation.
	 */
	private float interpolateAngle(float a, float b, float timeA, float timeB, long currentTime){
		return this.interpolator.interpolateAngle(a, b, timeA, timeB, currentTime);
	}
}
