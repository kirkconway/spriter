/**************************************************************************
 * Copyright 2013 by Trixt0r
 * (https://github.com/Trixt0r, Heinrich Reich, e-mail: trixter16@web.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
***************************************************************************/

package com.brashmonkey.spriter.converters;

import com.brashmonkey.spriter.file.Reference;
import com.brashmonkey.spriter.objects.SpriterObject;
import com.discobeard.spriter.dom.AnimationObject;

public class SpriterObjectConverter implements Converter<AnimationObject, SpriterObject>{

	public SpriterObject convert(AnimationObject from) {
		
		SpriterObject object = new SpriterObject();
		object.setAlpha(from.getA().floatValue());
		object.setAngle(from.getAngle().floatValue());
		object.setRef(new Reference(from.getFolder(), from.getFile()));
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
