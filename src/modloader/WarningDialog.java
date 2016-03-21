package modloader;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class WarningDialog extends Dialog {

	protected Object result;
	protected Shell shell;
	private Button checkWarnExec, checkWarnShader, checkWarnConfig;
	public static boolean warnExec = true, warnShader = true, warnConfig = true;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public WarningDialog(Shell parent, int style) {
		super(parent, style);
		setText("Warnings");
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
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
		shell.setSize(260, 314);
		shell.setText("Warnings");
		
		Button buttonOK = new Button(shell, SWT.NONE);
		buttonOK.setBounds(90, 253, 74, 23);
		buttonOK.setText("OK");
		buttonOK.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				boolean[] warnings = {checkWarnExec.getSelection(), checkWarnShader.getSelection(), checkWarnConfig.getSelection()};
				Preferences.setWarns(warnings);
				shell.close();
			}
		});
		shell.setDefaultButton(buttonOK);
		
		Button buttonCancel = new Button(shell, SWT.NONE);
		buttonCancel.setBounds(170, 253, 74, 23);
		buttonCancel.setText("Cancel");
		buttonCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				shell.close();
			}
		});
		
		TabFolder tabFolder = new TabFolder(shell, SWT.NONE);
		tabFolder.setBounds(10, 10, 234, 237);
		
		TabItem tabGeneral = new TabItem(tabFolder, SWT.NONE);
		tabGeneral.setText("General");
		
		Composite composite = new Composite(tabFolder, SWT.NONE);
		tabGeneral.setControl(composite);
		
		Label labelWarnExec = new Label(composite, SWT.WRAP);
		labelWarnExec.setBounds(10, 10, 206, 28);
		labelWarnExec.setText("Display a warning when starting a mod that uses a custom executable.");
		
		checkWarnExec = new Button(composite, SWT.CHECK);
		checkWarnExec.setSelection(warnExec);
		checkWarnExec.setBounds(10, 44, 91, 16);
		checkWarnExec.setText("Show");
		
		Label labelWarnShader = new Label(composite, SWT.WRAP);
		labelWarnShader.setBounds(10, 66, 206, 39);
		labelWarnShader.setText("Display a warning when the Modloader is about to install custom shaders if they are found.");
		
		checkWarnShader = new Button(composite, SWT.CHECK);
		checkWarnShader.setSelection(warnShader);
		checkWarnShader.setBounds(10, 111, 91, 16);
		checkWarnShader.setText("Show");
		
		Label labelWarnConfig = new Label(composite, SWT.WRAP);
		labelWarnConfig.setBounds(10, 143, 206, 28);
		labelWarnConfig.setText("Display a warning before updating the config file (upon new releases).");
		
		checkWarnConfig = new Button(composite, SWT.CHECK);
		checkWarnConfig.setSelection(warnConfig);
		checkWarnConfig.setBounds(10, 177, 91, 16);
		checkWarnConfig.setText("Show");

	}
}
