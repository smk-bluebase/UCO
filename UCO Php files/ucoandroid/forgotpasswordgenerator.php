<?php
include("config.php");
include("db_connect.php");
require 'sendmail.php';

$db = new DB_Connect();
$con = $db->connect();

$userName = $_POST['userName'];
$email = $_POST['email'];

function generateAndSendEmail($con, $userName, $email){
    $otp = rand(1000000, 10000000);

    $subject = "UCO APP Forgot Password";
    $message = "Dear ".$userName.",<br/><br/>

    Looks like you have forgotten your password. Please enter<br/>
    the OTP below to authenticate yourself.<br/><br/>

    OTP - ".$otp."<br/><br/>

    Continue with the UCO App!<br/><br/>

    Regards,<br/>
    UCO Team";

    $sql_query = "UPDATE `password_reset` SET `status` = 1 WHERE `member_no` = '".$userName."'";

    $con->query($sql_query);

    $sql_query = "INSERT INTO `password_reset` (`member_no`, `email_id`, `token`, `status`) VALUES ('$userName','$email','$otp', 0)";

    $con->query($sql_query);

    if(sendMail($email, $subject, $message)){
        $result = array("status"=>"true");
    }else{
        $result = array("status"=>"false");
    }

    return $result;
}

$sql_query = "SELECT email FROM member WHERE emp_no = '".$userName."'";

$res = mysqli_query($con, $sql_query);

$result = "";

while($row = mysqli_fetch_array($res)){
    $result = $row['email'];
}

if($email == $result){
    $result = generateAndSendEmail($con, $userName, $email);
}

echo json_encode([$result]);

mysqli_close($con);
?>











































































































