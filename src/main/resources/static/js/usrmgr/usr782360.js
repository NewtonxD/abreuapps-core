/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/javascript.js to edit this template
 */
    

$(document).on("click","tr",function(){

    let idUsuario=$(this).find('th').html();

    if(idUsuario===undefined || idUsuario===null)
        return;

    $("#content-page").css("overflow-y","hidden");
    var fadeout=$("#content-page").hide().delay(100).promise();

    closeEventSource();

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

