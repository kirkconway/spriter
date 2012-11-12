package com.discobeard.spriter;

import java.io.File;
import com.discobeard.spriter.dom.Animation;
import com.discobeard.spriter.dom.SpriterData;
import com.discobeard.spriter.draw.DrawInstruction;
import com.discobeard.spriter.draw.AbstractDrawer;
import com.discobeard.spriter.file.FileLoader;
import com.discobeard.spriter.file.Reference;
import com.discobeard.spriter.objects.SpriterAnimation;

public class Spriter {

	public static Spriter getSpriter(String path, AbstractDrawer drawer, FileLoader<?> loader) {
		return new Spriter(path, drawer, loader);
	}

	final private SpriterData spriter_data;
	final private File scml_file;
	final private AbstractDrawer drawer;
	final private FileLoader<?> loader;

	private long startTime = 0;
	private long animationTime = 0;
	private SpriterAnimation animation = null;

	public Spriter(String scml_path, AbstractDrawer drawer, FileLoader<?> loader) {
		this.scml_file = new File(getClass().getResource("/" + scml_path).getPath());
		this.spriter_data = new SCMLParser(scml_file).parse();
		this.drawer = drawer;
		this.loader = loader;
		loadResources();
	}

	public void draw(float xOffset, float yOffset) {

		updateAnimationTime();

		for (int k = animation.getKeyFrames().length - 1; k > -1; k--) {
			if (animationTime >= animation.getKeyFrames()[k].getStartTime()) {
				if (k == animation.getKeyFrames().length - 1) {

					if (animation.shouldLoop()) {
						drawSceneWithOffset(animation.createDrawInstructions(animation.getKeyFrames()[k],
								animation.getKeyFrames()[0], animationTime, animation.getLenght(),xOffset, yOffset));
					} else {
						drawSceneWithOffset(animation.createDrawInstructions(animation.getKeyFrames()[k], xOffset,
								yOffset));
					}
				} else {
					drawSceneWithOffset(animation.createDrawInstructions(animation.getKeyFrames()[k],
							animation.getKeyFrames()[k + 1], animationTime, xOffset, yOffset));
				}

				break;
			}
		}
	}

	private void drawSceneWithOffset(DrawInstruction[] instructions) {

		for (DrawInstruction instruction : instructions) {
			if (instruction != null) {
				drawer.draw(instruction);
			}
		}
	}

	private void loadResources() {

		for (int folder = 0; folder < spriter_data.getFolder().size(); folder++) {
			for (int file = 0; file < spriter_data.getFolder().get(folder).getFile().size(); file++) {
				loader.load(new Reference(folder, file),
						scml_file.getParent() + "/"
								+ spriter_data.getFolder().get(folder).getFile().get(file).getName());
			}
		}
	}

	public void playAnimation(int animationNumer, boolean isLooping) {
		startTime = System.currentTimeMillis();
		Animation currentAnimation = spriter_data.getEntity().get(0).getAnimation().get(animationNumer);
		animation = SpriterAnimation.createAnimation(currentAnimation, isLooping);
	}

	private void updateAnimationTime() {
		animationTime = System.currentTimeMillis() - startTime;

		if (animationTime > animation.getLenght()) {
			startTime = System.currentTimeMillis();
			animationTime = 0;
		}
	}
}
