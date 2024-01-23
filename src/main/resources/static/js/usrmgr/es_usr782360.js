/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/javascript.js to edit this template
 */

var eventSource_dtgnr = null;
                
function createEventSource() {
  if (eventSource_dtgnr === null) {
    eventSource_dtgnr = new EventSource('/see/usrmgr',{withCredentials:true});
    eventSource_dtgnr.onmessage = function(event) {
  
        var data = JSON.parse(event.data); 

        // Determinar si es una actualización o inserción basado en los datos recibidos
        if (data['U']!==undefined && data['U']!==null) {
          // Buscar y actualizar la fila correspondiente en la tabla
          $('#table tbody tr[data-id="' + data['U'].id + '"]').replaceWith(createTableRow(data['U']));
        } else {          
          var t=$('#table').DataTable();
          t.row.add($(createTableRow(data["I"])));
          t.draw();

        }
        
        //$("#act_dt").html(data["date"]);
        
        var notificacion=new Audio('/content/audio/n44.mp3');
        notificacion.volume=0.3;
        notificacion.play();
        
    };
    
    eventSource_dtgnr.onerror = function(event) {
        //a los 5 errores de conexion cerrar event emitter
        //eventSource_dtgnr.close();
    };
  
  }
}

function closeEventSource(){
    if(eventSource_dtgnr!==null && eventSource_dtgnr!==undefined){
        eventSource_dtgnr.close();
    }
}

function createTableRow(d) {
    var row = '<tr data-id="' + d.usuario.username + '">';
    row += '<th>' + d.usuario.username + '</th>';
    row += '<td>'+ d.nombre+' '+d.apellido + '</td>';
    row += '<td>' + d.usuario.correo + '</td>';
    row += '<td>' + (d.usuario.activo?'Activo':'Inactivo') + '</td>'; 
    row += '</tr>';
    return row;

}

$(function(){
    if (eventSource_dtgnr === null) {
        createEventSource();
    }
});