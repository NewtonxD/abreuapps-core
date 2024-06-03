var map;
$(function(){
    
    var data;
    
    var Placa="";
    if(! ($("#Placa")=== undefined || $("#Placa")===null) ){
        Placa=$("#Placa").val();
    }
    
    $.ajax({
        url:`${SERVER_IP}/vhl/getLastLoc`,
        type:"POST",
        async:false,
        data:{placa:Placa},
        success: function(res){
            data=res;
        }
    });
    
    if(data!==undefined && data!==null){
        
        
        let config = {
            minZoom: 7,
            maxZoom: 18
        };
        const zoom = 16;
        const lat = data.lat;
        const lng = data.lon;
        
        
        $("#date_last_loc").html(data.fecha);
        
        if (map != undefined) map.remove();
        map = L.map("map", config).setView([lat, lng], zoom);
        
        L.tileLayer.fetch(TILE_API_IP).addTo(map);
        
        L.control.scale({imperial: false,}).addTo(map);
        
        var marker = L.marker([lat, lng]).addTo(map);
        
    }
});

