@import abreuapps.core.control.general.ConfDTO
@import java.util.List

@param Boolean sys_configuracion
@param String msg
@param Boolean status
@param List<ConfDTO> conf

@template.shared.consulta(
    titulo = "Configuración de Sistema",
    id_registro = "",
    permiso_consulta = sys_configuracion,
    permiso_registro = false,
    msg = msg, status = status,
    table_content = false,
    content = @`
        <div class="row m-4 pb-4">
            <form id="form-guardar">
                @for(var c: conf)
                    <div class="col-auto mb-2 justify-content-start">
                        <label class="form-label" >- ${c.dsc()}:</label>
                        <input type="text" class="form-control" name="${c.cod()}" id="${c.cod()}" value="${c.val()}" />
                    </div>
                @endfor
                <div class="col-12 mb-2 justify-content-center">
                    <button type="submit" class="btn btn-primary"><i class="fa fa-save"></i> Guardar</button>
                </div>
            </form>
        </div>
    `,
    scripts_content = @`
        <script src="/content/js/conf/svd_conf345612.js"></script>
    `
)
