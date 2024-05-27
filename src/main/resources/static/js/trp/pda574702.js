$(document).on("click","tbody tr",function(){
        
    let IdParada=$(this).find('th').html();

    if(IdParada===undefined || IdParada===null)
        return;

    closeEventSource();
    
    var data={idParada:IdParada};
    
    post_plantilla("/pda/update",data);

});