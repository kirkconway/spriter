package com.spriter.objects;

import com.spriter.file.FileLoader;
import com.spriter.file.Reference;


@SuppressWarnings("rawtypes")
public class SpriterModObject extends SpriterAbstractObject{
	
	private float alpha;
	private String name;
	private Reference ref;
	private FileLoader loader;
	private boolean active;
	
	public SpriterModObject(){
		super();
		this.alpha = 1f;
		this.ref = null;
		this.loader = null;
	}

	public float getAlpha() {
		return alpha;
	}

	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Reference getRef() {
		return ref;
	}

	public void setRef(Reference ref) {
		this.ref = ref;
	}

	public FileLoader getLoader() {
		return loader;
	}

	public void setLoader(FileLoader loader) {
		this.loader = loader;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
	private void modObject(SpriterAbstractObject object){
		object.setAngle(object.getAngle()+this.angle);
		object.setScaleX(object.getScaleX()*this.scaleX);
		object.setScaleY(object.getScaleY()*this.scaleY);
		object.setX(object.getX()+this.x);
		object.setY(object.getY()+this.y);
	}
	
	public void modSpriterObject(SpriterObject object){
		this.modObject(object);
		object.setAlpha(object.getAlpha()*this.alpha);
	}
	
	public void modSpriterBone(SpriterBone bone){
		this.modObject(bone);
	}

}
