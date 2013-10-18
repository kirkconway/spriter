package lwjgl.test;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.newdawn.slick.opengl.Texture;

import com.brashmonkey.spriter.Spriter;
import com.brashmonkey.spriter.file.FileLoader;
import com.brashmonkey.spriter.player.SpriterPlayer;


public class SpriterTestLWJGL {

	private FileLoader<Texture> loader;
	private TextureDrawer drawer;

	SpriterPlayer spriterPlayer; 
	Spriter spriter;
	
	public SpriterTestLWJGL(){
		loader = new TextureLoader();
		drawer = new TextureDrawer(loader);
		
		this.start();
	}
	
	public void draw(){
		spriterPlayer.update(640, 360);
		drawer.draw(spriterPlayer);
	}
	

	
	/** time at last frame */
	private long lastFrame;
	/** frames per second */
	private int fps;
	/** last fps time */
	private long lastFPS;
	
	public void start() {
		
		try {
			Display.setDisplayMode(new DisplayMode(1280,720));
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		initGL();
		
		spriter = Spriter.getSpriter("assets/monster/basic.scml", loader);
		spriterPlayer = new SpriterPlayer(spriter.getSpriterData(), 0, loader);
		
		getDelta();
		lastFPS = getTime(); 
		//NanoBots nanoBots = new NanoBots();
		
		while (!Display.isCloseRequested()) {
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA,GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
			
			draw();
			
			Display.update();
			
			Display.sync(60);
			
			updateFPS();
			
		}
		
		Display.destroy();
	}
	
	
	public void initGL(){
	        GL11.glEnable(GL11.GL_TEXTURE_2D);
	        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
	        GL11.glEnable(GL11.GL_BLEND);
	        GL11.glBlendFunc(GL11.GL_SRC_ALPHA,GL11.GL_ONE_MINUS_SRC_ALPHA);
	        GL11.glMatrixMode(GL11.GL_PROJECTION); 
	        GL11.glLoadIdentity();
	        GLU.gluOrtho2D(0.0f,1280.0f,0,720.0f);
	        GL11.glMatrixMode(GL11.GL_MODELVIEW); 
	        //GL11.glPolygonMode(GL11.GL_FRONT, GL11.GL_LINE);
			//GL11.glPolygonMode(GL11.GL_BACK, GL11.GL_LINE);
		        
	}
	
	public static void main(String[] argv) {
		new SpriterTestLWJGL();
	}
	
	/** 
	 * Calculate how many milliseconds have passed 
	 * since last frame.
	 * 
	 * @return milliseconds passed since last frame 
	 */
	public int getDelta() {
	    long time = getTime();
	    int delta = (int) (time - lastFrame);
	    lastFrame = time;
 
	    return delta;
	}
 
	/**
	 * Get the accurate system time
	 * 
	 * @return The system time in milliseconds
	 */
	public long getTime() {
	    return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}
	
	/**
	 * Calculate the FPS and set it in the title bar
	 */
	public void updateFPS() {
		if (getTime() - lastFPS > 1000) {
			Display.setTitle("Spriter test for LWJGL - "+"FPS: " + fps);
			fps = 0;
			lastFPS += 1000;
		}
		fps++;
	}
}
