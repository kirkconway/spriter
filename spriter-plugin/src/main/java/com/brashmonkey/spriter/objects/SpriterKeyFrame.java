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

package com.brashmonkey.spriter.objects;

/**
 * A SpriterKeyFrame holds an array of #SpriterBone and an array of #SpriterObject and their transformations.
 * It also holds an start and end time, which are necessary to interpolate the data with the data of another #SpriterKeyFrame.
 * @author Trixt0r
 */
public class SpriterKeyFrame {
	
	private SpriterBone[] bones;
	private SpriterObject[] objects;
	private long startTime;
	private long endTime;

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
	 * @return the end time of this frame.
	 */
	public long getEndTime() {
		return endTime;
	}

	/**
	 * @param endTime of this key frame.
	 */
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	/**
	 * @return start time of this frame.
	 */
	public long getStartTime() {
		return startTime;
	}

	/**
	 * @param startTime of this frame.
	 */
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

}
