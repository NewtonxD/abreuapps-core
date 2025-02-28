@import abreuapps.core.control.transporte.ParadaDTO
@import java.util.List

@param Boolean trp_paradas_consulta
@param Boolean trp_paradas_registro
@param String msg
@param Boolean status
@param List<ParadaDTO> paradas

@template.shared.consulta(
    titulo = "Paradas",
    id_registro = "trp_paradas_registro",
    permiso_consulta = trp_paradas_consulta,
    permiso_registro = trp_paradas_registro,
    msg = msg, status = status,
    content = @`
        <thead class="bg-secondary text-white">
        <tr>
            <th scope="col">ID</th>
            <th scope="col">Descripción</th>
            <th scope="col">Activo</th>
        </tr>
        </thead>
        <tbody>
            @for(var d: paradas)
                <tr data-id="${d.id()}">
                    <th scope="row">${d.id()}</th>
                    <td>${d.dsc()}</td>
                    <td>${d.act()?"Activo":"Inactivo"}</td>
                </tr>
            @endfor
        </tbody>
    `,
    scripts_content = @`
        <script src="/content/js/tb_init.js"></script>
        @if(trp_paradas_registro) <script src="/content/js/trp/pda574702.js"></script> @endif
        <script src="/content/js/trp/es_pda574702.js"></script>
    `
)
