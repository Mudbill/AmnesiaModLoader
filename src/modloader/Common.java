package modloader;

import java.awt.Desktop;
import java.net.URL;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class Common {

	public Common() { }
	
	/**
	 * Scales an image to the spcified icon size.
	 * @param image
	 * @param size
	 * @return
	 */
	public static Image scaleImage(Image image, int size) {
	    ImageData data = image.getImageData();

	    // Image resize
	    data = data.scaledTo(size, size);
	    Image scaled = new Image(Display.getDefault(), data);
	    //image.dispose(); //Crashes if uncommented. Causes a slight memory leak as it is. You can notice it in the Task Manager if you drag the icon size slider a lot.
	    return scaled;
	}
	
	/**
	 * Opens a URL website in the default browser.
	 * @param url
	 */
	public static void openWebpage(URL url) {
	    Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
		if(desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
			try {
		        desktop.browse(url.toURI());
		    } catch (Exception e) {
		    	Log.error("Could not open website!");
		        e.printStackTrace();
		    }
		}
	}
	
	
	/**
	 * Positions a Shell at the center of the screen.
	 * @param shell = the Shell to center.
	 */
	public static void center(Shell shell) 
	{
        Rectangle bds = shell.getDisplay().getPrimaryMonitor().getBounds();
        Point p = shell.getSize();

        int nLeft = (bds.width - p.x) / 2;
        int nTop = (bds.height - p.y) / 2;

        shell.setBounds(nLeft, nTop, p.x, p.y);
    }
}
