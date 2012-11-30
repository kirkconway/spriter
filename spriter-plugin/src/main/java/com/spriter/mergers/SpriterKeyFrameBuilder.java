package com.spriter.mergers;

import java.util.ArrayList;
import java.util.List;

import com.discobeard.spriter.dom.Animation;
import com.discobeard.spriter.dom.AnimationObject;
import com.discobeard.spriter.dom.AnimationObjectRef;
import com.discobeard.spriter.dom.BoneRef;
import com.discobeard.spriter.dom.Key;
import com.discobeard.spriter.dom.MainLine;
import com.discobeard.spriter.dom.TimeLine;
import com.spriter.converters.SpriterObjectConverter;
import com.spriter.objects.SpriterBone;
import com.spriter.objects.SpriterKeyFrame;
import com.spriter.objects.SpriterObject;

public class SpriterKeyFrameBuilder {

	final private SpriterBoneMerger boneMerger = new SpriterBoneMerger();
	final private SpriterObjectMerger objectMerger = new SpriterObjectMerger();
	final private SpriterObjectConverter objectConverter = new SpriterObjectConverter();
	
	public SpriterKeyFrame[] buildKeyFrameArray(Animation animation){
		
		MainLine mainline = animation.getMainline();
		List<TimeLine> timeLines = animation.getTimeline();
		
		List<Key> keyFrames =  mainline.getKey();

		SpriterKeyFrame[] spriterKeyFrames = new SpriterKeyFrame[keyFrames.size()];
		
		for(int k=0;k<keyFrames.size();k++){
			Key key = keyFrames.get(k);
			
			List<SpriterObject> tempObjects = new ArrayList<SpriterObject>();
			List<SpriterBone> tempBones = new ArrayList<SpriterBone>();
			
			for(BoneRef boneRef : key.getBoneRef()){
				tempBones.add(boneMerger.merge(boneRef, timeLines.get(boneRef.getTimeline()).getKey().get(boneRef.getKey())));
			}
			
			for(AnimationObjectRef objectRef : key.getObjectRef()){
				tempObjects.add(objectMerger.merge(objectRef, timeLines.get(objectRef.getTimeline()).getKey().get(objectRef.getKey())));
			}
			
			for(AnimationObject object : key.getObject()){
				tempObjects.add(objectConverter.convert(object));
			}
			
			spriterKeyFrames[k] = new SpriterKeyFrame();
			spriterKeyFrames[k].setObjects(tempObjects.toArray(new SpriterObject[tempObjects.size()]));
			spriterKeyFrames[k].setBones(tempBones.toArray(new SpriterBone[tempBones.size()]));
			
			spriterKeyFrames[k].setStartTime(key.getTime());
			spriterKeyFrames[k].setEndTime(k<keyFrames.size()-1 ? keyFrames.get(k+1).getTime()-1 : animation.getLength());
		}
		
		return spriterKeyFrames;
	}
}
