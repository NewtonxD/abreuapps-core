@import abreuapps.core.control.transporte.RutaDTO
@import java.util.List

@param boolean trp_rutas_consulta
@param boolean trp_rutas_registro
@param String msg
@param Boolean status
@param List<RutaDTO> rutas

@template.shared.consulta(
    titulo = "Rutas",
    id_registro = "trp_rutas_registro",
    permiso_consulta = trp_rutas_consulta,
    permiso_registro = trp_rutas_registro,
    msg = msg, status = status,
    content = @`
        <thead class="bg-secondary text-white">
        <tr>
            <th scope="col">Ruta</th>
            <th scope="col">Sale desde: </th>
            <th scope="col">Se dirige a: </th>
            <th scope="col">Activo</th>
        </tr>
        </thead>
        <tbody>
        @for(var d:rutas)
            <tr data-id=${d.rta()}>
                <th scope="row">${d.rta()}</th>
                <td>${d.loc_ini()}</td>
                <td>${d.loc_fin()}</td>
                <td>${d.act()?"Activo":"Inactivo"}</td>
            </tr>
        @endfor
        </tbody>
    `,
    scripts_content = @`
        <script src="/content/js/tb_init.js"></script>
        @if(trp_rutas_registro)<script src="/content/js/trp/rta5984502.js"></script>@endif
        <script src="/content/js/trp/es_rta5984502.js"></script>
    `
)