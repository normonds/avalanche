package avalanche;

import java.awt.image.BufferedImage;

import org.json.JSONArray;

public class Region {

//public String name = "";
public float threshold;
public BufferedImage last;
public String font = "";
public int[] region = new int[]{};

public Region (JSONArray reg) {
	region = new int[]{reg.getInt(0), reg.getInt(1), reg.getInt(2), reg.getInt(3)};
//	name = nm;
//	region = reg;
}

}