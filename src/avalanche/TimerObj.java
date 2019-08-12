package avalanche;

public class TimerObj {
String eve;
int left;
Object data = null;
boolean dispatched = false;
public TimerObj (String eve2, int length, Object data2) {
	eve = eve2; left = length; data = data2;
}

}