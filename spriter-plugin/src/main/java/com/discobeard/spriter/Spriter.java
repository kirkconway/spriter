package com.discobeard.spriter;

import java.io.File;
import com.discobeard.spriter.dom.SpriterData;
import com.discobeard.spriter.file.AbstractLoader;
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

	public static Spriter getSpriter(String path, AbstractLoader<?> loader) {
		return new Spriter(path, loader);
	}
	
	final private AbstractLoader<?> loader;
	final private File scmlFile;
	final private SpriterData spriterData;

	private Spriter(String scmlPath, AbstractLoader<?> loader) {
		this.scmlFile = new File(getClass().getResource("/" + scmlPath).getPath());
		this.spriterData = new SCMLParser(scmlFile).parse();
		this.loader = loader;
		loadResources();
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
