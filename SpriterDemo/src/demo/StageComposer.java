package demo;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.brashmonkey.spriter.Spriter;
import com.brashmonkey.spriter.draw.AbstractDrawer;
import com.brashmonkey.spriter.file.FileLoader;
import com.brashmonkey.spriter.player.SpriterPlayerInterpolator;

public class StageComposer {
	
	public static SpriterPlayerController controller1, controller2;
	public static PlayerInterpolationControl middleController;
	
	public static void compose(Stage stage, FileLoader<?> loader, AbstractDrawer<?> drawer, Skin skin) throws IOException{
		Spriter spriter = Spriter.getSpriter(Gdx.files.internal("monster/basic.scml").file(), loader);
		Table table = new Table(skin);
		//table.debug();
		table.setFillParent(true);
		controller1 = new SpriterPlayerController(spriter, 0, loader, skin);
		controller1.playerGroup.drawer = drawer;
		controller2 = new SpriterPlayerController(spriter, 0, loader, skin);
		controller2.playerGroup.drawer = drawer;
		SpriterPlayerInterpolator interpolator = new SpriterPlayerInterpolator(controller1.playerGroup.player, controller2.playerGroup.player);
		interpolator.updatePlayers = false;
		middleController = new PlayerInterpolationControl(interpolator, skin);
		middleController.playerGroup.drawer = drawer;
		
		table.add(controller1).pad(10);
		table.add(middleController);
		table.add(controller2).pad(10);
		
		stage.addActor(table);
	}

}
