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
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;

public class Preferences extends Dialog {

	private static String prefPath = CurrentOS.getSaveDir() + File.separator + CurrentOS.getConfigName();
	private static String portPath = CurrentOS.getSaveDir() + File.separator + CurrentOS.getPortConfigName();
	
	private static final String urlWeb = "https://www.buttology.net/downloads/amnesia-modloader";
	private static final String urlForum = "https://www.frictionalgames.com/forum/thread-25806.html";
	private static final String urlYoutube = "https://www.youtube.com/MrMudbill";
	private static final String urlTwitter = "https://www.twitter.com/Mudbill";	
	
	private Object result;
	private Shell shell;
	private Button buttonBrowseGame, radioUseSame, radioUseCustom, buttonBrowseMod, buttonCancel, buttonOK, buttonRefreshBoot, buttonWeb, 
		radioLauncher, radioGame, buttonMinimize, buttonApplyShader, buttonForum, buttonCache, buttonClearCache, buttonUseSteam;
	private Text textGameDir, textModDir;
	private TabFolder tabFolder;
	private TabItem tabGeneral, tabAbout;
	private Composite panelGeneral, panelAbout;
	private Label labelGameDir, labelModDir, labelName, labelVer, labelAuthor, iconPreview, labelSize, labelPrimary, labelEmail, labelPromo;
	private Group groupMisc, groupDir, groupIcon;
	private Scale slider;
	private Button checkWarnConfig, checkWarnSteam, checkWarnCustomExec, checkWarnShaders, checkWarnPatch;
	private Label labelAdvanced;
		
	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public Preferences(Shell parent, int style) {
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
		shell.setImage(SWTResourceManager.getImage(MainFrame.class, "/resources/icon_application.png"));
		shell.setSize(560, 500);
		shell.setText("Options");
		shell.setLayout(null);
		if(MainFrame.getShell() == null) {
			shell.setSize(560, 520); //Fix sizing of shell when standalone vs sheeted.
			Common.center(shell);
		}
		
		tabFolder = new TabFolder(shell, SWT.NONE);
		tabFolder.setBounds(10, 10, 534, 450);
		
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
		groupWarnings.setBounds(10, 0, 494, 199);
		
		checkWarnCustomExec = new Button(groupWarnings, SWT.CHECK);
		checkWarnCustomExec.setFont(SWTResourceManager.getFont("Lucida Grande", 12, SWT.NORMAL));
		checkWarnCustomExec.setSelection(true);
		checkWarnCustomExec.setBounds(10, 10, 470, 18);
		checkWarnCustomExec.setText("Display a warning when starting a mod that uses a custom executable.");
		
		checkWarnShaders = new Button(groupWarnings, SWT.CHECK);
		checkWarnShaders.setFont(SWTResourceManager.getFont("Lucida Grande", 12, SWT.NORMAL));
		checkWarnShaders.setSelection(true);
		checkWarnShaders.setBounds(10, 34, 470, 33);
		checkWarnShaders.setText("Display a warning when the Modloader is about to install custom shaders\nif they are found.");
		
		checkWarnConfig = new Button(groupWarnings, SWT.CHECK);
		checkWarnConfig.setFont(SWTResourceManager.getFont("Lucida Grande", 12, SWT.NORMAL));
		checkWarnConfig.setSelection(true);
		checkWarnConfig.setBounds(10, 73, 470, 18);
		checkWarnConfig.setText("Display a warning before updating the config file (upon new releases).");
		
		checkWarnSteam = new Button(groupWarnings, SWT.CHECK);
		checkWarnSteam.setFont(SWTResourceManager.getFont("Lucida Grande", 12, SWT.NORMAL));
		checkWarnSteam.setSelection(true);
		checkWarnSteam.setBounds(10, 97, 470, 34);
		checkWarnSteam.setText("Display a warning about Steam pop-ups when launching a mod\nthrough Steam.");
		
		checkWarnPatch = new Button(groupWarnings, SWT.CHECK);
		checkWarnPatch.setFont(SWTResourceManager.getFont("Lucida Grande", 12, SWT.NORMAL));
		checkWarnPatch.setSelection(true);
		checkWarnPatch.setBounds(10, 137, 470, 33);
		checkWarnPatch.setText("Display a warning about patching mods to work with Amnesia update 1.3. \nDisabling this will automatically patch new out-of-date configs.");
		
		Group groupOther = new Group(panelAdvanced, SWT.NONE);
		groupOther.setText("Other");
		groupOther.setBounds(10, 205, 494, 197);
		
		buttonClearCache = new Button(groupOther, SWT.NONE);
		buttonClearCache.setLocation(10, 114);
		buttonClearCache.setSize(470, 56);
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
		
		labelAdvanced = new Label(groupOther, SWT.WRAP);
		labelAdvanced.setFont(SWTResourceManager.getFont("Lucida Grande", 12, SWT.NORMAL));
		labelAdvanced.setBounds(10, 10, 470, 56);
		labelAdvanced.setText("I don't have that many other advanced options yet, so this place is a bit empty. I will add more options here if I release any.");
		
		tabAbout = new TabItem(tabFolder, SWT.NONE);
		tabAbout.setText("About");
		tabAbout.setControl(panelAbout);
		
		labelName = new Label(panelAbout, SWT.NONE);
		labelName.setFont(SWTResourceManager.getFont("Lucida Grande", 12, SWT.NORMAL));
		labelName.setBounds(10, 0, 172, 17);
		labelName.setText("Amnesia Modloader");
		
		labelVer = new Label(panelAbout, SWT.NONE);
		labelVer.setFont(SWTResourceManager.getFont("Lucida Grande", 10, SWT.NORMAL));
		labelVer.setBounds(20, 23, 152, 13);
		labelVer.setText("Version " + MainFrame.getVersion());
		
		labelAuthor = new Label(panelAbout, SWT.NONE);
		labelAuthor.setFont(SWTResourceManager.getFont("Lucida Grande", 12, SWT.NORMAL));
		labelAuthor.setBounds(316, 0, 188, 17);
		labelAuthor.setAlignment(SWT.RIGHT);
		labelAuthor.setText("Developed by Mudbill");
		
		buttonWeb = new Button(panelAbout, SWT.NONE);
		buttonWeb.setBounds(10, 319, 250, 55);
		buttonWeb.setToolTipText(urlWeb);
		buttonWeb.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		buttonWeb.setText("View online page");
		buttonWeb.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				try {
					Common.openWebpage(new URL(urlWeb));
				} catch (MalformedURLException e) {
					Log.error("MalformedURLException", e);
				}
			}
		});
		
		buttonForum = new Button(panelAbout, SWT.NONE);
		buttonForum.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		buttonForum.setBounds(266, 319, 238, 55);
		buttonForum.setText("View forum thread");
		buttonForum.setToolTipText(urlForum);
		buttonForum.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				try {
					Common.openWebpage(new URL(urlForum));
				} catch (MalformedURLException e) {
					Log.error("MalformedURLException", e);
				}
			}
		});
		
		labelPromo = new Label(panelAbout, SWT.NONE);
		labelPromo.setFont(SWTResourceManager.getFont("Lucida Grande", 11, SWT.NORMAL));
		labelPromo.setBounds(10, 385, 172, 13);
		labelPromo.setText("You can find me here:");
		
		labelEmail = new Label(panelAbout, SWT.NONE);
		labelEmail.setFont(SWTResourceManager.getFont("Lucida Grande", 11, SWT.ITALIC));
		labelEmail.setAlignment(SWT.RIGHT);
		labelEmail.setBounds(300, 385, 152, 17);
		labelEmail.setText("mudbill@buttology.net");
		
		final ScrolledComposite scrolledComposite = new ScrolledComposite(panelAbout, SWT.V_SCROLL);
		scrolledComposite.setExpandVertical(true);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setBounds(10, 42, 494, 271);
		
		ShellAbout shellAbout = new ShellAbout(scrolledComposite, SWT.NONE);
		scrolledComposite.setContent(shellAbout);
		scrolledComposite.setMinSize(shellAbout.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
		ToolBar toolBar = new ToolBar(panelAbout, SWT.FLAT | SWT.RIGHT);
		toolBar.setBounds(458, 380, 46, 22);
		
		ToolItem itemTwitter = new ToolItem(toolBar, SWT.NONE);
		itemTwitter.setToolTipText(urlTwitter);
		itemTwitter.setImage(SWTResourceManager.getImage(Preferences.class, "/resources/icon_twitter.png"));
		itemTwitter.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				try {
					Common.openWebpage(new URL(urlTwitter));
				} catch (MalformedURLException e) {
					Log.error("MalformedURLException", e);
				}
			}
		});
		
		ToolItem itemYoutube = new ToolItem(toolBar, SWT.NONE);
		itemYoutube.setToolTipText(urlYoutube);
		itemYoutube.setImage(SWTResourceManager.getImage(Preferences.class, "/resources/icon_youtube.png"));
		itemYoutube.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				try {
					Common.openWebpage(new URL(urlYoutube));
				} catch (MalformedURLException e) {
					Log.error("MalformedURLException", e);
				}
			}
		});
				
		groupDir = new Group(panelGeneral, SWT.NONE);
		groupDir.setBounds(10, 0, 494, 201);
		groupDir.setText("Settings");
		groupDir.setLayout(null);

		labelGameDir = new Label(groupDir, SWT.NONE);
		labelGameDir.setBounds(10, 34, 138, 16);
		labelGameDir.setText("Game directory:");
		
		textGameDir = new Text(groupDir, SWT.BORDER);
		textGameDir.setEditable(false);
		textGameDir.setBounds(10, 56, 348, 20);
		textGameDir.setToolTipText("Your game installation directory.");

		buttonBrowseGame = new Button(groupDir, SWT.NONE);
		buttonBrowseGame.setBounds(364, 52, 113, 28);
		buttonBrowseGame.setImage(null);
		buttonBrowseGame.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		buttonBrowseGame.setText("Browse");
		
		labelModDir = new Label(groupDir, SWT.NONE);
		labelModDir.setBounds(10, 85, 138, 16);
		labelModDir.setText("Mod directory:");
		
		radioUseSame = new Button(groupDir, SWT.RADIO);
		radioUseSame.setBounds(20, 107, 138, 16);
		radioUseSame.setText("Use same as game");
		
		radioUseCustom = new Button(groupDir, SWT.RADIO);
		radioUseCustom.setBounds(20, 129, 138, 16);
		radioUseCustom.setText("Use custom:");
		
		textModDir = new Text(groupDir, SWT.BORDER);
		textModDir.setEditable(false);
		textModDir.setBounds(10, 151, 348, 20);
		textModDir.setEnabled(false);
		textModDir.setToolTipText("The folder you wish to search for mods in. Selecting a larger directory will increase the load time!");

		buttonBrowseMod = new Button(groupDir, SWT.NONE);
		buttonBrowseMod.setBounds(364, 149, 113, 25);
		buttonBrowseMod.setImage(null);
		buttonBrowseMod.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		buttonBrowseMod.setEnabled(false);
		buttonBrowseMod.setText("Browse");
		buttonBrowseMod.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if(CurrentOS.getSystem() == "MacOS") {
					
					DirectoryDialog dd= new DirectoryDialog(shell, SWT.SHEET);
					dd.setMessage("Select the directory to search for mods in.");
					dd.setText("Open Mod Directory");
					if(new File(textModDir.getText()).exists()) dd.setFilterPath(textModDir.getText());
					else dd.setFilterPath("/");
					
					Common.toggleHiddenFiles();
					
					String s = dd.open();
					
					if(s != null) {
						textModDir.setText(s);
					}
				}
			}
		});
		
		buttonUseSteam = new Button(groupDir, SWT.CHECK);
		buttonUseSteam.setToolTipText("Enable this if you use the Steam copy of Amnesia. If you use retail, leave this unchecked.");
		buttonUseSteam.setBounds(10, 10, 93, 18);
		buttonUseSteam.setFont(SWTResourceManager.getFont("Lucida Grande", 12, SWT.NORMAL));
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
				buttonBrowseMod.setEnabled(true);
			}
		});
		radioUseSame.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				textModDir.setEnabled(false);
				buttonBrowseMod.setEnabled(false);
			}
		});
		buttonBrowseGame.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if(CurrentOS.getSystem() == "MacOS") {

					DirectoryDialog dd= new DirectoryDialog(shell, SWT.SHEET);
					dd.setMessage("Select your game installation directory.");
					dd.setText("Open Game Directory");
					
					if(new File(textGameDir.getText()).exists()) dd.setFilterPath(textGameDir.getText());
					else dd.setFilterPath("/");

					Common.toggleHiddenFiles();

					String s = dd.open();

					if(s != null) {
						textGameDir.setText(s);
					}
				}
			}
		});

		groupMisc = new Group(panelGeneral, SWT.NONE);
		groupMisc.setBounds(10, 207, 320, 190);
		groupMisc.setText("Preferences");
		groupMisc.setLayout(null);
		
		buttonCache = new Button(groupMisc, SWT.CHECK);
		buttonCache.setSelection(true);
		buttonCache.setFont(SWTResourceManager.getFont("Lucida Grande", 11, SWT.NORMAL));
		buttonCache.setToolTipText("If the Modloader should save a cache of the last list shown, \r\nand load it upon next start. This will immediately show the mods in the list, \r\nbut the information might be out-of-date if you've made changes to your directory.");
		buttonCache.setBounds(10, 12, 296, 16);
		buttonCache.setText("Save last displayed list for next startup");
		
		buttonRefreshBoot = new Button(groupMisc, SWT.CHECK);
		buttonRefreshBoot.setFont(SWTResourceManager.getFont("Lucida Grande", 11, SWT.NORMAL));
		buttonRefreshBoot.setToolTipText("If the Modloader should automatically refresh the mod list when starting. Startup will be slower.");
		buttonRefreshBoot.setBounds(10, 34, 182, 16);
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
		groupIcon.setBounds(336, 207, 168, 190);
		groupIcon.setText("Icon size");
		groupIcon.setLayout(null);
		
		iconPreview = new Label(groupIcon, SWT.NONE);
		iconPreview.setBounds(61, 25, 64, 64);
		if(ConfigManager.loadConfig(prefPath) != null) {
			iconPreview.setImage(Common.scale(SWTResourceManager.getImage(Preferences.class, "/resources/icon_default.png"), MainFrame.getIconSize()));			
		} else iconPreview.setImage(Common.scale(SWTResourceManager.getImage(Preferences.class, "/resources/icon_default.png"), 48));

		labelSize = new Label(groupIcon, SWT.CENTER);
		labelSize.setBounds(13, 95, 42, 21);
		labelSize.setText("48x");
		
		slider = new Scale(groupIcon, SWT.VERTICAL);
		slider.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if(slider.getSelection() == 2) {
					labelSize.setText("64x");
					iconPreview.setImage(Common.scale(SWTResourceManager.getImage(Preferences.class, "/resources/icon_default.png"), 64));
				}
				if(slider.getSelection() == 1) {
					labelSize.setText("48x");
					iconPreview.setImage(Common.scale(SWTResourceManager.getImage(Preferences.class, "/resources/icon_default.png"), 48));
				}
				if(slider.getSelection() == 0) {
					labelSize.setText("32x");
					iconPreview.setImage(Common.scale(SWTResourceManager.getImage(Preferences.class, "/resources/icon_default.png"), 32));
				}
				if(slider.getSelection() == 3) {
					labelSize.setText("16x");
					iconPreview.setImage(Common.scale(SWTResourceManager.getImage(Preferences.class, "/resources/icon_default.png"), 16));
				}
			}
		});
		slider.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent arg0) {
				Log.info("Changed icon slider position to " + slider.getSelection());
			}
		});
		slider.setBounds(13, 25, 42, 64);
		slider.setToolTipText("Drag to select the size you want icons to show up with.");
		slider.setPageIncrement(1);
		slider.setMaximum(2);
		slider.setSelection(1);

		buttonMinimize = new Button(groupMisc, SWT.CHECK);
		buttonMinimize.setFont(SWTResourceManager.getFont("Lucida Grande", 11, SWT.NORMAL));
		buttonMinimize.setToolTipText("If the Modloader should minimize the window after starting a mod.");
		buttonMinimize.setBounds(10, 56, 296, 16);
		buttonMinimize.setText("Minimize Modloader on mod start");
		buttonApplyShader = new Button(groupMisc, SWT.CHECK);
		buttonApplyShader.setFont(SWTResourceManager.getFont("Lucida Grande", 11, SWT.NORMAL));
		buttonApplyShader.setToolTipText("If the Modloader should automatically install custom shaders when starting a mod. It will also uninstall the shaders when done.");
		buttonApplyShader.setBounds(10, 78, 296, 16);
		buttonApplyShader.setText("Apply custom shaders upon launch (if available)");
		buttonApplyShader.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if(buttonApplyShader.getSelection()) {
					MessageBox m = new MessageBox(shell, SWT.POP_UP | SWT.OK | SWT.ICON_WARNING);
					m.setMessage("Disclaimer:\nThis is currently an experimental feature. This means that, although everything should work fine, I have not tested it enough to confirm this. It will modify some files in your Amnesia directory. It creates a backup, but I recommend you create one yourself in case of malfunction. The folder that is modified is named \"shaders\" and the backup name will be \"shaders_backup\". Other files are left alone.\n\nIf you find an issue, please report it to me so I can fix it, but keep in mind that you're on your own if things go wrong.");
					m.setText("Beta Feature");
					m.open();
				}
			}
		});
		
		labelPrimary = new Label(groupMisc, SWT.NONE);
		labelPrimary.setFont(SWTResourceManager.getFont("Lucida Grande", 11, SWT.NORMAL));
		labelPrimary.setBounds(10, 100, 134, 15);
		labelPrimary.setText("Primary launch option:");
		radioLauncher = new Button(groupMisc, SWT.RADIO);
		radioLauncher.setFont(SWTResourceManager.getFont("Lucida Grande", 11, SWT.NORMAL));
		radioLauncher.setBounds(20, 121, 110, 16);
		radioLauncher.setText("Launcher");
		radioGame = new Button(groupMisc, SWT.RADIO);
		radioGame.setFont(SWTResourceManager.getFont("Lucida Grande", 11, SWT.NORMAL));
		radioGame.setBounds(20, 143, 110, 16);
		radioGame.setText("Direct game");
		
		
		buttonOK = new Button(shell, SWT.NONE);
		buttonOK.setBounds(454, 460, 90, 29);
		buttonOK.setText("Save");
		shell.setDefaultButton(buttonOK);
		buttonOK.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				acceptPrefs();
			}
		});
		
		buttonCancel = new Button(shell, SWT.NONE);
		buttonCancel.setBounds(358, 460, 90, 29);
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
		
		Properties p = ConfigManager.loadConfig(prefPath);

		if(p == null) {
			Log.warn("Could not read " + CurrentOS.getConfigName() + ". Will create new file upon OK.");
			radioUseSame.setSelection(true);
			radioLauncher.setSelection(true);
			buttonMinimize.setSelection(false);
			buttonRefreshBoot.setSelection(false);
		} else {
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
			
			if(useSteam) {
				radioGame.setEnabled(false);
				radioLauncher.setEnabled(false);
			}

			if(sliderVal == 2) labelSize.setText("64x");
			if(sliderVal == 1) labelSize.setText("48x");
			if(sliderVal == 0) labelSize.setText("32x");
			if(sliderVal == 3) labelSize.setText("16x");
			
			if(useSameDir) {
				radioUseSame.setSelection(true);
				textModDir.setEnabled(false);
				buttonBrowseMod.setEnabled(false);
			} else {
				radioUseCustom.setSelection(true);
				textModDir.setEnabled(true);
				buttonBrowseMod.setEnabled(true);
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
			
			textGameDir.setText(gameDir);
			textModDir.setText(modDir);
			slider.setSelection(sliderVal);
		}
	}
	
	private void acceptPrefs() 
	{
		if(MainFrame.getModDirectory() != null) {
			boolean firstTime = false;
			if(!new File(prefPath).exists()) firstTime = true;
			
			String gameDir = textGameDir.getText();
			String modDir = textModDir.getText();

			Properties p = new Properties();
			
			p.setProperty("RefreshOnStartup", buttonRefreshBoot.getSelection()+"");
			p.setProperty("UseCache", buttonCache.getSelection()+"");
			p.setProperty("UseSteam", buttonUseSteam.getSelection()+"");
			p.setProperty("Minimize", buttonMinimize.getSelection()+"");
			p.setProperty("ApplyShaders", buttonApplyShader.getSelection()+"");

			if(radioUseSame.getSelection()) {
				p.setProperty("UseSameDir", "true");
				MainFrame.useSameDir = true;
			} else {
				p.setProperty("UseSameDir", "false");
				MainFrame.useSameDir = false;
			}

			if(radioGame.getSelection()) {
				p.setProperty("PrimaryGame", "true");
				MainFrame.startGame = true;
			} else {
				p.setProperty("PrimaryGame", "false");
				MainFrame.startGame = false;
			}
			
			p.setProperty("IconSize", ""+slider.getSelection());
			p.setProperty("ModDir", modDir);
			p.setProperty("GameDir", gameDir);
			p.setProperty("WarnExec", ""+checkWarnCustomExec.getSelection());
			p.setProperty("WarnShader", ""+checkWarnShaders.getSelection());
			p.setProperty("WarnConfig", ""+checkWarnConfig.getSelection());
			p.setProperty("WarnSteam", ""+checkWarnSteam.getSelection());
			p.setProperty("WarnPatch", ""+checkWarnPatch.getSelection());
			MainFrame.setUseSteam(buttonUseSteam.getSelection());
			MainFrame.setModDirectory(modDir);
			MainFrame.setGameDirectory(gameDir);
			MainFrame.setWarnExec(checkWarnCustomExec.getSelection());
			MainFrame.setWarnShader(checkWarnShaders.getSelection());
			MainFrame.setWarnSteam(checkWarnSteam.getSelection());
			PatchConfigOSX.setWarnPatch(checkWarnPatch.getSelection());

			ConfigManager.writeConfig(p, prefPath);
			
			if(!new File(portPath).exists()) {
				Properties p2 = new Properties();
				p2.setProperty("Port", "9999");
				ConfigManager.writeConfig(p2, prefPath);
				Log.info("Writing default port settings.");
			}
			
			shell.close();
			
			if(firstTime) {
				Start.checkConfigFolder(new File(gameDir + File.separator + "config" + File.separator + "modloader.cfg"), Boolean.parseBoolean(p.getProperty("WarnConfig")));
				MainFrame frame = new MainFrame();
				frame.open();
				firstTime = false;
			}
		}

	}
}
