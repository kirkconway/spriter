package com.discobeard.spriter.test;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import com.discobeard.spriter.FileLoader;
import com.discobeard.spriter.Reference;
import com.discobeard.spriter.draw.Drawer;

public class ImageDrawer implements Drawer {

	private final FileLoader<Image> IMAGE_LOADER;
	private float screenheight;
	private Graphics g;

	public ImageDrawer(FileLoader<Image> loader, float screenheight, Graphics g) {
		this.IMAGE_LOADER = loader;
		this.screenheight = screenheight;
		this.g = g;
	}

	public void draw(Image image, float x, float y, float pivot_x, float pivot_y, float angle) {

		float newPivotX = (image.getWidth() * pivot_x);
		float newX = x - newPivotX;
		
		float newPivotY = (image.getHeight() * pivot_y);
		float newY = (screenheight - y) - (image.getHeight() - newPivotY);

		g.rotate(x, (screenheight - y), -angle);
		
		image.draw(newX, newY);
		g.resetTransform();
	}

	@Override
	public void draw(Reference ref, float x, float y, float pivot_x, float pivot_y, float angle) {
		draw(IMAGE_LOADER.get(ref), x, y, pivot_x, pivot_y, angle);
	}

}