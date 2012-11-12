package com.discobeard.spriter.test;
 
import java.io.File;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import com.discobeard.spriter.Spriter;
 
public class SlickTest extends BasicGame{
 
    float x = 400;
    float y = 300;
    float scale = 1;
    private Spriter spriter;
    
    public SlickTest()
    {
        super("Slick2D Spriter Importer Test");
    }
 
    @Override
    public void init(GameContainer gc)
			throws SlickException {
    	
    	ImageLoader loader = new ImageLoader();
    	Graphics g = gc.getGraphics();
		spriter = Spriter.getSpriter("monster/basic.scml",new ImageDrawer(loader,800,g),loader);
		spriter.playAnimation(2,true);
    }
 
    @Override
    public void update(GameContainer gc, int delta)
			throws SlickException
    {
    	
    }
 
    public void render(GameContainer gc, Graphics g)
			throws SlickException
    {
    	spriter.draw(400, 250);
    }
 
    public static void main(String[] args)
			throws SlickException
    {
    	 System.setProperty("org.lwjgl.librarypath",System.getProperty("user.dir") + File.separator + "target" + File.separator + "natives" + File.separator + System.getProperty("sun.desktop"));
         AppGameContainer app = new AppGameContainer( new SlickTest() );
         app.setDisplayMode(800, 600, false);
         app.setTargetFrameRate(60);
         app.start();
    }
}