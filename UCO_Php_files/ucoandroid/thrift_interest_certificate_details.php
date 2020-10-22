<?php
include("config.php");
include("db_connect.php");

$db = new DB_Connect();
$con = $db->connect();

$memberNo = $_POST['memberNo'];
$year = $_POST['year'];

$sql_query = "SELECT account_no FROM member WHERE member_no = ".$memberNo."";

$res = mysqli_query($con, $sql_query);

$accountNo = 0;

while($row = mysqli_fetch_array($res)){
    $accountNo = $row['account_no'];
}

$sql_query = "SELECT member_no, name, date, acc_no, interest_amount FROM thrift_interest_transaction WHERE member_no = ".$memberNo." AND year(date) = '".$year."'";

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