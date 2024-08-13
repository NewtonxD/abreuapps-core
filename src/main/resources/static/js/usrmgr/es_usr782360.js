
sse = null;
clientId="";

SSE_LINK="/see/usrmgr";
SSE_PK="usr";
SSE_FK=null;

function createTableRow(d) {
    return `<tr data-id="${d.usr}"><th>${d.usr}</th><td>${d.mail}</td><td>${d.pwd_chg?'Si':'No'}</td><td>${d.act?'Activo':'Inactivo'}</td></tr>`;
}

$(function(){
    if (sse === null || sse === undefined) {
        createEventSource();
    }
});