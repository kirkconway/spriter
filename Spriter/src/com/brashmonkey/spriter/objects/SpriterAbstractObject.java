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
 * A SpriterAbstractObject is, as the name says, an abstract object which holds the same properties a #SpriterObject and a #SpriterBone have.
 * Such as x,y coordinates, angle, id, parent, scale and the timeline.
 * @author Trixt0r
 */
public abstract class SpriterAbstractObject {
	protected float x, y, angle, scaleX, scaleY;
	protected int id, parentId, timeline;
	protected SpriterAbstractObject parent;

	public SpriterAbstractObject(){
		this.x = 0;
		this.y = 0;
		this.angle = 0f;
		this.scaleX = 1f;
		this.scaleY = 1f;
		this.id = -1;
		this.parentId = -1;
		this.parent = null;
	}
	
	/**
	 * @return the x
	 */
	public float getX() {
		return x;
	}

	/**
	 * @param x the x to set
	 */
	public void setX(float x) {
		this.x = x;
	}

	/**
	 * @return the y
	 */
	public float getY() {
		return y;
	}

	/**
	 * @param y the y to set
	 */
	public void setY(float y) {
		this.y = y;
	}

	/**
	 * @return the angle
	 */
	public float getAngle() {
		return angle;
	}

	/**
	 * @param angle the angle to set
	 */
	public void setAngle(float angle) {
		this.angle = angle;
	}

	/**
	 * @return the scaleX
	 */
	public float getScaleX() {
		return scaleX;
	}

	/**
	 * @param scaleX the scaleX to set
	 */
	public void setScaleX(float scaleX) {
		this.scaleX = scaleX;
	}

	/**
	 * @return the scaleY
	 */
	public float getScaleY() {
		return scaleY;
	}

	/**
	 * @param scaleY the scaleY to set
	 */
	public void setScaleY(float scaleY) {
		this.scaleY = scaleY;
	}
	
	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the parent
	 */
	public SpriterAbstractObject getParent() {
		return parent;
	}

	/**
	 * @param parent the parent to set
	 */
	public void setParent(SpriterAbstractObject parent) {
		this.parent = parent;
	}

	/**
	 * @return the parentId
	 */
	public Integer getParentId() {
		return parentId;
	}

	/**
	 * @param parentId the parentId to set
	 */
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	/**
	 * @return the timeline
	 */
	public Integer getTimeline() {
		return timeline;
	}

	/**
	 * @param timeline the timeline to set
	 */
	public void setTimeline(int timeline) {
		this.timeline = timeline;
	}

	/**
	 * Sets the values of this instance to the given one.
	 * @param object which has to be manipulated.
	 */
	public void copyValuesTo(SpriterAbstractObject object){
		object.setAngle(angle);
		object.setScaleX(scaleX);
		object.setScaleY(scaleY);
		object.setX(x);
		object.setY(y);
		object.setId(id);
		object.setParentId(parentId);
		object.setParent(parent);
		object.setTimeline(timeline);
	}
	
	/**
	 * @param object to compare with
	 * @return true if both objects have the same id.
	 */
	public boolean equals(SpriterAbstractObject object){
		return this.id == object.getId();
	}
	
	/**
	 * @return whether this has a parent or not.
	 */
	public boolean hasParent(){
		return this.parentId != -1;
	}
	
	@Override
	public String toString(){
		return "id: "+ this.id+", parent: "+ this.parentId +", x: "+this.x+", y: "+this.y+", angle:"+ this.angle;
	}
}
