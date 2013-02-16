package com.spriter.objects;

import java.util.LinkedList;
import java.util.List;

public class SpriterBone extends SpriterAbstractObject{
	
	List<SpriterBone> childBones;
	List<SpriterObject> childObjects;
	int spin;
	String name;
	
	public SpriterBone(){
		this.childBones = new LinkedList<SpriterBone>();
		this.childObjects = new LinkedList<SpriterObject>();
	}
	
	public void addChildBone(SpriterBone bone){
		bone.setParent(this);
		childBones.add(bone);
	}
	
	public List<SpriterBone> getChildBones(){
		return childBones;
	}
	
	public void addChildObject(SpriterObject object){
		object.setParent(this);
		childObjects.add(object);
	}
	
	public List<SpriterObject> getChildObjects(){
		return childObjects;
	}

	public int getSpin() {
		return spin;
	}

	public void setSpin(int spin) {
		this.spin = spin;
	}
	
	@Override
	public String toString(){
		return "x: "+this.x+", y: "+this.y+", angle:"+ this.angle+", ";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void copyValuesTo(SpriterBone bone){
		super.copyValuesTo(bone);
		bone.setTimeline(getTimeline());
		bone.setName(getName());
		bone.setSpin(getSpin());
		bone.childBones = this.childBones;
		bone.childObjects = this.childObjects;
	}
	
}
