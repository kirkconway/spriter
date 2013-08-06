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

package spriter.utils;

import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
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

/**
 * A loader class to load Spriter sprites inside LibGDX.
 * @author Trixt0r
 */

public class SpriterLoader extends FileLoader<Sprite> implements Disposable{
	
	private PixmapPacker packer;
	private boolean pack;
	
	/**
	 * Creates a loader object to load all necessary sprites for playing Spriter animations.
	 * @param pack Indicates whether the loaded sprites have to be packed into an atlas or not (resolves performance issues if true).
	 */
	public SpriterLoader(boolean pack){
		this.pack = pack;
	}

	@Override
	public void load(final Reference ref, String path) {
		FileHandle f = Gdx.files.absolute(path);
		if(!f.exists()) return;
		if(packer == null && this.pack)
			packer = new PixmapPacker(2048, 2048, Pixmap.Format.RGBA8888, 2, true);
		final Pixmap pix = new Pixmap(f);

		if(packer != null)
			packer.pack(ref.fileName, pix);
		
		files.put(ref, null);//Put first the reference into the map, in the next frame, the null value will be replaced with the actual texture.
		
		Gdx.app.postRunnable(new Runnable(){ //Post the creation of a OpenGL Texture to the LibGDX rendering thread.
			//This is necessary if you are loading the scml file asynchrouously with more than one thread.
			@Override
			public void run() {
				files.put(ref, createSprite(new Texture(pix)));
				pix.dispose();
			}
		});
	}
	
	private Sprite createSprite(Texture diffuse){
		diffuse.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		return new Sprite(diffuse);
	}
	
	/**
	 * Packs all loaded sprites into an atlas. Has to called after loading all sprites.
	 */
	public void generatePackedSprites(){
		TextureAtlas diffuseAtlas = this.packer.generateTextureAtlas(TextureFilter.Linear, TextureFilter.Linear, false);
		Set<Reference> keys = this.files.keySet();
		this.disposeNonPackedTextures();
		for(Reference ref: keys){
			TextureRegion texReg = diffuseAtlas.findRegion(ref.fileName);
			Sprite sprite = new Sprite(diffuseAtlas.findRegion(ref.fileName));
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
		if(this.pack && this.packer != null)
			this.packer.dispose();
		else this.disposeNonPackedTextures();
	}

}
