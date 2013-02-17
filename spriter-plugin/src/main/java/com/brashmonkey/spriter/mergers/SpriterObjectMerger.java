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

package com.brashmonkey.spriter.mergers;

import com.brashmonkey.spriter.file.Reference;
import com.brashmonkey.spriter.objects.SpriterObject;
import com.discobeard.spriter.dom.AnimationObject;
import com.discobeard.spriter.dom.AnimationObjectRef;
import com.discobeard.spriter.dom.Key;

public class SpriterObjectMerger implements ISpriterMerger<AnimationObjectRef, Key, SpriterObject> {

	public SpriterObject merge(AnimationObjectRef ref, Key key) {

		AnimationObject obj = key.getObject().get(0); 
		
		SpriterObject spriterObject = new SpriterObject();
		spriterObject.setId(ref.getId().intValue());
		spriterObject.setParentId((ref.getParent() == null) ? -1 : ref.getParent().intValue());
		spriterObject.setTimeline(ref.getTimeline().intValue());
		spriterObject.setAngle(obj.getAngle().floatValue());
		spriterObject.setRef(new Reference(obj.getFolder(),obj.getFile()));
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
