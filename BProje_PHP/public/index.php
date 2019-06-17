<?php


use \Psr\Http\Message\ServerRequestInterface as Request;
use \Psr\Http\Message\ResponseInterface as Response;

require '../vendor/autoload.php';

require '../includes/DbOperations.php';

$app = new \Slim\App([
    'settings'=>[
        'displayErrorDetails'=>true
    ]
]);

$app->post('/createuser', function(Request $request, Response $response){

    if(!haveEmptyParameters(array('lat', 'lng', 'okul', 'vakit', 'ad', 'soyad', 'telefon', 'plaka'), $request, $response)){

        $request_data = $request->getParsedBody(); 

        $lat = $request_data['lat'];
        $lng = $request_data['lng'];
        $okul = $request_data['okul'];
        $vakit = $request_data['vakit'];
        $ad = $request_data['ad'];
        $soyad = $request_data['soyad'];
        $telefon = $request_data['telefon'];
        $plaka = $request_data['plaka']; 


        $db = new DbOperations; 

        $result = $db->createUser($lat, $lng, $okul, $vakit, $ad, $soyad, $telefon, $plaka);
        
        if($result == USER_CREATED){

            $message = array(); 
            $message['error'] = false; 
            $message['message'] = 'User created successfully';

            $response->write(json_encode($message));

            return $response
                        ->withHeader('Content-type', 'application/json')
                        ->withStatus(201);

        }else if($result == USER_FAILURE){

            $message = array(); 
            $message['error'] = true; 
            $message['message'] = 'Some error occurred';

            $response->write(json_encode($message));

            return $response
                        ->withHeader('Content-type', 'application/json')
                        ->withStatus(422);    

        }else if($result == USER_EXISTS){
            $message = array(); 
            $message['error'] = true; 
            $message['message'] = 'User Already Exists';

            $response->write(json_encode($message));

            return $response
                        ->withHeader('Content-type', 'application/json')
                        ->withStatus(422);    
        }
    }
    return $response
        ->withHeader('Content-type', 'application/json')
        ->withStatus(422);    
});

$app->post('/createhesap', function(Request $request, Response $response){

    if(!haveEmptyParameters(array('plaka','sifre'), $request, $response)){

        $request_data = $request->getParsedBody(); 

        $sifre = $request_data['sifre'];
        $plaka = $request_data['plaka']; 

        $hash_password = password_hash($sifre, PASSWORD_DEFAULT);


        $db = new DbOperations; 

        $result = $db->createHesap($plaka, $hash_password);
        
        if($result == USER_CREATED){

            $message = array(); 
            $message['error'] = false; 
            $message['message'] = 'User created successfully';

            $response->write(json_encode($message));

            return $response
                        ->withHeader('Content-type', 'application/json')
                        ->withStatus(201);

        }else if($result == USER_FAILURE){

            $message = array(); 
            $message['error'] = true; 
            $message['message'] = 'Some error occurred';

            $response->write(json_encode($message));

            return $response
                        ->withHeader('Content-type', 'application/json')
                        ->withStatus(422);    

        }else if($result == USER_EXISTS){
            $message = array(); 
            $message['error'] = true; 
            $message['message'] = 'User Already Exists';

            $response->write(json_encode($message));

            return $response
                        ->withHeader('Content-type', 'application/json')
                        ->withStatus(422);    
        }
    }
    return $response
        ->withHeader('Content-type', 'application/json')
        ->withStatus(422);    
});

$app->post('/userlogin', function(Request $request, Response $response){
    if(!haveEmptyParameters(array('plaka', 'sifre'), $request, $response)){
        $request_data = $request->getParsedBody(); 
        $plaka = $request_data['plaka'];
        $sifre = $request_data['sifre'];
        
        $db = new DbOperations; 
        $result = $db->userLogin($plaka, $sifre);
        if($result == USER_AUTHENTICATED){
            $response_data = array();
            $response_data['error']=false; 
            $response_data['message'] = 'Login Successful';
            $response->write(json_encode($response_data));
            return $response
                ->withHeader('Content-type', 'application/json')
                ->withStatus(200);    
        }else if($result == USER_NOT_FOUND){
            $response_data = array();
            $response_data['error']=true; 
            $response_data['message'] = 'User not exist';
            $response->write(json_encode($response_data));
            return $response
                ->withHeader('Content-type', 'application/json')
                ->withStatus(200);    
        }else if($result == USER_PASSWORD_DO_NOT_MATCH){
            $response_data = array();
            $response_data['error']=true; 
            $response_data['message'] = 'Invalid credential';
            $response->write(json_encode($response_data));
            return $response
                ->withHeader('Content-type', 'application/json')
                ->withStatus(200);  
        }
    }
    return $response
        ->withHeader('Content-type', 'application/json')
        ->withStatus(422);    
});

$app->get('/allveri/{plaka}', function(Request $request, Response $response, array $args){

		$plaka = $args['plaka'];

		$db = new DbOperations; 

    	$veriler = $db->getAllVeri($plaka);

    	$response_data = array();

    	$response_data['error'] = false; 
    	$response_data['veriler'] = $veriler; 

    	$response->write(json_encode($response_data));

   		 return $response
    	->withHeader('Content-type', 'application/json')
    	->withStatus(200); 
	
});

$app->get('/getbirogr/{telefonno}/{plaka}', function(Request $request, Response $response, array $args){

	$telefonno = $args['telefonno'];
    $plaka = $args['plaka'];
     

	$db = new DbOperations; 

    $veriler = $db->getBirOgr($telefonno,$plaka);

    $response_data = array();

    $response_data['error'] = false; 
    $response_data['veri'] = $veriler; 

    $response->write(json_encode($response_data));

    return $response
    ->withHeader('Content-type', 'application/json')
    ->withStatus(200);
});

$app->get('/getbilgi/{okul}/{vakit}/{plaka}', function(Request $request, Response $response, array $args){

	$okul = $args['okul'];
    $vakit = $args['vakit'];
    $plaka = $args['plaka'];
     

	$db = new DbOperations; 

    $veriler = $db->getOgrBilgi($okul,$vakit,$plaka);

    $response_data = array();

    $response_data['error'] = false; 
    $response_data['veriler'] = $veriler; 

    $response->write(json_encode($response_data));

    return $response
    ->withHeader('Content-type', 'application/json')
    ->withStatus(200); 
});

$app->put('/updateogr/{telefonnum}', function(Request $request, Response $response, array $args){

    $telefonnum = $args['telefonnum'];

    if(!haveEmptyParameters(array('okul','vakit','ad','soyad','telefon'), $request, $response)){

        $request_data = $request->getParsedBody(); 
        $okul = $request_data['okul'];
        $vakit = $request_data['vakit'];
        $ad = $request_data['ad'];
        $soyad = $request_data['soyad'];
        $telefon = $request_data['telefon']; 
     

        $db = new DbOperations; 

        if($db->updateOgr($telefonnum, $okul, $vakit, $ad, $soyad, $telefon)){
            $response_data = array(); 
            $response_data['error'] = false; 
            $response_data['message'] = 'User Updated Successfully';
            //$user = $db->getUserByEmail($email);
            //$response_data['user'] = $user; 

            $response->write(json_encode($response_data));

            return $response
            ->withHeader('Content-type', 'application/json')
            ->withStatus(200);  
        
        }else{
            $response_data = array(); 
            $response_data['error'] = true; 
            $response_data['message'] = 'Please try again later';
            //$user = $db->getUserByEmail($email);
            //$response_data['user'] = $user; 

            $response->write(json_encode($response_data));

            return $response
            ->withHeader('Content-type', 'application/json')
            ->withStatus(200);  
              
        }

    }
    
    return $response
    ->withHeader('Content-type', 'application/json')
    ->withStatus(200);  

});


$app->get('/getokul/{plaka}', function(Request $request, Response $response, array $args){

	$plaka = $args['plaka'];

	$db = new DbOperations;

	$veriler = $db->getOkul($plaka);

	$response_data = array();

	$response_data['error'] = false;
	$response_data['veriler'] =$veriler;

	$response->write(json_encode($response_data));

    return $response
    ->withHeader('Content-type', 'application/json')
    ->withStatus(200); 
});

$app->get('/getvakit/{okul}', function(Request $request, Response $response, array $args){

	$okul = $args['okul'];

	$db = new DbOperations;

	$veriler = $db->getVakit($okul);

	$response_data = array();

	$response_data['error'] = false;
	$response_data['veriler'] =$veriler;

	$response->write(json_encode($response_data));

    return $response
    ->withHeader('Content-type', 'application/json')
    ->withStatus(200); 
});

$app->delete('/deleteogr/{telefon}', function(Request $request, Response $response, array $args){
    $telefon = $args['telefon'];

    $db = new DbOperations; 

    $response_data = array();

    if($db->deleteOgr($telefon)){
        $response_data['error'] = false; 
        $response_data['message'] = 'User has been deleted';    
    }else{
        $response_data['error'] = true; 
        $response_data['message'] = 'Plase try again later';
    }

    $response->write(json_encode($response_data));

    return $response
    ->withHeader('Content-type', 'application/json')
    ->withStatus(200);
});


function haveEmptyParameters($required_params, $request, $response){
    $error = false; 
    $error_params = '';
    $request_params = $request->getParsedBody(); 

    foreach($required_params as $param){
        if(!isset($request_params[$param]) || strlen($request_params[$param])<=0){
            $error = true; 
            $error_params .= $param . ', ';
        }
    }

    if($error){
        $error_detail = array();
        $error_detail['error'] = true; 
        $error_detail['message'] = 'Required parameters ' . substr($error_params, 0, -2) . ' are missing or empty';
        $response->write(json_encode($error_detail));
    }
    return $error; 
}

$app->run();