
$(document).on("click","tbody tr",function(){
        
    let Placa=$(this).find('th').html();

    if(Placa===undefined || Placa===null)
        return;

    closeEventSource();
    
    var data={placa:Placa};
    post_plantilla("/vhl/update",data);

});
