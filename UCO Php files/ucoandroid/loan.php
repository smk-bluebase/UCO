<?php
include("config.php");
include("db_connect.php");

$db = new DB_Connect();
$con = $db->connect();

$memberNo = $_POST['memberNo'];

$sql_query = "SELECT DISTINCT lt.member_no, lt.name, lt.surety_no, lt.surety_name, lt.amount, lt.flag_id, lt.date, 
			DATE_ADD(lt.date,interval 90 day) AS loan_ap_date, m.doa AS member_join_bank, m.ret_date AS member_ret_date,
			s.ret_date AS surety_ret_date, mb.srf_od_balance, mb.thrift_od_balance, mb.surety_od_balance, mb.surety_od_interest, mb.festival_od_balance, 
			mb.festival_od_interest, mb.flood_od_balance, mb.flood_od_interest FROM member_balance AS mb JOIN loan_transactions AS lt 
			ON mb.member_no = lt.member_no JOIN member m ON lt.member_no = m.member_no JOIN member s ON s.member_no = lt.surety_no 
			WHERE m.member_no = ".$memberNo." ORDER BY DATE DESC LIMIT 0, 1";

$res = mysqli_query($con, $sql_query);

$result = [];

while($row = mysqli_fetch_array($res)){
	$result[] =  array(
		"suretyName"=>$row[3], 
		"suretyNo"=>$row[2],
		"DOR"=>$row[9],
		"suretyRetireDate"=>$row[10],
		"lastLoanAmount"=>$row[4],
		"lastLoanDate"=>$row[6],
		"nextLoanDate"=>$row[7]
	);
}

if(empty($result)){
	$result[] =  array(
		"suretyName"=>" ", 
		"suretyNo"=>" ",
		"DOR"=>" ",
		"suretyRetireDate"=>" ",
		"lastLoanAmount"=>" ",
		"lastLoanDate"=>" ",
		"nextLoanDate"=>" "
	);
}

echo json_encode($result);    

mysqli_close($con);
?>