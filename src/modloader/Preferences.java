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
	private Button buttonBrowseGame, radioUseSame, radioUseCustom, buttonBrowseMod, buttonCancel, buttonOK, buttonRefreshBoot, buttonWeb, radioLauncher, radioGame, buttonMinimize, buttonApplyShader, buttonYoutube, buttonTwitter, buttonForum, buttonWarnings;
	private Text textGameDir, textModDir, textAbout;
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
		shell.setSize(560, 469);
		shell.setText("Preferences");
		shell.setLayout(null);
		
		tabFolder = new TabFolder(shell, SWT.NONE);
		tabFolder.setBounds(10, 10, 534, 391);
		
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
		
		textAbout = new Text(panelAbout, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		textAbout.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		textAbout.setBounds(10, 52, 506, 210);
		textAbout.setEditable(false);
		textAbout.setText("Thanks for using this application. More information as well as updates can be found on the official thread over at my website. Click the button below to open the page.\r\n\r\n--- How to use ---\r\n\r\nWhen you open the program for the first time, you will be presented with the preferences. \r\nUse the Game Directory browser to find your game location. You can also specify a mod directory and a few other settings as you like. \r\n\r\nThe default game directory depends on whether you have a Steam copy or a retail copy.\r\n\r\n\tSteam:\r\n32-bit\tC:\\Program Files\\Steam\\SteamApps\\common\\Amnesia The Dark Descent\r\n64-bit\tC:\\Program Files (x86)\\Steam\\SteamApps\\common\\Amnesia The Dark Descent\r\n\r\n\tRetail:\r\n32-bit\tC:\\Program Files\\Amnesia The Dark Descent\\redist\r\n64-bit\tC:\\Program Files (x86)\\Amnesia The Dark Descent\\redist\r\n\r\nOnce you accept the preferences, your settings will be saved in your Amnesia save directory/ModLoader. \r\n\r\nThe main window will open, and will open first the next time you start it. \r\nIf the list is empty, click Refresh in the top left corner to update the list depending on your mod directory. This might take some time depending on the size of your folder and the speed of your computer.\r\n\r\nWhen the list of mods is available, you may select any you wish to play and click Launch Mod at the bottom. Alternatively, you can hit the arrow on the button to specify whether you want to start the launcher or the game directly. \r\n\r\nAfter selecting a mod, some information will be displayed on the right side. This includes the name of the mod, as well as extra information if available. A mod creator can add a modloader.cfg file to their /config folder to specify entries to display. These include author, description, minimum version and custom shaders. It can also contain a custom icon which will show up in the list to the left. \r\n\r\n--- For creators ---\r\n\r\nIf you want to fully support this modloader (which I'd be very happy about) you can add an extra config file to your mod's /config folder. Instructions on how to do so are at the website. Click the button below.\r\n\r\n--- Credits ---\r\n\r\nMudbill (ME :D)\t\t- Primary development.\r\nAmn/Daemian\t\t- Extra development and custom shader support.\r\nTraggey\t\t\t- Background and icon artwork.\r\nMrBehemoth\t\t- Initial beta testing.\r\nKreekakon\t\t\t- Beta testing.\r\nLazzer\t\t\t\t- Beta testing.\r\n\r\n---\r\n\r\nPlease be on the lookout for issues and report them to me so I can try fixing them. Suggestions for features are also nice.");
		
		buttonWeb = new Button(panelAbout, SWT.NONE);
		buttonWeb.setBounds(10, 268, 250, 55);
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
		buttonForum.setBounds(266, 268, 250, 55);
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
		buttonTwitter.setBounds(458, 329, 26, 26);
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
		buttonYoutube.setBounds(490, 329, 26, 26);
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
		labelPromo.setBounds(10, 336, 172, 13);
		labelPromo.setText("You can find me here:");
		
		labelEmail = new Label(panelAbout, SWT.NONE);
		labelEmail.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.ITALIC));
		labelEmail.setAlignment(SWT.RIGHT);
		labelEmail.setBounds(300, 333, 152, 17);
		labelEmail.setText("mudbill@buttology.net");
		
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
		
		textModDir = new Text(groupDir, SWT.BORDER);
		textModDir.setBounds(13, 136, 393, 23);
		textModDir.setEnabled(false);
		textModDir.setToolTipText("The folder you wish to search for mods in. Selecting a larger directory will increase the load time!");

		groupMisc = new Group(panelGeneral, SWT.NONE);
		groupMisc.setBounds(10, 193, 320, 162);
		groupMisc.setText("Miscellaneous");
		groupMisc.setLayout(null);
		
		buttonRefreshBoot = new Button(groupMisc, SWT.CHECK);
		buttonRefreshBoot.setToolTipText("Whether the Modloader should automatically refresh the mod list when starting.");
		buttonRefreshBoot.setBounds(13, 24, 134, 16);
		buttonRefreshBoot.setSelection(true);
		buttonRefreshBoot.setText("Update list on startup");

		groupIcon = new Group(panelGeneral, SWT.NONE);
		groupIcon.setBounds(336, 193, 180, 162);
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
		
		labelPrimary = new Label(groupMisc, SWT.NONE);
		labelPrimary.setBounds(13, 90, 134, 15);
		labelPrimary.setText("Primary launch option:");
		radioLauncher = new Button(groupMisc, SWT.RADIO);
		radioLauncher.setBounds(23, 111, 70, 16);
		radioLauncher.setText("Launcher");
		radioGame = new Button(groupMisc, SWT.RADIO);
		radioGame.setBounds(23, 133, 85, 16);
		radioGame.setText("Direct game");
		buttonMinimize = new Button(groupMisc, SWT.CHECK);
		buttonMinimize.setToolTipText("Whether the Modloader should minimize after starting a mod.");
		buttonMinimize.setBounds(13, 46, 202, 16);
		buttonMinimize.setText("Minimize Modloader on mod start");
		buttonApplyShader = new Button(groupMisc, SWT.CHECK);
		buttonApplyShader.setToolTipText("Whether the Modloader should automatically install custom shaders when starting a mod. It will also uninstall the shaders when done.");
		buttonApplyShader.setBounds(13, 68, 273, 16);
		buttonApplyShader.setText("Apply custom shaders upon launch (if available)");
		buttonApplyShader.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if(buttonApplyShader.getSelection()) {
					MessageBox m = new MessageBox(shell, SWT.SHEET | SWT.OK | SWT.ICON_WARNING);
					m.setMessage("WARNING: This is currently an experimental feature. This means that, although everything should work fine, I have not tested it enough to confirm this. It will modify some files in your Amnesia directory. Although it creates a backup, I recommend you create one yourself in case of malfunction. The folder that is modified is named \"shaders\" and the backup name will be \"shaders_backup\". Other files are left alone.\n\nIf you find an issue, please report it to me so I can fix it, but keep in mind that you're on your own if things go wrong.");
					m.setText("Beta Feature");
					m.open();
				}
			}
		});
		
		buttonWarnings = new Button(groupMisc, SWT.NONE);
		buttonWarnings.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		buttonWarnings.setBounds(236, 130, 74, 23);
		buttonWarnings.setText("Warnings");
		buttonWarnings.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				WarningDialog w = new WarningDialog(shell, SWT.SHEET);
				w.open();
			}
		});
		
		buttonOK = new Button(shell, SWT.NONE);
		buttonOK.setBounds(378, 407, 80, 23);
		buttonOK.setText("OK");
		shell.setDefaultButton(buttonOK);
		buttonOK.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if(MainFrame.getModDirectory() != null) {
					boolean firstTime = false;
					if(!new File(prefPath).exists()) firstTime = true;
					
					String modDir = textModDir.getText();
					String gameDir = textGameDir.getText();

					Properties p = new Properties();

					if(buttonRefreshBoot.getSelection()) {
						p.setProperty("RefreshOnStartup", "true");
					} else p.setProperty("RefreshOnStartup", "false");

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

					if(buttonMinimize.getSelection()) {
						p.setProperty("Minimize", "true");
					} else p.setProperty("Minimize", "false");
					
					if(buttonApplyShader.getSelection()) {
						p.setProperty("ApplyShaders", "true");
					} else p.setProperty("ApplyShaders", "false");

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
					Log.info("Printing settings file to: " + prefPath);
					
					if(!new File(portPath).exists()) {
						Properties p2 = new Properties();
						p2.setProperty("Port", "9999");
						ConfigManager.writeConfig(p2, prefPath);
						Log.info("Writing default port settings.");
					}
					
					shell.close();
					
					if(firstTime) {
						MainFrame frame = new MainFrame();
						frame.open();
						firstTime = false;
					}
				}
			}
		});
		
		buttonCancel = new Button(shell, SWT.NONE);
		buttonCancel.setBounds(464, 407, 80, 23);
		buttonCancel.setText("Cancel");
		buttonCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				Log.info("Preferences cancelled.");
				//isOpen = false;
				shell.close();
			}
		});
		
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
			
			boolean b[] = {warnExec, warnShader};
			loadWarns(b);

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

			if(refreshOnStartup)	buttonRefreshBoot.setSelection(true);
			else					buttonRefreshBoot.setSelection(false);

			if(minimize)			buttonMinimize.setSelection(true);
			else 					buttonMinimize.setSelection(false);
			
			if(shader)				buttonApplyShader.setSelection(true);
			else 					buttonApplyShader.setSelection(false);

			textGameDir.setText(gameDir);
			textModDir.setText(modDir);
			slider.setSelection(sliderVal);
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
