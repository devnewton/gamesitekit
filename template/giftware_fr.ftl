<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/> 
    <title>Cadeaugiciel?</title>
    <#include "css.ftl">
</head>
<body>
<#include "header.ftl">
<div id="content">
    <main id="content-main">
        <h2>Cadeaugiciel?</h2>
        <p>Newton Adventure est un <em>cadeaugiciel</em>. Faites un cadeau à son auteur et il se sentira sans doute obligé de vous faire un cadeau en retour, par exemple le <a href="index.html#mod-pack-1">mod pack 1</a>.</p>
        <p>Le meilleur moyen d'envoyer un cadeau est de piocher dans ma liste d'envies et d'utiliser <a href="http://www.gog.com/support/website_help/gift_certificates">la procédure de gog</a> avec mon adresse mail: <em>${manifest.mail}</em></p>
        <h2>Liste d'envies</h2>
        <#include "wishlist.ftl">
    </main>
</div>
<#include "scripts.ftl">
<#include "footer.ftl">
</body>
</html> 