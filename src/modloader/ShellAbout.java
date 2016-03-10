package modloader;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.wb.swt.SWTResourceManager;

public class ShellAbout extends Composite {

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public ShellAbout(Composite parent, int style) {
		super(parent, style);
		
		Label labelCredits = new Label(this, SWT.NONE);
		labelCredits.setForeground(SWTResourceManager.getColor(0, 0, 51));
		labelCredits.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		labelCredits.setBounds(10, 10, 49, 21);
		labelCredits.setText("Credits");
		
		Label labelCreditsMain = new Label(this, SWT.NONE);
		labelCreditsMain.setFont(SWTResourceManager.getFont("Segoe UI", 8, SWT.BOLD));
		labelCreditsMain.setAlignment(SWT.RIGHT);
		labelCreditsMain.setBounds(10, 37, 121, 39);
		labelCreditsMain.setText("Coding Development\r\nBeta Testing\r\nArtwork");
		
		Label labelCreditsPeople = new Label(this, SWT.NONE);
		labelCreditsPeople.setBounds(137, 37, 164, 39);
		labelCreditsPeople.setText("Mudbill, Daemian\r\nMrBehemoth, Kreekakon, Lazzer\r\nTraggey");
		
		Label labelHowTo = new Label(this, SWT.NONE);
		labelHowTo.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		labelHowTo.setForeground(SWTResourceManager.getColor(0, 0, 51));
		labelHowTo.setBounds(10, 82, 78, 21);
		labelHowTo.setText("How to use");
		
		Label labelHowToText = new Label(this, SWT.WRAP);
		labelHowToText.setBounds(20, 109, 450, 290);
		labelHowToText.setText("When you open the program for the first time, you will be presented with this preference window. Use the Game Directory browser to find your game location. This should be the folder that contains your Amnesia.exe file. For Steam, it should be within your Steam Library folders. For retail, it should be within your Program Files (x86 for 64-bit OSes). Remember to select the \"redist\" folder if there is one. Steam does not use one. You can also input a different Mod Directory if you wish, but in most cases you do not need to.\r\n\r\nOnce you accept the preferences, they will be saved to your Documents\\Amnesia\\Modloader folder.\r\n\r\nYou will then see the main Modloader window. If the list is empty, click Refresh in the top left corner to update the list according on your mod directory. This might take some time depending on the size of your folder and the speed of your computer.\r\n\r\nWhen the list of mods is available, you may select any you wish to start. After selecting a mod, some information will be displayed on the right side. If a mod creator has added a modloader.cfg file to their mod, more entries will display. These include author, description, minimum version and custom shaders. It can also contain a custom icon which will show up in the list to the left. Click Launch Mod at the bottom to start it. Alternatively, you can hit the arrow on the button or right click the mod in the list to specify whether you want to start the launcher or the game directly. \r\n\r\n");
		
		Label lblForDevelopers = new Label(this, SWT.NONE);
		lblForDevelopers.setForeground(SWTResourceManager.getColor(0, 0, 51));
		lblForDevelopers.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		lblForDevelopers.setBounds(10, 405, 105, 21);
		lblForDevelopers.setText("For Developers");
		
		Label lblNewLabel = new Label(this, SWT.WRAP);
		lblNewLabel.setBounds(20, 432, 450, 39);
		lblNewLabel.setText("If you want to fully support this modloader for your mod (which I'd be very happy about) you can add an extra config file to your mod's /config folder. Updated instructions on how to do so are at the website. Click the button below.");
		
		Label label = new Label(this, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setBounds(10, 477, 460, 2);
		
		Label lblPleaseBeOn = new Label(this, SWT.WRAP | SWT.CENTER);
		lblPleaseBeOn.setFont(SWTResourceManager.getFont("Segoe UI", 8, SWT.NORMAL));
		lblPleaseBeOn.setBounds(91, 485, 297, 39);
		lblPleaseBeOn.setText("Please be on the lookout for issues and report them to me so I can fix them. Feature suggestions are also nice.");

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
