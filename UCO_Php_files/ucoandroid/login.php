<?php
include("config.php");

$db = new DB_Connect();
$con = $db->connect();

$userName = $_POST["userName"];
$password = $_POST["password"];

$sql_query = "SELECT um.member_id, mb.name 
				FROM user_master um JOIN member mb ON um.member_id = mb.member_no 
				WHERE um.username = '".$userName."' AND um.password = '".$password."'";
 
$res = mysqli_query($con, $sql_query);
 
$result = array("status"=>"false");
 
while($row = mysqli_fetch_array($res)){
	$result = array("status"=>"true",
				 "memberNo"=>$row[0],
				 "memberName"=>$row[1]
			);
}
 
echo json_encode([$result]);
 
mysqli_close($con);
?>