package modloader;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Button;

public class RefreshDialog extends Dialog {

	protected Object result;
	protected Shell shell;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public RefreshDialog(Shell parent, int style) {
		super(parent, style);
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		shell.open();
		shell.layout();
		//Display display = getParent().getDisplay();
		//while (!shell.isDisposed()) {
		//	if (!display.readAndDispatch()) {
		//		display.sleep();
		//	}
		//}
		return result;
	}
	
	/**
	 * Closes the dialog.
	 */
	public void close() {
		try {
			shell.close();
		} catch (Exception e) {
			
		}
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(getParent(), getStyle());
		shell.setImage(SWTResourceManager.getImage(RefreshDialog.class, "/resources/icon_refresh.png"));
		shell.setSize(314, 180);
		shell.setText("Refresh");
		
		Label labelRefresh = new Label(shell, SWT.NONE);
		labelRefresh.setBounds(10, 10, 288, 15);
		labelRefresh.setText("Updating list");
		
		Label labelModDir = new Label(shell, SWT.NONE);
		labelModDir.setBounds(10, 61, 288, 15);
		labelModDir.setText("Mod directory:");
		
		ProgressBar bar = new ProgressBar(shell, SWT.INDETERMINATE);
		bar.setBounds(10, 32, 288, 17);
		
		Label labelDir = new Label(shell, SWT.WRAP);
		labelDir.setBounds(10, 82, 288, 29);
		labelDir.setText(MainFrame.getModDirectory());
		
		Button btnCancel = new Button(shell, SWT.NONE);
		btnCancel.setBounds(223, 117, 75, 25);
		btnCancel.setText("Cancel");

	}
}
