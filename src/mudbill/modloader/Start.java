package mudbill.modloader;

import java.awt.EventQueue;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

public class Start {

	private static InputStream input = null;
	private static Properties settings = new Properties();
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
				    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				        if ("Nimbus".equals(info.getName())) {
				            UIManager.setLookAndFeel(info.getClassName());
				            break;
				        }
				    }
				} catch (Exception e) {}
				
				
				try {
					System.out.println("Starting application.");
					CurrentOS currentOS = new CurrentOS();
					currentOS.setOSValues();
					final Refresh refresh = new Refresh();
					
					String prefPath = CurrentOS.getSaveDir() + File.separator + "modloader.properties";
					File dir = new File(prefPath).getParentFile();
					dir.mkdir();
					input = new FileInputStream(prefPath);
					settings.load(input);
					String modDir = settings.getProperty("ModDir");
					
					MainFrame start = new MainFrame();
					start.setModDirectory(modDir);
					start.setupFrame();
					start.setVisible(true);
					refresh.displayRefreshWindow();

					EventQueue.invokeLater(new Runnable() {
						@Override
						public void run() {
							refresh.refreshList();
						}
					});
				
				} catch (FileNotFoundException e) {
					
					//If preferences file is not found, assumed first start. Will create a preferences window instead of main window.

					System.out.println("Preferences file not found. First startup?");
					Preferences prefs = new Preferences();
					prefs.displayPrefWindow();
					JOptionPane.showMessageDialog(prefs, "Please specify some settings before continuing.\nThese can be changed later by selecting the \"Preferences\" button.", "First time start", JOptionPane.INFORMATION_MESSAGE);
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (input != null) {
						try {
							input.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		});
	}
}
