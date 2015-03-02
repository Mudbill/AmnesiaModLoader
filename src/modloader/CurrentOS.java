package modloader;

import java.io.File;
import java.util.Locale;

public class CurrentOS {
	
	private static String gameExe;
	private static String launcherExe;
	private static String saveDir;
	private static String defDir;
	private static String config;
	private static String log;
	
	private static String system;
	
	/**
	 * Constructor that determines the system that this application is being ran from.
	 */
	public CurrentOS() 
	{
		if(system == null) {
			String os = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);
			if((os.indexOf("mac") >= 0) || (os.indexOf("darwin") >= 0)) {
				system = "MacOS";
				Log.info("System = Mac OS X.");
			}
			else if(os.indexOf("win") >= 0) {
				system = "Windows";
				Log.info("System = Windows.");
			}
			else if(os.indexOf("nux") >= 0) {
				system = "Linux";
				Log.info("System = Linux.");
			}
		}
	}
	
	/**
	 * Sets OS specific values. If ran from Windows, it will name the start file Amnesia.exe. If ran from OS X, it will name it Amnesia.app instead etc.
	 */
	public void setOSValues()
	{
		if(system == null) return;
		
		if(system == "Windows") {
			gameExe = "Amnesia.exe";
			launcherExe = "Launcher.exe";
			saveDir = System.getProperty("user.home") + File.separator + "My Documents\\Amnesia\\ModLoader";
			defDir = System.getProperty("user.dir");
			Log.info("Preference directory = " + saveDir);
		}
		else if(system == "MacOS") {
			gameExe = "Amnesia.app";
			launcherExe = "Launcher.app";
			saveDir = System.getProperty("user.home") + File.separator + "Amnesia/ModLoader";
			defDir = "~/Library";
			Log.info("gameExe = " + gameExe + ", launcherExe = " + launcherExe + ", saveDir = " + saveDir);
		}
		else if(system == "Linux") {
			gameExe = "Amnesia.what?";
			launcherExe = "Launcher.what?";
			saveDir = System.getProperty("user.home") + File.separator + "Amnesia/ModLoader";
			defDir = "/home";
			Log.info("gameExe = " + gameExe + ", launcherExe = " + launcherExe + ", saveDir = " + saveDir);
		}
		config = "modloader_settings.cfg";
		log = "modloader_log.log";
	}
	
	/**
	 * Gets the OS name of the game executable.
	 * @return gameExe
	 */
	public static String getGameExe()
	{
		return gameExe;
	}
	
	/**
	 * Gets the OS name of the launcher executable.
	 * @return gameExe
	 */
	public static String getLauncherExe()
	{
		return launcherExe;
	}
	
	/**
	 * Gets the OS path for the preferences' save directory.
	 * @return saveDir
	 */
	public static String getSaveDir()
	{
		return saveDir;
	}
	
	/**
	 * Gets the OS path for the default directory.
	 * @return saveDir
	 */
	public static String getDefDir()
	{
		return defDir;
	}
	
	/**
	 * Gets the name of the configuration file.
	 * @return saveDir
	 */
	public static String getConfigName()
	{
		return config;
	}
	
	/**
	 * Gets the name of the configuration file.
	 * @return saveDir
	 */
	public static String getLogName()
	{
		return log;
	}
}
