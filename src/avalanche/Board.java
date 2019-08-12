package avalanche;

import java.awt.image.BufferedImage;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Text;
import org.json.JSONObject;
import avalanche.Avalanche.RegionParent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;

public class Board extends BoardEntity {
public BoardRegion pot;
public BoardRegion	flop1Letter, flop1Symbol, flop2Letter, flop2Symbol, flop3Letter, flop3Symbol, turnLetter, turnSymbol,
	riverLetter, riverSymbol;
public String rawCards="", lastRawCards="", validCards="", lastRawPot="", rawPot="", textFieldStr="", lastNonEmptyBoard="";
public double validPot=0, lastValidPot=0;

public Board (JSONObject json) {
	pot = new BoardRegion(json.getJSONObject("pot"), RegionParent.BOARD);
	flop1Letter = new BoardRegion(json.getJSONObject("cards").getJSONObject("flop1").getJSONObject("letter"), RegionParent.BOARD);
	flop1Symbol = new BoardRegion(json.getJSONObject("cards").getJSONObject("flop1").getJSONObject("symbol"), RegionParent.BOARD);
	flop2Letter = new BoardRegion(json.getJSONObject("cards").getJSONObject("flop2").getJSONObject("letter"), RegionParent.BOARD);
	flop2Symbol = new BoardRegion(json.getJSONObject("cards").getJSONObject("flop2").getJSONObject("symbol"), RegionParent.BOARD);
	flop3Letter = new BoardRegion(json.getJSONObject("cards").getJSONObject("flop3").getJSONObject("letter"), RegionParent.BOARD);
	flop3Symbol = new BoardRegion(json.getJSONObject("cards").getJSONObject("flop3").getJSONObject("symbol"), RegionParent.BOARD);
	turnLetter = new BoardRegion(json.getJSONObject("cards").getJSONObject("turn").getJSONObject("letter"), RegionParent.BOARD);
	turnSymbol = new BoardRegion(json.getJSONObject("cards").getJSONObject("turn").getJSONObject("symbol"), RegionParent.BOARD);
	riverLetter = new BoardRegion(json.getJSONObject("cards").getJSONObject("river").getJSONObject("letter"), RegionParent.BOARD);
	riverSymbol = new BoardRegion(json.getJSONObject("cards").getJSONObject("river").getJSONObject("symbol"), RegionParent.BOARD);
	//bet = new BoardRegion(json.getJSONObject("bet"), RegionParent.OPPONENT);
	//chips = new BoardRegion(json.getJSONObject("chips"), RegionParent.OPPONENT);
	Object [] ret = AssetsSWT.addImgComp("board", pot.region[2]+150, 30, 6);
	canvas = (Canvas) ret[0];
	textField = (Text) ret[1];
	canvas.addPaintListener(new PaintListener() {
		public void paintControl (PaintEvent e) {
			e.gc.drawLine(0,0,((Canvas)e.getSource()).getSize().x,((Canvas)e.getSource()).getSize().y);
			redraw(e.gc);
		}
	});
}
public void redraw (GC refGC) {
	updateVisuals(Avalanche.screenShot, refGC, true);
}
public void updateVisuals (BufferedImage screen) {
	updateVisuals(screen, gcc, false);
}
public void updateVisuals (BufferedImage screen, GC refGC, boolean redraw) {
	if (canvas!=null) {
		
		if (refGC==null) {
			refGC = new GC(canvas);
			refGC.setForeground(green);
		}
		

		
		cPx = pot.region[2]; cPy = 0;//pot.region[3];
		pot.process(screen, refGC, redraw, 0, 0);
		flop1Letter.process(screen, refGC, redraw, cPx, cPy); cPx += flop1Letter.region[2];
		flop1Symbol.process(screen, refGC, redraw, cPx, cPy); cPx += flop1Symbol.region[2];
		flop2Letter.process(screen, refGC, redraw, cPx, cPy); cPx += flop2Letter.region[2];
		flop2Symbol.process(screen, refGC, redraw, cPx, cPy); cPx += flop2Symbol.region[2];
		flop3Letter.process(screen, refGC, redraw, cPx, cPy); cPx += flop3Letter.region[2];
		flop3Symbol.process(screen, refGC, redraw, cPx, cPy); cPx += flop3Symbol.region[2];
		cPx = pot.region[2]; cPy = flop3Symbol.region[3];
		turnLetter.process(screen, refGC, redraw, cPx, cPy); cPx += turnLetter.region[2];
		turnSymbol.process(screen, refGC, redraw, cPx, cPy); cPx += turnSymbol.region[2];
		riverLetter.process(screen, refGC, redraw, cPx, cPy); cPx += riverLetter.region[2];
		riverSymbol.process(screen, refGC, redraw, cPx, cPy);
		
		rawCards = flop1Letter.rawString+flop1Symbol.rawString
				+flop2Letter.rawString+flop2Symbol.rawString
				+flop3Letter.rawString+flop3Symbol.rawString
				+turnLetter.rawString+turnSymbol.rawString
				+riverLetter.rawString+riverSymbol.rawString;
		
		rawPot = pot.rawString;

		if (rawCards.equals("")) {
			//Avalanche.trace("not valid cards");
			refGC.setBackground(yellow);
			refGC.fillRectangle(0, 50, 300, 40);
		} else if (rawCards.length()>0 && !Cards.validCardSequence(rawCards)) {
			refGC.setBackground(red);
			refGC.fillRectangle(0, 50, 300, 40);
		}
		
		if (!rawCards.equals(lastRawCards)) {
			
			if (cardsSizeValid(rawCards) && Cards.validCardSequence(rawCards) && !rawCards.equals(validCards)) {
				validCards = rawCards;
				if (hasCards()) {lastNonEmptyBoard = validCards;}
				EngineLogical.dispatcher.dispatchEvent(new AvalEvent(AvalEvent.CHANGE_BOARD, this));
			}
			lastRawCards = rawCards;
		}
		if (!rawPot.equals(lastRawPot)) {
			validPot = sumPot(pot.rawString);
			if (validPot!=lastValidPot) {
				EngineLogical.dispatcher.dispatchEvent(new AvalEvent(AvalEvent.CHANGE_POT, this));
				//Avalanche.setEquity(EngineLogical.equity);
				lastValidPot = validPot;
			}
			lastRawPot = rawPot;
		}
		
		textFieldStr = flop1Letter.rawString+AvalancheUtil.createSymbols(flop1Symbol.rawString)+
				flop2Letter.rawString+AvalancheUtil.createSymbols(flop2Symbol.rawString)+
				flop3Letter.rawString+AvalancheUtil.createSymbols(flop3Symbol.rawString)+
				turnLetter.rawString+AvalancheUtil.createSymbols(turnSymbol.rawString)+
				riverLetter.rawString+AvalancheUtil.createSymbols(riverSymbol.rawString)
				+"^"+pot.rawString+"^"+sumPot(pot.rawString);
		updateLabel(textFieldStr);

		//Avalanche.tempImg.dispose();
		refGC.dispose();
	}
}
public boolean cardsSizeValid (String str) {
	if (str.length()==0 || str.length()==6 || str.length()==8 || str.length()==10) {
		return true;
	}
	return false;
}
public int sumPot (String inc) {
	if (inc.equals("")) return 0 ;
	int ret = 0;
	if (inc.startsWith("pot")) inc = inc.substring(3);
	String[] split = inc.split("\\$");
	for (String sum : split) {
		try {
			ret += Integer.valueOf(sum);
		} catch (NumberFormatException e) {}
	}
	return ret;
}
public boolean hasCards () {
	if (validCards.length()>0) {
		return true;
	}
	return false;
}
//public String boardState () {
//	if (lastValidString.length()==6) {
//		return "flop";
//	} else if (lastValidString.length()==8) {
//		return "turn";
//	} else if (lastValidString.length()==10) {
//		return "river";
//	}
//	return "none";
//}
/*public Board () {isBoard = true;}
public String handFromRaw () {
	String ret = "";
	for (int i=0;i<((rawSymbols.length()<rawLetters.length())?rawSymbols.length():rawLetters.length());i++) {
		ret += (rawLetters.substring(i, i+1)+rawSymbols.substring(i,i+1));
	}
	return ret;
}

public boolean validate () {
	if (rawIsValid()) {
		if (!handFromRaw().equals(lastValidString)) {
			lastValidString = handFromRaw();
			return true;
		}
	}
	return false;
}
public boolean rawIsValid () {
	if (rawLetters.length()==rawSymbols.length() && 
			(rawLetters.length()==0 || rawLetters.length()==3 || rawLetters.length()==4 || rawLetters.length()==5)
			&& (rawSymbols.length()==0 || rawSymbols.length()==3 || rawSymbols.length()==4 || rawSymbols.length()==5)) {
		return true;
	} else return false;
}
public void addCardRegion (JSONObject jsonObject) {
	regions.put("board", new Region(jsonObject.getJSONArray("region")));
	regions.get("board");
}
public void addPotRegion (JSONObject jsonObject) {
	regions.put("pot", new Region(jsonObject.getJSONArray("region")));
	regions.get("pot");
}*/

}