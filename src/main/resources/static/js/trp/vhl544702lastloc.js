/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/javascript.js to edit this template
 */

$(function(){
    
    var data=window.data_lastloc;
    if(data!==undefined && data!==null){
        
        delete window.data_lastloc;
        
        let config = {
            minZoom: 7,
            maxZoom: 18
        };
        const zoom = 16;
        const lat = data.lat;
        const lng = data.lon;
        
        $("#date_last_loc").html(data.fecha);
        
        const map = L.map("map", config).setView([lat, lng], zoom);
        
        L.tileLayer("/API/tiles/{z}/{x}/{y}", {}).addTo(map);
        
        L.control.scale({imperial: false,}).addTo(map);
        
        var marker = L.marker([lat, lng]).addTo(map);
        
    }
});

