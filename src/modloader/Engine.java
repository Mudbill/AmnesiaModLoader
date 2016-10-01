package modloader;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

public class Engine {
	
	public static final String appName = "Amnesia Modloader";
	public static final String appVersion = "1.6.3";
	public static final String cfgName = "main_init.cfg";
	public static final String urlWeb = "http://www.buttology.net/downloads/amnesia-modloader";
	public static final String urlForum = "https://www.frictionalgames.com/forum/thread-25806.html";
	public static final String urlYoutube = "https://www.youtube.com/MrMudbill";
	public static final String urlTwitter = "https://www.twitter.com/Mudbill";	

	public static String prefPath;// = CurrentOS.getSaveDir() + File.separator + CurrentOS.getConfigName();
	public static String portPath;// = CurrentOS.getSaveDir() + File.separator + CurrentOS.getPortConfigName();

	public static String modDirectory = "", gameDirectory = "";

	public static boolean warnExec = true, warnShader = true, warnSteam = true;

	private static List<String> shadersList = new ArrayList<String>();
	private static ModList modList = new ModList();
	public static boolean useSameDir = true;
	public static boolean refreshBoot = false;
	public static boolean startGame = false;
	public static boolean useCache = false;
	public static boolean abortRefresh = false;
	public static boolean useSteam = false;
	
	public static void openFrame() {
		if(CurrentOS.getSystem() == "Windows") {
			new MainFrameWin32().open();
		}
		if(CurrentOS.getSystem() == "MacOS") {
			new MainFrameOSX().open();
		}
	}
	
	public static boolean getFrameInit() {
		boolean b = false;
		if(CurrentOS.getSystem() == "Windows") {
			if(MainFrameWin32.getShell() != null) b = true;
		}
		if(CurrentOS.getSystem() == "MacOS") {
			if(MainFrameOSX.getShell() != null) b = true;
		}
		return b;
	}
	
	public static int getIconSize() {
		int i = 48;
		
		try {
			Properties p = ConfigManager.loadConfig(prefPath);

			int i2 = Integer.parseInt(p.getProperty("IconSize"));

			switch(i2) {
			case 0: {
				i = 64; break;
			}
			case 1: {
				i = 48; break;
			}
			case 2: {
				i = 32; break;
			}
			case 3: {
				i = 16; break;
			}
			default: {
				i = 48; break;
			}
			}
		} catch (Exception e) {
			new Error();
			Log.error("Could not get and scale image.", e);
		}
		return i;
	}
	
	public static Shell getShell() {
		Shell s = null;
		if(CurrentOS.getSystem() == "Windows") s = MainFrameWin32.getShell();
		if(CurrentOS.getSystem() == "MacOS") s = MainFrameOSX.getShell();
		if(s == null) s = new Shell();
		return s;
	}

	public static String getVersion()
	{
		return Engine.appVersion;
	}
	
	public static String getModDirectory()
	{
		if(useSameDir) return Engine.gameDirectory;
		else return Engine.modDirectory;
	}
	
	public static String getGameDirectory()
	{
		return Engine.gameDirectory;
	}

	public static void setWarnExec(boolean var) {
		warnExec = var;
	}
	
	public static boolean getWarnExec() {
		return warnExec;
	}
	
	public static void setWarnShader(boolean var) {
		warnShader = var;
	}
	
	public static boolean getWarnShader() {
		return warnShader;
	}
	
	public static void setWarnSteam(boolean var) {
		warnSteam = var;
	}
	
	public static boolean getWarnSteam() {
		return warnSteam;
	}
	
	public static void setModDirectory(String dir)
	{
		modDirectory = dir;
	}
	
	public static void setGameDirectory(String dir)
	{
		gameDirectory = dir;
	}
	
	public List<String> getModShaders(String fileName, File directory)
	{
		return shadersList;
	}
	
	public static void setUseSteam(boolean var, Table tableMods, Menu menuList, Menu menuList2) {
		if(var && getFrameInit()) tableMods.setMenu(menuList2);
		else if(!var && getFrameInit()) tableMods.setMenu(menuList);
		useSteam = var;
	}

	/**
	 * Searches specified directories and sub-directories for a file named "main_init.cfg"
	 */
	public static void checkMods(Display display, ProgressBar progressBar)
	{
		try {
			new FindFile(display, progressBar, new File(modDirectory), cfgName, MainFrameWin32.labelPath).start();

			if(display.isDisposed()) return;
			if(modDirectory != null) {
				progressBar.setVisible(true);
				Log.info("Mods found: " + ModList.getModsFound());
			}
		} catch (Exception e) {
			Log.error("Failed checking for mods.", e);			
		}
	}
	
	/**
	 * Displays the selected mod's information on the right panel.
	 */
	public static void displayModInfo(Shell shell, Label labelTitle, Label labelAuthor, Label labelVer, Label labelShaderVal, Text textDesc)
	{
		if(ModList.modsFoundTotal > 0) {
			//Default entries.
			String title = "Untitled";
			String author = "By Anonymous";
			String desc = "No description.";
			String compatMin = "Undefined";
			String shaders = "Undefined";
			String exec = null;
			
			try {
				if(modDirectory != null || modDirectory != "") {
					int index = Refresh.getListIndex();
					Log.info("Selected \"" + modList.getModTitle(index) + "\" at index " + Refresh.getListIndex());

					title = modList.getModTitle(index);
					author = "By " + modList.getModAuthor(index);
					desc = modList.getModDesc(index);

					compatMin = modList.getModMinCompatibility(index);
					if(compatMin == "" || compatMin == null) compatMin = "Any";
					
					shaders = modList.getModHasCustomShaders(index);
					
					exec = modList.getModExecName(index);
					CurrentOS.setCustomExecName(exec);
				}
			} catch (Exception e) {
				Log.error("Failed displaying mod info.", e);
			}
			labelTitle.setText(title);
			labelAuthor.setText(author);
			textDesc.setText(desc);
			labelVer.setText(compatMin);
			labelShaderVal.setText(shaders);
		} else {
			MessageBox m = new MessageBox(shell, SWT.SHEET | SWT.ICON_WARNING);
			m.setMessage("No mods were found in the specified directory. Double check the preferences.");
			m.setText("No mods found");
			m.open();
		}
	}
	
	/**
	 * Edits the launch button if necessary, by replacing it with another simple one.
	 * @param var
	 */
	public static void editLaunch(String var, Button button1, Button button2, MenuItem menuItemLauncher)
	{
		if(var != null) {
			button1.setVisible(false);
			button2.setVisible(true);
			menuItemLauncher.setEnabled(false);
		} else {
			button1.setVisible(true);
			button2.setVisible(false);
			menuItemLauncher.setEnabled(true);
		}
	}
	
	public static void openDir(String dir)
	{
		try {
			if(!dir.equals("")) {
				Log.info("Opening folder at: " + dir);
				Desktop.getDesktop().open(new File(dir));						
			} else {
				Log.warn("Dir is empty.");
				new Error("Directory is empty.");						
			}
		} catch (IllegalArgumentException e) {
			Log.error("Could not open folder destination.", e);
		} catch (IOException e) {
			Log.error("", e);
		}
	}
	
	public static void setup(Table tableMods, Menu menuList2)
	{
		if(useCache) {
			new ModCache().loadCache();
		}
		
		if(refreshBoot) {
			abortRefresh = false;
			new Refresh().refreshList();
		}
		
		if(useSteam) {
			Log.info("Using steam");
			tableMods.setMenu(menuList2);
		}
		if(PreferencesWin32.firstTime) {
			new Refresh().refreshList();
		}
	}
	
	/**
	 * Function used to launch the selected mod. 
	 * @param exec - The executable to run (Launcher or Amnesia or custom)
	 */
	public static void launchMod(Shell shell, String exec) {
		try {
			String filePath = modList.getLaunchIndex(Refresh.getListIndex());
			Log.info("Launching mod: " + filePath);
			boolean ignore = false;
			
			Properties p = ConfigManager.loadConfig(CurrentOS.getSaveDir() + File.separator + CurrentOS.getConfigName());

			//Shaders
			try {
				if(Boolean.parseBoolean(p.getProperty("ApplyShaders"))) {
					
					if(modList.getModIgnoreShaders(Refresh.getListIndex()).equals("true")) {
						Log.info("IgnoreShaders detected. Probably main game.");
						ignore = true;
					}
					
					if(!ignore) {
						Shaders.updateInfo();
						if(Shaders.checkShaders()) {
							if(Shaders.backupShaders()) {
								Log.info("Shaders were backed up.");;
							} else {
								Log.info("Shaders were not backed up.");
							}
							
							Shaders.installShaders();
						}						
					}
				}
			} catch (Exception e1) {
				Log.error("", e1);
			}
			
			//Runs the game depending on OS
			if(Shaders.cancelOperation < 0) {
				Shaders.cancelOperation = 1;
				return;
			}
			
			try {
				if(Boolean.parseBoolean(p.getProperty("Minimize"))) {
					shell.setMinimized(true);
				}
				
				new PatchConfig().checkConfig(filePath);
				if(PatchConfig.abort) return;
				if(!CurrentOS.getGameExe().equals(CurrentOS.getDefGameExe())) Engine.useSteam = false;
				
				if(Engine.useSteam) {
					if(Boolean.parseBoolean(p.getProperty("WarnSteam"))) {
						MessageBox m = new MessageBox(MainFrameWin32.getShell(), SWT.SHEET | SWT.OK | SWT.ICON_WARNING);
						m.setMessage("Launching this using Steam will require you to accept a prompt given by the Steam client. I will send a command to Steam, so please open Steam and click \"OK\" on the dialog that shows up.\n\nYou can disable this warning in the advanced options.");
						m.setText("Steam");
						m.open();
					}
					ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "start steam://rungameid/57300//" + filePath.replace("/", "%2F").replace(" ", "%20"));
				    builder.redirectErrorStream(true);
				    builder.start();
					Log.info("Running command: start steam://rungameid/57300//" + filePath.replace("/", "%2F").replace(" ", "%20"));
				} else {
					Runtime runTime = Runtime.getRuntime();
					String[] nullString = null;
					File workingDir = new File(gameDirectory);
					runTime.exec(gameDirectory + File.separator + exec + " " + filePath, nullString, workingDir);					
					Log.info("Running command: " + gameDirectory + File.separator + exec + " " + filePath);
				}
				
				if(!CurrentOS.getGameExe().equals(CurrentOS.getDefGameExe())) Engine.useSteam = true;
			} catch (IOException e) {
				Log.error("Could not find Amnesia executable in directory: " + gameDirectory, e);
				MessageBox m = new MessageBox(shell, SWT.SHEET | SWT.OK | SWT.ICON_ERROR);
				m.setMessage(exec + " was not found in the specified directory:\n\n" + gameDirectory + "\n\nDouble check the preferences to make sure you input the correct destination.");
				m.setText("Could not start");
				m.open();
			}
			
		} catch (ArrayIndexOutOfBoundsException e) {
			Log.warn("No selected index!");
			MessageBox m = new MessageBox(shell, SWT.SHEET | SWT.OK | SWT.ICON_ERROR);
			m.setMessage("No mod selected to launch.");
			m.setText("Could not start");
			m.open();
		} catch (NullPointerException e) {
			Log.error("", e);
		}
	}
	
	/**
	 * Global function for shutting down the application. Will check OS if Amnesia is running.
	 */
	public static boolean shutDown() {
//		if(CurrentOS.gameRunning()) {
//			MessageBox m = new MessageBox(null, SWT.SHEET | SWT.ICON_WARNING | SWT.YES | SWT.NO);
//			if(CurrentOS.getSystem() == "MacOS") m = new MessageBox(MainFrameOSX.getShell(), SWT.SHEET | SWT.ICON_WARNING | SWT.YES | SWT.NO);
//			else if(CurrentOS.getSystem() == "Windows") m = new MessageBox(MainFrameWin32.getShell(), SWT.SHEET | SWT.ICON_WARNING | SWT.YES | SWT.NO);
//			m.setText("Shutdown?");
//			m.setMessage("It looks like Amnesia might be running. If you shut down the Modloader now, it may not be able to install/uninstall any custom shaders that might be required by the mod. I recommend you shut down Amnesia first.\n\nDo you really want to exit now?");
//			if(m.open() != SWT.YES) {
//				return false;
//			}
//		}
		
		if(Engine.useCache) {
			new ModCache().writeCache();			
		}
		Log.info("Closing application.");
		return true;
	}
}
