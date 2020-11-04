<?php
include("config.php");

$db = new DB_Connect();
$con = $db->connect();

$memberNo = $_POST['memberNo'];
$title = $_POST['title'];

$result = array();

if($title == "SRF"){
	$sql_query = "SELECT DATE_FORMAT(date, '%d-%m-%Y') AS date, amount, current_amount, narration 
					FROM srf 
					WHERE member_no = '".$memberNo."'
					ORDER BY id DESC";

	$query=mysqli_query($con, $sql_query);

	while($row = mysqli_fetch_array($query)){
		array_push($result, array(
			'Date'=>$row['date'],
			'Amount'=>$row['amount'],
			'Current Amount'=>$row['current_amount'],
			'Narration'=>$row['narration']
		));
	}

	if(empty($result)){
		$result[] = array('Date'=>" ",
			'Amount'=>" ",
			'Current Amount'=>" ",
			'Narration'=>" "
		);
	}
}else if($title == "Share Capital"){
	$sql_query = "SELECT DATE_FORMAT(date, '%d-%m-%Y') AS date, amount, current_amount, narration
					 FROM share_capital
					 WHERE member_no = '".$memberNo."'
					 ORDER BY id DESC";

	$query = mysqli_query($con, $sql_query);

	while($row = mysqli_fetch_array($query)){
		array_push($result, array('Date'=>$row['date'],
			'Amount'=>$row['amount'],
			'Current Amount'=>$row['current_amount'],
			'Narration'=>$row['narration']
		));
	}

	if(empty($result)){
		$result[] = array('Date'=>" ",
			'Amount'=>" ",
			'Current Amount'=>" ",
			'Narration'=>" "
		);
	}

}else if($title == "Thrift"){
	$sql_query = "SELECT DATE_FORMAT(date, '%d-%m-%Y') AS date, amount, current_amount, narration
					 FROM thrift_deposit
					 WHERE member_no = '".$memberNo."'
					 ORDER BY id DESC";

	$query = mysqli_query($con, $sql_query);

	while($row = mysqli_fetch_array($query)){
		array_push($result, array(
			'Date'=>$row['date'],
			'Amount'=>$row['amount'],
			'Current Amount'=>$row['current_amount'],
			'Narration'=>$row['narration']
		));
	}	

	if(empty($result)){
		$result[] = array('Date'=>" ",
			'Amount'=>" ",
			'Current Amount'=>" ",
			'Narration'=>" "
		);
	}

}else if($title == "Surety Loan"){
	$sql_query = "SELECT DATE_FORMAT(date, '%d-%m-%Y') AS date, principal, interest, balance, narration
					 FROM surety_loan
					 WHERE member_no = '".$memberNo."'
					 ORDER BY id DESC";

	$query = mysqli_query($con, $sql_query);

	while($row = mysqli_fetch_array($query)){
		array_push($result, array(
			'Date'=>$row['date'],
			'Interest'=>$row['interest'],
			'Amount'=>$row['principal'],
			'Current Amount'=>$row['balance'],
			'Narration'=>$row['narration']
		));
	}

	if(empty($result)){
		$result[] = array('Date'=>" ",
			'Interest'=>" ",
			'Amount'=>" ",
			'Current Amount'=>" ",
			'Narration'=>" "
		);
	}

}else if($title == "Festival Loan"){
	$sql_query = "SELECT DATE_FORMAT(date, '%d-%m-%Y') AS date, principal, balance, narration
					 FROM festival_loan
					 WHERE member_no = '".$memberNo."'
					 ORDER BY id DESC";

	$query = mysqli_query($con, $sql_query);

	while($row = mysqli_fetch_array($query)){
		array_push($result, array('Date'=>$row['date'],
			'Amount'=>$row['principal'],
			'Current Amount'=>$row['balance'],
			'Narration'=>$row['narration']
		));
	}

	if(empty($result)){
		$result[] = array('Date'=>" ",
			'Amount'=>" ",
			'Current Amount'=>" ",
			'Narration'=>" "
		);
	}

}else if($title == "Flood Loan"){
	$sql_query = "SELECT DATE_FORMAT(date, '%d-%m-%Y') AS date, principal, balance, narration
					FROM flood_loan
					WHERE member_no = '".$memberNo."'
					ORDER BY id DESC";
	
	$query = mysqli_query($con, $sql_query);

	while($row = mysqli_fetch_array($query)){
		array_push($result, array('Date'=>$row['date'],
			'Amount'=>$row['principal'],
			'Current Amount'=>$row['balance'],
			'Narration'=>$row['narration']
		));
	}

	if(empty($result)){
		$result[] = array('Date'=>" ",
			'Amount'=>" ",
			'Current Amount'=>" ",
			'Narration'=>" "
		);
	}
}

echo json_encode($result);   

mysqli_close($con);
?>