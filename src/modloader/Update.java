package modloader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import org.eclipse.swt.SWT;
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
		} catch (MalformedURLException e) {
			Log.error("", e);
			return null;
		} catch (IOException e) {
			Log.error("", e);
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
		String[] current = Engine.appVersion.split("[.]");
				
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
	
	public static int promptUpdate() {
		MessageBox m = new MessageBox(Engine.getShell(), SWT.ICON_INFORMATION | SWT.YES | SWT.NO);
		m.setText("Update available");
		m.setMessage("There's an update available for download. Would you like to visit the download page?\n\nCurrent version: " + Engine.appVersion + "\nNew version: " + onlineVersion);
		int i = m.open();
		if(i == SWT.YES) {
			try {
				Log.info("Opening website: " + Engine.urlForum);
				Common.openWebpage(new URL(Engine.urlForum));
				Engine.getShell().close();
			} catch (MalformedURLException e) {
				Log.error("", e);
			}
		}
		return i;
	}
}
