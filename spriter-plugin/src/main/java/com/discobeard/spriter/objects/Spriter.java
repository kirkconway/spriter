package com.discobeard.spriter.objects;

import com.discobeard.spriter.FileLoader;
import com.discobeard.spriter.Reference;
import com.discobeard.spriter.SCMLParser;
import com.discobeard.spriter.dom.Animation;
import com.discobeard.spriter.dom.AnimationObject;
import com.discobeard.spriter.dom.Key;
import com.discobeard.spriter.dom.SpriterData;
import com.discobeard.spriter.dom.TimeLine;
import com.discobeard.spriter.draw.Drawer;

public class Spriter {

	final private SpriterData SPRITER_DATA;
	final private String DIRECTORY;
	final private Drawer DRAWER;
	final private FileLoader<?> LOADER; 
	private Animation currentAnimation;
	private long startTime=0;
	private long animationTime =0;
	
	public Spriter(String directory, Drawer drawer,FileLoader<?> loader){
		this.DIRECTORY = directory;
		this.SPRITER_DATA = new SCMLParser(directory).parse();
		this.DRAWER = drawer;
		this.LOADER = loader;
		
		loadResources();
	}
	
	private void loadResources(){
		
		for(int folder=0;folder<SPRITER_DATA.getFolder().size();folder++){
			for(int file=0;file<SPRITER_DATA.getFolder().get(folder).getFile().size();file++){
				LOADER.load(new Reference(folder,file), DIRECTORY+"/"+SPRITER_DATA.getFolder().get(folder).getFile().get(file).getName());
			}
		}
		
	}
	
	public static Spriter getSpriter(String directory, Drawer drawer,FileLoader<?> loader){
		return new Spriter(directory,drawer,loader);
	}
	
	public void playAnimation(int animation){
		startTime = System.currentTimeMillis();
		currentAnimation = SPRITER_DATA.getEntity().get(0).getAnimation().get(animation);
	}
	
	public void draw(float xOffset,float yOffset){
		
		animationTime = System.currentTimeMillis() - startTime;
		
		if(animationTime>currentAnimation.getLength()){
			startTime=System.currentTimeMillis();
			animationTime=0;
		}
		
		for(TimeLine timeline : currentAnimation.getTimeline()){

			for(int k=timeline.getKey().size()-1;k>-1;k--){
				Key key = timeline.getKey().get(k);
				if(animationTime>=key.getTime()){
					AnimationObject object = key.getObject().get(0);
					AnimationObject object2;
					Key key2;
					
					
					float x= object.getX().floatValue();
					float y =object.getY().floatValue();
					float rotation = object.getAngle().floatValue();
					
					if(k<(timeline.getKey().size()-1))
					{
						key2 = timeline.getKey().get(k+1);
						object2 = key2.getObject().get(0);
						x = calculateInterpolation(object.getX().floatValue(),object2.getX().floatValue(),key.getTime(),key2.getTime(),animationTime);
						y = calculateInterpolation(object.getY().floatValue(),object2.getY().floatValue(),key.getTime(),key2.getTime(),animationTime);
						
						float angleB = object2.getAngle().floatValue();
						
						if(key.getSpin()==1){
							if((object2.getAngle().floatValue() - object.getAngle().floatValue())<0){
								angleB+=360;
							}
						}
						else if (key.getSpin()==-1){
							if((object2.getAngle().floatValue() - object.getAngle().floatValue())>=0){
								angleB-=360;
							}
						}

						rotation = calculateInterpolation(object.getAngle().floatValue(),angleB,key.getTime(),key2.getTime(),animationTime);
					}

					draw(new Reference(object.getFolder(),object.getFile()),xOffset+x,yOffset+y,object.getPivotX().floatValue(),object.getPivotY().floatValue(),rotation);
					break;
				}
			}
		}
	}
	
	public void draw(Reference ref,float x, float y, float pivot_x,float pivot_y, float angle){
		DRAWER.draw(ref,x, y, pivot_x, pivot_y, angle);
	}
	
	private float calculateInterpolation(float a,float b,float timeA,float timeB,long currentTime){
		return a+((b - a)*((currentTime-timeA)/(timeB-timeA)));
	}
	
	
}
