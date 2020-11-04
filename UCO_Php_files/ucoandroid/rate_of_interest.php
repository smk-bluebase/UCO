<?php
include("config.php");

$db = new DB_Connect();
$con = $db->connect();

$result=array();

$srf_interest="";
$thirft_interest="";
$surety_loan_interest="";
$surety_loan_od_interest="";
$flood_loan_interest="";
$flood_loan_od_interest="";
$festival_loan_interest="";
$festival_loan_od_interest="";
$fd_interest="";

// Query 1

$sql_query = "SELECT percentage 
				FROM interest_master 
				WHERE name = 'THRIFT DEPOSIT'
				ORDER BY id DESC";

$res = mysqli_query($con, $sql_query);

$row = mysqli_fetch_array($res);

if($row){
	$thirft_interest = $row['percentage'];
}

// Query 2

$sql_query = "SELECT percentage
				FROM interest_master 
				WHERE name = 'SRF' 
				ORDER BY id DESC";

$res = mysqli_query($con, $sql_query);

$row = mysqli_fetch_array($res);

if($row){
	$srf_interest = $row['percentage'];
}

// Query 3

$sql_query = "SELECT percentage
				FROM fd_interest_rate
				ORDER BY id DESC";

$res = mysqli_query($con, $sql_query);

$row = mysqli_fetch_array($res);

if($row){
	$fd_interest = $row['percentage'];
}

// Query 4

$sql_query = "SELECT interest_rate, od_interest_rate
				FROM loan_type 
				WHERE code = 'LON-001'
				ORDER BY id DESC";

$res = mysqli_query($con, $sql_query);

$row = mysqli_fetch_array($res);

if($row){
	$surety_loan_interest = $row['interest_rate'];
	$surety_loan_od_interest = $row['od_interest_rate'];
}

// Query 5

$sql_query = "SELECT interest_rate, od_interest_rate
				FROM loan_type
				WHERE code = 'LON-002'
				ORDER BY id DESC";

$res = mysqli_query($con, $sql_query);

$row = mysqli_fetch_array($res);

if($row){
	$festival_loan_interest = $row['interest_rate'];
	$festival_loan_od_interest = $row['od_interest_rate'];
}

// Query 6

$sql_query = "SELECT interest_rate, od_interest_rate
				FROM loan_type
				WHERE code = 'LON-003'
				ORDER BY id DESC";

$res = mysqli_query($con, $sql_query);

$row = mysqli_fetch_array($res);
if($row){
	$flood_loan_interest = $row['interest_rate'];
	$flood_loan_od_interest = $row['od_interest_rate'];
}

array_push($result,array(
	'srf_interest'=>$srf_interest,
	'thrift_interest'=>$thirft_interest,
	'surety_loan_interest'=>$surety_loan_interest,
	'surety_loan_od_interest'=>$surety_loan_od_interest,
	'festival_loan_interest'=>$festival_loan_interest,
	'festival_loan_od_interest'=>$festival_loan_od_interest,
	'flood_loan_interest'=>$flood_loan_interest,
	'flood_loan_od_interest'=>$flood_loan_od_interest,
	'fd_interest'=>$fd_interest
));

echo json_encode($result);    

mysqli_close($con);
?>