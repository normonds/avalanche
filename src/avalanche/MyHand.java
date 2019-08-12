package avalanche;

import java.awt.image.BufferedImage;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Text;
import org.json.JSONObject;

import avalanche.Avalanche.Position;
import avalanche.Avalanche.RegionParent;
import static avalanche.EngineLogical.board;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Image;

public class MyHand extends Player {
public BoardRegion	card1letter, card1symbol, card2letter, card2symbol, controlCheckRaise, controlFold, betRegion, chipsRegion, allin, raiseinput,
betraise;
public String rawHand="", lastRawHand="", textFieldStr="", validHand="",lastValidHand="";//, lastValidHand="";
public String lastRawCheckRaise="";//, rawCheckRaise="";
public String rawCheckRaise = "";
public String strCall = "strCall";
public int call=-1, bet = -1, chipsInPlay = 0, lastCall = -1, playerDefaultPos = 4, initOppons = 0;
public boolean active = false, lastActive = false;
public boolean folded = false, isMyTurn;
public boolean paused = false, callActive = false;
public boolean playerActionSubmitted = false;

public double chipHandDifference = 0, chips = 0, chipLastDifference = 0, chipsPrev = 0;

public MyHand (JSONObject json) {
	super(json, RegionParent.ME);

	card1letter = new BoardRegion(json.getJSONObject("cards").getJSONObject("card1").getJSONObject("letter"), RegionParent.ME);
	card1symbol = new BoardRegion(json.getJSONObject("cards").getJSONObject("card1").getJSONObject("symbol"), RegionParent.ME);
	card2letter = new BoardRegion(json.getJSONObject("cards").getJSONObject("card2").getJSONObject("letter"), RegionParent.ME);
	card2symbol = new BoardRegion(json.getJSONObject("cards").getJSONObject("card2").getJSONObject("symbol"), RegionParent.ME);
	controlCheckRaise = new BoardRegion(json.getJSONObject("checkraise"), RegionParent.ME);
	controlFold = new BoardRegion(json.getJSONObject("fold"), RegionParent.ME);
	betRegion = new BoardRegion(json.getJSONObject("bet"), RegionParent.ME);
	chipsRegion = new BoardRegion(json.getJSONObject("chips"), RegionParent.ME);
	allin = new BoardRegion(json.getJSONObject("allin"), RegionParent.ME);
	raiseinput = new BoardRegion(json.getJSONObject("raiseinput"), RegionParent.ME);
	betraise = new BoardRegion(json.getJSONObject("betraise"), RegionParent.ME);
	Object [] ret = AssetsSWT.addImgComp("me", Avalanche.defaultSWTGridItemWidth*4, 20,2);
	canvas = (Canvas) ret[0];
	textField = (Text) ret[1];
	
	card1letter.trace = true;
	
	canvas.addPaintListener(new PaintListener() {
		public void paintControl (PaintEvent e) {
			e.gc.drawLine(0,0,((Canvas)e.getSource()).getSize().x,((Canvas)e.getSource()).getSize().y);
			redraw(e.gc);
		}
	});
}
public void redraw (GC refGC) {
	//Avalanche.trace("redraw");
	updateVisuals(Avalanche.screenShot, refGC, true);
}
public boolean hasHand () {
	if (validHand.length()==4) { return true; }
	return false;
}
public void newHand () {

	EngineLogical.dispatcher.dispatchEvent(new AvalEvent(AvalEvent.CHANGE_MY_HAND_ACTIVE, this));
	folded = false;
	initOppons = Opponent.activeOppons();
	//testForBoxCheck();
	dispatchActiveOrPassiveTurn();
	//updateVisuals(Avalanche.screenShot);
}
//public void testForBoxCheck () {
//	System.out.println("testForBoxCheck");
//}

public void dispatchActiveTurn () {
	if (rawCheckRaise.contains("ok") && controlFold.rawString.contains("delete") && isMyTurn) {
		call = -1;
		if (rawCheckRaise.contains("call")) {
			call = Integer.parseInt(rawCheckRaise.split("\\$")[1]);
			lastCall  = call;
			EngineLogical.dispatcher.dispatchEvent(new AvalEvent(AvalEvent.MY_TURN_CALL, this));
		} else if (rawCheckRaise.contains("check")) {
			EngineLogical.dispatcher.dispatchEvent(new AvalEvent(AvalEvent.MY_TURN_CHECK, this));
		}
	}
}
public void dispatchActiveOrPassiveTurn () {
	//System.out.println("rawCheckRaise:" + rawCheckRaise + " controlFold:"+controlFold.rawString);
	dispatchActiveTurn();
	if (rawCheckRaise.contains("box") && rawCheckRaise.contains("call") && active) {
		//System.out.println("dispatching AvalEvent.MY_TURN_BOXCALL");
		call = Integer.parseInt(rawCheckRaise.split("\\$")[1]);
		EngineLogical.dispatcher.dispatchEvent(new AvalEvent(AvalEvent.MY_TURN_BOXCALL, this));
	}
}
public void updateVisuals (BufferedImage screen) {
	updateVisuals(screen, gcc, false);
}
public void updateVisuals (BufferedImage screen, GC refGC, boolean redraw) {
//	System.out.println("updateVisuals");
	if (paused) return;
	if (canvas!=null) {
		//Avalanche.trace(screen.getWidth());
		if (refGC==null) {
			refGC = new GC(canvas);
			refGC.setForeground(green);
		}
		cPx = 0; cPy = 0; //pot.region[3];
		dealer.process(screen, refGC, redraw, 0, card1letter.region[3]);
		card1letter.process(screen, refGC, redraw, cPx, cPy); cPx += card1letter.region[2];
		card1symbol.process(screen, refGC, redraw, cPx, cPy); cPx += card1symbol.region[2];
		card2letter.process(screen, refGC, redraw, cPx, cPy); cPx += card2letter.region[2];
		card2symbol.process(screen, refGC, redraw, cPx, cPy); cPx += card2symbol.region[2];
		controlCheckRaise.process(screen, refGC, redraw, cPx, cPy); cPy += controlCheckRaise.region[3];
		controlFold.process(screen, refGC, redraw, cPx, cPy); cPy += controlFold.region[3]; cPx += controlFold.region[2];
		cPy = 0;
		betRegion.process(screen, refGC, redraw, cPx, cPy); cPy += betRegion.region[3];
		chipsRegion.process(screen, refGC, redraw, cPx, cPy); cPy += chipsRegion.region[3];
		allin.process(screen, refGC, redraw, cPx, cPy); cPy += allin.region[3];
		raiseinput.process(screen, refGC, redraw, cPx, cPy);
		

		try {
			if (chipsRegion.rawString.length()>0) { chips = Long.parseLong(chipsRegion.rawString); }
		} catch (NumberFormatException nfe) {Avalanche.trace("Chips int parse NumberFormatException: " + nfe.getMessage());	}
		if (chipsPrev!=chips && chips>0) {
			chipLastDifference = chipsPrev-chips;
			chipsPrev = chips;
			chipHandDifference += Math.abs(chipLastDifference);
			EngineLogical.dispatcher.dispatchEvent(new AvalEvent(AvalEvent.CHANGE_MY_CHIPS, this));
		}
		rawHand = card1letter.rawString+card1symbol.rawString+card2letter.rawString+card2symbol.rawString;
		
		if (rawHand.equals("")) {
			//Avalanche.trace("not valid cards");
			refGC.setBackground(yellow);
			refGC.fillRectangle(0, 50, 180, 40);
		} else if (rawHand.length()>0 && !Cards.validCardSequence(rawHand)) {
			refGC.setBackground(red);
			refGC.fillRectangle(0, 50, 180, 40);
		}
		
		if (!rawHand.equals(lastRawHand)) {
			if (handIsValid(rawHand) && Cards.validCardSequence(rawHand)) {
				validHand = rawHand;
				//lastValidHand;
				folded = false;
				Opponent.flush();
				if (!validHand.equals("") && !validHand.equals(lastValidHand)) {
					lastValidHand = validHand;
					EngineLogical.dispatcher.dispatchEvent(new AvalEvent(AvalEvent.CHANGE_MY_HAND, this));
					lastValidHand = validHand;
				}
			}
			lastRawHand = rawHand;
		}
		
		if (betRegion.rawString.contains("call") || raiseinput.rawString.contains("call")) {
			strCall = "Call EV:";
							
				call = -1;
				try {
					if (betRegion.rawString.contains("call")) {
						call= Integer.parseInt(betRegion.rawString.replace("call", ""));
					} else {
						call= Integer.parseInt(raiseinput.rawString.replace("call", ""));
					}
				} catch (NumberFormatException nfe) {Avalanche.trace("Call int parse NumberFormatException: " + nfe.getMessage());	}
				
				if (call>0) {
					if (!playerActionSubmitted) {
						EngineLogical.dispatcher.dispatchEvent(new AvalEvent(AvalEvent.REQUEST_SIMULATION, this));
						EngineLogical.dispatcher.dispatchEvent(new AvalEvent(AvalEvent.MY_TURN, this));
					}
					playerActionSubmitted = true;
					/*
					strCall += String.valueOf(EngineLogical.board.lastValidPot) + "*" ;
					strCall += String.valueOf(EngineLogical.equity/100).substring(0,5) + "=";
					strCall += String.valueOf(Math.round(EngineLogical.board.lastValidPot*EngineLogical.equity/100));
					
					strCall += "   "+String.valueOf(call) + "*" ;
					strCall += String.valueOf(1-EngineLogical.equity/100).substring(0,5) + "=";
					strCall += String.valueOf(Math.round(call*(1-EngineLogical.equity/100)));*/
					
					//strCall += String.valueOf(Math.round((EngineLogical.board.lastValidPot*EngineLogical.equity/100) - (call*(1-EngineLogical.equity/100))));
				} else {
					strCall = "Invalid call int";
				}
				
				AssetsSWT.firstTableInfo.quads.setForeground(Avalanche.display.getSystemColor(SWT.COLOR_GREEN));
				//strCall += bet.rawString.replace("call", "");
			
		} else if (betRegion.rawString.contains("check")) {
			
			try {
				if (raiseinput.rawString.contains("bet")) {
					bet = Integer.parseInt(raiseinput.rawString.replace("bet", ""));
				}
			} catch (NumberFormatException nfe) {Avalanche.trace("Call int parse NumberFormatException: " + nfe.getMessage());	}
			
			if (!playerActionSubmitted) {
				EngineLogical.dispatcher.dispatchEvent(new AvalEvent(AvalEvent.REQUEST_SIMULATION, this));
				EngineLogical.dispatcher.dispatchEvent(new AvalEvent(AvalEvent.MY_TURN, this));
			}
			playerActionSubmitted = true;
			//strCall = "Check: ";
			//strCall += String.valueOf(Math.round(EngineLogical.board.lastValidPot*(EngineLogical.equity/100)));
			AssetsSWT.firstTableInfo.quads.setForeground(Avalanche.display.getSystemColor(SWT.COLOR_YELLOW));
		} else {
			playerActionSubmitted = false;
			
			lastCall = call;
			call = 0;
			bet = 0;
			AssetsSWT.firstTableInfo.quads.setForeground(Avalanche.display.getSystemColor(SWT.COLOR_WHITE));
		}
		//AssetsSWT.firstTableInfo.quads;
		if (Avalanche.monteCarloActive) {
			AssetsSWT.firstTableInfo.quads.setForeground(Avalanche.display.getSystemColor(SWT.COLOR_GRAY));
		}
		
		if (controlCheckRaise.rawString.contains("box") || controlFold.rawString.contains("box") || controlFold.rawString.contains("delete")
				|| controlCheckRaise.rawString.contains("ok")) {
			active = true;
			if (lastActive!=active) { // NEW HAND
				//call = -1;
				newHand();
				lastActive=active;
			}
		} else {
			active = false;
			if (lastActive!=active) {
				EngineLogical.dispatcher.dispatchEvent(new AvalEvent(AvalEvent.CHANGE_MY_HAND_ACTIVE, this));
				lastActive=active;
			}
		}
		
		rawCheckRaise  = controlCheckRaise.rawString;
		if (!rawCheckRaise.equals(lastRawCheckRaise)) {
			dispatchActiveOrPassiveTurn();
			lastRawCheckRaise = rawCheckRaise;
		}
		
		textFieldStr = dealer.rawString+"^"+card1letter.rawString+AvalancheUtil.createSymbols(card1symbol.rawString)+
				card2letter.rawString+AvalancheUtil.createSymbols(card2symbol.rawString)+"^"
				+controlCheckRaise.rawString+"^"+controlFold.rawString+"^"+betRegion.rawString+"^"+chipsRegion.rawString+"^"
				+allin.rawString+"^"+raiseinput.rawString;
		
		//if (callActive) {}
		updateLabel(textFieldStr);
		
		//Avalanche.tempImg.dispose();
		refGC.dispose();
	}
}
public boolean rawHandIsValidExists () {
	return (rawHand.length()==4 && handIsValid(rawHand) && Cards.validCardSequence(rawHand));
}
public boolean isLowestTier () {
	return Player.cardValue().equals("u");
}
public boolean handIsValid (String str) {
	if (str.length()==0 || str.length()==4) {
		return true;
	}
	return false;
}
public Position calculatePosition () {
	if (dealer.rawString.equals("dealer")) { // player is dealer
		return Position.LATE;
	}
	int i = 0, dealer=0;
	for (Opponent oppon : Opponent.all) {
		if (oppon.dealer.rawString.equals("dealer")) {
			dealer = i;
		}
		//if () {
			
		//}
		i++;
	}
	int difference = dealer-4;
	
	return null;
}
public String pocketValue () {
	return Player.cardValue();
}
}