<pre>&spades;&diams;&clubs;&hearts;<br><style>body{background:#0C131D;color:#FFF;font-family:Arial;}td{padding:2px;border:1px solid rgba(255,255,255,.06);}</style><?php

$groups = array(
'AA'=>1,'KK'=>1, 'QQ'=>1, 'JJ'=>1,
'AK'=>2, 'AQs'=>2, 'AJs'=>2, 'KQs'=>2, 'TT'=>2, 'AKs'=>2,
'AQ'=>3, 'ATs'=>3, 'KJs'=>3, 'QJs'=>3, 'JTs'=>3, '99'=>3,
'AJ'=>4, 'KQ'=>4, 'KTs'=>4, 'QTs'=>4, 'J9s'=>4, 'T9s'=>4, '98s'=>4, '88'=>4,
'KJ'=>5, 'QJ'=>5, 'JT'=>5, 'Q9s'=>5, 'T8s'=>5, '97s'=>5, '87s'=>5, '77'=>5, '76s'=>5,'66'=>5,'A9s'=>5,'A8s'=>5,'A7s'=>5,'A6s'=>5,'A5s'=>5,'A4s'=>5,'A3s'=>5,'A2s'=>5,
'AT'=>6, 'KT'=>6, 'QT'=>6, 'J8s'=>6, '86s'=>6, '75s'=>6, '65s'=>6, '55'=>6, '54s'=>6,
'J9'=>7, 'T9'=>7, '98'=>7, '64s'=>7, '53s'=>7, '44'=>7, '43s'=>7, '33'=>7, '22'=>7, 'K9s'=>7, 'K8s'=>7, 'K7s'=>7, 'K6s'=>7, 'K5s'=>7, 'K4s'=>7, 'K3s'=>7, 'K2s'=>7,
'A9'=>8, 'K9'=>8, 'Q9'=>8, 'J8'=>8, 'J7s'=>8, 'T8'=>8, '96s'=>8, '87'=>8, '85s'=>8, '76'=>8, '74s'=>8, '65'=>8, '54'=>8, '42s'=>8, '32s'=>8
);

$hands = file_get_contents('2hand.all.montecarlo.txt');
//$hands = file_get_contents('2hand.all.montecarlo.txt');
$lines = preg_split("/\\n/m", $hands);
$sorted = array();
$i=1;
foreach ($lines as $line) {
	$split = explode(',',$line);
	$sorted[''.$split[0].''] = $split[2];
	
	//echo $split[0].' '..'<br>';
	//var_dump($split);
}
asort($sorted);
$sorted = array_reverse($sorted, true);
foreach ($sorted as $key=>$val) {
	//echo  $i . ' - ' . $key . ' - ' . $val . '<br>';
	//echo  $key . ',' . $val . '<br>';
	$i++;
	//$split = explode(',',$line);
	//$sorted[$split[0]] = $split[1];
	//$i++;
	//echo $split[0].' '..'<br>';
	//var_dump($split);
}
echo '</pre><br><br><br><br>';
function handIsWeak ($str) {
	global $sorted;
	//echo $str . ' - ' . substr($str,0,1).'-'.substr($str,2,1).'<br>';
	if (isPair($str)) {
		return ($sorted[substr($str,0,1).substr($str,0,1)]=='u')?true:false;
	}
	
	if (suited($str)) {
		if (array_key_exists(substr($str,0,1).substr($str,2,1), $sorted)) {
			//echo (strpos($sorted[substr($str,0,1).substr($str,2,1)],'u')>-1);
			return strpos($sorted[substr($str,0,1).substr($str,2,1)],'u')>-1;
		} else if (array_key_exists(substr($str,2,1).substr($str,0,1), $sorted)) {
			return strpos($sorted[substr($str,2,1).substr($str,0,1)],'u')>-1;
		}
		//echo $str.'<br>';
	} else {
		if (array_key_exists(substr($str,0,1).substr($str,2,1).'o', $sorted)) {
			return strpos($sorted[substr($str,0,1).substr($str,2,1).'o'],'u')>-1;
		} else if (array_key_exists(substr($str,2,1).substr($str,0,1).'o', $sorted)) {
			return strpos($sorted[substr($str,2,1).substr($str,0,1).'o'],'u')>-1;
		}
		//echo $str.'<br>';
	}
	//exit;
	//echo $str.'<br>';
	// if (array_key_exists(substr($str,0,1).substr($str,2,1), $sorted)) {
		
		// return ($sorted[substr($str,0,1).substr($str,2,1)]=='u');
	// } else if () {
		
	// }
	// if () {
		
	// }
	//return true;
}

$data = file_get_contents('history.log');
//$data = file_get_contents('http://95.68.56.191:8070/project.avalanche/bin/history.log');
$data = nl2br($data);
$data = preg_replace("/\\n/m", "<br>", $data);
$data = preg_replace('/\s+/', ',', $data);
//$data = preg_replace("/chips:|eq:|me:|call:|board:|lastb:|folded:|pot:|opn:/", "", $data);
//echo $data;

$lines = preg_split("/<br>/", $data);

$totalRows = -1;
$smallCalls = 0;
$atLeastFlop = 0;
$triples = 0;
$aggroCall = 0;
$atLeast50 = 0;
$earlyGroup = 0;
$autofolds = 0;
echo '<table border=1 style="border-collapse:collapse;"><tr><th>date</th><th>time</th><th>chips</th><th>equity</th>
<th>hand</th><th>call</th><th>opns</th><th>board</th><th>fold</th><th>pot</th><tr>';
$lines = array_reverse($lines, true);
foreach ($lines as $line) {
	$cells = preg_split("/,/", $line);
	//var_dump($cells);
	
	echo '<tr>';
	$outTR = '';
	$trStyle = '';
	$opacity = 0;
	$isPair = false;
	$isSuited = false;
	$myhand = '';
	$board = '';
	$totalRows++;
	$opacity = '';
	foreach ($cells as $cell) {
		//if () {}
		
		if (strpos($cell,'eq:')>-1) {
			$cell = floatval(substr($cell, 3))/100;
			$opacity = $cell;
			//$trStyle .= 'opacity:'.$cell.';';
			if ($cell<.5) {
				//$trStyle .= 'opacity:.1;';
				//$trStyle .= 'background-color:rgba(255,255,255,.2);';
			} else {
				$atLeast50++;
			}
		} else if (strpos($cell,'me:')>-1) {
			$myhand = substr($cell, 3);
			//echo handIsWeak($myhand);
			if (handIsWeak($myhand)) {
				$autofolds++;
				$opacity = .1;
			} else {
				
			}
			$trStyle .= 'opacity:'.$opacity.';';
			if (isPair(substr($cell, 3))) {
				//$trStyle .= 'color:red;';
				//echo substr($cell, 3, 1);
				if (in_array(substr($cell, 3, 1), array('A','K','Q','J','T','9','8'))) {
					$earlyGroup++;
					$trStyle .= 'background-color:#FF9933;';
				}
				
				$isPair = true;
			} else if (suited(substr($cell, 3))) {
				$isSuited = true;
				//$trStyle .= 'color:green;';
			}
		} else if (strpos($cell,'board:')>-1) {
			$board = substr($cell, 6);
			if (strlen($cell)>8) {
				$atLeastFlop++;
			}
			if ($isPair && strrpos($board, substr($myhand,0,1))>-1) {
				$triples++;
				$cell.='---';
			}
		} else if (strpos($cell,'folded:')>-1 && strpos($cell,'false')>-1) {
			//if (isPair(substr($cell, 3))) {
				$trStyle .= 'border:2px solid rgba(255,0,0,.3);';
			//}
		} else if (strpos($cell,'call:')>-1) {
			$call = floatval(substr($cell, 5));
			if ($call<50) {
				$smallCalls++;
				//$trStyle .= 'border:2px solid rgba(0,255,0,.3);';
			} else if ($call>350) {
				$aggroCall++;
			}
		} else if (strpos($cell,'history:')>-1) {
			$cell = str_replace('click;', '<span style="opacity:.3;">click;</span>', $cell);
			$cell = str_replace('call-', '<span style="font-weight:bold;">CALL</span>', $cell);
			$cell = str_replace('mefold', '<span style="font-weight:bold;color:red;">FOLD</span>', $cell);
			$cell = str_replace('raise-', '<span style="font-weight:bold;color:yellow;">RAISE</span>', $cell);
		}
		

		//$cell = preg_replace("/me:/", "-$1-", $cell);
		if (strpos($cell,'me:')>-1 || strpos($cell,'board:')>-1) {// && $opacity>.5) {
			
			$tmp = '';
			if ($isPair && strpos($cell,'me:')>-1) {
				$tmp .= 'color:red';
			} else if ($isSuited && strpos($cell,'me:')>-1) {
				$tmp .= 'color:green';
			}
			$cell = preg_replace("/chips:|eq:|me:|call:|board:|lastb:|folded:|pot:|opn:/", "", $cell);
			$cell = '<b style="font-size:22px;'.$tmp.'">'.pretty($cell).'</b>';
		} else {
			$cell = preg_replace("/chips:|eq:|me:|call:|board:|folded:|pot:|opn:/", "", $cell);
			//\(lastb:(.+)
		}
		//echo var_dump(strpos($cell, 'lastb:'));
		if (strpos($cell, 'lastb:')===false) {
			$outTR .= '<td>'.$cell.'</td>';
		}
	}
	
	
		echo '<tr style="'.$trStyle.'">'.$outTR.'</tr>';
	

}

echo '<div style="position:fixed;right:0;top:0;">rows:'.$totalRows 
			.'<br> autofolds '.toPercs($autofolds)
			.'<br> smallCalls:'.toPercs($smallCalls)
			.'<br> atLeastFlop:'.toPercs($atLeastFlop)
			.'<br> aggroCall:'.toPercs($aggroCall)
			.'<br> atLeast50:'.toPercs($atLeast50)
			.'<br> early group:'.$earlyGroup
			.'<br> triples:'.($triples).'/'.$totalRows.'</div>';

function toPercs ($num) {
	global $totalRows;
	return number_format($num/$totalRows, 4, '.', '')*100 .'%';
}
function pretty ($str) {//
	$patterns = array();
	$patterns[0] = '/d/i';
	$patterns[1] = '/h/i';
	$patterns[2] = '/c/i';
	$patterns[3] = "/s/i";
	$replacements = array();
	$replacements[0] = '♦';
	$replacements[1] = '♥';
	$replacements[2] = '♣';
	$replacements[3] = '♠';
	return preg_replace($patterns, $replacements, $str);
	//return preg_replace("/s/", "&spades;", $ret);
}
function suited ($str) {
	if (strlen($str)>0 && substr($str, 1,1)==substr($str, 3,1)) {
		return true;
	}
	return false;
}
function isPair ($str) {
	if (strlen($str)>0 && substr($str, 0,1)==substr($str, 2,1)) {
		return true;
	}
	return false;
}

?>