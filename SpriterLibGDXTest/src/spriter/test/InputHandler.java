package spriter.test;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Input.Keys;

public class InputHandler extends InputAdapter{
	File directory = new File(System.class.getResource("/").getFile());
	
	public SpriterApplication app;
	
	@Override
	public boolean keyDown(int keycode){
		if(keycode == Keys.LEFT){
			try{
				app.player.setAnimatioIndex(app.player.getAnimationIndex()-1, 1, 10);
			} catch(RuntimeException e){
				System.out.println(e.getMessage());
			}
		}
		if(keycode == Keys.RIGHT){
			try{
				app.player.setAnimatioIndex(app.player.getAnimationIndex()+1, 1, 10);
			} catch(RuntimeException e){
				System.out.println(e.getMessage());
			}
		}
		

		if(keycode == Keys.UP){
			try{
				app.loadEntity(app.player.getEntity().getId()-1);
			} catch(RuntimeException e){
				System.out.println(e.getMessage());
			}
		}
		if(keycode == Keys.DOWN){
			try{
				app.loadEntity(app.player.getEntity().getId()+1);
			} catch(RuntimeException e){
				System.out.println(e.getMessage());
			}
		}
		
		if(Gdx.input.isKeyPressed(Keys.CONTROL_LEFT) || Gdx.input.isKeyPressed(Keys.CONTROL_RIGHT)){
			if(keycode == Keys.O){
				JFileChooser chooser = this.showOpenSaveDialog(0, "scml", "A Spriter compatible file (*.scml)", "Choose SCML file");
				File file = chooser.getSelectedFile();
		        if(file == null) return false;
		        this.directory = new File(file.getParent());
		        String filename = chooser.getSelectedFile().getAbsolutePath();
		        app.loadSCML(filename);
			}
		}
		return false;
	}
	
	private JFileChooser showOpenSaveDialog(int type, final String filetype, final String filedescription, String title){
        final JFileChooser chooser = new JFileChooser(title);
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        File dir = directory;
        chooser.setCurrentDirectory(dir); 
        chooser.setFileHidingEnabled(false);
        chooser.setFileFilter(null);
        chooser.setFileFilter(new FileFilter(){

			@Override
			public boolean accept(File file) {
				if(file.isDirectory()) return true;
				String name = file.getName();
				int dotIndex = name.lastIndexOf('.');
				if (dotIndex == -1) return false;
				return name.substring(dotIndex + 1).equalsIgnoreCase(filetype);
			}

			@Override
			public String getDescription() {
				return filedescription;
			}
        	
        });
        if(type == 0) chooser.showOpenDialog(null);
        else chooser.showSaveDialog(null);
        return chooser;
	}
}
