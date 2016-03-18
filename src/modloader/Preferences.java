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

public class Preferences extends Dialog {

	public static String prefPath = CurrentOS.getSaveDir() + File.separator + CurrentOS.getConfigName();
	public static String portPath = CurrentOS.getSaveDir() + File.separator + CurrentOS.getPortConfigName();
	private static boolean warnExec = true;
	private static boolean warnShader = true;
	
	private static final String urlWeb = "https://www.buttology.net/downloads/amnesia-modloader";
	private static final String urlForum = "https://www.frictionalgames.com/forum/thread-25806.html";
	private static final String urlYoutube = "https://www.youtube.com/MrMudbill";
	private static final String urlTwitter = "https://www.twitter.com/Mudbill";	
	
	private Object result;
	private Shell shell;
	private Button buttonBrowseGame, radioUseSame, radioUseCustom, buttonBrowseMod, buttonCancel, buttonOK, buttonRefreshBoot, buttonWeb, 
		radioLauncher, radioGame, buttonMinimize, buttonApplyShader, buttonYoutube, buttonTwitter, buttonForum, buttonWarnings, buttonCache, buttonClearCache;
	private Text textGameDir, textModDir;
	private TabFolder tabFolder;
	private TabItem tabGeneral, tabAbout;
	private Composite panelGeneral, panelAbout;
	private Label labelGameDir, labelModDir, labelName, labelVer, labelAuthor, iconPreview, labelSize, labelPrimary, labelEmail, labelPromo;
	private Group groupMisc, groupDir, groupIcon;
	private Scale slider;
		
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
		shell.setImage(SWTResourceManager.getImage(Preferences.class, "/resources/icon_preferences.png"));
		shell.setSize(560, 500);
		shell.setText("Preferences");
		shell.setLayout(null);
		
		tabFolder = new TabFolder(shell, SWT.NONE);
		tabFolder.setBounds(10, 10, 534, 423);
		
		panelGeneral = new Composite(tabFolder, SWT.NONE);
		panelGeneral.setLayout(null);
		
		panelAbout = new Composite(tabFolder, SWT.NONE);
		panelAbout.setLayout(null);

		tabGeneral = new TabItem(tabFolder, SWT.NONE);
		tabGeneral.setText("General");
		tabGeneral.setControl(panelGeneral);
		
		tabAbout = new TabItem(tabFolder, SWT.NONE);
		tabAbout.setText("About");
		tabAbout.setControl(panelAbout);
		
		labelName = new Label(panelAbout, SWT.NONE);
		labelName.setBounds(10, 10, 172, 17);
		labelName.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
		labelName.setText("Amnesia Modloader");
		
		labelVer = new Label(panelAbout, SWT.NONE);
		labelVer.setBounds(20, 33, 152, 13);
		labelVer.setFont(SWTResourceManager.getFont("Segoe UI", 8, SWT.NORMAL));
		labelVer.setText("Version " + MainFrame.getVersion());
		
		labelAuthor = new Label(panelAbout, SWT.NONE);
		labelAuthor.setBounds(328, 10, 188, 17);
		labelAuthor.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
		labelAuthor.setAlignment(SWT.RIGHT);
		labelAuthor.setText("Developed by Mudbill");
		
		buttonWeb = new Button(panelAbout, SWT.NONE);
		buttonWeb.setBounds(10, 300, 250, 55);
		buttonWeb.setToolTipText(urlWeb);
		buttonWeb.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		buttonWeb.setText("View online page");
		buttonWeb.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				try {
					Common.openWebpage(new URL(urlWeb));
				} catch (MalformedURLException e) {
					Log.error(e);
				}
			}
		});
		
		buttonForum = new Button(panelAbout, SWT.NONE);
		buttonForum.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		buttonForum.setBounds(266, 300, 250, 55);
		buttonForum.setText("View forum thread");
		buttonForum.setToolTipText(urlForum);
		buttonForum.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				try {
					Common.openWebpage(new URL(urlForum));
				} catch (MalformedURLException e) {
					Log.error(e);
				}
			}
		});

		buttonTwitter = new Button(panelAbout, SWT.NONE);
		buttonTwitter.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		buttonTwitter.setImage(SWTResourceManager.getImage(Preferences.class, "/resources/icon_twitter.png"));
		buttonTwitter.setBounds(458, 361, 26, 26);
		buttonTwitter.setToolTipText(urlTwitter);
		buttonTwitter.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				try {
					Common.openWebpage(new URL(urlTwitter));
				} catch (MalformedURLException e) {
					Log.error(e);
				}
			}
		});

		buttonYoutube = new Button(panelAbout, SWT.NONE);
		buttonYoutube.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		buttonYoutube.setImage(SWTResourceManager.getImage(Preferences.class, "/resources/icon_youtube.png"));
		buttonYoutube.setBounds(490, 361, 26, 26);
		buttonYoutube.setToolTipText(urlYoutube);
		buttonYoutube.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				try {
					Common.openWebpage(new URL(urlYoutube));
				} catch (MalformedURLException e) {
					Log.error(e);
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
		
		ShellAbout shellAbout = new ShellAbout(scrolledComposite, SWT.NONE);
		scrolledComposite.setContent(shellAbout);
		scrolledComposite.setMinSize(shellAbout.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		shellAbout.addListener(SWT.Activate, new Listener() {
		    public void handleEvent(Event event) {
		        scrolledComposite.setFocus();
		    }
		});
		
		groupDir = new Group(panelGeneral, SWT.NONE);
		groupDir.setBounds(10, 10, 506, 177);
		groupDir.setText("Directories");
		groupDir.setLayout(null);

		labelGameDir = new Label(groupDir, SWT.NONE);
		labelGameDir.setBounds(13, 22, 138, 16);
		labelGameDir.setText("Game directory:");
		
		textGameDir = new Text(groupDir, SWT.BORDER);
		textGameDir.setBounds(13, 44, 393, 24);
		textGameDir.setToolTipText("Your game redist installation directory.");

		buttonBrowseGame = new Button(groupDir, SWT.NONE);
		buttonBrowseGame.setBounds(412, 43, 84, 26);
		buttonBrowseGame.setImage(SWTResourceManager.getImage(Preferences.class, "/resources/icon_folder.png"));
		buttonBrowseGame.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		buttonBrowseGame.setText("Browse...");
		
		labelModDir = new Label(groupDir, SWT.NONE);
		labelModDir.setBounds(13, 71, 138, 16);
		labelModDir.setText("Mod directory:");
		
		radioUseSame = new Button(groupDir, SWT.RADIO);
		radioUseSame.setBounds(23, 92, 138, 16);
		radioUseSame.setText("Use same as game");
		
		radioUseCustom = new Button(groupDir, SWT.RADIO);
		radioUseCustom.setBounds(23, 114, 138, 16);
		radioUseCustom.setText("Use custom:");
		
		textModDir = new Text(groupDir, SWT.BORDER);
		textModDir.setBounds(13, 136, 393, 23);
		textModDir.setEnabled(false);
		textModDir.setToolTipText("The folder you wish to search for mods in. Selecting a larger directory will increase the load time!");

		buttonBrowseMod = new Button(groupDir, SWT.NONE);
		buttonBrowseMod.setBounds(412, 135, 84, 25);
		buttonBrowseMod.setImage(SWTResourceManager.getImage(Preferences.class, "/resources/icon_folder.png"));
		buttonBrowseMod.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		buttonBrowseMod.setEnabled(false);
		buttonBrowseMod.setText("Browse...");
		buttonBrowseMod.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				DirectoryDialog dd= new DirectoryDialog(shell, SWT.SHEET);
				dd.setMessage("Select the directory to search for mods in.");
				dd.setText("Open Mod Directory");
				dd.setFilterPath(CurrentOS.getDefDir());
				String s = dd.open();
				
				if(s != null) {
					textModDir.setText(s);
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
				DirectoryDialog dd= new DirectoryDialog(shell, SWT.SHEET);
				dd.setMessage("Select your game installation directory.");
				dd.setText("Open Game Directory");
				dd.setFilterPath(CurrentOS.getDefDir());
				String s = dd.open();
				
				if(s != null) {
					textGameDir.setText(s);
				}
			}
		});

		groupMisc = new Group(panelGeneral, SWT.NONE);
		groupMisc.setBounds(10, 193, 320, 194);
		groupMisc.setText("Options");
		groupMisc.setLayout(null);
		
		buttonCache = new Button(groupMisc, SWT.CHECK);
		buttonCache.setToolTipText("If the Modloader should save a cache of the last list shown, \r\nand load it upon next start. This will immediately show the mods in the list, \r\nbut the information might be out-of-date if you've made changes to your directory.");
		buttonCache.setBounds(13, 24, 162, 16);
		buttonCache.setText("Use cached list on startup");
		
		buttonRefreshBoot = new Button(groupMisc, SWT.CHECK);
		buttonRefreshBoot.setToolTipText("If the Modloader should automatically refresh the mod list when starting. Startup will be slower.");
		buttonRefreshBoot.setBounds(23, 46, 134, 16);
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
		groupIcon.setBounds(336, 193, 180, 194);
		groupIcon.setText("Icon size");
		groupIcon.setLayout(null);
		
		iconPreview = new Label(groupIcon, SWT.NONE);
		iconPreview.setBounds(106, 25, 64, 64);
		if(ConfigManager.loadConfig(prefPath) != null) {
			iconPreview.setImage(Common.scale(SWTResourceManager.getImage(Preferences.class, "/resources/icon_default.png"), MainFrame.getIconSize()));			
		} else iconPreview.setImage(Common.scale(SWTResourceManager.getImage(Preferences.class, "/resources/icon_default.png"), 48));

		labelSize = new Label(groupIcon, SWT.CENTER);
		labelSize.setBounds(13, 95, 42, 21);
		labelSize.setFont(SWTResourceManager.getFont("GillSans", 14, SWT.NORMAL));
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
					iconPreview.setImage(Common.scale(SWTResourceManager.getImage(Preferences.class, "/resources/icon_default.png"), 64));
				}
				if(slider.getSelection() == 1) {
					labelSize.setText("48x");
					iconPreview.setImage(Common.scale(SWTResourceManager.getImage(Preferences.class, "/resources/icon_default.png"), 48));
				}
				if(slider.getSelection() == 2) {
					labelSize.setText("32x");
					iconPreview.setImage(Common.scale(SWTResourceManager.getImage(Preferences.class, "/resources/icon_default.png"), 32));
				}
				if(slider.getSelection() == 3) {
					labelSize.setText("16x");
					iconPreview.setImage(Common.scale(SWTResourceManager.getImage(Preferences.class, "/resources/icon_default.png"), 16));
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
		labelPrimary.setBounds(10, 125, 134, 15);
		labelPrimary.setText("Primary launch option:");
		radioLauncher = new Button(groupMisc, SWT.RADIO);
		radioLauncher.setBounds(20, 146, 70, 16);
		radioLauncher.setText("Launcher");
		radioGame = new Button(groupMisc, SWT.RADIO);
		radioGame.setBounds(20, 168, 85, 16);
		radioGame.setText("Direct game");
		
		buttonWarnings = new Button(groupMisc, SWT.NONE);
		buttonWarnings.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		buttonWarnings.setBounds(225, 161, 85, 23);
		buttonWarnings.setText("Warnings");
		buttonWarnings.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				WarningDialog w = new WarningDialog(shell, SWT.SHEET);
				w.open();
			}
		});
		
		buttonClearCache = new Button(groupMisc, SWT.NONE);
		buttonClearCache.setEnabled(false);
		buttonClearCache.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		buttonClearCache.setBounds(225, 21, 85, 23);
		buttonClearCache.setText("Clear cache");
		buttonClearCache.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				Log.info("Clearing cache via preferences.");
				new ModCache().clearCache();
				buttonClearCache.setEnabled(false);
			}
		});
		
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
		
		Properties p = ConfigManager.loadConfig(prefPath);

		if(p == null) {
			Log.warn("Could not read " + CurrentOS.getConfigName() + ". Will create new file upon OK.");
			radioUseSame.setSelection(true);
			radioLauncher.setSelection(true);
			buttonMinimize.setSelection(false);
			buttonRefreshBoot.setSelection(false);
		} else {
			boolean useSameDir;
			boolean refreshOnStartup;
			boolean minimize;
			boolean primaryGame;
			boolean shader;
			boolean cache;
			int sliderVal;
			String modDir;
			String gameDir;

			modDir = p.getProperty("ModDir");
			gameDir = p.getProperty("GameDir");
			useSameDir = Boolean.parseBoolean(p.getProperty("UseSameDir"));
			refreshOnStartup = Boolean.parseBoolean(p.getProperty("RefreshOnStartup"));
			minimize = Boolean.parseBoolean(p.getProperty("Minimize"));
			primaryGame = Boolean.parseBoolean(p.getProperty("PrimaryGame"));
			shader = Boolean.parseBoolean(p.getProperty("ApplyShaders"));
			sliderVal = Integer.parseInt(p.getProperty("IconSize"));
			warnExec = Boolean.parseBoolean(p.getProperty("WarnExec"));
			warnShader = Boolean.parseBoolean(p.getProperty("WarnShader"));
			cache = Boolean.parseBoolean(p.getProperty("UseCache"));
			
			boolean b[] = {warnExec, warnShader};
			loadWarns(b);

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
			}

			if(primaryGame)			radioGame.setSelection(true);
			else					radioLauncher.setSelection(true);

			if(refreshOnStartup) {
				buttonRefreshBoot.setSelection(true);
				buttonCache.setEnabled(false);
				buttonCache.setSelection(false);
			}
			else					buttonRefreshBoot.setSelection(false);

			if(minimize)			buttonMinimize.setSelection(true);
			else 					buttonMinimize.setSelection(false);
			
			if(shader)				buttonApplyShader.setSelection(true);
			else 					buttonApplyShader.setSelection(false);
			
			if(cache) 				buttonCache.setSelection(true);
			else 					buttonCache.setSelection(false);

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

			if(buttonRefreshBoot.getSelection()) 
				p.setProperty("RefreshOnStartup", "true");
			else p.setProperty("RefreshOnStartup", "false");

			if(buttonCache.getSelection()) 
				p.setProperty("UseCache", "true");
			else p.setProperty("UseCache", "false");
			
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

			if(buttonMinimize.getSelection())
				p.setProperty("Minimize", "true");
			else p.setProperty("Minimize", "false");
			
			if(buttonApplyShader.getSelection())
				p.setProperty("ApplyShaders", "true");
			else p.setProperty("ApplyShaders", "false");

			p.setProperty("IconSize", ""+slider.getSelection());
			p.setProperty("ModDir", modDir);
			p.setProperty("GameDir", gameDir);
			p.setProperty("WarnExec", ""+warnExec);
			p.setProperty("WarnShader", ""+warnShader);
			MainFrame.setModDirectory(modDir);
			MainFrame.setGameDirectory(gameDir);
			MainFrame.setWarnExec(warnExec);
			MainFrame.setWarnShader(warnShader);

			ConfigManager.writeConfig(p, prefPath);
			//Log.info("Printing settings file to: " + prefPath);
			
			if(!new File(portPath).exists()) {
				Properties p2 = new Properties();
				p2.setProperty("Port", "9999");
				ConfigManager.writeConfig(p2, prefPath);
				Log.info("Writing default port settings.");
			}
			
			shell.close();
			
			if(firstTime) {
				Start.checkConfigFolder(new File(gameDir + File.separator + "config" + File.separator + "modloader.cfg"));
				MainFrame frame = new MainFrame();
				frame.open();
				firstTime = false;
			}
		}

	}
	
	private static void loadWarns(boolean warning[]) 
	{
		WarningDialog.warnExec = warning[0];
		WarningDialog.warnShader = warning[1];
	}
	
	/**
	 * Sets the warning values from the Warning dialog.
	 */
	public static void setWarns(boolean[] warning) 
	{
		warnExec = warning[0];
		warnShader = warning[1];
	}
}
