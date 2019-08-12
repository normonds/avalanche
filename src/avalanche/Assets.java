package avalanche;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import avalanche.Avalanche.RegionParent;

public class Assets {
//public static Map <String,Region> all = new HashMap<String,Region>();
public static String regionFile;
public static JSONObject json;
public static int[]	safeClick;
public static void load (String dir, String file) {
	//all.clear();
	//file = "regions.zynga.9players.json";
	regionFile = "";//regions.zynga.9players.json";//regions.zynga.9players.json";
	
	System.out.println(System.getProperty("user.dir"));
	for(String str : AvalancheUtil.loadRegionFile(new File(dir), file)) {
		System.out.println("str"+str);
		regionFile += str+"\n";
	}
	System.out.println(dir+ file);
	json = new JSONObject(regionFile);
	
	safeClick = new int[]{json.getJSONObject("settings").getJSONArray("safeClick").getInt(0),
			json.getJSONObject("settings").getJSONArray("safeClick").getInt(1)};
	parseRegionJSON();
 //	System.out.println("----------------------------");
//	System.out.println("----------------------------");
}
//public static Map<String,Region> getOppons () {
//	Map<String,Region> ret = new HashMap<String,Region>();
//	for (Map.Entry<String, Region> entry : all.entrySet()) {
//		if (entry.getKey().startsWith("oppon")) {
//			ret.put(entry.getKey(), entry.getValue());
//		}
//	}
//	return ret;
//}
public static int[] parseRegionDot (String str, int radius) {
	String[] parse = str.split(",");
	return new int[]{Integer.parseInt(parse[0])-radius, Integer.parseInt(parse[1])-radius, 
			radius*2, radius*2};
}
public static int[] parseRegion (String str) {
	System.out.println(str);
	String[] parse = str.split(",");
	return new int[]{Integer.parseInt(parse[0]), Integer.parseInt(parse[1]), 
			Integer.parseInt(parse[2]), Integer.parseInt(parse[3])};
}
public static void parseRegionJSON () {
	for (String l1 : json.keySet()) {
		if (l1.equals("board")) {
			EngineLogical.board = new Board(json.getJSONObject("board"));
//			new BoardRegion(json.getJSONObject("board").getJSONObject("cards"), RegionParent.BOARD);
//			new BoardRegion(json.getJSONObject("board").getJSONObject("pot"), RegionParent.BOARD);
		} else if (l1.equals("player")) {
			EngineLogical.me = new MyHand(json.getJSONObject("player"));
//			new BoardRegion(json.getJSONObject("player").getJSONObject("cards"), RegionParent.ME);
//			new BoardRegion(json.getJSONObject("player").getJSONObject("bet"), RegionParent.ME);
//			new BoardRegion(json.getJSONObject("player").getJSONObject("chips"), RegionParent.ME);
		} else if (l1.equals("oppons")) {
			for (int n=0;n<json.getJSONArray("oppons").length();n++) {
				new Opponent(json.getJSONArray("oppons").getJSONObject(n));
				//Player.opponents.add(new Player(, RegionParent.OPPONENT));
			}
		}
		//System.out.println((key));
//		if (l1.equals("board")) {
//			Avalanche.board = new Board();
//			Avalanche.board.addCardRegion(json.getJSONObject("board").getJSONObject("cards"));
//			Avalanche.board.addPotRegion(json.getJSONObject("board").getJSONObject("pot"));
//			ret = SWTAssets.addImgComp("board", 100, 20,4); // Avalanche.board.region[2]+15, Avalanche.board.region[3]+20, 4);
//			Avalanche.board.canvas = (Canvas) ret[0];
//			Avalanche.board.textField = (Text) ret[1];
//			Avalanche.board.label = "board";
//		}
	}
	/*for (String line : regionsLines2) {
		String[] lineSplit = line.split("=");
		String[] incRegions = lineSplit[1].split(",");
		int[] outRegions = new int[]{};
		String name = lineSplit[0];
		
		if (lineSplit[0].equals("board")) {
			Avalanche.board = new Board();
			Avalanche.board.region = new Region("");//parseRegion(lineSplit[1]);
			ret = SWTAssets.addImgComp("board", Avalanche.board.region[2]+15, Avalanche.board.region[3]+20, 4);
			Avalanche.board.canvas = (Canvas) ret[0];
			Avalanche.board.textField = (Text) ret[1];
			Avalanche.board.label = "board";
		} else if (lineSplit[0].equals("player")) {
			Avalanche.me = new MyHand();
			Avalanche.me.region = parseRegion(lineSplit[1]);
			ret = SWTAssets.addImgComp("hand", Avalanche.me.region[2]+15, Avalanche.me.region[3]+20, 2);
			Avalanche.me.canvas = (Canvas) ret[0];
			Avalanche.me.textField = (Text) ret[1];
			Avalanche.me.label = "hand";
			System.out.println("adding player "+Avalanche.me.textField.getText());
		} else if (lineSplit[0].equals("oppon")) {
			Player oppon = new Player();
			oppon.isOpponent = true;
			ret = SWTAssets.addImgComp("oppon"+opponNum);
			oppon.canvas = (Canvas) ret[0];
			oppon.textField = (Text) ret[1];
			oppon.label = "oppon"+opponNum;
			
			Player.opponents.add(oppon);
			String[] opponSplit = lineSplit[1].split(";");
			name += opponNum;
			opponNum++;
			
			for (String opponLine : opponSplit) {
				String[] opponVar = opponLine.split(":");
				if (opponVar[0].equals("card")) {
					oppon.regionCard = parseRegionDot(opponVar[1], 10);
				} else if (opponVar[0].equals("bet")) {
					oppon.regionBet = parseRegion(opponVar[1]);
				} else if (opponVar[0].equals("chips")) {
					oppon.regionChips = parseRegion(opponVar[1]);
				}
			}
		} else if (lineSplit[0].startsWith("dot")) {
			outRegions = new int[]{Integer.parseInt(incRegions[0]), Integer.parseInt(incRegions[1])};
		} else {
			outRegions = new int[]{Integer.parseInt(incRegions[0]), Integer.parseInt(incRegions[1]),
				Integer.parseInt(incRegions[2]), Integer.parseInt(incRegions[3])};
		}
		//all.put(name, new Region("", outRegions));
	}*/
	//System.out.println(all);
}
//Avalanche.botComp.layout(true, true);
}