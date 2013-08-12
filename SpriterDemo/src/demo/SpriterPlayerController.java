package demo;

import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.brashmonkey.spriter.Spriter;
import com.brashmonkey.spriter.player.SpriterPlayer;
import com.brashmonkey.spriter.file.FileLoader;

public class SpriterPlayerController extends Table {
	public final Slider slider;
	public final SelectBox box;
	public final CheckBox drawBones, drawBBox, drawSprites;
	public final SpriterPlayerGroup playerGroup;
	private Skin skin;
	private Label playbackLabel;
	
	public SpriterPlayerController(Spriter spriter, int entityIndex, FileLoader<?> loader, Skin skin){
		super(skin);
		this.skin = skin;
		//this.debug();
		this.playerGroup = new SpriterPlayerGroup(spriter, entityIndex, loader);
		this.slider = new Slider(-400, 400, 1, false, skin);		
		this.slider.setValue(100);
		this.box = new SelectBox(this.animationNames(), skin);
		this.drawSprites = new CheckBox("draw sprites", skin);
		this.drawSprites.setChecked(true);
		this.drawBBox = new CheckBox("draw bounding boxes", skin);
		this.drawBones = new CheckBox("draw bones", skin);
		this.setupGUI();
	}
	
	protected void setupGUI(){
		this.add("Animation: ");
		this.add(box);
		this.row().padTop(10);
		this.add(drawSprites);
		this.add(drawBBox);
		this.add(drawBones);
		this.row();
		this.add(this.playerGroup);
		this.row();
		this.add("Playback speed: ");
		this.add(this.slider);
		this.playbackLabel = new Label(this.slider.getValue()+"", skin);
		this.add(playbackLabel).padLeft(10).width(100).center();
		this.playerGroup.setZIndex(0);
	}
	
	@Override
	public void act(float delta){
		super.act(delta);
		this.playbackLabel.setText((int)this.slider.getValue()+"%");
		this.playerGroup.player.setFrameSpeed((int)((1000f/60f)*(this.slider.getValue()/100)));
		this.setAnimation();
		this.setDrawing();
	}
	
	protected void setAnimation(){
		((SpriterPlayer) this.playerGroup.player).setAnimation(box.getSelection(), 1, 10);
	}
	
	protected void setDrawing(){
		this.playerGroup.drawBones = this.drawBones.isChecked();
		this.playerGroup.drawBoxes = this.drawBBox.isChecked();
		this.playerGroup.drawSprites = this.drawSprites.isChecked();
	}
	
	private String[] animationNames(){
		String[] anims = new String[((SpriterPlayer) this.playerGroup.player).getEntity().getAnimation().size()];
		for(int i = 0; i < anims.length; i++)
			anims[i] = ((SpriterPlayer) this.playerGroup.player).getEntity().getAnimation().get(i).getName();
		return anims;
	}

}
