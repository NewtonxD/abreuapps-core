/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/javascript.js to edit this template
 */

function rtaGetLoc(idRuta){        
    $.ajax({
       url:'/rta/getLoc',
       type:"POST",
       async:false,
       data:{idRuta:idRuta},
       success: function(res){
           window.data_loc=res;
       },
       error: function(xhr, status, error){

       }
   });
}

$(function (){
    
    $("tbody tr").click(function(){
        
        let idRuta=$(this).find('th').html();
        
        if(idRuta===undefined || idRuta===null)
            return;
        
        $("#content-page").css("overflow-y","hidden");
        var fadeout=$("#content-page").hide().delay(100).promise();
        
        closeEventSource();
        
        rtaGetLoc(idRuta);
        
        $.ajax({
            url:'/rta/update',
            type:"POST",
            async:true,
            data:{idRuta:idRuta},
            success: function(res){
                        
                if(res.indexOf('Login') !== -1 || res.indexOf('This session has been expired') !== -1)
                    window.location.href="/auth/login?logout=true";
            
                fadeout.then(function(){
                    $("#content-page").html(res).fadeIn(100).promise().then(function(){
                        $("#content-page").css("overflow-y","hidden");
                    }); 
                });
            },
            error: function(xhr, status, error){
                // Maneja cualquier error que ocurra durante la llamada    
                
                if(xhr.responseText.indexOf('This session has been expired') !== -1)
                    window.location.href="/auth/login?logout=true";  
                
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