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

package com.brashmonkey.spriter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.brashmonkey.spriter.file.Reference;
import com.brashmonkey.spriter.mergers.SpriterKeyFrameBuilder;
import com.brashmonkey.spriter.objects.SpriterBone;
import com.brashmonkey.spriter.objects.SpriterKeyFrame;
import com.brashmonkey.spriter.objects.SpriterObject;
import com.brashmonkey.spriter.player.SpriterPlayer;
import com.discobeard.spriter.dom.Animation;
import com.discobeard.spriter.dom.Entity;
import com.discobeard.spriter.dom.File;
import com.discobeard.spriter.dom.SpriterData;

/**
 * This class provides the {@link #generateKeyFramePool(SpriterData)} method to generate all necessary data which {@link SpriterPlayer} needs to animate.
 * It is highly recommended to call this method only once for a SCML file since {@link SpriterPlayer} does not modify the data you pass through the
 * constructor and also to save memory.
 * 
 * @author Trixt0r
 *
 */

public class SpriterKeyFrameProvider {
	
	/**
	 * Generates all needed keyframes from the given spriter data. This method sorts all objects by its z_index value to draw them in a proper way.
	 * @param spriterData SpriterData to generate from.
	 * @return generated keyframe list.
	 */
	public static List<SpriterKeyFrame[]> generateKeyFramePool(SpriterData data, Entity entity){
		List<SpriterKeyFrame[]> keyframeList = new ArrayList<SpriterKeyFrame[]>();
		List<Animation> animations = entity.getAnimation();
		for(Animation anim: animations){
			SpriterKeyFrame[] keyframes = new SpriterKeyFrameBuilder().buildKeyFrameArray(anim);
			boolean found = false;
			for(SpriterKeyFrame key: keyframes){
				if(!found) found = key.getStartTime() == anim.getLength();
				Arrays.sort(key.getObjects());
				/*for(int i = 0; i < key.getBones().length; i++)
					key.getBones()[i].setName(anim.getTimeline().get(key.getBones()[i].getTimeline()).getName());*/
				for(SpriterBone bone: key.getBones()){
					for(SpriterBone bone2: key.getBones()){
						if(bone2.getParentId() != null)
							if(!bone2.equals(bone) && bone2.getParentId() == bone.getId())
								bone.addChildBone(bone2);
					}
					for(SpriterObject object: key.getObjects()){
						Reference ref = object.getRef();
						File f = data.getFolder().get(ref.folder).getFile().get(ref.file);
						ref.dimensions = new SpriterRectangle(0, f.getHeight(), f.getWidth(), 0f);
						if(bone.getId()== object.getParentId())
							bone.addChildObject(object);
					}
				}
			}
			SpriterKeyFrame[] keys;
			if(!found){
				keys = new SpriterKeyFrame[keyframes.length+1];
				for(int i = 0; i < keyframes.length; i++) 
					keys[i] = keyframes[i];
					keys[keys.length-1] = new SpriterKeyFrame();
					keys[keys.length-1].setId(keyframes.length);
					keys[keys.length-1].setBones(new SpriterBone[keys[0].getBones().length]);
					keys[keys.length-1].setObjects(new SpriterObject[keys[0].getObjects().length]);
					for(int j = 0; j< keys[keys.length-1].getBones().length; j++){
						SpriterBone bone = new SpriterBone();
						keys[0].getBones()[j].copyValuesTo(bone);
						keys[keys.length-1].getBones()[j] = bone;
					}
					for(int j = 0; j< keys[keys.length-1].getObjects().length; j++){
						SpriterObject object = new SpriterObject();
						keys[0].getObjects()[j].copyValuesTo(object);
						keys[keys.length-1].getObjects()[j] = object;
					}
					keys[keys.length-1].setStartTime(anim.getLength());
					keys[keys.length-1].setEndTime(anim.getLength());
			}
			else keys = keyframes;
			keyframeList.add(keys);
		}
		return keyframeList;
	}
}
