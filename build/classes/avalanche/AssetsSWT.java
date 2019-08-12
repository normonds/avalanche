package avalanche;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.Arrays;

import mi.poker.calculation.EquityCalculation;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

public class AssetsSWT {
public static AssetSWTinfoRow firstTableInfo;

public static void createContents (Shell shell) {
	// Create the containing tab folder
	
	GridLayout gridLayout = new GridLayout(2, true);
	gridLayout.marginWidth = 0;
	gridLayout.marginHeight = 0;
	gridLayout.verticalSpacing = 2;
	//gridLayout.numColumns = 1;
	gridLayout.makeColumnsEqualWidth = false;
	GridData gridData = new GridData(GridData.FILL_BOTH);
	//gridData.heightHint = 300;
	gridData.horizontalSpan = 2;
	
	GridData gridData2 = new GridData(GridData.FILL_HORIZONTAL);
	gridData2.horizontalAlignment = GridData.FILL;
	gridData2.grabExcessHorizontalSpace = true;
	//gridData2.grabExcessVerticalSpace = true;
	gridData2.heightHint = 100;
	//gridData2.widthHint = 800;
	
	GridData gridData3 = new GridData(GridData.END);
	gridData3.horizontalAlignment = GridData.BEGINNING;
	//gridData3.grabExcessHorizontalSpace = true;
	gridData3.verticalAlignment = GridData.BEGINNING;
	gridData3.widthHint = 100;
	//gridData.
	//gridLayout.
	shell.setLayout(gridLayout);
	
//	new Button(shell, SWT.PUSH).setText("B1");
//	new Button(shell, SWT.PUSH).setText("Wide Button 2");
//	new Button(shell, SWT.PUSH).setText("Button 3");
//	new Button(shell, SWT.PUSH).setText("B4");
//	new Button(shell, SWT.PUSH).setText("Button 5");
	
	final TabFolder tabFolder = new TabFolder(shell, SWT.WRAP);

	//tabFolder.setLayoutData(gridData);
	tabFolder.setLayoutData(gridData);
	Avalanche.console = new StyledText(shell, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
	Avalanche.console.setLayoutData(gridData2);
	Avalanche.console.setText("hellld aw awd awd adawllo");
	
	Avalanche.statuss = new Text(shell, SWT.BORDER);
	Avalanche.statuss.setLayoutData(gridData3);
	//statuss.setSize(200, 200);
	Avalanche.statuss.setText("Idle");
	//shell.setControl(getTabTwoControl(tabFolder));
	// Create each tab and set its text, tool tip text,
	// image, and control
	TabItem one = new TabItem(tabFolder, SWT.NONE);
	one.setText("one");
	one.setToolTipText("This is tab one");
	//one.setImage(circle);
	one.setControl(getTabThreeControl(tabFolder));

//	TabItem two = new TabItem(tabFolder, SWT.NONE);
//	two.setText("two");
//	two.setToolTipText("This is tab two");
//	two.setImage(square);
//	two.setControl(getTabTwoControl(tabFolder));

	TabItem three = new TabItem(tabFolder, SWT.NONE);
	three.setText("three");
	three.setToolTipText("This is tab three");
	//three.setImage(triangle);
	three.setControl(getTabOneControl(tabFolder));

	TabItem four = new TabItem(tabFolder, SWT.NONE);
	four.setText("four");
	four.setToolTipText("This is tab four");
	//four.setImage(star);
	// Select the third tab (index is zero-based)
	tabFolder.setSelection(0);

	// Add an event listener to write the selected tab to stdout
	tabFolder.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected (org.eclipse.swt.events.SelectionEvent event) {
			Avalanche.trace(tabFolder.getSelection()[0].getText()	+ " selected");
		}
	});
}
public static void addTableInfoRow (Composite wrapComp, GridLayout wrapCompLayout) {
//	GridLayout topLayout = new GridLayout(5, false);
//	topLayout.marginHeight = 0;	topLayout.marginWidth = 0;
//	topLayout.horizontalSpacing = 0;

}
public static Control getTabThreeControl (TabFolder tabFolder) {
	// Create some labels and text fields
	GridLayout wrapCompLayout = new GridLayout(3, false);
	wrapCompLayout.verticalSpacing = 0; wrapCompLayout.horizontalSpacing = 0;
	wrapCompLayout.marginHeight = 0;
	wrapCompLayout.marginWidth = 0;

	Composite wrapComp = new Composite(tabFolder, SWT.NONE);
	wrapComp.setLayout(wrapCompLayout);
	
	GridData topCompGridData = new GridData();
	topCompGridData.horizontalSpan = wrapCompLayout.numColumns; // fill all row
	topCompGridData.grabExcessHorizontalSpace = true;
	topCompGridData.horizontalAlignment = GridData.FILL;
	
	GridLayout topLayout = new GridLayout(7, false);
	topLayout.marginHeight = 0;	topLayout.marginWidth = 0;
	topLayout.horizontalSpacing = 0;
	//topLayout.justify = true;
	
	Composite topComp = new Composite(wrapComp, SWT.NONE);
	topComp.setLayoutData(topCompGridData);
	topComp.setLayout(topLayout);
	
	firstTableInfo = new AssetSWTinfoRow(wrapComp, wrapCompLayout);
	
	final Button pause = new Button(topComp, SWT.NONE);
	pause.setFont(new Font(Avalanche.shell.getDisplay(), "Trebuchet MS", 16, SWT.NORMAL));
	pause.setText("||");
	pause.addSelectionListener(new SelectionListener() {
		public void widgetSelected (SelectionEvent event) {/*Avalanche.trace("widgetSelected");*/
			Avalanche.pause = !Avalanche.pause;
			pause.setText(Avalanche.pause?">":"||");
		}
		public void widgetDefaultSelected (SelectionEvent event) {/*Avalanche.trace("widgetDefaultSelected");*/}
	});
	//but.setImage(Avalanche.display.getSystemImage(SWT.ICON_WARNING));
	
	Avalanche.equityLabel = new Label(topComp, SWT.NONE);
	Avalanche.equityLabel.setText("Eq:-0.000% 0.00%  ");
	Avalanche.equityLabel.setFont(new Font(Avalanche.shell.getDisplay(), "Trebuchet MS", 20, SWT.NORMAL));
	
	
	Avalanche.opponLabel = new Label(topComp, SWT.NONE);
	Avalanche.opponLabel.setText("O:-1 ");
	Avalanche.opponLabel.setFont(new Font(Avalanche.shell.getDisplay(), "Trebuchet MS", 20, SWT.NORMAL));
	
	Avalanche.cardsLabel = new Label(topComp, SWT.NONE);
	GridData equityGridData = new GridData();
	equityGridData.grabExcessHorizontalSpace = true;
	Avalanche.cardsLabel.setText("0♦0♥ Y♠Y♣Y♠Y♣Y♠ 0000");
	Avalanche.cardsLabel.setFont(new Font(Avalanche.shell.getDisplay(), "Trebuchet MS", 20, SWT.NORMAL));
	Avalanche.cardsLabel.setLayoutData(equityGridData);
	
	
	//equityLabel.setLayoutData(rowlayout);
	//label.setSize(30);
	//new Text(composite, SWT.BORDER);
	//new Label(composite, SWT.RIGHT).setText("Label Two:");
	//new Text(composite, SWT.BORDER);
	
	
	Button but9 = new Button(topComp, SWT.NONE );
	but9.setFont(new Font(Avalanche.shell.getDisplay(), "Trebuchet MS", 16, SWT.NORMAL));
	but9.setText("9");
	//but9.setImage(Avalanche.display.getSystemImage(SWT.ICON_INFORMATION));
	but9.addSelectionListener(new SelectionListener() {
		public void widgetSelected (SelectionEvent event) { Avalanche.trace("widgetSelected");
			Avalanche.createAssets("pkst.9players.json");}
		public void widgetDefaultSelected (SelectionEvent event) { Avalanche.trace("widgetDefaultSelected");}
	});
	
	Button but6 = new Button(topComp, SWT.NONE  | SWT.PUSH);
	but6.setFont(new Font(Avalanche.shell.getDisplay(), "Trebuchet MS", 16, SWT.NORMAL));
	but6.setText("6");
	//but2.setImage(Avalanche.display.getSystemImage(SWT.ICON_INFORMATION));
	but6.addSelectionListener(new SelectionListener() {
		public void widgetSelected (SelectionEvent event) { Avalanche.trace("widgetSelected");
			Avalanche.createAssets("pkst.6players.json");}
		public void widgetDefaultSelected (SelectionEvent event) { Avalanche.trace("widgetDefaultSelected");}
	});
	
	Avalanche.botComp = new Composite(wrapComp, SWT.NONE);
	GridData botCompGridData = new GridData();
	botCompGridData.grabExcessVerticalSpace = true;
	botCompGridData.grabExcessHorizontalSpace = true;
	botCompGridData.horizontalAlignment = GridData.FILL;
	botCompGridData.verticalAlignment = GridData.FILL;
	Avalanche.botComp.setLayoutData(botCompGridData);
	GridLayout botLayout = new GridLayout(8,false);
	botLayout.verticalSpacing = 0;
	botLayout.horizontalSpacing = 0;
	botLayout.marginHeight = 0;
	botLayout.marginWidth = 0;
	Avalanche.botComp.setLayout(botLayout);
	
	return wrapComp;
}
public static Object[] addImgComp (String text) {	return addImgComp(text, Avalanche.defaultSWTGridItemWidth, 90, 1);}
public static Object[] addImgComp (String text, int w, int h, int span) {
	Composite comp = new Composite(Avalanche.botComp, SWT.NONE);
	GridData compGridData = new GridData();
	compGridData.heightHint = 120;
	compGridData.widthHint = w;
	compGridData.horizontalSpan = span;
	comp.setLayoutData(compGridData);
	GridLayout layout = new GridLayout(1,false);
	layout.marginHeight = 0;
	layout.marginWidth = 0;
	layout.horizontalSpacing = 0;
	layout.verticalSpacing = 0;
	comp.setLayout(layout);
	
	Canvas canvas = new Canvas(comp, SWT.BORDER);
	GridData canvasGridData = new GridData();
	canvasGridData.grabExcessHorizontalSpace = true;
	canvasGridData.horizontalAlignment = GridData.FILL;
	canvasGridData.heightHint = 80;
	//canvasGridData.verti
	canvas.setLayoutData(canvasGridData);
	//canvas.setSize(50, 20);
	
	/*canvas.addPaintListener(new PaintListener() {
		public void paintControl (PaintEvent e) {
                if (canvas.getData()!=null) {
				e.gc.drawImage((Image)comp.getData(), 0, 0);
				Avalanche.trace("getData");
			}

			e.gc.drawLine(0,0,((Canvas)e.getSource()).getSize().x,((Canvas)e.getSource()).getSize().y);
			e.gc.dispose();
			//EngineLogical.updateLabels();
			//e.gc.drawLine(0,0,10,10); 
		}
	});*/
	
	/*Button but = new Button(comp, SWT.NONE);
	but.setFont(new Font(shell.getDisplay(), "Trebuchet MS", 16, SWT.NORMAL));
	but.setText(text);
	GridData lblCompGridData = new GridData();
	lblCompGridData.grabExcessHorizontalSpace = true;
	lblCompGridData.horizontalAlignment = GridData.FILL;
	but.setLayoutData(lblCompGridData);*/
	
	Text txt = new Text(comp, SWT.BORDER | SWT.MULTI | SWT.WRAP);
	GridData txtCompGridData = new GridData();
	txtCompGridData.grabExcessVerticalSpace = true;
	txtCompGridData.grabExcessHorizontalSpace = true;
	txtCompGridData.horizontalAlignment = GridData.FILL;
	txtCompGridData.verticalAlignment = GridData.FILL;
	//botCompGridData.verticalAlignment = GridData.FILL;
	txt.setLayoutData(txtCompGridData);
	//txt.setSize(100,100);
	txt.setText(text);
	//but.setImage(display.getSystemImage(SWT.ICON_WARNING));
	//return comp;
	return new Object[]{canvas, txt};
}
public static Control getTabOneControl (TabFolder tabFolder) {
	// Create a composite and add four buttons to it
	Composite composite = new Composite(tabFolder, SWT.NONE);
	composite.setLayout(new FillLayout(SWT.VERTICAL));
	new Button(composite, SWT.PUSH).setText("Button one");
	new Button(composite, SWT.PUSH).setText("Button two");
	new Button(composite, SWT.PUSH).setText("Button three");
	new Button(composite, SWT.PUSH).setText("Button four");
	return composite;
}

private Control getTabTwoControl (TabFolder tabFolder) {
	// Create a multi-line text field
	Avalanche.console = new StyledText(tabFolder, SWT.BORDER | SWT.MULTI | SWT.WRAP);
	Avalanche.console.setText("helllllo");
	return Avalanche.console;
}
public static void generate () {
	int n = 0;
	int pair = 0;
	String outStr = "";
	String addStr = "";//",34.344,34.344,34.344,34.344,34.344,34.344,34.344,34.344";
	String[] cards = new String[]{"A","K","Q","J","T","9","8","7","6","5","4","3","2"};
	for (int a=0;a<cards.length;a++) {
		for (int b=Arrays.asList(cards).indexOf(cards[a]);b<cards.length;b++) {
			n++;
			if (cards[a]==cards[b]) {
				pair++;
			}
			System.out.println(cards[a]+cards[b]+"");
			writeLine("filename.txt", gen9player(cards[a]+cards[b]));
			//outStr += ;
			//outStr += (cards[a]+cards[b]+""+addStr+"\n");
		}
	}
	for (int a=0;a<cards.length;a++) {
		for (int b=Arrays.asList(cards).indexOf(cards[a]);b<cards.length;b++) {
			if (cards[a]!=cards[b]) {
				System.out.println(cards[a]+cards[b]+"o");
				writeLine("filename.txt", gen9player(cards[a]+cards[b]+"o"));
				//outStr += (cards[a]+cards[b]+"o"+addStr+"\n");
				n++;
				//pair++;
			}
		}
	}
	
	//out.write(arg0)
	//System.out.println(outStr);
	System.out.println("loops:"+" ("+n+")");
	System.out.println("pair:"+pair);
	//System.out.println(gen9player("AA"));
}
public static void writeLine (String filename, String toWrite) {
	String readfile = "";
	try {
        FileReader fileReader = new FileReader(filename);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line;
		while((line = bufferedReader.readLine()) != null) {		readfile +=(line)+"\n";       }
        bufferedReader.close();
    } catch (FileNotFoundException ex) {        System.out.println("Unable to open file");                 }
    catch (IOException ex) {       System.out.println("Error reading file");                }
	PrintWriter out = null;
	try {
		out = new PrintWriter(filename);
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	}
	out.write(readfile+toWrite);
	//System.out.println("writing"+readfile+"\n"+toWrite);
	out.close();
}
public static String gen9player (String inc) {
	String ops = "";
	String ret = "";
	//DecimalFormat				df			= new DecimalFormat("###.###")
	for (int n=0;n<9;n++) {
		ops+=",*";
		try {
			ret += ","+Avalanche.df.format(EquityCalculation.calculateMonteCarlo(inc+ops,"","").getHandInfo(0).getEquity()*100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	return inc+ret+"\n";
}

}