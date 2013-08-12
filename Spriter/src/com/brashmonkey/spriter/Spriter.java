/**************************************************************************
 * Copyright 2013 by Trixt0r
 * (https://github.com/Trixt0r, Heinrich Reich, e-mail: trixter16@web.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
***************************************************************************/

package com.brashmonkey.spriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.brashmonkey.spriter.file.FileLoader;
import com.brashmonkey.spriter.file.Reference;
import com.brashmonkey.spriter.xml.SCMLReader;
import com.discobeard.spriter.dom.SpriterData;

/**
 * This class reads an scml file and loads all necessary resources.
 * 
 * @author Discobeard.com, Trixt0r
 * 
 */
public class Spriter {

	/**
	 * Creates a spriter object.
	 * 
	 * @param path Path to the scml file
	 * @param loader
	 *            a loader extended from the AbstractLoader
	 * @return a Spriter Object
	 */

	public static Spriter getSpriter(String path, FileLoader<?> loader) {
		return new Spriter(path, loader);
	}
	
	public static Spriter getSpriter(File scmlFile, FileLoader<?> loader) throws FileNotFoundException {
		return new Spriter(scmlFile, loader);
	}
	
	public final FileLoader<?> loader;
	public final File scmlFile;
	public final SpriterData spriterData;
	
	public Spriter(File scmlfile, FileLoader<?> loader) throws FileNotFoundException {
		this.scmlFile = scmlfile;
		this.spriterData = SCMLReader.load(new FileInputStream(scmlFile));
		this.loader = loader;
		loadResources();
	}

	public Spriter(String scmlPath, FileLoader<?> loader) {
		this.scmlFile = new File(scmlPath);
		this.spriterData = SCMLReader.load(scmlPath);
		this.loader = loader;
		loadResources();
	}
	
	public Spriter(SpriterData spriterData, FileLoader<?> loader, File scmlFile){
		this.scmlFile = scmlFile;
		this.spriterData = spriterData;
		this.loader = loader;
		this.loadResources();
	}

	private void loadResources() {
		for (int folder = 0; folder < spriterData.getFolder().size(); folder++) {
			for (int file = 0; file < spriterData.getFolder().get(folder).getFile().size(); file++) {
				String folderName = spriterData.getFolder().get(folder).getName();
				String fileName = spriterData.getFolder().get(folder).getFile().get(file).getName();
				Reference ref = new Reference(folder, file,folderName, fileName);
				ref.dimensions = new SpriterRectangle(0,spriterData.getFolder().get(folder).getFile().get(file).getHeight() ,spriterData.getFolder().get(folder).getFile().get(file).getWidth(),0);
				ref.pivotX = spriterData.getFolder().get(folder).getFile().get(file).getPivotX();
				ref.pivotY = spriterData.getFolder().get(folder).getFile().get(file).getPivotY();
				loader.load(ref,
						scmlFile.getParent() + "/"
								+ fileName);
			}
		}
		this.loader.finishLoading();
	}
	
	/**
	 * @return Spriter data which has been read from the scml file before.
	 */
	public SpriterData getSpriterData(){
		return this.spriterData;
	}
}
