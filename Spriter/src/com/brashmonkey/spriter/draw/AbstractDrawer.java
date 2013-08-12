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

package com.brashmonkey.spriter.draw;


import com.brashmonkey.spriter.SpriterPoint;
import com.brashmonkey.spriter.file.FileLoader;
import com.brashmonkey.spriter.file.Reference;
import com.brashmonkey.spriter.objects.SpriterBone;
import com.brashmonkey.spriter.player.SpriterAbstractPlayer;

/**
 * An AbstractDrawer is an object which is able to draw an animated entity.
 * To use a drawer, you have to implement {@link #draw(DrawInstruction)} which fits to your environment.
 * @author Trixt0r
 * @param <L> A resource which has been loaded by a concrete #FileLoader. Something like a sprite.
 */
public abstract class AbstractDrawer<L> {
	
	public FileLoader<L> loader;
	
	public static float BONE_LENGTH = 200, BONE_WIDTH = 10;
	
	public boolean drawBones = true, drawBoxes = true;
	
	public AbstractDrawer(FileLoader<L> loader){
		this.loader=loader;
	}
	
	/**
	 * Draws a sprite with the given instruction.
	 * @param instruction Instruction to draw with.
	 */
	abstract public void draw(DrawInstruction instruction);
	
	/**
	* Draws the playing animation in <code>player</code> with all bones (if {@link #drawBones} is true) and bounding boxes (if {@link #drawBoxes} is true) but without the corresponding sprites.
	* @param player {@link [com.brashmonkey.spriter.player.]SpriterAbstractPlayer AbstractPlayer} to draw
	*/
	public void debugDraw(SpriterAbstractPlayer player){
		if(drawBoxes)this.drawBoxes(player);
		if(drawBones)this.drawBones(player);
	}
	
	protected void drawBones(SpriterAbstractPlayer player){
		for(int i = 0; i < player.getBonesToAnimate(); i++){
			SpriterBone bone = player.getRuntimeBones()[i];
			if(bone.active) this.setDrawColor(1, 0, 0, 1);
			else this.setDrawColor(0, 1, 1, 1);
			float xx = bone.getX()+(float)Math.cos(Math.toRadians(bone.getAngle()))*5;
			float yy = bone.getY()+(float)Math.sin(Math.toRadians(bone.getAngle()))*5;
			float x2 = (float)Math.cos(Math.toRadians(bone.getAngle()+90))*(BONE_WIDTH/2)*bone.getScaleY();
			float y2 = (float)Math.sin(Math.toRadians(bone.getAngle()+90))*(BONE_WIDTH/2)*bone.getScaleY();
			
			float targetX = bone.getX()+(float)Math.cos(Math.toRadians(bone.getAngle()))*BONE_LENGTH*bone.getScaleX(),
					targetY = bone.getY()+(float)Math.sin(Math.toRadians(bone.getAngle()))*BONE_LENGTH*bone.getScaleX();
			float upperPointX = xx+x2, upperPointY = yy+y2;
			this.drawLine(bone.getX(), bone.getY(), upperPointX, upperPointY);
			this.drawLine(upperPointX, upperPointY, targetX, targetY);

			float lowerPointX = xx-x2, lowerPointY = yy-y2;
			this.drawLine(bone.getX(), bone.getY(), lowerPointX, lowerPointY);
			this.drawLine(lowerPointX, lowerPointY, targetX, targetY);
			this.drawLine(bone.getX(), bone.getY(), targetX, targetY);
		}
	}
	
	protected void drawBoxes(SpriterAbstractPlayer player){
		this.setDrawColor(.25f, 1f, .25f, 1f);
		this.drawRectangle(player.getBoundingBox().left, player.getBoundingBox().bottom, player.getBoundingBox().width, player.getBoundingBox().height);
		
		for(int j = 0; j< player.getObjectsToDraw(); j++){
			SpriterPoint[] points = player.getRuntimeObjects()[j].getBoundingBox();
			this.drawLine(points[0].x, points[0].y, points[1].x, points[1].y);
			this.drawLine(points[1].x, points[1].y, points[3].x, points[3].y);
			this.drawLine(points[3].x, points[3].y, points[2].x, points[2].y);
			this.drawLine(points[2].x, points[2].y, points[0].x, points[0].y);
		}
	}
	
	abstract protected void setDrawColor(float r, float g, float b, float a);
	abstract protected void drawLine(float x1, float y1, float x2, float y2);
	abstract protected void drawRectangle(float x, float y, float width, float height);
	
	/**
	 * Draws the given player with its sprites.
	 * @param player Player to draw.
	 */
	public void draw(SpriterAbstractPlayer player){
		DrawInstruction[] instructions = player.getDrawInstructions();
		for(int i = 0; i< player.getObjectsToDraw(); i++){
			if(instructions[i].obj.isVisible()) this.draw(instructions[i]);
			for(SpriterAbstractPlayer pl: player.getAttachedPlayers())
				if(player.getZIndex() == i){
					draw(pl);
					pl.drawn = true;
				}
		}
		for(SpriterAbstractPlayer pl: player.getAttachedPlayers()){
			if(!player.drawn) draw(pl);
			player.drawn = false;
		}
	}
	
	protected L getFile(Reference reference){
		return loader.get(reference);
	}
}
