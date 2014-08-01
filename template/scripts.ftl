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