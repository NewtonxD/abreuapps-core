
sse = null;
clientId="";

SSE_LINK="/see/vhl";
SSE_PK="pl";

function createTableRow(d) {
    return `<tr data-id="${d.pl}"><th>${d.pl}</th><td>${d.marca_dat + ' ' + d.modelo_dat}</td><td>${d.color_dat}</td><td>${d.estado_dat}</td><td>${d.act?'Activo':'Inactivo'}</td></tr>`;
}

$(function(){
    if (sse === null || sse === undefined) {
        createEventSource();
    }
});