<?php

if(!empty($_POST['apiKey'])){
    $con = mysqli_connect("localhost","root","","UpMoBank");
    $apiKey = $_POST['apiKey'];
    $result = array();
    if($con){
        $sql = "select * from accounts where apiKey = '".$apiKey."'";
        $res = mysqli_query($con, $sql);
        if(mysqli_num_rows($res) != 0){
            $row = mysqli_fetch_assoc($res);

            $result = array(
                "status"=>"success", "message" => "Login successful", 
                "id"=> $row['id'], "phone"=>$row['phone'], 
                "firstName"=>$row['firstName'], "lastName"=>$row['lastName'], "email"=>$row['email'],
                "balanceUA"=>$row['balanceUA'], "balanceUS"=>$row['balanceUS'], "balanceEU"=>$row['balanceEU']
            );
        }else $result = array("status"=>"failed", "message" => "Retry with correct phone");
    }else $result = array("status"=>"failed", "message" => "Database connection failed");
} else $result = array("status"=>"failed", "message" => "All faileds are required");


echo json_encode($result, JSON_PRETTY_PRINT);