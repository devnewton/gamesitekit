<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/> 
    <title>${(manifest.game.name)!}</title>
    <#include "css.ftl">
</head>
<body>

<#include "header.ftl">

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
<#include "scripts.ftl">
</body>
</html> 