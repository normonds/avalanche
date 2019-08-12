package mi.poker.calculation;

import java.awt.AWTException;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
//import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
//import java.awt.RenderingHints;
import java.awt.Robot;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
//import java.awt.image.Raster;
import java.io.Console;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
//import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;

import org.apache.commons.lang.StringUtils;

//import mi.poker.calculation.EquityCalculation;
//import mi.poker.calculation.Result;

import com.github.axet.lookup.OCR;
import com.github.axet.lookup.common.ImageBinaryGrey;
import enumeration.EnumerateWindows;

//import enumeration.EnumerateWindows;

public class OCRTest {
public static OCR ocr;
public static String str = "",playerLetters="",playerSymbols="", boardStrings="", boardSymbols="";
public static BufferedImage screenShot, cachedBoard, cachedPlayer;
public static double milis;
public static double diff;
public static int randomPlayers, cacheLvL, lastRandPlayers = 0;

static public void main(String[] args) throws AWTException, IOException /*throws AWTException, IOException*/, InterruptedException {
	//Timer timer = new Timer();

	/*timer.schedule(new TimerTask() {
	  @Override public void run() {
	    // Your database code here
	  }
	}, 2*60*1000);*/
	int n = 0;
	int pair = 0;
	String outStr = "";
	String addStr = "";//",34.344,34.344,34.344,34.344,34.344,34.344,34.344,34.344";
	String[] cards = new String[]{"A","K","Q","J","T","9","8","7","6","5","4","3","2"};
	for (int a=0;a<cards.length;a++) {
		for (int b=Arrays.asList(cards).indexOf(cards[a]);b<cards.length;b++) {
			n++;
			if (cards[a]==cards[b]) {
				pair++;
			}
			outStr +=(cards[a]+cards[b]+"s"+addStr+"\n");
		}
	}
	for (int a=0;a<cards.length;a++) {
		for (int b=Arrays.asList(cards).indexOf(cards[a]);b<cards.length;b++) {
			if (cards[a]!=cards[b]) {
				outStr += (cards[a]+cards[b]+"o"+addStr+"\n");
				n++;
				//pair++;
			}
		}
	}
	String readfile = "";
	try {
        FileReader fileReader = new FileReader("filename.txt");
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line;
		while((line = bufferedReader.readLine()) != null) {		readfile +=(line);       }    
        bufferedReader.close();            
    } catch (FileNotFoundException ex) {        System.out.println("Unable to open file");                 }
    catch (IOException ex) {       System.out.println("Error reading file");                }
	PrintWriter out = new PrintWriter("filename.txt");
	out.write(readfile+outStr);
	out.close();
	//out.write(arg0)
	//System.out.println(outStr);
	System.out.println("loops:"+" ("+n+")");
	System.out.println("pair:"+pair);
	//System.out.println(EquityCalculation.calculateMonteCarlo("AsAd,*","",""));
	/*timer.scheduleAtFixedRate(new TimerTask() {
	  @Override public void run() {
		  try {
			processScreen();
		} catch (InterruptedException e) {			e.printStackTrace();
		} catch (AWTException e) {						e.printStackTrace();
		} catch (IOException e) {						e.printStackTrace();}
	  }
	}, 3000, 3000);
	milis = System.currentTimeMillis();
    ocr = new OCR(0.90f);*/
    //EnumerateWindows.printProcess();
        
    //System.out.println("screen captured:"+(System.currentTimeMillis()-milis) + "msecs");
    // will go to com/github/axet/lookup/fonts folder and load all font
    // familys (here is only font_1 family in this library)
   // System.out.println("loading fonts");
    //ocr.loadFontsDirectory(OCRTest.class, new File("fonts"));
    //System.out.println("symbols:"+ocr.getSymbols("pokerstars.card.letters").size());

    //System.out.println(EquityCalculation.calculateMonteCarlo("kd2d,*,*,*,*","Js8d2s",""));
    // example how to load only one family
    // "com/github/axet/lookup/fonts/font_1"
    //l.loadFont(OCRTest.class, new File("fonts", "font_1"));
    //ocr.loadFont(OCRTest.class, new File("fonts", "pokerstars.font"));

    
    // recognize using all familys set
    //str = ocr.recognize(Capture.load(OCRTest.class, "test3.png"));
   // System.out.println(str);

    // recognize using only one family set
    /*System.out.println("starting parsing");
   // ImageBinaryGrey i = new ImageBinaryGrey(Capture.load(OCRTest.class, "table.png"));
    
   // str = l.recognize(i, "pokerstars.font");
    System.out.println(str);

    // recognize using only one family set and rectangle
    //ImageBinaryGrey i = new ImageBinaryGrey(Capture.load(OCRTest.class, "full.png"));
    ImageBinaryGrey i = new ImageBinaryGrey(captureScreen());
    //graph.drawRect(320, 250, 400, 60);
    str = ocr.recognize(i, 320, 250, 400, 60, "pokerstars.card.letters");
    System.out.println(str);
    System.out.println((System.currentTimeMillis()-milis) /1000 + "secs");
    
    EnumerateWindows.printProcess();
    //Thread.sleep(3000);
    Toolkit.getDefaultToolkit().beep();
    captureScreen();*/
    /*l.loadFontsDirectory(OCRTest.class, new File("fonts"));

    // example how to load only one family
    // "com/github/axet/lookup/fonts/font_1"
    l.loadFont(OCRTest.class, new File("fonts", "font_1"));

    // recognize using all familys set
    str = l.recognize(Capture.load(OCRTest.class, "test3.png"));
    System.out.println(str);

    // recognize using only one family set
    str = l.recognize(Capture.load(OCRTest.class, "test3.png"), "font_1");
    System.out.println(str);

    // recognize using only one family set and rectangle
    ImageBinaryGrey i = new ImageBinaryGrey(Capture.load(OCRTest.class, "full.png"));
    str = l.recognize(i, 1285, 654, 1343, 677, "font_1");
    System.out.println(str);*/
}

public static void processScreen () throws InterruptedException, AWTException, IOException {
	milis = System.currentTimeMillis();
    //OCR l = new OCR(0.90f);
    //EnumerateWindows.printProcess();
        
    //System.out.println("screen captured:"+(System.currentTimeMillis()-milis) + "msecs");
    // will go to com/github/axet/lookup/fonts folder and load all font
    // familys (here is only font_1 family in this library)
    //System.out.println("loading fonts");
    //l.loadFontsDirectory(OCRTest.class, new File("fonts"));

    // example how to load only one family
    // "com/github/axet/lookup/fonts/font_1"
    //l.loadFont(OCRTest.class, new File("fonts", "font_1"));
    //ocr.loadFont(OCRTest.class, new File("fonts", "pokerstars.font"));

    // recognize using all familys set
    //str = ocr.recognize(Capture.load(OCRTest.class, "test3.png"));
    // System.out.println(str);

    // recognize using only one family set
    //System.out.println("starting parsing");
    // ImageBinaryGrey i = new ImageBinaryGrey(Capture.load(OCRTest.class, "table.png"));
    
    // str = l.recognize(i, "pokerstars.font");
    //System.out.println(str);

    // recognize using only one family set and rectangle
    //ImageBinaryGrey i = new ImageBinaryGrey(Capture.load(OCRTest.class, "full.png"));
	screenShot = captureScreen();
    ImageBinaryGrey i = new ImageBinaryGrey(captureScreen());
    //trace("captured screen");
    milis = System.currentTimeMillis();
    
    int[] board = {335, 258, 375, 54};
    /*
    drawRect(new int[]{330,73,2,2});
    drawRect(new int[]{650,73,2,2});
    
    drawRect(new int[]{130,145,2,2});
    drawRect(new int[]{80,295,2,2});
    drawRect(new int[]{838,147,2,2});
    drawRect(new int[]{915,295,2,2});
    
    drawRect(new int[]{183,446,2,2});
    drawRect(new int[]{785,446,2,2});/**/
    
    drawRect(board);
    cacheLvL = 0;
    randomPlayers = 0;
    playerHasHand(330, 73);
    playerHasHand(650, 73);
    
    playerHasHand(130, 145);
    playerHasHand(80, 295);
    playerHasHand(838, 147);
    playerHasHand(915, 295);
    
    playerHasHand(183, 446);
    playerHasHand(785, 446);/**/
    
    /*trace("player:11 " + playerHasHand(330, 73));
    trace("player:1 " + playerHasHand(650, 73));
    
    trace("player:10 " + playerHasHand(130, 145));
    trace("player:9 " + playerHasHand(80, 295));
    trace("player:2 " + playerHasHand(838, 147));
    trace("player:3 " + playerHasHand(915, 295));
    
    trace("player:7 " + playerHasHand(183, 446));
    trace("player:5 " + playerHasHand(785, 446));/**/
    //trace("random players: " + randomPlayers);
    if (lastRandPlayers==randomPlayers) {
    	cacheLvL++;
    } else {System.out.println("INFO: changing rand players to " + randomPlayers);}
    lastRandPlayers = randomPlayers;
    String randomPlayersStr = "";
    for (int n=0;n<randomPlayers;n++) {
    	randomPlayersStr += ",*";
    }
    //if (randomPlayersStr.length()>0) {randomPlayersStr = randomPlayersStr.substring(0, randomPlayersStr.length()-1); }
    
    int[] player = {450, 458, 150, 70};
    drawRect(player);
    /*System.out.println(screenShot.getRGB(130, 145));
    System.out.println(screenShot.getRGB(90, 290));
    System.out.println(screenShot.getRGB(833, 147));
    System.out.println(screenShot.getRGB(920, 295));*/
    if (cachedBoard==null) cachedBoard = new BufferedImage(board[2], board[3], BufferedImage.TYPE_INT_RGB);
    if (cachedPlayer==null) cachedPlayer = new BufferedImage(player[2], player[3], BufferedImage.TYPE_INT_RGB);
    milis = System.currentTimeMillis();
    //trace("compare:"+compareBitmaps(screenShot, lastScreenShot, board));
    if (screenShot.getWidth()<board[0]+board[2] || screenShot.getHeight()<player[1]+player[3]) {
    	return;
    }
    if (!compareBitmaps(cachedBoard, screenShot, board)) {
    	//Toolkit.getDefaultToolkit().beep();
    	boardStrings = ocr.recognize(i, board[0], board[1], board[2]+board[0], board[3]+board[1], "pokerstars.card.letters");
    	boardStrings = boardStrings.replace(" ", "");
    	//trace("Board letters: "+boardStrings);
    	boardSymbols = ocr.recognize(i, board[0], board[1], board[2]+board[0], board[3]+board[1], "pokerstars.big.symbols");
    	boardSymbols =boardSymbols.replace(" ", "");
    	if (!boardStrings.equals("") || !boardSymbols.equals("")) System.out.println("INFO:Board update: "+boardStrings+boardSymbols);
    	trace("Board symbols: "+boardStrings+boardSymbols);
    	//compareBitmaps(screenShot, cachedBoard, board, true);
    	cachedBoard = screenShot.getSubimage(board[0],board[1],board[2],board[3]);//.setData(screenShot.getData(new Rectangle(board[0],board[1],board[2],board[3])));
    	ImageIO.write(cachedBoard, "PNG", new File("board.png"));
    } else cacheLvL++;
    
    //if (boardStrings.length()==0) return;
    
    if (!compareBitmaps(cachedPlayer, screenShot, player)) {
    	playerLetters = ocr.recognize(i, player[0], player[1], player[2]+player[0], player[3]+player[1], "pokerstars.card.letters");
    	playerLetters = playerLetters.replace(" ", "");
	    //trace("Player letters: "+playerLetters);
	    playerSymbols = ocr.recognize(i, player[0], player[1], player[2]+player[0], player[3]+player[1], "pokerstars.big.symbols");
	    playerSymbols = playerSymbols.replace(" ", "");
	    //trace("Player symbols: "+playerSymbols);
	    if (!playerLetters.equals("") || !playerSymbols.equals("")) System.out.println("INFO:Player update: "+playerLetters+playerSymbols);
	    cachedPlayer = screenShot.getSubimage(player[0],player[1],player[2],player[3]);//.setData(screenShot.getData(new Rectangle(board[0],board[1],board[2],board[3])));
    	ImageIO.write(cachedPlayer, "PNG", new File("player.png"));
    } else cacheLvL++;
    
    if (playerLetters.length()==0) return;
    
    if (cacheLvL==3) return;
    //System.out.println((System.currentTimeMillis()-milis) + "ms");
    
    cachedBoard.setData(screenShot.getData(new Rectangle(board[0],board[1],board[2],board[3])));// = screenShot.getData(new Rectangle(board[0],board[1],board[2],board[3]));
    EnumerateWindows.printProcess();
    
    //playerLetters = "TT";
    //playerSymbols = "hd";
    if (playerLetters.length()==0 || playerSymbols.length()==0) {
    	System.out.println("INFO: empty player hand");
    } else if (playerLetters.length()!=2 || playerSymbols.length()!=2) {
    	System.out.println("ERROR:malformed player card names "+playerLetters+playerSymbols);
    //} else if (boardSymbols.length()==0 && boardSymbols.length()==0) {
    	//System.out.println("INFO: empty board");
    } else if (boardStrings.length()!=boardSymbols.length() || boardStrings.length()>5
    		|| boardSymbols.length()>5) {
    	System.out.println("ERROR:malformed board card names");
    } else {
    	//System.out.println("to carlo:"+boardStrings+":"+boardSymbols);
    	String playerStr = playerLetters.substring(0, 1)+playerSymbols.substring(0, 1)+playerLetters.substring(1, 2)+playerSymbols.substring(1, 2)
    			;
    	String boardStr = "";
    	if (boardStrings.length()>0 && boardSymbols.length()>0) {
    		boardStr = boardStrings.substring(0, 1)+boardSymbols.substring(0, 1)+boardStrings.substring(1, 2)+boardSymbols.substring(1, 2)
    			+boardStrings.substring(2, 3)+boardSymbols.substring(2, 3);
    	} 
    	//System.out.println("med:"+boardStr);
    	if (boardStrings.length()>=4) {
    		boardStr += boardStrings.substring(3, 4)+boardSymbols.substring(3, 4);
    	}
    	//System.out.println("med2:"+boardStr);
    	if (boardStrings.length()==5) {
    		boardStr += boardStrings.substring(4, 5)+boardSymbols.substring(4, 5);
    	}
    	
    	playerStr += randomPlayersStr;
    	Result result = EquityCalculation.calculateMonteCarlo(playerStr,boardStr,"");
	    System.out.println("-----------------------------");
	    System.out.println(playerStr + " - " + boardStr);
	    System.out.println(result);
	    System.out.println("-----------------------------");
	    trace("EquityCalculation ended");
    }
    
    ImageIO.write(screenShot, "PNG", new File("screenShot.png"));
    System.out.println("---------------------------------------------------------------------");
    //Thread.sleep(3000);
    //Toolkit.getDefaultToolkit().beep();
    //captureScreen();
}
public static boolean playerHasHand (int x, int y) {
	if (screenShot.getWidth()<x || screenShot.getHeight()<y) return false;
	boolean ret = screenShot.getRGB(x, y)>-8000800;
	if (ret) randomPlayers++;
	return ret;
}
public static void trace (Object msg) {
	 diff = (System.currentTimeMillis()-milis)/1000;
	 System.out.println(msg+ " (" + diff + "s)");
	 milis = System.currentTimeMillis();
}
public static void drawRect (int[] rect) {
	 //BufferedImage overlay = new BufferedImage(100, 50, BufferedImage.TYPE_INT_RGB);
	 Graphics2D graph = screenShot.createGraphics();
	 //screenShot.getRGB(x, y);
	 //final float dash1[] = {10.0f};
	 BasicStroke strk = new BasicStroke(1.0f);
	 graph.setPaint(Color.RED);
	 graph.setStroke(strk);
	 graph.drawRect(rect[0], rect[1], rect[2], rect[3]);
	 //return screenShot;
}
public static boolean compareBitmaps (BufferedImage cached, BufferedImage big, int[] rect) {
	//System.out.println(one.equals(two));
	//Raster rast = new Raster();
	//if (cached.getWidth()==big.getWidth() && cached.getHeight()==big.getHeight()) {
//	BufferedImage temp = null;
//	if (cache) {
//		temp = new BufferedImage(rect[0], rect[1], BufferedImage.TYPE_INT_RGB);
//	}
	
	for (int x=0;x<cached.getWidth();x++) {
		for (int y=0;y<cached.getHeight();y++) {
			if (cached.getRGB(x, y)!=big.getRGB(x+rect[0], y+rect[1])) {
				//System.out.println("compare false:"+cached.getRGB(x, y)+","+big.getRGB(x+rect[0], y+rect[1]));
				return false;
//			} else if (cache) {
//				temp.setRGB(x, y, big.getRGB(x+rect[0], y+rect[1]));
			}
		}
	}
	//if (cache) cached = temp;
	return true;
	//} else {
	//	return false;
	//}
}
public static BufferedImage captureScreen () throws AWTException, IOException {
	Robot robot = new Robot();
	//Frame f = new Frame();
	//WindowFocusListener listener = new WindowFocusListener();
	//f.addWindowFocusListener();
	//f.

	//System.out.println(getHWnd(new Frame()));
	//robot.mouseMove(300, 400);
	//robot.
	 Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize(); 
     //BufferedImage image = robot.createScreenCapture(new Rectangle(0, 0, (int)screenDim.getWidth()*2, 
     //      (int)screenDim.getHeight()));
	 int[] rect = EnumerateWindows.getActiveWinRect();
	 
	 Rectangle rect2 = new Rectangle(rect[0], rect[1], rect[2], rect[3]);
	 if (rect[2]<1) rect[2] = 1;
	 if (rect[3]<1) rect[3] = 1;
	 //Rectangle rect2 = new Rectangle(20, 20, 100, 100);
	 //System.out.println("rect" + Arrays.toString(rect));
	 //System.out.println("rect2" + rect2.toString());
	 screenShot = robot.createScreenCapture(rect2);
	 //BufferedImage overlay = new BufferedImage(100, 50, BufferedImage.TYPE_INT_RGB);
	 Graphics2D graph = screenShot.createGraphics();
	 
	 /*final float dash1[] = {10.0f};
	 BasicStroke strk = new BasicStroke(1.0f);
	 graph.setPaint(Color.RED);
	 graph.setStroke(strk);
	 graph.drawRect(320, 250, 400, 80);*/
	 //ImageIO.write(screenShot, "PNG", new File("screenShot.png"));
	 return screenShot;
	 //ImageIO.write(screenShot, "PNG", new File("screenShot.png"));
}
/*public static void runEquity () {
	long start = System.currentTimeMillis();
	Console console = System.console();
	String hand = console.readLine("Enter your hand:");
	if (hand.equals("")) {
		System.exit(0);
	}
	int randCount = Integer.parseInt(console.readLine("Enter rand hands:"));
	String rands = ",";
	for (int i=0;i<randCount;i++) {
		rands += "*,";
	}
	rands = rands.substring(0, rands.length()-1);
	System.out.println(hand + rands);
	Result result = EquityCalculation.calculateMonteCarlo(hand + rands,"","");
	//Result result = EquityCalculation.calculateMonteCarlo("acas,*,*,*","","");
	//Result result = EquityCalculation.calculate("AhAd,KK+|AKs|AKo","","");
	// s spades
	// h hearts
	// c clubs
	// d diamnods
	//Result result = EquityCalculation.calculateExhaustiveEnumration("AhKd,9c8c,3h3s","","");
            
	long end = System.currentTimeMillis();
	end = end - start;
	System.out.println(result);
	System.out.println("\n" + "time: " + end);
	
	String flop = console.readLine("Enter flop:");
	if (flop.equals("")) {run(); return;}
	result = EquityCalculation.calculateMonteCarlo(hand + rands,flop,"");
	System.out.println(result);
	
	String turn = console.readLine("Enter turn:");
	if (turn.equals("")) {run(); return;}
	result = EquityCalculation.calculateMonteCarlo(hand + rands,flop+turn,"");
	System.out.println(result);
	
	String river = console.readLine("Enter river:");
	if (river.equals("")) {run(); return;}
	result = EquityCalculation.calculateMonteCarlo(hand + rands,flop+turn+river,"");
	System.out.println(result);
	run();
	
}*/

}
