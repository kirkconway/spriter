package com.brashmonkey.spriter.ik;

import java.util.HashMap;
import java.util.Map.Entry;

import com.brashmonkey.spriter.objects.SpriterAbstractObject;
import com.brashmonkey.spriter.objects.SpriterBone;
import com.brashmonkey.spriter.objects.SpriterIKObject;
import com.brashmonkey.spriter.objects.SpriterObject;
import com.brashmonkey.spriter.player.SpriterAbstractPlayer;

public abstract class  SpriterIKResolver {
	
	/**
	 * Resolves the inverse kinematics constraint with a specific algtorithm
	 * @param x the target x value
	 * @param y the target y value
	 * @param chainLength number of parents which are affected
	 * @param effector the actual effector where the resolved information has to be stored in.
	 */
	protected abstract void resolve(float x, float y, int chainLength, SpriterAbstractObject effector, SpriterAbstractPlayer player);
	
	protected boolean resovling;
	protected HashMap<SpriterIKObject, SpriterAbstractObject> ikMap;
	protected float tolerance;

	public SpriterIKResolver() {
		this.resovling = true;
		this.tolerance = 5f;
		this.ikMap = new HashMap<SpriterIKObject, SpriterAbstractObject>();
	}
	
	/**
	 * Resolves the inverse kinematics constraints with the implemented algorithm in {@link #resolve(float, float, int, SpriterAbstractObject, SpriterAbstractPlayer)}.
	 * @param player player to apply the resolving.
	 */
	public void resolve(SpriterAbstractPlayer player){
		for(Entry<SpriterIKObject, SpriterAbstractObject> entry: this.ikMap.entrySet()){
			for(int j = 0; j < entry.getKey().iterations; j++)
				this.resolve(entry.getKey().getX(), entry.getKey().getY(), entry.getKey().chainLength, 
						(entry.getValue() instanceof SpriterBone) ? player.getRuntimeBones()[entry.getValue().getId()] : player.getRuntimeObjects()[entry.getValue().getId()],player);
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
	public void deactivateEffectors(SpriterAbstractPlayer player, boolean parents){
		for(Entry<SpriterIKObject, SpriterAbstractObject> entry: this.ikMap.entrySet()){
			SpriterAbstractObject obj = entry.getValue();
			obj = (obj instanceof SpriterBone) ? player.getRuntimeBones()[obj.getId()]: player.getRuntimeObjects()[obj.getId()];
			obj.active = false;
			if(!parents) continue;
			SpriterBone par = (SpriterBone) entry.getValue().getParent();
			for(int j = 0; j < entry.getKey().chainLength && par != null; j++){
				player.getRuntimeBones()[par.getId()].active = false;
				par = (SpriterBone) par.getParent();
			}
		}
	}
	
	public void activateEffectors(SpriterAbstractPlayer player){
		for(Entry<SpriterIKObject, SpriterAbstractObject> entry: this.ikMap.entrySet()){
			SpriterAbstractObject obj = entry.getValue();
			obj = (obj instanceof SpriterBone) ? player.getRuntimeBones()[obj.getId()]: player.getRuntimeObjects()[obj.getId()];
			obj.active = true;
			SpriterBone par = (SpriterBone) entry.getValue().getParent();
			for(int j = 0; j < entry.getKey().chainLength && par != null; j++){
				player.getRuntimeBones()[par.getId()].active = false;
				par = (SpriterBone) par.getParent();
			}
		}
	}
	
	public void activateAll(SpriterAbstractPlayer player){
		for(SpriterBone bone: player.getRuntimeBones())
			bone.active = true;
		for(SpriterObject obj: player.getRuntimeObjects())
			obj.active = true;
	}
	
	protected void updateObject(SpriterAbstractPlayer player, SpriterAbstractObject object){
		player.updateAbstractObject(object);
	}
	
	protected void updateRecursively(SpriterAbstractPlayer player, SpriterAbstractObject object){
		this.updateObject(player, object);
		if(object instanceof SpriterBone){
			for(SpriterBone child: ((SpriterBone) object).getChildBones())
				this.updateRecursively(player, player.getRuntimeBones()[child.getId()]);
			for(SpriterObject child: ((SpriterBone) object).getChildObjects())
				this.updateRecursively(player, player.getRuntimeObjects()[child.getId()]);
		}
	}

}
