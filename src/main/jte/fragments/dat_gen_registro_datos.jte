@import abreuapps.core.control.general.DatoDTO
@import abreuapps.core.control.general.Dato
@import abreuapps.core.control.utils.DateUtils
@import java.util.List

@param boolean dat_gen_registro_datos
@param Boolean update
@param DateUtils dateUtils
@param List<DatoDTO> grupos
@param Dato dato

<main id="content-page">
    @if(dat_gen_registro_datos)
        <div class="container-fluid px-4">
            <div class="row d-flex align-items-center justify-content-center mt-5 mb-5">
                <div class="card col-12 col-md-10 col-lg-8">
                    <div class="card-body mt-3 mb-3">
                        <div class="row">
                            <div class="col-12 d-flex align-items-start justify-content-start">
                                <button type="submit" class="btn btn-sm btn-secondary"
                                        onclick="$(function () {
                                                event.preventDefault();
                                                cargar_contenido('dat_gen_consulta_datos');
                                            });">
                                    <b><i class="fa fa-chevron-left"></i></b>
                                </button>&nbsp;&nbsp;
                                <h4>
                                    Datos Generales
                                    <i class="fa fa-${!update?"plus":"pencil"}"></i>
                                </h4>
                            </div>
                        </div>
                        <form class="form-group" id="form-guardar">
                            <div class="row">
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
                                    <textarea class="form-control" id="descripcion" name="descripcion" rows="3" required maxlength="255">
                                        ${update?dato.getDescripcion():""}
                                    </textarea>
                                </div>
                                <div class="col-6 d-flex justify-content-start">
                                    <div class="form-check">
                                        <input class="form-check-input" type="checkbox"  checked="${!update || dato.isActivo()}" name="activo" id="activo">
                                        <label class="form-check-label text-muted" for="activo">Activo</label>
                                    </div>
                                </div>
                                <div class="col-6 d-flex justify-content-end">

                                    <button type="submit" class="btn btn-primary"> 
                                        @if(!update)
                                            <b><i class="fa fa-save"></i>Guardar</b>
                                        @else
                                            <b><i class="fa fa-pencil"></i>Editar</b>
                                        @endif
                                    </button>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
        <script src="/content/js/dtgnr/svd_dt742520.js"></script>

    @else
        @template.error.403()
    @endif
</main>