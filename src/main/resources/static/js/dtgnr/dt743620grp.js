
$(document).on("click","tbody tr",function(){
        
    let idGrupo=$(this).find('th').html();

    if(idGrupo===undefined || idGrupo===null)
        return;

    closeEventSource();
    
    var data={idGrupo:idGrupo};
    
    post_plantilla("/dtgrp/update",data);

});