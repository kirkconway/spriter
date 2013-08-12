package com.brashmonkey.spriter.ik;

import com.brashmonkey.spriter.SpriterCalculator;
import com.brashmonkey.spriter.draw.AbstractDrawer;
import com.brashmonkey.spriter.objects.SpriterAbstractObject;
import com.brashmonkey.spriter.objects.SpriterBone;
import com.brashmonkey.spriter.player.SpriterAbstractPlayer;

public class SpriterCCDResolver extends SpriterIKResolver {

	@Override
	public void resolve(float x, float y, int chainLength,	SpriterAbstractObject effector, SpriterAbstractPlayer player) {
		super.updateRecursively(player, effector);
		float xx = effector.getX()+(float)Math.cos(Math.toRadians(effector.getAngle()))*AbstractDrawer.BONE_LENGTH*effector.getScaleX(),
				yy = effector.getY()+(float)Math.sin(Math.toRadians(effector.getAngle()))*AbstractDrawer.BONE_LENGTH*effector.getScaleX();
		effector.setAngle(SpriterCalculator.angleBetween(effector.getX(), effector.getY(), x, y));
		if(player.getFlipX() == -1) effector.setAngle(effector.getAngle()+180f);
		SpriterBone parent = null;
		if(effector.hasParent()){ 
			parent = player.getRuntimeBones()[effector.getParentId()];
			//effector.copyValuesTo(temp);
			//SpriterCalculator.reTranslateRelative(parent, temp);
			//if(effector instanceof SpriterBone)	temp.copyValuesTo(player.lastFrame.getBones()[effector.getId()]);
			//else temp.copyValuesTo(player.lastFrame.getObjects()[effector.getId()]);
		}
		for(int i = 0; i < chainLength && parent != null; i++){
			if(SpriterCalculator.distanceBetween(xx, yy, x, y) <= this.tolerance){
				SpriterBone p = null;
				if(parent.hasParent()) p = player.getRuntimeBones()[parent.getParentId()];
				int j = 0;
				while(p != null && j < chainLength){
					super.updateRecursively(player, p);
					if(p.hasParent()) p = player.getRuntimeBones()[p.getParentId()];
					else p = null;
					j++;
				}
				return;
			}
			parent.setAngle(parent.getAngle() + SpriterCalculator.angleDifference(SpriterCalculator.angleBetween(parent.getX(), parent.getY(), x, y),
					SpriterCalculator.angleBetween(parent.getX(), parent.getY(), xx, yy)));
			super.updateRecursively(player, parent);
			if(parent.hasParent())	parent = player.getRuntimeBones()[parent.getParent().getId()];
			else parent = null;
			xx = effector.getX()+(float)Math.cos(Math.toRadians(effector.getAngle()))*AbstractDrawer.BONE_LENGTH*effector.getScaleX();
			yy = effector.getY()+(float)Math.sin(Math.toRadians(effector.getAngle()))*AbstractDrawer.BONE_LENGTH*effector.getScaleX();
		}
	}

}
