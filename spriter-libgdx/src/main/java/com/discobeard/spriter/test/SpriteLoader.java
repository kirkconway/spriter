package com.discobeard.spriter.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.discobeard.spriter.file.FileLoader;
import com.discobeard.spriter.file.Reference;

public class SpriteLoader extends FileLoader<Sprite>{

	@Override
	public void load(Reference ref, String path) {
		Pixmap pix = new Pixmap(Gdx.files.local(path));
		Pixmap pix1 = new Pixmap(roundToPowerOfTwo(pix.getWidth()),roundToPowerOfTwo(pix.getHeight()),Pixmap.Format.RGBA8888); 
		pix1.drawPixmap(pix, 0, 0);
		Texture texture = new Texture(pix1);
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		TextureRegion region = new TextureRegion(texture,0,0,pix.getWidth(),pix.getHeight());
		Sprite sprite = new Sprite(region);
		sprite.setSize(pix.getWidth(), pix.getHeight());
		files.put(ref, sprite);
	}
	
	private int roundToPowerOfTwo(int n)
	{
		int x = 1;
		
		while(x < n) {
			x <<= 1;
		}

		return x;
	}

}
