//const SERVER_IP="https://3c53e981ee43d3.lhr.life";
//const SERVER_IP="https://192.168.100.76:8090";
const SERVER_IP="http://localhost:8090";
const TILE_API_IP=SERVER_IP+"/API/tiles/{z}/{x}/{y}";
const GOOGLE_MAPS_LINK='https://maps.google.com/maps?q=';// -36.623758386860175, 174.5020302019307;
const TILE_CACHE_NAME = 'tile-cache';
const ERROR_TOAST_INTERVAL = 6000;

var SSE_LINK=null;
var SSE_PK=null;
var sse = null;
var clientId="";
var errorToastTimeout = null;

var isCacheEnabled=('caches' in window);

function getLinkGM(lat, lon){
    window.open(`${GOOGLE_MAPS_LINK}${lat},${lon}`,"_blank");
}

function getButtonGM(lat,lon){
    return `<div class="row d-flex justify-content-end custom-fixed"><div class="col text-end">
            <a href="#" class="btn icon-google-maps" onclick="getLinkGM('${lat}','${lon}')"><img src="${SERVER_IP}/content/assets/img/google-maps1.webp"/></a>
            </div></div>`;
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