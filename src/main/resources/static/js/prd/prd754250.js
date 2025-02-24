$("#table").on("click","tbody tr",function(){
        
    let IdPublicidad=$(this).find('th').html();

    if(IdPublicidad===undefined || IdPublicidad===null)
        return;

    closeEventSource();
    
    let data={idProducto:IdPublicidad};
    
    post_plantilla("/prd/update",data);

});
