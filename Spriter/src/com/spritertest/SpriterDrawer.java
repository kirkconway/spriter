package com.spritertest;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.spriter.draw.AbstractDrawer;
import com.spriter.draw.DrawInstruction;
import com.spriter.file.FileLoader;

public class SpriterDrawer extends AbstractDrawer<Sprite> {

	private final SpriteBatch batch;
	
	public SpriterDrawer(FileLoader<Sprite> loader, SpriteBatch batch) {
		super(loader);
		this.batch = batch;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void draw(DrawInstruction instruction) {
		this.loader = instruction.loader;
		if(instruction.obj.isVisible())
			draw(getFile(instruction.getRef()), instruction.getX(), instruction.getY(), instruction.getPivotX(),
				instruction.getPivotY(), instruction.getScaleX(), instruction.getScaleY(), instruction.getAngle(),
				instruction.getAlpha());

	}

	private void draw(Sprite sprite, float x, float y, float pivotX, float pivotY, float scaleX, float scaleY,
			float angle, float alpha) {

		float newPivotX = (sprite.getWidth() * (pivotX));
		float newX = x - newPivotX;
		float newPivotY = (sprite.getHeight() * (pivotY));
		float newY = y - newPivotY;
		
		sprite.setX(newX);
		sprite.setY(newY);
		
		sprite.setOrigin(newPivotX, newPivotY);
		sprite.setRotation(angle);
		
		sprite.setColor(1f, 1f, 1f, alpha);
		sprite.setScale(scaleX, scaleY);

		sprite.draw(batch);
	}

}
