<?php
if(!empty($_POST['phone']) && !empty($_POST['password']) && !empty($_POST['firstName']) && !empty($_POST['lastName']) && !empty($_POST['email'])){
    $con = mysqli_connect("localhost","root","","UpMoBank");
    $firstName = $_POST['firstName'];
    $lastName = $_POST['lastName'];
    $phone = $_POST['phone'];
    $email = $_POST['email'];
    $password = password_hash($_POST['password'], PASSWORD_DEFAULT);
    if($con){
        $sql = "insert into accounts(phone, password, firstName, lastName, email, balanceUA, balanceUS, balanceEU) values ('".$phone."', '".$password."', '".$firstName."', '".$lastName."', '".$email."', 10000.01, 1000.02, 500.03)";
        if(mysqli_query($con, $sql)){
            echo "success";
        }else echo "Registration failed";
    }else echo "Database connection faied";
} else echo "All fields are required";
