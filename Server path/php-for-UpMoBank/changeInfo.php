<?php
if(!empty($_POST['phone']) && !empty($_POST['email']) && !empty($_POST['apiKey'])){
    $con = mysqli_connect("localhost","root","","UpMoBank");
    $phone = $_POST['phone'];
    $email = $_POST['email'];
    $apiKey = $_POST['apiKey'];
    if($con){
        $sql = "select * from accounts where apiKey = '".$apiKey."'";
        $res = mysqli_query($con, $sql);
        if(mysqli_num_rows($res) != 0){
            $row = mysqli_fetch_assoc($res);
            if(!empty($_POST['password'])){
                $password = password_hash($_POST['password'], PASSWORD_DEFAULT);
                $sqlUpdate = "update accounts set phone = '".$phone."', email = '".$email."', password = '".$password."' where apiKey = '".$apiKey."'";
                if(mysqli_query($con, $sqlUpdate)){
                    echo "success";
                }else echo "Login failed";

            } else{
                $sqlUpdate = "update accounts set phone = '".$phone."', email = '".$email."' where apiKey = '".$apiKey."'";
                if(mysqli_query($con, $sqlUpdate)){
                    echo "success";
                }else echo "Login failed";
            }
        }else echo "Unauthorised to access";
    }else echo "Database connection faied";
} else echo "All fields are required";


