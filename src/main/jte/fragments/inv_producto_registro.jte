@import abreuapps.core.control.inventario.Producto
@import abreuapps.core.control.utils.DateUtils
@import java.util.List
@import abreuapps.core.control.general.DatoDTO

@param boolean inv_producto_registro
@param boolean update
@param DateUtils dateUtils
@param Producto producto
@param List<DatoDTO> categorias

<main  id="content-page">
    @if(inv_producto_registro)
        <div class="container-fluid px-4">
            <div class="row d-flex align-items-center justify-content-center mt-5 mb-5">
                <div class="card col-12 col-md-10 col-lg-8">
                    <div class="card-body mt-3 mb-3">
                        <div class="row mb-3">
                            <div class="col-12 d-flex align-items-start justify-content-start">
                                <button type="submit" class="btn btn-sm btn-secondary"
                                        onclick="$(function () {
                                                event.preventDefault();
                                                cargar_contenido('inv_producto_consulta');
                                            });">
                                    <b><i class="fa fa-chevron-left"></i></b>
                                </button>
                                &nbsp;&nbsp;<h4>Producto&nbsp;
                                    <i class="fa fa-${producto.getId()==null?"plus":"pencil"}"></i>
                                </h4>
                            </div>
                        </div>


                        <form class="form-group" id="form-guardar">
                            <div class="row">

                                @if(producto.getId()!=null)
                                    <input type="text" style="display: none; width: 1px;height: 1px;" id="idPublicidad" name="id" value="${producto.getId()}" placeholder=" ... " class="form-control" aria-describedby="basic-addon4">
                                @endif

                                <input  type="datetime" name="fecha_actualizacionn" style="display: none; width: 1px;height: 1px;" value="${dateUtils.DateToFormato2(producto.getFecha_actualizacion())}">

                                <div class="mb-3 col-12">
                                    <label for="nom-dato" class="form-label text-muted">Nombre:</label>
                                    <input type="text" name="nombre" value="${producto.getNombre()}" required class="form-control" placeholder="..."/>
                                </div>
                                
                                <div class="mb-3 col-12 col-md-6">
                                    <label for="nom-dato" class="form-label text-muted">Precio de Venta:</label>
                                    <input type="number" name="precio_venta" value="${producto.getPrecio_venta()}" required class="form-control" placeholder="..."/>
                                </div>
                                
                                <div class="mb-3 col-12 col-md-6">
                                    <label for="nom-dato" class="form-label text-muted">Categoria:</label>
                                    <select required class="form-select custom-select" name="categoria" >
                                        <option value="0" selected disabled>--Seleccione--</option>
                                        @for(var c:categorias)
                                            @if(c.act())
                                                <option
                                                        value="${c.dat()}"
                                                        @if(producto.getId()!=null)
                                                            selected="${( producto.getCategoria().getDato().equals(c.dat()) ) ? "selected" : "" }"
                                                        @endif
                                                >
                                                    ${c.dat()}
                                                </option>
                                            @endif

                                        @endfor
                                    </select> 
                                </div>
                                
                                <div class="mb-3 col-12">
                                    <label for="descripcion" class="form-label text-muted">Descripción:</label>
                                    <textarea class="form-control" name="descripcion" rows="2" maxlength="255">${producto.getDescripcion()}</textarea>
                                </div>
                                
                                <div class="mb-3 col-12">
                                    <label for="nom-dato" class="form-label text-muted">Imagen:</label>
                                    @if(producto.getId()==null)
                                        <div class="alert alert-warning" role="alert" id="fileInfo" style="display: none;" ></div>
                                        <input type="file" class="form-control" id="fileInput" required accept="video/mp4,image/jpeg,image/webp,image/png">
                                    @endif
                                    <input type="text" style="display: none;" name="foto" id="archivo" value="${producto.getFoto()}" class="form-control"/>
                                    <div id="previewContainer" class="d-flex justify-content-center" style="display: none;">
                                        <div id="preview" class="mt-2"></div>
                                    </div>
                                </div>

                                <div class="col-6 d-flex justify-content-start">
                                    <div class="form-check">
                                        <input class="form-check-input" type="checkbox"  checked="${producto.getId()==null || producto.isActivo()}" name="activo" id="activo">
                                            <label class="form-check-label text-muted" for="activo">Activo</label>
                                    </div>
                                </div>

                                <div class="col-6 d-flex justify-content-end">
                                    <button type="submit" class="btn btn-primary" id="guardarBtn"> 
                                        @if(producto.getId()==null)
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
        <script src="/content/js/prd/prd804952.js"></script>
        <script src="/content/js/prd/svd_prd754250.js"></script>

        @if(producto.getId()!=null)
            <script src="/content/js/prd/prd825153.js"></script>
        @endif

    @else
        @template.error.403()
    @endif
</main>
