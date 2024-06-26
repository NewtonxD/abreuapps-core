var map;

$(function(){
    var data=[];
    
    var ruta="";
    if(! ($("#Ruta")=== undefined || $("#Ruta")===null) ){
        ruta=$("#Ruta").val();
    }
    
    $.ajax({
       url:`${SERVER_IP}/rta/getLoc`,
       type:"POST",
       async:false,
       data:{idRuta:ruta},
       success: function(res){
           data=res;
       }
    });
    
    if( data["lat"] === undefined || data["lat"]===null){
    //  por defecto omsa oficina santiago o actual
        data["lat"]=19.488365437890657;
        data["lon"]=-70.71529535723246;
    }
        
    let config = {
        minZoom: 7,
        maxZoom: 18
    };
    const zoom = 18;
    const lat = data.lat;
    const lng = data.lon;
    
    if (map != undefined) map.remove();
    map = L.map("map", config).setView([lat, lng], zoom);

    L.tileLayer.fetch(TILE_API_IP).addTo(map);
    
    L.control.zoom({ position: "topright" }).addTo(map);
    
    const options = {
        position: "topleft", // toolbar position, options are 'topleft', 'topright', 'bottomleft', 'bottomright'
        drawMarker: false, // adds button to draw markers
        drawPolygon: false, // adds button to draw a polygon
        drawPolyline: true, // adds button to draw a polyline
        drawCircle: false, // adds button to draw a cricle
        drawCircleMarker:false,
        drawRectangle:false,
        drawText:false,
        editPolygon: true, // adds button to toggle global edit mode
        deleteLayer: true, // adds a button to delete layers
    };

    // add leaflet.pm controls to the map
    map.pm.addControls(options);
    
    L.control.scale({imperial: false}).addTo(map);
    
    if(data.paradas!==null && data.paradas!==undefined){
        for (let i = 0; i < data.paradas.length; i++) {
            L.marker([data.paradas[i].lat,data.paradas[i].lon], {
              icon: L.divIcon({
                className: "custom-icon-marker",
                iconSize: L.point(25, 40),
                html: `<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 32 32" class="marker"><path fill-opacity="0.25" d="M16 32s1.427-9.585 3.761-12.025c4.595-4.805 8.685-.99 8.685-.99s4.044 3.964-.526 8.743C25.514 30.245 16 32 16 32z"/><path stroke="#fff" fill="red" d="M15.938 32S6 17.938 6 11.938C6 .125 15.938 0 15.938 0S26 .125 26 11.875C26 18.062 15.938 32 15.938 32zM16 6a4 4 0 100 8 4 4 0 000-8z"/></svg>`,
                iconAnchor: [12, 24],
                popupAnchor: [1, -24],
              }),
            })
              .bindPopup(`${data.paradas[i].dsc.toString()}`)
              .addTo(map);
        }
        
    }  
    
    if(data.ruta!==null && data.ruta!==undefined){
        
        var coordinates = data.ruta.map(function(point) {
            return [point.lat, point.lon];
        });
        
        if(coordinates.length>0){
            var polyline = L.polyline(coordinates).addTo(map);

            map.fitBounds(polyline.getBounds());
        }
    }
    
    $('#getPolylineData').click(function() {
        var polylines = [];
        map.eachLayer(function(layer) {
            if (layer instanceof L.Polyline && !(layer instanceof L.Polygon)) {
                polylines.push(layer.getLatLngs());
            }
        });
        window.data_poly=polylines;
    });
    
});



