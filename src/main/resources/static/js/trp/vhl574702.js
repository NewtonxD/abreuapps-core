
$(document).on("click","tbody tr",function(){
        
    let Placa=$(this).find('th').html();

    if(Placa===undefined || Placa===null)
        return;

    closeEventSource();

     $.ajax({
        url:`${SERVER_IP}/vhl/getLastLoc`,
        type:"POST",
        async:false,
        data:{placa:Placa},
        success: function(res){
            window.data_lastloc=res;
        },
        error: function(xhr, status, error){

        }
    });
    
    var data={placa:Placa};
    post_plantilla("/vhl/update",data);

});
