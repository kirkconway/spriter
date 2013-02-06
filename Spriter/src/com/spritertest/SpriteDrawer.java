package com.spritertest;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.spriter.draw.AbstractDrawer;
import com.spriter.draw.DrawInstruction;
import com.spriter.file.FileLoader;

public class SpriteDrawer extends AbstractDrawer<Sprite> {

	private final SpriteBatch batch;
	
	public SpriteDrawer(FileLoader<Sprite> loader, SpriteBatch batch) {
		super(loader);
		this.batch = batch;
	}

	@Override
	public void draw(DrawInstruction instruction) {

		draw(getFile(instruction.getRef()), instruction.getX(), instruction.getY(), instruction.getPivotX(),
				instruction.getPivotY(), instruction.getScaleX(), instruction.getScaleY(), instruction.getAngle(),
				instruction.getAlpha());

	}

	private void draw(Sprite sprite, float x, float y, float pivot_x, float pivot_y, float scale_x, float scale_y,
			float angle, float alpha) {

		float newPivotX = (sprite.getWidth() * (pivot_x));
		float newX = x - newPivotX;
		float newPivotY = (sprite.getHeight() * (pivot_y));
		float newY = y - newPivotY;
		
		sprite.setX(newX);
		sprite.setY(newY);
		
		sprite.setOrigin(newPivotX, newPivotY);
		sprite.setRotation(angle);
		
		sprite.setColor(1f, 1f, 1f, alpha);
		sprite.setScale(scale_x, scale_y);

		sprite.draw(batch);
	}

}
