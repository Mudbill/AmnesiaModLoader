package modloader;

import java.io.File;
import java.util.Properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

public class Start {
	
	public static void main(String[] args) {
		try {
			Log.info("Starting Amnesia Modloader version " + MainFrame.getVersion());
			
			CurrentOS currentOS = new CurrentOS();
			currentOS.setOSValues();

			String prefPath = CurrentOS.getSaveDir() + File.separator + CurrentOS.getConfigName();
			File dir = new File(prefPath).getParentFile();
			dir.mkdirs();
			
			Properties p = ConfigManager.loadConfig(prefPath);

			if(p != null) {		
				
				String modDir;
				
				if(Boolean.parseBoolean(p.getProperty("UseSameDir")) == true) {
					modDir = p.getProperty("GameDir");				
					Log.info("\tUsing same directory for game and mods.");
					Log.info("GameDir = " + modDir);
				}
				else {
					modDir = p.getProperty("ModDir");
					Log.info("\tUsing separate directories for game and mods.");
					Log.info("\tGameDir = " + p.getProperty("GameDir"));
					Log.info("\tModDir = " + modDir);
				}

				MainFrame frame = new MainFrame();
				MainFrame.setModDirectory(modDir);
				frame.open();
			} else {
				Log.info("Preferences not found, first startup?");

				MessageBox m = new MessageBox(new Shell(), SWT.SHEET | SWT.ICON_INFORMATION);
				m.setMessage("Please specify some settings before continuing.\nThese can be changed later by selecting the \"Preferences\" button.");
				m.setText("First time setup");
				m.open();

				Preferences prefs = new Preferences(new Shell(), SWT.SHEET);
				prefs.open();
			}
		} catch (Exception e) {
			Log.error(e);
			e.printStackTrace();
		} finally {			
			Log.printLog();				
		}
	}
}
