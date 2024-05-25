var map;
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
    
        if ('serviceWorker' in navigator) 
            navigator.serviceWorker.register(`${SERVER_IP}/service-worker.js`,{scope:"/"});
        
        
        $("#date_last_loc").html(data.fecha);
        
        if (map != undefined) map.remove();
        map = L.map("map", config).setView([lat, lng], zoom);
        
        L.tileLayer(TILE_API_IP, {}).addTo(map);
        
        L.control.scale({imperial: false,}).addTo(map);
        
        var marker = L.marker([lat, lng]).addTo(map);
        
    }
});

