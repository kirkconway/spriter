package com.discobeard.spriter.objects;

import com.discobeard.spriter.DrawInstruction;
import com.discobeard.spriter.FileLoader;
import com.discobeard.spriter.Reference;
import com.discobeard.spriter.SCMLParser;
import com.discobeard.spriter.dom.Animation;
import com.discobeard.spriter.dom.SpriterData;
import com.discobeard.spriter.draw.Drawer;

public class Spriter {

	public static Spriter getSpriter(String directory, Drawer drawer,FileLoader<?> loader){
		return new Spriter(directory,drawer,loader);
	}
	
	final private SpriterData SPRITER_DATA;
	final private String DIRECTORY;
	final private Drawer DRAWER; 
	final private FileLoader<?> LOADER;
	private long startTime=0;
	private long animationTime =0;
	private SpriterAnimation animation = null;
	
	public Spriter(String directory, Drawer drawer,FileLoader<?> loader){
		this.DIRECTORY = directory;
		this.SPRITER_DATA = new SCMLParser(directory).parse();
		this.DRAWER = drawer;
		this.LOADER = loader;
		
		loadResources();
	}
	
	public void draw(float xOffset,float yOffset){
		
		updateAnimationTime();
		
		for(int k=animation.getKeyFrames().length-1;k>-1;k--){
			if(animationTime>=animation.getKeyFrames()[k].getStartTime()){
				if(k==animation.getKeyFrames().length-1){
					
					if(animation.shouldLoop()){
						drawSceneWithOffset(animation.createDrawInstructions(animation.getKeyFrames()[k], animation.getKeyFrames()[0], animationTime,animation.getLenght()),xOffset,yOffset);
					}
					else{
						drawSceneWithOffset(animation.createDrawInstructions(animation.getKeyFrames()[k]),xOffset,yOffset);
					}
				}
				else{
					drawSceneWithOffset(animation.createDrawInstructions(animation.getKeyFrames()[k], animation.getKeyFrames()[k+1], animationTime),xOffset,yOffset);
				}
				
				break;
			}
		}
	}
	
	private void drawSceneWithOffset(DrawInstruction[] instructions,float xOffset,float yOffset){
		
		for(DrawInstruction instruction : instructions){
			if(instruction!=null)
			{
				DRAWER.draw(instruction.getREF(), instruction.getX()+xOffset, instruction.getY()+yOffset, instruction.getPIVOT_X(), instruction.getPIVOT_Y(), instruction.getANGLE());
			}
		}
	}
	
	private void loadResources(){
		
		for(int folder=0;folder<SPRITER_DATA.getFolder().size();folder++){
			for(int file=0;file<SPRITER_DATA.getFolder().get(folder).getFile().size();file++){
				LOADER.load(new Reference(folder,file), DIRECTORY+"/"+SPRITER_DATA.getFolder().get(folder).getFile().get(file).getName());
			}
		}
	}
	
	public void playAnimation(int animationNumer,boolean isLooping){
		startTime = System.currentTimeMillis();
		Animation currentAnimation = SPRITER_DATA.getEntity().get(0).getAnimation().get(animationNumer);
		animation = SpriterAnimation.createAnimation(currentAnimation,isLooping);
	}

	private void updateAnimationTime(){
		animationTime = System.currentTimeMillis() - startTime;
		
		if(animationTime>animation.getLenght()){
			startTime=System.currentTimeMillis();
			animationTime=0;
		}
	}	
}
