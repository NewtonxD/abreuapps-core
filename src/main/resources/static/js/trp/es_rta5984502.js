
sse = null;
clientId="";

SSE_LINK="/sse/rta";
SSE_PK="rta";

function createTableRow(d) {
    return `<tr data-id="${d.rta}"><th>${d.rta}</th><td>${d.loc_ini}</td><td>${d.loc_fin}</td><td>${d.act?'Activo':'Inactivo'}</td></tr>`;
}

$(function(){
    if (sse === null || sse === undefined) {
        createEventSource();
    }
});

