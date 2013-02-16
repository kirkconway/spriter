package com.spritertest;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.spriter.file.FileLoader;
import com.spriter.file.Reference;

public class SpriterLoader extends FileLoader<Sprite> implements Disposable{
	
	private Array<Texture> textures;

	@Override
	public void load(Reference ref, String path) {
		if(textures == null) textures = new Array<Texture>();
		Pixmap pix = new Pixmap(Gdx.files.internal(path));
		Pixmap pix1 = new Pixmap(roundToPowerOfTwo(pix.getWidth()),roundToPowerOfTwo(pix.getHeight()),Pixmap.Format.RGBA8888);
		pix1.drawPixmap(pix, 0, 0);
		Texture texture = new Texture(pix1);
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		TextureRegion region = new TextureRegion(texture,0,0,pix.getWidth(),pix.getHeight());
		Sprite sprite = new Sprite(region);
		sprite.setSize(pix.getWidth(), pix.getHeight());
		pix.dispose();
		pix1.dispose();
		files.put(ref, sprite);
		textures.add(texture);
	}
	
	private int roundToPowerOfTwo(int n)
	{
		int x = 1;
		
		while(x < n) {
			x <<= 1;
		}

		return x;
	}

	@Override
	public void dispose() {
		for(Texture tex: this.textures)
			tex.dispose();
	}

}
