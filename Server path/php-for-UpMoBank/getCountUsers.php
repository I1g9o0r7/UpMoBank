<?php
$con = mysqli_connect("localhost","root","","UpMoBank");
if($con){
    $sql = "select count(id) from accounts";
    $res = mysqli_query($con, $sql);
    $row = mysqli_fetch_array($res);
    echo $row[0];
}else echo "Database connection faied";
