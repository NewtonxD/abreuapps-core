
function dataPrepare(idForm){
    let d=$("#"+idForm).serializeArray();
    $("#"+idForm+" input[type=checkbox]").each(function() {
        if (!$(this)[0].checked) {
            d.push({name:$(this).attr("name"),value:"false"});
        }
    });
    return d;
}


function post_plantilla(LINK,DATA){
    
    $("#content-page").css("overflow-y","hidden");

    var fadeout=$("#content-page").hide().delay(100).promise();

    $.post({
        url: `${SERVER_IP}${LINK}`,
        async:true,
        data: DATA,
        success: function(response) {

            if(response.indexOf('Login') !== -1 || response.indexOf('This session has been expired') !== -1)
                window.location.href=`${SERVER_IP}/auth/login?logout=true`;

          fadeout.then(function(){
                var fadein=$("#content-page").html(response).fadeIn(100).promise();

                fadein.then(function(){
                    $("#content-page").css("overflow-y","hidden");

                }); 
            });
        },
        error: function(xhr, status, error) {
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

function get_plantilla(LINK){
    $("#content-page").css("overflow-y","hidden");
    var fadeout=$("#content-page").hide().delay(100).promise();

    $.get({
        url: `${SERVER_IP}${LINK}`,
        async:true,
        success: function(xhr, status, error) {

            if(xhr.indexOf('Login') !== -1 || xhr.indexOf('This session has been expired') !== -1)
                window.location.href=`${SERVER_IP}/auth/login?logout=true`;

            fadeout.then(function(){

                var fadein=$("#content-page").html(xhr).fadeIn(100).promise();

                fadein.then(function(){
                    $("#content-page").css("overflow-y","hidden");

                }); 
            });
        },
        error: function(xhr, status, error) {

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


function cargar_contenido(id){
    
    if(typeof closeEventSource==='function') closeEventSource();
    
    $(document).off("click","tr");
    
    var data={id:id};
    
    post_plantilla("/main/content-page/",data);
    
    
}
    
            
