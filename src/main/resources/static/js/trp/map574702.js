var map;

$(function(){
    
    let config = {
        minZoom: 7,
        maxZoom: 18
    };
    
    let zoom = 14;
    let lat = 19.488365437890657;
    let lng = -70.71529535723246;
    
    if ('serviceWorker' in navigator) {
        navigator.serviceWorker.register(`${SERVER_IP}/service-worker.js`,{scope:"/"});
    }
    
    if (map != undefined) map.remove();
    map = L.map("map", config).setView([lat, lng], zoom);
    
    let marker;
    // sets your location as default
    if (navigator.geolocation) {
       navigator.geolocation.getCurrentPosition(function(position) {

          lat = position.coords["latitude"];
          lng = position.coords["longitude"];
          marker = L.marker([lat, lng]).bindPopup(`Mi ubicación actual: ( ${lat} , ${lng} ).`).addTo(map);
          map = L.setView([lat, lng], 16);
       },
       function(error) {
          console.log("Error: ", error);
          marker = L.marker([lat, lng]).bindPopup(`Ubicación por defecto: ( ${lat} , ${lng} ).<br>Para obtener su ubicación actual acepte los permisos de localización y recargue la plataforma.`).addTo(map);
       },
       {
          enableHighAccuracy: true
       });
    }
    
    L.tileLayer(TILE_API_IP, {}).addTo(map);
        
    L.control.scale({imperial: false,}).addTo(map);
    
        
});