@import abreuapps.core.control.transporte.Parada
@import abreuapps.core.control.utils.DateUtils

@param Boolean trp_paradas_registro
@param DateUtils dateUtils
@param Parada parada

!{boolean update = parada.getId()!=null;}

@template.shared.registro(
    titulo = "Paradas",
    id_consulta = "trp_paradas_consulta",
    permiso_registro = trp_paradas_registro,
    update = update,
    content = @`
        @if(update)
            <input  type="datetime" name="fecha_actualizacionn" style="display: none; width: 1px;height: 1px;" value="${dateUtils.DateToFormato2(parada.getFecha_actualizacion())}">
            <input type="text" style="display: none; width: 1px;height: 1px;" id="idParada" name="id" value="${parada.getId()}">
        @endif

        <div class="mb-3 col-12">
            <label for="descripcion" class="form-label text-muted">Descripción:</label>
            <textarea class="form-control" name="descripción" rows="2" required maxlength="255">${parada.getDescripción()}</textarea>
        </div>

        <div class="col-12 text-start ">
            <h5>Ubicación:</h5>
            @if(!update)
                <label for="" class="form-label text-muted">Trate de colocar la parada del lado de la acera que corresponde. Nunca encima de la calle.</label>
            @endif
        </div>
        <div class="mb-3 col-12 d-flex justify-content-center">
            <div id="map" class="map-regular"></div>
        </div>

        <div class="col-6 justify-content-center mb-3">
            <label for="latitud" class="form-label text-muted">Latitud:</label>
            <input type="number" class="form-control" name="latitud" value="${parada.getLatitud()}" step=".00000000000000001" required />
        </div>

        <div class="col-6 justify-content-center mb-3">
            <label for="longitud" class="form-label text-muted">Longitud:</label>
            <input type="number" class="form-control" name="longitud" value="${parada.getLongitud()}" step=".00000000000000001" required />
        </div>

        <div class="col-6 d-flex justify-content-start">
            <div class="form-check">
                <input class="form-check-input" type="checkbox"  checked="${ !update || parada.isActivo() }" name="activo" id="activo">
                <label class="form-check-label text-muted" for="activo">Activo</label>
            </div>
        </div>
    `,
    scripts_content = @`
        <script src="/content/js/trp/svd_pda574702.js"></script>
        <script src="/content/js/lib/leaflet.js"></script>
        <script src="/content/js/fetch_cache.js"></script>
        <script src="/content/js/trp/pda544702loc.js"></script>
    `
)
