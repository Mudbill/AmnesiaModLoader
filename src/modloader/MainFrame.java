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
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import de.ikoffice.widgets.SplitButton;

import org.eclipse.swt.widgets.ProgressBar;

public class MainFrame {
	
	private final static String appName = "Amnesia Modloader";
	private final static String appVersion = "1.5.0";
	private final static String cfgName = "main_init.cfg";
	
	private static String modDirectory = "";
	private static String gameDirectory = "";
	
	private static boolean warnExec = true, warnShader = true;

	private static List<String> shadersList = new ArrayList<String>();
	private static ModList modList = new ModList();
	public static boolean useSameDir = true;
	public static boolean refreshBoot = false;
	public static boolean startGame = false;
	public static boolean useCache = false;
	public static boolean abortRefresh = false;
	
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
	private static MenuItem menuItemLauncher, menuItemGame;
	private static FormData 	fd_buttonRefresh, fd_buttonFolder, fd_buttonPrefs, fd_buttonLaunch, fd_buttonQuit,
								fd_rightPanel, fd_leftPanel, fd_tableMods, fd_labelModAmount, fd_labelModsFound, fd_sep, 
								fd_progressBar;
	private Menu menuList;
	public static ProgressBar progressBar, progressBarInf;
	private static int progressMax;
	
	public static int getIconSize() {
		int i = 48;
		
		try {
			Properties p = ConfigManager.loadConfig(Preferences.prefPath);

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
			Log.error("Could not get and scale image.");
		}
		return i;
	}
	
	public static Display getDisplay()
	{
		return MainFrame.display;
	}
	
	public static Table getTable()
	{
		return tableMods;
	}
	
	public static String getVersion()
	{
		return MainFrame.appVersion;
	}
	
	public static String getModDirectory()
	{
		if(useSameDir) return MainFrame.gameDirectory;
		else return MainFrame.modDirectory;
	}
	
	public static String getGameDirectory()
	{
		return MainFrame.gameDirectory;
	}
	
	public static Composite getModPanel()
	{
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
				progressBarInf.setVisible(false);
				progressBar.setVisible(true);
				progressMax = new File(modDirectory).listFiles().length;
				progressBar.setMaximum(progressMax);
				Log.info("Mods found: " + modList.getModsFound());
			}
		} catch (Exception e) {
			Log.error("Failed checking for mods.");			
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
				Log.error("Failed displaying mod info.");
				
//				MessageBox m = new MessageBox(shell, SWT.SHEET | SWT.ICON_ERROR);
//				m.setMessage("An unexpected error occurred while displaying mod info. It probably tried displaying info that doesn't exist because it failed searching for mods. This could be an issue on my part.\nI'm sorry D:");
//				m.setText("Error");
//				m.open();		
//				Log.error(e);
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
		shell.setImage(SWTResourceManager.getImage(MainFrame.class, "/resources/icon_application.png"));
		shell.setBackgroundImage(SWTResourceManager.getImage(MainFrame.class, "/resources/modloader_bg.png"));
		shell.setSize(703, 578);
		shell.setText(appName);
		shell.setLayout(new FormLayout());
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
			Properties p = ConfigManager.loadConfig(Preferences.prefPath);
				
			gameDirectory = p.getProperty("GameDir");
			refreshBoot = Boolean.parseBoolean(p.getProperty("RefreshOnStartup"));
			useCache = Boolean.parseBoolean(p.getProperty("UseCache"));
			startGame = Boolean.parseBoolean(p.getProperty("PrimaryGame"));
			setWarnExec(Boolean.parseBoolean(p.getProperty("WarnExec")));
			setWarnShader(Boolean.parseBoolean(p.getProperty("WarnShader")));
		} catch (Exception e1) {
			Log.error("Error loading config: " + Preferences.prefPath);
		}
		
		buttonRefresh = new Button(shell, SWT.NONE);
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
		buttonRefreshCancel.setLayoutData(new FormData());
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
		buttonFolder.setBackground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		buttonFolder.setText("Open mods folder");
		buttonFolder.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				try {
					Log.info("Opening mods folder at: " + getModDirectory());
					Desktop.getDesktop().open(new File(getModDirectory()));
				} catch (IllegalArgumentException e) {
					Log.error("Could not open folder destination.");
				} catch (IOException e) {
					Log.error(e);
				}
			}
		});
		
		buttonLaunch = new SplitButton(shell, SWT.NONE);
		buttonLaunch.setBackground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		buttonLaunch.setText("Launch mod");
		menuCreator(buttonLaunch.getMenu());
		shell.setDefaultButton(buttonLaunch);
		buttonLaunch.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if(startGame) launchMod(CurrentOS.getGameExe());
				else launchMod(CurrentOS.getLauncherExe());
			}
		});
		
		buttonLaunch2 = new Button(shell, SWT.NONE);
		buttonLaunch2.setLayoutData(new FormData());
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

		buttonQuit = new Button(shell, SWT.NONE);
		buttonQuit.setBackground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		buttonQuit.setText("Quit");
		buttonQuit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				//resetShaders();
				if(CurrentOS.shutDown()) {
					Log.printLog();
					System.exit(0);
				}
			}
		});
		
		buttonPrefs = new Button(shell, SWT.NONE);
		buttonPrefs.setBackground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		buttonPrefs.setText("Preferences");
		buttonPrefs.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				Preferences prefs = new Preferences(shell, SWT.SHEET);
				prefs.open();
			}
		});
		
		rightPanel = new Composite(shell, SWT.BORDER);
		rightPanel.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		
		leftPanel = new Composite(shell, SWT.BORDER);
		leftPanel.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		
		labelTitle = new Label(rightPanel, SWT.WRAP);
		labelTitle.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		labelTitle.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.BOLD));
		labelTitle.setAlignment(SWT.CENTER);
		labelTitle.setBounds(10, 10, 319, 21);
		labelTitle.setText("Mod Title");
		
		textDesc = new Text(rightPanel, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		textDesc.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		textDesc.setText("This is the description of the mod selected in the list to the right.");
		textDesc.setBounds(10, 59, 319, 174);
		
		labelAuthor = new Label(rightPanel, SWT.NONE);
		labelAuthor.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		labelAuthor.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.ITALIC));
		labelAuthor.setBounds(10, 37, 319, 16);
		labelAuthor.setText("By Author");
		
		labelReq = new Label(rightPanel, SWT.NONE);
		labelReq.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		labelReq.setBounds(10, 260, 200, 15);
		labelReq.setText("Required game version:");
		
		labelVer = new Label(rightPanel, SWT.NONE);
		labelVer.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		labelVer.setFont(SWTResourceManager.getFont("Segoe UI", 8, SWT.BOLD));
		labelVer.setAlignment(SWT.RIGHT);
		labelVer.setBounds(216, 261, 113, 13);
		labelVer.setText("Undefined");
		
		labelShader = new Label(rightPanel, SWT.NONE);
		labelShader.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		labelShader.setBounds(10, 239, 200, 15);
		labelShader.setText("Custom shaders:");
		
		labelShaderVal = new Label(rightPanel, SWT.NONE);
		labelShaderVal.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		labelShaderVal.setFont(SWTResourceManager.getFont("Segoe UI", 8, SWT.BOLD));
		labelShaderVal.setAlignment(SWT.RIGHT);
		labelShaderVal.setBounds(216, 240, 113, 15);
		labelShaderVal.setText("Undefined");
		leftPanel.setLayoutData(fd_leftPanel);
		leftPanel.setLayout(new FormLayout());
		
		tableMods = new Table(leftPanel, SWT.FULL_SELECTION);
		tableMods.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		tableMods.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				displayModInfo();
			}
		});
		
		sep = new Label(leftPanel, SWT.SEPARATOR | SWT.HORIZONTAL);
		columnMods = new TableColumn(tableMods, SWT.NONE);
		columnMods.setWidth(307);
		columnMods.setText("Mods");
		labelModsFound = new Label(leftPanel, SWT.NONE);
		labelModsFound.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		labelModsFound.setText("Mods found:");
		labelModAmount = new Label(leftPanel, SWT.RIGHT);
		labelModAmount.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		labelModAmount.setText("0");
		
		menuList = new Menu(tableMods);
		tableMods.setMenu(menuList);
		
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
		progressBar.setVisible(false);
		progressBar.setMaximum(2000);
		
		progressBarInf = new ProgressBar(shell, SWT.INDETERMINATE);
		progressBarInf.setLayoutData(new FormData());
		progressBarInf.setVisible(false);

		//Design setup
		fd_tableMods = new FormData();
		fd_tableMods.bottom = new FormAttachment(sep, -1);
		fd_tableMods.top = new FormAttachment(0, 4);
		fd_tableMods.left = new FormAttachment(0);
		fd_tableMods.right = new FormAttachment(0, 324);
		tableMods.setLayoutData(fd_tableMods);
		fd_leftPanel = new FormData();
		fd_leftPanel.top = new FormAttachment(rightPanel, 0, SWT.TOP);
		fd_leftPanel.left = new FormAttachment(0, 10);
		fd_leftPanel.right = new FormAttachment(rightPanel, -6);
		fd_leftPanel.bottom = new FormAttachment(100, -42);
		leftPanel.setLayoutData(fd_leftPanel);
		fd_sep = new FormData();
		fd_sep.top = new FormAttachment(0, 251);
		fd_sep.left = new FormAttachment(0);
		fd_sep.right = new FormAttachment(100);
		sep.setLayoutData(fd_sep);
		fd_sep.bottom = new FormAttachment(100, -32);
		fd_labelModsFound = new FormData();
		fd_labelModsFound.left = new FormAttachment(0, 10);
		fd_labelModsFound.right = new FormAttachment(labelModAmount, -6);
		labelModsFound.setLayoutData(fd_labelModsFound);
		fd_labelModsFound.top = new FormAttachment(labelModAmount, 0, SWT.TOP);
		fd_labelModAmount = new FormData();
		fd_labelModAmount.top = new FormAttachment(sep, 7);
		fd_labelModAmount.right = new FormAttachment(100, -10);
		fd_labelModAmount.left = new FormAttachment(0, 167);
		labelModAmount.setLayoutData(fd_labelModAmount);
		fd_buttonRefresh = new FormData();
		fd_buttonRefresh.right = new FormAttachment(100, -577);
		fd_buttonRefresh.left = new FormAttachment(0, 10);
		fd_buttonRefresh.bottom = new FormAttachment(0, 35);
		fd_buttonRefresh.top = new FormAttachment(0, 10);
		buttonRefresh.setLayoutData(fd_buttonRefresh);
		fd_buttonFolder = new FormData();
		fd_buttonFolder.bottom = new FormAttachment(100, -10);
		fd_buttonFolder.top = new FormAttachment(leftPanel, 6);
		fd_buttonFolder.left = new FormAttachment(0, 10);
		fd_buttonFolder.right = new FormAttachment(0, 120);
		buttonFolder.setLayoutData(fd_buttonFolder);
		fd_buttonPrefs = new FormData();
		fd_buttonPrefs.bottom = new FormAttachment(100, -10);
		fd_buttonPrefs.top = new FormAttachment(rightPanel, 6);
		fd_buttonPrefs.right = new FormAttachment(100, -10);
		fd_buttonPrefs.left = new FormAttachment(100, -120);
		buttonPrefs.setLayoutData(fd_buttonPrefs);
		fd_buttonLaunch = new FormData();
		fd_buttonLaunch.bottom = new FormAttachment(100, -10);
		fd_buttonLaunch.top = new FormAttachment(leftPanel, 6);
		fd_buttonLaunch.left = new FormAttachment(buttonFolder, 108);
		fd_buttonLaunch.right = new FormAttachment(buttonQuit, -6);
		buttonLaunch.setLayoutData(fd_buttonLaunch);
		fd_buttonQuit = new FormData();
		fd_buttonQuit.bottom = new FormAttachment(100, -10);
		fd_buttonQuit.top = new FormAttachment(rightPanel, 6);
		fd_buttonQuit.right = new FormAttachment(buttonPrefs, -123);
		fd_buttonQuit.left = new FormAttachment(0, 344);
		buttonQuit.setLayoutData(fd_buttonQuit);
		fd_rightPanel = new FormData();
		fd_rightPanel.top = new FormAttachment(0, 219);
		fd_rightPanel.bottom = new FormAttachment(100, -42);
		fd_rightPanel.left = new FormAttachment(100, -353);
		fd_rightPanel.right = new FormAttachment(100, -10);
		rightPanel.setLayoutData(fd_rightPanel);
		fd_progressBar = new FormData();
		fd_progressBar.right = new FormAttachment(buttonPrefs, 0, SWT.RIGHT);
		fd_progressBar.top = new FormAttachment(0, 12);
		fd_progressBar.left = new FormAttachment(buttonRefresh, 6);
		fd_progressBar.bottom = new FormAttachment(0, 33);
		progressBar.setLayoutData(fd_progressBar);
		
		buttonRefreshCancel.setLayoutData(fd_buttonRefresh);
		buttonLaunch2.setLayoutData(fd_buttonLaunch);
		progressBarInf.setLayoutData(fd_progressBar);
		
		if(useCache) {
			new ModCache().loadCache();
		}
		
		if(refreshBoot) {
			abortRefresh = false;
			new Refresh().refreshList();
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
			
			Properties p = ConfigManager.loadConfig(Preferences.prefPath);

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
				Log.error(e1);
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

				Runtime runTime = Runtime.getRuntime();
				String[] nullString = null;
				File workingDir = new File(gameDirectory);
				runTime.exec(gameDirectory + File.separator + exec + " " + filePath, nullString, workingDir);
				
				Log.info("Running command: " + gameDirectory + File.separator + exec + " " + filePath);
			} catch (IOException e) {
				Log.error("Could not find Amnesia.exe in directory: " + gameDirectory);
				MessageBox m = new MessageBox(shell, SWT.SHEET | SWT.OK | SWT.ICON_ERROR);
				m.setMessage(exec + " was not found in the specified directory:\n\n" + gameDirectory + "\n\nDouble check the preferences to make sure you input the correct destination.");
				m.setText("Could not start");
				m.open();
			}
			
		} catch (ArrayIndexOutOfBoundsException e) {
			Log.error("No selected index!");
			MessageBox m = new MessageBox(shell, SWT.SHEET | SWT.OK | SWT.ICON_ERROR);
			m.setMessage("No mod selected to launch.");
			m.setText("Could not start");
			m.open();
		} catch (NullPointerException e) {
			Log.error(e);
		}
	}
}
