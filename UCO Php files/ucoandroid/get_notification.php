<?php
include("config.php");
include("db_connect.php");

$db = new DB_Connect();
$con = $db->connect();

$member_no = $_POST['memberNo'];

$sql_query = "SELECT id, message FROM notifications where status = 0 and member_no = ".$member_no;

$result=array();

$query=mysqli_query($con, $sql_query);

while($row=mysqli_fetch_array($query)){
	array_push($result,array('id'=>$row[0],'message'=>$row[1]));
}

echo json_encode($result);   

mysqli_close($con);
?>