<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/> 
    <title>${(manifest.name)!} - Support</title>
    <#include "css.ftl">
</head>
<body>

<#include "header.ftl">

<div id="content">
    <main id="content-main">
        <section id="faq">
            <#include "faq.ftl" >
        </section>
    </main>
    <aside id="content-aside">
        <section id="cover">
            <img alt="" src="../media/cover.png">
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