package slick.test;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import com.brashmonkey.spriter.draw.AbstractDrawer;
import com.brashmonkey.spriter.draw.DrawInstruction;
import com.brashmonkey.spriter.file.FileLoader;

public class TextureDrawer extends AbstractDrawer<Image> {

	private float screenheight;
	private Graphics g;

	public TextureDrawer(FileLoader<Image> loader, float screenheight, Graphics g) {
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
		draw(getFile(instruction.getRef()), instruction.getX(), instruction.getY(), instruction.getPivotX(),
				instruction.getPivotY(), instruction.getScaleX(), instruction.getScaleY(), instruction.getAngle(),
				instruction.getAlpha());
	}

	@Override
	protected void setDrawColor(float r, float g, float b, float a) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void drawLine(float x1, float y1, float x2, float y2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void drawRectangle(float x, float y, float width, float height) {
		// TODO Auto-generated method stub
		
	}

}
