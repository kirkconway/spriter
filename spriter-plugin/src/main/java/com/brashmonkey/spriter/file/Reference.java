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

package com.brashmonkey.spriter.file;

import com.brashmonkey.spriter.SpriterRectangle;

/**
 * A Reference is an object which holds a loaded sprite.
 * @author Trixt0r
 */
public class Reference {
	
	public int folder, file;
	public String folderName, fileName;
	public SpriterRectangle dimensions;
	
	public Reference(int folder,int file, String folderName, String fileName){
		this.folder = folder;
		this.file = file;
		this.folderName = folderName;
		this.fileName = fileName;
	}
	public Reference(int folder,int file){
		this(folder,file,null,null);
	}

	public int getFolder() {
		return folder;
	}

	public int getFile() {
		return file;
	}
	
	@Override
	public int hashCode(){
		return (folder+","+file).hashCode();
	}
	
	@Override
	public boolean equals(Object ref){
		return ((Reference)ref).hashCode()==this.hashCode();
	}
}
