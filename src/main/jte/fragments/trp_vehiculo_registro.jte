@import abreuapps.core.control.transporte.Vehiculo
@import abreuapps.core.control.utils.DateUtils
@import abreuapps.core.control.general.DatoDTO
@import java.util.List


@param boolean trp_vehiculo_registro
@param DateUtils dateUtils
@param Vehiculo vehiculo
@param boolean last_loc
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
        <input type="text" @if(!update) name="placa" @endif value="${vehiculo.getPlaca()}" disabled="${update?"disabled":""}" placeholder=" ... " class="form-control" required aria-describedby="basic-addon4">
        @if(update) <input type="text" style="display: none; width: 1px;height: 1px;" id="Placa" name="placa" value="${vehiculo.getPlaca()}"> @endif
    </div>

    <div class="mb-3 col-12 col-md-6 col-lg-4">
        <label for="grupo" class="form-label text-muted">Color:</label>
        <select class="form-select custom-select" name="color" id="color" required>
            <option value="0" disabled>--Seleccione--</option>
            @for(var c : color)
                @if(c.act())
                    <option value="${c.dat()}"
                        @if(update)
                            selected="${( vehiculo.getColor().getDato().equals(c.dat()) ) ? "selected" : "" }"
                        @endif
                    > c.dat() </option>
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
                    <option value="${t.dat()}"
                        @if(update)
                            selected="${( vehiculo.getTipo_vehiculo().getDato().equals(t.dat()) ) ? "selected" : "" }"
                        @endif
                    >${t.dat()}</option>
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
                    <option value="${e.dat()}"
                            @if(update)
                                selected="${( vehiculo.getEstado().getDato().equals(e.dat()) ) ? "selected" : "" }"
                            @endif
                    >${e.dat()}</option>
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
                    <option value="${m.dat()}"
                            @if(update)
                                selected="${( vehiculo.getMarca().getDato().equals(m.dat()) ) ? "selected" : "" }"
                            @endif
                    >${m.dat()}</option>
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
                    <option value="${m.dat()}"
                            @if(update)
                                selected="${( vehiculo.getModelo().getDato().equals(m.dat()) ) ? "selected" : "" }"
                            @endif
                    >${m.dat()}</option>
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

    <input  type="datetime" name="fecha_actualizacionn" style="display: none; width: 1px;height: 1px;" value="${dateUtils.DateToFormato2(vehiculo.getFecha_actualizacion())}">

    @if(update && last_loc)
        <div class="col-12  d-flex justify-content-center text-center ">
            <h6>Fecha Ultima Ubicación: <span id="date_last_loc"></span></h6>
        </div>
        <div class="col-12 d-flex justify-content-center">
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
    <script src="/content/js/fetch_cache.js"></script>
    <script src="/content/js/trp/svd_vhl574702.js"></script>
    @if(update && last_loc)
        <script src="/content/js/lib/leaflet.js"></script>
        <script src="/content/js/trp/vhl544702lastloc.js"></script>
    @endif
    `
)
