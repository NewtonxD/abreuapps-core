
function pdaGetLoc(IdParada){        
    $.ajax({
       url:`${SERVER_IP}/pda/getLoc`,
       type:"POST",
       async:false,
       data:{idParada:IdParada},
       success: function(res){
           window.data_loc=res;
       },
       error: function(xhr, status, error){

       }
   });
}

$(document).on("click","tbody tr",function(){
        
    let IdParada=$(this).find('th').html();

    if(IdParada===undefined || IdParada===null)
        return;

    closeEventSource();
    pdaGetLoc(IdParada);
    
    var data={idParada:IdParada};
    
    post_plantilla("/pda/update",data);

});