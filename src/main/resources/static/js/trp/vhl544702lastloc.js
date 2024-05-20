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
    
        if ('serviceWorker' in navigator) {
            navigator.serviceWorker.register(`${SERVER_IP}/service-worker.js`,{scope:"/"}).then(function() {
                console.log('Service Worker Registered Successfully');
            });
        }
        
        $("#date_last_loc").html(data.fecha);
        
        const map = L.map("map", config).setView([lat, lng], zoom);
        
        L.tileLayer(TILE_API_IP, {}).addTo(map);
        
        L.control.scale({imperial: false,}).addTo(map);
        
        var marker = L.marker([lat, lng]).addTo(map);
        
    }
});

