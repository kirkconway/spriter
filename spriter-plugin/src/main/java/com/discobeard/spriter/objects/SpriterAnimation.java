package com.discobeard.spriter.objects;

import java.util.HashMap;

import com.discobeard.spriter.DrawInstruction;
import com.discobeard.spriter.Reference;
import com.discobeard.spriter.dom.Animation;
import com.discobeard.spriter.mergers.SpriterKeyFrameBuilder;

public class SpriterAnimation {
	
	final private SpriterKeyFrame[] keyFrames;
	final private long lenght;
	final private boolean shouldLoop;
	
	public static SpriterAnimation createAnimation(Animation from) {
		return new SpriterAnimation(new SpriterKeyFrameBuilder().buildKeyFrameArray(from),from.getLength(),Boolean.getBoolean("false"));
	}
	
	public static SpriterAnimation createAnimation(Animation from,boolean isLooping) {
		from.setLooping(isLooping);
		return new SpriterAnimation(new SpriterKeyFrameBuilder().buildKeyFrameArray(from),from.getLength(),Boolean.getBoolean("false"));
	}
	
	public SpriterAnimation(SpriterKeyFrame[] keyFrames, long length, boolean loop){
		this.keyFrames=keyFrames;
		this.lenght=length;
		this.shouldLoop=loop;
	}

	public SpriterKeyFrame[] getKeyFrames() {
		return keyFrames;
	}
	
	public DrawInstruction[] createDrawInstructions(SpriterKeyFrame key){
		DrawInstruction[] drawInstructions = new DrawInstruction[key.getObjects().length];
		
		for(int o=0;o<key.getObjects().length;o++){
			SpriterObject obj = key.getObjects()[o];
			drawInstructions[o] = new DrawInstruction(new Reference(obj.getFolder(),obj.getFile()),obj.getX(),obj.getY(),obj.getPivotX(),obj.getPivotY(),obj.getAngle());
		}
		
		return drawInstructions;
	}
	
	public DrawInstruction[] createDrawInstructions(SpriterKeyFrame key1,SpriterKeyFrame key2,long currentAnimationTime,long key2StartTime){
		
		DrawInstruction[] drawInstructions = new DrawInstruction[key1.getObjects().length];
		
		HashMap<Integer,SpriterBone> tempBones = new HashMap<Integer,SpriterBone>();
		
		for(int b=0;b<key1.getBones().length;b++){
			
			SpriterBone bone1 = key1.getBones()[b];
			SpriterBone bone2 = key2.getBones()[b];
			
			float bone2Angle = bone2.getAngle();
			
			//System.out.println(bone1.getSpin());
			
			if(bone1.getSpin()==1){
				if((bone2.getAngle() - bone1.getAngle())<0){
					bone2Angle+=360;
				}
			}
			else if(bone1.getSpin()==-1){
				if((bone2.getAngle() - bone1.getAngle())>=0){
					bone2Angle-=360;
				}
			}
			
			Float x = calculateInterpolation(bone1.getX(),bone2.getX(),key1.getStartTime(),key2StartTime,currentAnimationTime);
			Float y = calculateInterpolation(bone1.getY(),bone2.getY(),key1.getStartTime(),key2StartTime,currentAnimationTime);
			Float scaleX = calculateInterpolation(bone1.getScaleX(),bone2.getScaleX(),key1.getStartTime(),key2StartTime,currentAnimationTime);
			Float scaleY = calculateInterpolation(bone1.getScaleY(),bone2.getScaleY(),key1.getStartTime(),key2StartTime,currentAnimationTime);
			float rotation = calculateInterpolation(bone1.getAngle(),bone2Angle,key1.getStartTime(),key2StartTime,currentAnimationTime);
			
			SpriterBone bone = new SpriterBone();
			bone.setAngle(rotation);
			bone.setId(bone1.getId());
			bone.setParent(bone1.getParent());
			bone.setScaleX(scaleX);
			bone.setScaleY(scaleY);
			bone.setX(x);
			bone.setY(y);
			
			tempBones.put(bone1.getId(), bone);
			
		}
		
		for(int o=0;o<key1.getObjects().length;o++){
			
			SpriterObject obj1 = key1.getObjects()[o];
			SpriterObject obj2 = key2.getObjects()[o];
			
			
			
			float obj2Angle = obj2.getAngle();
			
			if(obj1.getSpin()>-1){
				if((obj2.getAngle() - obj1.getAngle())<0){
					obj2Angle+=360;
				}
			}
			else{
				if((obj2.getAngle() - obj1.getAngle())>=0){
					obj2Angle-=360;
				}
			}

			float x = calculateInterpolation(obj1.getX(),obj2.getX(),key1.getStartTime(),key2StartTime,currentAnimationTime);
			float y = calculateInterpolation(obj1.getY(),obj2.getY(),key1.getStartTime(),key2StartTime,currentAnimationTime);
			float rotation = calculateInterpolation(obj1.getAngle(),obj2Angle,key1.getStartTime(),key2StartTime,currentAnimationTime);
			
			if(obj1.getParent()!=null){
				float[] newCoords = rotatePoint(tempBones.get(obj1.getParent()),x,y,rotation*obj1.getSpin());
				rotation = newCoords[2];
				x=newCoords[0];
				y=newCoords[1];
			}
			
			
			System.out.println("rotation"+rotation);
			drawInstructions[o] = new DrawInstruction(new Reference(obj1.getFolder(),obj1.getFile()),x,y,obj1.getPivotX(),obj1.getPivotY(),rotation);
		}
		
		return drawInstructions;
	}
	
	public DrawInstruction[] createDrawInstructions(SpriterKeyFrame key1,SpriterKeyFrame key2,long currentAnimationTime){
		return createDrawInstructions(key1, key2, currentAnimationTime, key2.getStartTime());
	}
	
	private float[] rotatePoint(SpriterBone parent, float childx,float childy,float rotation)
	{
		  float s = (float) Math.sin(Math.toRadians(parent.getAngle()));
		  float c = (float) Math.cos(Math.toRadians(parent.getAngle()));
		  float xnew = ((parent.getScaleX()*childx) * c) - ((parent.getScaleY()*childy) * s);
		  float ynew = ((parent.getScaleX()*childx) * s) + ((parent.getScaleY()*childy) * c);
		  
		  rotation=+parent.getAngle();
		  childx =xnew;
		  childy =ynew;
		  
		  return new float[]{childx,childy,rotation};
	}
	
	private float calculateInterpolation(float a,float b,float timeA,float timeB,long currentTime){
		return a+((b - a)*((currentTime-timeA)/(timeB-timeA)));
	}

	public long getLenght() {
		return lenght;
	}

	public boolean shouldLoop() {
		return shouldLoop;
	}
}
