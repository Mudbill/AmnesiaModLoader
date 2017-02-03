package modloader;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;

public class Update {

	public Update() {}
	
	public static String onlineVersion = "";
		
	public static String getLatestVersion() {
		try {
			URL url = new URL(CurrentOS.getUpdateURL());
			Scanner s = new Scanner(url.openStream());
			String version = s.nextLine();
			Log.info("Getting latest version number: " + version);
			onlineVersion = version;
			s.close();
			return version;					
		} catch (Exception e) {
			Log.warn("Could not fetch current version. Offline?");
			return null;
		}
	}
	
	/**
	 * Reads the previous log file in the directory and returns the version. If not found, returns empty.
	 * @return version
	 */
	public static String readVersionFromLog() {
		try {
			File f = new File(CurrentOS.getSaveDir() + File.separator + CurrentOS.getLogName());
			if(!f.exists()) return "";
			Scanner s = new Scanner(f);
			String line = s.nextLine();
			String[] temp = line.split(" ");
			String version = temp[temp.length-1];
			s.close();
			return version;
		} catch (FileNotFoundException e) {
			Log.error("", e);
		}
		
		return "";
	}
	
	/**
	 * Tests the current version of the application against a potential new version to determine which is more up to date.
	 */
	public static boolean compareVersions(String newVersion) {
		String[] nv = newVersion.split("[.]");
		String[] current = Engine.APP_VERSION.split("[.]");
				
		for(int i = 0; i < nv.length; i++) {
			try {
				Log.info("new = " + nv[i] + " current = " + current[i]);
				if(Integer.parseInt(nv[i]) <= Integer.parseInt(current[i])) {
					Log.info("Already up to date.");
				} else {
					Log.info("New version available.");
					return true;
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				Log.warn("New versioning syntax does not match local versioning!");
			}
		}
		return false;
	}
	
	public static void promptUpdate() {
		Display display = Display.getDefault();
		display.asyncExec(new Runnable(){
			@Override
			public void run() {
				MessageBox m = new MessageBox(Engine.getShell(), SWT.ICON_INFORMATION | SWT.YES | SWT.NO);
				m.setText("Update available");
				m.setMessage("There's an update available for download. Would you like to visit the download page?\n\nCurrent version: " + Engine.APP_VERSION + "\nNew version: " + onlineVersion);
				int i = m.open();
				if(i == SWT.YES) {
					try {
						Log.info("Opening website: " + Engine.URL_FORUM);
						Common.openWebpage(new URL(Engine.URL_FORUM));
						Engine.getShell().close();
					} catch (MalformedURLException e) {
						Log.error("", e);
					}
				}
				return;
			}
		});
	}
}
