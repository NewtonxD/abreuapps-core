
function verificarCedula(){
    var respuesta = false;
    
    if($("#cedula").val()===$("#original_cedula").val()) respuesta=true;
    else{
        $.ajax({
            url:`${SERVER_IP}/infppl/vfyCedInUsr`,
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
            url:`${SERVER_IP}/usrmgr/vfyMail`,
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
            url:`${SERVER_IP}/infppl/getAllInfCed`,
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
            url:`${SERVER_IP}/usrmgr/vfyUsr`,
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
    window.location.href=`${SERVER_IP}/auth/logout`; 
}

function verificarPassword(){
    var respuesta = false;
    
    
        $.ajax({
            url:`${SERVER_IP}/usrmgr/vfyPwd`,
            type:"POST",
            async:false,
            data:{"pwd":$("#password").val()},
            success: function(res){
                respuesta=res;
            },
            error: function(xhr, status, error){
                respuesta=false;
            }
        });
        
    return respuesta;
}

function cambiarPassword(){

    post_plantilla("/main/changePwd",{});
    
}

$(function(){
    
    //desplegar antes de submitiar para los campos required
    $("#btn_guardar").on("click",function(event){
        if(!$("#inf_personal").hasClass("show"))$("#inf_personal").addClass("show");
    });
    
    $("#cedula").on("blur",function(event){
        if($(this).val()!=="")
            if(!verificarCedula()){
                $(".alert-cedula").css("display","block");
                $(this).val("");
                $(this).focus();
                return;
            }else{
                $(".alert-cedula").css("display","none");
                obtenerInfoPorCedula();
            }
        
    });
    
    $("#form-guardar").on("submit", function(event){
        event.preventDefault();
        
        if(!verificarCorreo()){
            $(".alert-correo").css("display","block");
            $("#correo").focus();
            return;
        }else $(".alert-correo").css("display","none");

        if(!verificarUsuario()){
            $(".alert-usuario").css("display","block");
            $("#username").focus();
            return;
        }else $(".alert-usuario").css("display","none");

        if(!verificarPassword()){
            $(".alert-pwd").css("display","block");
            $("#password").focus();
            return;
        }else $(".alert-pwd").css("display","none");
        
        guardar_datos();
    }); 
    
    
    $("#BtnCloseSess").on("click", cerrarSesion);
    $("#BtnResetPwd").on("click", cambiarPassword);
    
});

function guardar_datos(){
   
    //datos.push({name:"persona",value:dataPrepare("inf_personal")});
    let datosPersona=dataPrepareDiv("inf_personal");
    $.ajax({
        url:`${SERVER_IP}/infppl/save`,
        type:"POST",
        dataType: "json",
        async:false,
        data:datosPersona,
        success: function(res){
            let datosUsuario=dataPrepareDiv("inf_usuario");
            //datosUsuario+="&idPersona="+parseInt($(res).find('Integer').text(), 10);
            datosUsuario+="&idPersona="+res;
            post_plantilla("/usrmgr/save",datosUsuario);
        },
        error: function(xhr, status, error){
            if(xhr.responseText.indexOf('This session has been expired') !== -1)
                window.location.href=`${SERVER_IP}/auth/login?logout=true`;

            $("#content-page").html(xhr.responseText).fadeIn(100);
        }
    });
    
}


