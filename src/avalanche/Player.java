package avalanche;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Text;
import org.json.JSONObject;

import avalanche.Avalanche.Position;
import avalanche.Avalanche.RegionParent;

public class Player extends BoardEntity {

public BoardRegion bet, chips, dealer;
//public int[] regionCard, regionBet, regionChips;
//public boolean folded = true;

public static Map<String, CardPair> hand2any = new HashMap<String,CardPair>();
public Player (JSONObject json, RegionParent type) {
	super();
	dealer = new BoardRegion(json.getJSONObject("dealer"), RegionParent.ME);
}
public static void loadHandEquity (String dir, String file) {//"2hand.all.montecarlo.txt"
	List<String>basicHands = AvalancheUtil.readFile(dir, file);
	int i=0;
	for (String line : basicHands) {
		if (i>0) {
		String[] split = line.replaceAll("\\t", "").split(",");
		hand2any.put(split[0], new CardPair(split));
			/*new double[]{Double.parseDouble(split[1]), Double.parseDouble(split[2]), Double.parseDouble(split[3]),
			Double.parseDouble(split[4]), Double.parseDouble(split[5]), Double.parseDouble(split[6]), Double.parseDouble(split[7]),
			Double.parseDouble(split[8]), Double.parseDouble(split[7])}*/
		}
		i++;
	}
}
public static CardPair myPocket () {
	if (hand2any.containsKey(toAbstract(false))) {
		//System.out.println(toAbstract(false) + " " + hand2any.get(toAbstract(false)).position);
		return hand2any.get(toAbstract(false));//[Opponent.activeOppons()-1];///100;
	} else if (hand2any.containsKey(toAbstract(true))) {
		//System.out.println("Equity opps:"+activeOppons());
		//System.out.println(toAbstract(true) + " " + hand2any.get(toAbstract(true)).position);
		return hand2any.get(toAbstract(true));//[Opponent.activeOppons()-1];///100;
	}
	return null;
}
public static String cardValue () {
	if (hand2any.containsKey(toAbstract(false))) {
		System.out.println(toAbstract(false) + " " + hand2any.get(toAbstract(false)).position);
		return hand2any.get(toAbstract(false)).position;//[Opponent.activeOppons()-1];///100;
	} else if (hand2any.containsKey(toAbstract(true))) {
		//System.out.println("Equity opps:"+activeOppons());
		System.out.println(toAbstract(true) + " " + hand2any.get(toAbstract(true)).position);
		return hand2any.get(toAbstract(true)).position;//[Opponent.activeOppons()-1];///100;
	} else {
		//System.out.println("parsed:"+toAbstract(false) + " (" + handFromRaw() + ") valid:"+rawIsValid()+" not found in hand2any");
	}
	return "u";
}
public static double equity () {
	if (Opponent.activeOppons()>-1) {
		if (hand2any.containsKey(toAbstract(false))) {
			return hand2any.get(toAbstract(false)).equity;//[Opponent.activeOppons()-1];///100;
		} else if (hand2any.containsKey(toAbstract(true))) {
			//System.out.println("Equity opps:"+activeOppons());
			return hand2any.get(toAbstract(true)).equity;//[Opponent.activeOppons()-1];///100;
		} else {
			//System.out.println("parsed:"+toAbstract(false) + " (" + handFromRaw() + ") valid:"+rawIsValid()+" not found in hand2any");
		}
	}
	return -1;
}
public static String toAbstract (boolean reversed) {
	if (EngineLogical.me.validHand.equals("")) {
		return "";
	} else if (reversed) {
		return EngineLogical.me.validHand.substring(0,1)+EngineLogical.me.validHand.substring(2,3)+(!isSuited()&&!isPair()?"o":"");
	} else {
		return EngineLogical.me.validHand.substring(2,3)+EngineLogical.me.validHand.substring(0,1)+(!isSuited()&&!isPair()?"o":"");
	}
}
public static boolean isPair () {
	return EngineLogical.me.validHand.substring(0,1).equals(EngineLogical.me.validHand.substring(2,3));
}
public static boolean isSuited () {
	return EngineLogical.me.validHand.substring(1,2).equals(EngineLogical.me.validHand.substring(3,4));
}
//public boolean hasHand () {	return false;}
//public int equity () {	return 0;}
//public boolean rawIsValid () {	return false;}
//public static int activeOppons () {	return 0;}
//public String lastValidString () {	return "";}
/*
public boolean hasChanged () {
	if (rawIsValid()) {
		if (!handFromRaw().equals(lastValidString)) {
			//System.out.println("hand change "+handFromRaw()+" - "+lastValidString);
			lastValidString = handFromRaw();
			return true;
		}
	}
	return false;
}
public boolean hasHand () {
	return (lastValidString.length()==4);
}

public boolean isPair () {
	return lastValidString.substring(0,1).equals(lastValidString.substring(2,3));
}
public boolean isSuited () {
	return lastValidString.substring(1,2).equals(lastValidString.substring(3,4));
}
public boolean validOpps () {
	return (activeOppons()>0&&activeOppons()<9);
}


public String handFromRaw () {
	return (rawLetters.length()>1 && rawSymbols.length()>1) ?
			rawLetters.substring(0,1)+rawSymbols.substring(0,1)+rawLetters.substring(1,2)+rawSymbols.substring(1,2) : "";
}
public boolean rawIsValid () {
	if ((rawLetters.length()==2 && rawSymbols.length()==2) || (rawLetters.length()==0 && rawSymbols.length()==0)) {
		return true;
	} else return false;
}
public static int activeOppons () {
	int ret = 0;
	for (Player oppon : Player.opponents) {
		if (!oppon.folded) {
			ret++;
		}
	}
	return ret;
}
@Override
public void updateVisualsPost () {
	if (isOpponent) {
		if (!folded) {
			textField.setText("ACTV "+rawLetters);
		} else {
			textField.setText("FOLD "+rawLetters);
		}
	}
}
public BufferedImage lastBet () {
	
	return null;
}
*/
}