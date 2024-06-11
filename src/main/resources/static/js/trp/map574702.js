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

const markerMap = new Map();

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
        }).setContent(`<div class="row d-flex justify-content-center mt-2 mb-2">
                <div class="col text-center" data-id='' data-lat=${lat} data-lon=${lng} data-type='myloc' ><h4>Mi Ubicación actual.</h4><label class="text-muted">( ${lat} , ${lng} ).</label><br>
                </div></div>`);

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
        }).setContent(`
                <div class="row d-flex justify-content-center mt-2 mb-2">
                <div class="col text-center" data-id='' data-type='loc_def'>
                    <h4>Ubicación por defecto.</h4><label class="text-muted">( ${lat} , ${lng} ).</label><br><p>Para obtener su ubicación actual acepte los permisos de localización y recargue la plataforma.</p>
                </div></div>`);

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
    
    let data={id:id,type:type};
    
    if(type==="myloc"){
        data["lon"]=popupNode.getAttribute('data-lon');
        data["lat"]=popupNode.getAttribute('data-lat');
    }
    
    if (type === "pda") {
        const lat = popupNode.getAttribute('data-lat');
        const lon = popupNode.getAttribute('data-lon');
        const key = `${lat},${lon}`;
        const marker = markerMap.get(key);

        if (marker) {
            marker.setIcon(new L.Icon.Default({iconUrl: "marker-icon-red.png"}));
        }
    }

    // Example of calling another endpoint
    fetch(`${SERVER_IP}/API/trp/getInfoObject`, {
        method: 'POST',
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(data)
    }).then(response => response.json())
    .then(res => {
        
        if (res.locInfo && res.locInfo.length > 0) {
            const locInfo = res.locInfo[0];
            const locId = locInfo[0];
            const locName = locInfo[1];
            const locLat = locInfo[3];
            const locLon = locInfo[2];
            const locDistance = locInfo[4];

            const newContent = `
                    <div class="row d-flex justify-content-center">
                    <div class="col text-center mt-4 mb-2">
                    <button type="button" id="center-link-pd${locId}" class="btn btn-primary" data-lat="${locLat}" data-lon="${locLon}">
                        <h5>La parada más cercana:</h5>
                        <h6><b>${locName}</b> a <b>${locDistance} Mts</b>.<br></h6>
                    </button>
                    </div>
                    </div>
            `;

            // Append the new content to the existing popup content
            popupNode.innerHTML += newContent;
            
            // Add event listener to the link
            document.getElementById("center-link-pd"+locId).addEventListener('click', function(e) {
                e.preventDefault();
                const lat = parseFloat(this.getAttribute('data-lat'));
                const lon = parseFloat(this.getAttribute('data-lon'));
                map.flyTo([lat, lon], 18); // Adjust the zoom level as needed
                //
                 // Find the marker using the stored reference and open its popup
                const key = `${lat},${lon}`;
                const marker = markerMap.get(key);
                if (marker) {
                    marker.openPopup();
                }
            });
        }
        
    })
    .catch(error => console.error('Error fetching details:', error));
    
});

map.on('popupclose', function(event) {
    const popupNode = event.popup._contentNode.querySelector('div[data-id]');
    const id = popupNode.getAttribute('data-id');
    const type = popupNode.getAttribute('data-type');
    
    if (type === "pda") {
        const lat = popupNode.getAttribute('data-lat');
        const lon = popupNode.getAttribute('data-lon');
        const key = `${lat},${lon}`;
        const marker = markerMap.get(key);

        if (marker) {
            marker.setIcon(new L.Icon.Default()); // Revert to default marker icon
        }
    }
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
            const contentPopup = `<div class="row d-flex justify-content-center mt-2 mb-2">
                    <div class="col text-center" data-id="${routeName}" data-type="rta"><h4>${routeName}.</h4></div></div>`;

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
            
            const popupContent=`<div class="row d-flex justify-content-center mt-2 mb-2">
                    <div class="col text-center" data-id="${res.paradas[i].id}" data-type="pda" data-lat="${res.paradas[i].lat}" data-lon="${res.paradas[i].lon}"><h4>${res.paradas[i].dsc}.</h4><label class="text-muted">( ${res.paradas[i].lat} , ${res.paradas[i].lon} ).</label></div></div>`;
            
            const popup = L.popup({
                pane: "fixed",
                className: "popup-fixed test",
                autoPan: false
            }).setContent(popupContent);
            
            const marker=L.marker([res.paradas[i].lat,res.paradas[i].lon])
            .bindPopup(popup)
            .addTo(map);
    
            const key = `${res.paradas[i].lat},${res.paradas[i].lon}`;
            markerMap.set(key, marker);

        }
    }
    
}).catch(error => console.error('Error fetching data:', error));
    
//------------------------------------------------------------------------------
//-----------MEDIA QUERY--------------------------------------------------------
//------------------------------------------------------------------------------
const mediaQueryList = window.matchMedia("(min-width: 700px)");

mediaQueryList.addEventListener('change', (event) => onMediaQueryChange(event));

onMediaQueryChange(mediaQueryList);
