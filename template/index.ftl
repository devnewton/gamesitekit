<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/> 
    <title>${(manifest.name)!}</title>
    <#include "css.ftl">
</head>
<body>
<#include "header.ftl">
<div id="content">
    <main id="content-main">
        <div class="trailer">
            <#include "trailer.ftl" >
        </div>
        <section id="presentation">
            <#include "presentation.ftl" >
        </section>
        <section id="downloads">
            <#include "downloads.ftl" >
        </section>
        <article id="mods">
            <#include "mods.ftl" >
        </article>
    </main>
    <aside id="content-aside">
        <div class="cover">
            <img alt="" src="../media/cover.png">
        </div>
        <section id="factsheet">
            <#include "factsheet.ftl" >
        </section>
        <section id="screenshots">
            <#include "screenshots.ftl" >
        </section>
        <section id="features">
            <#include "features.ftl" >
        </section>
        <section id="links">
            <#include "links.ftl" >
        </section>
    </aside>
</div>
<#include "scripts.ftl">
<#include "footer.ftl">
</body>
</html> 