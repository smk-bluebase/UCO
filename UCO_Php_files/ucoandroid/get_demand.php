<?php
include("config.php");
include("db_connect.php");

$db = new DB_Connect();
$con = $db->connect();

$memberNo = $_POST['memberNo'];

$result = array();

$sql_query = "SELECT srf, srf_od_balance, thrift, thrift_od_balance, surety_principal, surety_interest, surety_od_balance, surety_od_interest, festival_principal, festival_interest, festival_od_balance, festival_od_interest, flood_principal, flood_od_balance, demand_amount FROM demand WHERE member_no = $memberNo ORDER BY id DESC LIMIT 0,1";

$res = mysqli_query($con, $sql_query);

while($row = mysqli_fetch_array($res)){

	array_push($result, array(
		'srf'=>$row['srf'],
		'srfOD'=>$row['srf_od_balance'],
		'thrift'=>$row['thrift'],
		'thriftOD'=>$row['thrift_od_balance'],
		'suretyPrinciple'=>$row['surety_principal'],
		'suretyODPrinciple'=>$row['surety_od_balance'],
		'suretyInterest'=>$row['surety_interest'],
		'suretyODInterest'=>$row['surety_od_interest'],
		'festivalPrinciple'=>$row['festival_principal'],
		'festivalODPrinciple'=>$row['festival_od_balance'],
		'festivalInterest'=>$row['festival_interest'],
		'festivalODInterest'=>$row['festival_od_interest'],
		'floodPrinciple'=>$row['flood_principal'],
		'floodODPrinciple'=>$row['flood_od_balance'],
		'demandAmount'=>$row['demand_amount']));

}

echo json_encode($result);    

mysqli_close($con);
?> 