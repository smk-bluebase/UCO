<?php
include("config.php");

$db = new DB_Connect();
$con = $db->connect();

$member_no = $_POST['memberNo'];
$member_name = $_POST['memberName'];

$result = array();
$demand_msg = "";
$demand_collection_msg = "";
$new_loan_msg = "";
$closed_loan_msg = "";
$count_fd_msg = "";
$interest_fd_msg = "";

//Demand
$sql_query = "SELECT date, demand_amount 
				FROM demand 
				WHERE member_no = '".$member_no."' 
				ORDER BY id DESC LIMIT 1";

$query = mysqli_query($con, $sql_query);

$row = mysqli_fetch_array($query);

if($row > 0){
	$demand_msg = "Last demand raised for Rs:".$row[1]." on ".date("d-m-Y",strtotime($row[0]));
}

//Demand Collection
$sql_query = "SELECT date, collection_amount
				 FROM demand_collection
				 WHERE member_no = '".$member_no."'
				 ORDER BY id DESC LIMIT 1";

$query = mysqli_query($con, $sql_query);

$row = mysqli_fetch_array($query);

if($row > 0){
	$demand_collection_msg = "Last demand collection for Rs:".$row[1]." on ".date("d-m-Y",strtotime($row[0]));
}

//New Loan
$sql_query = "SELECT date, amount
				FROM loan_transactions 
				WHERE member_no = '".$member_no."' AND flag_id = 2 
				ORDER BY id DESC LIMIT 1";

$query = mysqli_query($con, $sql_query);

$row = mysqli_fetch_array($query);

if($row > 0){
	$new_loan_msg = "You have taken loan Rs:".$row[1]." on ".date("d-m-Y",strtotime($row[0]));
}

//Closed Loan
$sql_query = "SELECT date, amount
				FROM loan_closed
				WHERE member_no = '".$member_no."'
				ORDER BY id DESC LIMIT 1";

$query = mysqli_query($con, $sql_query);

$row = mysqli_fetch_array($query);

if($row > 0){
	$closed_loan_msg = "Previous loan Rs:".$row[1]." closed on ".date("d-m-Y",strtotime($row[0]));
}

//FD Counts Display
$sql_query = "SELECT count(*) as count 
				FROM fd_balance 
				WHERE member_no = '".$member_no."' AND flag_id = 2";

$query = mysqli_query($con, $sql_query);

$row = mysqli_fetch_array($query);

if($row > 0){
	$count_fd_msg = "You have ".$row[0]." Fixed Deposit";
}

//FD Interest Sum
$sql_query = "SELECT ft.date, sum(ft.interest_amount) AS sum_amount
				FROM fd_balance f JOIN fd_interest_transaction ft ON ft.fdr_no = f.fdr_no 
				AND f.member_no = ".$member_no." AND f.flag_id = 2
				ORDER BY ft.id DESC LIMIT 1";

$query = mysqli_query($con, $sql_query);

$row = mysqli_fetch_array($query);

if($row > 0){
	$interest_fd_msg = "Fixed deposit interest Rs:".$row[1]." posted on ".date("d-m-Y",strtotime($row[0]));
}

$member = $member_name."    ".$member_no;

//Return Results
array_push($result,array(
	'member'=>$member,
	'demand_msg'=>$demand_msg,
	'demand_collection_msg'=>$demand_collection_msg,
	'new_loan_msg'=>$new_loan_msg,
	'closed_loan_msg'=>$closed_loan_msg,
	'count_fd_msg'=>$count_fd_msg,
	'interest_fd_msg'=>$interest_fd_msg
));

echo json_encode($result);    

mysqli_close($con);
?>