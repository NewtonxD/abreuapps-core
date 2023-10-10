/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/javascript.js to edit this template
 */


$(function (){
    
    $("tbody tr").click(function(){
        
        let idUsuario=$(this).find('th').html();
        
        if(idUsuario===undefined || idUsuario===null)
            return;
        
        $("#content-page").css("overflow-y","hidden");
        var fadeout=$("#content-page").hide().delay(150).promise();
        
        closeEventSource();
        
        $.ajax({
            url:'/usrmgr/update',
            type:"POST",
            async:true,
            data:{idUsuario:idUsuario},
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
        
    });
    
});
