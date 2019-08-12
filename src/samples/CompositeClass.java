package samples;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class CompositeClass {

public static void main (String[] args) {
	Display display = new Display();
	final Shell shell = new Shell(display);
	shell.setSize(300, 300);
	shell.setLayout(new FillLayout (SWT.BORDER));

	shell.setText("Composite Example");

	Composite composite = new Composite(shell, SWT.BORDER);
	GridLayout gridLayout = new GridLayout(2, false);

	composite.setLayout(gridLayout);

	for (int loopIndex = 0; loopIndex < 10; loopIndex++) {
		Label label = new Label(composite, SWT.SHADOW_NONE | SWT.BORDER);
		label.setText("Label " + loopIndex);
	}

	
	Composite composite2 = new Composite(shell, SWT.BORDER);
	composite2.setLayout(gridLayout);
	for (int loopIndex = 0; loopIndex < 10; loopIndex++) {
		Label label = new Label(composite2, SWT.SHADOW_NONE | SWT.BORDER);
		label.setText("Label " + loopIndex);
	}
	
	shell.open();
	while (!shell.isDisposed()) {if (!display.readAndDispatch())	display.sleep();}
	display.dispose();
}
}