
sse = null;
clientId="";

SSE_LINK="/see/vhl_log";
SSE_PK="id";

function createTableRow(d) {
    return `<tr data-id="${d.id}">
            <th scope="row">${d.id}</th>
            <td>
                ${ (d.est_new==="Averiado" || d.sys) ? ` 
                    <div class="spinner-grow text-${d.est_new==="Averiado"?"danger":"warning"}" role="status">
                    <span class="visually-hidden">Loading...</span>
                    </div>`:''}
            </td><td>${d.pl}</td><td>${d.rta_new=='null'?'':d.rta_new}</td><td>${(d.sys?'Sistema - ':'')+d.est_new}</td><td>${(new Date(d.reg_dt)).toISOString().replace("T"," ")}</td></tr>`;
}

$(function(){
    if (sse === null || sse === undefined) {
        createEventSource();
    }
});


