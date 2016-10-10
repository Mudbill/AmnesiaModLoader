package modloader;

import java.io.File;
import java.io.IOException;
import java.net.BindException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

public class Start {
	
	private static int PORT = 9999;

	public static void main(String[] args) {
		try {
			Log.info("Starting Amnesia Modloader version " + Engine.getVersion());
			
			new CurrentOS().setOSValues();
						
			String oldLogVer = Update.readVersionFromLog();
			if(!oldLogVer.equals(Engine.getVersion()) && !oldLogVer.equals("")) {
				Log.info("Previous log version does not match. First boot after update? Log version: " + oldLogVer);
				ChangelogDialog ud = new ChangelogDialog(new Shell(), SWT.DIALOG_TRIM);
				ud.open();
			}					

			File f = new File(Engine.prefPath).getParentFile();
			if(!f.exists()) f.mkdirs();
			
			Properties p = ConfigManager.loadConfig(Engine.prefPath);
			Properties p2 = ConfigManager.loadConfig(Engine.portPath);

			if(p2 != null) {
				try {
					PORT = Integer.parseInt(p2.getProperty("Port"));
				} catch (Exception e) {
					Log.error("", e);
				}
				Log.info("\tPort = " + PORT);
			} else {
				if(!new File(Engine.portPath).exists()) {
					p2 = new Properties();
					p2.setProperty("Port", "9999");
					ConfigManager.writeConfig(p2, Engine.portPath);
					Log.info("Writing default port settings.");
				}
			}
			
			checkIfRunning();
			
			if(p != null) {		
				String modDir;
				
				if(Boolean.parseBoolean(p.getProperty("UseSameDir")) == true) {
					modDir = p.getProperty("GameDir");				
					Log.info("\tUsing same directory for game and mods.");
					Log.info("\tGameDir = " + modDir);
				}
				else {
					modDir = p.getProperty("ModDir");
					Log.info("\tUsing separate directories for game and mods.");
					Log.info("\tGameDir = " + p.getProperty("GameDir"));
					Log.info("\tModDir = " + modDir);
				}
				
				File rootCfg = new File(p.getProperty("GameDir") + File.separator + "config" + File.separator + "modloader.cfg");
				if(!p.getProperty("GameDir").isEmpty()) checkConfigFolder(rootCfg, Boolean.parseBoolean(p.getProperty("WarnConfig")));
				
				Engine.setModDirectory(modDir);

				if(Boolean.parseBoolean(p.getProperty("CheckForUpdates"))) {
					Thread t = new Thread(new Runnable() {
						@Override
						public void run() {
							String s = Update.getLatestVersion();
							if(s != null) if(Update.compareVersions(s)) {
								Update.promptUpdate();
							}
						}
					});
					t.start();
				}
				
				if(CurrentOS.getSystem() == "MacOS") {
					MainFrameOSX frame = new MainFrameOSX();
					frame.open();
				} else if(CurrentOS.getSystem() == "Windows") {
					MainFrameWin32 frame = new MainFrameWin32();
					frame.open();
				}
				
			} else {
				Log.info("Preferences not found, first startup?");

				MessageBox m = new MessageBox(new Shell(), SWT.ICON_INFORMATION);
				m.setMessage("Welcome! Please specify some settings before continuing.\nThese can be changed later by selecting the \"Options\" button in the main window.");
				m.setText("First time setup");
				m.open();
				
				if(CurrentOS.getSystem() == "MacOS") {
					PreferencesOSX prefs = new PreferencesOSX(new Shell(), SWT.DIALOG_TRIM);
					prefs.open();
				} else if(CurrentOS.getSystem() == "Windows") {
					PreferencesWin32 prefs = new PreferencesWin32(new Shell(), SWT.DIALOG_TRIM);
					prefs.open();
				}

			}
		} catch (Exception e) {
			Log.error("", e);
		} finally {			
			Log.printLog();				
		}
	}
	
	public static void checkConfigFolder(File rootCfg, boolean warn) {
		MessageBox m = new MessageBox(new Shell(), SWT.ICON_INFORMATION | SWT.YES | SWT.NO);

		if (rootCfg.exists() && rootCfg != null) {
			Log.info("\tRoot config found: " + rootCfg.getPath());
			Properties rootConfig = ConfigManager.loadConfig(rootCfg.getPath());
			try {
				if(rootConfig.getProperty("CfgVersion").equals(Engine.getVersion())) {
					Log.info("\tRoot config version is up to date. Version: " + rootConfig.getProperty("CfgVersion"));
					return;
				} else {
					Log.warn("\tRoot config version is out of date.");
				}
			} catch (Exception e) {
				Log.warn("\tCould not load CfgVersion from config. Might be too old.");
			}
			m.setText("Config out of date");
			m.setMessage("Your specified game directory contains an important file whose version doesn't match the Modloader. Shall I update it? If not, the Modloader may not work properly.");
		} else {
			Log.warn("\tRoot config file not found.");
			m.setText("Config not found");
			m.setMessage("Your specified game directory is lacking an important file that the Modloader uses. Shall I copy it there now? If not, the Modloader may not work optimally.");
		}

		
		if(warn) if(rootCfg != null && m.open() != SWT.YES) return;
		
		try {
			Files.copy(Start.class.getResourceAsStream("/resources/modloader.cfg"), Paths.get(rootCfg.getPath()), StandardCopyOption.REPLACE_EXISTING);
			Files.copy(Start.class.getResourceAsStream("/resources/icon_default.png"), Paths.get(rootCfg.getParent() + File.separator + "default.png"), StandardCopyOption.REPLACE_EXISTING);
			Log.info("\tCopied files from jar to config folder.");
		} catch (IOException e) {
			Log.error("", e);
		}
	}

	private static void checkIfRunning() {
		try {
			new ServerSocket(PORT, 0, InetAddress.getByAddress(new byte[] {127,0,0,1}));
		}
		catch (BindException e) {
			Log.warn("Instance already running! Shutting down.");
			MessageBox m = new MessageBox(new Shell(), SWT.POP_UP | SWT.ICON_WARNING);
			m.setMessage("Another instance of the application is running. To avoid conflicts, only one can run at a time.\n\nIf you believe this is an error, change the port settings.\nCurrent port: " + PORT);
			m.setText("Duplicate Instance");
			m.open();
			System.exit(1);
		}
		catch (IOException e) {
			Log.error("", e);
			System.exit(2);
		}
	}
}
