package com.discobeard.spriter.test;

import java.util.List;

import org.newdawn.slick.opengl.Texture;

import com.discobeard.spriter.SpriterAnimation;
import com.discobeard.spriter.SpriterKeyFrameProvider;
import com.discobeard.spriter.SpriterPlayer;
import com.discobeard.spriter.file.AbstractLoader;
import com.discobeard.spriter.objects.SpriterKeyFrame;


public class SpriterTest {

	private AbstractLoader<Texture> loader;
	private TextureDrawer drawer;

	private SpriterPlayer spriterPlayer;
	private SpriterAnimation spriterAnimation;
	
	public SpriterTest(){
		loader = new TextureLoader();
		drawer = new TextureDrawer(loader);
		
		spriterAnimation = SpriterAnimation.createAnimation("Goblin_enemy/Goblin_edited.scml", loader);
		List<SpriterKeyFrame[]> keyframes = SpriterKeyFrameProvider.generateKeyFramePool(spriterAnimation.getSpriterData());
		spriterPlayer = new SpriterPlayer(spriterAnimation.getSpriterData(), drawer, keyframes);
		spriterPlayer.setFrameSpeed(10);
		spriterPlayer.setAnimatioIndex(spriterPlayer.getAnimationIndexByName("walk"), 0, 0);
		
	}
	
	public void draw(){
		spriterPlayer.update(300, 300);
		spriterPlayer.draw();
	}
}
