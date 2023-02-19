<?php
if(!empty($_POST['name']) && !empty($_POST['email']) && !empty($_POST['password'])){
    $con = mysqli_connect("localhost","root","","UpMoBank");
    $name = $_POST['name'];
    $email = $_POST['email'];
    $password = password_hash($_POST['password'], PASSWORD_DEFAULT);
    if($con){
        $sql = "insert into users(name, email, password) values ('".$name."', '".$email."', '".$password."')";
        if(mysqli_query($con, $sql)){
            echo "success";
        }else echo "Registration failed";
    }else echo "Database connection faied";
} else echo "All fields are required";
