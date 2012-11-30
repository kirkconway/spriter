package com.spriter.objects;

public class SpriterKeyFrame {
	
	private SpriterBone[] bones;
	private SpriterObject[] objects;
	private long startTime;
	private long endTme;
	
	public SpriterKeyFrame(){
		
	}

	public SpriterBone[] getBones() {
		return bones;
	}

	public void setBones(SpriterBone[] bones) {
		this.bones = bones;
	}

	public SpriterObject[] getObjects() {
		return objects;
	}

	public void setObjects(SpriterObject[] objects) {
		this.objects = objects;
	}

	public long getEndTime() {
		return endTme;
	}

	public void setEndTime(long endTme) {
		this.endTme = endTme;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

}
