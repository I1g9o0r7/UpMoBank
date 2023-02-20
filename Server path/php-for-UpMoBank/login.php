<?php
if(!empty($_POST['phone']) && !empty($_POST['password'])){
    $con = mysqli_connect("localhost","root","","dbUpMoBank");
    $phone = $_POST['phone'];
    $password = $_POST['password'];
    $result = array();
    if($con){
        $sql = "select * from accounts where phone = '".$phone."'"; //  '".$phone."'";
        $res = mysqli_query($con, $sql);
        if(mysqli_num_rows($res) != 0){
            $row = mysqli_fetch_assoc($res);
            if($phone == $row['phone'] && password_verify($password, $row['password'])){
                try {
                    $apiKey = bin2hex(random_bytes(16));
                } catch (Exception $e) {
                    $apiKey = bin2hex(uniqid($phone, true));
                }
                $sqlUpdate = "update accounts set apiKey = '".$apiKey."' where phone = '".$phone."'";
                if(mysqli_query($con, $sqlUpdate)){
                    $result = array(
                        "status"=>"success", "message" => "Login successful", 
                        "numCard"=> $row['numCard'], "phone"=>$row['phone'], 
                        "firstName"=>$row['firstName'], "lastName"=>$row['lastName'], "email"=>$row['email'],
                        "balanceUA"=>$row['balanceUA'], "balanceUS"=>$row['balanceUS'], "balanceEU"=>$row['balanceEU'],
                        "apiKey"=> $apiKey
                    );

                }else $result = array("status"=>"failed", "message" => "Login failed try again");
            }else $result = array("status"=>"failed", "message" => "Retry with correct password");
        }else $result = array("status"=>"failed", "message" => "Retry with correct phone");
    }else $result = array("status"=>"failed", "message" => "Database connection failed");
} else $result = array("status"=>"failed", "message" => "All faileds are required");


echo json_encode($result, JSON_PRETTY_PRINT);
