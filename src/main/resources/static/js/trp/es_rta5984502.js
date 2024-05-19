/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/javascript.js to edit this template
 */


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
    eventSource_dtgnr = new EventSource('/see/rta?clientId='+clientId,{withCredentials:true});
    eventSource_dtgnr.onmessage = function(event) {
  
        var data = JSON.parse(event.data); 

        // Determinar si es una actualización o inserción basado en los datos recibidos
        if (data['U']!==undefined && data['U']!==null) {
          // Buscar y actualizar la fila correspondiente en la tabla
          $('#table tbody tr[data-id="' + data['U'].rta + '"]').html(createTableRow(data['U'],true));
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
        // falta implementar toast para notificar de falta de conexion
    };
    
  }
}

function closeEventSource(callServer=true){
    if(eventSource_dtgnr!==null && eventSource_dtgnr!==undefined){
        eventSource_dtgnr.close();
        eventSource_dtgnr=undefined;
        if(callServer) $.get('/see/rta/close?clientId='+clientId);
    }
}

function createTableRow(d,update=false) {
    var row = !update ? '<tr data-id="' + d.rta + '">' : '';
    row += '<th>'+ d.rta + '</th>';
    row += '<td>' + d.loc_ini+'</td>';
    row += '<td>' + d.loc_fin+'</td>';
    row += '<td>' + (d.act?'Activo':'Inactivo') + '</td>';
    row +=  !update ? '</tr>' : '';
    return row;
}

$(function(){
    if (eventSource_dtgnr === null || eventSource_dtgnr === undefined) {
        createEventSource();
    }
});

