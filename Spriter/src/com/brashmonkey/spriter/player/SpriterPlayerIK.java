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

package com.brashmonkey.spriter.player;


import java.util.HashMap;
import java.util.Map.Entry;

import com.brashmonkey.spriter.Spriter;
import com.brashmonkey.spriter.SpriterCalculator;
import com.brashmonkey.spriter.file.FileLoader;
import com.brashmonkey.spriter.objects.SpriterAbstractObject;
import com.brashmonkey.spriter.objects.SpriterBone;
import com.brashmonkey.spriter.objects.SpriterIKObject;
import com.discobeard.spriter.dom.Entity;
import com.discobeard.spriter.dom.SpriterData;

public class SpriterPlayerIK extends SpriterPlayer {
	
	private boolean resovling;
	private HashMap<SpriterIKObject, SpriterAbstractObject> ikMap;
	private float tolerance;
	private ISpriterIKResolver resolver;
	private SpriterAbstractObject temp;

	public SpriterPlayerIK(SpriterData data, Entity entity, FileLoader<?> loader) {
		super(data, entity, loader);
		this.resovling = true;
		this.tolerance = 0.5f;
		this.resolver = new SpriterCCDResolver(this);
		super.step(0, 0);
		this.updateObjects = false;
		this.ikMap = new HashMap<SpriterIKObject, SpriterAbstractObject>();
		this.temp = new SpriterBone();
	}
	
	public SpriterPlayerIK(Spriter spriter, int entityIndex, FileLoader<?> loader) {
		this(spriter.getSpriterData(), spriter.getSpriterData().getEntity().get(entityIndex), loader);
	}
	
	public SpriterPlayerIK(SpriterData data, int entityIndex, FileLoader<?> loader) {
		this(data, data.getEntity().get(entityIndex), loader);
	}
	
	@Override
	protected void step(float xOffset, float yOffset){
		super.step(xOffset, yOffset);
		if(this.resovling) this.resolve(xOffset, yOffset);
		this.transformObjects(firstKeyFrame, this.secondKeyFrame, xOffset, yOffset);
		for(int i = 0; i < this.currenObjectsToDraw; i++)
			this.tempObjects[i].copyValuesTo(this.instructions[i]);
	}
	
	private void resolve(float xOffset, float yOffset){
		for(Entry<SpriterIKObject, SpriterAbstractObject> entry: this.ikMap.entrySet()){
			for(int j = 0; j < entry.getKey().iterations; j++)
				this.resolver.resolve(entry.getKey().getX(), entry.getKey().getY(), entry.getKey().chainLength, 
						(entry.getValue() instanceof SpriterBone) ? this.tempBones[entry.getValue().getId()] : this.tempObjects[entry.getValue().getId()]);
		}
	}

	/**
	 * @return the resovling
	 */
	public boolean isResovling() {
		return resovling;
	}

	/**
	 * @param resovling the resovling to set
	 */
	public void setResovling(boolean resovling) {
		this.resovling = resovling;
	}
	
	/**
	 * Adds the given object to the internal SpriterIKObject - SpriterBone map, which works like a HashMap.
	 * This means, the values of the given object affect the mapped bone.
	 * @param object
	 * @param bone
	 */
	public void mapIKObject(SpriterIKObject object, SpriterAbstractObject abstractObject){
		this.ikMap.put(object, abstractObject);
	}
	
	/**
	 * Removes the given object from the internal map.
	 * @param object
	 */
	public void unmapIKObject(SpriterIKObject object){
		this.ikMap.remove(object);
	}

	public float getTolerance() {
		return tolerance;
	}

	public void setTolerance(float tolerance) {
		this.tolerance = tolerance;
	}
	
	/**
	 * Changes the state of each effector to unactive. The effect results in non animated bodyparts.
	 * @param parents indicates whether parents of the effectors have to be deactivated or not.
	 */
	public void deactivateEffectors(boolean parents){
		for(Entry<SpriterIKObject, SpriterAbstractObject> entry: this.ikMap.entrySet()){
			SpriterAbstractObject obj = entry.getValue();
			obj = (obj instanceof SpriterBone) ? this.tempBones[obj.getId()]: this.tempObjects[obj.getId()];
			obj.tween = false;
			if(!parents) continue;
			SpriterBone par = (SpriterBone) entry.getValue().getParent();
			for(int j = 0; j < entry.getKey().chainLength && par != null; j++){
				this.tempBones[par.getId()].tween = false;
				par = (SpriterBone) par.getParent();
			}
		}
	}
	
	public void activateEffectors(){
		for(Entry<SpriterIKObject, SpriterAbstractObject> entry: this.ikMap.entrySet()){
			SpriterAbstractObject obj = entry.getValue();
			obj = (obj instanceof SpriterBone) ? this.tempBones[obj.getId()]: this.tempObjects[obj.getId()];
			obj.tween = true;
			SpriterBone par = (SpriterBone) entry.getValue().getParent();
			for(int j = 0; j < entry.getKey().chainLength && par != null; j++){
				this.tempBones[par.getId()].tween = false;
				par = (SpriterBone) par.getParent();
			}
		}
	}

	/**
	 * @return the resolver
	 */
	public ISpriterIKResolver getResolver() {
		return resolver;
	}

	/**
	 * @param resolver the resolver to set
	 */
	public void setResolver(ISpriterIKResolver resolver) {
		this.resolver = resolver;
	}
	

	
	@Override
	public void updateBone(SpriterBone bone){
		super.updateBone(bone);
		bone.copyValuesTo(temp);
		SpriterAbstractObject parent = (bone.hasParent()) ? getRuntimeBones()[bone.getParent().getId()] : this.tempParent;
		SpriterCalculator.reTranslateRelative(parent, temp);
		temp.copyValuesTo(this.lastFrame.getBones()[temp.getId()]);
	}

}
