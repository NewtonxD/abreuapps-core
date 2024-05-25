var map;

$(function(){
    
    let config = {
        minZoom: 7,
        maxZoom: 18
    };
    
    const zoom = 16;
    const lat = 19.4765428;
    const lng = -70.7097998;
    
    if ('serviceWorker' in navigator) {
        navigator.serviceWorker.register(`${SERVER_IP}/service-worker.js`,{scope:"/"});
    }
    
    if (map != undefined) map.remove();
    map = L.map("map", config).setView([lat, lng], zoom);
    
    L.tileLayer(TILE_API_IP, {}).addTo(map);
        
    L.control.scale({imperial: false,}).addTo(map);

    var marker = L.marker([lat, lng]).addTo(map);
        
});