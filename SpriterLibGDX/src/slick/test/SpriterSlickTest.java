package slick.test;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import com.brashmonkey.spriter.Spriter;
import com.brashmonkey.spriter.player.SpriterPlayer;

public class SpriterSlickTest extends BasicGame{
	 
    float x = 640;
    float y = 360;
    float scale = 1;
    private TextureDrawer drawer;
    private Spriter spriter;
    private SpriterPlayer spriterPlayer; 
    
    public SpriterSlickTest()
    {
        super("Spriter test for Slick2D");
    }
 
    @Override
    public void init(GameContainer gc)
			throws SlickException {
    	
    	TextureLoader loader = new TextureLoader();
    	Graphics g = gc.getGraphics();
    	this.drawer = new TextureDrawer(loader, 720, g);
		//spriter = Spriter.getSpriter("monster/basic.scml",new ImageDrawer(loader,800,g),loader);
		//spriter.playAnimation(2,true);
		
		spriter = Spriter.getSpriter("assets/monster/basic.scml", loader);
		spriterPlayer = new SpriterPlayer(spriter.getSpriterData(), 0, loader);
    }
 
    @Override
    public void update(GameContainer gc, int delta)
			throws SlickException
    {
    	spriterPlayer.update(x, y);
    }
 
    public void render(GameContainer gc, Graphics g)
			throws SlickException
    {
    	
    	drawer.draw(spriterPlayer);
    }
 
    public static void main(String[] args)
			throws SlickException
    {
    	 //System.setProperty("org.lwjgl.librarypath",System.getProperty("user.dir") + File.separator + "target" + File.separator + "natives" + File.separator + System.getProperty("sun.desktop"));
         AppGameContainer app = new AppGameContainer( new SpriterSlickTest() );
         app.setDisplayMode(1280, 720, false);
         app.setTargetFrameRate(60);
         app.start();
    }
}
