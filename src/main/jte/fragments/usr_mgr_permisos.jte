@import abreuapps.core.control.usuario.Usuario

@param Boolean usr_mgr_registro
@param Usuario usuario

@template.shared.registro(
    titulo = "Editar Permisos [ "+usuario.getUsername()+" ]",
    id_consulta = "usr_mgr_principal",
    permiso_registro = usr_mgr_registro,
    update = true,
    content = @`
        <div class="row me-4 pe-4  d-flex justify-content-end">
            <div class="mb-3 col-auto">
                <button type="button" class="btn btn-primary" onclick="saveUsuarioAccess()" id="BtnSave"><b><i class="fa fa-save"></i>&nbsp;Guardar</b></button>
            </div>
        </div>
        <div class="row m-4  d-flex justify-content-center">
            <input type="hidden" name="idUsuario" value="${usuario.getUsername()}" id="idUsuario"/>
            <div class="mb-3 col-auto">
                <button type="button" class="btn btn-primary" id="BtnMrkAll" onclick="checkAll()"><b><i class="fa fa-check-square"></i>&nbsp;Marcar Todos</b></button>
            </div>
            <div class="mb-3 col-auto">
                <button type="button" class="btn btn-danger" id="BtnMrkNone" onclick="checkNone()"><b><i class="fa fa-square-o"></i>&nbsp;Desmarcar Todos</b></button>
            </div>
            <div class="mb-3 col-auto">
                <input type="text" id="idUsuarioCopy" class="form-control"/>
            </div>
            <div class="mb-3 col-auto">
                <button type="button" class="btn btn-secondary" id="BtnCopy" onclick="usuarioCopyAccess()"><b><i class="fa fa-users"></i>&nbsp;Copiar</b></button>
            </div>
        </div>

        <div class="container">

        </div>
        <div class="row mt-4 mb-4">
            <div class="col-12 special-access"></div>
        </div>
    `,
    scripts_content = @`
        <script src="/content/js/usrmgr/acc812653.js"></script>
        <script src="/content/js/lib/tree.js"></script>
    `,
    saveButton = false
)

