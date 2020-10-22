<?php
include("config.php");
include("db_connect.php");

$db = new DB_Connect();
$con = $db->connect();

$memberNo = $_POST['memberNo'];
$currentPassword = $_POST['currentPassword'];
$newPassword = $_POST['newPassword'];

$result = array();

$oldPassword = "";

$sql_query = "SELECT password FROM user_master WHERE member_id = '".$memberNo."'";

$res = mysqli_query($con, $sql_query);

$row = mysqli_fetch_array($res);

if($row){
	$oldPassword = $row['password'];
}

if($oldPassword == $currentPassword){
	$sql_query = "UPDATE user_master SET password = '".$newPassword."' WHERE member_id = '".$memberNo."'";

	if(mysqli_query($con, $sql_query)){
		array_push($result, array('status'=>'Your password has been changed.'));
	}
}else{
	array_push($result, array('status'=>'Please enter yout Old password correctly.'));
}

echo json_encode($result);    

mysqli_close($con);
?>