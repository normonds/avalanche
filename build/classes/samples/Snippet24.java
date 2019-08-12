package samples;
/*
 * example snippet: detect CR in a text or combo control (default selection)
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.*;

public class Snippet24 {

public static Shell shell;
public static Combo combo;
public static Text text;
public static void refresh () {
	combo.dispose();
	text.dispose();
//	for (Control control : botComp.getChildren()) {
//			control.addDisposeListener(new DisposeListener() {
//	            public void widgetDisposed(DisposeEvent e)
//	            {System.out.println("widget disposed");
//	                //color.dispose();
//	            }
//	        });
//	        control.dispose();
//	    }
	createAssets();
	shell.layout(true, true);
}
public static void createAssets () {
	combo = new Combo (shell, SWT.NONE);
	combo.setItems (new String [] {"A-1", "B-1", "C-1"});
	text = new Text (shell, SWT.SINGLE | SWT.BORDER);
	text.setText (Double.toString(Math.random()));
	combo.addListener (SWT.DefaultSelection, new Listener () {
		@Override
		public void handleEvent (Event e) {
			System.out.println (e.widget + " - Default Selection");
		}
	});
	text.addListener (SWT.DefaultSelection, new Listener () {
		@Override
		public void handleEvent (Event e) {
			System.out.println (e.widget + " - Default Selection");
		}
	});
	text.addListener (SWT.MouseDoubleClick, new Listener () {
		@Override
		public void handleEvent (Event e) {
			System.out.println (e.widget + " - Double Click");
			refresh();
		}
	});
}
public static void main (String [] args) {
	Display display = new Display ();
	shell = new Shell (display);
	shell.setLayout (new RowLayout ());
	
	createAssets();
	
	shell.pack ();
	shell.open ();
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}
} 