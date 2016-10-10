package modloader;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;

public class CurrentOS {
	
	private static String system, gameExe, launcherExe, saveDir, config, portConfig, log, execExt;
	private static String updateURL = "http://buttology.net/assets/other/modloader_version_win.txt";
	private static final String gameExeDef = "Amnesia", launcherExeDef = "Launcher";
		
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
			//updateURL = "http://notexistant.net/text.txt";
		}
		else if(system == "MacOS") {
			execExt = ".app";
			updateURL = updateURL + "modloader_version_mac.txt";
		}
//		else if(system == "Linux") {
//			execExt = ".bin";
//			saveDir = System.getProperty("user.dir");
//			updateURL = updateURL + "modloader_version_nux.txt";
//		}
		
		saveDir = System.getProperty("user.dir");
		gameExe = gameExeDef;
		launcherExe = launcherExeDef;
		config = "preferences.cfg";
		portConfig = "port.cfg";
		log = "log.log";
		Engine.prefPath = saveDir + File.separator + config;
		Engine.portPath = saveDir + File.separator + portConfig;
		Log.info("\tSystem = " + system + ", preference directory = " + saveDir);
	}
	
	public static String getUpdateURL() {
		return updateURL;
	}
	
	public static String getSystem()
	{
		return system;
	}
	
	public static String getGameExe()
	{
		return gameExe + execExt;
	}
	
	public static String getDefGameExe()
	{
		return gameExeDef + execExt;
	}
	
	public static String getLauncherExe()
	{
		return launcherExe + execExt;
	}
	
	public static String getSaveDir()
	{
		return saveDir;
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
	 * Checks whether the game or launcher is currently running on the system.
	 * @return
	 */
	public static boolean gameRunning() {
		
		if(system != "Windows") {
			Log.info("Not running on Windows, skipping process check.");
			return false;
		}
		
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
			gameExe = gameExeDef;
			launcherExe = launcherExeDef;
		}
		if(CurrentOS.getSystem() == "Windows") Engine.editLaunch(s, MainFrameWin32.buttonLaunch, MainFrameWin32.buttonLaunch2, MainFrameWin32.menuItemLauncher);
	}
}
