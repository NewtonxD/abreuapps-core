
const TILE_API_IP=SERVER_IP+"/API/tiles/{z}/{x}/{y}";
const GOOGLE_MAPS_LINK='https://maps.google.com/maps?q=';// -36.623758386860175, 174.5020302019307;
const TILE_CACHE_NAME = 'tile-cache';
const ERROR_TOAST_INTERVAL = 6000;  

var MY_LAT=0;
var MY_LON=0;

var SSE_LINK=null;
var SSE_PK=null;
var SSE_FK=null;
var sse = null;
var clientId="";
var errorToastTimeout = null;

var isCacheEnabled=('caches' in window);

function getLinkGM(lat, lon){
    window.open(`${GOOGLE_MAPS_LINK}${lat},${lon}`,"_blank");
}

function getButtonGM(lat,lon){
    return `<a href="#" class="btn icon-google-maps" onclick="getLinkGM('${lat}','${lon}')"><img src="${SERVER_IP}/content/assets/img/google-maps1.webp"/></a>`;
}

function getMyLocationPopup(permitted){
    if(permitted) 
        return `<div class="row d-flex justify-content-center"><div class="col text-center mt-2" data-id='' data-lat=${MY_LAT} data-lon=${MY_LON} data-type='myloc' ><h4>Mi ubicación</h4>${getButtonGM(MY_LAT, MY_LON)}</div></div>`
    else 
        return `<div class="row d-flex justify-content-center"><div class="col text-center  mt-2" data-id='' data-type='loc_def'><h4>Ubicación por defecto</h4>${getButtonGM(BASE_LAT, BASE_LNG)}<p>Para obtener su ubicación actual acepte los permisos de localización y recargue la plataforma.</p><p>Esta es la localización de la base de la OMSA: <b>${BASE_DIR}</b></p></div></div>`;
}

function getVehiclePopup(placa,ruta,lat,lon,orientation,color){
    return `<div class="row d-flex justify-content-center"><div class="col text-center ms-0 me-0 ms-lg-2 me-lg-2 mt-2 mb-2" data-id="${placa}" data-type="vhl" data-rta="${ruta}" data-lat="${lat}" data-lon="${lon}"><h4><b>Vehiculo</b><br>${placa}</h4>${getButtonGM(lat, lon)}<h6>${velocidad} Km/h.<img src='${SERVER_IP}/content/css/images/downarrow-50.png' class='img-arrow-${placa}' style='width:20px; height:20px; transform:rotate(${orientation}deg);'/></h6>
            </div></div><div class="row d-flex justify-content-center"><div class="col mt-2 text-center"><button type="button" data-type="vhl" onclick="vhlToggleClick(event)" class="btn btn-lg custom-vhl-toggle-button" data-bs-toggle="button" data-vhl="${placa}" data-customId="custom-${ruta}-${placa}" id="custom-${ruta}-${placa}" style="background-color:${color};">Ruta : ${ruta}</button></div></div>`;
}

function getRoutePopup(routeName,desde,hasta,distancia_total,vehiculos_activos){
    return `<div class="row d-flex justify-content-center"><div class="col text-center ms-0 me-0 ms-lg-2 me-lg-2 mt-2" data-id="${routeName}" data-type="rta"><h4><b>Ruta</b><br>${routeName}</h4></div></div><div class="row d-flex justify-content-center">
     <div class="col text-center ms-0 me-0 ms-lg-2 me-lg-2"><p class="h6"><small class="text-muted">Inicia en:</small> ${desde}. <br><small class="text-muted">Termina en:</small> ${hasta}.<br><small class="text-muted">Distancia total:</small> ${distancia_total} metros.<br><small class="text-muted">Autobuses activos:</small> ${vehiculos_activos}.</p></div></div>`;
}

function getInformationPopup(){
    return `<div class="row d-flex justify-content-center"><div class="col text-center  mt-2" data-id='' data-type='info'><h4>Información</h4><p>Este es un nuevo popup para las informaciones dle sistema.</p></div></div>`;
}

function getNearestParadePopup(locId,locLat,locLon,locName,locDistance){
    return `<div class="row d-flex justify-content-center"><div class="col text-center mt-2"><h6>La parada más cercana:</h6><button type="button" id="center-link-pd${locId}" class="btn btn-primary" data-lat="${locLat}" data-lon="${locLon}"><b>${locName} a ${locDistance} Mts</b></button></div></div>`;
}

function getParadeInfoPopup(idPda,rta,vhl,vhlLat,vhlLon,idElement,color,minutos){
    return `<div class="row d-flex justify-content-center"><div class="col text-center mt-2"><button type="button" data-type="pda" onclick="pdaToggleClick(event)" data-pdaId="${idPda}" data-customId="custom-${rta}-${vhl}" data-vhl-lat="${vhlLat}" data-vhl-lon="${vhlLon}" id="${idElement}" class="btn ${vhl !== null ? 'custom-toggle-button' : ''} btn-lg" style="background-color:${color};" ${vhl !== null ? 'data-bs-toggle="button"' : ''}>Ruta: ${rta}&nbsp....&nbsp<b>${vhl !== null ? minutos + " min" : "n/a"}</b></button></div></div>`;
}

function getParadePopup(id,lat,lon,dsc){
    return `<div class="row d-flex justify-content-center"><div class="col text-center ms-0 me-0 ms-lg-2 me-lg-2 mt-2 mb-2" data-id="${id}" data-type="pda" data-lat="${lat}" data-lon="${lon}"><h4><b>Parada</b><br>${dsc}</h4>${getButtonGM(lat, lon)}</div></div>`
}


function showToast(message, type) {
    const toastHTML = `
        <div class="toast align-items-center text-bg-${type} border-0" role="alert" aria-live="assertive" aria-atomic="true" data-bs-autohide="true" data-bs-delay="${ERROR_TOAST_INTERVAL}">
          <div class="d-flex">
                <div class="toast-body">
                  ${message}
                </div>
                <button type="button" class="btn-close me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
          </div>
        </div>`;
    const toastContainer = $('#toast-container');
    toastContainer.html(toastHTML);
    $('.toast').toast('show').on('hidden.bs.toast', function () {
        this.remove();
    });
}

function getRandomColor(){
    const r = Math.floor(Math.random() * 156) + 100;
    const g = Math.floor(Math.random() * 156) + 100;
    const b = Math.floor(Math.random() * 156) + 100; 
    return `rgba(${r},${g},${b},0.7)`; 
}