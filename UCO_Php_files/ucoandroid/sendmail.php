<?php
require 'phpmailer/class.phpmailer.php';
require 'phpmailer/class.smtp.php';
require 'phpmailer/PHPMailerAutoload.php';

function sendMail($email, $subject, $message){
    $mail = new PHPMailer;
    $mail->isSMTP();
    $mail->Host = 'smtp.zoho.com';
    $mail->Port = 465;
    $mail->SMTPAuth = true;
    $mail->SMTPSecure = "ssl";
    $mail->Pool = true;
    $mail->Mailer   = 'smtp';
    $mail->Username = 'uco@ucosocietychennai.in';
    $mail->Password = 'Welcome@123';

    $mail->setFrom('uco@ucosocietychennai.in', 'UCOSAS');
    $mail->addReplyTo('uco@ucosocietychennai.in', 'UCOSAS');
    $mail->addAddress($email);

    $mail->isHTML(true);	
    $mail->Subject = $subject;
    $mail->Body = $message;

    if($mail->send()){
        return true;
    }else{
        return false;
    }
}

?>