package spriter.test;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class SpriterApplicationRunner {
	
	public static void main(String[] agrs) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException{
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.useGL20 = true;
		cfg.width = 1280;
		cfg.height = 720;
		cfg.title = "LibGDX-Spriter-Test @"+cfg.width+"x"+cfg.height;
		new LwjglApplication(new SpriterApplication(), cfg);
	}

}
