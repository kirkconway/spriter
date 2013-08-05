package com.brashmonkey.spriter.player;

import com.brashmonkey.spriter.SpriterCalculator;
import com.brashmonkey.spriter.objects.SpriterBone;

public class SpriterCCDResolver implements ISpriterIKResolver {

	private SpriterAbstractPlayer player;
	
	public SpriterCCDResolver(SpriterAbstractPlayer target){
		this.player = target;
	}
	
	@Override
	public void resolve(float x, float y, int chainLength, SpriterBone effector) {
		float xx = effector.getX()+(float)Math.cos(Math.toRadians(effector.getAngle()))*200*effector.getScaleX(),
				yy = effector.getY()+(float)Math.sin(Math.toRadians(effector.getAngle()))*200*effector.getScaleX();
		effector.setAngle(SpriterCalculator.angleBetween(effector.getX(), effector.getY(), x, y));
		if(this.player.getFlipX() == -1) effector.setAngle(effector.getAngle()+180f);
		SpriterBone parent = null;
		if(effector.hasParent()) parent = player.getRuntimeBones()[effector.getParentId()];
		for(int i = 0; i < chainLength && parent != null; i++){
			if(SpriterCalculator.distanceBetween(xx, yy, x, y) <= 1) return;
			parent.setAngle(parent.getAngle()+ Math.min(SpriterCalculator.angleDifference(SpriterCalculator.angleBetween(parent.getX(), parent.getY(), x, y),
					SpriterCalculator.angleBetween(parent.getX(), parent.getY(), xx, yy)), 5));
			this.player.updateRecursively(parent);
			if(parent.hasParent()) parent = player.getRuntimeBones()[parent.getParent().getId()];
			else parent = null;
			xx = effector.getX()+(float)Math.cos(Math.toRadians(effector.getAngle()))*200*effector.getScaleX();
			yy = effector.getY()+(float)Math.sin(Math.toRadians(effector.getAngle()))*200*effector.getScaleX();
		}
	}

	@Override
	public void setTarget(SpriterAbstractPlayer player) {
		this.player = player;
	}

}
