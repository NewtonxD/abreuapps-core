
sse = null;

clientId="";

SSE_LINK="/see/dtgnr";
SSE_PK="dat";

function createTableRow(d) {
    return `<tr data-id="${d.dat}"> <th>${d.dat}</th> <td>${d.dsc}</td> <td>${d.fat_dat}</td> <td>${d.act?'Activo':'Inactivo'}</td></tr>`;
}

$(function(){
    if (sse === null || sse === undefined) {
        createEventSource();
    }
});