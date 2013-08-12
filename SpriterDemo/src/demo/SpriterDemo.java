package demo;

import java.io.IOException;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class SpriterDemo implements ApplicationListener{
	
	private SpriteBatch batch;
	private ShapeRenderer shapeRenderer;
	public Stage uiStage;
	private Skin skin;
	private SpriterLoader loader;
	private SpriterDrawer drawer;
	SpriterPlayerController leftController, rightController;

	@Override
	public void create() {
		this.batch = new SpriteBatch();
		this.shapeRenderer = new ShapeRenderer();
		this.uiStage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),
				false, this.batch);
		Gdx.input.setInputProcessor(this.uiStage);
		this.loader = new SpriterLoader(true);
		this.drawer = new SpriterDrawer(this.batch);
		this.drawer.renderer = this.shapeRenderer;
		this.skin = new Skin(Gdx.files.internal("data/buttons.json"));
		try {
			StageComposer.compose(uiStage, loader, drawer, skin);
		} catch (IOException e) {
			FileHandle f = Gdx.files.absolute("fail.txt");
			f.writeString(e.getMessage()+"\n", true);
			e.printStackTrace();
		}
	}

	@Override
	public void resize(int width, int height) {
		this.uiStage.setViewport(width, height, false);
	}

	@Override
	public void render() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		this.uiStage.act();
		
		this.shapeRenderer.setProjectionMatrix(uiStage.getCamera().combined);
		this.shapeRenderer.begin(ShapeType.Filled);
		this.shapeRenderer.rect(0, 0, uiStage.getCamera().viewportWidth, uiStage.getCamera().viewportHeight, Color.GRAY, Color.GRAY, Color.BLACK, Color.BLACK);
		this.shapeRenderer.end();
		
		this.shapeRenderer.begin(ShapeType.Line);
			this.uiStage.draw();
		this.shapeRenderer.end();
		this.shapeRenderer.begin(ShapeType.Filled);
			this.shapeRenderer.setColor(.05f, .1f, .5f, 1);
			this.shapeRenderer.rect(400-2.5f, 0, 5, Gdx.graphics.getHeight());
			this.shapeRenderer.rect(Gdx.graphics.getWidth()-410-2.5f, 0, 5, Gdx.graphics.getHeight());
		this.shapeRenderer.end();
		//Table.drawDebug(uiStage);
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
		this.shapeRenderer.dispose();
		this.uiStage.dispose();
		this.skin.dispose();
	}

}
