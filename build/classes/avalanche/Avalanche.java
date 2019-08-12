package avalanche;
import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import mi.poker.calculation.EquityCalculation;
import mi.poker.calculation.HandInfo;
import mi.poker.calculation.OCRTest;
import mi.poker.calculation.Result;
import nooni.Util;
import nooni.events.Eve;
import nooni.events.EveListener;
import nooni.events.EventDispatcher;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.json.JSONArray;

import com.github.axet.lookup.OCR;
import com.github.axet.lookup.common.ImageBinaryGrey;

import enumeration.EnumerateWindows;

public class Avalanche {
public static int	defaultSWTGridItemWidth	= 75;
public static Shell					shell;
public static Composite	imgContainer, botComp;
public static GridLayout				imgContainerLayout;

public static java.util.List<String>	regions;
public static Display					display;
public static Label						imgButn, equityLabel;
public static Text						statuss;
public static double					milis=System.currentTimeMillis(), diff;
public static StyledText				console;
public static DecimalFormat			df = new DecimalFormat("###.##");
public static BufferedImage			screenShot, tempSubImg;
public static Image	img;
public static Robot robot;
public static Dimension screenDim;
public static boolean appLive = true; 
public static int gc = 0, msLoop = 500;

public static OCR	ocr;
public static ImageBinaryGrey	imgGray;
public static long fpsMilis;
public static String opponBack="", opponStr, handOpponBoardStr = "";
public static ImageData tmpImageData;
public static boolean imgGrayInited;
public static Result	monteResult;
public static HandInfo	monteHand;
public static Label opponLabel;
public static double	equity, startingEquityDiff;
public static String handParsed;
// = new Board();

public static Thread loopThread;
public static boolean test=true;
public static Result	resu=null;
public static RunnableEquity equityThread= new RunnableEquity("equityThread");
//public static RunnableEquity equityThread= new RunnableEquity("equityThread");
public static Map<String,String> symbols = new HashMap<String,String>();
public static enum RegionParent {ME,BOARD,OPPONENT};
public static enum Position {START,MID,LATE,WEAK};
public static float	 ocrDefaultThreshold = 0.82f;
public static Image tempImg;
public static int[] activeRect;
public static Label cardsLabel;
public static boolean monteCarloActive;
public static double monteWatcher = 0;
public static boolean pause = false;

public static void main (String[] args) {
	display = new Display();
	shell = new Shell(display, SWT.TITLE);
	ocr = new OCR(ocrDefaultThreshold);
	ocr.loadFontsDirectory(OCRTest.class, new File("fonts"));
	symbols.put("d", "♦");
	symbols.put("h", "♥");
	symbols.put("c", "♣");
	symbols.put("s", "♠");
	
	//EngineLogical.startTimer();
	//System.out.println(Cards.validCardSequence(""));
	Toolkit.getDefaultToolkit().beep();
	try {robot = new Robot();} catch (AWTException e1) {e1.printStackTrace();}
	//robot.delay(5000);
	//robot.mouseMove(30, 30);
	//robot.delay(1000);
	//EngineLogical.click(0);
	//	Timer timer = new Timer();
	//	timer.scheduleAtFixedRate(new TimerTask() {
	//		public void run () {
	//			//try {
	//				trace(System.currentTimeMillis());
	//			//} catch (Exception e) {System.out.println(e.getMessage());System.out.println(e.getLocalizedMessage());}
	//		}
	//	}, (long) 2000, (long) 2*60*1000);
	new Thread(new Runnable() { public void run () {
		while (appLive) {
			try {Thread.sleep(msLoop);} catch (Exception e) {}
			Display.getDefault().asyncExec(new Runnable() {
				public void run () {processScreen();}
			});
		}
	}}).start();
	
	shell.setText("Avalanchee");
	Player.loadHandEquity(".","2hand.all.montecarlo.txt");
	
	AssetsSWT.createContents(shell);
	captureScreen();
	createAssets();
	JSONArray arr = Assets.json.getJSONObject("settings").getJSONArray("initPos");
	shell.setLocation(arr.getInt(0), arr.getInt(1));
	EngineLogical.initListeners();
	//dispatcher.addEventListener(AvalEvent.OPPON_FOLD_CHANGE, opnFoldChange);
	
	//https://github.com/mihhailnovik/jSim
	//arsePlayersHands("JcJh,8s7s,99+|AJs+,QQ+|AQs+|AQo+,XxXx,XxXx,XxXx,XxXx");
	//HandParser.parsePossibleHands("QQ+|AQs+|AQo+").length == 22);
	reqMonteCarlo("AsAd,*", "" ,"");
	trace("availableProcessors:"+Runtime.getRuntime().availableProcessors());
	shell.open();
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch()) {
			display.sleep();
		}
	}
	appLive = false;
	display.dispose();
}
public static void createAssets () {
	createAssets("pkst.9players.json");
}
public static void createAssets (String file) {
	//if (board!=null) {
		for (Control control : botComp.getChildren()) {
			System.out.println(control.getSize());
//			control.addDisposeListener(new DisposeListener() {
//	            public void widgetDisposed(DisposeEvent e)
//	            {System.out.println("widget disposed");
//	                //color.dispose();
//	            }
//	        });
	        control.dispose();
	    }
		createAssetsRaw(file);
		botComp.layout();
		shell.layout(true, true);
		//shell.pack();
	//} else {
		//createAssetsRaw();
	//}
}
public static void createAssetsRaw (String file) {
	Opponent.all.clear();
	Assets.load("./", file); // first file
}
public static void processScreen () {
	if (pause) return;
	if (monteCarloActive) {monteWatcher += msLoop;} else {monteWatcher=0;}
	//if (monteWatcher>0) System.out.println(monteWatcher);
	shell.setActive();
	fpsMilis = System.currentTimeMillis();
	milis = System.currentTimeMillis();
	captureScreen();
	EngineLogical.updateLabels();
	double calc = System.currentTimeMillis()-fpsMilis;
	//trace("processScreen:"+System.currentTimeMillis());
	statuss.setText(""+df.format(calc/1000)+"ms " + Runtime.getRuntime().totalMemory()/(1024*1024)+"MB");
}
public static void reqMonteCarlo (String myStr, String boardStr, String outsStr) {
	trace("MONTE SEND "+myStr+" "+boardStr);
	monteCarloActive = true;
	equityLabel.setForeground(Avalanche.display.getSystemColor(SWT.COLOR_RED));
	System.out.println("MONTE SEND: " + myStr + boardStr + outsStr);
	equityThread.start(myStr, boardStr, outsStr, Assets.json.getJSONObject("settings").get("monteUrl")+"/montecarlo");
}
public static void clearEquityLabel (String reason) {
	equityLabel.setText("☛"+df.format(EngineLogical.equity));
	cardsLabel.setText(reason);
	equityThread.kill();
}
public static void setEquity (final double equity2) {
	double largestBet = 0;
	for (Opponent oppon : Opponent.all) {
		if (!oppon.bet.rawString.equals("") && largestBet<Integer.parseInt(oppon.bet.rawString)) largestBet = Integer.parseInt(oppon.bet.rawString);
	}
	String potOdds = "";
	
	if (largestBet>0 && EngineLogical.board.validPot>0) {
		//System.out.println(largestBet + " " + EngineLogical.board.validPot + " ");
		//System.out.println(largestBet/EngineLogical.board.validPot);
		potOdds = df.format(largestBet/EngineLogical.board.validPot*100) + "%";
	}
	EngineLogical.equity = equity2;
	if (!EngineLogical.board.hasCards()) {
		//EngineLogical.equity
		equityLabel.setText("Eq:"+df.format(equity2)+"% " + potOdds/*+df.format(equity2-100/(Opponent.activeOppons()+1))+"%"*/);
	} else {
		equityLabel.setText("Eq:"+df.format(equity2)+"% " + potOdds);
		//EngineLogical.equity = -1;
	}
}
public static synchronized void receiveSimulationResults (final double equity2, final String eqString) {
	Display.getDefault().asyncExec(new Runnable() {
		public void run () {
			if (eqString.equals("timeout")) {		return;		}
			monteCarloActive = false;
			//if (!EngineLogical.board.hasCards()) {trace("NOTICE:board doesnt have cards, bypass montecarlo receive");return; }
			trace("MONTE RECEIVE eq:"+equity2);
			//EngineLogical.dispatcher.dispatchEvent(new AvalEvent(AvalEvent.MONTE_EQUITY_RECEIVE, null));
			setEquity(equity2);
			int extrOppons = eqString.split("-")[0].split(",").length-1;
			
			cardsLabel.setText(AvalancheUtil.createSymbols(eqString.replace(",*", "").replace("-", "  "))+" ("+extrOppons+")");
			EngineLogical.dispatcher.dispatchEvent(new AvalEvent(AvalEvent.RECEIVE_SIMULATION_RESULTS, this));
		}
	});
}
public static void trace (Object msg) {
	//if (console==null) return;
	diff = (System.currentTimeMillis()-milis)/1000;
	console.append("\n"+msg.toString()+ " (" + diff + "s)");
	if (console.getText().length()>5000) {console.setText(console.getText().substring(
			console.getText().length()-4800, console.getText().length()));}
	console.setTopIndex(console.getLineCount() - 1);
	milis = System.currentTimeMillis();
}
public static BufferedImage captureScreen () {
	 screenDim = Toolkit.getDefaultToolkit().getScreenSize(); 
	 //activeRect = EnumerateWindows.getActiveWinRect();
	 //Rectangle rect2 = new Rectangle(activeRect[0], activeRect[1], activeRect[2], activeRect[3]);
	 Rectangle rect2 = new Rectangle(-4, -4, 900, 700);
	 //if (activeRect[2]<1) activeRect[2] = 1;
	 //if (activeRect[3]<1) activeRect[3] = 1;
	 try {
		 screenShot = robot.createScreenCapture(rect2);
		// trace("screen capture");
	 } catch (Exception e) {
	 }
	 return screenShot;
}

}