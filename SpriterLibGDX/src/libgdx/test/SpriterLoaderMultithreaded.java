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

package libgdx.test;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.utils.Disposable;
import com.brashmonkey.spriter.file.Reference;

/**
 * A loader class to load Spriter sprites inside LibGDX with multithreading support.
 * @author Trixt0r
 */

public class SpriterLoaderMultithreaded extends SpriterLoader implements Disposable{
	
	public SpriterLoaderMultithreaded(boolean pack){
		super(pack);
	}
	
	public SpriterLoaderMultithreaded(int atlasWidth, int atlasHeight){
		super(atlasWidth, atlasHeight);
	}
	
	@Override
	protected void createSprite(final Reference ref, final Pixmap image){
		Gdx.app.postRunnable(new Runnable(){
			@Override
			public void run() {
				SpriterLoaderMultithreaded.super.createSprite(ref, image);
			}
		});
	}
	
	@Override
	protected void generatePackedSprites(){
		Gdx.app.postRunnable(new Runnable(){
			@Override
			public void run() {
				SpriterLoaderMultithreaded.super.generatePackedSprites();
			}
		});
	}

}
