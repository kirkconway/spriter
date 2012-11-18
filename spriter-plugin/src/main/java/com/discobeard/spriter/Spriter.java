package com.discobeard.spriter;

import java.io.File;
import com.discobeard.spriter.dom.Animation;
import com.discobeard.spriter.dom.SpriterData;
import com.discobeard.spriter.draw.DrawInstruction;
import com.discobeard.spriter.draw.AbstractDrawer;
import com.discobeard.spriter.file.FileLoader;
import com.discobeard.spriter.file.Reference;
import com.discobeard.spriter.objects.SpriterAnimation;

/**
 * 
 * @author Discobeard.com
 * 
 */
public class Spriter {

	/**
	 * Creates a spriter object.
	 * 
	 * @param path
	 * @param drawer
	 *            a drawer extended from the AbstractDrawer
	 * @param loader
	 *            a loader extended from the AbstractLoader
	 * @return a Spriter Object
	 */

	public static Spriter getSpriter(String path, AbstractDrawer<?> drawer, FileLoader<?> loader) {
		return new Spriter(path, drawer, loader);
	}

	final private AbstractDrawer<?> drawer;
	final private FileLoader<?> loader;
	final private File scml_file;
	final private SpriterData spriter_data;
	private SpriterAnimation animation = null;
	private long animationTime = 0;
	private long startTime = 0;

	private Spriter(String scml_path, AbstractDrawer<?> drawer, FileLoader<?> loader) {
		this.scml_file = new File(getClass().getResource("/" + scml_path).getPath());
		this.spriter_data = new SCMLParser(scml_file).parse();
		this.drawer = drawer;
		this.loader = loader;
		loadResources();
	}

	/**
	 * Draw the current frame of the spriter object
	 * 
	 * @param xOffset
	 * @param yOffset
	 */
	public void draw(float xOffset, float yOffset) {

		updateAnimationTime();

		int currentKey = animation.getCurrentKey();

		if (animationTime > animation.getKeyFrames()[currentKey].getEndTme()) {
			animation.setCurrentKey(currentKey + 1);
			//System.out.println("next keyframe" + (currentKey + 1));
		}
		if (currentKey == animation.getKeyFrames().length - 1) {

			if (animation.shouldLoop()) {
				drawSceneWithOffset(animation.createDrawInstructions(animation.getKeyFrames()[currentKey],
						animation.getKeyFrames()[0], animationTime, animation.getLenght(), xOffset, yOffset));
			} else {
				drawSceneWithOffset(animation.createDrawInstructions(animation.getKeyFrames()[currentKey], xOffset,
						yOffset));
			}

		} else {
			drawSceneWithOffset(animation.createDrawInstructions(animation.getKeyFrames()[currentKey],
					animation.getKeyFrames()[currentKey + 1], animationTime, xOffset, yOffset));
		}

	}

	/**
	 * Starts the animation running.
	 * 
	 * @param animationNumer
	 *            the animation number in the scml file
	 * @param loop
	 *            if you want the animation to loop or not.
	 */
	public void playAnimation(int animationNumer, boolean loop) {
		startTime = System.currentTimeMillis();
		Animation currentAnimation = spriter_data.getEntity().get(0).getAnimation().get(animationNumer);
		animation = SpriterAnimation.createAnimation(currentAnimation, loop);
	}

	private void updateAnimationTime() {
		animationTime = System.currentTimeMillis() - startTime;

		if (animationTime > animation.getLenght()) {
			startTime = System.currentTimeMillis();
			animationTime = 0;
			animation.setCurrentKey(0);
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
				System.out.println("Loaded File " + scml_file.getParent() + "/"
						+ spriter_data.getFolder().get(folder).getFile().get(file).getName());
			}
		}
	}
}
