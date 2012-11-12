package com.discobeard.spriter.test;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import com.discobeard.spriter.file.FileLoader;
import com.discobeard.spriter.file.Reference;

public class ImageLoader extends FileLoader<Image>{

	@Override
	public void load(Reference ref, String path) {
		try {
			files.put(ref, new Image(path));
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
