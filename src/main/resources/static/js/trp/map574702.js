/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/javascript.js to edit this template
 */

$(function(){
    
    let config = {
        minZoom: 7,
        maxZoom: 18
    };
    const zoom = 16;
    const lat = 19.4765428;
    const lng = -70.7097998;

    const map = L.map("map", config).setView([lat, lng], zoom);
    
    L.tileLayer("/API/tiles/{z}/{x}/{y}", {}).addTo(map);
        
    L.control.scale({imperial: false,}).addTo(map);

    var marker = L.marker([lat, lng]).addTo(map);
        
});