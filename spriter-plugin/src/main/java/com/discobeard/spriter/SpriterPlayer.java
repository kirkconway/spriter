package com.discobeard.spriter;

import com.discobeard.spriter.dom.Animation;
import com.discobeard.spriter.dom.SpriterData;
import com.discobeard.spriter.draw.AbstractDrawer;
import com.discobeard.spriter.draw.DrawInstruction;
import com.discobeard.spriter.file.Reference;
import com.discobeard.spriter.objects.SpriterBone;
import com.discobeard.spriter.objects.SpriterKeyFrame;
import com.discobeard.spriter.objects.SpriterModObject;
import com.discobeard.spriter.objects.SpriterObject;

import java.util.List;

public class SpriterPlayer{

	private SpriterData spriterData;
	private Animation animation;
	private long frame = 0;
	private int frameSpeed = 30;
	private int animationIndex = 0;
	private int currentKey = 0;
	private DrawInstruction[] instructions;
	private List<SpriterKeyFrame[]> keyframes;
	private SpriterBone[] tempBones;
	private AbstractDrawer<?> drawer;
	private int currenObjectsToDraw;
	private int flipX = 1, flipY = 1;
	private float angle = 0;
	private float scale = 1f;
	private SpriterBone rootParent;
	private SpriterModObject[] moddedObjects,moddedBones;
	
	
	
	public SpriterPlayer(SpriterData spriterData, AbstractDrawer<?> drawer,List<SpriterKeyFrame[]> keyframes){
		this.spriterData = spriterData;
		this.keyframes = keyframes;
		this.drawer = drawer;
		this.generatePool();
		this.animation = this.spriterData.getEntity().get(0).getAnimation().get(0);
		this.rootParent = new SpriterBone();
		this.rootParent.setScaleX(this.scale);
		this.rootParent.setScaleY(this.scale);
	}
	
	private void generatePool(){
		this.instructions = new DrawInstruction[SpriterKeyFrameProvider.MAX_OBJECTS];
		this.moddedObjects = new SpriterModObject[this.instructions.length];
		for(int i = 0; i < this.instructions.length; i++){
			this.instructions[i] = new DrawInstruction(new Reference(0,0),0,0,0,0,0,0,0,0);
			this.moddedObjects[i] = new SpriterModObject();
		}
		this.tempBones = new SpriterBone[SpriterKeyFrameProvider.MAX_BONES];
		this.moddedBones = new SpriterModObject[this.tempBones.length];
		for(int i = 0; i < this.tempBones.length; i++){
			this.tempBones[i] = new SpriterBone();
			this.moddedBones[i] = new SpriterModObject();
		}
	}
	
	/**
	 * Draws the current animation
	 */
	public void draw(){
		for(int i = 0; i< this.currenObjectsToDraw; i++){
			DrawInstruction dI = this.instructions[i];
			this.drawer.draw(dI);
		}
	}
	
	/**
	 * Updates this player at current animation index.
	 * @param xOffset
	 * @param yOffset
	 */
	public void update(float xOffset, float yOffset){
		//Fetch information
		SpriterKeyFrame[] keyframes = this.keyframes.get(animationIndex);
		SpriterKeyFrame firstKeyFrame = keyframes[this.currentKey];
		SpriterKeyFrame secondKeyFrame = keyframes[(this.currentKey+1)%keyframes.length];
		this.currenObjectsToDraw = firstKeyFrame.getObjects().length;
		//Interpolate
		this.interpolateBones(firstKeyFrame, secondKeyFrame, xOffset, yOffset);		
		this.interpolateObjects(firstKeyFrame, secondKeyFrame, xOffset, yOffset);	
		
		//Update
		if(this.frame > this.animation.getLength())
			this.frame = 0;
		this.frame += this.frameSpeed;
		if (this.frame > keyframes[this.currentKey].getEndTime()){
			this.currentKey = (this.currentKey+1)%keyframes.length;
			this.frame = keyframes[this.currentKey].getStartTime();
		}
	}
	
	private void interpolateObjects(SpriterKeyFrame firstFrame, SpriterKeyFrame secondFrame, float xOffset, float yOffset) {
		float[] newstuff;
		for (int i = 0; i < this.currenObjectsToDraw; i++) {

			SpriterObject obj1 = firstFrame.getObjects()[i];
			DrawInstruction dI = this.instructions[i];

			if (!obj1.isTransientObject()) {
					SpriterObject obj2 = null;
					boolean found = false;
					for(int j = 0; j < secondFrame.getObjects().length && !found; j++){
						obj2 = secondFrame.getObjects()[j];
						found = secondFrame.getObjects()[j].getTimeline() == obj1.getTimeline();
					}
					float x=obj1.getX(),y=obj1.getY(),scaleX=obj1.getScaleX(),scaleY=obj1.getScaleY(),rotation=obj1.getAngle(),alpha=obj1.getAlpha();
					if(obj2 != null){
						x = SpriterCalculator.calculateInterpolation(obj1.getX(), obj2.getX(), firstFrame.getStartTime(),
								secondFrame.getStartTime(),	this.frame);
						y = SpriterCalculator.calculateInterpolation(obj1.getY(), obj2.getY(), firstFrame.getStartTime(),
								secondFrame.getStartTime(),	this.frame);
		
						scaleX = SpriterCalculator.calculateInterpolation(obj1.getScaleX(), obj2.getScaleX(), firstFrame.getStartTime(),
								secondFrame.getStartTime(), this.frame);
		
						scaleY = SpriterCalculator.calculateInterpolation(obj1.getScaleY(), obj2.getScaleY(), firstFrame.getStartTime(),
								secondFrame.getStartTime(), this.frame);
		
						rotation = SpriterCalculator.calculateAngleInterpolation(obj1.getAngle(), obj2.getAngle(), firstFrame.getStartTime(),
								secondFrame.getStartTime(),	this.frame);
						alpha = SpriterCalculator.calculateInterpolation(obj1.getAlpha(), obj2.getAlpha(), firstFrame.getStartTime(),
								secondFrame.getStartTime(), this.frame);
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
				if(secondFrame.getBones()[j].getTimeline() == bone1.getTimeline()){
					bone2 = secondFrame.getBones()[j];
					found = true;
				}
			}
			float x=bone1.getX(),y=bone1.getY(),scaleX=bone1.getScaleX(),scaleY=bone1.getScaleY(),rotation=bone1.getAngle();
			if(bone2 != null){
				x = SpriterCalculator.calculateInterpolation(bone1.getX(), bone2.getX(), firstFrame.getStartTime(), secondFrame.getStartTime(), this.frame);
				y = SpriterCalculator.calculateInterpolation(bone1.getY(), bone2.getY(), firstFrame.getStartTime(), secondFrame.getStartTime(), this.frame);
				scaleX = SpriterCalculator.calculateInterpolation(bone1.getScaleX(), bone2.getScaleX(), firstFrame.getStartTime(), secondFrame.getStartTime(), this.frame);
				scaleY = SpriterCalculator.calculateInterpolation(bone1.getScaleY(), bone2.getScaleY(), firstFrame.getStartTime(), secondFrame.getStartTime(), this.frame);
				rotation = SpriterCalculator.calculateAngleInterpolation(bone1.getAngle(), bone2.getAngle(), firstFrame.getStartTime(), secondFrame.getStartTime(), this.frame);
			}
			rotation += this.moddedBones[i].getAngle();
			scaleX *= this.moddedBones[i].getScaleX();
			scaleY *= this.moddedBones[i].getScaleY();
			this.tempBones[i].setAngle((this.tempBones[i].getSpin() == -1) ? 360-rotation: rotation);
			this.tempBones[i].setId(bone1.getId());
			this.tempBones[i].setParent(bone1.getParent());
			this.tempBones[i].setScaleX(scaleX);
			this.tempBones[i].setScaleY(scaleY);
			this.tempBones[i].setX(x);
			this.tempBones[i].setY(y);
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
	 * Sets the current animation index for this player.
	 * @param animationIndex
	 */
	public void setAnimatioIndex(int animationIndex){
		if(this.animationIndex != animationIndex){
			this.frame = 0;
			this.currentKey = 0;
			this.animationIndex = animationIndex;
			this.animation = this.spriterData.getEntity().get(0).getAnimation().get(animationIndex);
		}
	}
	
	public int getAnimationIndexByName(String name){
		List<Animation> anims = this.spriterData.getEntity().get(0).getAnimation();
		for(Animation anim: anims)
			if(anim.getName().equals(name)) return anim.getId();
		return 0;
	}
	
	public int getBoneIndexByName(String name){
		for(int i = 0; i < this.moddedBones.length; i++)
			if(name.equals(this.moddedBones[i].getName())) return i;
		return 0;
	}
	
	public void setBoneAngle(int index, float angle){
		this.moddedBones[index].setAngle(angle);
	}
	
	public void setBoneScaleX(int index, float scaleX){
		this.moddedBones[index].setScaleX(scaleX);
	}
	
	public void setBoneScaleY(int index, float scaleY){
		this.moddedBones[index].setScaleX(scaleY);
	}
	
	public float getBoneAngle(int index){
		return this.moddedBones[index].getAngle();
	}
	
	public float getBoneScaleX(int index){
		return this.moddedBones[index].getScaleX();
	}
	
	public float getBoneScaleY(int index){
		return this.moddedBones[index].getScaleY();
	}
	
	public float getBoneX(int index){
		return this.moddedBones[index].getX();
	}
	
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
	public SpriterData getSpriterData() {
		return spriterData;
	}

	/**
	 * @param spriterData the spriterData to set
	 */
	public void setSpriterData(SpriterData spriterData) {
		this.spriterData = spriterData;
	}

	/**
	 * @return the frame
	 */
	public long getFrame() {
		return frame;
	}

	/**
	 * @param frame the frame to set
	 */
	public void setFrame(long frame) {
		this.frame = frame;
	}

	/**
	 * @return the frameSpeed
	 */
	public int getFrameSpeed() {
		return frameSpeed;
	}

	/**
	 * @param frameSpeed the frameSpeed to set
	 */
	public void setFrameSpeed(int frameSpeed) {
		this.frameSpeed = frameSpeed;
	}

	
	/**
	 * @return the anim
	 */
	public Animation getAnimation() {
		return animation;
	}

	
	/**
	 * @param anim the anim to set
	 */
	public void setAnimation(Animation anim) {
		this.animation = anim;
	}
	
	public int getFlipX(){
		return this.flipX;
	}
	
	public void flipX(){
		this.flipX *=-1;
	}
	
	public void flipY(){
		this.flipY *=-1;
	}
	
	public void setAngle(float angle){
		this.rootParent.setAngle(this.angle);
		this.angle = angle;
	}
	
	public float getAngle(){
		return this.angle;
	}

	public float getFlipY() {
		return this.flipY;
	}

	/**
	 * @return the scaleX
	 */
	public float getScale() {
		return scale;
	}

	/**
	 * @param scaleX the scaleX to set
	 */
	public void setScale(float scale) {
		this.scale = scale;
		this.rootParent.setScaleX(this.scale);
		this.rootParent.setScaleY(this.scale);
	}
	
	public void setPivot(float pivotX, float pivotY){
		this.rootParent.setX(pivotX);
		this.rootParent.setY(pivotY);
	}
	
	public float getPivotX(){
		return this.rootParent.getX();
	}
	
	public float getPivotY(){
		return this.rootParent.getY();
	}
}
