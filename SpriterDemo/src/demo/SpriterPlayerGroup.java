package demo;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.brashmonkey.spriter.Spriter;
import com.brashmonkey.spriter.draw.AbstractDrawer;
import com.brashmonkey.spriter.file.FileLoader;
import com.brashmonkey.spriter.player.SpriterAbstractPlayer;
import com.brashmonkey.spriter.player.SpriterPlayer;

public class SpriterPlayerGroup extends Widget{
	
	public final SpriterAbstractPlayer player;
	public AbstractDrawer<?> drawer;
	public boolean drawSprites = true, drawBones = false, drawBoxes = false;
	private float prefWidth = 0, prefHeight = 0;
	
	public SpriterPlayerGroup(Spriter spriter, int entityIndex, FileLoader<?> loader){
		this.player = new SpriterPlayer(spriter, entityIndex, loader);
	}
	
	public SpriterPlayerGroup(SpriterAbstractPlayer player){
		this.player = player;
	}
	
	@Override
	public void act(float delta){
		super.act(delta);
		this.player.update(super.getParent().getX()+this.prefWidth/2-50, super.getParent().getY()+this.getParent().getHeight()/2-50 /*this.prefHeight/2+50*/);
		this.player.calcBoundingBox(null);
		this.prefWidth =Math.min( Math.max(this.prefWidth, this.player.getBoundingBox().width), 600);
		this.prefHeight = Math.min(Math.max(this.prefHeight, this.player.getBoundingBox().height), 450);
		super.invalidate();
		super.validate();
	}
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha){
		if(this.drawSprites) this.drawer.draw(player);
		this.drawer.drawBones = this.drawBones;
		this.drawer.drawBoxes = this.drawBoxes;
		this.drawer.debugDraw(this.player);
		super.draw(batch, parentAlpha);
	}
	
	@Override
	public float getPrefWidth(){
		return 0;
	}
	
	@Override
	public float getPrefHeight(){
		return prefHeight;
	}
	
	@Override
	public float getMaxWidth(){
		return 0;
	}
	
	@Override
	public float getMaxHeight(){
		return prefHeight;
	}

}
