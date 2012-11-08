package com.discobeard.spriter.mergers;

import com.discobeard.spriter.dom.Bone;
import com.discobeard.spriter.dom.BoneRef;
import com.discobeard.spriter.dom.Key;
import com.discobeard.spriter.objects.SpriterBone;

public class SpriterBoneMerger implements Merger<BoneRef,Key,SpriterBone>{

	@Override
	public SpriterBone merge(BoneRef ref, Key key) {
		
		Bone obj = key.getBone();
		
		SpriterBone bone = new SpriterBone();
		
		bone.setParent(ref.getParent());
		bone.setId(ref.getId());
		bone.setAngle(obj.getAngle().floatValue());
		bone.setScaleX(obj.getScaleX().floatValue());
		bone.setScaleY(obj.getScaleY().floatValue());
		bone.setX(obj.getX().floatValue());
		bone.setY(obj.getY().floatValue());
		bone.setSpin(key.getSpin());
		System.out.println(key.getSpin());
		return bone;
	}

}
