
//const SERVER_IP="http://192.168.100.76:8090";
const SERVER_IP="https://localhost:8090";
const TILE_API_IP=SERVER_IP+"/API/tiles/{z}/{x}/{y}";
const TILE_CACHE_NAME = 'tile-cache';

var SSE_LINK=null;
var SSE_PK=null;
var sse = null;
var clientId="";

var isCacheEnabled=('caches' in window);

function getRandomColor(){
    const r = Math.floor(Math.random() * 156) + 100;
    const g = Math.floor(Math.random() * 156) + 100;
    const b = Math.floor(Math.random() * 156) + 100; 
    return `rgba(${r},${g},${b},0.7)`; 
}