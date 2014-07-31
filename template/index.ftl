<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/> 
    <title>${(manifest.game.name)!}</title>
    <link rel="stylesheet" href="../style/style.css">
    <link rel="stylesheet" href="../style/colorbox.css">
</head>
<body>

<nav id="menu">
    <#include "menu.ftl" >
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
        <section id="links">
            <#include "links.ftl" >
        </section>
    </main>
    <aside id="content-aside">
        <section id="cover">
            <img src="../media/cover.png">
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

    <script src="../scripts/jquery-2.1.1.js"></script>
    <script src="../scripts/jquery.colorbox.js"></script>
    <script type="text/javascript">
    $(document).ready(function(){
        $('#screenshot-thumbnails > a').colorbox({
            rel:'gal',
            maxWidth:"90%",
            maxHeight:"90%",
            current: "{current} / {total}" 
        });
    });
    </script>
</body>
</html> 