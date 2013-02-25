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

import java.io.IOException;
import java.math.BigDecimal;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.brashmonkey.spriter.Spriter;
import com.brashmonkey.spriter.file.FileLoader;
import com.discobeard.spriter.dom.*;

/**
 * This class was implemented to give you the chance loading scml files on android with libGDX since JAXB does not run on android devices.
 * If you are using libGDX, you should use this class to load scml files.
 * @author Trixt0r
 */
public class GdxSpriter {
	
	/**
	 * Loads a whole spriter file.
	 * @param filename Path to the scml file.
	 * @param loader The concrete loader you have implemented.
	 * @return Spriter instance which holds the read spriter structure.
	 */
	public static Spriter getSpriter(String filename, FileLoader<?> loader){
		FileHandle fh = Gdx.files.internal(filename);
		return new Spriter(load(fh),loader, fh.file());
	}
	
	/**
	 * Reads the whole given scml file.
	 * @param filename Path to scml file.
	 * @return Spriter data in form of lists.
	 */
	public static SpriterData load(String filename){
		return load(Gdx.files.internal(filename));
	}
	
	private static SpriterData load(FileHandle file){
		XmlReader reader = new XmlReader();
		SpriterData data = new SpriterData();
		try {
			Element root = reader.parse(file);
			loadFolders(root.getChildrenByName("folder"), data);
			loadEntities(root.getChildrenByName("entity"), data);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}
	
	private static void loadFolders(Array<Element> folders, SpriterData data){
		for(int i = 0; i < folders.size; i++){
			Element repo = folders.get(i);
			Folder folder = new Folder();
			folder.setId(repo.getInt("id")); folder.setName(repo.getAttribute("name", ""));
			Array<Element> files = repo.getChildrenByName("file");
			for(int j = 0; j < files.size; j++){
				Element f = files.get(j);
				File file = new File();
				file.setId(f.getInt("id")); file.setName(f.getAttribute("name", ""));
				file.setWidth((long)f.getInt("width"));
				file.setHeight((long)f.getInt("height"));
				folder.getFile().add(file);
			}
			data.getFolder().add(folder);
		}
	}

	private static void loadEntities(Array<Element> entities, SpriterData data){
		for(int i = 0; i < entities.size; i++){
			Element e = entities.get(i);
			Entity entity = new Entity();
			entity.setId(e.getInt("id")); entity.setName(e.getAttribute("name", ""));
			data.getEntity().add(entity);
			loadAnimations(e.getChildrenByName("animation"), entity);
		}
	}
	
	private static void loadAnimations(Array<Element> animations, Entity entity){
		for(int i = 0; i < animations.size; i++){
			Element a = animations.get(i);
			Animation animation = new Animation();
			animation.setId(a.getInt("id"));
			animation.setName(a.getAttribute("name", ""));
			animation.setLength((long)a.getInt("length"));
			animation.setLooping(a.getBoolean("looping", false));
			entity.getAnimation().add(animation);
			loadMainline(a.getChildByName("mainline"), animation);
			loadTimelines(a.getChildrenByName("timeline"), animation);
		}
	}
	
	private static void loadMainline(Element mainline, Animation animation){
		MainLine main = new MainLine();
		animation.setMainline(main);
		loadMainlineKeys(mainline.getChildrenByName("key"),main);
	}
	
	private static void loadMainlineKeys(Array<Element> keys, MainLine main){
		for(int i = 0; i < keys.size; i++){
			Element k = keys.get(i);
			Key key = new Key();
			key.setId(k.getInt("id"));
			int time = k.getInt("time",-1);
			key.setTime((time == -1) ? null: new Long(time));
			main.getKey().add(key);
			loadRefs(k.getChildrenByName("object_ref"),k.getChildrenByName("bone_ref"),key);
		}
	}
	
	private static void loadRefs(Array<Element> objectRefs, Array<Element> boneRefs, Key key){
		for(int i = 0; i< boneRefs.size; i++){
			Element o = boneRefs.get(i);
			BoneRef bone = new BoneRef();
			bone.setId(o.getInt("id"));
			bone.setKey(o.getInt("key"));
			int par = o.getInt("parent",-1);
			bone.setParent((par == -1) ? null : par);
			bone.setTimeline(o.getInt("timeline"));
			key.getBoneRef().add(bone);
		}
		
		for(int i = 0; i < objectRefs.size; i++){
			Element o = objectRefs.get(i);
			AnimationObjectRef object = new AnimationObjectRef();
			object.setId(o.getInt("id"));
			object.setKey(o.getInt("key"));
			int par = o.getInt("parent", -1);
			object.setParent((par == -1) ? null : par);
			object.setTimeline(o.getInt("timeline"));
			object.setZIndex(o.getInt("z_index"));
			key.getObjectRef().add(object);
		}
	}
	
	private static void loadTimelines(Array<Element> timelines, Animation animation){
		for(int i = 0; i< timelines.size; i++){
			TimeLine timeline = new TimeLine();
			timeline.setId(timelines.get(i).getInt("id"));
			animation.getTimeline().add(timeline);
			loadTimelineKeys(timelines.get(i).getChildrenByName("key"), timeline);
		}
	}
	
	private static void loadTimelineKeys(Array<Element> keys, TimeLine timeline){
		for(int i = 0; i< keys.size; i++){
			Element k = keys.get(i);
			Key key = new Key();
			key.setId(k.getInt("id")); key.setSpin(k.getInt("spin", 1));
			key.setTime(new Long(k.getInt("time", 0)));
			try{
				String name = k.getParent().getAttribute("name");
				Element obj = k.getChildByName("bone");
				timeline.setName(name);
				Bone bone = new Bone();
				bone.setAngle(new BigDecimal(obj.getFloat("angle", 0f)));
				bone.setX(new BigDecimal(obj.getFloat("x", 0f)));
				bone.setY(new BigDecimal(obj.getFloat("y", 0f)));
				bone.setScaleX(new BigDecimal(obj.getFloat("scale_x", 1f)));
				bone.setScaleY(new BigDecimal(obj.getFloat("scale_y", 1f)));
				key.setBone(bone);
			}
			catch(GdxRuntimeException e){
				AnimationObject object = new AnimationObject();
				Element obj = k.getChildByName("object");
				object.setAngle(new BigDecimal(obj.getFloat("angle", 0f)));
				object.setX(new BigDecimal(obj.getFloat("x", 0f)));
				object.setY(new BigDecimal(obj.getFloat("y", 0f)));
				object.setScaleX(new BigDecimal(obj.getFloat("scale_x", 1f)));
				object.setScaleY(new BigDecimal(obj.getFloat("scale_y", 1f)));
				object.setPivotX(new BigDecimal(obj.getFloat("pivot_x", 0f)));
				object.setPivotY(new BigDecimal(obj.getFloat("pivot_y", 1f)));
				object.setFolder(obj.getInt("folder")); 
				object.setFile(obj.getInt("file"));
				key.getObject().add(object);
			}
			timeline.getKey().add(key);
		}
	}
	
}

