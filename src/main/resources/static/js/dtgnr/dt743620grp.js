/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/javascript.js to edit this template
 */


$(function (){
    
    $("tbody tr").click(function(){
        
        let idGrupo=$(this).find('th').html();
        
        if(idGrupo===undefined || idGrupo===null)
            return;
        
        $("#content-page").css("overflow-y","hidden");
        var fadeout=$("#content-page").hide().delay(150).promise();
        
        closeEventSource();
        
        $.ajax({
            url:'/dtgrp/update',
            type:"POST",
            async:true,
            data:{idGrupo:idGrupo},
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