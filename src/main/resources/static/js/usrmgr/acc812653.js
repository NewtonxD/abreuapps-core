/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/javascript.js to edit this template
 */

function dataPrepare(idForm){
    let d=$("#"+idForm+" :input").serializeArray();
    $("#"+idForm+" input[type=checkbox]").each(function() {
        if (!$(this)[0].checked) {
            d.push({name:$(this).attr("name"),value:"off"});
        }
    });
    return d;
}

function usuarioCopyAccess(){
    if($("#idUsuarioCopy").val()!==""){
        let data={ "idUsuario":$("#idUsuarioCopy").val() };
        $.ajax({
            url:`${SERVER_IP}/usrmgr/get-access`,
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
    
    $("#content-page").css("overflow-y","hidden");
    var fadeout=$("#content-page").hide().delay(100).promise();

    $.ajax({
        url:`${SERVER_IP}/usrmgr/save-acc`,
        type:"POST",
        async:true,
        data:data,
        success: function(res){

            if(res.indexOf('Login') !== -1 || res.indexOf('This session has been expired') !== -1)
                window.location.href=`${SERVER_IP}/auth/login?logout=true`;

            fadeout.then(function(){
                $("#content-page").html(res).fadeIn(100).promise().then(function(){
                    $("#content-page").css("overflow-y","hidden");
                }); 
            });
        },
        error: function(xhr, status, error){
            // Maneja cualquier error que ocurra durante la llamada    

            if(xhr.responseText.indexOf('This session has been expired') !== -1)
                window.location.href=`${SERVER_IP}/auth/login?logout=true`;  

            fadeout.then(function(){
                var fadein=$("#content-page").html(xhr.responseText).fadeIn(100).promise();

                fadein.then(function(){
                    $("#content-page").css("overflow-y","hidden");
                }); 
            });
        }
    });    
}

$(function(){
    
    $(".atras").on("click",function(){
        //idUsuario
        let idUsuario=$("#idUsuario").val();
        
        $("#content-page").css("overflow-y","hidden");
        var fadeout=$("#content-page").hide().delay(100).promise();
        
        $.ajax({
            url:`${SERVER_IP}/usrmgr/update`,
            type:"POST",
            async:true,
            data:{idUsuario:idUsuario},
            success: function(res){
                        
                if(res.indexOf('Login') !== -1 || res.indexOf('This session has been expired') !== -1)
                    window.location.href=`${SERVER_IP}/auth/login?logout=true`;
            
                fadeout.then(function(){
                    $("#content-page").html(res).fadeIn(100).promise().then(function(){
                        $("#content-page").css("overflow-y","hidden");
                    }); 
                });
            },
            error: function(xhr, status, error){
                // Maneja cualquier error que ocurra durante la llamada    
                
                if(xhr.responseText.indexOf('This session has been expired') !== -1)
                    window.location.href=`${SERVER_IP}/auth/login?logout=true`;  
                
                fadeout.then(function(){
                    var fadein=$("#content-page").html(xhr.responseText).fadeIn(100).promise();
                    
                    fadein.then(function(){
                        $("#content-page").css("overflow-y","hidden");
                    }); 
                });
            }
        });    
    });
    
});

