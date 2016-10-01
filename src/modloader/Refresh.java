package modloader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.eclipse.swt.widgets.TableItem;

public class Refresh {

	private ModList modList = new ModList();
	
	public Refresh() {}
	
	/**
	 * Creates a new ArrayList and gets the TableItems for the modlist, created by ModList();
	 */
	public void setupNewList() {
		List<TableItem> listNewData = new ArrayList<TableItem>();
		
		if(CurrentOS.getSystem() == "MacOS") if(MainFrameOSX.getModDirectory() != null || !MainFrameOSX.getModDirectory().isEmpty()) {
			listNewData = modList.getMods();	
			//Log.info("modList.getMods() amount = " + modList.getMods().size());
		} else if(CurrentOS.getSystem() == "Windows") if(Engine.getModDirectory() != null || !Engine.getModDirectory().isEmpty()) {
			listNewData = modList.getMods();	
			//Log.info("modList.getMods() amount = " + modList.getMods().size());
		}
		
		if(CurrentOS.getSystem() == "MacOS") MainFrameOSX.setupModList(listNewData);
		else if(CurrentOS.getSystem() == "Windows") MainFrameWin32.setupModList(listNewData);
	}
	
	/**
	 * Gets the selected index from the modlist Table().
	 * @return
	 */
	public static int getListIndex() {
		if(CurrentOS.getSystem() == "MacOS") return MainFrameOSX.tableMods.getSelectionIndex();
		else if(CurrentOS.getSystem() == "Windows") return MainFrameWin32.tableMods.getSelectionIndex();
		
		return 0;
	}
	
	/**
	 * Refreshes the list
	 */
	public void refreshList() {
		if(CurrentOS.getSystem() == "MacOS") {
			if(MainFrameOSX.getModDirectory() != null || !MainFrameOSX.getModDirectory().equals("")) {
				
				Properties p = ConfigManager.loadConfig(CurrentOS.getSaveDir() + File.separator + CurrentOS.getConfigName());
				if(Boolean.parseBoolean(p.getProperty("UseSameDir")) == true) 
					MainFrameOSX.setModDirectory(p.getProperty("GameDir"));				
				else MainFrameOSX.setModDirectory(p.getProperty("ModDir"));
				
				try {
					MainFrameOSX.buttonRefresh.setVisible(false);
					MainFrameOSX.buttonRefreshCancel.setVisible(true);
					MainFrameOSX.progressBar.setSelection(0);
					try {
						modList.resetList();
					} catch (Exception e) {
						Log.error("Failed resetList()", e);
					}
					try {
						Engine.checkMods(MainFrameOSX.getDisplay(), MainFrameOSX.progressBar);
					} catch (Exception e) {
						Log.error("Failed checkMods()", e);
					}
					try {
						setupNewList();
					} catch (Exception e) {
						Log.error("Failed setupNewList()", e);
					}
					ModCache.cacheChanged = true;
				} catch (Exception e) {
					Log.error("Failed to refresh list.", e);
				}
			} else {
				Log.warn("ModDir is empty!");
			}
			MainFrameOSX.abortRefresh = false;
			
		} else if(CurrentOS.getSystem() == "Windows") {
			if(Engine.getModDirectory() != null || !Engine.getModDirectory().equals("")) {
				
				Properties p = ConfigManager.loadConfig(CurrentOS.getSaveDir() + File.separator + CurrentOS.getConfigName());
				if(Boolean.parseBoolean(p.getProperty("UseSameDir")) == true) 
					Engine.setModDirectory(p.getProperty("GameDir"));				
				else Engine.setModDirectory(p.getProperty("ModDir"));
				
				try {
					MainFrameWin32.buttonRefresh.setVisible(false);
					MainFrameWin32.buttonRefreshCancel.setVisible(true);
					//MainFrameWin32.progressBar.setSelection(0);
					try {
						modList.resetList();
					} catch (Exception e) {
						Log.error("Failed resetList()", e);
					}
					try {
						Engine.checkMods(MainFrameWin32.getDisplay(), MainFrameWin32.progressBar);
					} catch (Exception e) {
						Log.error("Failed checkMods()", e);
					}
					try {
						setupNewList();
					} catch (Exception e) {
						Log.error("Failed setupNewList()", e);
					}
					ModCache.cacheChanged = true;
				} catch (Exception e) {
					Log.error("Failed to refresh list.", e);
				}
			} else {
				Log.warn("ModDir is empty!");
			}
			Engine.abortRefresh = false;
		}
	}
}
