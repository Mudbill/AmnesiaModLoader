package modloader;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;

public class PreferencesWin32 extends Dialog {

	public static boolean firstTime = false;
	private Object result;
	private Shell shell;
	private Button buttonBrowseGame, radioUseSame, radioUseCustom, buttonBrowseMod, buttonCancel, buttonOK, buttonRefreshBoot, buttonWeb, 
		radioLauncher, radioGame, buttonMinimize, buttonApplyShader, buttonYoutube, buttonTwitter, buttonForum, buttonCache, buttonClearCache, buttonUseSteam;
	private Text textGameDir, textModDir;
	private TabFolder tabFolder;
	private TabItem tabGeneral, tabAbout;
	private Composite panelGeneral, panelAbout;
	private Label labelGameDir, labelModDir, labelName, labelVer, labelAuthor, iconPreview, labelSize, labelPrimary, labelEmail, labelPromo;
	private Group groupMisc, groupDir, groupIcon;
	private Scale slider;
	private Button checkWarnConfig, checkWarnSteam, checkWarnCustomExec, checkWarnShaders, checkWarnPatch, checkUpdate;
	private Button buttonClearSettings;
	private Button buttonUpdate;
	
	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public PreferencesWin32(Shell parent, int style) {
		super(parent, style);
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}
	
	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(getParent(), getStyle());
		try {
			shell.setImage(SWTResourceManager.getImage(PreferencesWin32.class, "/resources/icon_preferences.png"));
		} catch (Exception e1) {
			Log.error("", e1);
		}
		shell.setSize(560, 500);
		shell.setText("Options");
		shell.setLayout(null);
		if(!Engine.getFrameInit())Common.center(shell);
		
		tabFolder = new TabFolder(shell, SWT.NONE);
		tabFolder.setBounds(10, 10, 534, 423);
		
		panelGeneral = new Composite(tabFolder, SWT.NONE);
		panelGeneral.setLayout(null);
		
		panelAbout = new Composite(tabFolder, SWT.NONE);
		panelAbout.setLayout(null);

		tabGeneral = new TabItem(tabFolder, SWT.NONE);
		tabGeneral.setText("General");
		tabGeneral.setControl(panelGeneral);
		
		TabItem tabAdvanced = new TabItem(tabFolder, SWT.NONE);
		tabAdvanced.setText("Advanced");
		
		Composite panelAdvanced = new Composite(tabFolder, SWT.NONE);
		tabAdvanced.setControl(panelAdvanced);
		
		Group groupWarnings = new Group(panelAdvanced, SWT.NONE);
		groupWarnings.setText("Warnings");
		groupWarnings.setBounds(10, 10, 494, 189);
		
		checkWarnCustomExec = new Button(groupWarnings, SWT.CHECK);
		checkWarnCustomExec.setSelection(true);
		checkWarnCustomExec.setBounds(10, 19, 16, 18);
		checkWarnCustomExec.setText("Display a warning when starting a mod that uses a custom executable.");
		
		checkWarnShaders = new Button(groupWarnings, SWT.CHECK);
		checkWarnShaders.setSelection(true);
		checkWarnShaders.setBounds(10, 43, 16, 28);
		checkWarnShaders.setText("Display a warning when the Modloader is about to install custom shaders\nif they are found.");
		
		checkWarnConfig = new Button(groupWarnings, SWT.CHECK);
		checkWarnConfig.setSelection(true);
		checkWarnConfig.setBounds(10, 82, 16, 18);
		checkWarnConfig.setText("Display a warning before updating the config file (upon new releases).");
		
		checkWarnSteam = new Button(groupWarnings, SWT.CHECK);
		checkWarnSteam.setSelection(true);
		checkWarnSteam.setBounds(10, 106, 16, 18);
		checkWarnSteam.setText("Display a warning about Steam pop-ups when launching a mod\nthrough Steam.");
		
		checkWarnPatch = new Button(groupWarnings, SWT.CHECK);
		checkWarnPatch.setSelection(true);
		checkWarnPatch.setBounds(10, 130, 16, 33);
		checkWarnPatch.setText("Display a warning about patching mods to work with Amnesia update 1.3. \nDisabling this will automatically patch new out-of-date configs.");
		
		Label labelWarnExec = new Label(groupWarnings, SWT.WRAP);
		labelWarnExec.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent arg0) {
				if(checkWarnCustomExec.getSelection()) checkWarnCustomExec.setSelection(false);
				else checkWarnCustomExec.setSelection(true);
			}
		});
		labelWarnExec.setBounds(32, 19, 452, 18);
		labelWarnExec.setText("Display a warning when starting a mod that uses a custom executable.");
		
		Label labelWarnShaders = new Label(groupWarnings, SWT.WRAP);
		labelWarnShaders.setBounds(32, 43, 452, 28);
		labelWarnShaders.setText("Display a warning when the Modloader is about to install custom shaders if they are found.");
		labelWarnShaders.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent arg0) {
				if(checkWarnShaders.getSelection()) checkWarnShaders.setSelection(false);
				else checkWarnShaders.setSelection(true);
			}
		});
		Label labelWarnConfig = new Label(groupWarnings, SWT.WRAP);
		labelWarnConfig.setBounds(32, 82, 452, 18);
		labelWarnConfig.setText("Display a warning before updating the config file (upon new releases).");
		labelWarnConfig.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent arg0) {
				if(checkWarnConfig.getSelection()) checkWarnConfig.setSelection(false);
				else checkWarnConfig.setSelection(true);
			}
		});
		Label labelWarnSteam = new Label(groupWarnings, SWT.WRAP);
		labelWarnSteam.setBounds(32, 106, 452, 18);
		labelWarnSteam.setText("Display a warning about Steam pop-ups when launching a mod through Steam.");
		labelWarnSteam.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent arg0) {
				if(checkWarnSteam.getSelection()) checkWarnSteam.setSelection(false);
				else checkWarnSteam.setSelection(true);
			}
		});
		Label labelWarnPatch = new Label(groupWarnings, SWT.WRAP);
		labelWarnPatch.setBounds(32, 130, 452, 33);
		labelWarnPatch.setText("Display a warning about patching mods to work with Amnesia update 1.3. Disabling this will automatically patch new out-of-date configs.");
		labelWarnPatch.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent arg0) {
				if(checkWarnPatch.getSelection()) checkWarnPatch.setSelection(false);
				else checkWarnPatch.setSelection(true);
			}
		});
		Group groupOther = new Group(panelAdvanced, SWT.NONE);
		groupOther.setText("Other");
		groupOther.setBounds(10, 205, 494, 182);
		
		buttonClearCache = new Button(groupOther, SWT.NONE);
		buttonClearCache.setLocation(254, 116);
		buttonClearCache.setSize(230, 56);
		buttonClearCache.setEnabled(false);
		buttonClearCache.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		buttonClearCache.setText("Clear cache");
		buttonClearCache.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				Log.info("Clearing cache via preferences.");
				new ModCache().clearCache();
				buttonClearCache.setEnabled(false);
			}
		});
		
		buttonClearSettings = new Button(groupOther, SWT.NONE);
		buttonClearSettings.setImage(SWTResourceManager.getImage(PreferencesWin32.class, "/resources/icon_reload.png"));
		buttonClearSettings.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		buttonClearSettings.setBounds(10, 116, 230, 56);
		buttonClearSettings.setText("Reset preferences");
		buttonClearSettings.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				MessageBox m = new MessageBox(shell, SWT.YES | SWT.NO | SWT.ICON_QUESTION);
				m.setText("Shut down?");
				m.setMessage("This will factory reset the settings. Doing this will cause the application to shut down. Are you sure?");
				if(m.open() == SWT.YES) {
					ConfigManager.clearPreferences();
					System.exit(0);
				}
			}
		});
		
		buttonUpdate = new Button(groupOther, SWT.NONE);
		buttonUpdate.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		buttonUpdate.setBounds(364, 18, 120, 23);
		buttonUpdate.setText("Check for updates");
		buttonUpdate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				boolean b = Update.compareVersions(Update.getLatestVersion());
				if(!b) {
					MessageBox m = new MessageBox(shell, SWT.ICON_INFORMATION);
					m.setText("Update");
					m.setMessage("Already up to date! Most recent version: " + Update.onlineVersion);
					m.open();
				} else Update.promptUpdate();
			}
		});
		
		checkUpdate = new Button(groupOther, SWT.CHECK);
		checkUpdate.setBounds(10, 21, 348, 16);
		checkUpdate.setText("Check for updates automatically when starting application");
		
		tabAbout = new TabItem(tabFolder, SWT.NONE);
		tabAbout.setText("About");
		tabAbout.setControl(panelAbout);
		
		labelName = new Label(panelAbout, SWT.NONE);
		labelName.setBounds(10, 10, 172, 17);
		labelName.setText(Engine.appName);
		
		labelVer = new Label(panelAbout, SWT.NONE);
		labelVer.setBounds(20, 33, 152, 13);
		labelVer.setFont(SWTResourceManager.getFont("Segoe UI", 8, SWT.NORMAL));
		labelVer.setText("Version " + Engine.getVersion());
		
		labelAuthor = new Label(panelAbout, SWT.NONE);
		labelAuthor.setBounds(328, 10, 188, 17);
		labelAuthor.setAlignment(SWT.RIGHT);
		labelAuthor.setText("Developed by Mudbill");
		
		buttonWeb = new Button(panelAbout, SWT.NONE);
		buttonWeb.setBounds(10, 300, 250, 55);
		buttonWeb.setToolTipText(Engine.urlWeb);
		buttonWeb.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		buttonWeb.setText("View online page");
		buttonWeb.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				try {
					Common.openWebpage(new URL(Engine.urlWeb));
				} catch (MalformedURLException e) {
					Log.error("MalformedURLException", e);
				}
			}
		});
		
		buttonForum = new Button(panelAbout, SWT.NONE);
		buttonForum.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		buttonForum.setBounds(266, 300, 250, 55);
		buttonForum.setText("View forum thread");
		buttonForum.setToolTipText(Engine.urlForum);
		buttonForum.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				try {
					Common.openWebpage(new URL(Engine.urlForum));
				} catch (MalformedURLException e) {
					Log.error("MalformedURLException", e);
				}
			}
		});

		buttonTwitter = new Button(panelAbout, SWT.NONE);
		buttonTwitter.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		buttonTwitter.setImage(SWTResourceManager.getImage(PreferencesWin32.class, "/resources/icon_twitter.png"));
		buttonTwitter.setBounds(458, 361, 26, 26);
		buttonTwitter.setToolTipText(Engine.urlTwitter);
		buttonTwitter.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				try {
					Common.openWebpage(new URL(Engine.urlTwitter));
				} catch (MalformedURLException e) {
					Log.error("MalformedURLException", e);
				}
			}
		});

		buttonYoutube = new Button(panelAbout, SWT.NONE);
		buttonYoutube.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		buttonYoutube.setImage(SWTResourceManager.getImage(PreferencesWin32.class, "/resources/icon_youtube.png"));
		buttonYoutube.setBounds(490, 361, 26, 26);
		buttonYoutube.setToolTipText(Engine.urlYoutube);
		buttonYoutube.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				try {
					Common.openWebpage(new URL(Engine.urlYoutube));
				} catch (MalformedURLException e) {
					Log.error("MalformedURLException", e);
				}
			}
		});
		
		labelPromo = new Label(panelAbout, SWT.NONE);
		labelPromo.setBounds(10, 368, 172, 13);
		labelPromo.setText("You can find me here:");
		
		labelEmail = new Label(panelAbout, SWT.NONE);
		labelEmail.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.ITALIC));
		labelEmail.setAlignment(SWT.RIGHT);
		labelEmail.setBounds(300, 365, 152, 17);
		labelEmail.setText("mudbill@buttology.net");
		
		ScrolledComposite scrolledComposite = new ScrolledComposite(panelAbout, SWT.BORDER | SWT.V_SCROLL);
		scrolledComposite.setExpandVertical(true);
		scrolledComposite.setBounds(10, 52, 506, 242);
		scrolledComposite.setExpandHorizontal(true);
		
		ShellAboutWin32 shellAbout = new ShellAboutWin32(scrolledComposite, SWT.NONE);
		scrolledComposite.setContent(shellAbout);
		scrolledComposite.setMinSize(shellAbout.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		shellAbout.addListener(SWT.Activate, new Listener() {
		    public void handleEvent(Event event) {
		        scrolledComposite.setFocus();
		    }
		});
		
		groupDir = new Group(panelGeneral, SWT.NONE);
		groupDir.setBounds(10, 10, 506, 194);
		groupDir.setText("Settings");
		groupDir.setLayout(null);

		labelGameDir = new Label(groupDir, SWT.NONE);
		labelGameDir.setBounds(10, 46, 138, 16);
		labelGameDir.setText("Game directory:");
		
		textGameDir = new Text(groupDir, SWT.BORDER);
		textGameDir.setEditable(true);
		textGameDir.setBounds(10, 68, 393, 24);
		textGameDir.setToolTipText("Your game installation directory.");

		buttonBrowseGame = new Button(groupDir, SWT.NONE);
		buttonBrowseGame.setBounds(409, 67, 84, 26);
		buttonBrowseGame.setImage(SWTResourceManager.getImage(PreferencesWin32.class, "/resources/icon_folder.png"));
		buttonBrowseGame.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		buttonBrowseGame.setText("Browse...");
		
		labelModDir = new Label(groupDir, SWT.NONE);
		labelModDir.setBounds(10, 95, 138, 16);
		labelModDir.setText("Mod directory:");
		
		radioUseSame = new Button(groupDir, SWT.RADIO);
		radioUseSame.setBounds(20, 116, 138, 16);
		radioUseSame.setText("Use same as game");
		
		radioUseCustom = new Button(groupDir, SWT.RADIO);
		radioUseCustom.setBounds(20, 138, 138, 16);
		radioUseCustom.setText("Use custom:");
		
		textModDir = new Text(groupDir, SWT.BORDER);
		textModDir.setEditable(false);
		textModDir.setBounds(10, 160, 393, 23);
		textModDir.setEnabled(false);
		textModDir.setToolTipText("The folder you wish to search for mods in. Selecting a larger directory will increase the load time!");

		buttonBrowseMod = new Button(groupDir, SWT.NONE);
		buttonBrowseMod.setBounds(409, 159, 84, 25);
		buttonBrowseMod.setImage(SWTResourceManager.getImage(PreferencesWin32.class, "/resources/icon_folder.png"));
		buttonBrowseMod.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		buttonBrowseMod.setEnabled(false);
		buttonBrowseMod.setText("Browse...");
		buttonBrowseMod.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				
				DirectoryDialog dd= new DirectoryDialog(shell, SWT.SHEET);
				dd.setText("Open Mod Directory");
				dd.setFilterPath(textGameDir.getText());
								
				String s = dd.open();
				
				if(s != null) {
					textModDir.setText(s);
				}
			}
		});
		
		buttonUseSteam = new Button(groupDir, SWT.CHECK);
		buttonUseSteam.setToolTipText("Enable this if you use the Steam copy of Amnesia. If you use retail, leave this unchecked.\r\nPS: If this is unchecked, you might get the \"Could not init Steam\" error for Steam copies.");
		buttonUseSteam.setBounds(10, 22, 93, 18);
		buttonUseSteam.setText("Use Steam");
		buttonUseSteam.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if(buttonUseSteam.getSelection()) {
					radioGame.setEnabled(false);
					radioLauncher.setEnabled(false);
				} else {
					radioGame.setEnabled(true);
					radioLauncher.setEnabled(true);
				}
			}
		});
		radioUseCustom.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				textModDir.setEnabled(true);
				textModDir.setEditable(true);
				buttonBrowseMod.setEnabled(true);
			}
		});
		radioUseSame.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				textModDir.setEnabled(false);
				textModDir.setEditable(false);
				buttonBrowseMod.setEnabled(false);
			}
		});
		buttonBrowseGame.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				
				DirectoryDialog dd= new DirectoryDialog(shell, SWT.SHEET);
				dd.setMessage("Select your game installation directory.");
				dd.setText("Open Game Directory");
				dd.setFilterPath(textGameDir.getText());
				
				if(CurrentOS.getSystem() == "MacOS") {
					if(new File(textGameDir.getText()).exists()) dd.setFilterPath(textGameDir.getText());
					else dd.setFilterPath("/");
					
					Common.toggleHiddenFiles();
				}
				
				String s = dd.open();
				
				if(s != null) {
					textGameDir.setText(s);
				}
			}
		});

		groupMisc = new Group(panelGeneral, SWT.NONE);
		groupMisc.setBounds(10, 210, 320, 177);
		groupMisc.setText("Preferences");
		groupMisc.setLayout(null);
		
		buttonCache = new Button(groupMisc, SWT.CHECK);
		buttonCache.setSelection(true);
		buttonCache.setToolTipText("If the Modloader should save a cache of the last list shown, \r\nand load it upon next start. This will immediately show the mods in the list, \r\nbut the information might be out-of-date if you've made changes to your directory.");
		buttonCache.setBounds(13, 24, 162, 16);
		buttonCache.setText("Use cached list on startup");
		
		buttonRefreshBoot = new Button(groupMisc, SWT.CHECK);
		buttonRefreshBoot.setToolTipText("If the Modloader should automatically refresh the mod list when starting. Startup will be slower.");
		buttonRefreshBoot.setBounds(13, 46, 134, 16);
		buttonRefreshBoot.setText("Update list on startup");
		buttonRefreshBoot.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if(buttonRefreshBoot.getSelection()) {
					buttonCache.setSelection(false);
					buttonCache.setEnabled(false);
				} else buttonCache.setEnabled(true);
			}
		});

		groupIcon = new Group(panelGeneral, SWT.NONE);
		groupIcon.setBounds(336, 210, 180, 177);
		groupIcon.setText("Icon size");
		groupIcon.setLayout(null);
		
		iconPreview = new Label(groupIcon, SWT.NONE);
		iconPreview.setBounds(106, 25, 64, 64);
		if(ConfigManager.loadConfig(Engine.prefPath) != null) {
			iconPreview.setImage(Common.scale(SWTResourceManager.getImage(PreferencesWin32.class, "/resources/icon_default.png"), Engine.getIconSize()));
		} else iconPreview.setImage(Common.scale(SWTResourceManager.getImage(PreferencesWin32.class, "/resources/icon_default.png"), 48));

		labelSize = new Label(groupIcon, SWT.CENTER);
		labelSize.setBounds(13, 95, 42, 21);
		labelSize.setText("48x");
		
		slider = new Scale(groupIcon, SWT.VERTICAL);
		slider.setBounds(13, 25, 42, 64);
		slider.setToolTipText("Drag to select the size you want icons to show up with.");
		slider.setPageIncrement(1);
		slider.setMaximum(2);
		slider.setSelection(1);
		slider.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				Log.info("Changed icon slider position to " + slider.getSelection());
				
				if(slider.getSelection() == 0) {
					labelSize.setText("64x");
					iconPreview.setImage(Common.scale(SWTResourceManager.getImage(PreferencesWin32.class, "/resources/icon_default.png"), 64));
				}
				if(slider.getSelection() == 1) {
					labelSize.setText("48x");
					iconPreview.setImage(Common.scale(SWTResourceManager.getImage(PreferencesWin32.class, "/resources/icon_default.png"), 48));
				}
				if(slider.getSelection() == 2) {
					labelSize.setText("32x");
					iconPreview.setImage(Common.scale(SWTResourceManager.getImage(PreferencesWin32.class, "/resources/icon_default.png"), 32));
				}
				if(slider.getSelection() == 3) {
					labelSize.setText("16x");
					iconPreview.setImage(Common.scale(SWTResourceManager.getImage(PreferencesWin32.class, "/resources/icon_default.png"), 16));
				}
			}
		});
		
		buttonMinimize = new Button(groupMisc, SWT.CHECK);
		buttonMinimize.setToolTipText("If the Modloader should minimize the window after starting a mod.");
		buttonMinimize.setBounds(13, 68, 202, 16);
		buttonMinimize.setText("Minimize Modloader on mod start");
		buttonApplyShader = new Button(groupMisc, SWT.CHECK);
		buttonApplyShader.setToolTipText("If the Modloader should automatically install custom shaders when starting a mod. It will also uninstall the shaders when done.");
		buttonApplyShader.setBounds(13, 90, 273, 16);
		buttonApplyShader.setText("Apply custom shaders upon launch (if available)");
		buttonApplyShader.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if(buttonApplyShader.getSelection()) {
					MessageBox m = new MessageBox(shell, SWT.SHEET | SWT.OK | SWT.ICON_WARNING);
					m.setMessage("Disclaimer:\nThis is currently an experimental feature. This means that, although everything should work fine, I have not tested it enough to confirm this. It will modify some files in your Amnesia directory. It creates a backup, but I recommend you create one yourself in case of malfunction. The folder that is modified is named \"shaders\" and the backup name will be \"shaders_backup\". Other files are left alone.\n\nIf you find an issue, please report it to me so I can fix it, but keep in mind that you're on your own if things go wrong.");
					m.setText("Beta Feature");
					m.open();
				}
			}
		});
		
		labelPrimary = new Label(groupMisc, SWT.NONE);
		labelPrimary.setBounds(13, 112, 134, 15);
		labelPrimary.setText("Primary launch option:");
		radioLauncher = new Button(groupMisc, SWT.RADIO);
		radioLauncher.setBounds(23, 133, 70, 16);
		radioLauncher.setText("Launcher");
		radioGame = new Button(groupMisc, SWT.RADIO);
		radioGame.setBounds(23, 155, 85, 16);
		radioGame.setText("Direct game");
				
		buttonOK = new Button(shell, SWT.NONE);
		buttonOK.setBounds(378, 439, 80, 23);
		buttonOK.setText("Save");
		shell.setDefaultButton(buttonOK);
		buttonOK.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				acceptPrefs();
			}
		});
		
		buttonCancel = new Button(shell, SWT.NONE);
		buttonCancel.setBounds(464, 439, 80, 23);
		buttonCancel.setText("Cancel");
		buttonCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				Log.info("Preferences cancelled.");
				//isOpen = false;
				shell.close();
			}
		});
		loadData();
	}
	
	private void loadData()
	{
		if(new ModCache().checkCache()) buttonClearCache.setEnabled(true);
		
		Properties p = ConfigManager.loadConfig(Engine.prefPath);

		if(p == null) {
			Log.warn("Could not read " + Engine.prefPath + ". Will create new file upon OK.");
			radioUseSame.setSelection(true);
			radioLauncher.setSelection(true);
			buttonMinimize.setSelection(false);
			buttonRefreshBoot.setSelection(false);
			checkUpdate.setSelection(true);
		} else {
			boolean update = true;
			boolean useSameDir = true;
			boolean refreshOnStartup = false;
			boolean minimize = false;
			boolean primaryGame = false;
			boolean shader = false;
			boolean cache = false;
			boolean useSteam = true;
			boolean warnExec = true, warnShader = true, warnConfig = true, warnSteam = true, warnPatch = true;
			int sliderVal = 2;
			String modDir = "";
			String gameDir = "";

			try {modDir = p.getProperty("ModDir");} catch (Exception e) {Log.error("", e);}
			try {gameDir = p.getProperty("GameDir");} catch (Exception e) {Log.error("", e);}
			try {useSameDir = Boolean.parseBoolean(p.getProperty("UseSameDir"));} catch (Exception e) {Log.error("", e);}
			try {useSteam = Boolean.parseBoolean(p.getProperty("UseSteam"));} catch (Exception e) {Log.error("", e);}
			try {refreshOnStartup = Boolean.parseBoolean(p.getProperty("RefreshOnStartup"));} catch (Exception e) {Log.error("", e);}
			try {minimize = Boolean.parseBoolean(p.getProperty("Minimize"));} catch (Exception e) {Log.error("", e);}
			try {primaryGame = Boolean.parseBoolean(p.getProperty("PrimaryGame"));} catch (Exception e) {Log.error("", e);}
			try {shader = Boolean.parseBoolean(p.getProperty("ApplyShaders"));} catch (Exception e) {Log.error("", e);}
			try {sliderVal = Integer.parseInt(p.getProperty("IconSize"));} catch (Exception e) {Log.error("", e);}
			try {warnExec = Boolean.parseBoolean(p.getProperty("WarnExec"));} catch (Exception e) {Log.error("", e);}
			try {warnShader = Boolean.parseBoolean(p.getProperty("WarnShader"));} catch (Exception e) {Log.error("", e);}
			try {warnConfig = Boolean.parseBoolean(p.getProperty("WarnConfig"));} catch (Exception e) {Log.error("", e);}
			try {warnSteam = Boolean.parseBoolean(p.getProperty("WarnSteam"));} catch (Exception e) {Log.error("", e);}
			try {warnPatch = Boolean.parseBoolean(p.getProperty("WarnPatch"));} catch (Exception e) {Log.error("", e);}
			try {cache = Boolean.parseBoolean(p.getProperty("UseCache"));} catch (Exception e) {Log.error("", e);}
			try {update = Boolean.parseBoolean(p.getProperty("CheckForUpdates"));} catch (Exception e) {Log.error("", e);}
			
//			boolean b[] = {warnExec, warnShader, warnConfig};
//			loadWarns(b);
			
			if(useSteam) {
				radioGame.setEnabled(false);
				radioLauncher.setEnabled(false);
			}
			
			if(sliderVal == 0) labelSize.setText("64x");
			if(sliderVal == 1) labelSize.setText("48x");
			if(sliderVal == 2) labelSize.setText("32x");
			if(sliderVal == 3) labelSize.setText("16x");
			
			if(useSameDir) {
				radioUseSame.setSelection(true);
				textModDir.setEnabled(false);
				buttonBrowseMod.setEnabled(false);
			} else {
				radioUseCustom.setSelection(true);
				textModDir.setEnabled(true);
				buttonBrowseMod.setEnabled(true);
				textModDir.setEditable(true);
			}

			if(primaryGame)			radioGame.setSelection(true);
			else					radioLauncher.setSelection(true);

			if(refreshOnStartup) {
				buttonRefreshBoot.setSelection(true);
				buttonCache.setEnabled(false);
				buttonCache.setSelection(false);
			}
			else buttonRefreshBoot.setSelection(false);

			buttonMinimize.setSelection(minimize);
			buttonApplyShader.setSelection(shader);
			buttonCache.setSelection(cache);
			buttonUseSteam.setSelection(useSteam);
			checkWarnConfig.setSelection(warnConfig);
			checkWarnSteam.setSelection(warnSteam);
			checkWarnCustomExec.setSelection(warnExec);
			checkWarnShaders.setSelection(warnShader);
			checkWarnPatch.setSelection(warnPatch);
			checkUpdate.setSelection(update);
			
			textGameDir.setText(gameDir);
			textModDir.setText(modDir);
			slider.setSelection(sliderVal);
		}
	}
	
	private void acceptPrefs() 
	{
		if(Engine.getModDirectory() != null) {
			boolean firstTime = false;
			if(!new File(Engine.prefPath).exists()) firstTime = true;
			
			String gameDir = textGameDir.getText();
			String modDir = textModDir.getText();

			Properties p = new Properties();

			p.setProperty("RefreshOnStartup", buttonRefreshBoot.getSelection()+"");
			p.setProperty("UseCache", buttonCache.getSelection()+"");
			p.setProperty("UseSteam", buttonUseSteam.getSelection()+"");
			p.setProperty("Minimize", buttonMinimize.getSelection()+"");
			p.setProperty("ApplyShaders", buttonApplyShader.getSelection()+"");
			p.setProperty("CheckForUpdates", checkUpdate.getSelection()+"");
			
			if(radioUseSame.getSelection()) {
				p.setProperty("UseSameDir", "true");
				Engine.useSameDir = true;
			} else {
				p.setProperty("UseSameDir", "false");
				Engine.useSameDir = false;
			}

			if(radioGame.getSelection()) {
				p.setProperty("PrimaryGame", "true");
				Engine.startGame = true;
			} else {
				p.setProperty("PrimaryGame", "false");
				Engine.startGame = false;
			}

			p.setProperty("IconSize", ""+slider.getSelection());
			p.setProperty("ModDir", modDir);
			p.setProperty("GameDir", gameDir);
			p.setProperty("WarnExec", ""+checkWarnCustomExec.getSelection());
			p.setProperty("WarnShader", ""+checkWarnShaders.getSelection());
			p.setProperty("WarnConfig", ""+checkWarnConfig.getSelection());
			p.setProperty("WarnSteam", ""+checkWarnSteam.getSelection());
			p.setProperty("WarnPatch", ""+checkWarnPatch.getSelection());
			
			ConfigManager.writeConfig(p, Engine.prefPath);

			PatchConfig.setWarnPatch(checkWarnPatch.getSelection());

			Engine.setUseSteam(buttonUseSteam.getSelection(), MainFrameWin32.tableMods, MainFrameWin32.menuList, MainFrameWin32.menuList2);
			Engine.setModDirectory(modDir);
			Engine.setGameDirectory(gameDir);
			Engine.setWarnExec(checkWarnCustomExec.getSelection());
			Engine.setWarnShader(checkWarnShaders.getSelection());
			Engine.setWarnSteam(checkWarnSteam.getSelection());
			
			if(!new File(Engine.portPath).exists()) {
				Properties p2 = new Properties();
				p2.setProperty("Port", "9999");
				ConfigManager.writeConfig(p2, Engine.prefPath);
				Log.info("Writing default port settings.");
			}
			
			shell.close();
			
			if(!gameDir.isEmpty()) Start.checkConfigFolder(new File(gameDir + File.separator + "config" + File.separator + "modloader.cfg"), Boolean.parseBoolean(p.getProperty("WarnConfig")));

			if(firstTime) {
				PreferencesWin32.firstTime = firstTime;
				Engine.openFrame();
			}
		}
	}
}
