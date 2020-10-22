<?php
include("config.php");
include("db_connect.php");

$db = new DB_Connect();
$con = $db->connect();

$memberNo = $_POST['memberNo'];

$sql_query = "SELECT fb.fdr_no, fb.amount, fb.maturity_date, fip.pay  FROM fd_balance fb JOIN fd_interest_payable fip ON fb.fd_interest_payable_code = fip.code WHERE flag_id = 2 AND member_no = ".$memberNo."";

$res = mysqli_query($con, $sql_query);

$result = array();

$row = mysqli_fetch_array($res);

if($row == 0){ 
	array_push($result, array('FDR_No'=>" ", "Amount"=>" ", "Maturity Date"=>" ", "Pay"=>" "));
}else {
	$res = mysqli_query($con, $sql_query);
	while($row = mysqli_fetch_array($res)){
		array_push($result, array('FDR_No'=>$row['fdr_no'],
								  'Amount'=>$row['amount'], 
								  'Maturity Date'=>$row['maturity_date'],
								  'Pay'=>$row['pay']));
	}
}

echo json_encode($result);    

mysqli_close($con);
?>