package com.discobeard.spriter.test;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import com.discobeard.spriter.file.AbstractLoader;
import com.discobeard.spriter.file.Reference;

public class ImageLoader extends AbstractLoader<Image>{

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
