package modloader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.wb.swt.SWTResourceManager;

public class ModList {
	
	public final String defIcon = "/resources/icon_default.png";
	
	private static String modIcon;
	private static String modTitle;
	private static String modAuthor;
	private static String modDesc;
	private static String modMinVer;
	private static String modShader;
	private static String modExecName;
	
	private static List<TableItem> listDataArr = new ArrayList<TableItem>();
	
	private static List<String> index = new ArrayList<String>();
	private static List<String> infoTitle = new ArrayList<String>();
	private static List<String> infoAuthor = new ArrayList<String>();
	private static List<String> infoDescription = new ArrayList<String>();
	private static List<String> infoMinCompatibility = new ArrayList<String>();
	private static List<String> infoCustomShaders = new ArrayList<String>();
	private static List<String> infoCustomExecName = new ArrayList<String>();
	private static String filePath;

	private Properties cfg = new Properties();
	private Properties mainInit = new Properties();
	private InputStream inputCFG = null;
	private InputStream inputInit = null;
	private static File[] list;
	public static int modsFoundTotal = 0;
	private boolean cfgFound = false;

	public ModList() {}
	
	/**
	 * Resets the lists by clearing existing information.
	 */
	public void resetList()
	{
		Log.info("Resetting list.");
		
		modsFoundTotal = 0;
		
		MainFrame.tableMods.removeAll();
		
		listDataArr.clear();
		index.clear();
		
		infoTitle.clear();
		infoAuthor.clear();
		infoDescription.clear();
		infoMinCompatibility.clear();
		infoCustomShaders.clear();
		infoCustomExecName.clear();
	}
	
	/**
	 * Gets the list of mods.
	 * @return listDataArr
	 */
	public List<TableItem> getMods() {
		return ModList.listDataArr;
	}

	public String getModTitle() {
		return ModList.modTitle;
	}
	
	public String getModIcon() {
		return ModList.modIcon;
	}
	
	public String getModAuthor() {
		return ModList.modAuthor;
	}
	
	public String getModDesc() {
		return ModList.modDesc;
	}
	
	public int getModsFound() {
		return ModList.modsFoundTotal;
	}
	
	public String getModHasCustomShaders() {
		return ModList.modShader;
	}
	
	public String getModExecName() {
		return ModList.modExecName;
	}
	
	public String getModMinCompatibility(int idx) {
		String comp = infoMinCompatibility.get(idx);
		return comp;
	}
	
	public String getModTitle(int idx) {
		String title = infoTitle.get(idx);
		return title;
	}
	
	public String getModAuthor(int idx) {
		String author = infoAuthor.get(idx);
		return author;
	}
	
	public String getModDesc(int idx) {
		String desc = infoDescription.get(idx);
		return desc;
	}
	
	public String getModHasCustomShaders(int idx) {
		String customShaders = infoCustomShaders.get(idx);
		return customShaders;
	}
	
	public String getModExecName(int idx) {
		String execName = infoCustomExecName.get(idx);
		return execName;
	}

	/**
	 * Searches through a directory and its sub-directories, then sets up a new mod list.
	 * @param name = name of file to find.
	 * @param file = directory to search.
	 */
	public boolean findFile(String name,File file)
    {
		boolean fileFound = false;
        list = file.listFiles();
        if(list != null)
        for (File fil : list)
        {
            if (fil.isDirectory())
            {
                findFile(name,fil);
            }
            else if (name.equalsIgnoreCase(fil.getName()))
            {
            	Log.info("\tFound file: " + fil);
            	fileFound = true;
            }
        }
        return fileFound;
    }
	
	/**
	 * Adds the file path to a mod to the index list.
	 */
	private void addLaunchIndex()
	{
		String launchPath = filePath;
		index.add(launchPath);
		Log.info("Adding flag to list.");// + filePath);
	}
	
	/**
	 * Adds all of a mod's information available to their respective lists.
	 */
	private void addModInfo()
	{		
		infoTitle.add(modTitle);
		infoAuthor.add(modAuthor);
		infoDescription.add(modDesc);
		infoMinCompatibility.add(modMinVer);
		infoCustomShaders.add(modShader);
		infoCustomExecName.add(modExecName);
		
		Log.info("Adding information to list at index: " + modsFoundTotal);
	}
	
	/**
	 * Gets the mod file path (launch parameter) of a listed mod.
	 * @param idx = Mod index to get from.
	 * @return filePath
	 */
	public String getLaunchIndex(int idx)
	{
		filePath = index.get(idx);
		return filePath;
	}

	/**
	 * Constructor for main functionality used. This is ran every time a "main_init.cfg" file is found and adds 
	 * all the associated information to that mod's index value.
	 * @param fil = The File object (path) of an already found main_init file.
	 */
	public ModList(File fil) {
		Log.info("Found game file: " + fil);
		
		filePath = fil.getPath();
		this.addLaunchIndex();
		
		//Defaults
		modTitle = "Untitled";
		modAuthor = "Anonymous";
		modIcon = "";
		modDesc = "No description.";
		modMinVer = "Undefined";
		modShader = "Undefined";
		modExecName = null;
				
		TableItem modItem = new TableItem(MainFrame.tableMods, SWT.NONE);
		modItem.setFont(SWTResourceManager.getFont("System", 11, SWT.NORMAL));
		
		try {
			String cfgName = "modloader.cfg";
			String cfgNameLegacy = "aml.cfg";
			String cfgPath = fil.getParent().toString();
			
			boolean found = findFile(cfgName, new File(cfgPath));
			if(!found) findFile(cfgNameLegacy, new File(cfgPath));

			inputInit = new FileInputStream(fil);
			
			try {
				if(found) inputCFG = new FileInputStream(cfgPath + File.separator + cfgName);
				else inputCFG = new FileInputStream(cfgPath + File.separator + cfgNameLegacy);
				cfgFound = true;
			} catch (FileNotFoundException e) {
				Log.warn("Could not find modloader.cfg or aml.cfg. Oh well.");
			}
			
			try {
				mainInit.load(inputInit);
				modTitle = mainInit.getProperty("GameName").replace("\"", "");
				Log.info("\t\tGameName \t= " + modTitle);
			} catch (IOException e) {
				modTitle = "Untitled";
				Log.warn("GameName not found. Mod incomplete?");
				//Log.error(e);
				modItem.setFont(SWTResourceManager.getFont("System", 11, SWT.ITALIC));
			}
			
			File f = new File(fil.getParent());
			f = new File(f.getParent() + File.separator + "shaders");

			Log.info(f.toString());

			if(f.isDirectory()) {
				modShader = "Yes";
			} else modShader = "No";
			
			if(cfgFound) {
				cfg.load(inputCFG);
				if(cfg.getProperty("Author") != null) modAuthor = cfg.getProperty("Author"); 
				if(cfg.getProperty("IconFile") != null) modIcon = cfg.getProperty("IconFile"); 
				if(cfg.getProperty("Description") != null) modDesc = cfg.getProperty("Description").replace("â", "'");
				try {
					if(cfg.getProperty("MinVersion") != null) modMinVer = ""+Float.parseFloat(cfg.getProperty("MinVersion")); 
				} catch (NumberFormatException e) {
					Log.warn("\t\tCould not parse float from MinVersion");
				}
				
				if(cfg.getProperty("CustomExecName") != null) modExecName = cfg.getProperty("CustomExecName");
				Log.info("\t\tAuthor \t\t= " + modAuthor);
				Log.info("\t\tDescription \t= " + modDesc);
				Log.info("\t\tIconFile \t= " + modIcon);
				Log.info("\t\tMinVersion \t= " + modMinVer);
				Log.info("\t\tCustomShaders \t= " + modShader);
				Log.info("\t\tCustomExecName \t= " + modExecName);
			}
			
			this.addModInfo();
			modItem.setText(this.getModTitle());
			
			try {
				File iconFile = new File(cfgPath + File.separator + modIcon);
				
				if(iconFile.exists())
				{
					//scale the image to specified size.
					Image image = new Image(Display.getDefault(), iconFile.toString());
					modItem.setImage(Common.scale(image, MainFrame.getIconSize()));
					
				} else {
					Log.warn("Icon file not found. Using default.");
					modItem.setImage(Common.scale(SWTResourceManager.getImage(MainFrame.class, defIcon), MainFrame.getIconSize()));
				}
				
			} catch(Exception e) {
				Log.error("\t\tFailed adding icon: " + cfgPath + File.separator + modIcon);
				modItem.setImage(Common.scale(SWTResourceManager.getImage(MainFrame.class, defIcon), MainFrame.getIconSize()));
				//Log.error(e);
			}
			
			modsFoundTotal += 1;
			listDataArr.add(modItem);
			Log.info("Adding mod to list: " + this.getModTitle());
			
		} catch (IOException e) {
			Log.error("IOException");
			Log.error(e);
		} finally {
			if(inputCFG != null) {
				try {
					inputCFG.close();
				} catch (IOException e) {
					Log.error("IOException");
					Log.error(e);
				}
			}
		}
	}

}
