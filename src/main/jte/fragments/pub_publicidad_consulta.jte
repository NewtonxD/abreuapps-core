@import abreuapps.core.control.general.PublicidadDTO
@import java.util.List

@param Boolean pub_publicidad_consulta
@param Boolean pub_publicidad_registro
@param String msg
@param Boolean status
@param List<PublicidadDTO> publicidades

@template.shared.consulta(
    titulo = "Publicidad",
    id_registro = "pub_publicidad_registro",
    permiso_consulta = pub_publicidad_consulta,
    permiso_registro = pub_publicidad_registro,
    msg = msg, status = status,
    content = @`
        <thead class="bg-secondary text-white">
        <tr>
            <th scope="col">ID</th>
            <th scope="col">Título</th>
            <th scope="col">Empresa</th>
            <th scope="col">Activo</th>
        </tr>
        </thead>
        <tbody>
        @for(var d:publicidades)
            <tr data-id="${d.id()}">
                <th scope="row">${d.id()}</th>
                <td>${d.tit()}</td>
                <td>${d.empresa_dat()}</td>
                <td>${d.act()?"Activo":"Inactivo"}</td>
            </tr>
        @endfor
        </tbody>
    `,
    scripts_content = @`
        <script src="/content/js/tb_init.js"></script>
        @if(pub_publicidad_registro) <script src="/content/js/pub/pub794851.js"></script> @endif
        <script src="/content/js/pub/es_pub794851.js"></script>
    `
)
