package com.discobeard.spriter;

import java.io.File;
import com.discobeard.spriter.dom.SpriterData;
import com.discobeard.spriter.draw.DrawInstruction;
import com.discobeard.spriter.draw.AbstractDrawer;
import com.discobeard.spriter.file.FileLoader;
import com.discobeard.spriter.file.Reference;

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
	final private File scmlFile;
	final private SpriterData spriterData;
	private SpriterPlayer sp;

	private Spriter(String scmlPath, AbstractDrawer<?> drawer, FileLoader<?> loader) {
		this.scmlFile = new File(getClass().getResource("/" + scmlPath).getPath());
		this.spriterData = new SCMLParser(scmlFile).parse();
		this.drawer = drawer;
		this.loader = loader;
		//this.sp = new SpriterPlayer(spriterData,drawer);
		loadResources();
	}

	/**
	 * Draw the current frame of the spriter object
	 * 
	 * @param xOffset
	 * @param yOffset
	 */
	public void draw(float xOffset, float yOffset) {

		this.sp.update(xOffset, yOffset);
		//updateAnimationTime();
		
		/*int currentKey = animation.getCurrentKey();

		if (this.frame > animation.getKeyFrames()[currentKey].getEndTime()){
			animation.setCurrentKey(++currentKey);
			this.frame %=animation.getLength();
		}
		currentKey = animation.getCurrentKey();*/
		
		drawSceneWithOffset(this.sp.getDrawInstructions());

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
		this.sp.setAnimatioIndex(animationNumer);
	}
	
	public void setFrameSpeed(int frameSpeed){
		this.sp.setFrameSpeed(frameSpeed);
	}

	private void drawSceneWithOffset(DrawInstruction[] instructions) {
		for (DrawInstruction instruction : instructions) 
			if (instruction != null) 
				drawer.draw(instruction);		
	}

	private void loadResources() {
		for (int folder = 0; folder < spriterData.getFolder().size(); folder++) {
			for (int file = 0; file < spriterData.getFolder().get(folder).getFile().size(); file++) {
				loader.load(new Reference(folder, file),
						scmlFile.getParent() + "/"
								+ spriterData.getFolder().get(folder).getFile().get(file).getName());
			}
		}
	}
	
	public SpriterData getSpriterData(){
		return this.spriterData;
	}
}
