package lwjgl.test;

import static org.lwjgl.opengl.GL11.*;

import org.newdawn.slick.opengl.Texture;

import com.brashmonkey.spriter.draw.AbstractDrawer;
import com.brashmonkey.spriter.draw.DrawInstruction;
import com.brashmonkey.spriter.file.FileLoader;

public class TextureDrawer extends AbstractDrawer<Texture> {

	public TextureDrawer(FileLoader<Texture> loader) {
		super(loader);
	}

	public void draw(Texture texture, float x, float y, float pivot_x, float pivot_y, float scale_x, float scale_y,
			float angle, float alpha) {
		glLoadIdentity();
		glTranslatef(x, y, 0);
		glRotatef(angle, 0f, 0f, 1f);

		float original_width = texture.getTextureWidth();
		float original_height = texture.getTextureHeight();

		float textureDown = 0;
		float textureUp = textureDown + (1 / (original_height / texture.getImageHeight()));
		float textureLeft = 0;
		float textureRight = textureLeft + (1 / (original_width / texture.getImageWidth()));

		texture.bind();

		glBegin(GL_QUADS);

		glColor4f(1.0f, 1.0f, 1.0f, alpha);
		glTexCoord2f(textureLeft, textureUp); // Upper left
		glVertex2f(-((texture.getImageWidth()*scale_x) * pivot_x), -(((texture.getImageHeight()*scale_y) * pivot_y)));

		glColor4f(1.0f, 1.0f, 1.0f, alpha);
		glTexCoord2f(textureRight, textureUp); // Upper right
		glVertex2f((-((texture.getImageWidth()*scale_x) * pivot_x)) + (texture.getImageWidth()*scale_x),
				(-((texture.getImageHeight()*scale_y) * pivot_y)));

		glColor4f(1.0f, 1.0f, 1.0f, alpha);
		glTexCoord2f(textureRight, textureDown); // Lower right
		glVertex2f((-((texture.getImageWidth()*scale_x) * pivot_x)) + (texture.getImageWidth()*scale_x),
				(-((texture.getImageHeight()*scale_y) * pivot_y)) + (texture.getImageHeight()*scale_y));

		glColor4f(1.0f, 1.0f, 1.0f, alpha);
		glTexCoord2f(textureLeft, textureDown); // Lower left
		glVertex2f(-((texture.getImageWidth()*scale_x) * pivot_x),
				(-((texture.getImageHeight()*scale_y) * pivot_y)) + (texture.getImageHeight()*scale_y));

		glEnd();
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
