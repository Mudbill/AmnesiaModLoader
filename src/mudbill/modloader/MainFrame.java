package mudbill.modloader;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;
		
	private final static String appVersion = "1.1";
	private final static String appName = "Amnesia Mod Loader";
	
	private static String modDirectory = "";
	private static String gameDirectory = "";
	private static String steamDirectory = "";

	private static Preferences prefWindow;
	private static ModList modList;
	private static Refresh refresh = new Refresh();
	private Properties settings = new Properties();
	private InputStream input = null;
	
	private static JList<Object> listMain;
	private File[] list;
	
	//Components
	private static JLabel modTitle;
	private static JLabel modAuthor;
	private static JLabel modMinCompat;
	private static JLabel modMaxCompat;
	private static JTextArea modDesc;
	private JPanelBackground contentPane;
	private JPanelBackground panelInfo;
	private JPanel panelList;
	private JPanel panelButtons;
	private JScrollPane scrollPane;
	private static JScrollPane scrollList;
	private JButton buttonRefresh;
	private JButton buttonLaunch;
	private JButton buttonQuit;
	private JButton buttonFolder;
	private JButton buttonPref;
	private JLabel labelMin;
	private JLabel labelMax;
	
	public MainFrame() {}
	
	public JScrollPane getScrollList()
	{
		return scrollList;
	}
	
	public String getVersion()
	{
		return MainFrame.appVersion;
	}
	
	public String getModDirectory()
	{
		return MainFrame.modDirectory;
	}
	
	public String getGameDirectory()
	{
		return MainFrame.gameDirectory;
	}
	
	public String getSteamDirectory()
	{
		return MainFrame.steamDirectory;
	}
	
	public void setModDirectory(String dir)
	{
		modDirectory = dir;
	}
	
	public void setSteamDirectory(String dir)
	{
		steamDirectory = dir;
	}
	
	public void setGameDirectory(String dir)
	{
		gameDirectory = dir;
	}
	
	
	public void findFile(String name,File file)
    {
        list = file.listFiles();
        if(list!=null)
        for (File fil : list)
        {
            if (fil.isDirectory())
            {
                findFile(name,fil);
            }
            else if (name.equalsIgnoreCase(fil.getName()))
            {
            	modList = new ModList(fil);
            }
        }
    }
	
	public void setList(JList<Object> list)
	{
		scrollList = new JScrollPane();
		scrollList.setAlignmentX(1.0f);
		scrollList.setViewportView(listMain);
		panelList.add(scrollList, BorderLayout.CENTER);
		scrollList.setViewportView(list);
	}
	
	public void checkMods()
	{
		try {
			if(modDirectory != null) {
				String cfgName = "main_init.cfg";
				findFile(cfgName, new File(modDirectory));
				System.out.println("Mods found: " + modList.getModsFound());
			}
		} catch (Exception e) {
			System.err.println("Failed checking for mods.");
		}
	}

	public void displayModInfo()
	{
		String title = "Untitled";
		String author = "By Anonymous";
		String desc = "No description.";
		String compatMin = "Undefined";
		String compatMax = "Undefined";
		
		try {
			if(modDirectory != null || modDirectory != "") {
				int index = Refresh.getListIndex();
				System.out.println("List Index = " + Refresh.getListIndex());

				title = modList.getModTitle(index);
				author = "By " + modList.getModAuthor(index);
				desc = modList.getModDesc(index);

				compatMin = modList.getModMinCompatibility(index);
				compatMax = modList.getModMaxCompatibility(index);

				if(compatMin == "0.0") compatMin = "Any";
				if(compatMax == "0.0") compatMax = "Any";
			}
		} catch (Exception e) {
			System.err.println("Failed displaying mod info.");
		}
		modTitle.setText(title);
		modAuthor.setText(author);
		modDesc.setText(desc);
		modMinCompat.setText(compatMin);
		modMaxCompat.setText(compatMax);
	}
	
	public void setupFrame()
	{
		contentPane = new JPanelBackground("/resources/launcher_bg.jpg");
		contentPane.setBackground(Color.BLACK);
		this.setContentPane(contentPane);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(700, 583);
		this.setTitle(appName);
		ImageIcon icon = new ImageIcon(MainFrame.class.getResource("/resources/icon_application.png"));
		this.setIconImage(icon.getImage());
		this.setLocationRelativeTo(null);
		this.setBackground(new Color(0, 0, 0));
		this.setResizable(false);
		this.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		        System.out.print("Exiting application.");
		    }
		});
		
		try {
			input = new FileInputStream(Preferences.prefPath);
			settings.load(input);
			
			String gameDir = settings.getProperty("GameDir");
			gameDirectory = gameDir;
			String steamDir = settings.getProperty("SteamDir");
			steamDirectory = steamDir;
			
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		panelInfo = new JPanelBackground("/resources/mm_bg.jpg");
		panelInfo.setBackground(Color.DARK_GRAY);
		
		panelList = new JPanel();
		panelList.setBackground(Color.DARK_GRAY);
		
		panelButtons = new JPanel();
		panelButtons.setOpaque(false);
		panelButtons.setBackground(Color.BLACK);
		
		buttonRefresh = new JButton("Refresh");
		buttonRefresh.setIcon(new ImageIcon(MainFrame.class.getResource("/resources/icon_refresh.png")));
		
		//Setup layout.
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(panelButtons, GroupLayout.DEFAULT_SIZE, 682, Short.MAX_VALUE)
						.addComponent(buttonRefresh)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(panelList, GroupLayout.PREFERRED_SIZE, 342, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(panelInfo, GroupLayout.PREFERRED_SIZE, 330, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(buttonRefresh)
					.addGap(190)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(panelList, GroupLayout.DEFAULT_SIZE, 269, Short.MAX_VALUE)
						.addComponent(panelInfo, GroupLayout.DEFAULT_SIZE, 269, Short.MAX_VALUE))
					.addGap(18)
					.addComponent(panelButtons, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		panelList.setLayout(new BorderLayout(0, 0));
		
		scrollList = new JScrollPane();
		scrollList.setAlignmentX(1.0f);
		scrollList.setViewportView(listMain);
		panelList.add(scrollList, BorderLayout.CENTER);
		
		refresh.createNewList(scrollList);
		
		modTitle = new JLabel("Untitled");
		modTitle.setForeground(Color.LIGHT_GRAY);
		modTitle.setHorizontalAlignment(SwingConstants.CENTER);
		modTitle.setFont(new Font("Verdana", Font.PLAIN, 20));
		
		modAuthor = new JLabel("Anonymous");
		modAuthor.setFont(new Font("SansSerif", Font.ITALIC, 14));
		modAuthor.setForeground(Color.LIGHT_GRAY);
		
		modMinCompat = new JLabel("Undefined");
		modMinCompat.setFont(new Font("SansSerif", Font.PLAIN, 12));
		modMinCompat.setForeground(Color.LIGHT_GRAY);
		
		scrollPane = new JScrollPane();
		
		modDesc = new JTextArea("No description.");
		scrollPane.setViewportView(modDesc);
		modDesc.setWrapStyleWord(true);
		modDesc.setFont(new Font("SansSerif", Font.PLAIN, 14));
		modDesc.setFocusable(false);
		modDesc.setLineWrap(true);
		modDesc.setEditable(false);
		modDesc.setBackground(Color.LIGHT_GRAY);
		
		labelMin = new JLabel("Minimum game version:");
		labelMin.setFont(new Font("SansSerif", Font.PLAIN, 12));
		labelMin.setForeground(Color.LIGHT_GRAY);
		
		labelMax = new JLabel("Maximum game version:");
		labelMax.setForeground(Color.LIGHT_GRAY);
		
		modMaxCompat = new JLabel("Undefined");
		modMaxCompat.setForeground(Color.LIGHT_GRAY);
		
		//Setup layout
		GroupLayout gl_panelInfo = new GroupLayout(panelInfo);
		gl_panelInfo.setHorizontalGroup(
			gl_panelInfo.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panelInfo.createSequentialGroup()
					.addGroup(gl_panelInfo.createParallelGroup(Alignment.TRAILING)
						.addGroup(Alignment.LEADING, gl_panelInfo.createSequentialGroup()
							.addGap(3)
							.addComponent(scrollPane))
						.addGroup(Alignment.LEADING, gl_panelInfo.createSequentialGroup()
							.addGap(12)
							.addComponent(modAuthor, GroupLayout.PREFERRED_SIZE, 312, GroupLayout.PREFERRED_SIZE)
							.addGap(0, 29, Short.MAX_VALUE))
						.addGroup(Alignment.LEADING, gl_panelInfo.createSequentialGroup()
							.addContainerGap()
							.addComponent(modTitle, GroupLayout.PREFERRED_SIZE, 318, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panelInfo.createSequentialGroup()
							.addContainerGap()
							.addGroup(gl_panelInfo.createParallelGroup(Alignment.LEADING)
								.addGroup(Alignment.TRAILING, gl_panelInfo.createSequentialGroup()
									.addComponent(labelMin)
									.addPreferredGap(ComponentPlacement.RELATED, 129, Short.MAX_VALUE)
									.addComponent(modMinCompat))
								.addGroup(gl_panelInfo.createSequentialGroup()
									.addComponent(labelMax)
									.addPreferredGap(ComponentPlacement.RELATED, 127, Short.MAX_VALUE)
									.addComponent(modMaxCompat)))))
					.addContainerGap())
		);
		gl_panelInfo.setVerticalGroup(
			gl_panelInfo.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelInfo.createSequentialGroup()
					.addComponent(modTitle)
					.addGap(10)
					.addComponent(modAuthor)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 157, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panelInfo.createParallelGroup(Alignment.LEADING)
						.addComponent(modMinCompat)
						.addComponent(labelMin))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panelInfo.createParallelGroup(Alignment.LEADING)
						.addComponent(labelMax)
						.addComponent(modMaxCompat))
					.addContainerGap(7, Short.MAX_VALUE))
		);
		panelInfo.setLayout(gl_panelInfo);
		
		buttonLaunch = new JButton("Launch mod");
		buttonLaunch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				try {
					String filePath;
					filePath = modList.getLaunchIndex(Refresh.getListIndex());
					System.out.println("Launch mod: " + filePath);
					
					input = new FileInputStream(Preferences.prefPath);
					settings.load(input);
					
					if(Boolean.parseBoolean(settings.getProperty("UseSteam")) == true) {
						try {
							Runtime runTime = Runtime.getRuntime();
							runTime.exec(steamDirectory + File.separator + CurrentOS.getSteamExe() + " -applaunch 57300 " + filePath);
							System.out.println("Running command: " + steamDirectory + File.separator + CurrentOS.getSteamExe() + " -applaunch 57300 " + filePath);
						} catch (IOException e) {
							System.err.println("Could not find Steam.exe in directory: " + steamDirectory);
							JOptionPane.showMessageDialog(null, CurrentOS.getSteamExe() + " was not found in directory:\n" + steamDirectory + "\n\nPlease check the preferences to make sure the folder is set correctly.", "Steam not found", JOptionPane.ERROR_MESSAGE);
						}
					}
					else {
						try {
							Runtime runTime = Runtime.getRuntime();
							runTime.exec(gameDirectory + File.separator + CurrentOS.getGameExe() + " " + filePath);
							System.out.println("Running command: " + gameDirectory + File.separator + CurrentOS.getGameExe() + " " + filePath);
						} catch (IOException e) {
							System.err.println("Could not find Launcher.exe in directory: " + gameDirectory);
							JOptionPane.showMessageDialog(null, CurrentOS.getGameExe() + " was not found in directory:\n" + gameDirectory + "\n\nPlease check the preferences to make sure the folder is set correctly.", "Game file not found", JOptionPane.ERROR_MESSAGE);
						}
					}
					
					
				} catch (NullPointerException e) {
					System.err.println("No selected index!");
					JOptionPane.showMessageDialog(null, "No mod selected to launch.", "No selection.", JOptionPane.ERROR_MESSAGE);
					e.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		buttonQuit = new JButton("Quit");
		buttonQuit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				System.out.println("Exiting application.");
				System.exit(EXIT_ON_CLOSE);
			}
		});
		buttonFolder = new JButton("Open mods folder");
		buttonFolder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				try {
					System.out.println("Opening mods folder at: " + modDirectory);
					Desktop.getDesktop().open(new File(modDirectory));
				} catch (IllegalArgumentException e) {
					System.err.println("Could not open folder destination.");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		buttonPref = new JButton("Preferences");
		buttonPref.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				prefWindow = new Preferences();
				prefWindow.displayPrefWindow();
			}
		});
		
		GroupLayout gl_panelButtons = new GroupLayout(panelButtons);
		gl_panelButtons.setHorizontalGroup(
			gl_panelButtons.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelButtons.createSequentialGroup()
					.addComponent(buttonFolder)
					.addGap(113)
					.addComponent(buttonLaunch, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(buttonQuit, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 139, Short.MAX_VALUE)
					.addComponent(buttonPref)
					.addContainerGap())
		);
		gl_panelButtons.setVerticalGroup(
			gl_panelButtons.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panelButtons.createSequentialGroup()
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addGroup(gl_panelButtons.createParallelGroup(Alignment.BASELINE)
						.addComponent(buttonQuit)
						.addComponent(buttonLaunch)
						.addComponent(buttonPref)
						.addComponent(buttonFolder))
					.addGap(4))
		);
		panelButtons.setLayout(gl_panelButtons);
		contentPane.setLayout(gl_contentPane);
		
		buttonRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				refresh = new Refresh();
				refresh.displayRefreshWindow();
				EventQueue.invokeLater(new Runnable() {
					@Override
					public void run() {
						refresh.refreshList();
					}
				});
			}
		});
	}
}
