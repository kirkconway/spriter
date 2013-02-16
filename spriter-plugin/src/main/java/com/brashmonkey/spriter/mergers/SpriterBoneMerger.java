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

import com.brashmonkey.spriter.objects.SpriterBone;
import com.discobeard.spriter.dom.Bone;
import com.discobeard.spriter.dom.BoneRef;
import com.discobeard.spriter.dom.Key;

public class SpriterBoneMerger implements ISpriterMerger<BoneRef,Key,SpriterBone>{

	public SpriterBone merge(BoneRef ref, Key key) {
		
		Bone obj = key.getBone();
		
		SpriterBone bone = new SpriterBone();
		bone.setTimeline(ref.getTimeline().intValue());
		bone.setId(ref.getId().intValue());
		bone.setParentId((ref.getParent() == null) ? -1 : ref.getParent().intValue());
		bone.setAngle(obj.getAngle().floatValue());
		bone.setScaleX(obj.getScaleX().floatValue());
		bone.setScaleY(obj.getScaleY().floatValue());
		bone.setX(obj.getX().floatValue());
		bone.setY(obj.getY().floatValue());
		bone.setSpin(key.getSpin());
		
		return bone;
	}

}
