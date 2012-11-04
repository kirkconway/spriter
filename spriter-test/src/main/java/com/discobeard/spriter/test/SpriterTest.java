package com.discobeard.spriter.test;

import org.newdawn.slick.opengl.Texture;
import com.discobeard.spriter.FileLoader;
import com.discobeard.spriter.objects.Spriter;

public class SpriterTest {
	
	private Spriter spriter;
	private FileLoader<Texture> loader;
	private int i=0;
	private int currentAnimation=0;
	
	public SpriterTest(){
		loader = new TextureLoader();
		spriter = Spriter.getSpriter("monster",new TextureDrawer(loader),loader);
		spriter.playAnimation(currentAnimation);
	}
	
	public void draw(){
		spriter.draw(200,20);
		
		i++;
		
		if(i>120){
			i=0;
			
			currentAnimation = currentAnimation==2?0:2;
			spriter.playAnimation(currentAnimation);
		}
		
	}
}
