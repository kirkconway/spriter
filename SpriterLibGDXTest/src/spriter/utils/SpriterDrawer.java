package spriter.utils;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.brashmonkey.spriter.draw.AbstractDrawer;
import com.brashmonkey.spriter.draw.DrawInstruction;
import com.brashmonkey.spriter.file.FileLoader;

/**
* A drawer class which draws Spriter animations in a proper way inside LibGDX.
* @author Trixt0r
*/

public class SpriterDrawer extends AbstractDrawer<Sprite> {

	public SpriteBatch batch;
	public ShapeRenderer renderer;
	public boolean drawNormals = false;
	public ShaderProgram shaderProgram;
	//private float[] vertices = new float[10];

	public SpriterDrawer(SpriteBatch batch){
		this(null, batch);
	}

	public SpriterDrawer(FileLoader<Sprite> loader, SpriteBatch batch) {
		super(loader);
		this.batch = batch;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void draw(DrawInstruction instruction) {
		this.loader = instruction.loader;
		draw(getFile(instruction.getRef()), instruction.getX(), instruction.getY(), instruction.getPivotX(),
		instruction.getPivotY(), instruction.getScaleX(), instruction.getScaleY(), instruction.getAngle(),
		instruction.getAlpha());
	}

	private void draw(Sprite sprite, float x, float y, float pivotX, float pivotY, float scaleX, float scaleY,
	float angle, float alpha) {
		if(sprite == null) return;
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
		/*if(this.drawNormals){
			sprite.setNormal();
			//this.shaderProgram.setAttributef("transform", angle, Math.signum(scaleX), Math.signum(scaleY), 0);
			sprite.normalSprite.draw(batch);
			batch.flush();
		}
		else*/
			sprite.draw(batch);
	}

	@Override
	protected void drawLine(float x1, float y1, float x2, float y2) {
		this.renderer.line(x1, y1, x2, y2);
	}

	@Override
	protected void drawRectangle(float x, float y, float width, float height) {
		this.renderer.rect(x, y, width, height);
	}

	@Override
	protected void setDrawColor(float r, float g, float b, float a) {
		this.renderer.setColor(r, g, b, a);
	}

}