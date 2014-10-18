package mudbill.modloader;

import java.awt.Font;
import java.awt.SystemColor;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class Refresh extends JDialog {
	
	private static final long serialVersionUID = 1L;
	
	private MainFrame main = new MainFrame();
	private ModList modList = new ModList();
	private Properties settings = new Properties();
	private InputStream input = null;
	private static JList<Object> listMain = new JList<Object>();

	public Refresh() {}
	
	/**
	 * Displays the refresh window.
	 */
	public void displayRefreshWindow()
	{
		setResizable(false);
		setTitle("Refreshing");
		setSize(315, 130);
		setLocationRelativeTo(null);
		setVisible(true);

		JLabel labelUpdate = new JLabel("Updating mod list. Please wait.");
		labelUpdate.setIcon(new ImageIcon(Refresh.class.getResource("/resources/icon_refresh.png")));
		labelUpdate.setFont(new Font("Tahoma", Font.PLAIN, 13));
		
		JLabel labelModDir = new JLabel("Mod directory: " + main.getModDirectory());
		GroupLayout groupLayout = new GroupLayout(this.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(56)
							.addComponent(labelUpdate))
						.addGroup(groupLayout.createSequentialGroup()
							.addContainerGap()
							.addComponent(labelModDir, GroupLayout.DEFAULT_SIZE, 289, Short.MAX_VALUE)))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(20)
					.addComponent(labelUpdate)
					.addGap(18)
					.addComponent(labelModDir, GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
					.addContainerGap())
		);
		getContentPane().setLayout(groupLayout);
	}

	/**
	 * Creates a new List object to create a new modlist in. 
	 * @param scrollList = The JScrollPane to place this list inside.
	 */
	public void createNewList(JScrollPane scrollList)
	{
		List<Object> listData = new ArrayList<Object>();
		listMain = new JList<Object>();
		
		try {
			if(main.getModDirectory() != null) {
				listData = modList.getMods();
				System.out.println("Setting up list...");
			}
		} catch (Exception e) {
			System.err.println("Could not fetch modList.");
			return;
		}
		
		listMain.setVisibleRowCount(1);
		listMain.setOpaque(false);
		listMain.setBackground(SystemColor.activeCaption);
		listMain.setCellRenderer(new ImageListCellRenderer());
		listMain.setListData(listData.toArray());
		listMain.setSelectedIndex(0);
		listMain.setLayoutOrientation(JList.VERTICAL);
		listMain.setFixedCellHeight(74);
		listMain.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listMain.addListSelectionListener(new ListSelectionListener(){
			public void valueChanged(ListSelectionEvent event) {
				main.displayModInfo();
			}
		});
		scrollList.setViewportView(listMain);
	}
		
	/**
	 * Gets the index of the selection in the list.
	 * @return getSelectedIndex.
	 */
	public static int getListIndex()
	{
		return listMain.getSelectedIndex();
	}
		
	/**
	 * Refreshes the mod list by updating the search directory and re-running the setup.
	 */
	public void refreshList()
	{
		if(main.getModDirectory() != "") {
			try {
				input = new FileInputStream(CurrentOS.getSaveDir() + File.separator + "modloader.properties");
				settings.load(input);
				
				String modDir = settings.getProperty("ModDir");
				main.setModDirectory(modDir);
				
			} catch (IOException e1) {
				System.err.println("Failed to get property.");
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
			try {
				try {
					modList.resetList();
				} catch (Exception e) {
					System.err.println("Failed resetList()");
					e.printStackTrace();
					return;
				}
				try {
					main.checkMods();
				} catch (Exception e) {
					System.err.println("Failed checkMods()");
					e.printStackTrace();
					return;
				}
				try {
					this.createNewList(main.getScrollList());
				} catch (Exception e) {
					System.err.println("Failed setupList()");
					e.printStackTrace();
					return;
				}
				try {
					main.displayModInfo();
				} catch (Exception e) {
					System.err.println("Failed displayModInfo()");
					e.printStackTrace();
					return;
				}
			} catch (Exception e) {
				System.err.println("Failed to refresh list.");
			}
		}
		
		dispose();
	}
}
