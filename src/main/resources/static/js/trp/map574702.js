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
    const API_KEY='6e5478c8a4f54c779f85573c0e399391';
    
    

    L.tileLayer(`https://c.tile.thunderforest.com/transport/{z}/{x}/{y}.png?apikey=${API_KEY}`, {
      attribution:
        '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a>',
    }).addTo(map);

    var marker = L.marker([lat, lng]).addTo(map);
        
});