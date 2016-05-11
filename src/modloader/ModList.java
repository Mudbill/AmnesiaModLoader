package modloader;

import java.io.File;
import java.io.FileInputStream;
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
	
	public static final String defIcon = "/resources/icon_default.png";
	
	private static String modIcon, modTitle, modAuthor, modDesc, modMinVer, modShader, modExecName, modIgnoreShaders;
	
	public static List<TableItem> listDataArr = new ArrayList<TableItem>();
	
	public static List<String> index = new ArrayList<String>();
	public static List<String> infoTitle = new ArrayList<String>();
	public static List<String> infoAuthor = new ArrayList<String>();
	public static List<String> infoDescription = new ArrayList<String>();
	public static List<String> infoMinCompatibility = new ArrayList<String>();
	public static List<String> infoCustomShaders = new ArrayList<String>();
	public static List<String> infoCustomExecName = new ArrayList<String>();
	public static List<String> infoIgnoreShaders = new ArrayList<String>();
	private static String filePath;
	public static List<File> listIcon = new ArrayList<File>();

	private Properties cfg = new Properties();
	private Properties mainInit = new Properties();
	private InputStream inputCFG = null;
	private InputStream inputInit = null;
	//private static File[] list;
	public static int modsFoundTotal = 0;
	private boolean cfgFound = false;
	//private boolean found;

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
		infoIgnoreShaders.clear();
		listIcon.clear();
		
		MainFrame.getDisplay().syncExec(new Runnable() {
			public void run() {
				MainFrame.labelModAmount.setText("0");
			}
		});
	}
	
	/**
	 * Gets the list of mods.
	 * @return listDataArr
	 */
	public List<TableItem> getMods() {
		return ModList.listDataArr;
	}
	
	public static int getModsFound() {
		return ModList.modsFoundTotal;
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
	
	public String getModHasCustomShaders() {
		return ModList.modShader;
	}
	
	public String getModExecName() {
		return ModList.modExecName;
	}
	
	public String getModIgnoreShaders() {
		return ModList.modIgnoreShaders;
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
	
	public String getModIcon(int idx) {
		String icon = listIcon.get(idx).toString();
		return icon;
	}
	
	public String getModHasCustomShaders(int idx) {
		String customShaders = infoCustomShaders.get(idx);
		return customShaders;
	}
	
	public String getModExecName(int idx) {
		String execName = infoCustomExecName.get(idx);
		return execName;
	}
	
	public String getModIgnoreShaders(int idx) {
		String entry = infoIgnoreShaders.get(idx);
		return entry;
	}
	
	/**
	 * Adds the file path to a mod to the index list.
	 */
	private void addLaunchIndex()
	{
		String launchPath = filePath;
		index.add(launchPath);
		Log.info("Adding flag to list.");
	}
	
	/**
	 * Adds all of a mod's information available to their respective lists.
	 */
	public static void addModInfo(List<String> list)
	{
		if(list == null) {
			infoTitle.add(modTitle);
			infoAuthor.add(modAuthor);
			infoDescription.add(modDesc);
			infoMinCompatibility.add(modMinVer);
			infoCustomShaders.add(modShader);
			infoCustomExecName.add(modExecName);
			infoIgnoreShaders.add(modIgnoreShaders);			
		} else {
			infoTitle.add(list.get(0));
			infoAuthor.add(list.get(1));
			infoDescription.add(list.get(3));
			infoMinCompatibility.add(list.get(4));
			infoCustomShaders.add(list.get(5));
			infoCustomExecName.add(list.get(7));
			infoIgnoreShaders.add(list.get(6));
		}
		
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
		
		filePath = fil.getPath();
		this.addLaunchIndex();
		
		//Defaults
		modTitle = "Untitled";
		modAuthor = "Anonymous";
		modIcon = "";
		modDesc = "No description.";
		modMinVer = "Undefined";
		modShader = "No";
		modExecName = null;
		modIgnoreShaders = "false";
		
		try {
			String cfgName = "modloader.cfg";
			String cfgNameLegacy = "aml.cfg";
			String cfgPath = fil.getParent().toString();
			
			FindFile.found = false;			
			
			//Check if this mod has an accompanying modloader.cfg or aml.cfg file. If so, set cfgFound to true.
			File cfgML = new File(cfgPath + File.separator + cfgName);
			if(!cfgML.exists()) cfgML = new File(cfgPath + File.separator + cfgNameLegacy);
			if(cfgML.exists()) {
				inputCFG = new FileInputStream(cfgML);
				cfgFound = true;
				Log.info("\tFound modloader.cfg or aml.cfg.");
			} else Log.warn("\tCould not find modloader.cfg or aml.cfg. Oh well.");
			
			try {
				inputInit = new FileInputStream(fil);
				mainInit.load(inputInit);
				modTitle = mainInit.getProperty("GameName").replace("\"", "");
				Log.info("\t\tGameName \t= " + modTitle);
			} catch (Exception e) {
				modTitle = "Untitled";
				Log.warn("GameName not found. Mod incomplete?");
			}
			
			File f = new File(fil.getParent());
			f = new File(f.getParent() + File.separator + "core" + File.separator + "shaders");
			if(f.isDirectory()) modShader = "Yes";
			
			if(cfgFound) {
				cfg.load(inputCFG);
				if(cfg.getProperty("Author") != null) modAuthor = cfg.getProperty("Author"); 
				if(cfg.getProperty("IconFile") != null) modIcon = cfg.getProperty("IconFile"); 
				if(cfg.getProperty("Description") != null) modDesc = cfg.getProperty("Description").replace("�", "'"); //Patch odd formatting
				
				try {
					if(cfg.getProperty("MinVersion") != null) modMinVer = ""+Float.parseFloat(cfg.getProperty("MinVersion")); 
				} catch (NumberFormatException e) {
					Log.warn("\t\tCould not parse float from MinVersion");
				}
				
				if(cfg.getProperty("CustomExecName") != null) modExecName = cfg.getProperty("CustomExecName");
				if(cfg.getProperty("IgnoreShaders") != null) {
					modIgnoreShaders = cfg.getProperty("IgnoreShaders");
					modShader = "No";
				}
				Log.info("\t\tAuthor \t\t= " + modAuthor);
				Log.info("\t\tDescription \t= " + modDesc);
				Log.info("\t\tIconFile \t= " + modIcon);
				Log.info("\t\tMinVersion \t= " + modMinVer);
				Log.info("\t\tCustomExecName \t= " + modExecName);
				Log.info("\t\tIgnoreShaders \t= " + modIgnoreShaders);
			}
			Log.info("\t\tCustomShaders \t= " + modShader);
			
			addModInfo(null);
			
			final File iconFile = new File(cfgPath + File.separator + modIcon);
			listIcon.add(iconFile);
						
			modsFoundTotal += 1;
			
			MainFrame.getDisplay().syncExec(new Runnable() {
				public void run() {
					MainFrame.modItem = new TableItem(MainFrame.tableMods, SWT.NONE);
					MainFrame.modItem.setFont(SWTResourceManager.getFont("System", 11, SWT.NORMAL));
					MainFrame.modItem.setText(modTitle);
					if(iconFile.exists() && iconFile.isFile()) {
						Log.info("Applying icon: " + iconFile.toString());
						Image image = new Image(Display.getDefault(), iconFile.toString());
						MainFrame.modItem.setImage(Common.scale(image, MainFrame.getIconSize()));						
					} else {
						Log.warn("Icon file not found. Using default.");
						MainFrame.modItem.setImage(Common.scale(SWTResourceManager.getImage(MainFrame.class, defIcon), MainFrame.getIconSize()));
					}
					listDataArr.add(MainFrame.modItem);
					MainFrame.labelModAmount.setText(""+modsFoundTotal);
				}
			});
			
			Log.info("Adding mod to list: " + this.getModTitle());
			
		} catch (IOException e) {
			new Error(null);
			Log.error("IOException", e);
		} finally {
			if(inputCFG != null) {
				try {
					inputCFG.close();
				} catch (IOException e) {
					Log.error("IOException", e);
				}
			}
		}
	}
}