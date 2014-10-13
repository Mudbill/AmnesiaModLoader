package mudbill.modloader;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ModList extends JLabel {

	private static final long serialVersionUID = 1L;
	
	private JPanel modPanel;
	private JLabel modLabel;
	
	public final String defIcon = "/resources/icon_default.png";
	
	private static String modIcon;
	private static String modTitle;
	private static String modAuthor;
	private static String modDesc;
	private static String modMinCompat;
	private static String modMaxCompat;
	
	private static List<Object> listDataArr = new ArrayList<Object>();
	private static List<String> index = new ArrayList<String>();
	private static List<String> infoTitle = new ArrayList<String>();
	private static List<String> infoAuthor = new ArrayList<String>();
	private static List<String> infoDescription = new ArrayList<String>();
	private static List<String> infoMinCompatibility = new ArrayList<String>();
	private static List<String> infoMaxCompatibility = new ArrayList<String>();
	private static String filePath;
	
	private Properties cfg = new Properties();
	private Properties mainInit = new Properties();
	private InputStream inputCFG = null;
	private InputStream inputInit = null;
	private static File[] list;
	private static int modsFoundTotal = 0;
	private boolean cfgFound = false;

	public ModList() {}
	
	public void resetList()
	{
		modsFoundTotal = 0;
		listDataArr = new ArrayList<Object>();
		System.out.println("Resetting list.");
		index = new ArrayList<String>();
		infoTitle = new ArrayList<String>();
		infoAuthor = new ArrayList<String>();
		infoDescription = new ArrayList<String>();
		infoMinCompatibility = new ArrayList<String>();
		infoMaxCompatibility = new ArrayList<String>();
	}
	
	public String getDefIcon()
	{
		return this.defIcon;
	}
	
	public String getModTitle()
	{
		return ModList.modTitle;
	}
	
	public List<Object> getMods()
	{
		return ModList.listDataArr;
	}
	
	public String getModIcon()
	{
		return ModList.modIcon;
	}
	
	public String getModAuthor()
	{
		return ModList.modAuthor;
	}
	
	public String getModDesc()
	{
		return ModList.modDesc;
	}
	
	public int getModsFound()
	{
		return ModList.modsFoundTotal;
	}
	
	public void findFile(String name,File file)
    {
        list = file.listFiles();
        if(list!=null)
        for (File fil : list)
        {
            if (fil.isDirectory())
            {
                findFile(name,fil);
            }
            else if (name.equalsIgnoreCase(fil.getName()))
            {
            	System.out.println("\tFound file: " + fil);
            }
        }
    }
	
	private void addLaunchIndex()
	{
		String launchPath = filePath;
		index.add(launchPath);
		System.out.println("Adding flag to list: " + filePath);
	}
	
	private void addModInfo()
	{		
		infoTitle.add(modTitle);
		infoAuthor.add(modAuthor);
		infoDescription.add(modDesc);
		infoMinCompatibility.add(modMinCompat);
		infoMaxCompatibility.add(modMaxCompat);

		System.out.println("Adding information to list.");
	}
	
	public String getModMinCompatibility(int idx)
	{
		return infoMinCompatibility.get(idx);
	}
	
	public String getModMaxCompatibility(int idx)
	{
		return infoMaxCompatibility.get(idx);
	}
	
	public String getModTitle(int idx)
	{
		String title = infoTitle.get(idx);
		return title;
	}
	
	public String getModAuthor(int idx)
	{
		String author = infoAuthor.get(idx);
		return author;
	}
	
	public String getModDesc(int idx)
	{
		String desc = infoDescription.get(idx);
		return desc;
	}
	
	public String getLaunchIndex(int idx)
	{
		filePath = index.get(idx);
		return filePath;
	}

	public static BufferedImage resize(BufferedImage image, int width, int height) 
	{
	    BufferedImage bi = new BufferedImage(width, height, BufferedImage.TRANSLUCENT);
	    Graphics2D g2d = (Graphics2D) bi.createGraphics();
	    g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
	    g2d.drawImage(image, 0, 0, width, height, null);
	    g2d.dispose();
	    return bi;
	}
	
	public ModList(File fil) {
		
		System.out.println("Found game file: " + fil);
		filePath = fil.getPath();
		this.addLaunchIndex();
		
		modTitle = "Untitled";
		modAuthor = "Anonymous";
		modIcon = "";
		modDesc = "No description.";
		modMinCompat = "Undefined";
		modMaxCompat = "Undefined";
		
		modPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		modLabel = new JLabel(this.getModTitle());
		modLabel.setFont(new Font("Verdana", Font.PLAIN, 16));

        try {
            String cfgName = "aml.cfg";
            String cfgPath = fil.getParent().toString();
            this.findFile(cfgName, new File(cfgPath));
            			
        	inputInit = new FileInputStream(fil);
			try {
				inputCFG = new FileInputStream(cfgPath + File.separator + cfgName);
				cfgFound = true;
			} catch (FileNotFoundException e) {
				System.err.println("\tMod aml file not found; ignoring.");
			}
	 
			try {
				mainInit.load(inputInit);
				modTitle = mainInit.getProperty("GameName").replace("\"",""); System.out.println("\t\tGameName \t= " + modTitle);
			} catch (Exception e) {
				modTitle = "Untitled";
				modLabel.setFont(new Font("Verdana", Font.ITALIC, 18));
				System.err.println("\t\tGameName not found! Mod incomplete?");
				//e.printStackTrace();
			}
			
			if(cfgFound) {
				cfg.load(inputCFG);
				modAuthor = cfg.getProperty("Author"); System.out.println("\t\tAuthor \t\t= " + modAuthor); 
				modIcon = cfg.getProperty("IconFile"); System.out.println("\t\tIconFile \t= " + modIcon); 
				modDesc = cfg.getProperty("Description"); System.out.println("\t\tDescription \t= " + modDesc); 
				modMinCompat = cfg.getProperty("MinVersion"); System.out.println("\t\tMinVersion \t= " + modMinCompat);
				modMaxCompat = cfg.getProperty("MaxVersion"); System.out.println("\t\tMaxVersion \t= " + modMaxCompat);
			}

			this.addModInfo();
			modLabel.setText(this.getModTitle());
			
			try {				
				File iconFile = new File(cfgPath + File.separator + modIcon);
				
				if(iconFile.exists())
				{
					//Resize the image to 64x64.
					BufferedImage image = ImageIO.read(iconFile);
					BufferedImage resizedImage = resize(image, 64, 64);
					modLabel.setIcon(new ImageIcon(resizedImage));
				}
				else {
					System.err.println("\t\tIcon file not found; using default.");
				}
				

			} catch (Exception e) {
				System.err.println("\t\tFailed adding icon: " + cfgPath + File.separator + modIcon);
				modLabel.setIcon(new ImageIcon(ModList.class.getResource(defIcon)));
			}
			
			modPanel.add(modLabel);
			modsFoundTotal += 1;
			listDataArr.add(modPanel);
			System.out.println("Adding mod to list: " + this.getModTitle());
			
		} catch (IOException ex) {
			ex.printStackTrace();
			System.err.println("IOException here.");
		} finally {
			if (inputCFG != null) {
				try {
					inputCFG.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}