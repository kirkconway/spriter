package com.brashmonkey.spriter.xml;

import java.io.IOException;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.SerializationException;
import com.brashmonkey.spriter.xml.XmlReader;

public class FileHandleXmlReader extends XmlReader{
	
	public Element parse (FileHandle file) throws IOException {
		try {
			return parse(file.read());
		} catch (Exception ex) {
			throw new SerializationException("Error parsing file: " + file, ex);
		}
	}

}
