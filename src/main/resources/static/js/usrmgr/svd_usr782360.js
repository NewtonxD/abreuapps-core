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

$(function(){
    
    //desplegar antes de submitiar para los campos required
    $("#btn_guardar").on("click",function(event){
        if(!$("#inf_personal").hasClass("show"))$("#inf_personal").addClass("show");
    });
    
    $("#form-guardar").on("submit", function(event){
        event.preventDefault();
        guardar_datos();
    }); 
    
});

function guardar_datos(){
    $("#content-page").css("overflow-y","hidden");
    var fadeout=$("#content-page").hide().delay(150).promise();
   
    //datos.push({name:"persona",value:dataPrepare("inf_personal")});
    let datosPersona=dataPrepare("inf_personal");
    var idPersona=0;
    $.ajax({
        url:'/usrmgr/infppl/save',
        type:"POST",
        async:false,
        data:datosPersona,
        success: function(res){
            idPersona=res;
        },
        error: function(xhr, status, error){
            
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

            if(res.indexOf('Login') !== -1)
                    window.location.href="/auth/login?logout=true";

            fadeout.then(function(){
                $("#content-page").html(res).fadeIn(200).promise().then(function(){
                    $("#content-page").css("overflow-y","hidden");
                }); 
            });
        },
        error: function(xhr, status, error){
            fadeout.then(function(){
                var fadein=$("#content-page").html(xhr.responseText).fadeIn(200).promise();

                fadein.then(function(){
                    $("#content-page").css("overflow-y","hidden");
                }); 
            });
        }
    });
    
}


