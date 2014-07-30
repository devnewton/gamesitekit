<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/> 
    <title>${(manifest.game.name)!}</title>
    <link rel="stylesheet" href="style/style.css">
    <link rel="stylesheet" href="style/gallerie.css"/>
    <script src="scripts/jquery-2.1.1.js"></script>
    <script src="scripts/jquery.gallerie.js"></script>
    <script type="text/javascript">
    $(document).ready(function(){
        $('#screenshot-thumbnails').gallerie();
    });
    </script>
</head>
<body>

<nav id="menu">
    <a href="index.html">Home</a>
</nav>

<header id="header">
    <h1 id="title">${(manifest.game.name)!}</h1>
    <em id="catchphrase">${(manifest.game.catchphrase)!}</em>
</header>

<div id="content">
    <main id="content-main">
        <section id="trailer">
            <#include "trailer.ftl" >
        </section>
        <section id="presentation">
            <#include "presentation.ftl" >
        </section>
        <section id="buy">
        </section>
        <section id="downloads">
            <#include "downloads.ftl" >
        </section>
    </main>
    <aside id="content-aside">
        <section id="cover">
            <img src="media/cover.png">
        </section>
        <section id="factsheet">
            <#include "factsheet.ftl" >
        </section>
        <section id="screenshots">
            <#include "screenshots.ftl" >
        </section>
        <section id="features">
            <#include "features.ftl" >
        </section>
    </aside>
</div>

</body>
</html> 