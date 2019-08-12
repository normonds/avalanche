package avalanche;
import java.io.Console;

import mi.poker.calculation.EquityCalculation;

public class ConsTest {

public static void main (String[] args) {
	// TODO Auto-generated method stub
	Console console = System.console();
	
	System.out.println("availableProcessors:"+Runtime.getRuntime().availableProcessors());
	System.out.println("maxMemory:"+Runtime.getRuntime().maxMemory()/1000000);
	System.out.println("totalMemory:"+Runtime.getRuntime().totalMemory()/1000000);
	System.out.println("freeMemory:"+Runtime.getRuntime().freeMemory()/1000000);
	//Runtime.getRuntime().
	//Thread.currentThread().
	String input = console.readLine("Enter input:");
	//System.out.println(input);
	while (!input.equals("")) {
		long milis = System.currentTimeMillis();
		try {
			System.out.println(EquityCalculation.calculateMonteCarlo(input,"",""));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println((System.currentTimeMillis()-milis)+"ms");
		input = console.readLine("Enter input:");
		
	}
	
	 //= console.readLine("Enter input:");
	
}

}
