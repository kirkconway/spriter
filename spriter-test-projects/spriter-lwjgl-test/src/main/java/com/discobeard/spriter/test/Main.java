package com.discobeard.spriter.test;
import java.io.File;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

public class Main {
	
	/** time at last frame */
	private long lastFrame;
	/** frames per second */
	private int fps;
	/** last fps time */
	private long lastFPS;
	
	public void start() {
		
		try {
			Display.setDisplayMode(new DisplayMode(800,600));
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		initGL();
		
		getDelta();
		lastFPS = getTime(); 
		
		SpriterTest spriterTest = new SpriterTest();
		//NanoBots nanoBots = new NanoBots();
		
		while (!Display.isCloseRequested()) {
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA,GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
			
			spriterTest.draw();
			
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
		        GLU.gluOrtho2D(0.0f,800.0f,0,600.0f);
		        GL11.glMatrixMode(GL11.GL_MODELVIEW); 
		        //GL11.glPolygonMode(GL11.GL_FRONT, GL11.GL_LINE);
				//GL11.glPolygonMode(GL11.GL_BACK, GL11.GL_LINE);
		        
	}
	
	public static void main(String[] argv) {
		System.setProperty("org.lwjgl.librarypath",System.getProperty("user.dir") + File.separator + "target" + File.separator + "natives" + File.separator + System.getProperty("sun.desktop"));
		Main main = new Main();
		main.start();
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
			Display.setTitle("FPS: " + fps);
			fps = 0;
			lastFPS += 1000;
		}
		fps++;
	}
}
