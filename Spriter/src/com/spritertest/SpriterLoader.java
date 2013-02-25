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

package com.spritertest;

import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.PixmapPacker;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;
import com.brashmonkey.spriter.file.FileLoader;
import com.brashmonkey.spriter.file.Reference;

public class SpriterLoader extends FileLoader<Sprite> implements Disposable{
	
	private PixmapPacker packer;
	private boolean pack;
	
	public SpriterLoader(boolean pack){
		this.pack = pack;
	}

	@Override
	public void load(Reference ref, String path) {
		if(packer == null && this.pack)	packer = new PixmapPacker(1024, 1024, Pixmap.Format.RGBA8888, 2, true);
		Pixmap pix = new Pixmap(Gdx.files.internal(path));
		if(packer != null) packer.pack(ref.fileName, pix);
		
		files.put(ref, this.createSprite(new Texture(pix)));
		
		pix.dispose();
	}
	
	private Sprite createSprite(Texture texture){
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		TextureRegion region = new TextureRegion(texture,0,0,texture.getWidth(),texture.getHeight());
		Sprite sprite = new Sprite(region);
		sprite.setSize(texture.getWidth(), texture.getHeight());
		return sprite;
	}
	
	public void generatePackedSprites(){
		TextureAtlas atlas = this.packer.generateTextureAtlas(TextureFilter.Linear, TextureFilter.Linear, false);
		Set<Reference> keys = this.files.keySet();
		this.disposeNonPackedTextures();
		for(Reference ref: keys){
			TextureRegion texReg = atlas.findRegion(ref.fileName);
			Sprite sprite = new Sprite(atlas.findRegion(ref.fileName));
			sprite.setSize(texReg.getRegionWidth(), texReg.getRegionHeight());
			files.put(ref, sprite);
		}
	}
	
	private void disposeNonPackedTextures(){
		Set<Reference> keys = this.files.keySet();
		for(Reference ref: keys)
			files.get(ref).getTexture().dispose();
	}

	@Override
	public void dispose() {
		if(this.pack && this.packer != null) this.packer.dispose();
		else this.disposeNonPackedTextures();
	}

}
