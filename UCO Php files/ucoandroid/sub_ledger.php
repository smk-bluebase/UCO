<?php
include("config.php");
include("db_connect.php");

$db = new DB_Connect();
$con = $db->connect();

$memberNo = $_POST['memberNo'];
$title = $_POST['title'];

$result = array();

if($title == "SRF"){
	$sql_query = "SELECT date, amount, current_amount, narration FROM srf WHERE member_no = ".$memberNo;

	$query=mysqli_query($con, $sql_query);
	while($row = mysqli_fetch_array($query)){
		array_push($result, array(
			'Date'=>$row['date'],
			'Amount'=>$row['amount'],
			'Current Amount'=>$row['current_amount'],
			'Narration'=>$row['narration']
		));
	}
}else if($title == "Share Capital"){
	$sql_query = "SELECT date, amount, current_amount, narration FROM share_capital WHERE member_no = ".$memberNo;

	$query = mysqli_query($con, $sql_query);
	while($row = mysqli_fetch_array($query)){
		array_push($result, array(
			'Date'=>$row['date'],
			'Amount'=>$row['amount'],
			'Current Amount'=>$row['current_amount'],
			'Narration'=>$row['narration']
		));
	}

}else if($title == "Thrift"){
	$sql_query = "SELECT date, amount, current_amount, narration FROM thrift_deposit WHERE member_no = ".$memberNo;

	$query = mysqli_query($con, $sql_query);
	while($row = mysqli_fetch_array($query)){
		array_push($result, array(
			'Date'=>$row['date'],
			'Amount'=>$row['amount'],
			'Current Amount'=>$row['current_amount'],
			'Narration'=>$row['narration']
		));
	}	
}else if($title == "Surety Loan"){
	$sql_query = "SELECT date, principal, interest, balance, narration FROM surety_loan WHERE member_no = ".$memberNo;

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
}else if($title == "Festival Loan"){
	$sql_query = "SELECT date, principal, balance, narration FROM festival_loan WHERE member_no = ".$memberNo;

	$query = mysqli_query($con, $sql_query);
	while($row = mysqli_fetch_array($query)){
		array_push($result, array(
			'Date'=>$row['date'],
			'Amount'=>$row['principal'],
			'Current Amount'=>$row['balance'],
			'Narration'=>$row['narration']
		));
	}
}else if($title == "Flood Loan"){
	$sql_query = "SELECT date, principal, balance, narration FROM flood_loan WHERE member_no = ".$memberNo;
	
	$query = mysqli_query($con, $sql_query);
	while($row = mysqli_fetch_array($query)){
		array_push($result, array(
			'Date'=>$row['date'],
			'Amount'=>$row['principal'],
			'Current Amount'=>$row['balance'],
			'Narration'=>$row['narration']
		));
	}
}

echo json_encode($result);   

mysqli_connect($con);
?>