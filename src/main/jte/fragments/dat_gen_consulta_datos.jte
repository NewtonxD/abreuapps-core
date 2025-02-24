@import abreuapps.core.control.general.DatoDTO
@import java.util.List

@param Boolean dat_gen_consulta_datos
@param Boolean dat_gen_registro_datos
@param String msg
@param Boolean status
@param List<DatoDTO> datos


@template.shared.consulta(
    titulo = "Datos Generales",
    id_registro = "dat_gen_registro_datos",
    permiso_consulta = dat_gen_consulta_datos,
    permiso_registro = dat_gen_registro_datos,
    msg = msg, status = status,
    content = @`
        <thead class="bg-secondary text-white">
            <tr>
                <th scope="col">Dato</th>
                <th scope="col">Descripción</th>
                <th scope="col">Grupo</th>
                <th scope="col">Estado</th>
            </tr>
        </thead>
        <tbody>
            @for(var d : datos)
                <tr data-id="${d.dat()}">
                    <th scope="row">${d.dat()}</th>
                    <td>${d.dsc()}</td>
                    <td>${d.fat_dat()}</td>
                    <td>${d.act()?"Activo":"Inactivo"}</td>
                </tr>
            @endfor
        </tbody>
    `,
    scripts_content = @`
        <script src="/content/js/tb_init.js"></script>
        <script src="/content/js/dtgnr/es_dt742520.js"></script>
        @if(dat_gen_registro_datos)
            <script src="/content/js/dtgnr/dt742520.js"></script>
        @endif
    `
)
