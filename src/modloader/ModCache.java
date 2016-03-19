package modloader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.wb.swt.SWTResourceManager;

public class ModCache {

	public ModCache() {}
	
	private String cacheDir = CurrentOS.getSaveDir() + File.separator + "cache";
	public static boolean cacheChanged = false;
	
	public boolean checkCache() {
		File dir = new File(cacheDir);
		if(dir.exists() && dir.list().length > 0) return true;
		else return false;
	}
	
	public void clearCache() {
		File dir = new File(cacheDir);
		if(!dir.exists()) dir.mkdirs();
		
		for(File f : dir.listFiles()) {
			f.delete();
			Log.info("Deleting: " + f);
		}
	}
	
	public void writeCache() {
		if(!cacheChanged) return;
		
		Log.info("Writing cache for next boot...");
		
		clearCache();
		
		for(int index = 0; index < ModList.modsFoundTotal; index++) {
			String title = "Untitled";
			String author = "By Anonymous";
			String icon = "";
			String desc = "No description.";
			String compatMin = "Undefined";
			String shaders = "Undefined";
			String shadersIgnore = "Undefined";
			String exec = null;
			String launch = "";
			
			ModList modList = new ModList();
			Properties p = new Properties();
			
			title = modList.getModTitle(index); Log.info("\tCache title: " + title);
			author = modList.getModAuthor(index); Log.info("\tCache author: " + author);
			desc = modList.getModDesc(index); Log.info("\tCache desc: " + desc);
			icon = ModList.listIcon.get(index).getAbsolutePath(); Log.info("\tCache icon: " + icon);
			compatMin = modList.getModMinCompatibility(index); Log.info("\tCache compatMin: " + compatMin);
			shaders = modList.getModHasCustomShaders(index); Log.info("\tCache shaders: " + shaders);
			shadersIgnore = modList.getModIgnoreShaders(index); Log.info("\tCache shadersIgnore: " + shadersIgnore);
			exec = modList.getModExecName(index); Log.info("\tCache exec: " + exec);
			launch = modList.getLaunchIndex(index); Log.info("\tCache launch: " + launch);
			
			if(exec == null) exec = "";
			
			p.setProperty("Title", title);
			p.setProperty("Author", author);
			p.setProperty("Desc", desc);
			p.setProperty("Icon", icon);
			p.setProperty("CompatMin", compatMin);
			p.setProperty("Shaders", shaders);
			p.setProperty("ShadersIgnore", shadersIgnore);
			p.setProperty("Exec", exec);
			p.setProperty("Launch", launch);
			
			ConfigManager.writeConfig(p, cacheDir + File.separator + "cache_index_"+index+".cache");
		}
	}
	
	public void loadCache() {
		Log.info("Loading cache...");
		
		File[] cacheFiles = new File(cacheDir).listFiles();
		
		if(cacheFiles != null) {
			for(int index = 0; index < cacheFiles.length; index++) {
				Log.info("Adding cached index " + index + " to list.");
				
				Properties p = ConfigManager.loadConfig(cacheDir + File.separator + "cache_index_"+index+".cache");
				
				String title = p.getProperty("Title"); 
				String author = p.getProperty("Author");
				String icon = p.getProperty("Icon");
				String desc = p.getProperty("Desc");
				String compatMin = p.getProperty("CompatMin");
				String shaders = p.getProperty("Shaders");
				String shadersIgnore = p.getProperty("ShadersIgnore");
				String exec = p.getProperty("Exec");
				String launch = p.getProperty("Launch");
				
				List<String> loadCache = new ArrayList<String>();
				loadCache.add(title);
				loadCache.add(author);
				loadCache.add(icon);
				loadCache.add(desc);
				loadCache.add(compatMin);
				loadCache.add(shaders);
				loadCache.add(shadersIgnore);
				loadCache.add(exec);
				loadCache.add(launch);
				
				setupCache(loadCache);
			}
			MainFrame.labelModAmount.setText(""+cacheFiles.length);			
		}
	}
	
	private void setupCache(List<String> col) {
		String title = col.get(0);
		String author = col.get(1);
		String icon = col.get(2);
		String desc = col.get(3);
		String compatMin = col.get(4);
		String shaders = col.get(5);
		String shadersIgnore = col.get(6);
		String exec = col.get(7);
		String launch = col.get(8);
		
		TableItem modItem = new TableItem(MainFrame.tableMods, SWT.NONE);
		modItem.setFont(SWTResourceManager.getFont("System", 11, SWT.NORMAL));

		try {
			File iconFile = new File(icon);
			
			if(title.equals("")) {
				title = "Untitled";
				modItem.setFont(SWTResourceManager.getFont("System", 11, SWT.ITALIC));
			}
		
			if(iconFile.exists() && iconFile.isFile())
			{
				//scale the image to specified size.
				Image image = new Image(Display.getDefault(), iconFile.toString());
				modItem.setImage(Common.scale(image, MainFrame.getIconSize()));
				
			} else {
				Log.warn("Icon file not found. Using default.");
				modItem.setImage(Common.scale(SWTResourceManager.getImage(MainFrame.class, ModList.defIcon), MainFrame.getIconSize()));
			}
			
		} catch(Exception e) {				
			Log.error("\tFailed adding icon: " + icon, e);
			modItem.setImage(Common.scale(SWTResourceManager.getImage(MainFrame.class, ModList.defIcon), MainFrame.getIconSize()));
		}
				
		ModList.infoTitle.add(title);
		ModList.infoAuthor.add(author); 
		ModList.infoDescription.add(desc); 
		ModList.infoMinCompatibility.add(compatMin); 
		ModList.infoCustomShaders.add(shaders); 
		ModList.infoCustomExecName.add(exec); 
		ModList.infoIgnoreShaders.add(shadersIgnore); 
		ModList.index.add(launch);
		
		Log.info("\tCache title: " + title);
		Log.info("\tCache author: " + author);
		Log.info("\tCache desc: " + desc);
		Log.info("\tCache icon: " + icon);
		Log.info("\tCache compatMin: " + compatMin);
		Log.info("\tCache shaders: " + shaders);
		Log.info("\tCache shadersIgnore: " + shadersIgnore);
		Log.info("\tCache exec: " + exec);
		Log.info("\tCache launch: " + launch);
		
		modItem.setText(title);
		ModList.modsFoundTotal += 1;
		ModList.listDataArr.add(modItem);
	}
}
