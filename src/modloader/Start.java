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
			Log.info("Starting Amnesia Modloader version " + MainFrame.getVersion());
			
			new CurrentOS().setOSValues();

			String prefPath = CurrentOS.getSaveDir() + File.separator + CurrentOS.getConfigName();
			String portPath = CurrentOS.getSaveDir() + File.separator + CurrentOS.getPortConfigName();
			File dir = new File(prefPath).getParentFile();
			dir.mkdirs();
			
			Properties p = ConfigManager.loadConfig(prefPath);
			Properties p2 = ConfigManager.loadConfig(portPath);

			if(p2 != null) {
				try {
					PORT = Integer.parseInt(p2.getProperty("Port"));
				} catch (Exception e) {
					Log.error(e);
				}
				Log.info("\tPort = " + PORT);
			} else {
				if(!new File(portPath).exists()) {
					p2 = new Properties();
					p2.setProperty("Port", "9999");
					ConfigManager.writeConfig(p2, portPath);
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
				checkConfigFolder(rootCfg);
				
				MainFrame frame = new MainFrame();
				MainFrame.setModDirectory(modDir);
				frame.open();
			} else {
				Log.info("Preferences not found, first startup?");

				MessageBox m = new MessageBox(new Shell(), SWT.SHEET | SWT.ICON_INFORMATION);
				m.setMessage("Welcome! Please specify some settings before continuing.\nThese can be changed later by selecting the \"Preferences\" button in the main window.");
				m.setText("First time setup");
				m.open();
				
				Preferences prefs = new Preferences(new Shell(), SWT.SHEET);
				prefs.open();
			}
		} catch (Exception e) {
			Log.error(e);
		} finally {			
			Log.printLog();				
		}
	}
	
	public static void checkConfigFolder(File rootCfg) {
		MessageBox m = new MessageBox(new Shell(), SWT.SHEET | SWT.ICON_INFORMATION | SWT.YES | SWT.NO);

		if (rootCfg.exists() && rootCfg != null) {
			Log.info("\tRoot config found: " + rootCfg.getPath());
			Properties rootConfig = ConfigManager.loadConfig(rootCfg.getPath());
			try {
				if(rootConfig.getProperty("CfgVersion").equals(MainFrame.getVersion())) {
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
			m.setMessage("Your specified game directory is lacking an important file for the Modloader to work. Shall I copy it there now? If not, the Modloader may not work properly.");
		}

		if(m.open() == SWT.YES) {
			try {
				Files.copy(Start.class.getResourceAsStream("/resources/modloader.cfg"), Paths.get(rootCfg.getPath()), StandardCopyOption.REPLACE_EXISTING);
				Files.copy(Start.class.getResourceAsStream("/resources/icon_default.png"), Paths.get(rootCfg.getParent() + File.separator + "default.png"), StandardCopyOption.REPLACE_EXISTING);
				Log.info("\tCopied files from jar to config folder.");						
			} catch (IOException e) {
				Log.error(e);
			}
		}
	}

	private static void checkIfRunning() {
		try {
			new ServerSocket(PORT, 0, InetAddress.getByAddress(new byte[] {127,0,0,1}));
		}
		catch (BindException e) {
			Log.warn("Instance already running! Shutting down.");
			MessageBox m = new MessageBox(new Shell(), SWT.SHEET | SWT.ICON_WARNING);
			m.setMessage("Another instance of the application is running. To avoid conflicts, only one can run at a time.\n\nIf you believe this is an error, change the port settings.\nCurrent port: " + PORT);
			m.setText("Duplicate Instance");
			m.open();
			System.exit(1);
		}
		catch (IOException e) {
			Log.error(e);
			System.exit(2);
		}
	}
}
