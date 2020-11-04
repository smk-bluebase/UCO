<?php
include("config.php");

$db = new DB_Connect();
$con = $db->connect();

$otp = $_POST['otp'];
$userName = $_POST['userName'];
$password = $_POST['password'];

$sql_query = "SELECT * 
                FROM `password_reset` 
                WHERE token = '".$otp."' AND member_no = '".$userName."'
                LIMIT 1";

$res = mysqli_query($con, $sql_query);

$result = array("status"=>"false");

while($row = mysqli_fetch_array($res)){
    $result = $row['status'];

    if ($row['status'] == 0) {
    
        $sql_query = "UPDATE `user_master` 
                        SET `password` = '".$password."' 
                        WHERE username = '".$userName."'";

        $con->query($sql_query);

        $sql_query = "UPDATE `password_reset` 
                        SET `status` = 1 
                        WHERE member_no = '".$userName."'";

        $con->query($sql_query);

        $result = array("status"=>"true");
    }
}

echo json_encode([$result]);

mysqli_close($con);
?>