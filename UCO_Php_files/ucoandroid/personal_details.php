<?php
include("config.php");

$db = new DB_Connect();
$con = $db->connect();

$member_no = $_POST['memberNo'];

$result = array();

$sql_query = "SELECT m.name, m.dob, m.ret_date, m.branch_code, b.name AS branch_name, m.mobile_no,
				m.email, m.account_no, m.pancard_no, m.nominee_name, m.address, m.nominee_relationship 
				FROM `member` m JOIN branch b ON m.branch_code = b.code
				WHERE m.member_no = '".$member_no."'";

$query = mysqli_query($con, $sql_query);

$row = mysqli_fetch_array($query);

if($row){
	array_push($result,array('name'=>$row['name'],
	'dob'=>$row['dob'],
	'ret_date'=>$row['ret_date'],
	'branch_code'=>$row['branch_code'],
	'branch_name'=>$row['branch_name'],
	'mobile_no'=>$row['mobile_no'],
	'email'=>$row['email'],
	'account_no'=>$row['account_no'],
	'pancard_no'=>$row['pancard_no'],
	'nominee_name'=>$row['nominee_name'],
	'address'=>$row['address'],
	'nominee_relationship'=>$row['nominee_relationship']));
}

echo json_encode($result);    

mysqli_close($con);
?>