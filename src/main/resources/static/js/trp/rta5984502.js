$("#table").on("click","tbody tr",function(){
        
    let idRuta=$(this).find('th').html();

    if(idRuta===undefined || idRuta===null)
        return;

    closeEventSource();
    
    var data={idRuta:idRuta};
    
    post_plantilla("/rta/update",data);

});