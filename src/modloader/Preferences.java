package modloader;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

public class Preferences extends Dialog {

	public static String prefPath = CurrentOS.getSaveDir() + File.separator + CurrentOS.getConfigName();
	private static final String url = "https://www.frictionalgames.com/forum/thread-25806.html";
	
	private Object result;
	private Shell shell;
	private Text textGameDir, textModDir, textAbout;
	private TabFolder tabFolder;
	private TabItem tabGeneral, tabAbout;
	private Composite panelGeneral, panelAbout;
	private Label labelGameDir, labelModDir, labelName, labelVer, labelAuthor;
	private Button buttonBrowseGame, radioUseSame, radioUseCustom, buttonBrowseMod, buttonCancel, buttonOK, buttonRefreshBoot, buttonForum, radioLauncher, radioGame, buttonMinimize;
	private Group groupMisc, groupDir, groupIcon;
	private Label iconPreview, labelSize;
	private Scale slider;
	private Label labelPrimary;
		
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
		shell.setSize(560, 460);
		shell.setText("Preferences");
		shell.setLayout(new FormLayout());
		
		tabFolder = new TabFolder(shell, SWT.NONE);
		tabGeneral = new TabItem(tabFolder, SWT.NONE);
		tabGeneral.setText("General");
		
		panelGeneral = new Composite(tabFolder, SWT.NONE);
		panelGeneral.setLayout(new FormLayout());
		tabGeneral.setControl(panelGeneral);
		
		tabAbout = new TabItem(tabFolder, SWT.NONE);
		tabAbout.setText("About");
		
		panelAbout = new Composite(tabFolder, SWT.NONE);
		tabAbout.setControl(panelAbout);
		panelAbout.setLayout(new FormLayout());
		
		labelName = new Label(panelAbout, SWT.NONE);
		labelName.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
		labelName.setText("Amnesia Modloader");
		
		labelVer = new Label(panelAbout, SWT.NONE);
		labelVer.setFont(SWTResourceManager.getFont("Segoe UI", 8, SWT.NORMAL));
		labelVer.setText("Version " + MainFrame.getVersion());
		
		labelAuthor = new Label(panelAbout, SWT.NONE);
		labelAuthor.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
		labelAuthor.setAlignment(SWT.RIGHT);
		labelAuthor.setText("Developed by Mudbill");
		
		textAbout = new Text(panelAbout, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		textAbout.setEditable(false);
		textAbout.setText("Thanks for using this application. More information as well as updates can be found on the official thread over at the Frictional Games forum. Click the button below to open the page.\r\n\r\n--- How to use ---\r\n\r\nWhen you open the program for the first time, you will be presented with the preferences. \r\nUse the Game Directory browser to find your game location. You can also specify a mod directory and a few other settings as you like. \r\n\r\nThe default game directory depends on whether you have a Steam copy or a retail copy.\r\n\r\n\tSteam:\r\n32-bit\tC:\\Program Files\\Steam\\SteamApps\\common\\Amnesia The Dark Descent\r\n64-bit\tC:\\Program Files (x86)\\Steam\\SteamApps\\common\\Amnesia The Dark Descent\r\n\r\n\tRetail:\r\n32-bit\tC:\\Program Files\\Amnesia The Dark Descent\\redist\r\n64-bit\tC:\\Program Files (x86)\\Amnesia The Dark Descent\\redist\r\n\r\nOnce you accept the preferences, your settings will be saved in your Amnesia save directory/ModLoader. \r\n\r\nThe main window will open, and will open first the next time you start it. \r\nIf the list is empty, click Refresh in the top left corner to update the list depending on your mod directory. This might take some time depending on the size of your folder and the speed of your computer.\r\n\r\nWhen the list of mods is available, you may select any you wish to play and click Launch Mod at the bottom. Alternatively, you can hit the arrow on the button to specify whether you want to start the launcher or the game directly. \r\n\r\nAfter selecting a mod, some information will be displayed on the right side. This includes the name of the mod, as well as extra information if available. A mod creator can add a modloader.cfg file to their /config folder to specify entries to display. These include author, description, minimum version and custom shaders. It can also contain a custom icon which will show up in the list to the left. \r\n\r\n--- For creators ---\r\n\r\nIf you want to fully support this modloader (which I'd be very happy about) you can add an extra config file to your mod's /config folder. Make a file and name it modloader.cfg, then add these lines and specify them to your liking:\r\n_______\r\n\r\nAuthor = Your Name\r\nIconFile = icon.png\r\nDescription = Your description.\r\nCustomShaders = false\r\nMinVersion = 1.2\r\n_______\r\n\r\nIf you want a custom icon as well, add it next to your modloader.cfg file (also in /config). The suggested resolution is 64x64. \r\n\r\n--- Credits\r\n\r\nMudbill (ME :D)\t- Primary development.\r\nAmn/Daemian\t- Extra development and custom shader support.\r\n\r\nTraggey\t\t- Background and icon artwork.\r\n\r\nMrBehemoth\t- Initial beta testing.\r\nKreekakon\t\t- Beta testing.\r\nLazzer\t\t\t- Beta testing.\r\n\r\n---\r\n\r\nPlease be on the lookout for issues and report them to me so I can try fixing them. Suggestions for features are also nice.");
		
		buttonForum = new Button(panelAbout, SWT.NONE);
		buttonForum.setToolTipText(url);
		buttonForum.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		buttonForum.setText("Open forum post");
		buttonForum.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				try {
					Common.openWebpage(new URL(url));
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
			}
		});
		
		buttonCancel = new Button(shell, SWT.NONE);
		buttonCancel.setText("Cancel");
		buttonCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				Log.info("Preferences cancelled.");
				//isOpen = false;
				shell.close();
			}
		});
		
		buttonOK = new Button(shell, SWT.NONE);
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

					p.setProperty("IconSize", ""+slider.getSelection());
					p.setProperty("ModDir", modDir);
					p.setProperty("GameDir", gameDir);
					MainFrame.setModDirectory(modDir);
					MainFrame.setGameDirectory(gameDir);

					ConfigManager.writeConfig(p, prefPath);
					Log.info("Printing settings file to: " + prefPath);

					shell.close();
					
					if(firstTime) {
						MainFrame frame = new MainFrame();
						frame.open();
						firstTime = false;
					}
				}
			}
		});
		
		groupDir = new Group(panelGeneral, SWT.NONE);
		groupDir.setText("Directories");
		groupDir.setLayout(new FormLayout());

		labelGameDir = new Label(groupDir, SWT.NONE);
		labelGameDir.setText("Game directory:");
		
		textGameDir = new Text(groupDir, SWT.BORDER);
		textGameDir.setToolTipText("Your game redist installation directory.");

		buttonBrowseGame = new Button(groupDir, SWT.NONE);
		buttonBrowseGame.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		buttonBrowseGame.setText("Browse");
		
		labelModDir = new Label(groupDir, SWT.NONE);
		labelModDir.setText("Mod directory:");
		
		radioUseSame = new Button(groupDir, SWT.RADIO);
		radioUseSame.setText("Use same as game");
		
		radioUseCustom = new Button(groupDir, SWT.RADIO);
		radioUseCustom.setText("Use custom:");

		buttonBrowseMod = new Button(groupDir, SWT.NONE);
		buttonBrowseMod.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		buttonBrowseMod.setEnabled(false);
		buttonBrowseMod.setText("Browse");
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
		textModDir.setEnabled(false);
		textModDir.setToolTipText("The folder you wish to search for mods in. Selecting a larger directory will increase the load time!");

		groupMisc = new Group(panelGeneral, SWT.NONE);
		groupMisc.setText("Miscellaneous");
		groupMisc.setLayout(new FormLayout());
		
		buttonRefreshBoot = new Button(groupMisc, SWT.CHECK);
		buttonRefreshBoot.setSelection(true);
		buttonRefreshBoot.setText("Update list on startup");
		buttonRefreshBoot.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				
			}
		});

		groupIcon = new Group(panelGeneral, SWT.NONE);
		groupIcon.setText("Icon size");
		groupIcon.setLayout(null);
		
		iconPreview = new Label(groupIcon, SWT.NONE);
		iconPreview.setBounds(61, 25, 64, 64);
		if(ConfigManager.loadConfig(prefPath) != null) {
			iconPreview.setImage(Common.scaleImage(SWTResourceManager.getImage(Preferences.class, "/resources/icon_default.png"), MainFrame.getIconSize()));			
		} else iconPreview.setImage(SWTResourceManager.getImage(Preferences.class, "/resources/icon_default.png"));

		labelSize = new Label(groupIcon, SWT.CENTER);
		labelSize.setBounds(13, 95, 42, 21);
		labelSize.setFont(SWTResourceManager.getFont("GillSans", 14, SWT.NORMAL));
		labelSize.setText(MainFrame.getIconSize() + "x");
		
		slider = new Scale(groupIcon, SWT.VERTICAL);
		slider.setBounds(13, 25, 42, 64);
		slider.setToolTipText("Drag to select the size you want icons to show up with.");
		slider.setPageIncrement(1);
		slider.setMaximum(3);
		slider.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				Log.info("Changed icon slider position to " + slider.getSelection());
				
				if(slider.getSelection() == 0) {
					labelSize.setText("64x");
					iconPreview.setImage(Common.scaleImage(SWTResourceManager.getImage(Preferences.class, "/resources/icon_default.png"), 64));
				}
				if(slider.getSelection() == 1) {
					labelSize.setText("48x");
					iconPreview.setImage(Common.scaleImage(SWTResourceManager.getImage(Preferences.class, "/resources/icon_default.png"), 48));
				}
				if(slider.getSelection() == 2) {
					labelSize.setText("32x");
					iconPreview.setImage(Common.scaleImage(SWTResourceManager.getImage(Preferences.class, "/resources/icon_default.png"), 32));
				}
				if(slider.getSelection() == 3) {
					labelSize.setText("16x");
					iconPreview.setImage(Common.scaleImage(SWTResourceManager.getImage(Preferences.class, "/resources/icon_default.png"), 16));
				}
			}
		});
		
		labelPrimary = new Label(groupMisc, SWT.NONE);
		labelPrimary.setText("Primary launch option:");
		radioLauncher = new Button(groupMisc, SWT.RADIO);
		radioLauncher.setText("Launcher");
		radioGame = new Button(groupMisc, SWT.RADIO);
		radioGame.setText("Direct game");
		buttonMinimize = new Button(groupMisc, SWT.CHECK);
		buttonMinimize.setText("Minimize Modloader on mod start");

		//GUI Design
		FormData fd_buttonRefreshBoot = new FormData();
		fd_buttonRefreshBoot.top = new FormAttachment(0, 9);
		fd_buttonRefreshBoot.left = new FormAttachment(0, 10);
		buttonRefreshBoot.setLayoutData(fd_buttonRefreshBoot);
		FormData fd_labelVer = new FormData();
		fd_labelVer.top = new FormAttachment(labelName, 6);
		fd_labelVer.right = new FormAttachment(labelName, -10, SWT.RIGHT);
		fd_labelVer.left = new FormAttachment(0, 20);
		fd_labelVer.bottom = new FormAttachment(0, 46);
		labelVer.setLayoutData(fd_labelVer);
		FormData fd_labelName = new FormData();
		fd_labelName.right = new FormAttachment(0, 182);
		fd_labelName.top = new FormAttachment(0, 10);
		fd_labelName.left = new FormAttachment(0, 10);
		labelName.setLayoutData(fd_labelName);
		FormData fd_labelAuthor = new FormData();
		fd_labelAuthor.top = new FormAttachment(labelName, 0, SWT.TOP);
		fd_labelAuthor.right = new FormAttachment(100, -10);
		fd_labelAuthor.left = new FormAttachment(0, 328);
		labelAuthor.setLayoutData(fd_labelAuthor);
		FormData fd_textAbout = new FormData();
		fd_textAbout.bottom = new FormAttachment(buttonForum, -6);
		fd_textAbout.top = new FormAttachment(labelVer, 6);
		fd_textAbout.left = new FormAttachment(0, 10);
		fd_textAbout.right = new FormAttachment(100, -10);
		textAbout.setLayoutData(fd_textAbout);
		FormData fd_buttonForum = new FormData();
		fd_buttonForum.right = new FormAttachment(labelAuthor, 0, SWT.RIGHT);
		fd_buttonForum.top = new FormAttachment(0, 285);
		fd_buttonForum.bottom = new FormAttachment(100, -10);
		fd_buttonForum.left = new FormAttachment(0, 10);
		buttonForum.setLayoutData(fd_buttonForum);
		FormData fd_groupDir = new FormData();
		fd_groupDir.top = new FormAttachment(0, 10);
		fd_groupDir.left = new FormAttachment(0, 10);
		fd_groupDir.bottom = new FormAttachment(0, 187);
		fd_groupDir.right = new FormAttachment(0, 516);
		groupDir.setLayoutData(fd_groupDir);
		FormData fd_labelGameDir = new FormData();
		fd_labelGameDir.bottom = new FormAttachment(textGameDir, -6);
		fd_labelGameDir.left = new FormAttachment(textGameDir, 0, SWT.LEFT);
		labelGameDir.setLayoutData(fd_labelGameDir);
		FormData fd_textGameDir = new FormData();
		fd_textGameDir.right = new FormAttachment(0, 403);
		fd_textGameDir.top = new FormAttachment(0, 31);
		fd_textGameDir.left = new FormAttachment(0, 10);
		textGameDir.setLayoutData(fd_textGameDir);
		FormData fd_buttonBrowseGame = new FormData();
		fd_buttonBrowseGame.left = new FormAttachment(0, 416);
		fd_buttonBrowseGame.right = new FormAttachment(0, 490);
		fd_buttonBrowseGame.top = new FormAttachment(0, 30);
		buttonBrowseGame.setLayoutData(fd_buttonBrowseGame);
		FormData fd_labelModDir = new FormData();
		fd_labelModDir.top = new FormAttachment(0, 58);
		fd_labelModDir.left = new FormAttachment(0, 10);
		labelModDir.setLayoutData(fd_labelModDir);
		FormData fd_radioUseSame = new FormData();
		fd_radioUseSame.right = new FormAttachment(0, 158);
		fd_radioUseSame.top = new FormAttachment(0, 79);
		fd_radioUseSame.left = new FormAttachment(0, 20);
		radioUseSame.setLayoutData(fd_radioUseSame);
		FormData fd_radioUseCustom = new FormData();
		fd_radioUseCustom.right = new FormAttachment(0, 158);
		fd_radioUseCustom.top = new FormAttachment(0, 101);
		fd_radioUseCustom.left = new FormAttachment(0, 20);
		radioUseCustom.setLayoutData(fd_radioUseCustom);
		FormData fd_buttonBrowseMod = new FormData();
		fd_buttonBrowseMod.top = new FormAttachment(0, 122);
		fd_buttonBrowseMod.left = new FormAttachment(0, 416);
		fd_buttonBrowseMod.bottom = new FormAttachment(0, 145);
		fd_buttonBrowseMod.right = new FormAttachment(0, 490);
		buttonBrowseMod.setLayoutData(fd_buttonBrowseMod);
		FormData fd_textModDir = new FormData();
		fd_textModDir.top = new FormAttachment(radioUseCustom, 6);
		fd_textModDir.left = new FormAttachment(labelGameDir, 0, SWT.LEFT);
		fd_textModDir.bottom = new FormAttachment(0, 146);
		fd_textModDir.right = new FormAttachment(0, 403);
		textModDir.setLayoutData(fd_textModDir);
		FormData fd_groupMisc = new FormData();
		fd_groupMisc.left = new FormAttachment(0, 10);
		fd_groupMisc.right = new FormAttachment(groupIcon, -6);
		fd_groupMisc.top = new FormAttachment(groupDir, 6);
		fd_groupMisc.bottom = new FormAttachment(100, -10);
		groupMisc.setLayoutData(fd_groupMisc);
		FormData fd_labelPrimary = new FormData();
		fd_labelPrimary.left = new FormAttachment(buttonRefreshBoot, 0, SWT.LEFT);
		fd_labelPrimary.top = new FormAttachment(buttonMinimize, 6);
		labelPrimary.setLayoutData(fd_labelPrimary);
		FormData fd_radioLauncher = new FormData();
		fd_radioLauncher.top = new FormAttachment(labelPrimary, 6);
		fd_radioLauncher.left = new FormAttachment(labelPrimary, 10, SWT.LEFT);
		radioLauncher.setLayoutData(fd_radioLauncher);
		FormData fd_radioGame = new FormData();
		fd_radioGame.top = new FormAttachment(radioLauncher, 6);
		fd_radioGame.left = new FormAttachment(0, 20);
		radioGame.setLayoutData(fd_radioGame);
		FormData fd_buttonMinimize = new FormData();
		fd_buttonMinimize.top = new FormAttachment(buttonRefreshBoot, 6);
		fd_buttonMinimize.left = new FormAttachment(buttonRefreshBoot, 0, SWT.LEFT);
		buttonMinimize.setLayoutData(fd_buttonMinimize);
		FormData fd_groupIcon = new FormData();
		fd_groupIcon.bottom = new FormAttachment(groupMisc, 0, SWT.BOTTOM);
		fd_groupIcon.left = new FormAttachment(0, 374);
		fd_groupIcon.right = new FormAttachment(100, -10);
		fd_groupIcon.top = new FormAttachment(groupDir, 6);
		groupIcon.setLayoutData(fd_groupIcon);
		FormData fd_tabFolder = new FormData();
		fd_tabFolder.bottom = new FormAttachment(buttonCancel, -6);
		fd_tabFolder.right = new FormAttachment(buttonCancel, 0, SWT.RIGHT);
		fd_tabFolder.top = new FormAttachment(0, 10);
		fd_tabFolder.left = new FormAttachment(0, 10);
		tabFolder.setLayoutData(fd_tabFolder);
		FormData fd_buttonCancel = new FormData();
		fd_buttonCancel.bottom = new FormAttachment(100, -10);
		fd_buttonCancel.right = new FormAttachment(100, -10);
		fd_buttonCancel.left = new FormAttachment(100, -90);
		buttonCancel.setLayoutData(fd_buttonCancel);
		FormData fd_buttonOK = new FormData();
		fd_buttonOK.top = new FormAttachment(buttonCancel, 0, SWT.TOP);
		fd_buttonOK.right = new FormAttachment(buttonCancel, -6);
		fd_buttonOK.left = new FormAttachment(0, 378);
		buttonOK.setLayoutData(fd_buttonOK);
		
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
			int sliderVal;
			String modDir;
			String gameDir;

			modDir = p.getProperty("ModDir");
			gameDir = p.getProperty("GameDir");
			useSameDir = Boolean.parseBoolean(p.getProperty("UseSameDir"));
			refreshOnStartup = Boolean.parseBoolean(p.getProperty("RefreshOnStartup"));
			minimize = Boolean.parseBoolean(p.getProperty("Minimize"));
			primaryGame = Boolean.parseBoolean(p.getProperty("PrimaryGame"));
			sliderVal = Integer.parseInt(p.getProperty("IconSize"));

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

			textGameDir.setText(gameDir);
			textModDir.setText(modDir);
			slider.setSelection(sliderVal);
		}
	}
}
