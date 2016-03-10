package modloader;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;

public class CurrentOS {
	
	private static String system, gameExe, launcherExe, saveDir, defDir, config, portConfig, log, execExt;
		
	/**
	 * Constructor that determines the system that this application is being ran from.
	 */
	public CurrentOS() 
	{
		if(system == null) {
			String os = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);
			if((os.indexOf("mac") >= 0) || (os.indexOf("darwin") >= 0)) {
				system = "MacOS";
			}
			else if(os.indexOf("win") >= 0) {
				system = "Windows";
			}
			else if(os.indexOf("nux") >= 0) {
				system = "Linux";
			}
		}
	}
	
	/**
	 * Sets OS specific values. If ran from Windows, it will name the start file Amnesia.exe.
	 */
	public void setOSValues()
	{
		if(system == null) return;
		
		if(system == "Windows") {
			execExt = ".exe";
			saveDir = System.getProperty("user.home") + File.separator + "AppData\\Roaming\\Amnesia ModLoader";
		}
//		else if(system == "MacOS") {
//			execExt = ".app";
//			saveDir = System.getProperty("user.home") + File.separator + "Library/Application Support/Frictional Games/Amnesia/ModLoader";
//		}
//		else if(system == "Linux") {
//			execExt = ".bin";
//			saveDir = System.getProperty("user.home") + File.separator + "Amnesia/ModLoader";
//		}

		gameExe = "Amnesia";
		launcherExe = "Launcher";
		defDir = System.getProperty("user.dir");
		config = "preferences.cfg";
		portConfig = "port.cfg";
		log = "log.log";
		Log.info("\tSystem = " + system + ", preference directory = " + saveDir);
	}
	
	public static String getGameExe()
	{
		return gameExe + execExt;
	}
	
	public static String getLauncherExe()
	{
		return launcherExe + execExt;
	}
	
	public static String getSaveDir()
	{
		return saveDir;
	}
	
	public static String getDefDir()
	{
		return defDir;
	}
	
	public static String getConfigName()
	{
		return config;
	}
	
	public static String getPortConfigName()
	{
		return portConfig;
	}
	
	public static String getLogName()
	{
		return log;
	}
	
	/**
	 * Global function for shutting down the application. Will check OS if Amnesia is running.
	 */
	public static boolean shutDown() {
		if(gameRunning()) {
			MessageBox m = new MessageBox(MainFrame.getShell(), SWT.SHEET | SWT.ICON_WARNING | SWT.YES | SWT.NO);
			m.setText("Shutdown?");
			m.setMessage("It looks like Amnesia might be running. If you shut down the Modloader now, it will not be able to install/uninstall any custom shaders that might be required by the mod. I recommend you shut down Amnesia first.\n\nDo you really want to exit now?");
			if(m.open() != SWT.YES) {
				return false;
			}
		}
		Log.info("Closing application.");
		return true;
	}
	
	/**
	 * Checks whether the game or launcher is currently running on the system.
	 * @return
	 */
	public static boolean gameRunning() {
		String line;
		String pidInfo = "";
		
		Log.info("Checking for processes: \"" + launcherExe + execExt + "\" \"" + gameExe + execExt + "\"");
		
		try {
			Process isRunning = Runtime.getRuntime().exec(System.getenv("windir") + "/system32/tasklist.exe");
			BufferedReader sysInput = new BufferedReader(new InputStreamReader(isRunning.getInputStream()));
			while ((line = sysInput.readLine()) != null) {
				pidInfo+=line;
			}
			sysInput.close();
			
			if(pidInfo.contains(launcherExe + execExt) || pidInfo.contains(gameExe + execExt)) {
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return false;
	}
	
	/**
	 * Sets the name of the executable. Uses defined custom name, or default if not found.
	 * @param name - relative path and name of executable
	 */
	public static void setCustomExecName(String name)
	{
		String s = name;
		
		try {
			if(s.isEmpty()) s = null;
		} catch (Exception e) { }
		
		if(s != null) {
			gameExe = s;
			launcherExe = s;				
		} else {
			gameExe = "Amnesia";
			launcherExe = "Launcher";
		}
		MainFrame.editLaunch(s);
	}
}
