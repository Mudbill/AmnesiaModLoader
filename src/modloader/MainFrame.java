package modloader;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.internal.cocoa.NSWindow;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import de.ikoffice.widgets.SplitButton;

public class MainFrame {
	
	private final static String appName = "Amnesia Modloader";
	private final static String appVersion = "1.6.1";
	private final static String cfgName = "main_init.cfg";
	
	private static String modDirectory = "", gameDirectory = ""; 
	
	private static boolean warnExec = true, warnShader = true, warnSteam = true;

	private static List<String> shadersList = new ArrayList<String>();
	private static ModList modList = new ModList();
	public static boolean useSameDir = true;
	public static boolean refreshBoot = false;
	public static boolean startGame = false;
	public static boolean useCache = false;
	public static boolean abortRefresh = false;
	public static boolean useSteam = false;
	
	//Components
	private static Display display = Display.getDefault();
	private static Label labelAuthor, labelReq, labelVer, labelTitle;
	private static Text textDesc;
	private static Shell shell;
	public static Composite rightPanel, leftPanel;
	public static Table tableMods;
	public static TableItem modItem;
	private static Button buttonFolder, buttonPrefs, buttonLaunch, buttonQuit, buttonLaunch2;
	public static Button buttonRefresh, buttonRefreshCancel;
	private static TableColumn columnMods;
	private static Label labelShader, labelShaderVal, sep, labelModsFound;
	public static Label labelModAmount;
	private static MenuItem menuItemLauncher, menuItemGame, menuItemGame2;
	private static Menu menuList, menuList2;
	public static ProgressBar progressBar;
	
	public static int getIconSize() {
		int i = 48;
		
		try {
			Properties p = ConfigManager.loadConfig(CurrentOS.getSaveDir() + File.separator + CurrentOS.getConfigName());

			int i2 = Integer.parseInt(p.getProperty("IconSize"));

			switch(i2) {
			case 0: {
				i = 32; break;
			}
			case 1: {
				i = 48; break;
			}
			case 2: {
				i = 64; break;
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
	
	public static Display getDisplay() {
		return MainFrame.display;
	}
	
	public static Table getTable() {
		return tableMods;
	}
	
	public static String getVersion() {
		return MainFrame.appVersion;
	}
	
	public static String getModDirectory() {
		if(useSameDir) return MainFrame.gameDirectory;
		else return MainFrame.modDirectory;
	}
	
	public static String getGameDirectory() {
		return MainFrame.gameDirectory;
	}
		
	public static Composite getModPanel() {
		return MainFrame.leftPanel;
	}
	
	public static Shell getShell() {
		return shell;
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
	
	public static void setUseSteam(boolean var) {
		if(var && (MainFrame.getShell() != null)) tableMods.setMenu(menuList2);
		else if(!var && (MainFrame.getShell() != null)) tableMods.setMenu(menuList);
		useSteam = var;
	}
	
	public static void setModDirectory(String dir) {
		modDirectory = dir;
	}
	
	public static void setGameDirectory(String dir) {
		gameDirectory = dir;
	}
	
	public List<String> getModShaders(String fileName, File directory) {
		return shadersList;
	}
		
	public static void setupModList(List<TableItem> data) 
	{	
		tableMods.setData(data);
		tableMods.select(0);
		tableMods.setFocus();
	}
	
	/**
	 * Open the window.
	 * @wbp.parser.entryPoint
	 */
	public void open() {
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
		
	/**
	 * Searches specified directories and sub-directories for a file named "main_init.cfg"
	 */
	public void checkMods()
	{
		try {
			new FindFile(display, progressBar, new File(modDirectory), cfgName).start();

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
	public static void displayModInfo()
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
	public static void editLaunch(String var)
	{
		if(var != null) {
			buttonLaunch.setVisible(false);
			buttonLaunch2.setVisible(true);
			menuItemLauncher.setEnabled(false);
		} else {
			buttonLaunch.setVisible(true);
			buttonLaunch2.setVisible(false);
			menuItemLauncher.setEnabled(true);
		}
	}
	
	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell(display, SWT.CLOSE | SWT.MIN | SWT.TITLE);
		
		NSWindow nswindow = shell.view.window();
		nswindow.setCollectionBehavior(0);  
		nswindow.setShowsResizeIndicator(false);
		
		shell.setImage(SWTResourceManager.getImage(MainFrame.class, "/resources/icon_application.png"));
		shell.setBackgroundImage(SWTResourceManager.getImage(MainFrame.class, "/resources/modloader_bg.png"));
		shell.setSize(703, 572);
		shell.setText(appName);
		shell.addListener(SWT.Close, new Listener() {
			public void handleEvent(Event event) {
				event.doit = CurrentOS.shutDown();
			}
		});
		shell.addMouseMoveListener(new MouseMoveListener() {
			public void mouseMove(MouseEvent arg0) {
				if(Shaders.installed) {
					MessageBox m = new MessageBox(shell, SWT.OK | SWT.ICON_QUESTION);
					m.setText("Restore Shaders?");
					m.setMessage("Are you ready to restore the shaders? Please make sure you've closed the game, then click OK. The custom shaders will be uninstalled and the original ones restored.");
					if(m.open() != -1) {
						Log.info("Restore shaders!");
						boolean b = Shaders.uninstallShaders();
						if(b) {
							MessageBox m2 = new MessageBox(shell, SWT.OK | SWT.ICON_INFORMATION);
							m2.setText("Success");
							m2.setMessage("The shaders were successfully restored!");
							m2.open();
						} else {
							MessageBox m2 = new MessageBox(shell, SWT.OK | SWT.ICON_INFORMATION);
							m2.setText("Failed");
							m2.setMessage("The shaders could not be restored. You might have to do so manually.");
							m2.open();
						}
					}
				}
			}
		});
		Common.center(shell);
		
		try {
			Properties p = ConfigManager.loadConfig(CurrentOS.getSaveDir() + File.separator + CurrentOS.getConfigName());
				
			gameDirectory = p.getProperty("GameDir");
			refreshBoot = Boolean.parseBoolean(p.getProperty("RefreshOnStartup"));
			useCache = Boolean.parseBoolean(p.getProperty("UseCache"));
			startGame = Boolean.parseBoolean(p.getProperty("PrimaryGame"));
			setWarnExec(Boolean.parseBoolean(p.getProperty("WarnExec")));
			setWarnShader(Boolean.parseBoolean(p.getProperty("WarnShader")));
		} catch (Exception e) {
			Log.error("Error loading config: " + CurrentOS.getSaveDir() + File.separator + CurrentOS.getConfigName(), e);
		}
		shell.setLayout(null);
		
		buttonRefresh = new Button(shell, SWT.NONE);
		buttonRefresh.setBounds(0, 3, 130, 28);
		buttonRefresh.setImage(SWTResourceManager.getImage(MainFrame.class, "/resources/icon_refresh.png"));
		buttonRefresh.setBackground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		buttonRefresh.setText("Refresh");
		buttonRefresh.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				abortRefresh = false;
				new Refresh().refreshList();
			}
		});

		buttonRefreshCancel = new Button(shell, SWT.NONE);
		buttonRefreshCancel.setBounds(0, 3, 130, 28);
		buttonRefreshCancel.setBackground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		buttonRefreshCancel.setText("Cancel");
		buttonRefreshCancel.setVisible(false);
		buttonRefreshCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				abortRefresh = true;
				buttonRefreshCancel.setVisible(false);
				buttonRefresh.setVisible(true);
			}
		});

		buttonFolder = new Button(shell, SWT.NONE);
		buttonFolder.setBounds(5, 518, 145, 28);
		buttonFolder.setBackground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		buttonFolder.setText("Open mods folder");
		buttonFolder.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				try {
					if(!getModDirectory().equals("")) {
						Log.info("Opening mods folder at: " + getModDirectory());
						Desktop.getDesktop().open(new File(getModDirectory()));						
					} else {
						Log.warn("ModDir is empty.");
						new Error("Mod directory is empty.");						
					}
				} catch (IllegalArgumentException e) {
					Log.error("Could not open folder destination.", e);
				} catch (IOException e) {
					Log.error("", e);
				}
			}
		});
		
		buttonLaunch = new Button(shell, SWT.NONE); //Used for WindowBuilder Pro.

		if(CurrentOS.getSystem() == "Windows") {
			buttonLaunch = new SplitButton(shell, SWT.NONE);
			menuCreator(buttonLaunch.getMenu());
		} else  buttonLaunch = new Button(shell, SWT.NONE);
		
		buttonLaunch.setBounds(220, 518, 130, 28);
		buttonLaunch.setBackground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		buttonLaunch.setText("Launch mod");
		shell.setDefaultButton(buttonLaunch);
		buttonLaunch.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if(startGame) launchMod(CurrentOS.getGameExe());
				else launchMod(CurrentOS.getLauncherExe());
			}
		});
		
		buttonQuit = new Button(shell, SWT.NONE);
		buttonQuit.setImage(null);
		buttonQuit.setBounds(344, 518, 130, 28);
		buttonQuit.setBackground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		buttonQuit.setText("Quit");
		buttonQuit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if(CurrentOS.shutDown()) {
					Log.printLog();
					System.exit(0);
				}
			}
		});
		
		buttonLaunch2 = new Button(shell, SWT.NONE);
		buttonLaunch2.setBounds(0, 0, 98, 28);
		buttonLaunch2.setBackground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		buttonLaunch2.setText("Launch mod");
		buttonLaunch2.setVisible(false);
		buttonLaunch2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if(warnExec) {
					MessageBox m = new MessageBox(shell, SWT.SHEET | SWT.YES | SWT.NO | SWT.ICON_INFORMATION);
					m.setText("Mod Launch");
					m.setMessage("This mod features a custom executable. This means that, according to the author, you must start this executable in order to play this mod.\nAre you sure you want to start this mod?");
					int i = m.open();
					if(i != SWT.YES) return;
				}
				launchMod(CurrentOS.getGameExe());
			}
		});
		
		buttonPrefs = new Button(shell, SWT.NONE);
		buttonPrefs.setImage(null);
		buttonPrefs.setBounds(553, 518, 145, 28);
		buttonPrefs.setBackground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		buttonPrefs.setText("Options");
		buttonPrefs.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				Preferences prefs = new Preferences(shell, SWT.SHEET);
				prefs.open();
			}
		});
		
		rightPanel = new Composite(shell, SWT.BORDER);
		rightPanel.setBounds(350, 219, 343, 295);
		rightPanel.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		
		leftPanel = new Composite(shell, SWT.BORDER);
		leftPanel.setBounds(10, 219, 334, 295);
		leftPanel.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		
		labelTitle = new Label(rightPanel, SWT.WRAP);
		labelTitle.setFont(SWTResourceManager.getFont("Lucida Grande", 12, SWT.NORMAL));
		labelTitle.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		labelTitle.setAlignment(SWT.CENTER);
		labelTitle.setBounds(10, 10, 319, 21);
		labelTitle.setText("Mod Title");
		
		textDesc = new Text(rightPanel, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		textDesc.setFont(SWTResourceManager.getFont("Lucida Grande", 11, SWT.NORMAL));
		textDesc.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		textDesc.setText("This is the description of the mod selected in the list to the right.");
		textDesc.setBounds(10, 59, 319, 174);
		
		labelAuthor = new Label(rightPanel, SWT.NONE);
		labelAuthor.setFont(SWTResourceManager.getFont("Lucida Grande", 10, SWT.NORMAL));
		labelAuthor.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		labelAuthor.setBounds(10, 37, 319, 16);
		labelAuthor.setText("By Author");
		
		labelReq = new Label(rightPanel, SWT.NONE);
		labelReq.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		labelReq.setBounds(10, 260, 200, 15);
		labelReq.setText("Required game version:");
		
		labelVer = new Label(rightPanel, SWT.NONE);
		labelVer.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		labelVer.setAlignment(SWT.RIGHT);
		labelVer.setBounds(216, 261, 113, 13);
		labelVer.setText("Undefined");
		
		labelShader = new Label(rightPanel, SWT.NONE);
		labelShader.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		labelShader.setBounds(10, 239, 200, 15);
		labelShader.setText("Custom shaders:");
		
		labelShaderVal = new Label(rightPanel, SWT.NONE);
		labelShaderVal.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		labelShaderVal.setAlignment(SWT.RIGHT);
		labelShaderVal.setBounds(216, 240, 113, 15);
		labelShaderVal.setText("Undefined");
		leftPanel.setLayout(null);
		
		tableMods = new Table(leftPanel, SWT.FULL_SELECTION);
		tableMods.setBounds(0, 4, 332, 246);
		tableMods.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		tableMods.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				displayModInfo();
			}
		});
		
		sep = new Label(leftPanel, SWT.SEPARATOR | SWT.HORIZONTAL);
		sep.setBounds(0, 251, 332, 10);
		columnMods = new TableColumn(tableMods, SWT.NONE);
		columnMods.setWidth(307);
		columnMods.setText("Mods");
		labelModsFound = new Label(leftPanel, SWT.NONE);
		labelModsFound.setBounds(10, 268, 151, 14);
		labelModsFound.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		labelModsFound.setText("Mods found:");
		labelModAmount = new Label(leftPanel, SWT.RIGHT);
		labelModAmount.setBounds(167, 268, 155, 14);
		labelModAmount.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		labelModAmount.setText("0");
		
		menuList = new Menu(tableMods);
		menuList2 = new Menu(tableMods);
		tableMods.setMenu(menuList);
		
		menuItemGame2 = new MenuItem(menuList2, SWT.NONE);
		menuItemGame2.setImage(SWTResourceManager.getImage(MainFrame.class, "/resources/icon_game.png"));
		menuItemGame2.setText("Start Game");
		menuItemGame2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				launchMod(CurrentOS.getGameExe());
			}
		});
		
		menuItemLauncher = new MenuItem(menuList, SWT.NONE);
		menuItemLauncher.setImage(SWTResourceManager.getImage(MainFrame.class, "/resources/icon_launcher.png"));
		menuItemLauncher.setText("Start Launcher");
		menuItemLauncher.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				launchMod(CurrentOS.getLauncherExe());
			}
		});
		
		menuItemGame = new MenuItem(menuList, SWT.NONE);
		menuItemGame.setImage(SWTResourceManager.getImage(MainFrame.class, "/resources/icon_game.png"));
		menuItemGame.setText("Start Game");
		menuItemGame.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				launchMod(CurrentOS.getGameExe());
			}
		});
		
		progressBar = new ProgressBar(shell, SWT.SMOOTH);
		progressBar.setBounds(130, 5, 561, 21);
		progressBar.setMaximum(2000);
		progressBar.setVisible(false);
				
		if(useCache) {
			new ModCache().loadCache();
		}
		
		if(refreshBoot) {
			abortRefresh = false;
			new Refresh().refreshList();
		}
		
		Properties p = ConfigManager.loadConfig(CurrentOS.getSaveDir() + File.separator + CurrentOS.getConfigName());
		useSteam = Boolean.parseBoolean(p.getProperty("UseSteam"));
		
		if(useSteam) {
			tableMods.setMenu(menuList2);
		}
	}
	
	/**
	 * Creates a menu for a SplitButton.
	 * @param menu
	 */
	private static void menuCreator(Menu menu) {

		MenuItem[] items = menu.getItems();
		if (items != null) {
			for (MenuItem item : items) {
				item.dispose();
			}
		}

		MenuItem launcher = new MenuItem(menu, SWT.PUSH);
		launcher.setText("Start Launcher");
		launcher.setImage(SWTResourceManager.getImage(MainFrame.class, "/resources/icon_launcher.png"));
		launcher.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				Log.info("Launcher selected.");
				launchMod(CurrentOS.getLauncherExe());
			}
		});

		MenuItem amnesia = new MenuItem(menu, SWT.PUSH);
		amnesia.setText("Start Game");
		amnesia.setImage(SWTResourceManager.getImage(MainFrame.class, "/resources/icon_game.png"));
		amnesia.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				Log.info("Direct game selected.");
				launchMod(CurrentOS.getGameExe());
			}
		});
	}
	
	/**
	 * Function used to launch the selected mod. 
	 * @param exec - The executable to run (Launcher or Amnesia or custom)
	 */
	private static void launchMod(String exec) {
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
				Runtime runTime = Runtime.getRuntime();
				File workingDir = new File(gameDirectory);

				if(CurrentOS.getSystem() == "Windows") {
					
					String cmd = gameDirectory + File.separator + exec + " " + filePath;
					runTime.exec(cmd, null, workingDir);
					Log.info("Running command: " + cmd);
					
				} else if(CurrentOS.getSystem() == "MacOS") {
					
					new PatchConfigOSX().checkConfig(filePath);
					
					if(Boolean.parseBoolean(p.getProperty("UseSteam"))) {
						
						if(Boolean.parseBoolean(p.getProperty("WarnSteam"))) {
							MessageBox m = new MessageBox(shell, SWT.SHEET | SWT.OK | SWT.ICON_WARNING);
							m.setMessage("Launching this using Steam will require you to accept a prompt given by Steam in order to start the mod. Please click \"OK\" on the proceeding dialog.\n\nIf nothing happens, you might need to right click Steam in the Dock and select Library (this is just a Steam client bug that causes the window to not show up).\n\nYou can disable this warning in the advanced options.");
							m.setText("Steam");
							m.open();
						}
						ProcessBuilder pb = new ProcessBuilder("open", "steam://rungameid/57300//" + filePath.replace("/", "%2F").replace(" ", "%20"));
						pb.start();
					} else {
						ProcessBuilder pb = new ProcessBuilder("open", gameDirectory + File.separator + exec, "--args", filePath);
						pb.start();
					}
				}
				
				if(Boolean.parseBoolean(p.getProperty("Minimize"))) {
					shell.setMinimized(true);
				}
				
			} catch (IOException e) {
				Log.error("Could not find " + exec + " in directory: " + gameDirectory, e);
				MessageBox m = new MessageBox(shell, SWT.SHEET | SWT.OK | SWT.ICON_ERROR);
				m.setMessage(exec + " was not found in the specified directory:\n\n" + gameDirectory + "\n\nDouble check the options to make sure you input the correct destination.");
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
}
