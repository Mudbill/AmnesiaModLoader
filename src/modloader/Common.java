package modloader;

import java.awt.AWTException;
import java.awt.Desktop;
import java.awt.Robot;
import java.io.IOException;
import java.net.URL;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.atomic.AtomicLong;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Shell;

import com.sun.glass.events.KeyEvent;

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
		    	Log.error("Could not open website!", e);
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
	
	public static int getFileAmount(Path path) 
	{
		final AtomicLong files = new AtomicLong(0);
		
		try {
			Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
				
				@Override 
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
					if(CurrentOS.getSystem() == "MacOS") if(MainFrameOSX.abortRefresh) return null;
					else if(CurrentOS.getSystem() == "Windows") if(Engine.abortRefresh) return null;
					files.addAndGet(1);
					return FileVisitResult.CONTINUE;
				}

				@Override 
				public FileVisitResult visitFileFailed(Path file, IOException exc) {
					if(CurrentOS.getSystem() == "MacOS") if(MainFrameOSX.abortRefresh) return null;
					else if(CurrentOS.getSystem() == "Windows") if(Engine.abortRefresh) return null;
					Log.info("Skipped file traversing: " + file + " (" + exc + ")");
					return FileVisitResult.CONTINUE;
				}

				@Override 
				public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
					if(CurrentOS.getSystem() == "MacOS") if(MainFrameOSX.abortRefresh) return null;
					else if(CurrentOS.getSystem() == "Windows") if(Engine.abortRefresh) return null;
					if (exc != null)
						Log.warn("Had trouble traversing: " + dir + " (" + exc + ")");
					return FileVisitResult.CONTINUE;
				}
			});
		} catch (IOException e) {
			throw new AssertionError("walkFileTree will not throw IOException if the FileVisitor does not");
		} catch (NullPointerException e) {
			Log.info("Cancelled file scanning.");
		}
		return files.intValue();
	}
	
	/**
	 * Patch for SWT bug which displays hidden files by default on OS X. Manually inputs keyboard shortcut Shift+Cmd+Period to hide them.
	 */
	public static void toggleHiddenFiles() {
		if(CurrentOS.getSystem() != "MacOS") return;
		MainFrameOSX.getDisplay().asyncExec(new Runnable() {
			public void run() {
				try {
					Robot robot = new Robot();
					robot.keyPress(157);
					robot.keyPress(KeyEvent.VK_SHIFT);
					robot.keyPress(KeyEvent.VK_PERIOD);

					robot.keyRelease(157);
					robot.keyRelease(KeyEvent.VK_SHIFT);
					robot.keyRelease(KeyEvent.VK_PERIOD);
				} catch (AWTException e) {
					Log.error("", e);
				}	
			}
		});
	}
}
