<?php
include("config.php");
include("db_connect.php");

$db = new DB_Connect();
$con = $db->connect();

$memberNo = $_POST['memberNo'];
 
$sql_query = "SELECT member_no, name, surety_no, surety_name FROM member_mapping WHERE member_no = '".$memberNo."' ORDER BY name";
 
$res = mysqli_query($con, $sql_query);
 
$result = [];
 
while($row = mysqli_fetch_array($res)){
	$result[] = array(
		'Member No'=>$row['member_no'],
		'Name'=>$row['name'],
		'Surety No'=>$row['surety_no'],
		'SuretyName'=>$row['surety_name']
	);
}

if(empty($result)){
	$result[] = array(
		'Member No'=>" ",
		'Name'=>" ",
		'Surety No'=>" ",
		'SuretyName'=>" "
	);
}
 
echo json_encode($result);
 
mysqli_close($con);
?>