<?php
include("config.php");

$db = new DB_Connect();
$con = $db->connect();

$result = array();

$sql_query = "SELECT m.member_no, m.name, m.mobile_no, m.email, m.branch_code, b.name AS branch_name 
				FROM member m JOIN branch b ON m.branch_code = b.code 
				WHERE m.flag_id = 2 AND m.name IS NOT NULL AND m.mobile_no IS NOT NULL 
				ORDER BY m.name";

$query = mysqli_query($con, $sql_query);

while($row = mysqli_fetch_array($query)){
	array_push($result,array(
		'memberNo'=>$row['member_no'],
		'name'=>$row['name'],
		'mobileNo'=>$row['mobile_no'],
		'email'=>$row['email'],
		'branch'=>$row['branch_name']
	));
}

echo json_encode($result);   

mysqli_close($con);
?>