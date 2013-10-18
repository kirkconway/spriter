package com.brashmonkey.spriter.xml;

import java.io.IOException;

import com.badlogic.gdx.files.FileHandle;
import com.brashmonkey.spriter.Spriter;
import com.brashmonkey.spriter.file.FileLoader;
import com.brashmonkey.spriter.xml.XmlReader;
import com.discobeard.spriter.dom.SpriterData;

public class FileHandleSCMLReader {
	
	/**
	 * Loads a whole spriter file.
	 * @param filename Path to the scml file.
	 * @param loader The concrete loader you have implemented.
	 * @return Spriter instance which holds the read spriter structure.
	 */
	public static Spriter getSpriter(FileHandle fileHandle, FileLoader<?> loader){
		return new Spriter(load(fileHandle),loader, fileHandle.file());
	}
	
	public static SpriterData load(FileHandle file){
		FileHandleXmlReader reader = new FileHandleXmlReader();
		SCMLReader.data = new SpriterData();
		try {
			XmlReader.Element root = reader.parse(file);
			SCMLReader.loadFolders(root.getChildrenByName("folder"));
			SCMLReader.loadEntities(root.getChildrenByName("entity"));			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return SCMLReader.data;
	}
}
