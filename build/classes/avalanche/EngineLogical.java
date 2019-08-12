package avalanche;

import static avalanche.Avalanche.equityLabel;
import static avalanche.Avalanche.msLoop;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import nooni.events.Eve;
import nooni.events.EveListener;
import nooni.events.EventDispatcher;
import org.eclipse.swt.SWT;

public class EngineLogical {
public static double	equity;
public static MyHand me;
public static Board board;
public static String boardHandOppon="", lastBoardHandOppon="";
public static EventDispatcher dispatcher = new EventDispatcher();
public static int calcTimeout = -1;
public static int callMaximum = 600;
public static int buyin = 400;
public static int bigBlind = 2;
public static int checkMyControlsTimeout = -1;
public static ArrayList<TimerObj> ss = new ArrayList<TimerObj>();
public static Timer timer = new Timer();
public static String lastInfoStr = "";
public static long lastEV = 0;

public static void startTimer () {
	Timer timer = new Timer();
	timer.scheduleAtFixedRate(new TimerTask() {
		@Override
		public void run () {
			if (ss.size()>0) {
				ss.get(0).left-=16;
				if (ss.get(0).left<=0) {
					if (!ss.get(0).dispatched) {
						System.out.println("DISPATCHING "+ss.get(0).eve);
						dispatcher.dispatchEvent(new AvalEvent(ss.get(0).eve, null, ss.get(0).data));
						ss.get(0).dispatched = true;
					}
					if (ss.get(0).left<=0) {
						ss.remove(0);
					}
				}
			}
		}
	}, 0, 16);
}
public static void addToTimer (String eve, int length, Object data) {
	ss.add(new TimerObj(eve, length, data));
}
public static void addToTimer (String eve, int length) {
	ss.add(new TimerObj(eve, length, null));
}

public static void initListeners () {
	dispatcher.addEventListener(AvalEvent.CHANGE_BOARD, boardChange);
	dispatcher.addEventListener(AvalEvent.CHANGE_POT, potChange);
	dispatcher.addEventListener(AvalEvent.CHANGE_MY_HAND, myHandChange);
	dispatcher.addEventListener(AvalEvent.CHANGE_OPN_FOLD, oppnFold);
	dispatcher.addEventListener(AvalEvent.CHANGE_OPN_ADD, oppnAdd);
	dispatcher.addEventListener(AvalEvent.MY_TURN_CHECK, myTurnCheck);
	dispatcher.addEventListener(AvalEvent.MY_TURN_CALL, myTurnCall);
	dispatcher.addEventListener(AvalEvent.CHANGE_MY_HAND_ACTIVE, myHandActiveChange);
	dispatcher.addEventListener(AvalEvent.MY_TURN_BOXCALL, myTurnBoxCall);
	dispatcher.addEventListener(AvalEvent.MOVE_TO, moveTo);
	dispatcher.addEventListener(AvalEvent.CLICK, click);
	dispatcher.addEventListener(AvalEvent.KEY_PRESS, press);
	dispatcher.addEventListener(AvalEvent.CHECK_IF_TURN_ACTIVE, checkIfTurnActive);
	dispatcher.addEventListener(AvalEvent.PAUSE_ME, pauseLoop);
	dispatcher.addEventListener(AvalEvent.RESUME_ME, resumeLoop);
	dispatcher.addEventListener(AvalEvent.REQUEST_SIMULATION, requestSimulation);
	dispatcher.addEventListener(AvalEvent.RECEIVE_SIMULATION_RESULTS, receiveSimulation);
}
public static void updateLabels () {
	for (Opponent oppon : Opponent.all) {
		oppon.updateVisuals(Avalanche.screenShot);
	}
	board.updateVisuals(Avalanche.screenShot);
	me.updateVisuals(Avalanche.screenShot);
	if (calcTimeout>0) {
		calcTimeout -= Avalanche.msLoop;
		if (calcTimeout<=0) {
			calcTimeout = -1;
			trace("CALC TIMEOUT calculateMyTurn");
			calculateMyTurn(0);
		}
	}
//	if (checkMyControlsTimeout>0) {
//		checkMyControlsTimeout -= Avalanche.msLoop;
//		if (checkMyControlsTimeout<=0) {
//			checkMyControlsTimeout = -1;
//			if (me.isMyTurn) {
//				trace("POST CLICK CALC " +me.controlFold.rawString+" - "+me.controlCheckRaise.rawString);
//				calculateMyTurn(0);
//			}
//		}
//	}
}
public static EveListener receiveSimulation = new EveListener () {		@Override public void onEvent (Eve e) { //Event clientEve=(Event) e;
	//trace("listener:pauseLoop");
	trace("receive simulation");
	equityLabel.setForeground(Avalanche.display.getSystemColor(SWT.COLOR_WHITE));
	String strCall = "";
	Double potValue = board.lastValidPot*equity/100;
	boolean folding = false, checking = false, calling = false, bettingRaising = false;
	
	Double callValue = me.call*(1-equity/100);
	// 63000*.5 - 6505*.5
	// 30000 - 3000 = 2700
	// 63000>me.chips*(equity/100)
	// 
	// 2661*.62 = 1649 - .38*2000 = 1649 - 768
	lastEV = Math.round(potValue - callValue);
	strCall = "EV: " + String.valueOf(lastEV);
	if (me.call==0) {
		trace("Checking");
		strCall = "Check, "+strCall;
	} else {
		trace("Call for " + me.call);
	}
	
	AssetsSWT.firstTableInfo.quads.setText(strCall + " " + me.chipsRegion.rawString);
	
	if (me.call>me.chips*(equity/100) && equity/100<0.96) {
		lastInfoStr = ("Call is greater than equity*chips, folding");
		folding = true;
	} else if (lastEV > -5 && !board.hasCards()) {
		lastInfoStr = ("EV less than -5, folding");
		calling = true;
	} else if (me.call==0) {
		trace("Bet: " + me.bet);
		if (me.bet>0 && lastEV>=me.bet) {
			lastInfoStr = ("Betting " + me.bet);
			bettingRaising = true;
		} else {
			lastInfoStr = ("Cheking");
			checking = true;
		}
	} else if (lastEV>0) {
		lastInfoStr = ("EV call");
		calling = true;
	} else {
		lastInfoStr = ("Folding");
		folding = true;
	}
	trace(lastInfoStr);
	
	int keyEvent = -1;
	if (folding) {
		trace("Pressing Fold (F4)");
		keyEvent = KeyEvent.VK_F4;
		me.folded = true;
		//if (board.hasCards()) 
	} else if (checking || calling) {
		trace("Pressing check/call (F8)");
		keyEvent = KeyEvent.VK_F8;
	} else if (bettingRaising) {
		trace("Pressing betting/raising (F9)");
		keyEvent = KeyEvent.VK_F9;
	}
	
	History.capturePlay();
	final int keyEvenetFinal = -1;//keyEvent;
	if (keyEvenetFinal>-1) {
		
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				Avalanche.robot.keyPress(keyEvenetFinal);
				timer.schedule(new TimerTask() {
					@Override
					public void run() {Avalanche.robot.keyRelease(keyEvenetFinal);}
				}, 35);
			}
		}, 1000);

	}
	//calculateEquity();
}};
public static EveListener requestSimulation = new EveListener () {		@Override public void onEvent (Eve e) { //Event clientEve=(Event) e;
	//trace("listener:pauseLoop");
	trace("request simulation");
	calculateEquity();
}};
public static EveListener pauseLoop = new EveListener () {		@Override public void onEvent (Eve e) { //Event clientEve=(Event) e;
	//trace("listener:pauseLoop");
	System.out.println("pauseME");
	me.paused = true;
}};
public static EveListener resumeLoop = new EveListener () {		@Override public void onEvent (Eve e) { //Event clientEve=(Event) e;
	//trace("listener:resumeLoop");
	System.out.println("resumeME");
	me.paused = false;
}};
public static EveListener moveTo = new EveListener () {		@Override public void onEvent (Eve e) { //Event clientEve=(Event) e;
	//trace("listener:moveTo");
	moveTo(((int[])e.data)[0], ((int[])e.data)[1]);
	ss.get(0).left = 100;
}};
public static void moveTo (int x, int y) {
	Avalanche.robot.mouseMove((int)(x+Math.round(Math.random()*25)-12), (int)(y+Math.round(Math.random()*10)-5));
}
private static int[] charsFromInt (int sum) {
	String str = String.valueOf(sum);
	int[] ret = new int[str.length()];
	
	for (int i=0;i<str.length();i++) {
		if (str.subSequence(i, i+1).equals("0")) ret[i] = KeyEvent.VK_0;
		else if (str.subSequence(i, i+1).equals("1")) ret[i] = KeyEvent.VK_1;
		else if (str.subSequence(i, i+1).equals("2")) ret[i] = KeyEvent.VK_2;
		else if (str.subSequence(i, i+1).equals("3")) ret[i] = KeyEvent.VK_3;
		else if (str.subSequence(i, i+1).equals("4")) ret[i] = KeyEvent.VK_4;
		else if (str.subSequence(i, i+1).equals("5")) ret[i] = KeyEvent.VK_5;
		else if (str.subSequence(i, i+1).equals("6")) ret[i] = KeyEvent.VK_6;
		else if (str.subSequence(i, i+1).equals("7")) ret[i] = KeyEvent.VK_7;
		else if (str.subSequence(i, i+1).equals("8")) ret[i] = KeyEvent.VK_8;
		else if (str.subSequence(i, i+1).equals("9")) ret[i] = KeyEvent.VK_9;
	}
	//String chrs[] = str.
	return ret;
}

public static EveListener press = new EveListener () {		@Override public void onEvent (Eve e) { //Event clientEve=(Event) e;
	//trace("listener:press");
	//Toolkit.getDefaultToolkit().beep();
	Avalanche.robot.keyPress((int) e.data);
	Avalanche.robot.delay((int) (Math.round(Math.random()*150)+50));
	Avalanche.robot.keyRelease((int) e.data);
	Avalanche.robot.delay((int) (Math.round(Math.random()*150)+50));
	ss.get(0).left = 450;
}};
public static EveListener click = new EveListener () {		@Override public void onEvent (Eve e) { //Event clientEve=(Event) e;
	//trace("listener:click");
	click();
	ss.get(0).left = 500;
}};
public static void click () {
	//Toolkit.getDefaultToolkit().beep();
	//trace("clicking");
	History.two+="click;";
	//Avalanche.robot.delay((int) (Math.round(Math.random()*400)+120));
	//Avalanche.robot.delay(300);
	Avalanche.robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
	Avalanche.robot.delay((int) (Math.round(Math.random()*200)+120));
	Avalanche.robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
	//moveCursorToSafePlace();
	//	try {	Thread.sleep(550); // zynga after call click bug
	//	} catch (InterruptedException e) {e.printStackTrace();	}
	
	//checkMyControlsTimeout = 950;
	//me.updateVisuals(Avalanche.captureScreen());
	//me.updateVisuals(Avalanche.screenShot);
}
public static void moveCursorToSafePlace () {
	Avalanche.robot.mouseMove(Assets.safeClick[0], Assets.safeClick[1]);
}
public static EveListener checkIfTurnActive = new EveListener () {		@Override public void onEvent (Eve e) { //Event clientEve=(Event) e;
	//trace("listner:checkIfTurnActive");
	me.controlCheckRaise.rawString = "";
//	if (me.isMyTurn) {
//		System.out.println("checkIfTurnActive: " +me.controlCheckRaise.rawString + " - " + me.controlFold.rawString);
//		calculateMyTurn(0);
//		ss.get(0).left = 100;
//	}
}};
public static void calculateMyTurn (int delay) {
	if (!me.isMyTurn) return;
	History.two+= "calcME;";
	trace("calculate my turn, call:"+me.call+" equity:"+equity+" hand:"+me.validHand+" board has cards:"+board.hasCards());
	if (me.call>callMaximum && !board.hasCards()) {
		trace("max limit preflop fold");
		fold(delay);
	} else if (me.call>callMaximum && board.hasCards() && equity<96) {
		trace("max limit flop+ fold");
		fold(delay);
	} else if (!board.hasCards()) {
		if (me.isLowestTier() && me.call<=4) {
			//clickRaise(8);
			trace("me.isLowestTier()");
			fold(delay);
		} else if (Player.myPocket().raise>-1) {
			trace("pocket raise>-1");
			//raise(8);
			if (me.allin.rawString.equals("allin")) {
				int deviation = (int) (Math.round(Math.random()*20)-10);
				int raiseTo = Player.myPocket().raise+((Player.myPocket().raise+deviation)>2?deviation:0);
				trace("preflop equity raise for hand: "+raiseTo);
				clickRaise(raiseTo);
			} else clickCheckOrCall(delay);
			//checkOrCall(delay);
		} else if (me.call<=4 && equity>50) {
			//raise(4);
			trace("me.call<=4 && equity>50");
			clickCheckOrCall(delay);
		} else if (me.call<=2) {
			//raise(6);
			trace("me.call<=2");
			clickCheckOrCall(delay);
		} else {
			trace("no board default fold");
			fold(delay);
		}
	
	//if (true) { // always call small rises/checks
		//checkOrCall(delay);
	//} else if (equity<50 || me.call>) {
		
	} else if (equity>=90) {
		trace("equity>90");
		if (me.allin.rawString.equals("allin")) {
			clickRaise(100);
		} else clickCheckOrCall(delay);
	} else if (equity>=80 && me.call<40) {
		trace("equity>=80 && me.call<40");
		clickCheckOrCall(delay);
	} else if (equity>=70 && me.call<20) {
		trace("equity>=70 && me.call<20");
		clickCheckOrCall(delay);
	} else if (equity>=60 && me.call<10) {
		trace("equity>=60 && me.call<10");
		clickCheckOrCall(delay);
	} else { // fold
		trace("default board fold");
		fold(delay);
	}
}
public static void clickRaise (int sum) {
	System.out.println("clickRaise");
	History.two += ("raise-"+sum+";");
	addToTimer(AvalEvent.PAUSE_ME, 20);
	int chars[] = charsFromInt(sum);
	trace("raise sequence");
	addToTimer(AvalEvent.MOVE_TO, 50, new int[]{me.raiseinput.region[0]+20, 
			me.raiseinput.region[1]+me.raiseinput.region[3]/2});
	addToTimer(AvalEvent.CLICK, 20, null);
	addToTimer(AvalEvent.CLICK, 20, null);
	for (int chr : chars) {
		addToTimer(AvalEvent.KEY_PRESS, 50, chr);
	}
	//moveTo(me.betraise.region[0]+me.betraise.region[2]/2,
	//		me.betraise.region[1]+me.betraise.region[3]/2, 100);
	//System.out.println("listener:resumeLoop");
	
	addToTimer(AvalEvent.KEY_PRESS, 50, KeyEvent.VK_ENTER);
	addToTimer(AvalEvent.CHECK_IF_TURN_ACTIVE, 1000);
	addToTimer(AvalEvent.RESUME_ME, 100);

	//moveCursorToSafePlace();
//	try {	Thread.sleep(550); // zynga after call click bug
//	} catch (InterruptedException e) {e.printStackTrace();	}
}
public static void clickCheckOrCall (int delay) {
	trace("moving to checkCall");
	System.out.println("moving to checkCall");
	History.two += ("toCC;");
	//me.history+="call-"+me.call+";";
	addToTimer(AvalEvent.PAUSE_ME, 10);
	addToTimer(AvalEvent.MOVE_TO, 50, new int[]{me.controlCheckRaise.region[0]+me.controlCheckRaise.region[2]/2,
			me.controlCheckRaise.region[1]+me.controlCheckRaise.region[3]/2});
	addToTimer(AvalEvent.CLICK, delay, null);
	addToTimer(AvalEvent.CHECK_IF_TURN_ACTIVE, 1000);
	addToTimer(AvalEvent.RESUME_ME, 100);
}
public static void fold (int delay) {
	trace("move to fold");
	History.two+="mefold;";
	addToTimer(AvalEvent.PAUSE_ME, 10);
	addToTimer(AvalEvent.MOVE_TO, 50, new int[]{me.controlFold.region[0]+me.controlFold.region[2]/2,
			me.controlFold.region[1]+me.controlFold.region[3]/2});
	//me.folded = true;
	addToTimer(AvalEvent.CLICK, delay, null);
	addToTimer(AvalEvent.RESUME_ME, 1000);
}
public static void testBoxCallFold () {
	trace("testBoxCallFold:$"+me.call + " hand:" + me.validHand + " rawHandIsValid:" + me.rawHandIsValidExists());
	if (me.isLowestTier() && me.call>0 && !me.folded && me.rawHandIsValidExists()) {
		History.two += "preTurnFOLD;";
		//me.folded = true;
		fold(1000);
	}
}
public static EveListener myTurnBoxCall = new EveListener () {		@Override public void onEvent (Eve e) { //Event clientEve=(Event) e;
	trace("listener:myTurnBoxCall");
	testBoxCallFold();
}};
public static EveListener myTurnCall = new EveListener () {			@Override public void onEvent (Eve e) { //Event clientEve=(Event) e;
	trace("listener:myTurnCall:"+me.controlCheckRaise.rawString + " - "+me.controlFold.rawString);
	History.two +=("call-"+me.call+";");
	if (!me.rawHandIsValidExists()) {return;}
	if (Avalanche.monteWatcher>50000) {
		Avalanche.monteWatcher=51000;
		return;
	}
//	int moveDelay = 0;
	if (Avalanche.monteCarloActive) {
		calcTimeout = 5000;
	} else {
		trace("CALC myTurnCall");
		calculateMyTurn((int) (200+Math.round(Math.random()*3700)));
		//int moveDelay = (int) (200+Math.round(Math.random()*3700));
	}
	/*if (<=2) {
		Avalanche.robot.mouseMove(me.controlCheckRaise.region[0]+me.controlCheckRaise.region[2]/2, 
				me.controlCheckRaise.region[1]+me.controlCheckRaise.region[3]/2);
	} else if (equity>0) {
		//controlFold
	}*/
}};
public static EveListener myTurnCheck = new EveListener () {			@Override public void onEvent (Eve e) {//Event clientEve=(Event) e;
	trace("listener:myTurnCheck: controlCheckRaise.rawString"+me.controlCheckRaise.rawString 
			+ " rawCheckRaise:"+me.rawCheckRaise+" isMyTurn:"+me.isMyTurn);
	History.two += "check;";
	if (!me.rawHandIsValidExists() || !me.allin.rawString.equals("allin")) {return;}
	int moveDelay = (int) (1000+Math.round(Math.random()*2000));
	//Avalanche.robot.delay(moveDelay);
	if (Player.myPocket().raise>-1) {
		if (me.allin.rawString.equals("allin")) {
			
			int deviation = (int) (Math.round(Math.random()*20)-10);
			int raiseTo = Player.myPocket().raise+((Player.myPocket().raise+deviation)>2?deviation:0);
			trace("check raise for hand: "+raiseTo);
			clickRaise(raiseTo);
			/*trace("raise after check");
			int deviation = (int) (Math.round(Math.random()*20)-10);
			clickRaise(Player.myPocket().raise+((Player.myPocket().raise+deviation)>2?deviation:0));*/
		} else clickCheckOrCall(moveDelay);
		//checkOrCall(delay);
	} else clickCheckOrCall(moveDelay);
}};
public static EveListener availableCheckRaise = new EveListener () {			@Override public void onEvent (Eve e) {//Event clientEve=(Event) e;
	trace("listener:availableCheckRaise:"+me.controlCheckRaise.rawString);
}};
public static EveListener oppnFold = new EveListener () {			@Override public void onEvent (Eve e) {//Event clientEve=(Event) e;
	//Avalanche.clearEquityLabel("oppnFold");
	History.two += "oFold;";
	trace("   listener:oppnFold, opons:"+Opponent.activeOppons());
	me.calculatePosition();
	//calculateEquity();
}};
public static EveListener oppnAdd = new EveListener () {			@Override public void onEvent (Eve e) {//Event clientEve=(Event) e;
	//Avalanche.clearEquityLabel("oppnFold");
	trace("listener:oppnAdd, opons:"+Opponent.activeOppons());
	//calculateEquity();
}};
public static EveListener myHandActiveChange = new EveListener () {	@Override public void onEvent (Eve e) {//Event clientEve=(Event) e;
	//Avalanche.clearEquityLabel("oppnFold");
	trace("listener:myHandActive:"+me.active);
	me.calculatePosition();
	if (!me.active) {
		History.capturePlay();
	} else {
		History.capturePostPlay();
		calculateEquity("newHand");
	}
	//calculateEquity();
}};
public static EveListener myHandChange = new EveListener () { 	@Override public void onEvent (Eve e) {//Event clientEve=(Event) e;
	//Avalanche.clearEquityLabel("myHandChange");
	trace("listener:myHandChange " + me.validHand + " oppons:"+Opponent.activeOppons());
	
    //System.out.println("Current Date: " + ft.format(dNow));
    if (me.hasHand()) {

    }
    if (me.active) {
    	me.newHand();
    }
    //calculateEquity();
	//AvalancheUtil.
	//if (!board.hasCards()) {
		//boardHandOppon
	//}
	
}};
public static EveListener boardChange = new EveListener () { 		@Override public void onEvent (Eve e) {//Event clientEve=(Event) e;
	//Avalanche.clearEquityLabel("boardChange");
	if (board.hasCards()) History.two += "BRD-"+board.validCards+";";//kettle:"+board.pot+";";
	trace("listener:board change "+board.rawCards);
	//calculateEquity();
}};
public static EveListener potChange = new EveListener () { 			@Override public void onEvent (Eve e) {//Event clientEve=(Event) e;
	trace("  listener:pot change "+board.validPot);
	History.two += "kettle:"+board.validPot+";";
}};

public static void calculateEquity () {
	calculateEquity("");
}
public static void calculateEquity (String str) {
	//trace("calc"+me.hasHand()+board.hasCards());
	boardHandOppon = me.validHand+board.validCards;//+Opponent.activeOppons();
	if (str.equals("newHand")) {
		boardHandOppon = "";
	}
	if (!boardHandOppon.equals(lastBoardHandOppon)) {
		if (me.hasHand()) {
			
			if (Opponent.activeOppons()<1) {
				
			} else if (board.hasCards()) {
				//trace("INIT MONTECARLO");
				Avalanche.clearEquityLabel(me.validHand + " " + board.validCards + " ("+Opponent.activeOppons()+")");
				Avalanche.reqMonteCarlo(me.validHand+""+Opponent.opponStr(), board.validCards, "");
				//boardHandOppon
				//trace("CALC:"+me.validHand+" - "+board.validCards + " - " + Opponent.activeOppons());
				//System.out.println("");
			} else {
				//trace("INIT MONTECARLO");
				//trace("oppons empty");
				//Avalanche.setEquity(Player.equity());
				//Avalanche.cardsLabel.setText(AvalancheUtil.createSymbols(me.validHand) + "" /*+ EngineLogical.me.pocketValue()*/);
				Avalanche.clearEquityLabel(me.validHand + " ");
				Avalanche.reqMonteCarlo(me.validHand+Opponent.opponStr(), "", "");
			}
			
			//trace("CALC:"+me.validHand+" - " + Opponent.activeOppons());
			lastBoardHandOppon = boardHandOppon;
		}
		
	} else {
		EngineLogical.dispatcher.dispatchEvent(new AvalEvent(AvalEvent.RECEIVE_SIMULATION_RESULTS, null));
	}

//	if (me.rawIsValid()) { // in play
//		opponStr = "";
//		int n;
//		for (n=0;n<Player.activeOppons();n++) {	opponStr += ",*";}
//		if (me.equity()>-1 && board.empty()) setEquity(me.lastValidString()+"  "+me.equity()+" ("+Player.activeOppons()+") "+df.format(me.equity()-100/(Player.activeOppons()+1))+" "+100/(Player.activeOppons()+1));
//		if (board.boardState()=="none" || !me.hasHand()) {
//		} else if (Player.activeOppons()<1 || Player.activeOppons()>8) {
//			System.out.println("opponents out of bounds " + Player.activeOppons());
//		} else if (!handOpponBoardStr.equals(me.lastValidString()+opponStr+board.lastValidString)) {
//			botComp.layout(true, true);
//			handOpponBoardStr = me.lastValidString()+opponStr+board.lastValidString;
//			if (!board.empty()) {
//					doMonteCarlo(me.lastValidString()+opponStr, board.lastValidString, "");
//			}
//		}
//	}
}

public static void trace (final Object obj) {
	Avalanche.display.asyncExec(new Runnable() {
	    public void run() {
	    	Avalanche.trace(obj);
	    }
	});
	
}
}