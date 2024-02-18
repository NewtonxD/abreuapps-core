/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/javascript.js to edit this template
 */
//document ready
$(function(){
    
    $(".acceso").on("click",function(e){
        e.preventDefault;
        var id=$(this).attr("id");
        $('.nav').children('a.acceso').removeClass('active');
        $(this).addClass("active");
        $("#sidebarToggle").click();
        $("#content-page").css("overflow-y","hidden");
        var fadeout=$("#content-page").hide().delay(150).promise();
        
        $.post({
            url: '/main/content-page/',
            async:true,
            data: {id:id},
            success: function(xhr, status, error) {
                
                if(xhr.indexOf('Login') !== -1 || xhr.indexOf('This session has been expired') !== -1)
                    window.location.href="/auth/login?logout=true";
                
              fadeout.then(function(){
                    
                    var fadein=$("#content-page").html(xhr).fadeIn(200).promise();
                    
                    fadein.then(function(){
                        $("#content-page").css("overflow-y","hidden");
                        
                    }); 
                });
            },
            error: function(xhr, status, error) {
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
        
    });
    
});
