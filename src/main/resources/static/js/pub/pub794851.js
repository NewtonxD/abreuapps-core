$(document).on("click","tbody tr",function(){
        
    let IdPublicidad=$(this).find('th').html();

    if(IdPublicidad===undefined || IdPublicidad===null)
        return;

    closeEventSource();
    
    let data={idPublicidad:IdPublicidad};
    
    post_plantilla("/pub/update",data);

});
