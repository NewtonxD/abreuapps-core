/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/javascript.js to edit this template
 */

function dataPrepare(idForm){
    let d=$("#"+idForm+" :input").serializeArray();
    $("#"+idForm+" input[type=checkbox]").each(function() {
        if (!$(this)[0].checked) {
            d.push({name:$(this).attr("name"),value:"false"});
        }
    });
    return d;
}

function dataCustomPrepare(idForm){
    var formData = {};
    $('#'+idForm).find('input').each(function() {
        formData[$(this).attr('name')] = $(this).val();
    });
    /*    
    let d=$("#"+idForm+" :input").serializeArray();
    $("#"+idForm+" input[type=checkbox]").each(function() {
        if (!$(this)[0].checked) {
            d.push({name:$(this).attr("name"),value:"false"});
        }
    });*/
    return formData;
}

function verificarCedula(){
    var respuesta = false;
    
    if($("#cedula").val()===$("#original_cedula").val()) respuesta=true;
    else{
        $.ajax({
            url:'/infppl/vfyCedInUsr',
            type:"POST",
            async:false,
            data:{"cedula":$("#cedula").val()},
            success: function(res){
                respuesta=res;
            },
            error: function(xhr, status, error){
                respuesta=false;
            }
        });
    }
    return respuesta;
}

function verificarCorreo(){
    var respuesta = false;
    
    if($("#correo").val()===$("#original_correo").val()) respuesta=true;
    else{
        $.ajax({
            url:'/usrmgr/vfyMail',
            type:"POST",
            async:false,
            data:{"correo":$("#correo").val()},
            success: function(res){
                respuesta=res;
            },
            error: function(xhr, status, error){
                respuesta=false;
            }
        });
    }
    return respuesta;
}

function obtenerInfoPorCedula(){
    if($("#original_cedula").val!==$("#cedula").val())
    $.ajax({
            url:'/infppl/getAllInfCed',
            type:"POST",
            async:true,
            data:{"cedula":$("#cedula").val(),update:$("#update").val()},
            success: function(res){
                
                $("#info-dinamica-personal").html(res);
                $("#original_cedula").val($("#cedula").val());
            }
    });
}

function verificarUsuario(){
    var respuesta = false;
    
    if($("#username").val()===$("#original_usuario").val()) respuesta=true;
    else{
        $.ajax({
            url:'/usrmgr/vfyUsr',
            type:"POST",
            async:false,
            data:{"username":$("#username").val()},
            success: function(res){
                respuesta=res;
            },
            error: function(xhr, status, error){
                respuesta=false;
            }
        });
    }
    return respuesta;
}

function cerrarSesion(){
    let data={ "usuario":$("#original_usuario").val() };
    $("#content-page").css("overflow-y","hidden");
    var fadeout=$("#content-page").hide().delay(150).promise();
    
    $.ajax({
        url:'/usrmgr/closeUsrSess',
        type:"POST",
        async:false,
        data:data,
        success: function(res){

            if(res.indexOf('Login') !== -1)
                    window.location.href="/auth/login?logout=true";

            fadeout.then(function(){
                $("#content-page").html(res).fadeIn(200).promise().then(function(){
                    $("#content-page").css("overflow-y","hidden");
                }); 
            });
        },
        error: function(xhr, status, error){
            // Maneja cualquier error que ocurra durante la llamada    
                
            if(xhr.responseText.indexOf('This session has been expired') !== -1)
                window.location.href="/auth/login?logout=true";  
                
            fadeout.then(function(){
                var fadein=$("#content-page").html(xhr.responseText).fadeIn(200).promise();

                fadein.then(function(){
                    $("#content-page").css("overflow-y","hidden");
                }); 
            });
        }
    });
}

function cambiarPassword(){
    let data={ "usuario":$("#original_usuario").val() };
    $("#content-page").css("overflow-y","hidden");
    var fadeout=$("#content-page").hide().delay(150).promise();
    
    $.ajax({
        url:'/usrmgr/resetPwd',
        type:"POST",
        async:false,
        data:data,
        success: function(res){

            if(res.indexOf('Login') !== -1)
                    window.location.href="/auth/login?logout=true";

            fadeout.then(function(){
                $("#content-page").html(res).fadeIn(200).promise().then(function(){
                    $("#content-page").css("overflow-y","hidden");
                }); 
            });
        },
        error: function(xhr, status, error){
            // Maneja cualquier error que ocurra durante la llamada    
                
            if(xhr.responseText.indexOf('This session has been expired') !== -1)
                window.location.href="/auth/login?logout=true";  
                
            fadeout.then(function(){
                var fadein=$("#content-page").html(xhr.responseText).fadeIn(200).promise();

                fadein.then(function(){
                    $("#content-page").css("overflow-y","hidden");
                }); 
            });
        }
    });
}

$(function(){
    
    //desplegar antes de submitiar para los campos required
    $("#btn_guardar").on("click",function(event){
        if(!$("#inf_personal").hasClass("show"))$("#inf_personal").addClass("show");
    });
    
    $("#cedula").on("blur",function(event){
        if($(this).val()!=="")
            if(!verificarCedula()){
                alert("La cedula ya se encuentra registrada en un usuario. Verifique los datos y vuelva a intentarlo.");
                $(this).val("");
                $(this).focus();
                return;
            }else{
                obtenerInfoPorCedula();
            }
        
    });
    
    $("#form-guardar").on("submit", function(event){
        event.preventDefault();
        
        if($("#id")===undefined){

            if(!verificarCorreo()){
                alert("El correo ya se encuentra en el sistema. Verifique los datos y vuelva a intentarlo.");
                $("#correo").focus();
                return;
            }

            if(!verificarUsuario()){
                alert("El usuario ya se encuentra en uso. Verifique los datos y vuelva a intentarlo.");
                $("#username").focus();
                return;
            }
            
        }
        
        guardar_datos();
    }); 
    
    
    $("#BtnCloseSess").on("click", cerrarSesion);
    $("#BtnResetPwd").on("click", cambiarPassword);
    
});

function guardar_datos(){
    $("#content-page").css("overflow-y","hidden");
    var fadeout=$("#content-page").hide().delay(150).promise();
   
    //datos.push({name:"persona",value:dataPrepare("inf_personal")});
    let datosPersona=dataPrepare("inf_personal");
    var idPersona=0;
    $.ajax({
        url:'/infppl/save',
        type:"POST",
        async:false,
        data:datosPersona,
        success: function(res){
            idPersona=res;
        },
        error: function(xhr, status, error){
            idPersona=0;
        }
    });
    
    
    let datosUsuario=dataPrepare("inf_usuario");
    datosUsuario.push({name:"idPersona",value:idPersona});
    
    $.ajax({
        url:'/usrmgr/save',
        type:"POST",
        async:true,
        data:datosUsuario,
        success: function(res){

            if(res.indexOf('Login') !== -1 || res.indexOf('This session has been expired') !== -1)
                    window.location.href="/auth/login?logout=true";

            fadeout.then(function(){
                $("#content-page").html(res).fadeIn(200).promise().then(function(){
                    $("#content-page").css("overflow-y","hidden");
                }); 
            });
        },
        error: function(xhr, status, error){
            // Maneja cualquier error que ocurra durante la llamada    
                
            if(xhr.responseText.indexOf('This session has been expired') !== -1)
                window.location.href="/auth/login?logout=true";  
                
                
            fadeout.then(function(){
                var fadein=$("#content-page").html(xhr.responseText).fadeIn(200).promise();

                fadein.then(function(){
                    $("#content-page").css("overflow-y","hidden");
                }); 
            });
        }
    });
    
}


