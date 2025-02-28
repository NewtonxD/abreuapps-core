@import abreuapps.core.control.general.Dato
@import abreuapps.core.control.utils.DateUtils

@param Boolean dat_gen_registro_empresa
@param Boolean update
@param DateUtils dateUtils
@param Dato dato

@template.shared.registro(
    titulo = "Empresa",
    id_consulta = "dat_gen_consulta_empresa",
    permiso_registro = dat_gen_registro_empresa,
    update = update,
    content = @`
        @if(!update)
            <div class="mb-3 col-12 col-md-6">
                <label for="nom-dato" class="form-label text-muted">Nombre del Dato:</label>
                <input type="text" id="nom-dato" name="dato" placeholder=" ... " class="form-control" required aria-describedby="basic-addon4">
            </div>

            <div class="mb-3 col-12 col-md-6" style="display: none;">
                <label for="grupo" class="form-label text-muted">Grupo:</label>
                <select class="form-select custom-select" id="dato_padre" name="dato_padre" required>
                    <option value="Empresas" selected>Empresas</option>
                </select>
            </div>
        @else
            <input  type="datetime" name="fecha_actualizacionn" style="display: none; width: 1px;height: 1px;" value="${dateUtils.DateToFormato2(dato.getFecha_actualizacion())}">
            <input  type="text" name="dato_padre" style="display: none; width: 1px;height: 1px;" value="${dato.getDato_padre()}" required>
            <input type="text" style="display: none; width: 1px;height: 1px;" value="${dato.getDato()}" name="dato" required>

            <div class="mb-3 col-12 col-md-6">
                <label for="nom-dato" class="form-label text-muted">Nombre de la empresa:</label>
                <input type="text" id="nom-dato" disabled value="${dato.getDato()}" placeholder=" ... " class="form-control" aria-describedby="basic-addon4">
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
        <script src="/content/js/dtgnr/svd_emp742520.js"></script>
    `
)
