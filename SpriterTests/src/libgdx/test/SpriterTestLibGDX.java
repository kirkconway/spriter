package libgdx.test;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.brashmonkey.spriter.Spriter;
import com.brashmonkey.spriter.player.SpriterAbstractPlayer;
import com.brashmonkey.spriter.player.SpriterPlayer;
import com.brashmonkey.spriter.xml.FileHandleSCMLReader;

public class SpriterTestLibGDX implements ApplicationListener{
	
	public static void main(String... args){
		LwjglApplicationConfiguration cfg =  new LwjglApplicationConfiguration();
		cfg.title = "Spriter test for LibGDX";
		cfg.useGL20 = false;
		cfg.width = 1280;
		cfg.height = 720;
		cfg.resizable = false;
		new LwjglApplication(new SpriterTestLibGDX(), cfg);
	}

	private SpriteBatch batch;
	private OrthographicCamera cam;
	private SpriteLoader loader;
	private SpriteDrawer drawer;
	private SpriterAbstractPlayer player;
	private Spriter spriter;
	
	@Override
	public void create() {
		this.batch = new SpriteBatch();
		this.cam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		this.loader = new SpriteLoader(2048, 2048);
		this.drawer = new SpriteDrawer(this.loader, this.batch);
		
		this.spriter = FileHandleSCMLReader.getSpriter(Gdx.files.internal("monster/basic.scml"), this.loader);
		this.player = new SpriterPlayer(this.spriter.getSpriterData(), 0, this.loader);
	}

	@Override
	public void resize(int width, int height) {
		this.cam.setToOrtho(false, width, height);
		this.batch.setProjectionMatrix(this.cam.combined);
	}

	@Override
	public void render() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		this.player.update(this.cam.viewportWidth/2, this.cam.viewportHeight/2);
		
		this.batch.begin();
			this.drawer.draw(player);
		this.batch.end();
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		this.batch.dispose();
		this.loader.dispose();
	}

}
