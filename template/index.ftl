<!DOCTYPE html>
<html>
<head>
    <title>${(manifest.game.name)!}</title>
</head>
<body>

<div id="menu">
  <ul>
    <li>Home</li>
  </ul>
</div>

<div id="trailer">
</div>

<div id="presentation">
    <#include "presentation.ftl" >
</div>

<div id="buy">
</div>

<div id="downloads">
    <ul>
    <#list manifest.game.download as download >
        <li><a href="${download.url}">${download.platform}</a></li>
    </#list>
    </ul>
</div>

<div id="cover">
</div>

<div id="factsheet">
    <ul>
        <li>Genre: ${manifest.game.genre}</li>
        <li>License: ${manifest.game.license}</li>
        <li>Platforms: ${manifest.game.platforms}</li>
    </ul>
</div>

<div id="screenshots">
</div>

<div id="features">
</div>

</body>
</html> 