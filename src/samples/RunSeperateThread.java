package samples;
import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class RunSeperateThread {

	public static void main (String[] args) {
		final Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setLayout(new GridLayout());
		final Text text = new Text(shell, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL);
		text.setLayoutData(new GridData(GridData.FILL_BOTH));
		final int[] nextId = new int[1];
		Button b = new Button(shell, SWT.PUSH);
		b.setText("invoke long running job");
		b.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Runnable longJob = new Runnable() {
					boolean done = false;
					int id;
					@Override
					public void run() {
						Thread thread = new Thread(new Runnable() {
							@Override
							public void run() {
								id = nextId[0]++;
								display.syncExec(new Runnable() {
									@Override
									public void run() {
										if (text.isDisposed()) return;
										text.append("\nStart long running task "+id);
									}
								});
								for (int i = 0; i < 100000; i++) {
									if (display.isDisposed()) return;
									System.out.println("do task that takes a long time in a separate thread "+id);
								}
								if (display.isDisposed()) return;
								display.syncExec(new Runnable() {
									@Override
									public void run() {
										if (text.isDisposed()) return;
										text.append("\nCompleted long running task "+id);
									}
								});
								done = true;
								display.wake();
							}
						});
						thread.start();
						while (!done && !shell.isDisposed()) {
							if (!display.readAndDispatch())
								display.sleep();
						}
					}
				};
				BusyIndicator.showWhile(display, longJob);
			}
		});
		shell.setSize(250, 150);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}