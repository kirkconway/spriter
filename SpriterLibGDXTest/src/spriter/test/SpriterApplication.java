package spriter.test;

import spriter.utils.SpriterDrawer;
import spriter.utils.SpriterLoader;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;
import com.brashmonkey.spriter.Spriter;
import com.brashmonkey.spriter.objects.SpriterIKObject;
//import com.brashmonkey.spriter.player.SpriterPlayer;
import com.brashmonkey.spriter.player.SpriterPlayerIK;
import com.brashmonkey.spriter.player.SpriterPlayerInterpolator;

public class SpriterApplication implements ApplicationListener{
	
	//LibGDX related stuff
	SpriteBatch batch; //Needed to draw the sprites
	ShapeRenderer shape; //Needed to debug draw the skeleton
	OrthographicCamera cam; //Not needed but usefull, if you want to scroll through the world
	BitmapFont font;
	InputHandler inputHandler = new InputHandler();
	
	//Spriter related stuff
	SpriterPlayerIK player, player2; //Needed to play the animations of a spriter entity
	SpriterPlayerInterpolator inter;
	SpriterLoader loader; //Needed to load all textures
	SpriterDrawer drawer; //Needed to draw the sprites and debug draw the animations
	Spriter spriter; //The spriter data in an scml file
	SpriterIKObject ikObj, ikObj2;

	@Override
	public void create() {
		this.batch = new SpriteBatch();
		this.shape = new ShapeRenderer();
		this.font = new BitmapFont();
		this.cam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		this.cam.position.x = Gdx.graphics.getWidth()/2;
		this.cam.position.y = Gdx.graphics.getHeight()/2;
		
		this.loader = new SpriterLoader(true); //If the parameter in the constructor is true,
		//you will have the ability to pack all textures into one atlas, to increase performance
		this.drawer = new SpriterDrawer(this.batch); //A drawer needs a batch to draw
		//You can change the batch at runtime
		this.drawer.renderer = this.shape;
		
		this.loadSCML(Gdx.files.classpath("assets/monster/basic.scml")); //Loads the scml file
		Gdx.input.setInputProcessor(inputHandler);
		inputHandler.app = this;
	}

	@Override
	public void resize(int width, int height) {
		this.cam.viewportWidth = width;
		this.cam.viewportHeight = height;
		this.cam.update();
	}

	@Override
	public void render() {
		Gdx.gl.glDisable(GL20.GL_BLEND);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glEnable(GL20.GL_BLEND);
		this.cam.update();
		Vector3 mouse = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0f);
		this.cam.unproject(mouse);
		this.ikObj.setX(mouse.x); this.ikObj.setY(mouse.y);
		this.ikObj2.setX(mouse.x); this.ikObj2.setY(mouse.y);
		
		this.player.mapIKObject(ikObj, this.player.getBoneByName("leftHand"));
		this.player.mapIKObject(ikObj2, this.player.getBoneByName("rightHand"));
		this.player2.mapIKObject(ikObj, this.player2.getBoneByName("leftHand"));
		this.player2.mapIKObject(ikObj2, this.player2.getBoneByName("rightHand"));
		
		this.inter.update(cam.viewportWidth/2, cam.viewportHeight/2);//Call this method to perform the tweening for the current animation.
		this.inter.calcBoundingBox(null);//Calculate the bouding box for the current animation
		this.batch.setProjectionMatrix(cam.combined);
		Gdx.gl.glBlendFunc(GL20.GL_BLEND_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
			this.batch.begin();
				this.drawer.draw(this.inter); //The drawer is responsible for drawing the player
				//this.font.draw(batch, "Press left/right arrow keys to change animation. Press space to debug draw the animation.", cam.viewportWidth/2, 100);
				this.font.drawMultiLine(batch, "Press left/right arrow keys to change animation, up/down to change entity. Press space to debug draw the animation. Current entity: \""+
				this.player.getEntity().getName()+"\", animation: "+this.player.getAnimation().name, cam.viewportWidth/2, 100, 0, BitmapFont.HAlignment.CENTER);
				
			this.batch.end();
		
			if(Gdx.input.isKeyPressed(Keys.SPACE)) {
				//this.drawer.drawBoxes = false; //test it, the drawer will then just draw the bones and no boxes
				this.shape.begin(ShapeType.Line);
				this.drawer.debugDraw(inter);//Here the drawer draws all bounding boxes and bones from the current animation
				this.shape.end();
			}
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		//LibGDX related stuff
		this.shape.dispose();
		this.batch.dispose();
		this.font.dispose();
		
		this.loader.dispose(); //Don't forget to dispose the loader,
		//since it holds all the textures which have to be released from the memory
	}
	
	/**
	 * Loads the scml file and creates a player with the first entity in the given file.
	 * @param filename the file path of the scml file
	 */
	public void loadSCML(String filename){
		this.loader.dispose();
		this.loader = new SpriterLoader(true);
		this.drawer.loader = this.loader; //Set the loader for the drawer.
		this.loadSCML(Gdx.files.absolute(filename));
	}
	
	/**
	 * Loads the scml file and creates a player with the first entity in the given file.
	 * @param file the file handle of the scml file
	 */
	public void loadSCML(FileHandle file){
		this.ikObj = new SpriterIKObject(2, 10);
		this.ikObj2 = new SpriterIKObject(2, 10);
		
		this.spriter = Spriter.getSpriter(file.file().getAbsolutePath(), loader);
		this.player = new SpriterPlayerIK(this.spriter,0,loader);//Creates a new SpriterPlayer object which is responsible for tweening all animations in the loaded entity.
		this.player.setFrameSpeed(15);//Changes the frame speed
		this.player.mapIKObject(ikObj, this.player.getBoneByName("leftHand"));
		this.player.mapIKObject(ikObj2, this.player.getBoneByName("rightHand"));
		//this.player.deactivateEffectors(true);
		this.player.setResovling(false);
		
		this.player2 = new SpriterPlayerIK(this.spriter,0,loader);
		this.player2.setFrameSpeed(10);//Changes the frame speed
		this.player2.mapIKObject(ikObj, this.player2.getBoneByName("leftHand"));
		this.player2.mapIKObject(ikObj2, this.player2.getBoneByName("rightHand"));
		//this.player2.deactivateEffectors(true);
		this.player2.setResovling(false);
		this.inter = new SpriterPlayerInterpolator(this.player, this.player2);
		this.inter.setWeight(0);
	}
	
	/**
	 * Creates a new player object to animate the given entity.
	 * @param index the index of the entity.
	 * @throws RuntimeException is thrown if the entity index does not exist!
	 */
	public void loadEntity(int index) throws RuntimeException{
		if(index < 0 || index >= this.spriter.getSpriterData().getEntity().size()) throw new RuntimeException("This entity index does not exist: "+index);
		this.player = new SpriterPlayerIK(this.spriter, index, loader);
	}

}
