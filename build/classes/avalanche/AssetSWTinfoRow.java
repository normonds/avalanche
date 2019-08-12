package avalanche;

import nooni.events.Eve;
import nooni.events.EveListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class AssetSWTinfoRow {
public Label straightFlush, quads, fullHouse, flush, straight, threes, twoPair, pair;

public EveListener boardChange = new EveListener () { 		@Override public void onEvent (Eve e) {//Event clientEve=(Event) e;
	System.out.println("boardChange, board:"+EngineLogical.board.hasCards());
	//straightFlush.setText(null);
	//Avalanche.clearEquityLabel("boardChange");
	if (EngineLogical.board.hasCards()) {
		straightFlush.setText(EngineLogical.me.validHand + " " + EngineLogical.board.validCards);
	}
	else straightFlush.setText("");
	// History.two += "BRD-"+board.validCards+";";//kettle:"+board.pot+";";
	//	trace("listener:board change "+board.rawCards);
	//	calculateEquity();
}};
public AssetSWTinfoRow (Composite wrapComp, GridLayout wrapCompLayout) {
	//EngineLogical.dispatcher.addEventListener(null, null);
	EngineLogical.dispatcher.addEventListener(AvalEvent.CHANGE_BOARD, boardChange);
	FillLayout fillLayout = new FillLayout();
	fillLayout.type = SWT.VERTICAL;
	//shell.setLayout(fillLayout);

	GridData topCompTableGridData = new GridData();
	topCompTableGridData.horizontalSpan = wrapCompLayout.numColumns; // fill all row
	topCompTableGridData.grabExcessHorizontalSpace = true;
	topCompTableGridData.horizontalAlignment = GridData.FILL;

	Composite firstTableInfo = new Composite(wrapComp, SWT.NONE | SWT.BORDER);
	firstTableInfo.setLayoutData(topCompTableGridData);
	topCompTableGridData.heightHint = 60;
	firstTableInfo.setLayout(fillLayout);

	straightFlush = new Label(firstTableInfo, SWT.NONE);
	straightFlush.setText("royalFlush");
	straightFlush.setFont(new Font(Avalanche.shell.getDisplay(), "Trebuchet MS", 11, SWT.NORMAL));
	
	quads = new Label(firstTableInfo, SWT.NONE);
	quads.setText("quads");
	quads.setFont(new Font(Avalanche.shell.getDisplay(), "Trebuchet MS", 20, SWT.NORMAL));
	
//	fullHouse = new Label(firstTableInfo, SWT.NONE);
//	fullHouse.setText("fullHouse");
//	fullHouse.setFont(new Font(Avalanche.shell.getDisplay(), "Trebuchet MS", 11, SWT.NORMAL));
//	
//	flush = new Label(firstTableInfo, SWT.NONE);
//	flush.setText("flush");
//	flush.setFont(new Font(Avalanche.shell.getDisplay(), "Trebuchet MS", 11, SWT.NORMAL));
//	
//	straight = new Label(firstTableInfo, SWT.NONE);
//	straight.setText("straight");
//	straight.setFont(new Font(Avalanche.shell.getDisplay(), "Trebuchet MS", 11, SWT.NORMAL));
//	
//	threes = new Label(firstTableInfo, SWT.NONE);
//	threes.setText("threes");
//	threes.setFont(new Font(Avalanche.shell.getDisplay(), "Trebuchet MS", 11, SWT.NORMAL));
//	
//	twoPair = new Label(firstTableInfo, SWT.NONE);
//	twoPair.setText("twoPair");
//	twoPair.setFont(new Font(Avalanche.shell.getDisplay(), "Trebuchet MS", 11, SWT.NORMAL));
//	
//	pair = new Label(firstTableInfo, SWT.NONE);
//	pair.setText("twoPair");
//	pair.setFont(new Font(Avalanche.shell.getDisplay(), "Trebuchet MS", 11, SWT.NORMAL));
}

}