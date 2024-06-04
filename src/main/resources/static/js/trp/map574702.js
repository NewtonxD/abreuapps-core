//------------------------------------------------------------------------------
//---------DECLARACION DEL MAPA-------------------------------------------------
//------------------------------------------------------------------------------
var map;

let config = {
    minZoom: 7,
    maxZoom: 18
};

let zoom = 16;
let lat = 19.488365437890657;
let lng = -70.71529535723246;

if (map != undefined)
    map.remove();
map = L.map("map", config).setView([lat, lng], zoom);

L.tileLayer.fetch(TILE_API_IP).addTo(map);

L.control.scale({imperial: false, }).addTo(map);
//------------------------------------------------------------------------------
//----------PANEL PARA LA INFORMACION DE LOS OBJETOS----------------------------
//------------------------------------------------------------------------------
var pane = map.createPane("fixed", document.getElementById("map"));


//------------------------------------------------------------------------------
//----------PERMISO DE LOCALIZACION---------------------------------------------
//------------------------------------------------------------------------------
if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(function (position) {
        // PERMISO ACEPTADO: MARCADOR EN LOCALIZACION USUARIO + BOTON DE CASITA PARA VOLVER
        lat = position.coords["latitude"];
        lng = position.coords["longitude"];
        
        const home ='<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 32 32"><path d="M32 18.451L16 6.031 0 18.451v-5.064L16 .967l16 12.42zM28 18v12h-8v-8h-8v8H4V18l12-9z" /></svg>';

        const marker = L.marker([lat, lng], {
              icon: L.divIcon({
                className: "custom-icon-marker",
                iconSize: L.point(25, 25),
                html: home
              })
        }).addTo(map);
        
        const popup = L.popup({
            pane: "fixed",
            className: "popup-fixed test",
            autoPan: false,
        }).setContent(`<h5>Mi Ubicación actual.</h5><label class="text-muted">( ${lat} , ${lng} ).</label><br>`);

        marker.bindPopup(popup).on("click", fitBoundsPadding);
        map.setView([lat, lng], zoom);

        
        const customControl = L.Control.extend({
            options: {position: "topleft"},
            onAdd: function () {
                const btn = L.DomUtil.create("button");
                btn.title = "Inicio";
                btn.innerHTML = home;
                btn.className += "leaflet-bar back-to-home hidden";
                return btn;
            },
        });
        
        map.addControl(new customControl());
        map.on("moveend", getCenterOfMap);
    },
    function (error) {
        // LOCALIZACION POR DEFECTO AL RECHAZAR PERMISOS
        const marker = L.marker([lat, lng]).addTo(map);

        const popup = L.popup({
            pane: "fixed",
            className: "popup-fixed test",
            autoPan: false,
        }).setContent(`<h5>Ubicación por defecto.</h5><label class="text-muted">( ${lat} , ${lng} ).</label><br><p>Para obtener su ubicación actual acepte los permisos de localización y recargue la plataforma.</p>`);

        marker.bindPopup(popup).on("click", fitBoundsPadding);
        map.setView([lat, lng], zoom);
    },
    {
        enableHighAccuracy: true
    });
}

const pairRtaWithLoc = (rtaArray, locArray) => {
    return rtaArray.map(rtaItem => {
        const matchLocRta = locArray.filter(loc => loc.rta === rtaItem.rta);
        return {
            ...rtaItem,
            loc: matchLocRta
        };
    });
};

$.ajax({
    url:`${SERVER_IP}/API/trp/getStatic`,
    type:"POST",
    async:true,
    success: function(res){
        
        if(res.rutas!==null && res.rutas!==undefined){
            for (let i = 0; i < res.rutas.length; i++) {
                let coordinates = 
                        res.rutasLoc
                         .filter(loc => loc.rta === res.rutas[i].rta)
                         .map(point => [point.lat, point.lon]);
                // en coordenadas tenemos los locs
                // en res.rutas[i] la info de la ruta.
                // cambiar popup pane y obtener color random para la ruta. ex 24 polyline
                if(coordinates.length>0) L.polyline(coordinates).addTo(map);
            }
        }
        
        if(res.paradas!==null && res.paradas!==undefined){
            for (let i = 0; i < res.paradas.length; i++) {
                const popup = L.popup({
                    pane: "fixed",
                    className: "popup-fixed test",
                    autoPan: false,
                }).setContent(`<h5>${res.paradas[i].dsc.toString()}.</h5><label class="text-muted">( ${res.paradas[i].lat} , ${res.paradas[i].lon} ).</label>`);

                L.marker([res.paradas[i].lat,res.paradas[i].lon])
                .bindPopup(popup)
                .addTo(map);
        
            }
        }
        
    }
 });
    
//------------------------------------------------------------------------------
//-----------MEDIA QUERY--------------------------------------------------------
//------------------------------------------------------------------------------
const mediaQueryList = window.matchMedia("(min-width: 700px)");

mediaQueryList.addEventListener('change', (event) => onMediaQueryChange(event));

onMediaQueryChange(mediaQueryList);


//------------------------------------------------------------------------------
//------------FUNCIONES IMPORTANTES---------------------------------------------
//------------------------------------------------------------------------------
function onMediaQueryChange(event) {
    if (event.matches) {
        document.documentElement.style.setProperty("--min-width", "true");
    } else {
        document.documentElement.style.removeProperty("--min-width");
    }
}
//------------------------------------------------------------------------------
function fitBoundsPadding(e) {
    const boxInfoWith = document.querySelector(".leaflet-popup-content-wrapper")
            .offsetWidth;
    
    const featureGroup = L.featureGroup([e.target]).addTo(map);

    const getPropertyWidth = document.documentElement.style.getPropertyValue("--min-width");
    
    map.fitBounds(featureGroup.getBounds(), {
        paddingTopLeft: [getPropertyWidth ? -boxInfoWith : 0, 10],
    });
}
//------------------------------------------------------------------------------
const compareToArrays = (a, b) => JSON.stringify(a) === JSON.stringify(b);
//------------------------------------------------------------------------------
function getCenterOfMap() {
    const buttonBackToHome = document.querySelector(".back-to-home");
    buttonBackToHome.classList.remove("hidden");

    buttonBackToHome.addEventListener("click", () => {
        map.flyTo([lat, lng], zoom);
    });

    map.on("moveend", () => {
        const {lat: latCenter, lng: lngCenter} = map.getCenter();

        const latC = latCenter.toFixed(3) * 1;
        const lngC = lngCenter.toFixed(3) * 1;

        const defaultCoordinate = [+lat.toFixed(3), +lng.toFixed(3)];

        const centerCoordinate = [latC, lngC];

        if (compareToArrays(centerCoordinate, defaultCoordinate)) {
            buttonBackToHome.classList.add("hidden");
        }
    });
}
//------------------------------------------------------------------------------