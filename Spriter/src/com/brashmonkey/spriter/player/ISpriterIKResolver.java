package com.brashmonkey.spriter.player;

import com.brashmonkey.spriter.objects.SpriterBone;

public interface ISpriterIKResolver {
	
	public void resolve(float x, float y, int chainLength, SpriterBone effector);
	
	public void setTarget(SpriterAbstractPlayer player);

}
