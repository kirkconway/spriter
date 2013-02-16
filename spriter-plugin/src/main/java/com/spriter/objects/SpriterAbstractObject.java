package com.spriter.objects;

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
	
	public boolean equals(SpriterAbstractObject object){
		return this.id == object.getId();
	}
	
	public boolean hasParent(){
		return this.parentId != -1;
	}
}
