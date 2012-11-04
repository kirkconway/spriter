package com.discobeard.spriter.test;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import com.discobeard.spriter.FileLoader;
import com.discobeard.spriter.Reference;
import com.discobeard.spriter.draw.Drawer;

public class TextureDrawer implements Drawer{
	
	private final FileLoader<Texture> TEXTURE_LOADER;
	
	public TextureDrawer(FileLoader<Texture> loader){
		this.TEXTURE_LOADER =loader;
	}
	
	public void draw(Texture texture,float x, float y, float pivot_x,float pivot_y,float angle)
	{
		GL11.glLoadIdentity();            
		GL11.glTranslatef(x,y,0);
		GL11.glRotatef(angle,0f,0f,1f);

		float original_width = texture.getTextureWidth();
		float original_height = texture.getTextureHeight();
				
		
		float textureDown = 0;
		float textureUp = textureDown + (1/(original_height/texture.getImageHeight()));
		float textureLeft = 0;
		float textureRight = textureLeft+(1/(original_width/texture.getImageWidth()));

		texture.bind();
		
		GL11.glBegin(GL11.GL_QUADS);
		
			GL11.glTexCoord2f(textureLeft,textureUp); //Upper left	
			GL11.glVertex2f(-(texture.getImageWidth()*pivot_x), -((texture.getImageHeight()*pivot_y))+0.01f);

			GL11.glTexCoord2f(textureRight,textureUp); //Upper right
			GL11.glVertex2f((-(texture.getImageWidth()*pivot_x))+texture.getImageWidth(), (-(texture.getImageHeight()*pivot_y)+0.01f));

			GL11.glTexCoord2f(textureRight,textureDown); // Lower right	
			GL11.glVertex2f((-(texture.getImageWidth()*pivot_x))+texture.getImageWidth(), (-(texture.getImageHeight()*pivot_y)+0.01f)+texture.getImageHeight());
			
			GL11.glTexCoord2f(textureLeft,textureDown); //Lower left
			GL11.glVertex2f(-(texture.getImageWidth()*pivot_x), (-(texture.getImageHeight()*pivot_y)+0.01f)+texture.getImageHeight());

       	GL11.glEnd(); 
	}
	
	@Override
	public void draw(Reference ref, float x, float y, float pivot_x,float pivot_y, float angle) {
		draw(TEXTURE_LOADER.get(ref),x ,y,pivot_x,pivot_y,angle);
	}
	
}