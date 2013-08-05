package spriter.test;

import spriter.utils.GdxSpriter;
import spriter.utils.SpriterDrawer;
import spriter.utils.SpriterLoader;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.brashmonkey.spriter.player.SpriterPlayer;

public class SpriterApplication implements ApplicationListener{
	
	//LibGDX related stuff
	SpriteBatch batch; //Needed to draw the sprites
	ShapeRenderer shape; //Needed to debug draw the skeleton
	OrthographicCamera cam; //Not needed but usefull, if you want to scroll through the world :)
	
	//Spriter related stuff
	SpriterPlayer player; //Needed to play the animations of a spriter entity
	SpriterLoader loader; //Needed to load all textures
	SpriterDrawer drawer; //Needed to draw the sprites and debug draw the animations

	@Override
	public void create() {
		this.batch = new SpriteBatch();
		this.shape = new ShapeRenderer();
		this.cam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		this.loader = new SpriterLoader(true); //If the parameter in the constructor is true,
		//you will have the ability to pack all textures into one atlas, to increase performance
		this.drawer = new SpriterDrawer(this.batch); //A drawer needs a batch to draw
		//You can change the batch at runtime
		
		player = new SpriterPlayer(GdxSpriter.getSpriter("monster/basic.scml", loader),0, loader);
		//Creates a new SpriterPlayer object which is responsible for tweening all animations in the loaded entity.
		player.setFrameSpeed(20);//Changes the frame speed
		
		Gdx.input.setInputProcessor(new InputAdapter(){
			@Override
			public boolean keyDown(int keycode){
				if(keycode == Keys.LEFT){
					try{
						player.setAnimatioIndex(player.getAnimationIndex()-1, 1, 10);
					} catch(RuntimeException e){
						System.out.println(e.getMessage());
					}
				}
				if(keycode == Keys.RIGHT){
					try{
						player.setAnimatioIndex(player.getAnimationIndex()+1, 1, 10);
					} catch(RuntimeException e){
						System.out.println(e.getMessage());
					}
				}
				return false;
			}
		});
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void render() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		this.player.update(cam.viewportWidth/2, cam.viewportHeight/2);//Call this method to perform the tweening for the current animation.
		
		this.batch.begin();
			this.drawer.draw(this.player); //The drawer is responsible for drawing the player
		this.batch.end();
		
		if(Gdx.input.isKeyPressed(Keys.SPACE)) 
			this.drawer.debugDraw(shape, player);//Here the drawer draws all bounding boxes and bones from the current animation
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		//LibGDX related stuff
		this.shape.dispose();
		this.batch.dispose();
		
		this.loader.dispose(); //Don't forget to dispose the loader,
		//since it holds all the textures which have to be released from the memory
	}

}
