package modloader;

import java.io.File;
import java.io.IOException;
import java.net.BindException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.Properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

public class Start {
	
	private static int PORT = 9999;

	public static void main(String[] args) {
		try {
			Log.info("Starting Amnesia Modloader version " + MainFrame.getVersion());
			
			CurrentOS currentOS = new CurrentOS();
			currentOS.setOSValues();

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
		} finally {			
			Log.printLog();				
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
