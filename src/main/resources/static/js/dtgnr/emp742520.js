$(document).on("click","tbody tr",function(){
        
    let idDato=$(this).find('th').html();

    if(idDato===undefined || idDato===null)
        return;
    
    closeEventSource();
    
    var data={idDato:idDato};
    
    post_plantilla("/dtgnr/emp/update",data);

});
