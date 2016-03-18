package modloader;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.eclipse.swt.widgets.TableItem;

public class Refresh {

	private MainFrame main = new MainFrame();
	private ModList modList = new ModList();
	
	public Refresh() {}
	
	/**
	 * Creates a new ArrayList and gets the TableItems for the modlist, created by ModList();
	 */
	public void setupNewList() {
		List<TableItem> listNewData = new ArrayList<TableItem>();
		
		if(MainFrame.getModDirectory() != null || !MainFrame.getModDirectory().isEmpty()) {
			listNewData = modList.getMods();	
			//Log.info("modList.getMods() amount = " + modList.getMods().size());
		}
		
		MainFrame.setupModList(listNewData);
	}
	
	/**
	 * Gets the selected index from the modlist Table().
	 * @return
	 */
	public static int getListIndex() {
		return MainFrame.tableMods.getSelectionIndex();
	}
	
	/**
	 * Refreshes the list
	 */
	public void refreshList() {
		if(MainFrame.getModDirectory() != null || !MainFrame.getModDirectory().equals("")) {
			
			Properties p = ConfigManager.loadConfig(Preferences.prefPath);
			if(Boolean.parseBoolean(p.getProperty("UseSameDir")) == true) 
				 MainFrame.setModDirectory(p.getProperty("GameDir"));				
			else MainFrame.setModDirectory(p.getProperty("ModDir"));
			
			try {
				MainFrame.buttonRefresh.setVisible(false);
				MainFrame.buttonRefreshCancel.setVisible(true);
				MainFrame.progressBarInf.setVisible(true);
				MainFrame.progressBar.setSelection(0);
				try {
					modList.resetList();
				} catch (Exception e) {
					Log.error("Failed resetList()");
					Log.error(e);
				}
				try {
					main.checkMods();
				} catch (Exception e) {
					Log.error("Failed checkMods()");
					Log.error(e);
				}
				try {
					setupNewList();
				} catch (Exception e) {
					Log.error("Failed setupNewList()");
					Log.error(e);
				}
				ModCache.cacheChanged = true;
			} catch (Exception e) {
				Log.error("Failed to refresh list.");
				Log.error(e);
			}
		} else {
			Log.error("ModDir is empty!");
		}
		MainFrame.abortRefresh = false;
	}
}
