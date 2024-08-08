
sse = null;
clientId="";

SSE_LINK="/see/pub";
SSE_PK="id";      

function createTableRow(d) {
    return `<tr data-id="${d.id}"><th>${d.id}</th><td>${d.tit}</td><td>${d.empresa_dat}</td><td>${d.act?'Activo':'Inactivo'}</td></tr>` ;
}

$(function(){
    if (sse === null || sse === undefined) {
        createEventSource();
    }
});