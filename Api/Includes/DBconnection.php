<?php

class DBconnect{
    
    private $con;

    function __construct(){
        
    }
    function connect(){
        include_once dirname(__FILE__).'/Constants.php';
    
        $this->con = new mysqli(DB_HOST,DB_USER,DB_PASS,DB_NAME);

        if(mysqli_connect_errno()){
            echo "Falha ao conectar ao MySQL: ".mysqli_connect_errno();
        }

        return $this->con;

    }

}
?>