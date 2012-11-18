package com.discobeard.spriter.mergers;

import com.discobeard.spriter.dom.AnimationObject;
import com.discobeard.spriter.dom.AnimationObjectRef;
import com.discobeard.spriter.dom.Key;
import com.discobeard.spriter.objects.SpriterObject;

public class SpriterObjectMerger implements Merger<AnimationObjectRef, Key, SpriterObject> {

	@Override
	public SpriterObject merge(AnimationObjectRef ref, Key key) {

		AnimationObject obj = key.getObject().get(0); 
		SpriterObject spriterObject = new SpriterObject();
		spriterObject.setAngle(obj.getAngle().floatValue());
		spriterObject.setFile(obj.getFile());
		spriterObject.setFolder(obj.getFolder());
		spriterObject.setPivotX(obj.getPivotX().floatValue());
		spriterObject.setPivotY(obj.getPivotY().floatValue());
		spriterObject.setX(obj.getX().floatValue());
		spriterObject.setY(obj.getY().floatValue());
		spriterObject.setzIndex(ref.getZIndex());
		spriterObject.setSpin(key.getSpin());
		spriterObject.setParent(ref.getParent());
		spriterObject.setAlpha(obj.getA().floatValue());
		spriterObject.setScale_x(obj.getScaleX().floatValue());
		spriterObject.setScale_y(obj.getScaleY().floatValue());
		
		return spriterObject;
	}

}
