
$(function(){
    $("#form-guardar").on("submit", function(event){
        event.preventDefault();
        guardar_datos();
    });
    
    $("#marca").on("change", function(event){
        event.preventDefault();
        getModelos();
    });
    
});

// 
function getModelos(){
    if($("#marca").val()!==""){
        let data={ "Marca":$("#marca").val() };
        $.ajax({
            url:`${SERVER_IP}/vhl/get-modelos`,
            type:"POST",
            async:false,
            data:data,
            success: function(res){
                
                $("#marca").css("border-color","green");
                
                $("#modelo").html("");
                $("#modelo").append('<option value="0" disabled>--Seleccione--</option>');
                if(res.length>0){
                    res.forEach(function(i) {
                        if(i["activo"]) $("#modelo").append(`<option value='${i["dato"]}' >${i["dato"]}</option>`); 
                    });
                }
            },
            error: function(xhr, status, error){

                $("#marca").css("border-color","red");

                if(xhr.responseText.indexOf('This session has been expired') !== -1)
                    window.location.href=`${SERVER_IP}/auth/login?logout=true`;  

            }
        });
    }
}

function guardar_datos(){
    let data=dataPrepare("form-guardar");
    post_plantilla("/vhl/save",data);
}