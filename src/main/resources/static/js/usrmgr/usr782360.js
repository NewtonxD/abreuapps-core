
$("#table").on("click","tbody tr",function(){

    let idUsuario=$(this).find('th').html();

    if(idUsuario===undefined || idUsuario===null)
        return;

    closeEventSource();

    var data={idUsuario:idUsuario};
    post_plantilla("/usrmgr/update",data);

});

