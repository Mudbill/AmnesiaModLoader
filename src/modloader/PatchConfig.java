package modloader;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

public class PatchConfig {

	public PatchConfig() {}
	
	private static final String MAIN = "\tDefaultMainSettingsSDL2 = \"config/default_main_settings_sdl2.cfg\"";
	private static final String KEYS = "\tDefaultUserKeysSDL2	= \"config/default_user_keys_sdl2.cfg\"\n";
	
	public static boolean abort = false;
	
	private String path;
	private static boolean warnPatch = true;
	
	public static void setWarnPatch(boolean var) {
		warnPatch = var;
	}
	
	public static boolean getWarnPatch() {
		return warnPatch;
	}
	
	public void checkConfig(String configPath) {
		try {
			abort = false;
			this.path = configPath;
			
			String main, keys;
			
			Properties p = ConfigManager.loadConfig(configPath);
			main = p.getProperty("DefaultMainSettingsSDL2");
			keys = p.getProperty("DefaultUserKeysSDL2");

			if(main == null) Log.warn("DefaultMainSettingsSDL2 not found.");
			if(keys == null) Log.warn("DefaultUserKeysSDL2 not found.");
			if(main != null && keys != null) return;
			
			if(!getWarnPatch()) {
				if(patchConfig()) Log.info("Patched successfully.");
				else Log.warn("Failed patching config.");
				return;
			}
			
			MessageBox m = new MessageBox(new Shell(), SWT.SHEET | SWT.YES | SWT.NO | SWT.CANCEL | SWT.ICON_QUESTION);
			if(CurrentOS.getSystem() == "MacOS") m = new MessageBox(MainFrameOSX.getShell(), SWT.SHEET | SWT.YES | SWT.NO | SWT.CANCEL | SWT.ICON_QUESTION);
			else if(CurrentOS.getSystem() == "Windows") m = new MessageBox(MainFrameWin32.getShell(), SWT.SHEET | SWT.YES | SWT.NO | SWT.CANCEL | SWT.ICON_QUESTION);
			m.setMessage("Due to a bug in Amnesia version 1.3.1, this mod might not start unless its config is updated to support 1.3.1. I can automatically do this for you now.\n\nYES - Will patch the file in the mod and launch it.\nNO - Will skip patching and launch it regardless (it might not start).");
			m.setText("Patch mod");
			int res = m.open();
			
			if(res == SWT.CANCEL) abort = true;
			
			if(res == SWT.NO) return;
			
			if(res == SWT.YES) {
				if(patchConfig()) Log.info("Patched successfully.");
				else Log.warn("Failed patching config.");
			}
		} catch (Exception e) {
			Log.error("", e);
		}				
	}
	
	private boolean patchConfig() throws IOException {
		Log.info("Patching config...>" + path);
		
		Scanner file = new Scanner(new File(path));
		List<String> list = new ArrayList<String>();
		String entry = "";
		
		while(file.hasNextLine()) {
			entry = file.nextLine();
			list.add(entry);
			
			if(list.size() == 1) {
				list.add(MAIN);
				list.add(KEYS);
			}
		}
		
		FileWriter writer = new FileWriter(path); 
		for(String str: list) {
			writer.write(str);
			writer.write("\n");
		}

		file.close();
		writer.close();
		list.clear();
	    
		return true;
	}
	
}
