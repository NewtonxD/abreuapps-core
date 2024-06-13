
function dataPrepare(idForm){
    let d=$("#"+idForm).serializeArray();
    $("#"+idForm+" input[type=checkbox]").each(function() {
        if (!$(this)[0].checked) {
            d.push({name:$(this).attr("name"),value:"false"});
        }
    });
    return d;
}

function generateClientId() {
    var millis = new Date().getTime();
    var randomNum = Math.floor(Math.random() * 1000000); // Generate a random number with 6 digits
    return millis + '-' + randomNum;
} 

function createEventSource() {
  if (sse === null || sse === undefined) {
    clientId=generateClientId();
    sse = new EventSource(`${SERVER_IP}${SSE_LINK}?clientId=${clientId}`,{withCredentials:true});
    sse.onmessage = function(event) {
  
        var data = JSON.parse(event.data); 

        // Determinar si es una actualización o inserción basado en los datos recibidos
        if (data['U']!==undefined && data['U']!==null) {
          // Buscar y actualizar la fila correspondiente en la tabla
          $(`#table tbody tr[data-id="${ data['U'][SSE_PK]}"]`).replaceWith(createTableRow(data['U']));
        } else {  
            if($(`#table tbody tr[data-id="${ data['I'][SSE_PK] }"]`)===undefined){
                let t=$('#table').DataTable();
                t.row.add($(createTableRow(data["I"])));
                t.draw();
            }
        }
        
        let notificacion=new Audio(`${SERVER_IP}/content/audio/n44.mp3`);
        notificacion.volume=1;
        notificacion.play();
        
    };
    
    sse.onerror = function(event){
        if (!errorToastTimeout) {
            showToast('Conexión inestable. Verifica tu Internet y refresca la plataforma.', 'warning');
            errorToastTimeout = setTimeout(() => {
              errorToastTimeout = null;
            }, ERROR_TOAST_INTERVAL);
        }
    };
    
  }
}

function closeEventSource(){
    if(sse!==null && sse!==undefined){
        sse.close();
        sse=null;
        $.get(`${SERVER_IP}${SSE_LINK}/close?clientId=${clientId}`);
        clientId="";
    }
}

function post_plantilla(LINK,DATA){
    
    $("#content-page").css("overflow-y","hidden");

    var fadeout=$("#content-page").hide().delay(100).promise();

    $.post({
        url: `${SERVER_IP}${LINK}`,
        async:true,
        data: DATA,
        dataType : 'html',
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
        dataType : 'html',
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
    
    $(document).off("click","tbody tr");
    
    var data={id:id};
    
    post_plantilla("/main/content-page/",data);
    
    
}
    
            
