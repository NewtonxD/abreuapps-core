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
        paddingTopLeft: [getPropertyWidth ? -boxInfoWith : 0, 10]
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

    map.addEventListener('moveend', () => {
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

//------------------------------------------------------------------------------
//---------DECLARACION DEL MAPA-------------------------------------------------
//------------------------------------------------------------------------------
var map;

let config = {
    minZoom: 7,
    maxZoom: 18,
    preferCanvas: true
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
            autoPan: false
        }).setContent(`<div data-id='' data-lat=${lat} data-lon=${lng} data-type='myloc' ><h5>Mi Ubicación actual.</h5><label class="text-muted">( ${lat} , ${lng} ).</label><br></div>`);

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
        }).setContent(`<div data-id='' data-type='loc_def'><h5>Ubicación por defecto.</h5><label class="text-muted">( ${lat} , ${lng} ).</label><br><p>Para obtener su ubicación actual acepte los permisos de localización y recargue la plataforma.</p></div>`);

        marker.bindPopup(popup).on("click", fitBoundsPadding);
        map.setView([lat, lng], zoom);
    },
    {
        enableHighAccuracy: true
    });
}

map.on('popupopen', function(event) {
    const popupNode = event.popup._contentNode.querySelector('div[data-id]');
    const id = popupNode.getAttribute('data-id');
    const type = popupNode.getAttribute('data-type');

    // You can now use the id and type to call another endpoint
    console.log(`ID: ${id}, Type: ${type}`);

    // Example of calling another endpoint
    /*fetch(`${SERVER_IP}/API/trp/getDetails?id=${id}&type=${type}`, {
        method: 'GET'
    })
    .then(response => response.json())
    .then(details => {
        // Handle the response with additional details here
        console.log(details);
    })
    .catch(error => console.error('Error fetching details:', error));*/
});

fetch(`${SERVER_IP}/API/trp/getStatic`, {
    method: 'GET'
}).then(response => response.json())
.then(res => {
    if(res.rutasLoc!==null && res.rutasLoc!==undefined){
        const routePointsMap = new Map();

        res.rutasLoc.forEach(loc => {
            if (!routePointsMap.has(loc.rta)) {
                routePointsMap.set(loc.rta, []);
            }
            routePointsMap.get(loc.rta).push([loc.lat, loc.lon]);
        });
        
        routePointsMap.forEach((coordinates, routeName) => {
             
            const color=getRandomColor();            
            const contentPopup = `<div data-id="${routeName}" data-type="rta"><h5>${routeName}.</h5></div>`;

            const popup = L.popup({
                pane: "fixed",
                className: "popup-fixed test",
                autoPan: false
            }).setContent(contentPopup);
            
            if(coordinates.length>0) L.polyline(coordinates,{
                color: color,
                weight: 7
            }).bindPopup(popup).addTo(map);
        });
    }

    if(res.paradas!==null && res.paradas!==undefined){
        for (let i = 0; i < res.paradas.length; i++) {
            
            const popupContent=`<div data-id="${res.paradas[i].id}" data-type="pda"><h5>${res.paradas[i].dsc}.</h5><label class="text-muted">( ${res.paradas[i].lat} , ${res.paradas[i].lon} ).</label></div>`;
            
            const popup = L.popup({
                pane: "fixed",
                className: "popup-fixed test",
                autoPan: false
            }).setContent(popupContent);
            
            L.marker([res.paradas[i].lat,res.paradas[i].lon])
            .bindPopup(popup)
            .addTo(map);

        }
    }
    
}).catch(error => console.error('Error fetching data:', error));
    
//------------------------------------------------------------------------------
//-----------MEDIA QUERY--------------------------------------------------------
//------------------------------------------------------------------------------
const mediaQueryList = window.matchMedia("(min-width: 700px)");

mediaQueryList.addEventListener('change', (event) => onMediaQueryChange(event));

onMediaQueryChange(mediaQueryList);
