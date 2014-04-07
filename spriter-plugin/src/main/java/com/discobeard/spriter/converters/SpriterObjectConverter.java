package com.discobeard.spriter.converters;

import com.discobeard.spriter.dom.AnimationObject;
import com.discobeard.spriter.objects.SpriterObject;

public class SpriterObjectConverter implements Converter<AnimationObject, SpriterObject>{
	public SpriterObject convert(AnimationObject from) {
		
		SpriterObject object = new SpriterObject();
		object.setAlpha(from.getA().floatValue());
		object.setAngle(from.getAngle().floatValue());
		object.setFile(from.getFile());
		object.setFolder(from.getFolder());
		object.setPivotX(from.getPivotX().floatValue());
		object.setPivotY(from.getPivotY().floatValue());
		object.setScaleX(from.getScaleX().floatValue());
		object.setScaleY(from.getScaleY().floatValue());
		object.setX(from.getX().floatValue());
		object.setY(from.getY().floatValue());
		object.setzIndex(from.getZIndex());
		object.setTransientObject(true);
		return object;
	}

	
}
