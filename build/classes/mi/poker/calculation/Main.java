/**
 * @author m1
 */
package mi.poker.calculation;

import java.io.Console;

public class Main {

public static void main (String[] args){
	runs();
}
public static void runs () {
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
	Result result = null;
	try {
		result = EquityCalculation.calculateMonteCarlo(hand + rands,"","");
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
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
	if (flop.equals("")) {runs(); return;}
	try {
		result = EquityCalculation.calculateMonteCarlo(hand + rands,flop,"");
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	System.out.println(result);
	
	String turn = console.readLine("Enter turn:");
	if (turn.equals("")) {runs(); return;}
	try {
		result = EquityCalculation.calculateMonteCarlo(hand + rands,flop+turn,"");
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	System.out.println(result);
	
	String river = console.readLine("Enter river:");
	if (river.equals("")) {runs(); return;}
	try {
		result = EquityCalculation.calculateMonteCarlo(hand + rands,flop+turn+river,"");
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	System.out.println(result);
	runs();
	
}
public static Object parseInput (String input) {
	//if () 
	return "";
}
}