package avalanche;

import static avalanche.EngineLogical.lastInfoStr;
import java.text.SimpleDateFormat;
import java.util.Date;

public class History {
public static String one="", two="";

public static void capturePostPlay () {
	if (!one.equals("")) {
		EngineLogical.trace("saving:"+two);
		AvalancheUtil.appendToFile("history.log", "		history:"+two);
	}
	flush();
}
public static void capturePlay () {
	Date dNow = new Date();
    SimpleDateFormat ft = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss");
    one = "\n"+ft.format(dNow)
    		+"		chips:"+EngineLogical.me.chips
    		+"		eq:"+Avalanche.df.format(EngineLogical.equity)
		+"		EV:"+EngineLogical.lastEV
    		+"		me:"+EngineLogical.me.validHand
    		+"		call:"+EngineLogical.me.call
    		+"		opn:"+Opponent.activeOppons()+"/"+EngineLogical.me.initOppons
    		+"		board:"+EngineLogical.board.validCards 
    		+" 	(lastb:"+EngineLogical.board.lastNonEmptyBoard+") folded:"+EngineLogical.me.folded
    		+" 	pot:"+EngineLogical.board.validPot
		+ "		info:"+EngineLogical.lastInfoStr.replaceAll(" ", "+");
    		//+ "	history:"+two;
    EngineLogical.trace("saving:"+one);
    AvalancheUtil.appendToFile("history.log", one);
}
public static void flush () {one=""; two="";}
}