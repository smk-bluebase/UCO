<?php
include("config.php");

$db = new DB_Connect();
$con = $db->connect();

$memberNo = $_POST['memberNo'];
$fromYear = $_POST['fromYear'];
$toYear = $_POST['toYear'];

$fromDate = $fromYear . "-04-01";
$toDate = $toYear . "-03-31";

$sql_query = "SELECT fdr_no, amount, sum(interest_amount) AS interest_amount
                FROM `fd_interest_posting` 
                WHERE date BETWEEN '".$fromDate."' AND '".$toDate."' AND member_no = '".$memberNo."' 
                GROUP BY fdr_no";

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