package com.discobeard.spriter.objects;

import java.util.List;

public class SpriterObject {
	
	private int file;
	private int folder;
	private float x;
	private float y;
	private float pivotX;
	private float pivotY;
	private int zIndex;
	private float angle;
	private int spin;
	private Integer parent = null;
	
	public int getFile() {
		return file;
	}
	
	public void setFile(int file) {
		this.file = file;
	}
	public int getFolder() {
		return folder;
	}
	public void setFolder(int folder) {
		this.folder = folder;
	}
	public float getX() {
		return x;
	}
	public void setX(float x) {
		this.x = x;
	}
	public float getY() {
		return y;
	}
	public void setY(float y) {
		this.y = y;
	}
	public float getPivotX() {
		return pivotX;
	}
	public void setPivotX(float pivotX) {
		this.pivotX = pivotX;
	}
	public float getPivotY() {
		return pivotY;
	}
	public void setPivotY(float pivotY) {
		this.pivotY = pivotY;
	}
	public int getzIndex() {
		return zIndex;
	}
	public void setzIndex(int zIndex) {
		this.zIndex = zIndex;
	}
	public float getAngle() {
		return angle;
	}
	public void setAngle(float angle) {
		this.angle = angle;
	}
	public int getSpin() {
		return spin;
	}
	public void setSpin(int spin) {
		this.spin = spin;
	}

	public Integer getParent() {
		return parent;
	}

	public void setParent(Integer parentId) {
		this.parent = parentId;
	}
	
	
}
