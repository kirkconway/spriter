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

package com.brashmonkey.spriter.animation;

import com.brashmonkey.spriter.objects.SpriterBone;
import com.brashmonkey.spriter.objects.SpriterObject;

/**
 * A SpriterKeyFrame holds an array of #SpriterBone and an array of #SpriterObject and their transformations.
 * It also holds an start and end time, which are necessary to interpolate the data with the data of another #SpriterKeyFrame.
 * @author Trixt0r
 */
public class SpriterKeyFrame {
	
	private SpriterBone[] bones;
	private SpriterObject[] objects;
	private long time;
	private int id;

	/**
	 * @return array of bones, this keyframe holds.
	 */
	public SpriterBone[] getBones() {
		return bones;
	}

	/**
	 * @param bones to set to this keyframe.
	 */
	public void setBones(SpriterBone[] bones) {
		this.bones = bones;
	}

	/**
	 * @return array of objects, this keyframe holds.
	 */
	public SpriterObject[] getObjects() {
		return objects;
	}

	/**
	 * @param objects to set to this keyframe.
	 */
	public void setObjects(SpriterObject[] objects) {
		this.objects = objects;
	}

	/**
	 * @return start time of this frame.
	 */
	public long getTime() {
		return time;
	}

	/**
	 * @param startTime of this frame.
	 */
	public void setTime(long startTime) {
		this.time = startTime;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * Returns whether this frame has information about the given object.
	 * @param object 
	 * @return True if this frame contains the object, false otherwise.
	 */
	public boolean containsObject(SpriterObject object){
		return this.getObjectFor(object) != null;
	}
	
	/**
	 * Returns whether this frame has information about the given bone.
	 * @param bone 
	 * @return True if this frame contains the bone, false otherwise.
	 */
	public boolean containsBone(SpriterBone bone){
		return this.getBoneFor(bone) != null;
	}
	
	public SpriterBone getBoneFor(SpriterBone bone){
		for(SpriterBone b: this.bones){
			if(b.equals(bone))	return b;
		}
		return null;
	}
	
	public SpriterObject getObjectFor(SpriterObject object){
		for(SpriterObject obj: this.objects)
			if(obj.equals(object)) return obj;
		return null;
	}

}
