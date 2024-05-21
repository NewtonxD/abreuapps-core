
function rtaGetLoc(idRuta){        
    $.ajax({
       url:`${SERVER_IP}/rta/getLoc`,
       type:"POST",
       async:false,
       data:{idRuta:idRuta},
       success: function(res){
           window.data_loc=res;
       },
       error: function(xhr, status, error){

       }
   });
}


$(document).on("click","tbody tr",function(){
        
    let idRuta=$(this).find('th').html();

    if(idRuta===undefined || idRuta===null)
        return;

    closeEventSource();

    rtaGetLoc(idRuta);
    
    var data={idRuta:idRuta};
    
    post_plantilla("/rta/update",data);

});