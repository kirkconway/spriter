package spriter.utils;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.brashmonkey.spriter.Point;
import com.brashmonkey.spriter.draw.AbstractDrawer;
import com.brashmonkey.spriter.draw.DrawInstruction;
import com.brashmonkey.spriter.file.FileLoader;
import com.brashmonkey.spriter.objects.SpriterBone;
import com.brashmonkey.spriter.objects.SpriterObject;
import com.brashmonkey.spriter.player.SpriterAbstractPlayer;

/**
 * A drawer class which draws Spriter animations in a proper way inside LibGDX.
 * @author Trixt0r
 */

public class SpriterDrawer extends AbstractDrawer<Sprite> {

	public SpriteBatch batch;
	public boolean drawNormals = false;
	public ShaderProgram shaderProgram;
	private float[] vertices = new float[10];
	
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
	
	/**
	 * Draws the playing animation in <code>player</code> with all bones and bounding boxes but without the corresponding sprites.
	 * @param shapeRenderer {@link [com.badlogic.gdx.graphics.glutils.]ShapeRenderer ShapeRenderer} to draw with. This object is not disposed by this drawer.
	 * @param player {@link [com.brashmonkey.spriter.player.]SpriterAbstractPlayer AbstractPlayer} to draw
	 */
	public void debugDraw(ShapeRenderer shapeRenderer, SpriterAbstractPlayer player){
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		for(int i = 0; i < player.getRuntimeBones().length; i++){
			this.setShapeColor(i, shapeRenderer);
			SpriterBone bone =  player.getRuntimeBones()[i];
			shapeRenderer.circle(bone.getX(), bone.getY(), 5);
		}
		
		shapeRenderer.end();
		shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
		for(int i = 0; i < player.getRuntimeBones().length; i++){
			this.setShapeColor(i, shapeRenderer);
			SpriterBone bone =  player.getRuntimeBones()[i];
			shapeRenderer.line(bone.getX(), bone.getY(), bone.getX()+(float)Math.cos(Math.toRadians(bone.getAngle()))*200*bone.getScaleX(), bone.getY()+(float)Math.sin(Math.toRadians(bone.getAngle()))*200*bone.getScaleX());
		}
			shapeRenderer.setColor(1f, 0f, 0f, 1f);
			shapeRenderer.rect(player.getBoundingBox().left, player.getBoundingBox().bottom, player.getBoundingBox().width, player.getBoundingBox().height);
		
		for(int j = 0; j< player.getObjectsToDraw(); j++){
			SpriterObject object = player.getRuntimeObjects()[j];
			int i = 0;
			Point[] points = object.getBoundingBox();
			
			vertices[i++] = points[0].x;
			vertices[i++] = points[0].y;
			vertices[i++] = points[1].x;
			vertices[i++] = points[1].y;
			vertices[i++] = points[3].x;
			vertices[i++] = points[3].y;
			vertices[i++] = points[2].x;
			vertices[i++] = points[2].y;
			vertices[i++] = points[0].x;
			vertices[i++] = points[0].y;
			
			shapeRenderer.polyline(vertices);
		}
		shapeRenderer.end();
	}
	
	private void setShapeColor(int i, ShapeRenderer shapeRenderer){
		switch(i%8){
		case 0: shapeRenderer.setColor(1f, 0f, 0f, 0.5f); break;
		case 1: shapeRenderer.setColor(0f, 1f, 0f, 0.5f); break;
		case 2: shapeRenderer.setColor(0f, 0f, 1f, 0.5f); break;
		case 3: shapeRenderer.setColor(0f, 1f, 1f, 0.5f); break;
		case 4: shapeRenderer.setColor(1f, 0f, 1f, 0.5f); break;
		case 5: shapeRenderer.setColor(1f, 1f, 0f, 0.5f); break;
		case 6: shapeRenderer.setColor(0f, 0f, 0f, 0.5f); break;
		case 7: shapeRenderer.setColor(1f, 1f, 1f, 0.5f); break;
		}
	}

}
