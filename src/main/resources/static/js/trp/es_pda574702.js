
sse = null;
clientId="";

SSE_LINK="/see/pda";
SSE_PK="id";
SSE_FK=null;      

function createTableRow(d) {
    return `<tr data-id="${d.id}"><th>${d.id}</th><td>${d.dsc}</td><td>${d.act?'Activo':'Inactivo'}</td></tr>` ;
}

$(function(){
    if (sse === null || sse === undefined) {
        createEventSource();
    }
});

