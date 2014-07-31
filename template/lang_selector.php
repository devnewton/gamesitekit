<?php
require_once "HTTP.php";

$supportedLanguages = array(
    'en' => true,
    'fr' => true,
);
$http = new HTTP();
$negotiatedLanguages = $http->negotiateLanguage($supportedLanguages);
switch ($negotiatedLanguages) {
    case 'fr' :
        $http->redirect("fr/index.html");
    break;
    default :
        $http->redirect("en/index.html");
    break;
}
?>