
//const SERVER_IP="http://192.168.100.76:8090";
const SERVER_IP="http://localhost:8090";
const TILE_API_IP=SERVER_IP+"/API/tiles/{z}/{x}/{y}";

var SSE_LINK=null;
var SSE_PK=null;
var sse = null;
var clientId="";
