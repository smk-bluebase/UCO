<?php
include("config.php");

$db = new DB_Connect();
$con = $db->connect();

$memberNo = $_POST['memberNo'];
$fromYear = $_POST['fromYear'];
$toYear = $_POST['toYear'];

$fromDate = $fromYear . "-04-01";
$toDate = $toYear . "-03-31";

$suretyBalance = 0;
$interestRate = 0;

$interestRecovered = 0;
$interestToBeRecovered = 0;
$total = 0;

$sql_query = "SELECT SUM(`interest`) AS surety_interest_sum, SUM(`od_interest`) AS surety_od_interest_sum
                FROM surety_loan 
                WHERE member_no = '".$memberNo."' AND `date` BETWEEN '".$fromDate."' AND '".$toDate."'";

$res = mysqli_query($con, $sql_query);

$result = [];

while($row = mysqli_fetch_array($res)){
    $interestRecovered = $row['surety_interest_sum'];
}

$sql_query = "SELECT interest_rate
                FROM loan_type
                WHERE code='LON-001'";

$res = mysqli_query($con, $sql_query);

while($row = mysqli_fetch_array($res)){
    $interestRate = $row['interest_rate'];
}

$sql_query = "SELECT surety_balance
                FROM member_balance 
                WHERE member_no = '".$memberNo."'";

$res = mysqli_query($con, $sql_query);

while($row = mysqli_fetch_array($res)){
    $suretyBalance = $row['surety_balance'];
}


// Calculating remaining date
$ts1 = strtotime($fromDate);
$ts2 = strtotime($toDate);

$year1 = date('Y', $ts1);
$year2 = date('Y', $ts2);

$month1 = date('m', $ts1);
$month2 = date('m', $ts2);


$year_diff = (($year2 - $year1) * 12);
$month_diff = ($month2 - $month1);

$diff = $year_diff + $month_diff;

$remaining_month=12 - $diff;


// Calculating interest and total
$interestToBeRecovered = ((($suretyBalance * $interestRate)/100)/12);
$interestToBeRecovered = round($interestToBeRecovered) * $remaining_month;
$total = $interestRecovered + $interestToBeRecovered;

$result[] = ["interestRecovered" => (int) $interestRecovered,
             "interestToBeRecovered" => (int) $interestToBeRecovered,
             "total" => (int) $total,
             "fromDate"=>$fromDate,
             "toDate"=>$toDate];

echo json_encode($result);    

mysqli_close($con);
?>