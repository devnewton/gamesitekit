<table>
<#list downloads as download >
    <tr>
    <#if download.url?ends_with(".exe") >
        <td><img src="../media/logos/os/logo_microsoft.svg" height="32" width="32">Windows XP / Vista / 7</td>
        <td><a href="${download.url}">T&eacute;l&eacute;charger l'installeur Windows</a></td>
    <#elseif download.url?ends_with(".deb") >
        <td><img src="../media/logos/os/logo_debian.svg" height="32" width="32">Debian <strong>stable</strong><img src="../media/logos/os/logo_ubuntu.svg" height="32" width="32"> Ubuntu <strong>12.04 LTS</strong></td>
        <td><a href="${download.url}">T&eacute;l&eacute;charger le paquet deb</a></td>
    <#elseif download.url?ends_with(".i686.rpm") >
        <td><img src="../media/logos/os/logo_redhat.svg" height="32" width="32">Red Hat <img src="../media/logos/os/logo_fedora.svg" height="32" width="32">Fedora <img src="../media/logos/os/logo_opensuse.svg" height="32" width="32">Suse </td>
        <td><a href="${download.url}">T&eacute;l&eacute;charger le paquet rpm 32 bits</a></td>
    <#elseif download.url?ends_with(".x86_64.rpm") >
        <td><img src="../media/logos/os/logo_redhat.svg" height="32" width="32">Red Hat <img src="../media/logos/os/logo_fedora.svg" height="32" width="32">Fedora <img src="../media/logos/os/logo_opensuse.svg" height="32" width="32">Suse </td>
        <td><a href="${download.url}">T&eacute;l&eacute;charger le paquet rpm 64 bits</a></td>
    <#elseif download.url?ends_with(".apk") >
        <td><img src="../media/logos/os/logo_android.svg" height="32" width="32"> Android</td>
        <td><a href="${download.url}">T&eacute;l&eacute;charger le paquet apk</a></td>
    <#elseif download.url?ends_with(".jar") >
        <td><img src="../media/logos/os/logo_linux.svg" height="32" width="32"> Linux <img src="../media/logos/os/logo_apple.svg" height="32" width="32"> MacosX and other OS</td>
        <td><a href="${download.url}">T&eacute;l&eacute;charger l'installeur multiplateforme</a></td>
    <#else>
        <td colspan="2"><a href="${download.url}">${download.fileName}</a></td>
    </#if>
    </tr>
</#list>
</table>