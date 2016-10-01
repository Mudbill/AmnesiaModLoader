package modloader;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.wb.swt.SWTResourceManager;

public class ShellAboutWin32 extends Composite {

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public ShellAboutWin32(Composite parent, int style) {
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
		labelCreditsPeople.setFont(SWTResourceManager.getFont("Segoe UI", 8, SWT.NORMAL));
		labelCreditsPeople.setBounds(137, 37, 164, 39);
		labelCreditsPeople.setText("Mudbill, Daemian\r\nMrBehemoth, Kreekakon, Lazzer\r\nTraggey");
		
		Label labelHowTo = new Label(this, SWT.NONE);
		labelHowTo.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		labelHowTo.setForeground(SWTResourceManager.getColor(0, 0, 51));
		labelHowTo.setBounds(10, 82, 78, 21);
		labelHowTo.setText("How to use");
		
		Label labelHowToText = new Label(this, SWT.WRAP);
		labelHowToText.setFont(SWTResourceManager.getFont("Segoe UI", 8, SWT.NORMAL));
		labelHowToText.setBounds(20, 109, 450, 290);
		labelHowToText.setText("Edit any settings you'd like in the \"General\" and \"Advanced\" tab. Remember to use the Game Directory browser to find your game location. This should be the folder that contains your Amnesia.exe file. For Steam, it should be within your Steam Library folders. For retail, it should be within your Program Files (x86 for 64-bit OSes). You can also input a different Mod Directory if you wish, but in most cases you do not need to.\r\n\r\nOnce you accept the preferences, they will be saved to the directory the Modloader was installed to. Default is \"%appdata%\\Amnesia Modloader\".\r\n\r\nYou can hit the \"Scan for mods\" button in the top left of the main window to update the list of mods. If you have enabled the cache in the settings, then you will only need to rescan after installing a new mod.\r\n\r\nWhen the list of mods is available, you may select any you wish to start. After selecting a mod, some information will be displayed on the right side. If a mod creator has added a support for the Modloader, more entries will display. These include author, description, minimum version etc. It can also contain a custom icon which will show up in the list to the left. Click \"Launch mod\" at the bottom to start it.\r\n\r\nHave fun!\r\n");
		
		Label labelForDevs = new Label(this, SWT.NONE);
		labelForDevs.setForeground(SWTResourceManager.getColor(0, 0, 51));
		labelForDevs.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		labelForDevs.setBounds(10, 405, 105, 21);
		labelForDevs.setText("For Developers");
		
		Label labelForDevsText = new Label(this, SWT.WRAP);
		labelForDevsText.setFont(SWTResourceManager.getFont("Segoe UI", 8, SWT.NORMAL));
		labelForDevsText.setBounds(20, 432, 450, 39);
		labelForDevsText.setText("If you want to fully support this modloader for your mod (which I'd be very happy about) you can add an extra config file to your mod's /config folder. Updated instructions on how to do so are at the website. Click the button below.");
		
		Label separator = new Label(this, SWT.SEPARATOR | SWT.HORIZONTAL);
		separator.setBounds(10, 477, 460, 2);
		
		Label labelFooter = new Label(this, SWT.WRAP | SWT.CENTER);
		labelFooter.setFont(SWTResourceManager.getFont("Segoe UI", 8, SWT.NORMAL));
		labelFooter.setBounds(91, 485, 297, 39);
		labelFooter.setText("Please be on the lookout for issues and report them to me so I can fix them. Feature suggestions are also nice.");

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
