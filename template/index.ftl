<!DOCTYPE html>
<html>
<head>
    <title>${(manifest.game.name)!}</title>
    <link rel="stylesheet" href="style/style.css">
</head>
<body>

<nav id="menu">
    <a href="index.html">Home</a>
</nav>

<header id="header">
    <div id="title">${(manifest.game.name)!}</div>
    <div id="catchphrase">${(manifest.game.catchphrase)!}</div>
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
        </section>
    </aside>
</div>

</body>
</html> 