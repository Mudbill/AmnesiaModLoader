package mudbill.modloader;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JFileChooser;
import javax.swing.JTabbedPane;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JCheckBox;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle.ComponentPlacement;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.util.Properties;

public class Preferences extends JFrame {

	private static final long serialVersionUID = 1L;
	
	public static String prefPath = CurrentOS.getSaveDir() + File.separator + "modloader.properties";
	private final String about = "Thanks for using this application. More information as well as updates can be found on the official thread over at https://www.frictionalgames.com/forum/thread-25806.html\n\nPlease be on the lookout for issues and report them to me so I can try fixing them. Suggestions for features are also nice.";
	
	private Properties settings = new Properties();
	private OutputStream output = null;
	private InputStream input = null;

	private MainFrame mainFrame = new MainFrame();
	private String version = mainFrame.getVersion();

	//Components
	private JTabbedPane tabbedPane;
	private JPanel contentPane;
	private JPanel tabSettings;
	private JPanel tabAbout;
	private JTextField inputModDir;
	private JTextField inputGameDir;
	private JTextField inputSteamDir;
	private JTextArea textInfo;
	private JCheckBox boxSteam;
	private JButton buttonBrowseMod;
	private JButton buttonBrowseGame;
	private JButton buttonOK;
	private JButton buttonCancel;
	private JButton buttonBrowseSteam;
	private JLabel labelGameDir;
	private JLabel lableModDir;
	private JLabel labelTitle;
	private JLabel labelVersion;
	private JLabel labelAuthor;
	private JLabel labelSteamDir;
	private JFileChooser chooser;
	
	private static boolean isOpen = false;
	
	public Preferences() {}
	
	/**
	 * Opens the Preferences window. 
	 */
	public void displayPrefWindow()
	{		
		if(isOpen == false) {
			System.out.println("Preferences opened.");

			this.setTitle("Preferences");
			this.setType(Type.POPUP);
			this.setResizable(false);
			this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			this.setSize(450, 322);
			this.setLocationRelativeTo(null);
			this.contentPane = new JPanel();
			this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
			this.setContentPane(contentPane);
			this.contentPane.setLayout(new BorderLayout(0, 0));
			this.setVisible(true);
			this.addWindowListener(new java.awt.event.WindowAdapter() {
			    @Override
			    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
			        isOpen = false;
			        System.out.println("Preferences cancelled.");
			    }
			});

			isOpen = true;
			setupPrefs();
		}
		else {
			System.out.println("Preferences already open.");
		}
	}
	
	/**
	 * Opens a file chooser to browse the file system for a path.
	 * @param title = Top bar text.
	 * @param button = The button calling.
	 */
	private void fileChooser(String title, int button)
	{		
		chooser = new JFileChooser(); 
		chooser.setPreferredSize(new Dimension(550, 400));
	    
	    if(button == 1) {
	    	try {
				chooser.setCurrentDirectory(new java.io.File(inputSteamDir.getText()));
			} catch (Exception e) {
				e.printStackTrace();
				chooser.setCurrentDirectory(new java.io.File("."));
			}
	    }
	    
	    if(button == 2) {
	    	try {
				chooser.setCurrentDirectory(new java.io.File(inputGameDir.getText()));
			} catch (Exception e) {
				e.printStackTrace();
				chooser.setCurrentDirectory(new java.io.File("."));
			}
	    }
	    
	    if(button == 3) {
	    	try {
				chooser.setCurrentDirectory(new java.io.File(inputModDir.getText()));
			} catch (Exception e) {
				e.printStackTrace();
				chooser.setCurrentDirectory(new java.io.File("."));
			}
	    }
	    
	    chooser.setDialogTitle(title);
	    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	    chooser.setAcceptAllFileFilterUsed(true);
	    
	    if (chooser.showOpenDialog(contentPane) == JFileChooser.APPROVE_OPTION) { 
	    	
	    	System.out.println("getSelectedFile() : " + chooser.getSelectedFile());
	    	if(button == 1) inputSteamDir.setText(chooser.getSelectedFile().toString());
	    	if(button == 2) inputGameDir.setText(chooser.getSelectedFile().toString());
	    	if(button == 3) inputModDir.setText(chooser.getSelectedFile().toString());
	    }
	    else {
	    	System.out.println("No Selection.");
	    }
	}
	
	/**
	 * Creates the Preferences window design and layout.
	 */
	private void setupPrefs()
	{
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setFocusable(false);
		contentPane.add(tabbedPane, BorderLayout.CENTER);
		
		tabSettings = new JPanel();
		tabSettings.setFocusable(false);
		tabSettings.setBackground(Color.WHITE);
		tabbedPane.addTab("Settings", null, tabSettings, null);
		
		inputModDir = new JTextField(mainFrame.getModDirectory());
		inputModDir.setToolTipText("The folder you wish to search for mods in. Do not select a huge directory, as it will take more time to load.");
		inputModDir.setColumns(10);
		inputGameDir = new JTextField(mainFrame.getGameDirectory());
		inputGameDir.setToolTipText("Your game redist installation directory.");
		inputGameDir.setColumns(10);

		labelGameDir = new JLabel("Game directory:");
		labelGameDir.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		lableModDir = new JLabel("Mod directory:");
		lableModDir.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		buttonBrowseSteam = new JButton("Browse");
		buttonBrowseSteam.setEnabled(false);
		buttonBrowseSteam.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				fileChooser("Browse for Steam directory", 1);
			}
		});
		buttonBrowseGame = new JButton("Browse");
		buttonBrowseGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				fileChooser("Browse for game directory", 2);
			}
		});
		buttonBrowseMod = new JButton("Browse");
		buttonBrowseMod.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				fileChooser("Browse for mod directory", 3);
			}
		});
		
		boxSteam = new JCheckBox("Use Steam");
		boxSteam.setFocusable(false);
		boxSteam.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if(boxSteam.isSelected()) {
					System.out.println("Steam checked, diabling retail dir.");
					inputGameDir.setEnabled(false); buttonBrowseGame.setEnabled(false); labelGameDir.setEnabled(false);
					inputSteamDir.setEnabled(true); buttonBrowseSteam.setEnabled(true); labelSteamDir.setEnabled(true);
				} else {
					System.out.println("Steam unchecked, enabling retail dir.");
					inputGameDir.setEnabled(true); buttonBrowseGame.setEnabled(true); labelGameDir.setEnabled(true);
					inputSteamDir.setEnabled(false); buttonBrowseSteam.setEnabled(false); labelSteamDir.setEnabled(false);
				}
			}
		});		
		
		labelSteamDir = new JLabel("Steam directory:");
		labelSteamDir.setEnabled(false);
		labelSteamDir.setFont(new Font("Tahoma", Font.PLAIN, 12));
				
		inputSteamDir = new JTextField();
		inputSteamDir.setToolTipText("Your Steam client installation directory.");
		inputSteamDir.setEnabled(false);
		inputSteamDir.setColumns(10);
		GroupLayout gl_tabSettings = new GroupLayout(tabSettings);
		gl_tabSettings.setHorizontalGroup(
			gl_tabSettings.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_tabSettings.createSequentialGroup()
					.addGroup(gl_tabSettings.createParallelGroup(Alignment.LEADING)
						.addGroup(Alignment.TRAILING, gl_tabSettings.createSequentialGroup()
							.addContainerGap()
							.addComponent(inputSteamDir, GroupLayout.DEFAULT_SIZE, 346, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(buttonBrowseSteam))
						.addGroup(Alignment.TRAILING, gl_tabSettings.createSequentialGroup()
							.addContainerGap()
							.addComponent(inputGameDir, GroupLayout.DEFAULT_SIZE, 346, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(buttonBrowseGame))
						.addGroup(Alignment.TRAILING, gl_tabSettings.createSequentialGroup()
							.addContainerGap()
							.addGroup(gl_tabSettings.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_tabSettings.createSequentialGroup()
									.addComponent(inputModDir, GroupLayout.DEFAULT_SIZE, 346, Short.MAX_VALUE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(buttonBrowseMod))
								.addComponent(lableModDir)))
						.addGroup(gl_tabSettings.createSequentialGroup()
							.addContainerGap()
							.addComponent(labelSteamDir))
						.addGroup(gl_tabSettings.createSequentialGroup()
							.addContainerGap()
							.addComponent(boxSteam))
						.addGroup(gl_tabSettings.createSequentialGroup()
							.addContainerGap()
							.addComponent(labelGameDir)))
					.addContainerGap())
		);
		gl_tabSettings.setVerticalGroup(
			gl_tabSettings.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_tabSettings.createSequentialGroup()
					.addGap(8)
					.addComponent(boxSteam)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(labelSteamDir)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_tabSettings.createParallelGroup(Alignment.BASELINE)
						.addComponent(buttonBrowseSteam)
						.addComponent(inputSteamDir, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(labelGameDir)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_tabSettings.createParallelGroup(Alignment.TRAILING)
						.addComponent(inputGameDir, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(buttonBrowseGame))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lableModDir)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_tabSettings.createParallelGroup(Alignment.BASELINE)
						.addComponent(buttonBrowseMod)
						.addComponent(inputModDir, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(34, Short.MAX_VALUE))
		);
		tabSettings.setLayout(gl_tabSettings);
		
		tabAbout = new JPanel();
		tabAbout.setBackground(Color.WHITE);
		tabbedPane.addTab("About", null, tabAbout, null);
		
		labelTitle = new JLabel("Amnesia Mod Loader");
		labelTitle.setFont(new Font("Tahoma", Font.PLAIN, 13));
		labelVersion = new JLabel("Version " + version);
		labelVersion.setFont(new Font("SansSerif", Font.PLAIN, 11));
		labelAuthor = new JLabel("Developed by Mudbill");
		labelAuthor.setFont(new Font("Tahoma", Font.PLAIN, 13));
		
		textInfo = new JTextArea();
		textInfo.setFocusable(false);
		textInfo.setEditable(false);
		textInfo.setFont(new Font("Tahoma", Font.PLAIN, 13));
		textInfo.setLineWrap(true);
		textInfo.setWrapStyleWord(true);
		textInfo.setText(about);
		
		GroupLayout gl_tabAbout = new GroupLayout(tabAbout);
		gl_tabAbout.setHorizontalGroup(
			gl_tabAbout.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_tabAbout.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_tabAbout.createParallelGroup(Alignment.LEADING)
						.addComponent(textInfo, GroupLayout.DEFAULT_SIZE, 409, Short.MAX_VALUE)
						.addGroup(Alignment.TRAILING, gl_tabAbout.createSequentialGroup()
							.addComponent(labelTitle)
							.addPreferredGap(ComponentPlacement.RELATED, 169, Short.MAX_VALUE)
							.addComponent(labelAuthor))
						.addComponent(labelVersion))
					.addContainerGap())
		);
		gl_tabAbout.setVerticalGroup(
			gl_tabAbout.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_tabAbout.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_tabAbout.createParallelGroup(Alignment.BASELINE)
						.addComponent(labelAuthor)
						.addComponent(labelTitle))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(labelVersion)
					.addGap(18)
					.addComponent(textInfo, GroupLayout.DEFAULT_SIZE, 158, Short.MAX_VALUE)
					.addContainerGap())
		);
		tabAbout.setLayout(gl_tabAbout);
		
		JPanel panelBottom = new JPanel();
		contentPane.add(panelBottom, BorderLayout.SOUTH);
		
		buttonOK = new JButton("OK");
		buttonOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {

				if(mainFrame.getModDirectory() != null) {
					boolean firstTime = false;
					if(!new File(prefPath).exists()) firstTime = true;
					
					String modDir = inputModDir.getText();
					String gameDir = inputGameDir.getText();
					String steamDir = inputSteamDir.getText();
									
					try {
						output = new FileOutputStream(prefPath);
				 
						if(boxSteam.isSelected()) {
							settings.setProperty("UseSteam", "true");
						}
						else settings.setProperty("UseSteam", "false");
						settings.setProperty("ModDir", modDir);
						settings.setProperty("GameDir", gameDir);
						settings.setProperty("SteamDir", steamDir);
						mainFrame.setModDirectory(modDir);
						mainFrame.setSteamDirectory(steamDir);
						mainFrame.setGameDirectory(gameDir);
						
						settings.store(output, null);
						System.out.println("Printing settings file to: " + prefPath);
						
						//mainFrame.refresh();
					} catch (IOException io) {
						io.printStackTrace();
					} finally {
						if (output != null) {
							try {
								output.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						isOpen = false;
						dispose();
					}
					if(firstTime) {
						MainFrame start = new MainFrame();
						start.checkMods();
						start.setupFrame();
						start.setVisible(true);
						firstTime = false;
					}

				}
			}
		});
		
		buttonCancel = new JButton("Cancel");
		buttonCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				System.out.println("Preferences cancelled.");
				isOpen = false;
				dispose();
			}
		});
		GroupLayout gl_panelBottom = new GroupLayout(panelBottom);
		gl_panelBottom.setHorizontalGroup(
			gl_panelBottom.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_panelBottom.createSequentialGroup()
					.addContainerGap(283, Short.MAX_VALUE)
					.addComponent(buttonOK, GroupLayout.PREFERRED_SIZE, 69, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(buttonCancel, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		gl_panelBottom.setVerticalGroup(
			gl_panelBottom.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_panelBottom.createSequentialGroup()
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addGroup(gl_panelBottom.createParallelGroup(Alignment.BASELINE)
						.addComponent(buttonCancel)
						.addComponent(buttonOK)))
		);
		panelBottom.setLayout(gl_panelBottom);
		
		try {
			boolean steam;
			String modDir;
			String gameDir;
			String steamDir;
			
			input = new FileInputStream(prefPath);
			settings.load(input);
			
			modDir = settings.getProperty("ModDir");
			gameDir = settings.getProperty("GameDir");
			steamDir = settings.getProperty("SteamDir");
			steam = Boolean.parseBoolean(settings.getProperty("UseSteam"));
			
			if(steam == true) {
				boxSteam.setSelected(true);
				inputGameDir.setEnabled(false); buttonBrowseGame.setEnabled(false); labelGameDir.setEnabled(false);
				inputSteamDir.setEnabled(true); buttonBrowseSteam.setEnabled(true); labelSteamDir.setEnabled(true);
			}
			else {
				boxSteam.setSelected(false);
				inputGameDir.setEnabled(true); buttonBrowseGame.setEnabled(true); labelGameDir.setEnabled(true);
				inputSteamDir.setEnabled(false); buttonBrowseSteam.setEnabled(false); labelSteamDir.setEnabled(false);
			}
			inputModDir.setText(modDir);
			inputGameDir.setText(gameDir);
			inputSteamDir.setText(steamDir);
			
		} catch (IOException ex) {
			System.out.println("modloader.properties not found. Will create upon OK.");
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
