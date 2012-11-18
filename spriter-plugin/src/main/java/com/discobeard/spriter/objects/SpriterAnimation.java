package com.discobeard.spriter.objects;

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
					+ xOffset, obj.getY() + yOffset, obj.getPivotX(), obj.getPivotY(), obj.getScale_x(),
					obj.getScale_y(), obj.getAngle(), obj.getAlpha());
		}

		return drawInstructions;
	}

	public DrawInstruction[] createDrawInstructions(SpriterKeyFrame key1, SpriterKeyFrame key2,
			long currentAnimationTime, long key2StartTime, float xOffset, float yOffset) {

		DrawInstruction[] drawInstructions = new DrawInstruction[key1.getObjects().length];

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

			Float x = calculateInterpolation(bone1.getX(), bone2.getX(), key1.getStartTime(), key2StartTime,
					currentAnimationTime);// + xOffset;
			Float y = calculateInterpolation(bone1.getY(), bone2.getY(), key1.getStartTime(), key2StartTime,
					currentAnimationTime);// + yOffset;
			Float scaleX = calculateInterpolation(bone1.getScaleX(), bone2.getScaleX(), key1.getStartTime(),
					key2StartTime, currentAnimationTime);
			Float scaleY = calculateInterpolation(bone1.getScaleY(), bone2.getScaleY(), key1.getStartTime(),
					key2StartTime, currentAnimationTime);
			float rotation = calculateInterpolation(bone1.getAngle(), bone2Angle, key1.getStartTime(), key2StartTime,
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
				float[] newstuff = rotatePoint(tempBones[bone.getParent()], bone.getX(), bone.getY());
				bone.setX(newstuff[0]);
				bone.setY(newstuff[1]);

			}
		}

		for (int o = 0; o < key1.getObjects().length; o++) {

			SpriterObject obj1 = key1.getObjects()[o];

			if (!obj1.isTransientObject()) {
				SpriterObject obj2 = key2.getObjects()[o];

				float obj2Angle = obj2.getAngle();

				if (obj1.getSpin() > -1) {
					if ((obj2.getAngle() - obj1.getAngle()) < 0) {
						obj2Angle += 360;
					}
				} else {
					if ((obj2.getAngle() - obj1.getAngle()) >= 0) {
						obj2Angle -= 360;
					}
				}

				float x = calculateInterpolation(obj1.getX(), obj2.getX(), key1.getStartTime(), key2StartTime,
						currentAnimationTime);// + xOffset;
				float y = calculateInterpolation(obj1.getY(), obj2.getY(), key1.getStartTime(), key2StartTime,
						currentAnimationTime);// + yOffset;

				float scale_x = calculateInterpolation(obj1.getScale_x(), obj2.getScale_x(), key1.getStartTime(),
						key2StartTime, currentAnimationTime);

				float scale_y = calculateInterpolation(obj1.getScale_y(), obj2.getScale_y(), key1.getStartTime(),
						key2StartTime, currentAnimationTime);

				float rotation = calculateInterpolation(obj1.getAngle(), obj2Angle, key1.getStartTime(), key2StartTime,
						currentAnimationTime);
				float alpha = calculateInterpolation(obj1.getAlpha(), obj2.getAlpha(), key1.getStartTime(),
						key2StartTime, currentAnimationTime);

				if (obj1.getParent() != null) {
					rotation = rotation + tempBones[obj1.getParent()].getAngle();
					scale_x = scale_x * tempBones[obj1.getParent()].getScaleX();
					scale_y = scale_x * tempBones[obj1.getParent()].getScaleY();

					float[] newstuff = rotatePoint(tempBones[obj1.getParent()], x, y);
					x = newstuff[0];
					y = newstuff[1];

				}

				drawInstructions[o] = new DrawInstruction(new Reference(obj1.getFolder(), obj1.getFile()), x + xOffset,
						y + yOffset, obj1.getPivotX(), obj1.getPivotY(), scale_x, scale_y, rotation, alpha);
			}
			else
			{
				drawInstructions[o] = new DrawInstruction(new Reference(obj1.getFolder(), obj1.getFile()), obj1.getX() + xOffset,
						obj1.getY() + yOffset, obj1.getPivotX(), obj1.getPivotY(), obj1.getScale_x(), obj1.getScale_y(), obj1.getAngle(), obj1.getAlpha());
			}
		}

		return drawInstructions;
	}

	public DrawInstruction[] createDrawInstructions(SpriterKeyFrame key1, SpriterKeyFrame key2,
			long currentAnimationTime, float xOffset, float yOffset) {
		return createDrawInstructions(key1, key2, currentAnimationTime, key2.getStartTime(), xOffset, yOffset);
	}

	private float[] rotatePoint(SpriterBone parent, float childx, float childy) {

		float px = childx * (parent.getScaleX());
		float py = childy * (parent.getScaleY());

		float s = (float) Math.sin(Math.toRadians(parent.getAngle()));
		float c = (float) Math.cos(Math.toRadians(parent.getAngle()));
		float xnew = (px * c) - (py * s);
		float ynew = (px * s) + (py * c);
		xnew += parent.getX();
		ynew += parent.getY();

		return new float[] { xnew, ynew };
	}

	private float calculateInterpolation(float a, float b, float timeA, float timeB, long currentTime) {
		return a + ((b - a) * ((currentTime - timeA) / (timeB - timeA)));
	}

	public long getLenght() {
		return length;
	}

	public boolean shouldLoop() {
		return shouldLoop;
	}

	public int getCurrentKey() {
		return currentKey;
	}

	public void setCurrentKey(int currentKey) {
		this.currentKey = currentKey;
	}
}
