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

package com.brashmonkey.spriter.mergers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.brashmonkey.spriter.SpriterCalculator;
import com.brashmonkey.spriter.animation.SpriterAnimation;
import com.brashmonkey.spriter.animation.SpriterKeyFrame;
import com.brashmonkey.spriter.interpolation.SpriterLinearInterpolator;
import com.brashmonkey.spriter.objects.SpriterAbstractObject;
//import com.brashmonkey.spriter.converters.SpriterObjectConverter;
import com.brashmonkey.spriter.objects.SpriterBone;
import com.brashmonkey.spriter.objects.SpriterObject;
import com.discobeard.spriter.dom.Animation;
//import com.discobeard.spriter.dom.AnimationObject;
import com.discobeard.spriter.dom.AnimationObjectRef;
import com.discobeard.spriter.dom.BoneRef;
import com.discobeard.spriter.dom.Key;
import com.discobeard.spriter.dom.MainLine;
import com.discobeard.spriter.dom.TimeLine;

public class SpriterAnimationBuilder {

	final private SpriterBoneMerger boneMerger = new SpriterBoneMerger();
	final private SpriterObjectMerger objectMerger = new SpriterObjectMerger();
	//final private SpriterObjectConverter objectConverter = new SpriterObjectConverter();
	
	HashMap<SpriterBone, Integer> bonesToTween;
	HashMap<SpriterObject, Integer> objectsToTween;
	
	public SpriterAnimation buildAnimation(Animation animation){
		
		MainLine mainline = animation.getMainline();
		List<TimeLine> timeLines = animation.getTimeline();
		
		List<Key> keyFrames =  mainline.getKey();
		
		bonesToTween = new HashMap<SpriterBone, Integer>();
		objectsToTween = new HashMap<SpriterObject, Integer>();
		
		SpriterAnimation spriterAnimation = new SpriterAnimation(animation.getId(), animation.getName(), animation.getLength());
		
		for(int k=0;k<keyFrames.size();k++){
			Key mainlineKey = keyFrames.get(k);
			
			List<SpriterObject> tempObjects = new ArrayList<SpriterObject>();
			List<SpriterBone> tempBones = new ArrayList<SpriterBone>();
			
			SpriterKeyFrame frame = new SpriterKeyFrame();
			frame.setTime(mainlineKey.getTime());
			frame.setId(mainlineKey.getId());
			
			for(BoneRef boneRef : mainlineKey.getBoneRef()){
				TimeLine timeline = timeLines.get(boneRef.getTimeline());
				Key timelineKey = timeline.getKey().get(boneRef.getKey());
				SpriterBone bone = boneMerger.merge(boneRef, timelineKey);
				bone.setName(timeline.getName());
				if(mainlineKey.getTime() != timelineKey.getTime()) bonesToTween.put(bone, k);
				else tempBones.add(bone);
				/*if(key.getTime() == timeLines.get(boneRef.getTimeline()).getKey().get(boneRef.getKey()).getTime()){
					if(bone.hasParent()){
						SpriterBone parent = null;
						for(int i = 0; i< spriterAnimation.frames() && parent == null; i++){
							parent = this.searchForParentBone(spriterAnimation.frames.get(i), bone.getParentId());
							bone.setParent(parent);
						}
					}*/
				//}
			}
			
			for(AnimationObjectRef objectRef : mainlineKey.getObjectRef()){
				TimeLine timeline = timeLines.get(objectRef.getTimeline());
				Key timelineKey = timeline.getKey().get(objectRef.getKey());
				SpriterObject object = objectMerger.merge(objectRef, timelineKey);
				object.setName(timeline.getName());
				if(mainlineKey.getTime() != timelineKey.getTime()) objectsToTween.put(object, k);
				else tempObjects.add(object);
				/*if(key.getTime() == timeLines.get(objectRef.getTimeline()).getKey().get(objectRef.getKey()).getTime()){
					if(object.hasParent()){
						SpriterBone parent = null;
						for(int i = 0; i< spriterAnimation.frames() && parent == null; i++){
							parent = this.searchForParentBone(spriterAnimation.frames.get(i), object.getParentId());
							object.setParent(parent);
						}
					}*/
				//}
			}
			
			/*for(AnimationObject object : key.getObject()){
				tempObjects.add(objectConverter.convert(object));
			}*/
			frame.setObjects(tempObjects.toArray(new SpriterObject[tempObjects.size()]));
			frame.setBones(tempBones.toArray(new SpriterBone[tempBones.size()]));
			
			spriterAnimation.frames.add(frame);
		}
		
		this.tweenBones(spriterAnimation);
		this.tweenObjects(spriterAnimation);
		
		return spriterAnimation;
	}
	
	public void tweenBones(SpriterAnimation animation){
		for(Entry<SpriterBone, Integer> entry: bonesToTween.entrySet()){
			SpriterBone toTween = entry.getKey();
			SpriterKeyFrame frame = animation.frames.get(entry.getValue());
			long time = frame.getTime();
			SpriterKeyFrame currentFrame = animation.getPreviousFrameForBone(toTween, time);
			SpriterKeyFrame nextFrame = animation.getNextFrameFor(toTween, currentFrame, 1);
			if(nextFrame != currentFrame){
				SpriterBone bone1 = currentFrame.getBoneFor(toTween), bone2 = nextFrame.getBoneFor(toTween);
				this.interpolateAbstractObject(toTween, bone1, bone2, currentFrame.getTime(), nextFrame.getTime(), time);
			}
			SpriterBone[] bones = new SpriterBone[frame.getBones().length+1];
			for(int i = 0; i < bones.length-1; i++)
				bones[i] = frame.getBones()[i];
			bones[bones.length-1] = toTween;
			frame.setBones(bones);
		}
	}
	
	public void tweenObjects(SpriterAnimation animation){
		for(Entry<SpriterObject, Integer> entry: objectsToTween.entrySet()){
			SpriterObject toTween = entry.getKey();
			SpriterKeyFrame frame = animation.frames.get(entry.getValue());
			long time = frame.getTime();
			SpriterKeyFrame currentFrame = animation.getPreviousFrameForObject(toTween, time);
			SpriterKeyFrame nextFrame = animation.getNextFrameFor(toTween, currentFrame, 1);
			if(nextFrame != currentFrame){
				SpriterObject object1 = currentFrame.getObjectFor(toTween), object2 = nextFrame.getObjectFor(toTween);
				this.interpolateSpriterObject(toTween, object1, object2, currentFrame.getTime(), nextFrame.getTime(), time);
			}
			SpriterObject[] objects = new SpriterObject[frame.getObjects().length+1];
			for(int i = 0; i < objects.length-1; i++)
				objects[i] = frame.getObjects()[i];
			objects[objects.length-1] = toTween;
			frame.setObjects(objects);
		}
	}
	
	private void interpolateAbstractObject(SpriterAbstractObject target, SpriterAbstractObject obj1, SpriterAbstractObject obj2, float startTime, float endTime, float frame){
		if(obj2 == null) return;
		target.setX(this.interpolate(obj1.getX(), obj2.getX(), startTime, endTime, frame));
		target.setY(this.interpolate(obj1.getY(), obj2.getY(), startTime, endTime, frame));
		target.setScaleX(this.interpolate(obj1.getScaleX(), obj2.getScaleX(), startTime, endTime, frame));
		target.setScaleY(this.interpolate(obj1.getScaleY(), obj2.getScaleY(), startTime, endTime, frame));
		target.setAngle(this.interpolateAngle(obj1.getAngle(), obj2.getAngle(), startTime, endTime, frame));
	}
	
	private void interpolateSpriterObject(SpriterObject target, SpriterObject obj1, SpriterObject obj2, float startTime, float endTime, float frame){
		if(obj2 == null) return;
		this.interpolateAbstractObject(target, obj1, obj2, startTime, endTime, frame);
		target.setPivotX(this.interpolate(obj1.getPivotX(), obj2.getPivotX(), startTime, endTime, frame));
		target.setPivotY(this.interpolate(obj1.getPivotY(), obj2.getPivotY(), startTime, endTime, frame));
		target.setAlpha(this.interpolateAngle(obj1.getAlpha(), obj2.getAlpha(), startTime, endTime, frame));
	}
	
	/**
	 * See {@link SpriterCalculator#calculateInterpolation(float, float, float, float, long)}
	 * Can be inherited, to handle other interpolation techniques. Standard is linear interpolation.
	 */
	protected float interpolate(float a, float b, float timeA, float timeB, float currentTime){
		return SpriterLinearInterpolator.interpolator.interpolate(a, b, timeA, timeB, currentTime);
	}
	
	/**
	 * See {@link SpriterCalculator#calculateInterpolation(float, float, float, float, long)}
	 * Can be inherited, to handle other interpolation techniques. Standard is linear interpolation.
	 */
	protected float interpolateAngle(float a, float b, float timeA, float timeB, float currentTime){
		return SpriterLinearInterpolator.interpolator.interpolateAngle(a, b, timeA, timeB, currentTime);
	}
	
	/*private SpriterBone searchForParentBone(SpriterKeyFrame frame, Integer parentId){
		if(frame == null) return null;
		for(SpriterBone bone: frame.getBones())
			if(bone.getParentId().equals(parentId)) return bone;
		return null;
	}*/
}
