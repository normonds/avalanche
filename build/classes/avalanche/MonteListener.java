package avalanche;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.swt.widgets.Display;

import mi.poker.calculation.EquityCalculation;
import mi.poker.calculation.HandInfo;
import mi.poker.calculation.Result;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class MonteListener {
public static RunnableEquity equityThread = new RunnableEquity("equityThread");
public static double equity = -1;
public static long milis = 0;
public static boolean newThread = false;
//public long inte = -1;

public static void doMonteCarlo (String myStr) {
	System.out.println("MONTECARLO "+myStr);
	String[] str = myStr.split("-");

	//equityThread.setStats();
	if (str.length>1) {
		equityThread.start(str[1], (str.length>2)?str[2]:"", "");
	} else System.out.println("MONTECARLO array too short: "+str.length);
}
public static synchronized void setResult (final double equity2) {
	//Display.getDefault().asyncExec(new Runnable() {
		//public void run () {
	//if () {
			equity = equity2;
			System.out.println("SETTING RESULT MONTE EQUITY "+equity2);
			System.out.println("");
	//}
			//setEquity(equity2);
			///cardsLabel.setText(AvalancheUtil.createSymbols(EngineLogical.me.validHand)
			//		+" "+AvalancheUtil.createSymbols(EngineLogical.board.validCards)+" ");
		//}
	//});
}
public static void main (String[] args) throws Exception {
	HttpServer server;
	server = HttpServer.create(new InetSocketAddress(8001), 0);
	server.createContext("/montecarlo", new MyHandler());
	server.setExecutor(null); // creates a default executor
	server.start();
	System.out.println("Starting server");
}

static class MyHandler implements HttpHandler {
public void handle (final HttpExchange t) throws IOException {
	new Thread(new Runnable() { public void run () {
		//System.out.println("Request " + t.getRequestURI());
		///Result result = null;
		//if (t.getRequestURI().toString().split("-")) {
			//doMonteCarlo("AcAs,*,*,*,*", "", "");
		//} else
			doMonteCarlo(t.getRequestURI().toString());
		//if (t.getRequestURI().toString().startsWith("/montecarlo/gwetrdd")) {
			//doMonteCarlo("AcAs,*,*,*,*", "", "");
		//} else doMonteCarlo("2c3s,*", "7d4h6dThKc", "");
		equity = -1;
		//result = EquityCalculation.calculateMonteCarlo("AcAs,*,*,*,*", "", "");
		long inte = 0;
		Date dNow = new Date();
	    SimpleDateFormat ft = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss");
		System.out.println(ft.format(dNow));
		milis = System.currentTimeMillis();
		while (!equityThread.t.isInterrupted() && equityThread.t.isAlive()) {
			if (System.currentTimeMillis()-milis>=1000) {
				milis = System.currentTimeMillis();
				System.out.println(inte++);
//				if (inte>6) {
//					inte = -1;
//				}
			}
		}
		String response = t.getRequestURI().toString().replace("/montecarlo-", "") + ":";
		if (equityThread.t.isInterrupted()) {
			response += "interrupted";
			System.out.println("CALC INTERRUPTED");
		} else {
			response += equity;
			System.out.println("posting result ");
		}
		
		// String monteHand = result.toString();
		try {
			t.sendResponseHeaders(200, response.length());
			OutputStream os = t.getResponseBody();

			// try {Thread.sleep(5000);}catch(InterruptedException
			// e){e.printStackTrace();}
			os.write(response.getBytes());
			os.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}}).start();

}
}

}