package samples;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class SampleGrabExcess {
	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new GridLayout(2, false));
		
		Button button1 = new Button(shell, SWT.PUSH);
		button1.setText("B1");
		GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		button1.setLayoutData(gridData);

		new Button(shell, SWT.PUSH).setText("Wide Button 2");

		Button button3 = new Button(shell, SWT.PUSH);
		button3.setText("Button 3");
		gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.verticalSpan = 2;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		button3.setLayoutData(gridData);

		Button button4 = new Button(shell, SWT.PUSH);
		button4.setText("B4");
		gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		button4.setLayoutData(gridData);

		new Button(shell, SWT.PUSH).setText("Button 5");
				
		shell.pack();
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
	}
}