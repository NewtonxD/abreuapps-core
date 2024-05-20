var eventSource_dtgnr = null;

var clientId="";

function generateClientId() {
    // Generate a unique identifier (you can use UUID or any other method)
    var millis = new Date().getTime();
    var randomNum = Math.floor(Math.random() * 1000000); // Generate a random number with 6 digits
    return millis + '-' + randomNum;
} 

                
function createEventSource() {
  if (eventSource_dtgnr === null || eventSource_dtgnr === undefined) {
    clientId=generateClientId();
    eventSource_dtgnr = new EventSource(`${SERVER_IP}/see/pda?clientId=${clientId}`,{withCredentials:true});
    eventSource_dtgnr.onmessage = function(event) {
  
        var data = JSON.parse(event.data); 

        // Determinar si es una actualización o inserción basado en los datos recibidos
        if (data['U']!==undefined && data['U']!==null) {
          // Buscar y actualizar la fila correspondiente en la tabla
          $('#table tbody tr[data-id="' + data['U'].id + '"]').html(createTableRow(data['U']));
        } else {          
          var t=$('#table').DataTable();
          t.row.add($(createTableRow(data["I"])));
          t.draw();

        }
        
        var notificacion=new Audio(`${SERVER_IP}/content/audio/n44.mp3`);
        notificacion.volume=1;
        notificacion.play();
        
    };
    
    eventSource_dtgnr.onerror = function(event){
        // falta implementar toast para notificar de falta de conexion
    };
    
  }
}

function closeEventSource(callServer=true){
    if(eventSource_dtgnr!==null && eventSource_dtgnr!==undefined){
        eventSource_dtgnr.close();
        eventSource_dtgnr=undefined;
        if(callServer) $.get(`${SERVER_IP}/see/pda/close?clientId=${clientId}`);
    }
}

function createTableRow(d) {
    return `<tr data-id="${d.id}"><th>${d.id}</th><td>${d.dsc}</td><td>${d.act?'Activo':'Inactivo'}</td></tr>` ;
}

$(function(){
    if (eventSource_dtgnr === null || eventSource_dtgnr === undefined) {
        createEventSource();
    }
});

