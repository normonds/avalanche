package samples;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class ImageEmpty {
  public static void main(String[] args) {
    final Display display = new Display();
    final Shell shell = new Shell(display);
    shell.setText("Canvas Example");
    shell.setLayout(new GridLayout(2,false));

    Canvas canvas = new Canvas(shell, SWT.BORDER);
   
   // canvas.setSize(20, 20);
    //canvas.setLocation(20, 20);
    GridData canvasGridData = new GridData();
    canvasGridData.widthHint = 200;
    canvasGridData.heightHint = 200;
	//canvasGridData.grabExcessHorizontalSpace = true;
	//canvasGridData.horizontalAlignment = GridData.FILL;
	canvas.setLayoutData(canvasGridData);
    canvas.addPaintListener(new PaintListener() {
      public boolean	sizeSet;

	public void paintControl(PaintEvent e) {
    	//if (!sizeSet) {((Canvas)e.getSource()).setSize(25,25);sizeSet=true;}
       /* Image image = new Image(display, 300, 200);
        GC gc = new GC(image);
        gc.drawLine(0,0,200,200);
        gc.dispose();

        e.gc.drawImage(image, 10, 10);
        image.dispose();*/
    	//System.out.println(((Canvas)e.getSource()).getSize());
    	//System.out.println(canvas.getBounds());
        e.gc.drawLine(0,0,((Canvas)e.getSource()).getSize().x,((Canvas)e.getSource()).getSize().y); 
        e.gc.dispose();
      }
    });

    shell.open();
    while (!shell.isDisposed()) {
      if (!display.readAndDispatch()) {
        display.sleep();
      }
    }
    display.dispose();
  }
}