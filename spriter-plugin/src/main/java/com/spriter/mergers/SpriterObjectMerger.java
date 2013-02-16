package com.spriter.mergers;

import com.discobeard.spriter.dom.AnimationObject;
import com.discobeard.spriter.dom.AnimationObjectRef;
import com.discobeard.spriter.dom.Key;
import com.spriter.file.Reference;
import com.spriter.objects.SpriterObject;

public class SpriterObjectMerger implements ISpriterMerger<AnimationObjectRef, Key, SpriterObject> {

	public SpriterObject merge(AnimationObjectRef ref, Key key) {

		AnimationObject obj = key.getObject().get(0); 
		
		SpriterObject spriterObject = new SpriterObject();
		spriterObject.setId(ref.getId().intValue());
		spriterObject.setParentId((ref.getParent() == null) ? -1 : ref.getParent().intValue());
		spriterObject.setTimeline(ref.getTimeline().intValue());
		spriterObject.setAngle(obj.getAngle().floatValue());
		spriterObject.setReference(new Reference(obj.getFolder(),obj.getFile()));
		spriterObject.setPivotX(obj.getPivotX().floatValue());
		spriterObject.setPivotY(obj.getPivotY().floatValue());
		spriterObject.setX(obj.getX().floatValue());
		spriterObject.setY(obj.getY().floatValue());
		spriterObject.setZIndex(ref.getZIndex());
		spriterObject.setSpin(key.getSpin());
		spriterObject.setAlpha(obj.getA().floatValue());
		spriterObject.setScaleX(obj.getScaleX().floatValue());
		spriterObject.setScaleY(obj.getScaleY().floatValue());
		
		return spriterObject;
	}

}
