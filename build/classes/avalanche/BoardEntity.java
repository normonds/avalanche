package avalanche;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Text;

public class BoardEntity {
public int cPx = 0, cPy = 0;
public GC gcc;
//public Map<String,Region> regions = new HashMap<String,Region>();
//public boolean					isPlayer = false, isBoard = false, isOpponent = false, isMe = false;
//public int[]						region, regionCard, regionBet, regionChips;
//public String					label="", rawLetters="", rawSymbols="", lastValidString="";
public Canvas					canvas;
public Text						textField;
public String 					lastTextFieldString="";
//public BufferedImage		lastImg = null;
public Color green 			= new Color(Avalanche.display, 0, 255, 0);
//public int curSide			= 0;

public void updateLabel (String str) {
	if (!lastTextFieldString.equals(str) && textField!=null) {
		textField.setText(str);
		lastTextFieldString = str;
	}
}
/*public void updateVisuals (ImageData tmpImageData, String opponStr) {}
public void updateVisualsPost () {
	//System.out.println(isOpponent);
}
public void imgInitCheck () {
	if (lastImg==null) {
		//lastImg = new BufferedImage(region[2], region[3], BufferedImage.TYPE_INT_RGB);
	}
}
public BufferedImage lastImg () {
	imgInitCheck();
	return lastImg;
}
public BufferedImage getSubimg (BufferedImage img) {
	return null;//img.getSubimage(region[0], region[1], region[2], region[3]);
}
public void updateLastImg (BufferedImage scanSubImg) {
	lastImg.flush();
	lastImg = new BufferedImage(scanSubImg.getWidth(), scanSubImg.getHeight(), BufferedImage.TYPE_INT_RGB);
	lastImg.setData(scanSubImg.getData());
}
public boolean insideAnyRegion (BufferedImage img) {
	for (Region region : regions.values()) {
		if (region.region[0]+region.region[2]>img.getWidth() || region.region[1]+region.region[3]>img.getHeight()) {
			return false;
		}
	}
	return true;
}
public void rawLetters (String replace) {
	rawLetters = replace.replace(" ", "").replace("X", "T");
}
public void rawSymbols (String replace) {
	rawSymbols = replace.replace(" ", "");
}
public void updLabel () {
	if (isPlayer || isBoard) {
		textField.setText(rawLetters+"-"+rawSymbols);
	}
}
public boolean empty () {
	return rawLetters.length()==0;
}*/

}