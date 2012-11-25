package com.discobeard.spriter.objects;

import java.util.Arrays;

import com.discobeard.spriter.SpriterCalculator;
import com.discobeard.spriter.dom.Animation;
import com.discobeard.spriter.draw.DrawInstruction;
import com.discobeard.spriter.file.Reference;
import com.discobeard.spriter.mergers.SpriterKeyFrameBuilder;

public class SpriterAnimation {

	final private SpriterKeyFrame[] keyFrames;
	final private long length;
	final private boolean shouldLoop;

	private int currentKey = 0;

	public static SpriterAnimation createAnimation(Animation from) {
		return new SpriterAnimation(new SpriterKeyFrameBuilder().buildKeyFrameArray(from), from.getLength(),
				Boolean.getBoolean("false"));
	}

	public static SpriterAnimation createAnimation(Animation from, boolean isLooping) {
		from.setLooping(isLooping);
		return new SpriterAnimation(new SpriterKeyFrameBuilder().buildKeyFrameArray(from), from.getLength(),
				Boolean.getBoolean("false"));
	}

	public SpriterAnimation(SpriterKeyFrame[] keyFrames, long length, boolean loop) {
		this.keyFrames = keyFrames;
		this.length = length;
		this.shouldLoop = loop;
	}

	public SpriterKeyFrame[] getKeyFrames() {
		return keyFrames;
	}

	public DrawInstruction[] createDrawInstructions(SpriterKeyFrame key, float xOffset, float yOffset) {
		DrawInstruction[] drawInstructions = new DrawInstruction[key.getObjects().length];

		for (int o = 0; o < key.getObjects().length; o++) {
			SpriterObject obj = key.getObjects()[o];
			drawInstructions[o] = new DrawInstruction(new Reference(obj.getFolder(), obj.getFile()), obj.getX()
					+ xOffset, obj.getY() + yOffset, obj.getPivotX(), obj.getPivotY(), obj.getScaleX(),
					obj.getScaleY(), obj.getAngle(), obj.getAlpha());
		}

		return drawInstructions;
	}

	public DrawInstruction[] createDrawInstructions(SpriterKeyFrame firstFrame, SpriterKeyFrame secondFrame,
			long currentAnimationTime, float xOffset, float yOffset) {

		DrawInstruction[] drawInstructions = new DrawInstruction[firstFrame.getObjects().length];

		SpriterBone[] tempBones = this.interpolateBones(firstFrame, secondFrame, currentAnimationTime);
		
		//Sort sprites by z_index.
		Arrays.sort(firstFrame.getObjects());
		Arrays.sort(secondFrame.getObjects());

		for (int i = 0; i < firstFrame.getObjects().length; i++) {

			SpriterObject obj1 = firstFrame.getObjects()[i];

			if (!obj1.isTransientObject()) {
				SpriterObject obj2 = secondFrame.getObjects()[i];

				float obj2Angle = obj2.getAngle();

				if (obj1.getSpin() > -1) {
					if ((obj2Angle - obj1.getAngle()) < 0) 
						obj2Angle += 360;
				} else if ((obj2Angle - obj1.getAngle()) >= 0) 
						obj2Angle -= 360;

				float x = SpriterCalculator.calculateInterpolation(obj1.getX(), obj2.getX(), firstFrame.getStartTime(), secondFrame.getStartTime(),
						currentAnimationTime);
				float y = SpriterCalculator.calculateInterpolation(obj1.getY(), obj2.getY(), firstFrame.getStartTime(), secondFrame.getStartTime(),
						currentAnimationTime);

				float scale_x = SpriterCalculator.calculateInterpolation(obj1.getScaleX(), obj2.getScaleX(), firstFrame.getStartTime(),
						secondFrame.getStartTime(), currentAnimationTime);

				float scale_y = SpriterCalculator.calculateInterpolation(obj1.getScaleY(), obj2.getScaleY(), firstFrame.getStartTime(),
						secondFrame.getStartTime(), currentAnimationTime);

				float rotation = SpriterCalculator.calculateAngleInterpolation(obj1.getAngle(), obj2Angle, firstFrame.getStartTime(), secondFrame.getStartTime(),
						currentAnimationTime);
				float alpha = SpriterCalculator.calculateInterpolation(obj1.getAlpha(), obj2.getAlpha(), firstFrame.getStartTime(),
						secondFrame.getStartTime(), currentAnimationTime);

				if (obj1.getParent() != null) {
					rotation = rotation + tempBones[obj1.getParent()].getAngle();
					scale_x = scale_x * tempBones[obj1.getParent()].getScaleX();
					scale_y = scale_y * tempBones[obj1.getParent()].getScaleY();

					float[] newstuff = SpriterCalculator.rotatePoint(tempBones[obj1.getParent()], x, y);
					x = newstuff[0];
					y = newstuff[1];

				}

				drawInstructions[i] = new DrawInstruction(new Reference(obj1.getFolder(), obj1.getFile()), x + xOffset,
						y + yOffset, obj1.getPivotX(), obj1.getPivotY(), scale_x, scale_y, rotation, alpha);
			}
			else
			{
				drawInstructions[i] = new DrawInstruction(new Reference(obj1.getFolder(), obj1.getFile()), obj1.getX() + xOffset,
						obj1.getY() + yOffset, obj1.getPivotX(), obj1.getPivotY(), obj1.getScaleX(), obj1.getScaleY(), obj1.getAngle(), obj1.getAlpha());
			}
		}

		return drawInstructions;
	}
	
	/**
	 * Interpolates the bones for this animation.
	 * @param key1 first keyframe
	 * @param key2 second keyframe
	 * @param currentAnimationTime
	 * @param key2StartTime
	 * @return interpolated SpriterBone array
	 */
	private SpriterBone[] interpolateBones(SpriterKeyFrame key1, SpriterKeyFrame key2, long currentAnimationTime){
		
		SpriterBone[] tempBones = new SpriterBone[key1.getBones().length];
		for (int b = 0; b < key1.getBones().length; b++) {

			SpriterBone bone1 = key1.getBones()[b];
			SpriterBone bone2 = key2.getBones()[b];
	
			float bone2Angle = bone2.getAngle();
	
			if (bone1.getSpin() == 1) {
				if ((bone2.getAngle() - bone1.getAngle()) < 0) {
					bone2Angle += 360;
				}
			} else if (bone1.getSpin() == -1) {
				if ((bone2.getAngle() - bone1.getAngle()) >= 0) {
					bone2Angle -= 360;
				}
			}
	
			Float x = SpriterCalculator.calculateInterpolation(bone1.getX(), bone2.getX(), key1.getStartTime(), key2.getStartTime(),
					currentAnimationTime);
			Float y = SpriterCalculator.calculateInterpolation(bone1.getY(), bone2.getY(), key1.getStartTime(), key2.getStartTime(),
					currentAnimationTime);
			Float scaleX = SpriterCalculator.calculateInterpolation(bone1.getScaleX(), bone2.getScaleX(), key1.getStartTime(),
					key2.getStartTime(), currentAnimationTime);
			Float scaleY = SpriterCalculator.calculateInterpolation(bone1.getScaleY(), bone2.getScaleY(), key1.getStartTime(),
					key2.getStartTime(), currentAnimationTime);
			float rotation = SpriterCalculator.calculateAngleInterpolation(bone1.getAngle(), bone2Angle, key1.getStartTime(), key2.getStartTime(),
					currentAnimationTime);
	
			SpriterBone bone = new SpriterBone();
			bone.setAngle(rotation);
			bone.setId(bone1.getId());
			bone.setParent(bone1.getParent());
			bone.setScaleX(scaleX);
			bone.setScaleY(scaleY);
			bone.setX(x);
			bone.setY(y);
	
			tempBones[bone1.getId()] = bone;

		}

		for (SpriterBone bone : tempBones) {
			if (bone.getParent() != null) {
				bone.setAngle(bone.getAngle() + tempBones[bone.getParent()].getAngle());
				bone.setScaleX(bone.getScaleX() * tempBones[bone.getParent()].getScaleX());
				bone.setScaleY(bone.getScaleY() * tempBones[bone.getParent()].getScaleY());
				float[] newstuff = SpriterCalculator.rotatePoint(tempBones[bone.getParent()], bone.getX(), bone.getY());
				bone.setX(newstuff[0]);
				bone.setY(newstuff[1]);
			}
		}
		return tempBones;
	}

	public long getLength() {
		return length;
	}

	public boolean shouldLoop() {
		return shouldLoop;
	}

	public int getCurrentKey() {
		return currentKey;
	}

	public void setCurrentKey(int currentKey) {
		this.currentKey = currentKey % this.getKeyFrames().length;
	}
}
