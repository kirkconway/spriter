package com.spriter;

import java.io.File;

import com.discobeard.spriter.dom.SpriterData;
import com.spriter.file.FileLoader;
import com.spriter.file.Reference;

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

	public static Spriter getSpriter(String path, FileLoader<?> loader) {
		return new Spriter(path, loader);
	}
	
	final private FileLoader<?> loader;
	final private File scmlFile;
	final private SpriterData spriterData;

	private Spriter(String scmlPath, FileLoader<?> loader) {
		this.scmlFile = new File(getClass().getResource("/" + scmlPath).getPath());
		this.spriterData = new SCMLParser(scmlFile).parse();
		this.loader = loader;
		loadResources();
	}
	
	public Spriter(SpriterData spriterData, FileLoader<?> loader, File scmlFile){
		System.out.println(scmlFile.getAbsolutePath());
		this.scmlFile = scmlFile;
		this.spriterData = spriterData;
		this.loader = loader;
		this.loadResources();
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
