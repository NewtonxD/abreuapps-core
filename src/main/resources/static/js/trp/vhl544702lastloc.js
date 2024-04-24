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
        // magnification with which the map will start
        const zoom = 18;
        // co-ordinates
        const lat = data.lat;
        const lng = data.lon;

        // calling map
        const map = L.map("map", config).setView([lat, lng], zoom);

        // Used to load and display tile layers on the map
        // Most tile servers require attribution, which you can set under `Layer`
        L.tileLayer("https://tile.openstreetmap.org/{z}/{x}/{y}.png", {
          attribution:
            '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors',
        }).addTo(map);
        
    }
});

