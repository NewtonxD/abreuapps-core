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
    eventSource_dtgnr = new EventSource('/see/pda?clientId='+clientId,{withCredentials:true});
    eventSource_dtgnr.onmessage = function(event) {
  
        var data = JSON.parse(event.data); 

        // Determinar si es una actualización o inserción basado en los datos recibidos
        if (data['U']!==undefined && data['U']!==null) {
          // Buscar y actualizar la fila correspondiente en la tabla
          $('#table tbody tr[data-id="' + data['U'].dato + '"]').html(createTableRow(data['U'],true));
        } else {          
          var t=$('#table').DataTable();
          t.row.add($(createTableRow(data["I"])));
          t.draw();

        }
        
        var notificacion=new Audio('/content/audio/n44.mp3');
        notificacion.volume=1;
        notificacion.play();
        
    };
    
    eventSource_dtgnr.onerror = function(event){
        closeEventSource(false);
    };
    
  }
}

function closeEventSource(callServer=true){
    if(eventSource_dtgnr!==null && eventSource_dtgnr!==undefined){
        eventSource_dtgnr.close();
        eventSource_dtgnr=undefined;
        if(callServer) $.get('/see/pda/close?clientId='+clientId);
    }
}

function createTableRow(d,update=false) {
    var row = !update ? '<tr data-id="' + d.id + '">' : '';
    row += '<th>'+ d.id + '</th>';
    row += '<td>' + d.descripción+'</td>';
    row += '<td>' + (d.activo?'Activo':'Inactivo') + '</td>';
    row +=  !update ? '</tr>' : '';
    return row;
}

$(function(){
    if (eventSource_dtgnr === null || eventSource_dtgnr === undefined) {
        createEventSource();
    }
});
