
sse = null;

clientId="";

SSE_LINK="/see/dtgnr";
SSE_PK="dat";
SSE_FK="Empresas"

function createTableRow(d) {
    return `<tr data-id="${d.dat}"> <th>${d.dat}</th> <td>${d.dsc}</td> <td>${d.act?'Activo':'Inactivo'}</td></tr>`;
}

$(function(){
    if (sse === null || sse === undefined) {
        createEventSource();
    }
});