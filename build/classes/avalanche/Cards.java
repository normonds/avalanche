package avalanche;

import nooni.Util;

public class Cards {
public static String[] cards = {"A","K","Q","J","T","9","8","7","6","5","4","3","2"};
public static String[] suits = {"c","h","s","d"};

public static boolean isCardValid (String str) {
	if (str.length()!=2) return false;
	if (Util.indexOf(cards, str.substring(0, 1).toUpperCase())>-1 && Util.indexOf(suits, str.substring(1, 2).toLowerCase())>-1) {
		return true;
	}
	return false;
}
public static boolean validCardSequence (String str) {
	//if (str.length()==0) return false;
	if (str.length()%2 == 0) {
		for (int i=0;i<str.length();i+=2) {
//			System.out.println(i+"-"+str.substring(i, i+2));
			if (!isCardValid(str.substring(i, i+2))) {
				return false;
			}
		}
		return true;
	}
	return false;
}

}