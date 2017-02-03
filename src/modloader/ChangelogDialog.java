package modloader;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

public class ChangelogDialog extends Dialog {

	protected Object result;
	protected Shell shell;
	private Text textChangelog;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public ChangelogDialog(Shell parent, int style) {
		super(parent, style);
		setText("SWT Dialog");
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		Engine.scaleControl(shell);
		Engine.scaleToDPI(shell);
		Common.center(shell);
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(getParent(), getStyle());
		shell.setSize(500, 500);
		shell.setText("Update");
		Common.center(shell);
		
		Label labelChangelog = new Label(shell, SWT.NONE);
		labelChangelog.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		labelChangelog.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		labelChangelog.setBounds(10, 10, 145, 21);
		labelChangelog.setText("Changelog");
		
		Label labelVersion = new Label(shell, SWT.RIGHT);
		labelVersion.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		labelVersion.setBounds(302, 10, 182, 21);
		labelVersion.setText("Version " + Engine.getVersion());
		
		Label separator = new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL);
		separator.setBounds(10, 37, 474, 2);
		
		textChangelog = new Text(shell, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		textChangelog.setFont(SWTResourceManager.getFont("Lucida Console", 8, SWT.NORMAL));
		textChangelog.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		textChangelog.setBounds(10, 97, 474, 237);
		
		InputStream is = ChangelogDialog.class.getResourceAsStream("/resources/changelog.txt");
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		try {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();
			while(line != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
				line = br.readLine();
			}
			textChangelog.setText(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Label labelReset = new Label(shell, SWT.WRAP);
		labelReset.setFont(SWTResourceManager.getFont("Segoe UI", 8, SWT.NORMAL));
		labelReset.setBounds(10, 340, 474, 58);
		labelReset.setText("Sometimes it could be a good idea to clear out the old preferences when using a new version in case there are any conflicts, but also to not miss out on certain new features. If you wish to clear your old settings, hit the button here. Otherwise, carry on as normal.");
		
		Button buttonReset = new Button(shell, SWT.NONE);
		buttonReset.setEnabled(false);
		buttonReset.setImage(SWTResourceManager.getImage(ChangelogDialog.class, "/resources/icon_reload.png"));
		buttonReset.setBounds(10, 404, 232, 58);
		buttonReset.setText("Reset preferences");
		buttonReset.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				
				buttonReset.setEnabled(!ConfigManager.clearPreferences());
			}
		});
		
		Button buttonContinue = new Button(shell, SWT.RIGHT_TO_LEFT);
		buttonContinue.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				shell.close();
			}
		});
		buttonContinue.setImage(SWTResourceManager.getImage(ChangelogDialog.class, "/resources/icon_arrow_right.png"));
		buttonContinue.setBounds(252, 404, 232, 58);
		buttonContinue.setText("Continue");
		
		Label labelFirst = new Label(shell, SWT.WRAP);
		labelFirst.setFont(SWTResourceManager.getFont("Segoe UI", 8, SWT.NORMAL));
		labelFirst.setBounds(10, 45, 474, 46);
		labelFirst.setText("It's changelog time! \r\nRead below to get up-to-date with any changes in this version. As always, thank you for using this application. If you have any feedback, please don't hesitate to contact me.");

		if (new File(Engine.prefPath).exists() || 
			new File(Engine.portPath).exists() || 
			new File(CurrentOS.getSaveDir() + File.separator + CurrentOS.getLogName()).exists() || 
			new File(CurrentOS.getSaveDir() + File.separator + "cache").exists()) {
				buttonReset.setEnabled(true);
		}
	}
}
