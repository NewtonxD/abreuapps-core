
sse = null;
clientId="";

SSE_LINK="/see/prd";
SSE_PK="id";
SSE_FK=null;

function createTableRow(d) {
    return `<tr data-id="${d.id}"><th>${d.id}</th><td>${d.tit}</td><td>${d.empresa_dat}</td><td>${d.act?'Activo':'Inactivo'}</td></tr>` ;
}

$(function(){
    if (sse === null || sse === undefined) {
        createEventSource();
    }
});