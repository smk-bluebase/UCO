<?php
include("config.php");

$db = new DB_Connect();
$con = $db->connect();

$memberNo = $_POST['memberNo'];
$fromYear = $_POST['fromYear'];
$toYear = $_POST['toYear'];

$sql_query = "SELECT account_no
                FROM member 
                WHERE member_no = ".$memberNo."";

$res = mysqli_query($con, $sql_query);

$accountNo = 0;

while($row = mysqli_fetch_array($res)){
    $accountNo = $row['account_no'];
}

$sql_query = "SELECT member_no, name, date, acc_no, interest_amount
                FROM thrift_interest_transaction
                WHERE member_no = ".$memberNo." AND year(date) = '".$toYear."'";

$res = mysqli_query($con, $sql_query);

$result = [];

while($row = mysqli_fetch_array($res)){
    $result[] =  array("accountNo"=>$accountNo,
                              "interestAmount"=>$row["interest_amount"]);
}

if (empty($result)){
    $result[] = array("accountNo"=>$accountNo,
                "interestAmount"=>0);
}

echo json_encode($result);    

mysqli_close($con);
?>