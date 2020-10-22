<?php
include("config.php");
include("db_connect.php");

$db = new DB_Connect();
$con = $db->connect();

$memberNo = $_POST['memberNo'];

$sql_query = "SELECT percentage FROM interest_master WHERE name = 'SRF' ORDER BY id DESC LIMIT 0,1";

$result = array();

$suretyPercentage = 0;

$res = mysqli_query($con, $sql_query);

$row = mysqli_fetch_array($res);

if($row){
	$suretyPercentage = $row['percentage'];
}

$sql_query = "SELECT name, srf_balance, thrift_balance, share_capital, surety_balance, festival_balance, flood_balance FROM member_balance WHERE member_no = ".$memberNo;

$query = mysqli_query($con, $sql_query);

$row = mysqli_fetch_array($query);

if($row){
	array_push($result, array(
		'name'=>$row['name'],
		'srf_balance'=>$row['srf_balance'],
		'thrift_balance'=>$row['thrift_balance'],
		'share_capital'=>$row['share_capital'],
		'surety_balance'=>$row['surety_balance'],
		'festival_balance'=>$row['festival_balance'],
		'flood_balance'=>$row['flood_balance'],
		'surety_percentage'=>$suretyPercentage));
}

echo json_encode($result);    

mysqli_connect($con);
?>