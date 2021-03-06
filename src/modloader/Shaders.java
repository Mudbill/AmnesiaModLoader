package modloader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;

public class Shaders {
	
	public static boolean installed, backupExists = false;
	private static File rootShaderDir, rootBackupDir, modDir, modShaderDir;
	public static int cancelOperation = 1;
	
	public Shaders() {}
	
	public static void updateInfo() {
		ModList modList = new ModList();
		
		modDir = new File(modList.getLaunchIndex(Refresh.getListIndex()));
		modDir = new File(modDir.getParent());
		modDir = new File(modDir.getParent());
		
		modShaderDir = new File(modDir + File.separator + "core" + File.separator + "shaders");
		rootShaderDir = new File(MainFrameOSX.getGameDirectory() + File.separator + "core" + File.separator + "shaders");
		rootBackupDir = new File(MainFrameOSX.getGameDirectory() + File.separator + "core" + File.separator + "shaders_backup");
	}
	
	public static void promptRestore() {
		
		if(Shaders.installed) {
			MessageBox m = new MessageBox(Engine.getShell(), SWT.OK | SWT.ICON_QUESTION);
			m.setText("Restore Shaders?");
			m.setMessage("Are you ready to restore the shaders? Please make sure you've closed the game, then click OK. The custom shaders will be uninstalled and the original ones restored.");
			if(m.open() != -1) {
				Log.info("Restore shaders!");
				boolean b = Shaders.uninstallShaders();
				MessageBox m2 = new MessageBox(Engine.getShell(), SWT.OK | SWT.ICON_INFORMATION);
				if(b) {
					m2.setText("Success");
					m2.setMessage("The shaders were successfully restored!");
				} else {
					m2.setText("Failed");
					m2.setMessage("The shaders could not be restored. You might have to do so manually.");
				}
				m2.open();
			}
		}
	}
	
	public static boolean checkShaders()
	{
		backupExists = false;
		
		if(!installed) {
			
			//Checking if original folder exists.
			if(rootBackupDir.isDirectory()) {
				backupExists = true;
			}

			if(rootShaderDir.isDirectory() && modShaderDir.isDirectory()) {
								
				if(MainFrameOSX.getWarnShader()) {
					MessageBox m = new MessageBox(MainFrameOSX.getShell(), SWT.SHEET | SWT.YES | SWT.NO | SWT.CANCEL | SWT.ICON_INFORMATION);
					m.setText("Install Shaders");
					m.setMessage("This mod features custom shaders. Would you like to install them now? It's not recommended to play the mod without installing them.\n\n- Yes will install and launch.\n- No will skip installation and launch.\n- Cancel will not make any changes and not launch.");
					int i = m.open();
					if(i == SWT.YES) {
						cancelOperation = 1;
						return true;
					}
					if(i == SWT.NO) {
						cancelOperation = 0;
						return false;
					}
					if(i != SWT.YES || i != SWT.NO) {
						cancelOperation = -1;
					}
				}
				if(cancelOperation < 0) {
					Log.info("Execution cancelled.");
				}
				else return true;
			}
		}
		return false;
	}
	
	public static boolean backupShaders()
	{
		if(backupExists) {
			Log.info("Backup already exists.");
			return false;
		}
		Log.info("Backing up shaders...");
		
		if(!rootBackupDir.exists()) {
			rootBackupDir.mkdir();
			Log.info("\tCreating backup shaders directory.");
		} else return false;
		
		if(rootBackupDir.isDirectory()) {
			File[] shaderFiles = rootShaderDir.listFiles();
			try {
				for(int i = 0; i < shaderFiles.length; i++) {
					Files.copy(Paths.get(shaderFiles[i].getPath()), Paths.get(rootBackupDir.getPath() + File.separator + shaderFiles[i].getName()), StandardCopyOption.REPLACE_EXISTING);
				}
			} catch (IOException e) {
				Log.error("Failed to back up shaders.", e);
				return false;
			}
		}
		Log.info("\tCopied files from root shaders to backup.");
		return true;
	}
	
	public static void installShaders()
	{
		if(!rootShaderDir.exists()) {
			rootShaderDir.mkdir();
			Log.info("Creating new shaders directory.");
		}

		if(rootShaderDir.isDirectory()) {
			copyShaders();
			Log.info("Copied files from mod shaders to root shaders.");
		}

		installed = true;
	}
	
	private static void copyShaders()
	{
		File[] files = modShaderDir.listFiles();
		
		if(files != null)
		for(File file : files) {
			try {
				Log.info("\tCopying: " + file.getAbsolutePath());
				
				Files.copy(
						Paths.get(file.getPath()),
						Paths.get(rootShaderDir.getPath() + File.separator + file.getName()),
						StandardCopyOption.REPLACE_EXISTING
				);
			} catch (IOException e) {
				Log.error("IOException", e);
			}
			if(file.isDirectory()) {
				Log.info(Log.tab() + file.getName() + " is a directory. Delving deeper to copy the contents.");
				modShaderDir = new File(file.getAbsolutePath());
				rootShaderDir = new File(rootShaderDir.getPath() + File.separator + file.getName());
				copyShaders();
				return;
			}
		}
		rootShaderDir = new File(MainFrameOSX.getGameDirectory() + File.separator + "core" + File.separator + "shaders");
	}
	
	private static void deleteShaders()
	{
		File[] files = rootShaderDir.listFiles();
		
		for(File file : files) {
			if(file.isDirectory()) {
				Log.info(Log.tab() + file.getName() + " is a directory. Deleting contents before folder.");
				rootShaderDir = new File(rootShaderDir.getPath() + File.separator + file.getName());
				files = rootShaderDir.listFiles();
				deleteShaders();
			}
			try {
				Log.info("\tDeleting: " + file.getAbsolutePath());
				
				Files.delete(
						Paths.get(file.getAbsolutePath())
				);
			} catch (Exception e) {
				Log.error("Exception", e);
			}
		}
		rootShaderDir = new File(MainFrameOSX.getGameDirectory() + File.separator + "core" + File.separator + "shaders");
	}
	
	public static boolean uninstallShaders()
	{
		if(rootShaderDir.exists()) {
			deleteShaders();

			Log.info("Cleaning root shaders folder.");

			File[] shaderFilesBackup = rootBackupDir.listFiles();
			try {
				for(File file : shaderFilesBackup) {
					Files.copy(
							Paths.get(file.getPath()), 
							Paths.get(rootShaderDir.getPath() + File.separator + file.getName()), 
							StandardCopyOption.REPLACE_EXISTING
					);
				}
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		Log.info("Copied original shaders back into place.");
		
		installed = false;
		return true;
	}
}
