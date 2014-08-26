<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/> 
    <title>Giftware?</title>
    <#include "css.ftl">
</head>
<body>
<#include "header.ftl">
<div id="content">
    <main id="content-main">
        <h2>Gifware?</h2>
        <p>Newton Adventure is a <em>giftware</em>. If you make a gift to his author, he may feel obliged to send you a present, like the <a href="index.html#mod-pack-1">mod pack 1</a>.</p>
        <p>The preferred way to send a gift is to pick an item from the wish list and use <a href="http://www.gog.com/support/website_help/gift_certificates">gog gift process</a> with my mail address: <em>${manifest.mail}</em></p>
        <h2>Wish list</h2>
        <#include "wishlist.ftl">
    </main>
</div>
<#include "scripts.ftl">
<#include "footer.ftl">
</body>
</html> 