@import abreuapps.core.control.transporte.Vehiculo
@import abreuapps.core.control.utils.DateUtils
@import abreuapps.core.control.general.DatoDTO
@import java.util.List


@param Boolean trp_vehiculo_registro
@param DateUtils dateUtils
@param Vehiculo vehiculo
@param Boolean last_loc
@param List<DatoDTO> color
@param List<DatoDTO> tipo_vehiculo
@param List<DatoDTO> estado
@param List<DatoDTO> marca
@param List<DatoDTO> modelo



!{boolean update = vehiculo.getPlaca()!=null;}

@template.shared.registro(
    titulo = "Vehiculo",
    id_consulta = "trp_vehiculo_consulta",
    permiso_registro = trp_vehiculo_registro,
    update = update,
    content = @`
    <div class="mb-3 col-12 col-md-6 col-lg-4">
        <label for="placa" class="form-label text-muted">Placa:</label>
        @if(update)
            <input type="text" style="display: none; width: 1px;height: 1px;" id="Placa" name="placa" value="${vehiculo.getPlaca()}">
        @endif
        <input type="text" name="${!update?"placa":""}" value="${vehiculo.getPlaca()}" disabled="${update}" placeholder=" ... " class="form-control" required aria-describedby="basic-addon4">
    </div>

    <div class="mb-3 col-12 col-md-6 col-lg-4">
        <label for="grupo" class="form-label text-muted">Color:</label>
        <select class="form-select custom-select" name="color" id="color" required>
            <option value="0" disabled>--Seleccione--</option>
            @for(var c : color)
                @if(c.act())
                    @if(update)
                        <option
                                value="${c.dat()}"
                                selected="${vehiculo.getColor().getDato().equals(c.dat())}"
                        >${c.dat()}</option>
                    @else
                        <option value="${c.dat()}">${c.dat()}</option>
                    @endif
                @endif
            @endfor
        </select>
    </div>

    <div class="mb-3 col-12 col-md-6 col-lg-4">
        <label for="grupo" class="form-label text-muted">Tipo:</label>
        <select class="form-select custom-select" name="tipo_vehiculo" id="tipo_vehiculo" required>
            <option value="0" disabled>--Seleccione--</option>
            @for(var t:tipo_vehiculo)
                @if(t.act())

                    @if(update)
                        <option value="${t.dat()}"
                                selected="${ vehiculo.getTipo_vehiculo().getDato().equals(t.dat()) }"
                        >${t.dat()}</option>
                    @else
                        <option value="${t.dat()}">${t.dat()}</option>
                    @endif

                @endif
            @endfor
        </select>
    </div>

    <div class="mb-3 col-12 col-md-6 col-lg-4">
        <label for="grupo" class="form-label text-muted">Estado:</label>
        <select class="form-select custom-select" name="estado" id="estado" required>
            <option value="0" disabled>--Seleccione--</option>
            @for(var e:estado)
                @if(e.act())
                    @if(update)
                        <option
                            value="${e.dat()}"
                            selected="${ vehiculo.getEstado().getDato().equals(e.dat()) }"
                        >${e.dat()}</option>
                    @else
                        <option value="${e.dat()}">${e.dat()}</option>
                    @endif
                @endif
            @endfor
        </select>
    </div>

    <div class="mb-3 col-12 col-md-6 col-lg-4">
        <label for="grupo" class="form-label text-muted">Marca:</label>
        <select class="form-select custom-select" name="marca" id="marca" required>
            <option value="0" disabled>--Seleccione--</option>
            @for(var m:marca)
                @if(m.act())

                    @if(update)
                        <option
                            value="${m.dat()}"
                            selected="${ vehiculo.getMarca().getDato().equals(m.dat()) }"
                        >${m.dat()}</option>
                    @else
                        <option value="${m.dat()}">${m.dat()}</option>
                    @endif

                @endif
            @endfor
        </select>
    </div>

    <div class="mb-3 col-12 col-md-6 col-lg-4">
        <label for="grupo" class="form-label text-muted">Módelo:</label>
        <select class="form-select custom-select" name="modelo" id="modelo" required>
            <option value="0" disabled>--Seleccione--</option>
            @for(var m:modelo)
                @if(m.act())

                    @if(update)
                        <option
                            value="${m.dat()}"
                            selected="${ vehiculo.getModelo().getDato().equals(m.dat()) }"
                        >${m.dat()}</option>
                    @else
                        <option value="${m.dat()}">${m.dat()}</option>
                    @endif

                @endif
            @endfor
        </select>
    </div>

    <div class="mb-3 col-12 col-md-6 col-lg-4">
        <label for="placa" class="form-label text-muted">Año de Fabricación:</label>
        <input type="number" name='anio_fabricacion' value="${vehiculo.getAnio_fabricacion()}" placeholder=" ... " class="form-control" required aria-describedby="basic-addon4">
    </div>

    <div class="mb-3 col-12 col-md-6 col-lg-4">
        <label for="placa" class="form-label text-muted">Capacidad Máxima (Pasajeros):</label>
        <input type="number" name='capacidad_pasajeros' value="${vehiculo.getCapacidad_pasajeros()}" placeholder=" ... " class="form-control" required aria-describedby="basic-addon4">

    </div>

    @if(update)
        <input  type="datetime" name="fecha_actualizacionn" style="display: none; width: 1px;height: 1px;" value="${dateUtils.DateToFormato2(vehiculo.getFecha_actualizacion())}">
    @endif

    @if(update && last_loc)
        <div class="col-12  d-flex justify-content-center text-center ">
            <h6>Fecha Ultima Ubicación: <span id="date_last_loc"></span></h6>
        </div>
        <div class="col-12 d-flex justify-content-center pb-2">
            <div id="map" class="map-regular"></div>
        </div>
    @endif

    <div class="col-6 d-flex justify-content-start mt-2">
        <div class="form-check">
            <input class="form-check-input" type="checkbox"  checked="${ !update || vehiculo.isActivo() }" name="activo" id="activo">
            <label class="form-check-label text-muted" for="activo">Activo</label>
        </div>
    </div>
    `,
    scripts_content = @`
    <script src="/content/js/trp/svd_vhl574702.js"></script>
    @if(update && last_loc)
        <script src="/content/js/lib/leaflet.js"></script>
        <script src="/content/js/fetch_cache.js"></script>
        <script src="/content/js/trp/vhl544702lastloc.js"></script>
    @endif
    `
)
