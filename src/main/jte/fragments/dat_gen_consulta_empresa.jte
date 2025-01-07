@import abreuapps.core.control.general.DatoDTO
@import java.util.List

@param boolean dat_gen_consulta_empresa
@param boolean dat_gen_registro_empresa
@param String msg
@param Boolean status
@param List<DatoDTO> datos


@template.shared.consulta(
    titulo = "Empresas",
    id_registro = "dat_gen_registro_empresa",
    permiso_consulta = dat_gen_consulta_empresa,
    permiso_registro = dat_gen_registro_empresa,
    msg = msg, status = status,
    content = @`
        <thead class="bg-secondary text-white">
            <tr>
                <th scope="col">Empresa</th>
                <th scope="col">Descripción</th>
                <th scope="col">Estado</th>
            </tr>
        </thead>
        <tbody>
            @for(var d : datos)
                <tr data-id=${d.dat()}>
                    <th scope="row">${d.dat()}</th>
                    <td>${d.dsc()}</td>
                    <td>${d.act()?"Activo":"Inactivo"}</td>
                </tr>
            @endfor
        </tbody>
    `,
    scripts_content = @`
        <script src="/content/js/tb_init.js"></script>
        <script src="/content/js/dtgnr/es_emp742520.js"></script>
        @if(dat_gen_registro_empresa)
            <script src="/content/js/dtgnr/emp742520.js"></script>
        @endif
    `
)
