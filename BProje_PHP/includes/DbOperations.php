<?php 

    class DbOperations{

        private $con; 

        function __construct(){
            require_once dirname(__FILE__) . '/DbConnect.php';
            $db = new DbConnect; 
            $this->con = $db->connect(); 
        }

        public function createUser($lat, $lng, $okul, $vakit, $ad, $soyad, $telefon, $plaka){
           
            $stmt = $this->con->prepare("INSERT INTO yeni_tablo (lat, lng, okul, vakit, ad, soyad, telefon, plaka) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            $stmt->bind_param("ddssssss", $lat, $lng, $okul, $vakit, $ad, $soyad, $telefon, $plaka);
            if($stmt->execute()){
                 return USER_CREATED; 
            }else{
                return USER_FAILURE;
            }
           
           return USER_EXISTS; 
        }

        public function createHesap($plaka,$sifre){
           
            $stmt = $this->con->prepare("INSERT INTO hesap (plaka,sifre) VALUES (?, ?)");
            $stmt->bind_param("ss", $plaka,$sifre);
            if($stmt->execute()){
                 return USER_CREATED; 
            }else{
                return USER_FAILURE;
            }
           
           return USER_EXISTS; 
        }

        public function getAllVeri($plaka){
            $stmt = $this->con->prepare("SELECT lat, lng, okul, vakit, ad, soyad, telefon FROM yeni_tablo WHERE plaka = ?;");
            $stmt->bind_param("s",$plaka);
            $stmt->execute(); 
            $stmt->bind_result($lat, $lng, $okul, $vakit, $ad, $soyad, $telefon);
            $veriler = array(); 
            while($stmt->fetch()){ 
                $veri = array(); 
                $veri['lat'] = $lat; 
                $veri['lng']=$lng; 
                $veri['Okul'] = $okul; 
                $veri['Vakit'] = $vakit;
                $veri['Ad'] = $ad;
                $veri['Soyad'] = $soyad;
                $veri['TelefonNo'] = $telefon;
                array_push($veriler, $veri);
            }             
            return $veriler; 
        }

        public function getOgrBilgi($okul,$vakit,$plaka)
        {
            $stmt = $this->con->prepare("SELECT lat, lng, okul, vakit, ad, soyad, telefon FROM yeni_tablo WHERE okul = ? AND vakit = ? AND plaka = ?;");
            $stmt->bind_param("sss",$okul,$vakit,$plaka);
            $stmt->execute();
            $stmt->bind_result($lat, $lng, $okul_sonuc, $vakit_sonuc, $ad, $soyad, $telefon);
            $veriler = array(); 
            while($stmt->fetch()){ 
                $veri = array(); 
                $veri['lat'] = $lat; 
                $veri['lng']=$lng; 
                $veri['Okul'] = $okul_sonuc; 
                $veri['Vakit'] = $vakit_sonuc;
                $veri['Ad'] = $ad;
                $veri['Soyad'] = $soyad;
                $veri['TelefonNo'] = $telefon;
                array_push($veriler, $veri);
            }             
            return $veriler;
        }
        public function getBirOgr($telefonno,$plaka)
        {
            $stmt = $this->con->prepare("SELECT lat, lng, okul, vakit, ad, soyad, telefon FROM yeni_tablo WHERE telefon = ? AND plaka = ?;");
            $stmt->bind_param("ss",$telefonno,$plaka);
            $stmt->execute();
            $stmt->bind_result($lat, $lng, $okul_sonuc, $vakit_sonuc, $ad, $soyad, $telefon);
            $veriler = array();
            $stmt->fetch();
            $veri = array(); 
            $veri['lat'] = $lat; 
            $veri['lng']=$lng; 
            $veri['Okul'] = $okul_sonuc; 
            $veri['Vakit'] = $vakit_sonuc;
            $veri['Ad'] = $ad;
            $veri['Soyad'] = $soyad;
            $veri['TelefonNo'] = $telefon;
             
            return $veri;
        }

        public function getOkul($plaka)
        {
            $stmt = $this->con->prepare("SELECT DISTINCT okul FROM yeni_tablo WHERE plaka = ?;");
            $stmt->bind_param("s",$plaka);
            $stmt->execute();
            $stmt->bind_result($okul);
            $veriler = array();
            while($stmt->fetch())
            {
                $veri = array();
                $veri['Okul'] = $okul;
                array_push($veriler, $veri);
            }
            return $veriler;
        }

        public function getVakit($okul)
        {
            $stmt = $this->con->prepare("SELECT DISTINCT vakit FROM yeni_tablo WHERE okul = ?;");
            $stmt->bind_param("s",$okul);
            $stmt->execute();
            $stmt->bind_result($vakit);
            $veriler = array();
            while($stmt->fetch())
            {
                $veri = array();
                $veri['Vakit'] = $vakit;
                array_push($veriler, $veri);
            }
            return $veriler;
        }

        public function updateOgr($telefonnum,$okul, $vakit, $ad, $soyad, $telefon){
            $stmt = $this->con->prepare("UPDATE yeni_tablo SET okul = ?, vakit = ?, ad = ?, soyad = ?, telefon = ? WHERE telefon = ?");
            $stmt->bind_param("ssssss", $okul, $vakit, $ad, $soyad, $telefon, $telefonnum);
            if($stmt->execute())
                return true; 
            return false; 
        }

        public function deleteOgr($telefon){
            $stmt = $this->con->prepare("DELETE FROM yeni_tablo WHERE telefon = ?");
            $stmt->bind_param("s", $telefon);
            if($stmt->execute())
                return true; 
            return false; 
        }

        public function isPlaka($plaka)
        {
            $stmt = $this->con->prepare("SELECT plaka FROM hesap WHERE plaka = ?");
            $stmt->bind_param("s", $plaka);
            if($stmt->execute())
                return true; 
            return false; 
        }

        public function getSifre($plaka)
        {
            $stmt = $this->con->prepare("SELECT sifre FROM hesap WHERE plaka = ?");
            $stmt->bind_param("s", $plaka);
            $stmt->execute();
            $stmt->bind_result($sifre);
            $stmt->fetch();
            return $sifre;
            
        }

        public function userLogin($plaka, $sifre){
            if($this->isPlaka($plaka)){
                $hashed_password = $this->getSifre($plaka); 
                if(password_verify($sifre, $hashed_password)){
                    return USER_AUTHENTICATED;
                }else{
                    return USER_PASSWORD_DO_NOT_MATCH; 
                }
            }else{
                return USER_NOT_FOUND; 
            }
        }

    }