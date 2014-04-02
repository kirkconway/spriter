package com.discobeard.spriter.test;

import java.util.List;

import org.newdawn.slick.opengl.Texture;

import com.discobeard.spriter.Spriter;
import com.discobeard.spriter.SpriterKeyFrameProvider;
import com.discobeard.spriter.SpriterPlayer;
import com.discobeard.spriter.file.AbstractLoader;
import com.discobeard.spriter.objects.SpriterKeyFrame;


public class SpriterTest {

	private AbstractLoader<Texture> loader;
	private TextureDrawer drawer;

	SpriterPlayer spriterPlayer; 
	Spriter spriter;
	
	public SpriterTest(){
		loader = new TextureLoader();
		drawer = new TextureDrawer(loader);
		
		spriter = Spriter.getSpriter("monster/basic.scml", loader);
		List<SpriterKeyFrame[]> keyframes = SpriterKeyFrameProvider.generateKeyFramePool(spriter.getSpriterData());
		spriterPlayer = new SpriterPlayer(spriter.getSpriterData(), drawer, keyframes);
		spriterPlayer.setFrameSpeed(10);
		spriterPlayer.setAnimatioIndex(spriterPlayer.getAnimationIndexByName("idle"), 0, 0);
		
	}
	
	public void draw(){
		spriterPlayer.update(300, 300);
		spriterPlayer.draw();
	}
}
