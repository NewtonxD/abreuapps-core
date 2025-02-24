@import abreuapps.core.control.general.DatoDTO
@import abreuapps.core.control.general.Dato
@import abreuapps.core.control.utils.DateUtils
@import java.util.List

@param Boolean dat_gen_registro_datos
@param Boolean update
@param DateUtils dateUtils
@param List<DatoDTO> grupos
@param Dato dato

@template.shared.registro(
    titulo = "Datos Generales",
    id_consulta = "dat_gen_consulta_datos",
    permiso_registro = dat_gen_registro_datos ,
    update = update,
    content = @`
        @if(!update)
            <div class="mb-3 col-12 col-md-6">
                <label for="nom-dato" class="form-label text-muted">Nombre del Dato:</label>
                <input type="text" id="nom-dato" name="dato" placeholder=" ... " class="form-control" required aria-describedby="basic-addon4">
            </div>
            <div class="mb-3 col-12 col-md-6">
                <label for="grupo" class="form-label text-muted">Grupo:</label>
                <select class="form-select custom-select" id="dato_padre" name="dato_padre" required>
                    <option value="0" selected disabled>--Seleccione--</option>
                    @for(var g : grupos)
                        @if(g.act())
                            <option value="${g.dat()}" >${g.dat()}</option>
                        @endif
                    @endfor
                </select>
            </div>
        @else
            <input  type="datetime" name="fecha_actualizacionn" style="display: none; width: 1px;height: 1px;" value="${dateUtils.DateToFormato2(dato.getFecha_actualizacion())}">
            <input  type="text" name="dato_padre" style="display: none; width: 1px;height: 1px;" value="${dato.getDato_padre()}" required>
            <input type="text" style="display: none; width: 1px;height: 1px;" value="${dato.getDato()}" name="dato" required>

            <div class="mb-3 col-12 col-md-6">
                <label for="nom-dato" class="form-label text-muted">Nombre del Dato:</label>
                <input type="text" id="nom-dato" disabled value="${dato.getDato()}" placeholder=" ... " class="form-control" aria-describedby="basic-addon4">
            </div>

            <div class="mb-3 col-12 col-md-6">
                <label for="grupo" class="form-label text-muted">Grupo:</label>
                <select class="form-select custom-select" disabled >
                    @if(dato.getDato_padre().equals(null))
                        <option value="0" selected disabled>--Seleccione--</option>
                    @else
                        <option value="${dato.getDato_padre()}">${dato.getDato_padre()}</option>
                    @endif
                </select>
            </div>

        @endif

        <div class="mb-3 col-12">
            <label for="descripcion" class="form-label text-muted">Descripción:</label>
            <textarea class="form-control" id="descripcion" name="descripcion" rows="3" required maxlength="255">${update?dato.getDescripcion():""}</textarea>
        </div>
        <div class="col-6 d-flex justify-content-start">
            <div class="form-check">
                <input class="form-check-input" type="checkbox"  checked="${!update || dato.isActivo()}" name="activo" id="activo">
                <label class="form-check-label text-muted" for="activo">Activo</label>
            </div>
        </div>
    `,
    scripts_content = @`
        <script src="/content/js/dtgnr/svd_dt742520.js"></script>
    `
)