package modloader;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.ProgressBar;

public class FindFile extends Thread 
{	
	private Display display;
	private ProgressBar progressBar;
	private File cfgPath;
	private String cfgName;
	private static boolean firstLoop = false;
	public static boolean found;

	public FindFile(Display display, ProgressBar progressBar, File cfgPath, String cfgName) {
		this.display = display;
		this.progressBar = progressBar;
		this.cfgPath = cfgPath;
		this.cfgName = cfgName;
		found = false;
	}
	
	public void run() {
		Log.info("Starting file finding.");
		
		findFile(cfgName, cfgPath);
		Log.info("Finished searching for mods in directory.");
		
		display.asyncExec(new Runnable() {
			public void run() {
				MainFrame.buttonRefresh.setVisible(true);
				MainFrame.buttonRefreshCancel.setVisible(false);
				if (progressBar.isDisposed()) return;
				progressBar.setVisible(false);
				progressBar.setSelection(progressBar.getMaximum());
				if(ModList.getModsFound() == 0) {
					MessageBox m = new MessageBox(MainFrame.getShell(), SWT.ICON_WARNING);
					m.setText("No mods found");
					m.setMessage("No mods were found in the specified directory. Double check the preferences, and make sure you have mods installed.");
					m.open();
				}
			}
		});
	}
	
	/**
	 * Searches through a directory and its sub-directories, then sets up a new mod list.
	 * @param name = name of file to find.
	 * @param file = directory to search.
	 * @throws IOException 
	 */
	private void findFile(String name, File file)
    {
		if(MainFrame.abortRefresh) return;
		
        try (DirectoryStream<Path> list = Files.newDirectoryStream(file.toPath())) {
			if (!firstLoop) {
				//int i = Common.getFileAmount(file.toPath());
				display.asyncExec(new Runnable() {
					public void run() {
						if (progressBar.isDisposed()) return;
						progressBar.setMaximum(10000);
						//Log.info("Files to scan = " + i);
					}
				});
				firstLoop = true;
			}

        	for (Path fil : list) {
				display.asyncExec(new Runnable() {
					public void run() {
						if(progressBar.isDisposed()) return;
						if(progressBar.getSelection() < progressBar.getMaximum() * 0.99)
							progressBar.setSelection(progressBar.getSelection() + 1);
					}
				});
        		if (Files.isDirectory(fil)) {
        			findFile(name, fil.toFile());
        		}
        		else if (name.equalsIgnoreCase(fil.toFile().getName())) {
        			Log.info("Found file: " + fil);
        			new ModList(fil.toFile());
        		}
        	}
        } catch (IOException e) {
        	Log.error("", e);
        }
    }
}