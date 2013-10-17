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

package demo;

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
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.brashmonkey.spriter.file.FileLoader;
import com.brashmonkey.spriter.file.Reference;

/**
 * A loader class to load Spriter sprites inside LibGDX.
 * @author Trixt0r
 */

public class SpriterLoader extends FileLoader<Sprite> implements Disposable{
	
	private PixmapPacker packer;
	private boolean pack;
	private int atlasWidth, atlasHeight;
	
	public SpriterLoader(){
		this(true);
	}
	
	/**
	 * Creates a loader object to load all necessary sprites for playing Spriter animations.
	 * @param pack Indicates whether the loaded sprites have to be packed into an atlas (with a 1024x1024 dimension)
	 * or not (resolves performance issues if true).
	 */
	public SpriterLoader(boolean pack){
		this(1024, 1024);
		this.pack = pack;
	}
	
	/**
	 * Creates a loader object to load all necessary sprites for playing Spriter animations.
	 * @param pack Indicates whether the loaded sprites have to be packed into an atlas or not (resolves performance issues if true).
	 */
	/**
	 * Creates a loader object to load all necessary sprites for playing Spriter animations.
	 * @param atlasWidth The desired width of the atlas which is going to be generated.
	 * @param atlasHeight The desired height of the atlas which is going to be generated.
	 */
	public SpriterLoader(int atlasWidth, int atlasHeight){
		this.pack = true;
		if(!Gdx.graphics.isGL20Available() && (!MathUtils.isPowerOfTwo(atlasWidth) || !MathUtils.isPowerOfTwo(atlasHeight)))
			throw new GdxRuntimeException("Wrong dimensions for the texture atlas ("+atlasWidth+"x"+atlasHeight+")!\n"
					+ " Use OpenGL ES 2.0 or change the dimensions to power of two (e.g. "+MathUtils.nextPowerOfTwo(atlasWidth)+"x"+MathUtils.nextPowerOfTwo(atlasHeight)+")");
		this.atlasWidth = atlasWidth;
		this.atlasHeight = atlasHeight;
	}

	@Override
	public void load(final Reference ref, String path) {
		FileHandle f;
		switch(Gdx.app.getType()){
		case iOS: f = Gdx.files.absolute(path); break;
		default: f = Gdx.files.internal(path); break;
		}
		
		if(!f.exists()) throw new GdxRuntimeException("Could not find file handle "+ path + "! Please check your paths.");
		if(this.packer == null && this.pack)
			this.packer = new PixmapPacker(this.atlasWidth, this.atlasHeight, Pixmap.Format.RGBA8888, 2, true);
		final Pixmap pix;
		Texture tex;
		TextureRegion texRegion;
		if(!Gdx.graphics.isGL20Available()){
			Pixmap temp = new Pixmap(f);
			pix = new Pixmap(MathUtils.nextPowerOfTwo(temp.getWidth()), MathUtils.nextPowerOfTwo(temp.getHeight()), temp.getFormat());
			pix.drawPixmap(temp, 0, 0);
			tex = new Texture(pix);
			texRegion = new TextureRegion(tex, temp.getWidth(), temp.getHeight());
			temp.dispose();
		}
		else{
			pix = new Pixmap(f);
			tex = new Texture(pix);
			texRegion = new TextureRegion(tex, pix.getWidth(), pix.getHeight());
		}
		
		tex.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		if(this.packer != null)	packer.pack(ref.fileName, pix);
		
		this.files.put(ref, new Sprite(texRegion));
		pix.dispose();
	}
	
	/**
	 * Packs all loaded sprites into an atlas. Has to called after loading all sprites.
	 */
	public void generatePackedSprites(){
		if(this.packer == null) return;
		TextureAtlas tex = this.packer.generateTextureAtlas(TextureFilter.Linear, TextureFilter.Linear, false);
		Set<Reference> keys = this.files.keySet();
		this.disposeNonPackedTextures();
		for(Reference ref: keys){
			TextureRegion texReg = tex.findRegion(ref.fileName);
			texReg.setRegionWidth((int) ref.dimensions.width);
			texReg.setRegionHeight((int) ref.dimensions.height);
			Sprite sprite = new Sprite(texReg);			
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

	@Override
	public void finishLoading() { //This method basically calls the method to create an atlas for all loaded textures
		if(this.pack) generatePackedSprites();
	}

}
