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

import com.brashmonkey.spriter.animation.SpriterAnimation;
import com.brashmonkey.spriter.animation.SpriterKeyFrame;
import com.brashmonkey.spriter.file.Reference;
import com.brashmonkey.spriter.mergers.SpriterAnimationBuilder;
import com.brashmonkey.spriter.objects.SpriterBone;
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
	public static List<SpriterAnimation> generateKeyFramePool(SpriterData data, Entity entity){
		List<SpriterAnimation> spriterAnimations = new ArrayList<SpriterAnimation>();
		List<Animation> animations = entity.getAnimation();
		SpriterAnimationBuilder frameBuilder = new SpriterAnimationBuilder();
		for(Animation anim: animations){
			SpriterAnimation spriterAnimation = frameBuilder.buildAnimation(anim);
			boolean found = false;
			for(SpriterKeyFrame key: spriterAnimation.frames){
				if(!found) found = key.getTime() == anim.getLength();
				Arrays.sort(key.getObjects());
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
			if(!found){
				SpriterKeyFrame firstFrame = spriterAnimation.frames.get(0);
				SpriterKeyFrame lastFrame =  new SpriterKeyFrame();
				lastFrame.setId(spriterAnimation.frames());
				lastFrame.setBones(new SpriterBone[firstFrame.getBones().length]);
				lastFrame.setObjects(new SpriterObject[firstFrame.getObjects().length]);
				for(int j = 0; j< lastFrame.getBones().length; j++){
					SpriterBone bone = new SpriterBone();
					firstFrame.getBones()[j].copyValuesTo(bone);
					lastFrame.getBones()[j] = bone;
				}
				for(int j = 0; j< lastFrame.getObjects().length; j++){
					SpriterObject object = new SpriterObject();
					firstFrame.getObjects()[j].copyValuesTo(object);
					lastFrame.getObjects()[j] = object;
				}
				lastFrame.setTime(anim.getLength());
				spriterAnimation.frames.add(lastFrame);
			}
			spriterAnimations.add(spriterAnimation);
		}
		return spriterAnimations;
	}
}
