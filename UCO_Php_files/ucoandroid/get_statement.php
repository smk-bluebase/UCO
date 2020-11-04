<?php
include("config.php");

$db = new DB_Connect();
$con = $db->connect();

$memberNo = $_POST['memberNo'];

// SRF
$sql_query = "SELECT DATE_FORMAT(date, '%d-%m-%Y') AS date, amount, debit_amount, current_amount, od_amount 
                FROM srf 
                WHERE member_no = '".$memberNo."'
                ORDER BY id DESC LIMIT 5";
 
$res = mysqli_query($con, $sql_query);

$result = array();
 
while($row = mysqli_fetch_array($res)){
    $result['srf'][] = array("Date"=>$row['date'],
                        "Amount"=>$row["amount"],
                        "Debit Amount"=>$row["debit_amount"],
                        "Current Amount"=>$row["current_amount"]
                    );
}

if(empty($result['srf'])){
    $result['srf'][] = array("Date"=>" ",
                        "Amount"=>" ",
                        "Debit Amount"=>" ",
                        "Current Amount"=>" "
                    );
}


// THRIFT
$sql_query = "SELECT DATE_FORMAT(date, '%d-%m-%Y') AS date, amount, debit_amount, current_amount, od_amount
                FROM thrift_deposit
                WHERE member_no = '".$memberNo."'
                ORDER BY id DESC LIMIT 5";

$res = mysqli_query($con, $sql_query);

while($row = mysqli_fetch_array($res)){
    $result['thrift'][] = array("Date"=>$row["date"],
                            "Amount"=>$row["amount"],
                            "Debit Amount"=>$row["debit_amount"],
                            "Current Amount"=>$row["current_amount"]
                        );
}

if(empty($result['thrift'])){
    $result['thrift'][] = array("Date"=>" ",
                            "Amount"=>" ",
                            "Debit Amount"=>" ",
                            "Current Amount"=>" "
                        );
}


// SURETY
$sql_query = "SELECT DATE_FORMAT(date, '%d-%m-%Y') AS date, interest, principal, debit_principal, balance, od_balance
                FROM surety_loan
                WHERE member_no = '".$memberNo."'
                ORDER BY id DESC LIMIT 5";

$res = mysqli_query($con, $sql_query);

while($row = mysqli_fetch_array($res)){
    $result['surety'][] = array("Date"=>$row["date"],
                            "Interest"=>$row["interest"],
                            "Amount"=>$row["principal"],
                            "Debit Amount"=>$row["debit_principal"],
                            "Current Amount"=>$row["balance"]
                        );
}

if(empty($result['surety'])){
    $result['surety'][] = array("Date"=>" ",
                            "Interest"=>" ",
                            "Amount"=>" ",
                            "Debit Amount"=>" ",
                            "Current Amount"=>" "
                        );
}


// FESTIVAL
$sql_query = "SELECT DATE_FORMAT(date, '%d-%m-%Y') AS date, interest, principal, debit_principal, balance, od_balance
                FROM festival_loan
                WHERE member_no = '".$memberNo."'
                ORDER BY id DESC LIMIT 5";

$res = mysqli_query($con, $sql_query);

while($row = mysqli_fetch_array($res)){
    $result['festival'][] = array("Date"=>$row["date"],
                            "Interest"=>$row["interest"],
                            "Amount"=>$row["principal"],
                            "Debit Amount"=>$row["debit_principal"],
                            "Current Amount"=>$row["balance"]
                        );
}

if(empty($result['festival'])){
    $result['festival'][] = array("Date"=>" ",
                            "Interest"=>" ",
                            "Amount"=>" ",
                            "Debit Amount"=>" ",
                            "Current Amount"=>" "
                        );
}
 
echo json_encode([$result]);
 
mysqli_close($con);
?>