
sse = null;
clientId="";

SSE_LINK="/see/vhl_log";
SSE_PK="id";
SSE_FK=null;

function createTableRow(d) {
    return `<tr data-id="${d.id}">
            <th scope="row">${d.id}</th>
            <td>
                ${ (d.est_new==="Averiado") ? `<img src="${SERVER_IP}/content/assets/img/car-crash-icon.webp" alt="alt" height="32" width="32"/>`:''}
                ${ (d.sys) ? `<img src="${SERVER_IP}/content/assets/img/bot-icon.webp" alt="alt" height="32" width="32"/>`:''}
            </td><td>${d.pl}</td><td>${(d.rta_new==='null' || d.rta_new===null)?'':d.rta_new}</td><td>${(d.sys?'Sistema - ':'')+d.est_new}</td><td>${(new Date(d.reg_dt)).toISOString().replace("T"," ")}</td></tr>`;
}

$(function(){
    if (sse === null || sse === undefined) {
        createEventSource();
    }
});


