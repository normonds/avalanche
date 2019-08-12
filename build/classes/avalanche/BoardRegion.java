package avalanche;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.json.JSONObject;


import com.github.axet.lookup.common.ImageBinaryGrey;
import avalanche.Avalanche.RegionParent;

public class BoardRegion {
public static ArrayList<BoardRegion> all = new ArrayList<BoardRegion>();

public RegionParent type;
public int[] region;
public double ocrThreshold=-1;
public String font, safeString="", rawString="";
public BufferedImage lastBitmap;
public int curSide = 0;
public Canvas canvas;
public boolean invert = false;
public BufferedImage  tempSubImg;
public Image tempImg;
public boolean trace = false;

public static void flush () {
	for (BoardRegion region : all) {
		if (region.type!=RegionParent.ME) {
			region.rawString = "";
		}
	}
}
public BoardRegion (JSONObject reg, RegionParent type_) {
	all.add(this);
	type = type_;
	region = new int[]{reg.getJSONArray("region").getInt(0), reg.getJSONArray("region").getInt(1),
		reg.getJSONArray("region").getInt(2), reg.getJSONArray("region").getInt(3)};
	if (reg.has("ocrThreshold")) {ocrThreshold = reg.getDouble("thres");}
	if (reg.has("font")) { font = reg.getString("font");
//		if (reg.optJSONArray("font")!=null) {
//			font = reg.getJSONArray("font").getString(0);
//		} else if (reg.optJSONObject("font")!=null) { // board cards
//			font = reg.getJSONObject("font").getJSONArray("letters").getString(0);
//			//font2 = reg.getJSONObject("font").getJSONArray("symbols").getString(0);
//		}
	}
//	int itemWidth = Avalanche.defaultSWTGridItemWidth;
//	if (region[2]>itemWidth) itemWidth = region[2]+10;
//	Object [] ret = SWTAssets.addImgComp("board", itemWidth, 20,1);
//	canvas = (Canvas) ret[0];
//	textField = (Text) ret[1];
}


public boolean insideRegion (BufferedImage screenShot) {
	return (region[0]+region[2]<screenShot.getWidth() && region[1]+region[3]<screenShot.getHeight());
}
public boolean newBitmapIsDifferent (BufferedImage subImg) {
	if (lastBitmap==null) {
		lastBitmap = subImg;
		return true;
	}
	return !AvalancheUtil.compareBitmaps(lastBitmap, subImg);
}
public String bitmapParse (BufferedImage screenShot) {
	Avalanche.imgGray = new ImageBinaryGrey(screenShot);
	if (ocrThreshold>-1) {Avalanche.ocr.setThreshold((float) ocrThreshold);}
	if (font!=null) {
		rawString = Avalanche.ocr.recognize(Avalanche.imgGray, font).replace(" ", "");
		rawString = rawString.replaceAll("(\\r|\\n|\\r\\n)+", "");
	}
//	if (font2!=null) {
//		rawString2 = Avalanche.ocr.recognize(Avalanche.imgGray, font2).replace(" ", "");
//	}
	Avalanche.ocr.setThreshold(Avalanche.ocrDefaultThreshold);
	return rawString;
//	if ((type==RegionParent.OPPONENT || type==RegionParent.ME) && rawString!=null) {
//		if (rawString.length()==0 || rawString.length()==2) {
//			return true;
//		} else return false;
//	}
//	return true;
}
public boolean valuesChanged () {
	if (safeString==null || rawString==null) return false;
	if (!safeString.equals(rawString)) {
		return false;
//		if (rawString2!=null) {
//			if (!safeString2.equals(rawString2)) {
//				return true;
//			}
//		} else return true;
	}
	return false;
}
public void updateNewValues () {
	safeString = rawString;
//	if (rawString2 != null) {
//		safeString2 = rawString2;
//	}
}
public void updateBitmap (BufferedImage subImg) {	lastBitmap = subImg;}

public BufferedImage getSubimg (BufferedImage screenShot) {
	return screenShot.getSubimage(region[0], region[1], region[2], region[3]);
}
/**
 * @param offX - gcc offset
 * @param offY - gcc offset
 */
public void process (BufferedImage screen, GC gcc, boolean redraw, int offX, int offY) {
	if (insideRegion(screen)) {
		
		tempSubImg = getSubimg(screen);
		if (invert) tempSubImg = AvalancheUtil.invertImage(tempSubImg, true);
		if (newBitmapIsDifferent(tempSubImg) || redraw) {
			
			if (trace && redraw) Avalanche.trace("redraw region");
			tempImg = new Image(Avalanche.display, AvalancheUtil.convertToSWT(tempSubImg));
			bitmapParse(tempSubImg);
			gcc.drawImage(tempImg, offX, offY);
			drawNextSide(gcc, offX, offY, tempImg.getImageData().width-1, tempImg.getImageData().height-1);
			updateBitmap(tempSubImg);
			
			tempImg.dispose();
		}
		//if (trace) Avalanche.trace("w:"+);
		//gcc.drawLine(0, 0, 44, 44);
		//gcc.drawText("a", 0, 0);
	}
	
//	if (bitmapParse(Avalanche.tempSubImg).length()>1) { // zynga oppon card back at least 2 corners
//		updateNewValues();
//	}
}
public void drawNextSide (GC gc, int offX, int offY, int w, int h) {
	if (curSide ==0) gc.drawLine(offX+0, offY+0, offX+w, offY+0);
	else if (curSide==1) {gc.drawLine(offX+w, offY+0, offX+w, offY+h);}
	else if (curSide==2) {gc.drawLine(offX+w, offY+h, offX+0, offY+h);}
	else if (curSide==3) {gc.drawLine(offX+0, offY+h, offX+0, offY+0);}
	curSide++;
	if (curSide>=4) {curSide=0;}
}

}