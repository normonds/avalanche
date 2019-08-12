package avalanche;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Text;
import org.json.JSONObject;
import avalanche.Avalanche.RegionParent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;

public class Opponent extends Player {
public BoardRegion cardBack;
public static ArrayList<Opponent> all = new ArrayList<Opponent>();
public Color green = new Color(Avalanche.display, 0, 255, 0);
public String rawFolded = "", lastRawFolded="";
public boolean active = false;
public String betVal = "", lastBetVal = "";
public static int lastActiveOppons = 1;

public static void flush () {
	for (Opponent oppon : all) {
		oppon.cardBack.lastBitmap = null;
		//oppon.cardBack.lastBitmap = null;
	}
}
public static void setActiveOppons (int n) {
	Avalanche.opponLabel.setText("♟"+String.valueOf(n));
}
public static String opponStr () {
	return opponStr(activeOppons()>0 ? activeOppons() : lastActiveOppons);
}
public static String opponStr (int count) {
	String ret = "";
	Avalanche.trace(count);
	for (int i=0;i<count;i++) {
		ret += ",*";
	}
	return ret;
}
public static int activeOppons () {
	int ret = 0;
	for (Opponent oppon : all) {
		if (oppon.active) ret++; 
	}
	setActiveOppons(ret);
	return ret;
}
public Opponent (JSONObject json) {
	super(json, RegionParent.OPPONENT);
	cardBack = new BoardRegion(json.getJSONObject("cardback"), RegionParent.OPPONENT);
	bet = new BoardRegion(json.getJSONObject("bet"), RegionParent.OPPONENT);
	//bet.invert = true;
	chips = new BoardRegion(json.getJSONObject("chips"), RegionParent.OPPONENT);
	Object [] ret = AssetsSWT.addImgComp("oppon", Avalanche.defaultSWTGridItemWidth, 20,1);
	canvas = (Canvas) ret[0];
	textField = (Text) ret[1];
	all.add(this);
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
		
		cardBack.process(screen, refGC, redraw, 0, 0);
		dealer.process(screen, refGC, redraw, cardBack.region[2], 0);
		bet.process(screen, refGC, redraw, 0, cardBack.region[3]+1);
		chips.process(screen, refGC, redraw, 0, cardBack.region[3]+1+bet.region[3]+1);
		
		rawFolded = cardBack.rawString.replaceAll("\\r|\\n", "");//+bet.rawString;
		
		if (!bet.rawString.equals(lastBetVal)) {
			lastBetVal = bet.rawString;
			
			//System.out.println("oppon bet");
		}
		//lastActiveOppons = Opponent.activeOppons();
		if (!rawFolded.equals(lastRawFolded)) {
			if (rawFolded.length()>0 /* || bet.rawString.length()>0*/) {
				if (!active && EngineLogical.board.validCards.length()==0) { // prevent adding opponent if board cards exist
					active = true;
					Opponent.activeOppons();
					EngineLogical.dispatcher.dispatchEvent(new AvalEvent(AvalEvent.CHANGE_OPN_ADD, this));
				}
			} else if (active) {
				active = false;
				Opponent.activeOppons();
				EngineLogical.dispatcher.dispatchEvent(new AvalEvent(AvalEvent.CHANGE_OPN_FOLD, this));
			}
			lastRawFolded = rawFolded;
		}
		
		updateLabel((dealer.rawString.equals("dealer")?"D":"")+"^"+cardBack.rawString.replaceAll("\\r|\\n", "") + "^"+bet.rawString+"^"+chips.rawString);

		//Avalanche.tempImg.dispose();
		refGC.dispose();
	}
}
}