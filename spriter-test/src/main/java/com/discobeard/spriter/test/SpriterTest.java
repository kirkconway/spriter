package com.discobeard.spriter.test;

import org.newdawn.slick.opengl.Texture;

import com.discobeard.spriter.Spriter;
import com.discobeard.spriter.file.FileLoader;

public class SpriterTest {
	
	private Spriter spriter;
	private FileLoader<Texture> loader;
	private int currentAnimation=1;
	
	public SpriterTest(){
		loader = new TextureLoader();
		spriter = Spriter.getSpriter("monster/basic.scml",new TextureDrawer(loader),loader);
		spriter.playAnimation(currentAnimation,true);
	}
	
	public void draw(){
		spriter.draw(400,100);
	}
}
