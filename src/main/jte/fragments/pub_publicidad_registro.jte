@import abreuapps.core.control.general.Publicidad
@import abreuapps.core.control.utils.DateUtils
@import java.util.List
@import abreuapps.core.control.general.DatoDTO

@param Boolean pub_publicidad_registro
@param DateUtils dateUtils
@param Publicidad publicidad
@param List<DatoDTO> empresas


!{boolean update =  (publicidad.getId()!=null) ;}


@template.shared.registro(
    titulo = "Publicidad",
    id_consulta = "pub_publicidad_consulta",
    permiso_registro = pub_publicidad_registro,
    update = update,
    content = @`
        @if(update)
            <input type="text" style="display: none; width: 1px;height: 1px;" id="idPublicidad" name="id" value="${publicidad.getId()}" >
            <input  type="datetime" name="fecha_actualizacionn" style="display: none; width: 1px;height: 1px;" value="${dateUtils.DateToFormato2(publicidad.getFecha_actualizacion())}">
        @endif


        <div class="mb-3 col-12 col-md-6">
            <label for="nom-dato" class="form-label text-muted">Título:</label>
            <input type="text" name="titulo" id="titulo" value="${publicidad.getTitulo()}" required class="form-control" placeholder="..."/>
        </div>

        <div class="mb-3 col-12 col-md-6">
            <label for="nom-dato" class="form-label text-muted">Empresa:</label>
            <select required class="form-select custom-select" name="empresa" id="empresa">
                <option value="0" selected disabled>--Seleccione--</option>
                @for(var g : empresas )
                    @if(g.act())

                        @if(!update)
                            <option value="${g.dat()}">${g.dat()}</option>
                        @else
                            <option
                                    value="${g.dat()}"
                                    selected="${ g.dat().equals(publicidad.getEmpresa().getDato()) }"
                            >${g.dat()}</option>

                        @endif

                    @endif
                @endfor
            </select>
        </div>

        <div class="mb-3 col-12 col-md-5">
            <label for="descripcion" class="form-label text-muted">Fecha Inicial:</label>

            @if(update)
                <input required class="form-control" value="${dateUtils.dateFormat.format(publicidad.getFecha_inicio())}" name="fecha_inicio" id="startDate" type="date"/>
            @else
                <input required class="form-control" name="fecha_inicio" id="startDate" type="date"/>
            @endif

            <div class="alert alert-warning" role="alert" id="dateDifference" style="display: none;" ></div>
        </div>
        <div class="mb-3 mt-3 col-12 col-md-2 text-center">
            <p class="text-wrap m-0">Total de Dias <br>de Vigencia: <span id="dias">0</span></p>
        </div>
        <div class="mb-3 col-12 col-md-5">
            <label for="descripcion" class="form-label text-muted">Fecha Final:</label>
            @if(update)
                <input required name="fecha_fin" value="${dateUtils.dateFormat.format(publicidad.getFecha_fin())}"  class="form-control" id="endDate" type="date"/>
            @else
                <input required name="fecha_fin" class="form-control" id="endDate" type="date"/>
            @endif
        </div>

        <div class="mb-3 col-12">
            <label for="nom-dato" class="form-label text-muted">Link de Acceso:</label>
            <input type="text" required name="link_destino" value="${publicidad.getLink_destino()}" placeholder="https://www.ejemplo.com" class="form-control"/>
        </div>

        <div class="mb-3 col-12">
            <label for="nom-dato" class="form-label text-muted">Video o Imagen:</label>
            @if(!update)
                <div class="alert alert-warning" role="alert" id="fileInfo" style="display: none;" ></div>
                <input type="file" class="form-control" id="fileInput" required accept="video/mp4,image/jpeg,image/webp,image/png">
            @endif
            <input type="text" style="display: none;" value="${publicidad.getImagen_video_direccion()}" name="imagen_video_direccion" id="archivo" class="form-control"/>
            <div id="previewContainer" class="d-flex justify-content-center" style="display: none;">
                <div id="preview" class="mt-2"></div>
            </div>
        </div>

        <div class="mb-3 col-12">
            <label for="descripcion" class="form-label text-muted">Descripción:</label>
            <textarea class="form-control" name="descripcion" rows="2" maxlength="255">${publicidad.getDescripcion()}</textarea>
        </div>

        <div class="col-6 d-flex justify-content-start">
            <div class="form-check">
                <input class="form-check-input" type="checkbox"  checked="${ !update || publicidad.isActivo() }" name="activo" id="activo">
                <label class="form-check-label text-muted" for="activo">Activo</label>
            </div>
        </div>
    `,
    scripts_content = @`
        <script src="/content/js/pub/pub804952.js"></script>
        <script src="/content/js/pub/svd_pub794851.js"></script>
        @if(update)
            <script src="/content/js/pub/pub825153.js"></script>
        @endif
    `
)
