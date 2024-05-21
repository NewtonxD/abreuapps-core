
sse = null;
clientId="";

SSE_LINK="/see/dtgrp";
SSE_PK="grp";

function createTableRow(d) {
    return `<tr data-id="${d.grp}"><th>${d.grp}</th><td>${d.dsc}</td><td>${d.act?'Activo':'Inactivo'}</td></tr>`;
}

$(function(){
    if (sse === null || sse === undefined) {
        createEventSource();
    }
});