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
		//setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		
		Label labelCredits = new Label(this, SWT.NONE);
		labelCredits.setFont(SWTResourceManager.getFont("Lucida Grande", 12, SWT.NORMAL));
		labelCredits.setForeground(SWTResourceManager.getColor(0, 0, 51));
		labelCredits.setBounds(10, 10, 49, 21);
		labelCredits.setText("Credits");
		
		Label labelCreditsMain = new Label(this, SWT.NONE);
		labelCreditsMain.setFont(SWTResourceManager.getFont("Lucida Grande", 11, SWT.BOLD));
		labelCreditsMain.setAlignment(SWT.RIGHT);
		labelCreditsMain.setBounds(10, 37, 121, 42);
		labelCreditsMain.setText("Coding Development\r\nBeta Testing\r\nArtwork");
		
		Label labelCreditsPeople = new Label(this, SWT.NONE);
		labelCreditsPeople.setFont(SWTResourceManager.getFont("Lucida Grande", 11, SWT.NORMAL));
		labelCreditsPeople.setBounds(137, 37, 303, 42);
		labelCreditsPeople.setText("Mudbill, Daemian\r\nMrBehemoth, Kreekakon, Lazzer\r\nTraggey");
		
		Label labelHowTo = new Label(this, SWT.NONE);
		labelHowTo.setFont(SWTResourceManager.getFont("Lucida Grande", 12, SWT.NORMAL));
		labelHowTo.setForeground(SWTResourceManager.getColor(0, 0, 51));
		labelHowTo.setBounds(10, 85, 78, 21);
		labelHowTo.setText("How to use");
		
		Label labelHowToText = new Label(this, SWT.WRAP);
		labelHowToText.setFont(SWTResourceManager.getFont("Lucida Grande", 11, SWT.NORMAL));
		labelHowToText.setBounds(20, 112, 435, 314);
		labelHowToText.setText("When you open the program for the first time, you will be presented with this options window. Use the Game Directory browser to find your game location. This should be the folder that contains your Amnesia.app file. For Steam, it should be within your Steam Library folders. For retail, it should be within your Applications folder. Remember to select the \"redist\" folder if there is one. You can also input a different Mod Directory if you wish, but in most cases you do not need to.\r\n\r\nOnce you accept the options, they will be saved within the application package.\r\n\r\nYou will then see the main Modloader window. If the list is empty, click Refresh in the top left corner to update the list according on your mod directory. This might take some time depending on the size of your folder and the speed of your computer.\r\n\r\nWhen the list of mods is available, you may select any you wish to start. After selecting a mod, some information will be displayed on the right side. If a mod creator has added a modloader.cfg file to their mod, more entries will display. These include author, description, minimum versio, custom shaders and more. It can also contain a custom icon which will show up in the list to the left. Click Launch Mod at the bottom to start it.\r\n");
		
		Label labelDev = new Label(this, SWT.NONE);
		labelDev.setFont(SWTResourceManager.getFont("Lucida Grande", 12, SWT.NORMAL));
		labelDev.setForeground(SWTResourceManager.getColor(0, 0, 51));
		labelDev.setBounds(10, 432, 105, 21);
		labelDev.setText("For Developers");
		
		Label labelDevText = new Label(this, SWT.WRAP);
		labelDevText.setFont(SWTResourceManager.getFont("Lucida Grande", 11, SWT.NORMAL));
		labelDevText.setBounds(20, 459, 435, 47);
		labelDevText.setText("If you want to fully support this modloader for your mod (which I'd be very happy about) you can add an extra config file to your mod's /config folder. Updated instructions on how to do so are at the website. Click the button below.");
		
		Label sep = new Label(this, SWT.SEPARATOR | SWT.HORIZONTAL);
		sep.setBounds(10, 512, 430, 2);
		
		Label labelFooter = new Label(this, SWT.WRAP | SWT.CENTER);
		labelFooter.setFont(SWTResourceManager.getFont("Lucida Grande", 11, SWT.NORMAL));
		labelFooter.setBounds(73, 520, 319, 39);
		labelFooter.setText("Please be on the lookout for issues and report them to me so I can fix them. Feature suggestions are also nice.");

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
