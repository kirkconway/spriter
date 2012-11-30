package com.spriter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.discobeard.spriter.dom.Animation;
import com.discobeard.spriter.dom.SpriterData;
import com.spriter.mergers.SpriterKeyFrameBuilder;
import com.spriter.objects.SpriterKeyFrame;

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
	public static List<SpriterKeyFrame[]> generateKeyFramePool(SpriterData spriterData){
		List<SpriterKeyFrame[]> keyframeList = new ArrayList<SpriterKeyFrame[]>();
		List<Animation> animations = spriterData.getEntity().get(0).getAnimation();
		for(Animation anim: animations){
			SpriterKeyFrame[] keyframes = new SpriterKeyFrameBuilder().buildKeyFrameArray(anim);
			for(SpriterKeyFrame key: keyframes){
				Arrays.sort(key.getObjects());
				for(int i = 0; i < key.getBones().length; i++)
					key.getBones()[i].setName(anim.getTimeline().get(key.getBones()[i].getTimeline()).getName());
			}
			keyframeList.add(keyframes);
		}
		return keyframeList;
	}
}
