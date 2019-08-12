package avalanche;

import nooni.events.Eve;

public class AvalEvent extends Eve {
//				eve:"change_board",			region:[400,319,195,43], font:{letters:["zynga.card.letters"],symbols:["zynga.big.symbols"]}},
//pot:{		eve:"change_pot",				region:[400,360,195,50], 			font:["zynga.pot.letters"]}
//},
//player : {
//	card: 	{eve:"change_myhand",		region:[324,309,10,10], 				font:["zynga.oppon.back"]},
//	bet: 		{eve:"change_mybet",			region:[300,300,30,10], 				font:["zynga.oppon.chips", 0.82]},
//	chips: 	{eve:"change_mychips",		region:[300,300,30,10], 				font:["zynga.oppon.chips"]},	},
//oppons : [
//{	card: {eve:"change_opncard",		region:[324,309,10,10], 				font:["zynga.oppon.back"]},
//	bet: 		{eve:"change_opnbet",		region:[300,300,30,10], 				font:["zynga.oppon.chips", 0.82]},
//	chips: 	{eve:"change_opnchips",	

public static final String CHANGE_POT 					= "change_pot";
public static final String CHANGE_BOARD 			= "change_board";
public static final String CHANGE_MY_HAND			= "change_my_hand";
public static final String CHANGE_MY_BET			= "change_my_bet";
public static final String CHANGE_MY_CHIPS		= "change_my_chips";
public static final String CHANGE_OPN_CARD 		= "change_opn_card";
public static final String CHANGE_OPN_FOLD 		= "change_opn_fold";
public static final String CHANGE_OPN_BET 			= "change_opn_bet";
public static final String CHANGE_OPN_CHIPS 		= "change_opn_chips";
public static final String MY_TURN 						= "my_turn";
public static final String MY_TURN_CALL				= "my_turn_call";
public static final String MY_TURN_CHECK			= "my_turn_check";
public static final String CHANGE_OPN_ADD			= "change_opn_add";
public static final String CHANGE_MY_HAND_ACTIVE	= "change_my_hand_active";
public static final String RECEIVE_SIMULATION_RESULTS	= "monte_equity_receive";
public static final String MY_TURN_BOXCALL			= "my_turn_boxcall";
public static final String MOVE_TO 						= "move_to";
public static final String CLICK							= "click";
public static final String KEY_PRESS					= "key_press";
public static final String CHECK_IF_TURN_ACTIVE = "check_if_turn_active";
public static final String PAUSE_ME					= "pause_loop";
public static final String RESUME_ME				= "resume_loop";
public static final String REQUEST_SIMULATION = "request_simulation";

public AvalEvent (String eventType, Object eventTarget) {
	super(eventType, eventTarget);
}
public AvalEvent (String eventType, Object eventTarget, Object data_) {
	super(eventType, eventTarget);
	data = data_;
}

}
