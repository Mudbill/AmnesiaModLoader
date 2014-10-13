package mudbill.modloader;

import java.io.File;
import java.util.Locale;

public class CurrentOS {
	
	private static String steamExe;
	private static String gameExe;
	private static String saveDir;
	
	private static String system;
	
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
	
	public void setOSValues()
	{
		if(system == null) return;
		
		if(system == "Windows") {
			steamExe = "Steam.exe";
			gameExe = "Launcher.exe";
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
	
	public static String getSteamExe()
	{
		return steamExe;
	}
	
	public static String getGameExe()
	{
		return gameExe;
	}
	
	public static String getSaveDir()
	{
		return saveDir;
	}
}
