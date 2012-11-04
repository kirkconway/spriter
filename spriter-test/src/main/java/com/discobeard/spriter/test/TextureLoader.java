package com.discobeard.spriter.test;

import java.io.IOException;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.util.ResourceLoader;

import com.discobeard.spriter.FileLoader;
import com.discobeard.spriter.Reference;

public class TextureLoader extends FileLoader<Texture>{

	@Override
	public void load(Reference ref, String path) {
		files.put(ref, getTexture(path));
	}
	
	public Texture getTexture(String path){
		try {
			return org.newdawn.slick.opengl.TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(path));
		} catch (IOException e) {
			System.out.println("Failed to create texture "+path);
			e.printStackTrace();
			return null;
		}
	}

}
