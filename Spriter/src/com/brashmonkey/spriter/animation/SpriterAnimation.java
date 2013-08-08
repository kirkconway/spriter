package com.brashmonkey.spriter.animation;

import java.util.ArrayList;

import com.brashmonkey.spriter.objects.SpriterAbstractObject;
import com.brashmonkey.spriter.objects.SpriterBone;
import com.brashmonkey.spriter.objects.SpriterObject;

public class SpriterAnimation {
	
	public final ArrayList<SpriterKeyFrame> frames;
	public final String name;
	public final int id;
	public final long length;
	
	public SpriterAnimation(int id, String name, long length){
		this.frames = new ArrayList<SpriterKeyFrame>();
		this.id = id;
		this.name = name;
		this.length = length;
	}
	
	/**
	 * Searches for a keyframe in this animation which has a smaller or equal starting time as the given time.
	 * @param time
	 * @return A keyframe object which has a smaller or equal starting time than the given time.
	 */
	public SpriterKeyFrame getPreviousFrame(long time){
		SpriterKeyFrame frame = null;
		for(SpriterKeyFrame key: this.frames){
			if(key.getTime() <= time)	frame = key;
			else break;
		}
		return frame;
	}
	
	/**
	 * Searches for a keyframe in this animation which has a smaller or equal starting time as the given time and contains the given bone.
	 * @param bone
	 * @param time
	 * @return A keyframe object which has a smaller or equal starting time than the given time and contains the given bone.
	 */
	public SpriterKeyFrame getPreviousFrameForBone(SpriterBone bone, long time){
		SpriterKeyFrame frame = null;
		for(SpriterKeyFrame key: this.frames){
			if(!key.containsBone(bone)) continue;
			if(key.getTime() <= time)	frame = key;
			else break;
		}
		return frame;
	}
	
	/**
	 * Searches for a keyframe in this animation which has a smaller or equal starting time as the given time and contains the given object.
	 * @param object
	 * @param time
	 * @return A keyframe object which has a smaller or equal starting time than the given time and contains the given object.
	 */
	public SpriterKeyFrame getPreviousFrameForObject(SpriterObject object, long time){
		SpriterKeyFrame frame = null;
		for(SpriterKeyFrame key: this.frames){
			if(!key.containsObject(object)) continue;
			if(key.getTime() <= time)	frame = key;
			else break;
		}
		return frame;
	}
	
	/**
	 * @return number of frames in this animation.
	 */
	public int frames(){
		return this.frames.size();
	}
	
	public SpriterKeyFrame getNextFrameFor(SpriterAbstractObject object, SpriterKeyFrame currentFrame, int direction){
		SpriterKeyFrame nextFrame = null;
		int cnt = 0;
		boolean isBone = object instanceof SpriterBone;
		for(int j = (currentFrame.getId()+ direction + this.frames())%this.frames(); nextFrame == null && cnt < this.frames();
				j = (j+ direction + this.frames())%this.frames(), cnt++){
			SpriterKeyFrame frame = this.frames.get(j);
			boolean contains = (isBone) ? frame.containsBone((SpriterBone) object) : frame.containsObject((SpriterObject) object);
			if(contains){
				SpriterAbstractObject objectInFrame = (isBone) ? frame.getBoneFor((SpriterBone) object)	: frame.getObjectFor((SpriterObject) object);
				if(object.equals(objectInFrame))
					nextFrame = frame;
				
			} 
		}
		return nextFrame;
	}

}
