@import abreuapps.core.control.usuario.UsuarioDTO
@import java.util.List

@param Boolean usr_mgr_principal
@param Boolean usr_mgr_registro
@param String msg
@param Boolean status
@param List<UsuarioDTO> usuarios

@template.shared.consulta(
    titulo = "Usuarios",
    id_registro = "usr_mgr_registro",
    permiso_consulta = usr_mgr_principal,
    permiso_registro = usr_mgr_registro,
    msg = msg,
    status = status,
    content = @`
        <thead class="bg-secondary text-white">
        <tr>
            <th scope="col">Usuario</th>
            <th scope="col">Correo</th>
            <th scope="col">Clave Vencida</th>
            <th scope="col">Estado</th>
        </tr>
        </thead>
        <tbody>
        @for(var u: usuarios)
            <tr data-id="${u.usr()}">
                <th>${u.usr()}</th>
                <td>${u.mail()}</td>
                <td>${u.pwd_chg()?"Si":"No"}</td>
                <td>${u.act()?"Activo":"Inactivo"}</td>
            </tr>
        @endfor
        </tbody>
    `,
    scripts_content = @`
        <script src="/content/js/tb_init.js"></script>
        @if(usr_mgr_registro) <script src="/content/js/usrmgr/usr782360.js"></script> @endif
        <script src="/content/js/usrmgr/es_usr782360.js"></script>
    `
)

