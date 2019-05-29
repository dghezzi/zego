<?php

$redirect = $actual_link = "http://$_SERVER[HTTP_HOST]";
$headers = getallheaders();

if(isset($headers['zegotoken']) || isset($headers['Zegotoken'])){
  $token = isset($headers['zegotoken']) ? $headers['zegotoken'] : $headers['Zegotoken'];
  header("location: $redirect?zegotoken=$token");
  exit();
}else{
  header("location: $redirect");
  exit();
}
?>
