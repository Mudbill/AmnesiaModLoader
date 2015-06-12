package modloader;

import java.awt.Desktop;
import java.net.URL;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Shell;

public class Common {

	public Common() { }
	
	/**
	 * Scales an image to the specified icon size.
	 * @param image
	 * @param size
	 * @return
	 */
	public static Image scale(Image image, int size) {
		Image newImage = new Image(image.getDevice(), size, size);
		GC gc = new GC(newImage);
		gc.setAdvanced(true);
		gc.setAntialias(SWT.ON);
		gc.drawImage(image, 0, 0, image.getBounds().width, image.getBounds().height, 0, 0, size, size);
		gc.dispose();
		return newImage;
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
		        Log.error(e);
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
