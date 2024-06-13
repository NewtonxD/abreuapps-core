/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/javascript.js to edit this template
 */

function usuarioCopyAccess(){
    if($("#idUsuarioCopy").val()!==""){
        let data={ "idUsuario":$("#idUsuarioCopy").val() };
        $.ajax({
            url:`${SERVER_IP}/acc/get-access`,
            type:"POST",
            async:false,
            data:data,
            success: function(res){
                if(res.length>0){
                    $(".container").html("");
                    var forest = constructForest(res);
                    $(".container").prepend(forest);

                    $(".special-access").html("");
                    var specialAccess = constructSpecialAccess(res);
                    $('.special-access').append(specialAccess);

                    $("#idUsuarioCopy").css("border-color","green");
                }else{
                    $("#idUsuarioCopy").css("border-color","red");
                }
            },
            error: function(xhr, status, error){

                $("#idUsuarioCopy").css("border-color","red");

                if(xhr.responseText.indexOf('This session has been expired') !== -1)
                    window.location.href=`${SERVER_IP}/auth/login?logout=true`;  

            }
        });
    }
}

function saveUsuarioAccess(){
    var data=dataPrepare("form-guardar");
    post_plantilla("/acc/save-acc",data);
}

$(function(){
    
    $(".atras").on("click",function(){
        let idUsuario=$("#idUsuario").val();
        var data={idUsuario:idUsuario};
        post_plantilla("/usrmgr/update",data);
        
    });
    
});

