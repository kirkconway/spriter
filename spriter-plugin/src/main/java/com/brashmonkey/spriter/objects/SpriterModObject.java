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

import com.brashmonkey.spriter.file.FileLoader;
import com.brashmonkey.spriter.file.Reference;

/**
 * A SpriterModObject is an object which is able to manipulate animated bones and objects at runtime.
 * @author Trixt0r
 */
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
	
	/**
	 * Manipulates the given object.
	 * @param object
	 */
	public void modSpriterObject(SpriterObject object){
		this.modObject(object);
		object.setAlpha(object.getAlpha()*this.alpha);
	}
	
	/**
	 * Manipulates the given bone.
	 * @param bone
	 */
	public void modSpriterBone(SpriterBone bone){
		this.modObject(bone);
	}

}
