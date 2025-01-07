@import abreuapps.core.control.transporte.Ruta
@import abreuapps.core.control.utils.DateUtils

@param boolean trp_rutas_registro
@param DateUtils dateUtils
@param Ruta ruta

!{boolean update = ruta.getRuta()!=null;}

@template.shared.registro(
    titulo = "Rutas",
    id_consulta = "trp_rutas_consulta",
    permiso_registro = trp_rutas_registro,
    update = update,
    content = @`
        <div class="mb-3 col-12 col-lg-8">
            <label for="ruta" class="form-label text-muted">Nombre de la Ruta:</label>
            @if(update)
                <input type="text" style="display: none; width: 1px;height: 1px;" id="Ruta" name="ruta" value="${ruta.getRuta()}">
            @endif
            <input type="text" name="ruta" disabled="${update?"disabled":""}" placeholder=" ... " class="form-control" required aria-describedby="basic-addon4">
            <input  type="datetime" name="fecha_actualizacionn" style="display: none; width: 1px;height: 1px;" value="${dateUtils.DateToFormato2(ruta.getFecha_actualizacion())}">
        </div>

        <div class="mb-3 col-12 col-lg-6">
            <label for="localizacion_inicial" class="form-label text-muted">Lugar donde inicia:</label>
            <input type="text" name="localizacion_inicial" placeholder=" ... " class="form-control" required aria-describedby="basic-addon4">
        </div>

        <div class="mb-3 col-12 col-lg-6">
            <label for="localizacion_final" class="form-label text-muted">Lugar donde termina:</label>
            <input type="text" name="localizacion_final" placeholder=" ... " class="form-control" required aria-describedby="basic-addon4">
        </div>

        <div class="col-12 text-start ">
            <h5>Ubicación:</h5>
            @if(!update) <label for="" class="form-label text-muted">Coloque las líneas de la ruta sobre la calle</label> @endif
        </div>
        <div class="mb-3 col-12 d-flex justify-content-center">
            <div id="map" class="map-regular"></div>
        </div>

        <button type="button" id="getPolylineData"  style="display: none; width: 1px;height: 1px;" ></button>

        <div class="col-6 d-flex justify-content-start">
            <div class="form-check">
                <input class="form-check-input" type="checkbox"  checked="${ !update || ruta.isActivo() }" name="activo" id="activo">
                <label class="form-check-label text-muted" for="activo">Activo</label>
            </div>
        </div>`
    , scripts_content = @`
        <script src="/content/js/lib/leaflet.js"></script>
        <script src="/content/js/lib/leaflet-geoman.min.js"></script>
        <script src="/content/js/fetch_cache.js"></script>
        <script src="/content/js/trp/rta5984502loc.js"></script>
        <script src="/content/js/trp/svd_rta5984502.js"></script>
    `
)



