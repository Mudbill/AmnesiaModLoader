package modloader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;

public class Shaders {
	
	public static boolean installed = false, backupExists = false;
	private static File rootShaderDir, rootBackupDir, modDir, modShaderDir;
	
	public Shaders() {}
	
	public static void updateInfo() {
		ModList modList = new ModList();
		
		modDir = new File(modList.getLaunchIndex(Refresh.getListIndex()));
		modDir = new File(modDir.getParent());
		modDir = new File(modDir.getParent());
		
		modShaderDir = new File(modDir + File.separator + "shaders");
		
		rootShaderDir = new File(MainFrame.getGameDirectory() + File.separator + "shaders");
		rootBackupDir = new File(MainFrame.getGameDirectory() + File.separator + "shaders_backup");
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
				if(MainFrame.getWarnShader()) {
					MessageBox m = new MessageBox(MainFrame.getShell(), SWT.SHEET | SWT.YES | SWT.NO | SWT.ICON_INFORMATION);
					m.setText("Install Shaders");
					m.setMessage("This mod features custom shaders. Would you like to install them now? It's not recommended to play the mod without installing them.");
					int i = m.open();
					if(i != SWT.YES) return false;
				}
				return true;
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
				Log.error("Failed to back up shaders.");
				Log.error(e);
				return false;
			}
		}
		Log.info("\tCopied files from root shaders to backup.");
		return true;
	}
	
	public static void installShaders()
	{
		try {
			if(!rootShaderDir.exists()) {
				rootShaderDir.mkdir();
				Log.info("Creating new shaders directory.");
			}
						
			if(rootShaderDir.isDirectory()) {
				File[] shaderFiles = modShaderDir.listFiles();
				for(int i = 0; i < shaderFiles.length; i++) {
					Files.copy(Paths.get(shaderFiles[i].getPath()), Paths.get(rootShaderDir.getPath() + File.separator + shaderFiles[i].getName()), StandardCopyOption.REPLACE_EXISTING);
				}
				Log.info("Copied files from mod shaders to root shaders.");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		installed = true;
	}
	
	public static boolean uninstallShaders()
	{
		if(rootShaderDir.exists()) {
			File[] shaderFilesRoot = rootShaderDir.listFiles();
			for(int i = 0; i < shaderFilesRoot.length; i++) {
				try {
					Files.delete(Paths.get(rootShaderDir.getPath() + File.separator + shaderFilesRoot[i].getName()));
				} catch (IOException e) {
					e.printStackTrace();
					return false;
				}
			}
			Log.info("Cleaning root shaders folder.");

			File[] shaderFilesBackup = rootBackupDir.listFiles();
			try {
				for(int i = 0; i < shaderFilesBackup.length; i++) {
					Files.copy(Paths.get(shaderFilesBackup[i].getPath()), Paths.get(rootShaderDir.getPath() + File.separator + shaderFilesBackup[i].getName()), StandardCopyOption.REPLACE_EXISTING);
				}
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		Log.info("Restored original shaders.");
		
		installed = false;
		return true;
	}
}
