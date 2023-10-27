<?php

require_once '../includes/DBOperation.php';


function parametrosEstaoDisponiveis($params){
    $disponivel = true;
    $missingParams = "";

    foreach($params as $param){
        if(!isset($_POST[$param]) || strlen($_POST[$param])<=0){
            $disponivel = false;
            $missingParams = $missingParams . ", ".$param;
        }
    }

    if(!$disponivel){
        $response = array();
        $response['error'] = true;
        $response['message'] = 'Parâmetros '. substr($missingParams, 1, strlen($missingParams)) . ' desaparecidos';

        echo json_encode($response);


        die();
    }

}
$response = array();

    if(isset($_GET['apicall'])){

        switch($_GET['apicall']){

            //Cria o Usuario
            case 'criarProduto':

            parametrosEstaoDisponiveis(array('nome','valor','rating'));

            $db = new DBOperation();

        $result = $db->criarProduto(
            $_POST['nome'],
            $_POST['valor'],
            $_POST['rating'],
        );

        if($result){
            $response['error'] = false;
            $response['message'] = 'Produto criado com sucesso';
            $response['produtos']= $db->getProduto();

        }else{
            $response['error'] = true;
            $response['message'] = 'Ocorreu um erro por favor tente novamente';
            
        }
        break;

        //Mostra os produtos no banco
            case 'getProduto':
                $db = new DBOperation();
                $response['error'] = false;
                $response['message'] = 'Pedido feito com sucesso';
                $response['produtos']= $db->getProduto();
                break;

        //Atualiza os produtos
            case 'updateProduto':
                parametrosEstaoDisponiveis(array('id','nome','valor','rating'));

             $db = new DBOperation();
    
                $result = $db->updateProduto(
                    $_POST['id'],
                    $_POST['nome'],
                    $_POST['valor'],
                    $_POST['rating'],
            );

                if($result){
                    $response['error'] = false;
                    $response['message'] = 'Usuário atualizado com sucesso';
                    $response['produtos']= $db->getProduto();
                }else{
                    $response['error'] = true;
                    $response['message'] = 'Houve um erro por favor tente novamente';

            }
        break;
    
        //Deleta o produto
   
        
            case 'deletarProduto':

                if(isset($_POST['id'])){
                    $db = new DBOperation();
                    if($db->deletarProduto($_POST['id'])){
                            $response['error'] = false;
                            $response['message'] = 'Produto excluido com sucesso';
                            $response['usuarios']= $db->getProduto();
                    }else{
                            $response['error'] = true;
                            $response['message'] = 'Houve um erro por favor tente novamente';
                    }
                }else{
                            $response['error'] = true;
                            $response['message'] = 'Nada para apagar tente novamente';
                }
        break;

    }
    
    }else{
        $response['error'] = true;
        $response['message'] = 'Chamada de API inválida';

    }
    echo json_encode($response);

?>