package com.discobeard.spriter.converters;

import com.discobeard.spriter.dom.AnimationObject;
import com.discobeard.spriter.objects.SpriterObject;

public class SpriterObjectConverter implements Converter<AnimationObject, SpriterObject>{

	@Override
	public SpriterObject convert(AnimationObject from) {
		
		SpriterObject object = new SpriterObject();
		object.setAlpha(from.getA().floatValue());
		object.setAngle(from.getAngle().floatValue());
		object.setFile(from.getFile());
		object.setFolder(from.getFolder());
		object.setPivotX(from.getPivotX().floatValue());
		object.setPivotY(from.getPivotY().floatValue());
		object.setScale_x(from.getScaleX().floatValue());
		object.setScale_y(from.getScaleY().floatValue());
		object.setX(from.getX().floatValue());
		object.setY(from.getY().floatValue());
		object.setzIndex(from.getZIndex());
		object.setTransientObject(true);
		return object;
	}

	
}
