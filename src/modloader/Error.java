package modloader;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

public class Error {
	
	private String boxText;
	
	public Error() {
		
		boxText = "An error occurred. Check the log for details:\n\n" + CurrentOS.getSaveDir() + File.separator + CurrentOS.getLogName();
		
		MessageBox m = new MessageBox(new Shell(), SWT.ICON_ERROR | SWT.OK);
		m.setMessage(boxText);
		m.setText("Error");
		m.open();
	}
	
	public Error(String warningMessage) {
		this.boxText = warningMessage;
		
		if(boxText == null) boxText = "An error occurred. Check the log for details:\n\n" + CurrentOS.getSaveDir() + File.separator + CurrentOS.getLogName();
		
		MessageBox m = new MessageBox(new Shell(), SWT.ICON_ERROR | SWT.OK);
		m.setMessage(boxText);
		m.setText("Error");
		m.open();
	}
}
