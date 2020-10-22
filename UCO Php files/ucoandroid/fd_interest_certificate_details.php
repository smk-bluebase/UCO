<?php
include("config.php");
include("db_connect.php");

$db = new DB_Connect();
$con = $db->connect();

$memberNo = $_POST['memberNo'];
$year = $_POST['year'];

$fromDate = "";
$toDate = "";

if($year == 2020){
    $fromDate = "2019-04-01";
    $toDate = "2020-03-31";
}else if($year == 2021){
    $fromDate = "2020-04-01";
    $toDate = "2021-03-31";
}

$sql_query = "SELECT fdr_no, amount, sum(interest_amount) as interest_amount FROM `fd_interest_posting` WHERE date BETWEEN '".$fromDate."' and '".$toDate."' and member_no = '".$memberNo."' GROUP by fdr_no";

$res = mysqli_query($con, $sql_query);

$result = [];

while($row = mysqli_fetch_array($res)){
    $result[] = array("fdr_no"=>$row['fdr_no'], 
                      "amount"=>round($row['amount']), 
                      "interest_amount"=>round($row['interest_amount']));
}

echo json_encode($result);

mysqli_close($con);
?>