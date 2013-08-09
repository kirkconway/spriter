package com.brashmonkey.spriter.player;

import com.brashmonkey.spriter.SpriterCalculator;
import com.brashmonkey.spriter.draw.AbstractDrawer;
import com.brashmonkey.spriter.objects.SpriterAbstractObject;
import com.brashmonkey.spriter.objects.SpriterBone;

public class SpriterCCDResolver implements ISpriterIKResolver {

	private SpriterAbstractPlayer player;
	private SpriterAbstractObject temp;
	
	public SpriterCCDResolver(SpriterAbstractPlayer target){
		this.player = target;
		this.temp = new SpriterBone();
	}
	
	@Override
	public void resolve(float x, float y, int chainLength, SpriterAbstractObject effector) {
		float xx = effector.getX()+(float)Math.cos(Math.toRadians(effector.getAngle()))*AbstractDrawer.BONE_LENGTH*effector.getScaleX(),
				yy = effector.getY()+(float)Math.sin(Math.toRadians(effector.getAngle()))*AbstractDrawer.BONE_LENGTH*effector.getScaleX();
		effector.setAngle(SpriterCalculator.angleBetween(effector.getX(), effector.getY(), x, y));
		if(this.player.getFlipX() == -1) effector.setAngle(effector.getAngle()+180f);
		SpriterBone parent = null;
		if(effector.hasParent()){ 
			parent = player.getRuntimeBones()[effector.getParentId()];
			effector.copyValuesTo(temp);
			SpriterCalculator.reTranslateRelative(parent, temp);
			if(effector instanceof SpriterBone)	temp.copyValuesTo(player.lastFrame.getBones()[effector.getId()]);
			else temp.copyValuesTo(player.lastFrame.getObjects()[effector.getId()]);
		}
		for(int i = 0; i < chainLength && parent != null; i++){
			if(SpriterCalculator.distanceBetween(xx, yy, x, y) <= 1) return;
			parent.setAngle(parent.getAngle()+ SpriterCalculator.angleDifference(SpriterCalculator.angleBetween(parent.getX(), parent.getY(), x, y),
					SpriterCalculator.angleBetween(parent.getX(), parent.getY(), xx, yy)));
			this.player.updateRecursively(parent);
			if(parent.hasParent())	parent = player.getRuntimeBones()[parent.getParent().getId()];
			else parent = null;
			xx = effector.getX()+(float)Math.cos(Math.toRadians(effector.getAngle()))*AbstractDrawer.BONE_LENGTH*effector.getScaleX();
			yy = effector.getY()+(float)Math.sin(Math.toRadians(effector.getAngle()))*AbstractDrawer.BONE_LENGTH*effector.getScaleX();
		}
	}

	@Override
	public void setTarget(SpriterAbstractPlayer player) {
		this.player = player;
	}

}
