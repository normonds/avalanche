package avalanche;

import java.util.concurrent.TimeUnit;

import nooni.Util;

import mi.poker.calculation.EquityCalculation;
import mi.poker.calculation.HandInfo;
import mi.poker.calculation.Result;

class RunnableEquity implements Runnable {
public Thread	t=null;
private String	threadName, me, board, outs, getURL;
boolean			suspended	= false, interrupted=false;
int running = 0;

RunnableEquity (String name) {
	threadName = name;
	//System.out.println("Creating " + threadName);
}
public void setStats (String myStr, String boardStr, String outsStr) {
	me = myStr; board = boardStr; outs = outsStr;
}
public void run () {
	try {
		//TimeUnit.SECONDS.sleep(1);
	//System.out.println("Running " + threadName);
	running++;
	//System.out.println("MONTE CARLO: STARTED, thread:"+threadName);
	//try {
		//for (int i = 10; i > 0; i--) {
		//try {
	double equity = -1;
	String eqString = "timeout";
	if (getURL!=null) {
		System.out.println("STARTING MONTE GET "+getURL);
		try {String get = Util.urlGET(getURL);
			if (get.contains("http")) {
				
			} else if (!get.contains("interrupted")) {
				eqString = get.split("\\:")[0];
				equity = Double.valueOf(get.split("\\:")[1]);
			}
		} catch (Exception e) {
			System.out.println("MONTE CARLO get exception");
			e.printStackTrace();
		}
		System.out.println("Ended get montecarlo");
	} else {
		System.out.println("STARTING MONTE");
		Result result = null;
		try {
			result = EquityCalculation.calculateMonteCarlo(me,board,outs);
		} catch (NullPointerException e) {
			System.out.println("MONTE CARLO NullPointerException");
			e.printStackTrace();
		}
		
		HandInfo monteHand = result.getHandInfo(0);
		//trace(monteResult);
		//trace("players:"+monteResult.getMap().size());
		equity = monteHand.getEquity()*100;
		System.out.println("ENDED MONTE");
	}
	if (!t.isInterrupted()) {
		if (getURL==null) {
			MonteListener.setResult(equity);
		} else {
			Avalanche.receiveSimulationResults(equity, eqString);
		}
	}
	
	//Avalanche.setResult(equity);
	//} catch (Exception e) {
	//	System.out.println("MONTE CARLO EXCEPTION me:"+me+", board:"+board+" outs:"+outs);
	//}
	
	//System.out.println("MONTE CARLO: DONE, interrupted:"+t.isInterrupted());
	// Let the thread sleep for a while.
	//Thread.sleep(300);
//		synchronized (this) {
//			while (suspended) {
//				//wait();
//			}
//		}
	//}

	} catch (InterruptedException e) {
		System.out.println("THREAD " + threadName + " INTERRUPTED.");
		//MonteListener.setResult(0);
	}

	running--;
	if (t.isInterrupted()) {//System.out.println("THREAD " + threadName + " exiting, interrupted:"+t.isInterrupted());
		System.out.println("Exiting as interrupted");
	}
}
public void kill () {
	if (t!=null) {
		if (t.isAlive()) {
			t.interrupt();
			//Avalanche.trace("MONTECARLO INTERRUPT");
			//t.stop();
			//System.out.println("trying to INTERRUPT");
			System.out.println("interrupted:"+t.isInterrupted());
			//t.
//			try {
//				//t.join();
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
		} //else {System.out.println("t is not alive");}
	} //else {System.out.println("t is null");}
}
public void start (String myStr, String boardStr, String outsStr) {
	kill();
	//if (t == null /*|| !t.isAlive()*/) {
		//System.out.println("THREAD " + threadName + " starting");
		t = new Thread(this, threadName);
		setStats(myStr, boardStr, outsStr);
		t.start();
	/*} else if (t.isAlive()) {
		System.out.println("THREAD is still alive:"+t.isAlive());
	} else {
		System.out.println("THREAD is not null, couldnt restart, alive:"+t.isAlive());
		System.out.println("interrupted:"+t.isInterrupted());
	}*/
}
public void start (String myStr, String boardStr, String outsStr, String url) {
	kill();
	t = new Thread(this, threadName);
	t.start();
	getURL = url+"-"+myStr+"-"+boardStr;
	//setStats(myStr, boardStr, outsStr);
}

//@SuppressWarnings("deprecation")
//public void stop () {
//	t.stop();
//}
void suspend () {
	suspended = false;
}
synchronized void resume () {
	suspended = false;
	notify();
}

}