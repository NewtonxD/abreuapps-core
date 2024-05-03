$(function(){
    var data=[];
    var tmp=window.data_loc;
    
    if(tmp!==undefined && tmp!==null){
        delete window.data_lastloc;
        data=tmp;
    }else{
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
    
    // debemos ingresar todos  los marcadores de la data para poder ver las paradas que ya estan
    // y evitar poner o cargar el marcador que estamos editando en ese listado
    // marcadores de otro color, rojo o no se
    
    map.on('click', function (e) {
        //set marker update on click
        marker.setLatLng([e.latlng.lat, e.latlng.lng]);
    });
        
    
});
