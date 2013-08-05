package spriter.test;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class SpriterApplicationRunner {
	
	public static void main(String[] agrs){
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.useGL20 = true;
		cfg.fullscreen = false;
		cfg.width = 1280;
		cfg.height = 720;
		cfg.title = "LibGDX-Spriter-Test @"+cfg.width+"x"+cfg.height;
		
		new LwjglApplication(new SpriterApplication(), cfg);
	}

}
