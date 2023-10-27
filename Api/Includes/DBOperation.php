<?php

class DBOperation{

    private $con;

    function __construct()
    {

        require_once dirname(__FILE__) .'/DBconnection.php';


        $db = new DBconnect();

        $this->con =$db->connect();
    }


    function criarProduto($nome, $valor, $rating){
        $stmt = $this->con->prepare("INSERT INTO produtos (nome, valor, rating) VALUES (?, ?, ?)");
        $stmt->bind_param("ssi",$nome,$valor,$rating);
        if($stmt->execute())
                return true;
        return false;
    }

    function getProduto(){
        $stmt = $this->con->prepare("SELECT id,nome,valor,rating FROM produtos");
        $stmt->execute();
        $stmt->bind_result($id,$nome,$valor,$rating);

        $produtos = array();

        while($stmt->fetch()){
            $produto = array();
            $produto['id'] = $id;
            $produto['nome'] = $nome;
            $produto['valor'] = $valor;
            $produto['rating'] = $rating;
            
            array_push($produtos, $produto);
        }
        return $produtos;

    }


    function updateProduto($nome, $valor, $rating){
        $stmt = $this->con->prepare("UPDATE produtos SET nome = ?, valor = ?,, rating = ? WHERE id = ?");
        $stmt->bind_param("ssii",$nome,$valor,$rating,$id);
        if($stmt->execute())
                return true;
        return false;
        
    }


    function deletarProduto($id){
        $stmt = $this->con->prepare("DELETE FROM produtos WHERE id = ? ");
        $stmt->bind_param("i",$id);
        if($stmt->execute())
            return true;

        return false;
    }
}
    
    


?>