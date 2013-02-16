package com.spriter.converters;

import com.discobeard.spriter.dom.AnimationObject;
import com.spriter.file.Reference;
import com.spriter.objects.SpriterObject;

public class SpriterObjectConverter implements Converter<AnimationObject, SpriterObject>{

	public SpriterObject convert(AnimationObject from) {
		
		SpriterObject object = new SpriterObject();
		object.setAlpha(from.getA().floatValue());
		object.setAngle(from.getAngle().floatValue());
		object.setReference(new Reference(from.getFolder(), from.getFile()));
		object.setPivotX(from.getPivotX().floatValue());
		object.setPivotY(from.getPivotY().floatValue());
		object.setScaleX(from.getScaleX().floatValue());
		object.setScaleY(from.getScaleY().floatValue());
		object.setX(from.getX().floatValue());
		object.setY(from.getY().floatValue());
		object.setZIndex(from.getZIndex());
		object.setTransientObject(true);
		return object;
	}

	
}
