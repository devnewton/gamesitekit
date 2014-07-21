<!DOCTYPE html>
<html>
<head>
    <title>${(game.name)!}</title>
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
</div>

<div id="buy">
</div>

<div id="downloads">
    <ul>
    <#list game.downloads as download >
        <li><a href="${download.url}">${download.platform}</a></li>
    </#list>
    </ul>
</div>

<div id="cover">
</div>

<div id="factsheet">
    <ul>
        <#if game.genre??><li>Genre: ${game.genre}</li></#if>
        <#if game.licenses??><li>License<#if game.licenses?size &gt; 0 >s</#if>: <#list game.licenses as license >${license}<#if license_has_next>, </#if></#list></li></#if>
    </ul>
</div>

<div id="screenshots">
</div>

<div id="features">
</div>

</body>
</html> 