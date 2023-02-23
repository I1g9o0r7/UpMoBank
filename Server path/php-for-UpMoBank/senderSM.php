<?php
if(!empty($_POST['valute']) && !empty($_POST['method']) && !empty($_POST['recipient']) && !empty($_POST['anount']) && !empty($_POST['apiKey'])){
    $con = mysqli_connect("localhost","root","","dbUpMoBank");
    $valute = $_POST['valute'];
    $method = $_POST['method'];
    $recipient = $_POST['recipient'];
    $anount = $_POST['anount'];
    $apiKey = $_POST['apiKey'];


    if($con){
        $sql = "select * from accounts"; // "select * from accounts where apiKey = '".$apiKey."'";
        $res = mysqli_query($con, $sql);
        if(mysqli_num_rows($res) != 0){
            $row = mysqli_fetch_assoc($res);

            $sql2 = "SELECT EXISTS(SELECT id FROM accounts WHERE id = ".$recipient.")";
            $res2 = mysqli_query($con, $sql2);
            $row2 = mysqli_fetch_row($res2);
            if ($row2[0] == 1)
            {
                if(!strcmp($method,'Card')){
                    $sqlUpdate1 = "update accounts set ".$valute." = ".$valute." + ".$anount." where id = ".$recipient."";
                    $sqlUpdate2 = "update accounts set ".$valute." = ".$valute." - ".$anount." where apiKey = '".$apiKey."'";
                    if(mysqli_query($con, $sqlUpdate1) && mysqli_query($con, $sqlUpdate2)){
                        echo "success";
                    }else echo "Operation failed";
                }else{
                    $sqlUpdate3 = "update accounts set ".$valute." = ".$valute." - ".$anount." where apiKey = '".$apiKey."'";
                    if(mysqli_query($con, $sqlUpdate3)){
                        echo "success";
                    }else echo "Operation failed";
                }
            } else echo "This account does not exist";
            

                

        }else echo "Unauthorised to access";
    }else echo "Database connection faied";
} else echo "All fields are required";


