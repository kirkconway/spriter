package demo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.brashmonkey.spriter.ik.SpriterCCDResolver;
import com.brashmonkey.spriter.ik.SpriterIKResolver;
import com.brashmonkey.spriter.objects.SpriterBone;
import com.brashmonkey.spriter.objects.SpriterIKObject;
import com.brashmonkey.spriter.player.SpriterPlayerInterpolator;

public class PlayerInterpolationControl extends Table{
	public final SelectBox bones;
	public final Slider weightSlider, chainLength;
	public final CheckBox drawBones, drawBBox, drawSprites, ikBox;
	public final TextButton flipX, flipY;
	public final SpriterPlayerGroup playerGroup;
	private Skin skin;
	private Label weightLabel, chainLengthLabel;
	private SpriterIKObject obj;
	private int lastLength;
	SpriterPlayerInterpolator player;
	SpriterIKResolver resolver;
	private String lastBone = "";

	public PlayerInterpolationControl(final SpriterPlayerInterpolator player, Skin skin) {
		super(skin);
		this.player = player;
		this.ikBox = new CheckBox("activate inverse kinematics for bone", skin);
		this.obj = new SpriterIKObject(2,1);
		this.resolver = new SpriterCCDResolver();
		this.ikBox.addCaptureListener(new ClickListener(){
			public void clicked (InputEvent event, float x, float y) {
				SpriterBone bone = player.getBoneByName(bones.getSelection());
				chainLength.setRange(0, getMaxChainLength(bone));
				obj.chainLength = (int) chainLength.getValue();
				resolver.mapIKObject(obj, bone);
				resolver.deactivateEffectors(player, true);
			}
		});
		this.skin = skin;
		//this.debug();
		
		this.chainLength = new Slider(0,3,1, false,skin);
		this.chainLength.setValue(2);
		this.bones = new SelectBox(boneNames(), skin);
		this.bones.setSelection("rightHand");
		this.obj.chainLength = 2;
		this.flipX = new TextButton("Flip horizontal", skin);
		this.flipX.addCaptureListener(new ClickListener(){
			public void clicked (InputEvent event, float x, float y) {
				if(!flipX.isDisabled()) player.flipX();
			}
		});
		this.flipY = new TextButton("Flip vertical", skin);
		this.flipY.addCaptureListener(new ClickListener(){
			public void clicked (InputEvent event, float x, float y) {
				if(!flipY.isDisabled()) player.flipY();
			}
		});
		this.playerGroup = new SpriterPlayerGroup(player);
		this.weightSlider = new Slider(0, 100, 1, false, skin);		
		this.weightSlider.setValue(player.getWeight()*100f);
		this.drawSprites = new CheckBox("draw sprites", skin);
		this.drawSprites.setChecked(true);
		this.drawBBox = new CheckBox("draw bounding boxes", skin);
		this.drawBones = new CheckBox("draw bones", skin);
		this.setupGUI();
	}

	protected void setupGUI(){
		this.add(this.ikBox).colspan(2);
		this.add(bones);
		this.row().padTop(10);
		this.add("Chain length: ");
		this.add(this.chainLength);
		this.chainLengthLabel = new Label(""+(int)this.chainLength.getValue(),skin);
		this.add(chainLengthLabel);
		this.row();
		this.add(drawSprites);
		this.add(drawBBox);
		this.add(drawBones);
		this.row();
		this.add(this.playerGroup);
		this.row();
		this.add("Interpolation weight: ");
		this.add(this.weightSlider);
		this.weightLabel = new Label(this.weightSlider.getValue()+"", skin);
		this.add(weightLabel).padLeft(10).width(100).center();
		this.row();
		this.add(this.flipX);
		this.add(this.flipY).padLeft(10);
		this.playerGroup.setZIndex(0);
	}
	
	@Override
	public void act(float delta){
		if(obj.chainLength != this.lastLength){
			this.lastLength = obj.chainLength;
			this.resolver.activateAll(player);
			this.resolver.deactivateEffectors(player, true);
		}
		if(this.ikBox.isChecked()){
			this.obj.chainLength = (int) this.chainLength.getValue();
			this.flipX.setDisabled(true);
			this.flipY.setDisabled(true);
			if(!this.lastBone.equals(this.bones.getSelection())){
				this.resolver.activateAll(player);
				super.act(delta);
				this.lastBone = this.bones.getSelection();
				SpriterBone bone = player.getBoneByName(this.bones.getSelection());
				obj.chainLength = this.getMaxChainLength(bone);
				this.chainLength.setRange(0, obj.chainLength);
				this.resolver.mapIKObject(obj, bone);
				this.resolver.deactivateEffectors(player, true);
			}
			this.obj.setX(Gdx.input.getX());
			this.obj.setY(Gdx.graphics.getHeight()-Gdx.input.getY());
			this.resolver.resolve(player);
		} else{
			this.flipX.setDisabled(false);
			this.flipY.setDisabled(false);
			this.resolver.activateAll(player);
			super.act(delta);
		}
		super.act(delta);
		this.chainLengthLabel.setText(""+(int)this.chainLength.getValue());
		this.weightLabel.setText((int)this.weightSlider.getValue()+"%");
		this.player.setWeight(this.weightSlider.getValue()/100);
		this.setBooleans();
	}
	
	protected void setBooleans(){
		this.playerGroup.drawBones = this.drawBones.isChecked();
		this.playerGroup.drawBoxes = this.drawBBox.isChecked();
		this.playerGroup.drawSprites = this.drawSprites.isChecked();
	}
	
	private String[] boneNames(){
		String[] bones = new String[this.player.getRuntimeBones().length];
		for(int i = 0; i < bones.length; i++)
			bones[i] = this.player.getRuntimeBones()[i].getName();
		return bones;
	}
	
	public int getMaxChainLength(SpriterBone bone){
		int length = 0;
		SpriterBone parent = null;
		if(bone.hasParent()) parent = this.player.getRuntimeBones()[bone.getParentId()];
		while(parent != null){
			length++;
			if(parent.hasParent()) parent = this.player.getRuntimeBones()[parent.getParentId()];
			else parent = null;
		}
		return length;
	}

}
