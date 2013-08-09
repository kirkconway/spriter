package com.brashmonkey.spriter.player;

import com.brashmonkey.spriter.objects.SpriterAbstractObject;

public interface ISpriterIKResolver {
	
	/**
	 * Resolves the inverse kinematics constraint with a specific algtorithm
	 * @param x the target x value
	 * @param y the target y value
	 * @param chainLength number of parents which are affected
	 * @param effector the actual effector where the resolved information has to be stored in.
	 */
	public void resolve(float x, float y, int chainLength, SpriterAbstractObject effector);
	
	public void setTarget(SpriterAbstractPlayer player);

}
