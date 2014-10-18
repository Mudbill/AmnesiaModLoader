package mudbill.modloader;

import java.io.File;
import java.util.Locale;

public class CurrentOS {
	
	private static String steamExe;
	private static String gameExe;
	private static String saveDir;
	
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
				System.out.println("System = Mac OS X.");
			}
			else if(os.indexOf("win") >= 0) {
				system = "Windows";
				System.out.println("System = Windows.");
			}
			else if(os.indexOf("nux") >= 0) {
				system = "Linux";
				System.out.println("System = Linux.");
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
			steamExe = "Steam.exe";
			gameExe = "Amnesia.exe";
			saveDir = System.getProperty("user.home") + File.separator + "Documents\\Amnesia\\ModLoader";
			System.out.println("steamExe = " + steamExe + ", gameExe = " + gameExe + ", saveDir = " + saveDir);
		}
		else if(system == "MacOS") {
			steamExe = "Steam.app";
			gameExe = "Amnesia.app";
			saveDir = System.getProperty("user.home") + File.separator + "Amnesia/ModLoader";
			System.out.println("steamExe = " + steamExe + ", gameExe = " + gameExe + ", saveDir = " + saveDir);
		}
		else if(system == "Linux") {
			steamExe = "Steam.exe";
			gameExe = "Launcher.exe";
			saveDir = System.getProperty("user.home") + File.separator + "Amnesia/ModLoader";
			System.out.println("steamExe = " + steamExe + ", gameExe = " + gameExe + ", saveDir = " + saveDir);
		}
	}
	
	/**
	 * Gets the OS name of the Steam executable.
	 * @return steamExe
	 */
	public static String getSteamExe()
	{
		return steamExe;
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
	 * Gets the OS path for the preferences' save directory.
	 * @return saveDir
	 */
	public static String getSaveDir()
	{
		return saveDir;
	}
}
