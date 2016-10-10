package modloader;

import java.util.List;
import java.util.Properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
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
import org.eclipse.swt.events.MouseAdapter;

public class MainFrameWin32 {
	
	//Components
	private static Display display = Display.getDefault();
	private static Label labelAuthor, labelReq, labelVer, labelTitle;
	private static Text textDesc;
	private static Shell shell;
	public static Composite rightPanel, leftPanel;
	public static Table tableMods;
	public static TableItem modItem;
	private static Button buttonFolder, buttonPrefs, buttonQuit;
	public static Button buttonRefresh, buttonRefreshCancel, buttonLaunch, buttonLaunch2;
	private static TableColumn columnMods;
	private static Label labelShader, labelShaderVal, sep, labelModsFound;
	public static Label labelModAmount, labelPath;
	public static MenuItem menuItemLauncher, menuItemGame, menuItemGame2;
	public static Menu menuList, menuList2;
	public static ProgressBar progressBar;
		
	public static Display getDisplay()
	{
		return MainFrameWin32.display;
	}
	
	public static Table getTable()
	{
		return tableMods;
	}
		
	public static Composite getModPanel()
	{
		return MainFrameWin32.leftPanel;
	}
	
	public static Shell getShell() {
		return shell;
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
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell(display, SWT.CLOSE | SWT.MIN | SWT.TITLE);
		shell.setImage(SWTResourceManager.getImage(MainFrameWin32.class, "/resources/icon_application.png"));
		shell.setBackgroundImage(SWTResourceManager.getImage(MainFrameWin32.class, "/resources/modloader_bg.png"));
		shell.setSize(703, 578);
		shell.setText(Engine.appName);
		shell.addListener(SWT.Close, new Listener() {
			public void handleEvent(Event event) {
				event.doit = Engine.shutDown();
			}
		});
		shell.addMouseMoveListener(new MouseMoveListener() {
			public void mouseMove(MouseEvent arg0) {
				Shaders.promptRestore();
			}
		});
		
		Common.center(shell);
		
		try {
			Properties p = ConfigManager.loadConfig(Engine.prefPath);
				
			Engine.setGameDirectory(p.getProperty("GameDir"));
			Engine.refreshBoot = Boolean.parseBoolean(p.getProperty("RefreshOnStartup"));
			Engine.useCache = Boolean.parseBoolean(p.getProperty("UseCache"));
			Engine.useSteam = Boolean.parseBoolean(p.getProperty("UseSteam"));
			Engine.startGame = Boolean.parseBoolean(p.getProperty("PrimaryGame"));
			Engine.setWarnExec(Boolean.parseBoolean(p.getProperty("WarnExec")));
			Engine.setWarnShader(Boolean.parseBoolean(p.getProperty("WarnShader")));
		} catch (Exception e) {
			Log.error("Error loading config: " + Engine.prefPath, e);
		}
		shell.setLayout(null);
		
		buttonRefresh = new Button(shell, SWT.NONE);
		buttonRefresh.setBounds(10, 10, 110, 25);
		buttonRefresh.setImage(SWTResourceManager.getImage(MainFrameWin32.class, "/resources/icon_refresh.png"));
		buttonRefresh.setBackground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		buttonRefresh.setText("Scan for mods");
		buttonRefresh.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				Engine.abortRefresh = false;
				new Refresh().refreshList();
			}
		});

		buttonRefreshCancel = new Button(shell, SWT.NONE);
		buttonRefreshCancel.setBounds(10, 10, 110, 25);
		buttonRefreshCancel.setBackground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		buttonRefreshCancel.setText("Cancel");
		buttonRefreshCancel.setVisible(false);
		buttonRefreshCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				Engine.abortRefresh = true;
				buttonRefreshCancel.setVisible(false);
				buttonRefresh.setVisible(true);
			}
		});

		buttonFolder = new Button(shell, SWT.NONE);
		buttonFolder.setBounds(10, 514, 110, 26);
		buttonFolder.setBackground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		buttonFolder.setText("Open mods folder");
		buttonFolder.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				Engine.openDir(Engine.getModDirectory());
			}
		});
		
		buttonLaunch = new SplitButton(shell, SWT.NONE);
		buttonLaunch.setBounds(228, 514, 110, 26);
		buttonLaunch.setBackground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		buttonLaunch.setText("Launch mod");
		menuCreator(buttonLaunch.getMenu());
		shell.setDefaultButton(buttonLaunch);
		buttonLaunch.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if(Engine.startGame) Engine.launchMod(shell, CurrentOS.getGameExe());
				else Engine.launchMod(shell, CurrentOS.getLauncherExe());
			}
		});
		
		buttonLaunch2 = new Button(shell, SWT.NONE);
		buttonLaunch2.setBounds(228, 514, 110, 26);
		buttonLaunch2.setBackground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		buttonLaunch2.setText("Launch mod");
		buttonLaunch2.setVisible(false);
		buttonLaunch2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if(Engine.warnExec) {
					MessageBox m = new MessageBox(shell, SWT.SHEET | SWT.YES | SWT.NO | SWT.ICON_INFORMATION);
					m.setText("Mod Launch");
					if(Engine.useSteam) m.setMessage("This mod features a custom executable. This means that, according to the author, you must start this executable in order to play this mod. Custom executables must be launched outside of Steam.\nAre you sure you want to start this mod?");
					else m.setMessage("This mod features a custom executable. This means that, according to the author, you must start this executable in order to play this mod.\nAre you sure you want to start this mod?");
					int i = m.open();
					if(i != SWT.YES) return;
				}
				Engine.launchMod(shell, CurrentOS.getGameExe());
			}
		});

		buttonQuit = new Button(shell, SWT.NONE);
		buttonQuit.setBounds(344, 514, 110, 26);
		buttonQuit.setBackground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		buttonQuit.setText("Quit");
		buttonQuit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				//resetShaders();
				if(Engine.shutDown()) {
					Log.printLog();
					System.exit(0);
				}
			}
		});
		
		buttonPrefs = new Button(shell, SWT.NONE);
		buttonPrefs.setBounds(577, 514, 110, 26);
		buttonPrefs.setBackground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		buttonPrefs.setText("Options");
		buttonPrefs.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if(CurrentOS.getSystem() == "MacOS") {
					PreferencesOSX prefs = new PreferencesOSX(shell, SWT.SHEET);
					prefs.open();
				} else if(CurrentOS.getSystem() == "Windows") {
					PreferencesWin32 prefs = new PreferencesWin32(shell, SWT.SHEET);
					prefs.open();
				}
			}
		});
		
		shell.setBackgroundMode(SWT.INHERIT_FORCE);
		
		rightPanel = new Composite(shell, SWT.BORDER);
		rightPanel.setBounds(344, 203, 343, 305);
		rightPanel.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		
		leftPanel = new Composite(shell, SWT.BORDER);
		leftPanel.setBounds(10, 203, 328, 305);
		leftPanel.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		leftPanel.setBackgroundMode(SWT.INHERIT_FORCE);
		
		labelTitle = new Label(rightPanel, SWT.WRAP);
		labelTitle.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		labelTitle.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.BOLD));
		labelTitle.setAlignment(SWT.CENTER);
		labelTitle.setBounds(10, 10, 319, 21);
		labelTitle.setText("Mod Title");
		
		textDesc = new Text(rightPanel, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		textDesc.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		textDesc.setText("This is the description of the mod selected in the list to the right.");
		textDesc.setBounds(10, 59, 319, 196);
		
		labelAuthor = new Label(rightPanel, SWT.NONE);
		labelAuthor.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		labelAuthor.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.ITALIC));
		labelAuthor.setBounds(10, 37, 319, 16);
		labelAuthor.setText("By Author");
		
		labelReq = new Label(rightPanel, SWT.NONE);
		labelReq.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		labelReq.setBounds(10, 278, 200, 13);
		labelReq.setText("Required game version:");
		
		labelVer = new Label(rightPanel, SWT.NONE);
		labelVer.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		labelVer.setFont(SWTResourceManager.getFont("Segoe UI", 8, SWT.BOLD));
		labelVer.setAlignment(SWT.RIGHT);
		labelVer.setBounds(216, 278, 113, 13);
		labelVer.setText("Undefined");
		
		labelShader = new Label(rightPanel, SWT.NONE);
		labelShader.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		labelShader.setBounds(10, 259, 200, 13);
		labelShader.setText("Custom shaders:");
		
		labelShaderVal = new Label(rightPanel, SWT.NONE);
		labelShaderVal.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		labelShaderVal.setFont(SWTResourceManager.getFont("Segoe UI", 8, SWT.BOLD));
		labelShaderVal.setAlignment(SWT.RIGHT);
		labelShaderVal.setBounds(216, 259, 113, 13);
		labelShaderVal.setText("Undefined");
		leftPanel.setLayout(null);
		
		sep = new Label(leftPanel, SWT.SEPARATOR | SWT.HORIZONTAL);
		sep.setBounds(0, 251, 324, 2);
		
		tableMods = new Table(leftPanel, SWT.FULL_SELECTION);
		tableMods.setBounds(0, 4, 324, 245);
		tableMods.setBackgroundMode(SWT.INHERIT_FORCE);
		tableMods.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		tableMods.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				Engine.displayModInfo(shell, labelTitle, labelAuthor, labelVer, labelShaderVal, textDesc);
			}
		});
		tableMods.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent arg0) {
				if(Engine.startGame) Engine.launchMod(shell, CurrentOS.getGameExe());
				else Engine.launchMod(shell, CurrentOS.getLauncherExe());
			}
		});
		columnMods = new TableColumn(tableMods, SWT.NONE);
		columnMods.setWidth(307);
		columnMods.setText("Mods");
		labelModsFound = new Label(leftPanel, SWT.NONE);
		labelModsFound.setBounds(10, 278, 151, 13);
		labelModsFound.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		labelModsFound.setText("Mods found:");
		labelModAmount = new Label(leftPanel, SWT.RIGHT);
		labelModAmount.setBounds(167, 278, 147, 13);
		labelModAmount.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		labelModAmount.setText("0");
		
		menuList = new Menu(tableMods);
		menuList2 = new Menu(tableMods);
		tableMods.setMenu(menuList);
		
		menuItemGame2 = new MenuItem(menuList2, SWT.NONE);
		menuItemGame2.setImage(SWTResourceManager.getImage(MainFrameOSX.class, "/resources/icon_game.png"));
		menuItemGame2.setText("Start Game");
		menuItemGame2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				Engine.launchMod(shell, CurrentOS.getGameExe());
			}
		});
		
		menuItemLauncher = new MenuItem(menuList, SWT.NONE);
		menuItemLauncher.setImage(SWTResourceManager.getImage(MainFrameWin32.class, "/resources/icon_launcher.png"));
		menuItemLauncher.setText("Start Launcher");
		menuItemLauncher.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				Engine.launchMod(shell, CurrentOS.getLauncherExe());
			}
		});
		
		menuItemGame = new MenuItem(menuList, SWT.NONE);
		menuItemGame.setImage(SWTResourceManager.getImage(MainFrameWin32.class, "/resources/icon_game.png"));
		menuItemGame.setText("Start Game");
		menuItemGame.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				Engine.launchMod(shell, CurrentOS.getGameExe());
			}
		});
		
		progressBar = new ProgressBar(shell, SWT.SMOOTH | SWT.INDETERMINATE);
		progressBar.setBounds(126, 12, 561, 21);
		progressBar.setVisible(false);
		
		Engine.setup(tableMods, menuList2);
		labelPath = new Label(leftPanel, SWT.NONE);
		labelPath.setFont(SWTResourceManager.getFont("Consolas", 8, SWT.NORMAL));
		labelPath.setBounds(10, 259, 304, 13);
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

		if(!Engine.useSteam) {			
			MenuItem launcher = new MenuItem(menu, SWT.PUSH);
			launcher.setText("Start Launcher");
			launcher.setImage(SWTResourceManager.getImage(MainFrameWin32.class, "/resources/icon_launcher.png"));
			launcher.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent arg0) {
					Log.info("Launcher selected.");
					Engine.launchMod(shell, CurrentOS.getLauncherExe());
				}
			});
		}

		MenuItem amnesia = new MenuItem(menu, SWT.PUSH);
		amnesia.setText("Start Game");
		amnesia.setImage(SWTResourceManager.getImage(MainFrameWin32.class, "/resources/icon_game.png"));
		amnesia.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				Log.info("Direct game selected.");
				Engine.launchMod(shell, CurrentOS.getGameExe());
			}
		});
	}
}
