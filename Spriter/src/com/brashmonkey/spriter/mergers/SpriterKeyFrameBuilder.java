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
import java.util.List;

import com.brashmonkey.spriter.converters.SpriterObjectConverter;
import com.brashmonkey.spriter.objects.SpriterBone;
import com.brashmonkey.spriter.objects.SpriterKeyFrame;
import com.brashmonkey.spriter.objects.SpriterObject;
import com.discobeard.spriter.dom.Animation;
import com.discobeard.spriter.dom.AnimationObject;
import com.discobeard.spriter.dom.AnimationObjectRef;
import com.discobeard.spriter.dom.BoneRef;
import com.discobeard.spriter.dom.Key;
import com.discobeard.spriter.dom.MainLine;
import com.discobeard.spriter.dom.TimeLine;

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
				if(key.getTime() == timeLines.get(boneRef.getTimeline()).getKey().get(boneRef.getKey()).getTime()){
					SpriterBone bone = boneMerger.merge(boneRef, timeLines.get(boneRef.getTimeline()).getKey().get(boneRef.getKey()));
					bone.setName(timeLines.get(boneRef.getTimeline()).getName());
					if(bone.hasParent()){
						SpriterBone parent = null;
						for(int i = 0; i< spriterKeyFrames.length && parent == null; i++){
							parent = this.searchForParentBone(spriterKeyFrames[i], bone.getParentId());
							bone.setParent(parent);
						}
					}
					tempBones.add(bone);
				}
			}
			
			for(AnimationObjectRef objectRef : key.getObjectRef()){
				if(key.getTime() == timeLines.get(objectRef.getTimeline()).getKey().get(objectRef.getKey()).getTime()){
					SpriterObject object = objectMerger.merge(objectRef, timeLines.get(objectRef.getTimeline()).getKey().get(objectRef.getKey()));
					object.setName(timeLines.get(objectRef.getTimeline()).getName());
					if(object.hasParent()){
						SpriterBone parent = null;
						for(int i = 0; i< spriterKeyFrames.length && parent == null; i++){
							parent = this.searchForParentBone(spriterKeyFrames[i], object.getParentId());
							object.setParent(parent);
						}
					}
					tempObjects.add(object);
				}
			}
			
			for(AnimationObject object : key.getObject()){
				tempObjects.add(objectConverter.convert(object));
			}
			
			spriterKeyFrames[k] = new SpriterKeyFrame();
			spriterKeyFrames[k].setObjects(tempObjects.toArray(new SpriterObject[tempObjects.size()]));
			spriterKeyFrames[k].setBones(tempBones.toArray(new SpriterBone[tempBones.size()]));
			
			spriterKeyFrames[k].setStartTime(key.getTime());
			spriterKeyFrames[k].setEndTime(k<keyFrames.size()-1 ? keyFrames.get(k+1).getTime()-1 : animation.getLength());
			spriterKeyFrames[k].setId(key.getId());
		}
		
		return spriterKeyFrames;
	}
	
	private SpriterBone searchForParentBone(SpriterKeyFrame frame, Integer parentId){
		if(frame == null) return null;
		for(SpriterBone bone: frame.getBones())
			if(bone.getParentId().equals(parentId)) return bone;
		return null;
	}
}
