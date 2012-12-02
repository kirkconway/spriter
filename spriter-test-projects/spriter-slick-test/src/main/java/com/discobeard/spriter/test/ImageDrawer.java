package com.discobeard.spriter.test;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import com.discobeard.spriter.file.AbstractLoader;
import com.discobeard.spriter.draw.DrawInstruction;
import com.discobeard.spriter.draw.AbstractDrawer;

public class ImageDrawer extends AbstractDrawer<Image> {

	private float screenheight;
	private Graphics g;

	public ImageDrawer(AbstractLoader<Image> loader, float screenheight, Graphics g) {
		super(loader);
		this.screenheight = screenheight;
		this.g = g;
	}

	public void draw(Image image, float x, float y, float pivot_x, float pivot_y,float scale_x,float scale_y, float angle, float alpha) {

		float newPivotX = (image.getWidth() * pivot_x);
		float newX = x - newPivotX;

		float newPivotY = (image.getHeight() * pivot_y);
		float newY = (screenheight - y) - (image.getHeight() - newPivotY);

		//float width = image.getWidth()*scale_x;
		//float height = image.getHeight()*scale_y;
		
		g.rotate(x, (screenheight - y), -angle);
		image.setAlpha(alpha);
		image.draw(newX, newY,image.getWidth()*scale_x,image.getHeight()*scale_y);
		g.resetTransform();
	}

	@Override
	public void draw(DrawInstruction instruction) {
		draw(getFile(instruction.getRef()), instruction.getX(), instruction.getY(), instruction.getPivot_x(),
				instruction.getPivot_y(), instruction.getScale_x(), instruction.getScale_y(), instruction.getAngle(),
				instruction.getAlpha());
	}

}