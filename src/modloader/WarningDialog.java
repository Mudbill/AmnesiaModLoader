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
	private Button checkWarnExec, checkWarnShader;
	public static boolean warnExec = true, warnShader = true;

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
		shell.setSize(260, 300);
		shell.setText("Warnings");
		
		Button buttonOK = new Button(shell, SWT.NONE);
		buttonOK.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				boolean[] warnings = {checkWarnExec.getSelection(), checkWarnShader.getSelection()};
				Preferences.setWarns(warnings);
				shell.close();
			}
		});
		buttonOK.setBounds(90, 239, 74, 23);
		buttonOK.setText("OK");
		shell.setDefaultButton(buttonOK);
		
		Button buttonCancel = new Button(shell, SWT.NONE);
		buttonCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				shell.close();
			}
		});
		buttonCancel.setBounds(170, 239, 74, 23);
		buttonCancel.setText("Cancel");
		
		TabFolder tabFolder = new TabFolder(shell, SWT.NONE);
		tabFolder.setBounds(10, 10, 234, 223);
		
		TabItem tabGeneral = new TabItem(tabFolder, SWT.NONE);
		tabGeneral.setText("General");
		
		Composite composite = new Composite(tabFolder, SWT.NONE);
		tabGeneral.setControl(composite);
		
		Label labelWarnExec = new Label(composite, SWT.WRAP);
		labelWarnExec.setBounds(10, 23, 206, 28);
		labelWarnExec.setText("Display a warning when starting a mod that uses a custom executable.");
		
		checkWarnExec = new Button(composite, SWT.CHECK);
		checkWarnExec.setSelection(warnExec);
		checkWarnExec.setBounds(10, 57, 91, 16);
		checkWarnExec.setText("Show");
		
		Label labelWarnShader = new Label(composite, SWT.WRAP);
		labelWarnShader.setBounds(10, 105, 206, 40);
		labelWarnShader.setText("Display a warning when the Modloader is about to install custom shaders if they are found.");
		
		checkWarnShader = new Button(composite, SWT.CHECK);
		checkWarnShader.setSelection(warnShader);
		checkWarnShader.setBounds(10, 151, 91, 16);
		checkWarnShader.setText("Show");

	}
}
