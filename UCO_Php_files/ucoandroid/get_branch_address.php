<?php
include("config.php");

$db = new DB_Connect();
$con = $db->connect();

$memberNo = $_POST['memberNo'];
 
$sql_query = "SELECT UPPER(name) as name, UPPER(zone) as zone
                FROM branch
                WHERE code IN (SELECT branch_code FROM member WHERE member_no = '".$memberNo."')";
 
$res = mysqli_query($con, $sql_query);
 
$result = [];
 
while($row = mysqli_fetch_array($res)){
    $result = array("name"=>$row["name"],
                "zone"=>$row["zone"],
            );
}
 
echo json_encode([$result]);
 
mysqli_close($con);
?>