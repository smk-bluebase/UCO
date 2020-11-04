<?php
include("config.php");

$db = new DB_Connect();
$con = $db->connect();

$memberNo = $_POST['memberNo'];
 
$sql_query = "SELECT member_no, name, surety_no, surety_name
				FROM member_mapping
				WHERE member_no = '".$memberNo."'
				ORDER BY name";
 
$res = mysqli_query($con, $sql_query);
 
$result = [];
 
while($row = mysqli_fetch_array($res)){
	$result['registered_members'][] = array(
		'Member No'=>$row['member_no'],
		'Name'=>$row['name'],
		'Surety No'=>$row['surety_no'],
		'SuretyName'=>$row['surety_name']
	);
}

if(empty($result)){
	$result['registered_members'][] = array(
		'Member No'=>" ",
		'Name'=>" ",
		'Surety No'=>" ",
		'SuretyName'=>" "
	);
}

$sql_query = "SELECT m.member_no, m.name
				FROM member m
				WHERE m.employee_class = 'class a' AND m.flag_id = 2 
				AND NOT EXISTS (SELECT '' FROM member_mapping mp WHERE m.member_no = mp.member_no)
				ORDER BY name";

$res = mysqli_query($con, $sql_query);

while($row = mysqli_fetch_array($res)){
	$result['available_members'][] = array(
		'Member No'=>$row['member_no'],
		'Name'=>$row['name']
	);
}
 
echo json_encode([$result]);
 
mysqli_close($con);
?>