package avalanche;

public class CardPair {
double equity;
String position;
int raise= -1;

public CardPair (String[] split) {
	equity = Double.parseDouble(split[1]);
	//System.out.println(split[0]);
	position = split[2];
	if (split.length>3) {
		try {
			raise = Integer.parseInt(split[3]);
		} catch (IllegalArgumentException e) {
			
		}
	}
}

}