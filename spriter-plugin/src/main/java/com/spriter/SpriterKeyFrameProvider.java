package com.spriter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.discobeard.spriter.dom.Animation;
import com.discobeard.spriter.dom.SpriterData;
import com.spriter.mergers.SpriterKeyFrameBuilder;
import com.spriter.objects.SpriterKeyFrame;

public class SpriterKeyFrameProvider {
	
	public static int MAX_OBJECTS,MAX_BONES;
	
	/**
	 * Generates all needed keyframes from the given spriter data.
	 * @param spriterData SpriterData to generate from.
	 * @return generated keyframe list.
	 */
	public static List<SpriterKeyFrame[]> generateKeyFramePool(SpriterData spriterData){
		List<SpriterKeyFrame[]> keyframeList = new ArrayList<SpriterKeyFrame[]>();
		List<Animation> animations = spriterData.getEntity().get(0).getAnimation();
		for(Animation anim: animations){
			SpriterKeyFrame[] keyframes = new SpriterKeyFrameBuilder().buildKeyFrameArray(anim);
			for(SpriterKeyFrame key: keyframes){
				Arrays.sort(key.getObjects());
				MAX_BONES = Math.max(key.getBones().length, MAX_BONES);
				MAX_OBJECTS = Math.max(key.getObjects().length, MAX_OBJECTS);
				for(int i = 0; i < key.getBones().length; i++)
					key.getBones()[i].setName(anim.getTimeline().get(key.getBones()[i].getTimeline()).getName());
			}
			keyframeList.add(keyframes);
		}
		return keyframeList;
	}
}
