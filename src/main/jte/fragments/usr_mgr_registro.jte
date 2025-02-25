@import abreuapps.core.control.usuario.Usuario
@import abreuapps.core.control.general.Persona
@import abreuapps.core.control.utils.DateUtils
@import abreuapps.core.control.general.DatoDTO
@import java.util.List

@param Boolean usr_mgr_registro
@param DateUtils dateUtils
@param Boolean configuracion
@param Usuario user
@param Persona persona
@param List<DatoDTO> sexo
@param List<DatoDTO> sangre

@param boolean update

@template.shared.registro(
    titulo = (configuracion ? "Editar Mi" : "" ) +" Usuario",
    id_consulta = "usr_mgr_principal",
    permiso_registro = usr_mgr_registro,
    update = update,
    backButton = !configuracion,
    content = @`
        <section class="mt-3 mb-3 me-4 ms-4">
            <input type="text" id="original_usuario" value="${user.getUsername()}" style="display: none; width: 1px;height: 1px;" >
            <input type="text" id="original_correo" value="${user.getCorreo()}" style="display: none; width: 1px;height: 1px;"  >
            <input type="text" id="original_cedula" value="${persona.getCedula()}" style="display: none; width: 1px;height: 1px;" >
            <input type="text" id="update" value="${update}" style="display: none; width: 1px;height: 1px;" >
            @if(update)
                <article class="pt-2">
                    <div class="row">
                        <div class="mb-3 col-12 col-md-6 col-lg-4 d-flex justify-content-center">
                            <button type="button" class="btn btn-danger" id="BtnResetPwd"><b><i class="fa fa-rotate-right"></i>&nbsp;Reestablecer Clave</b></button>
                        </div>
                        <div class="mb-3 col-12 col-md-6 col-lg-4 d-flex justify-content-center">
                            <button type="button" class="btn btn-primary" id="BtnCloseSess"><b><i class="fa fa-sign-out"></i>&nbsp;Cerrar Sesión</b></button>
                        </div>
                        @if(!configuracion)
                            <div class="mb-3 col-12 col-md-6 col-lg-4 d-flex justify-content-center">
                                <button type="button" class="btn btn-success" id="BtnEditAcc"><b><i class="fa fa-users"></i>&nbsp;Editar Permisos</b></button>
                            </div>
                        @endif
                    </div>
                </article>
            @endif
            <article class="pt-2">
                <h5>Información del Usuario</h5>
                <div class="row" id="inf_usuario">
                    <div class="col-12 mt-2 mb-2">
                        <div class="alert alert-warning alert-correo" role="alert" style="display:none;">
                            <span>Correo en uso. Verifique y vuelva a intentarlo.</span>
                        </div>
                        <div class="alert alert-warning alert-usuario" role="alert" style="display:none;">
                            <span>Usuario en uso. Verifique y vuelva a intentarlo.</span>
                        </div>
                    </div>
                    @if(update)
                        <input  type="datetime" name="fecha_actualizacionn" style="display: none; width: 1px;height: 1px;" value="${dateUtils.DateToFormato2(user.getFecha_actualizacion())}">
                        <input  type="number" class="idusuario" name="id" id="id" value="${user.getId()}" style="display: none; width: 1px;height: 1px;">
                    @endif
                    <div class="mb-3 col-12 col-md-6">
                        <label for="usuario" class="form-label text-muted">Usuario:</label>
                        <input type="text" name="username" id="username" value="${user.getUsername()}" placeholder=" ... " class="form-control" required aria-describedby="basic-addon4">
                    </div>

                    <div class="mb-3 col-12 col-md-6">
                        <label for="correo" class="form-label text-muted">Correo:</label>
                        <input type="email" name="correo" id="correo" value="${user.getCorreo()}" placeholder=" ... " class="form-control" required aria-describedby="basic-addon4">
                    </div>

                    <div class="col-12 d-flex justify-content-start">
                        <div class="form-check">
                            <input class="form-check-input" type="checkbox"  checked="${ !update || user.isActivo() }" name="activo" id="activo">
                            <label class="form-check-label text-muted" for="activo">Activo</label>
                        </div>
                    </div>


                </div>

            </article>
            <article class="pt-2">
                <h5>Información Personal &nbsp;<button type="button" class="btn collapsed" data-bs-toggle="collapse" data-bs-target="#inf_personal"><i class="fa fa-angle-down"></i></button></h5>
                <div class="row collapse" id="inf_personal">
                    @template.shared.info_personal(persona = persona, dateUtils = dateUtils, update = update, sexo = sexo, sangre = sangre)
                </div>
            </article>
        </section>

        <div class="row pt-2 pb-2 me-4 ms-4  d-flex justify-content-end align-items-center">
            <div class="col-12 mt-2 mb-2">
                <div class="alert alert-warning alert-pwd" role="alert" style="display:none;">
                    <span>Contraseña incorrecta. Verifique e intentelo de nuevo.</span>
                </div>
            </div>
            <div class="col-12 col-md-6 d-flex justify-content-end">
                @if(configuracion)
                    <input
                            class="form-control"
                            id="password"
                            type="password"
                            placeholder="Contraseña"
                            required="required"
                    />
                @endif
            </div>
        </div>
    `,
    scripts_content = @`
        <script src="/content/js/lib/input-formats.js"></script>
        @if(configuracion)
            <script src="/content/js/usrmgr/svd_usr802562.js"></script>
        @else
            <script src="/content/js/usrmgr/svd_usr782360.js"></script>
        @endif
    `
)