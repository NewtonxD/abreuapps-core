
const SERVER_IP="https://192.168.100.76:8090";
//const SERVER_IP="https://localhost:8090";
const TILE_API_IP=SERVER_IP+"/API/tiles/{z}/{x}/{y}";
const TILE_CACHE_NAME = 'tile-cache';
const ERROR_TOAST_INTERVAL = 6000;

var SSE_LINK=null;
var SSE_PK=null;
var sse = null;
var clientId="";
var errorToastTimeout = null;

var isCacheEnabled=('caches' in window);

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
    const toastContainer = document.getElementById('toast-container');
    toastContainer.innerHTML= toastHTML;
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