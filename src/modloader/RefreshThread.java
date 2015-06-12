package modloader;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TableItem;

public class RefreshThread extends Thread {

	private MainFrame main = new MainFrame();
	private ModList modList = new ModList();

	public RefreshThread() {}

	/**
	 * Creates a new ArrayList and gets the TableItems for the modlist, created by ModList();
	 */
	public void setupNewList() {
		List<TableItem> listNewData = new ArrayList<TableItem>();

		if(MainFrame.getModDirectory() != null || !MainFrame.getModDirectory().isEmpty()) {
			listNewData = modList.getMods();	
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

			RefreshDialog dialog = new RefreshDialog(MainFrame.getShell(), SWT.SHEET);
			Thread t = new Thread() {
				@Override
				public void run() {  // override the run() for the running behaviors

					dialog.open();

					Properties p = ConfigManager.loadConfig(Preferences.prefPath);
					if(Boolean.parseBoolean(p.getProperty("UseSameDir")) == true) {
						MainFrame.setModDirectory(p.getProperty("GameDir"));				
						Log.info("UseSameDir == true");
					} 
					else {
						MainFrame.setModDirectory(p.getProperty("ModDir"));
						Log.info("UseSameDir == false");
					}

					try {
						try {
							modList.resetList();
						} catch (Exception e) {
							Log.error("Failed resetList()");
							Log.error(e);
							//return;
						}
						try {
							main.checkMods();
						} catch (Exception e) {
							Log.error("Failed checkMods()");
							Log.error(e);
							dialog.close();
							//return;
						}

						try {
							setupNewList();
						} catch (Exception e) {
							Log.error("Failed setupNewList()");
							Log.error(e);
							//return;
						}
						try {
							MainFrame.displayModInfo();
						} catch (Exception e) {
							Log.error("Failed displayModInfo()");
							Log.error(e);
							//return;
						}
					} catch (Exception e) {
						Log.error("Failed to refresh list.");
					}

					try {
						sleep(10);  // milliseconds
					} catch (InterruptedException ex) {}
				}
			};
			t.start();
			dialog.close();
		}
	}
}
