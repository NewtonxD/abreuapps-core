$(function(){
    var data=[];
    var tmp=window.data_loc;
    if(tmp!==undefined && tmp!==null){
        data=tmp;
        delete window.data_loc;
    }
    
    if( data["lat"] !== undefined && data["lat"]!==null){
//         por defecto omsa oficina santiago o actual
        data["lat"]=19.488365437890657;
        data["lon"]=-70.71529535723246;
    }
        
    let config = {
        minZoom: 7,
        maxZoom: 18
    };
    const zoom = 16;
    const lat = data.lat;
    const lng = data.lon;
    
    const map = L.map("map", config).setView([lat, lng], zoom);

    L.tileLayer("https://tile.openstreetmap.org/{z}/{x}/{y}.png", {
      attribution:
        '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors',
    }).addTo(map);
    
    L.control.scale({imperial: false,}).addTo(map);
  
    var marker = L.marker([lat, lng]).addTo(map);
    
    if(data.paradas!==null && data.paradas!==undefined){
        for (let i = 0; i < data.paradas.length; i++) {
            console.log([data.paradas[i]);
            L.marker([data.paradas[i].latitud,data.paradas[i].longitud], {
              icon: L.divIcon({
                className: "custom-icon-marker",
                iconSize: L.point(40, 40),
                html: `<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 32 32" class="marker"><path fill-opacity="0.25" d="M16 32s1.427-9.585 3.761-12.025c4.595-4.805 8.685-.99 8.685-.99s4.044 3.964-.526 8.743C25.514 30.245 16 32 16 32z"/><path stroke="#fff" fill="red" d="M15.938 32S6 17.938 6 11.938C6 .125 15.938 0 15.938 0S26 .125 26 11.875C26 18.062 15.938 32 15.938 32zM16 6a4 4 0 100 8 4 4 0 000-8z"/></svg>`,
                iconAnchor: [12, 24],
                popupAnchor: [9, -26],
              }),
            })
              .bindPopup(`<b>Marker coordinates</b>:<br>${data.paradas[i].descripción.toString()}`)
              .addTo(map);
        }
        
    }
    
    // debemos ingresar todos  los marcadores de la data para poder ver las paradas que ya estan
    // y evitar poner o cargar el marcador que estamos editando en ese listado
    // marcadores de otro color, rojo o no se
    
    $("#latitud").on("blur",function(e){
        marker.setLatLng([$("#latitud").val(), $("#longitud").val()]);
        map.setView(marker.getLatLng(), zoom);
    });
    
    $("#longitud").on("blur",function(e){
        marker.setLatLng([$("#latitud").val(), $("#longitud").val()]);
        map.setView(marker.getLatLng(), zoom);
    });
    
    map.on('click', function (e) {
        //set marker update on click
        marker.setLatLng([e.latlng.lat, e.latlng.lng]);
        $("#latitud").val(e.latlng.lat);
        $("#longitud").val(e.latlng.lng);
    });
        
    
});
