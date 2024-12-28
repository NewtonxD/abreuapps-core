
@template.content()
<main  id="content-page">
    <th:block th:if="${trp_vehiculo_registro}">
        <div class="container-fluid px-4">
            <div class="row d-flex align-items-center justify-content-center mt-5 mb-5">
                <div class="card col-12 col-md-10 col-lg-8">
                    <div class="card-body mt-3 mb-3">
                        <div class="row mb-3">
                            <div class="col-12 d-flex align-items-start justify-content-start">
                                <button type="submit" class="btn btn-sm btn-secondary"
                                        onclick="$(function () {
                                                event.preventDefault();
                                                cargar_contenido('trp_vehiculo_consulta');
                                            });">
                                    <b><i class="fa fa-chevron-left"></i></b>
                                </button>
                                &nbsp;&nbsp;<h4>Vehículos&nbsp;
                                    <th:block th:if="${placa==null}">
                                        <i class="fa fa-plus"></i>
                                    </th:block>
                                    <th:block th:if="${placa!=null}">
                                        <i class="fa fa-pencil"></i>
                                    </th:block>
                                </h4>
                            </div>
                        </div>
                        <form class="form-group" id="form-guardar" th:object="${vehiculo}">
                            <div class="row">
                                <div class="mb-3 col-12 col-md-6 col-lg-4">
                                    <label for="placa" class="form-label text-muted">Placa:</label>
                                    <input type="text" th:field='*{placa}' th:disabled="${vehiculo.placa!=null && vehiculo.placa!=''}" placeholder=" ... " class="form-control" required aria-describedby="basic-addon4">        
                                    <input type="text" style="display: none; width: 1px;height: 1px;" th:if="${vehiculo.placa!=null && vehiculo.placa!=''}" id="Placa" name="placa" th:value="${vehiculo.placa}" placeholder=" ... " class="form-control"  aria-describedby="basic-addon4">
                                </div>

                                <div class="mb-3 col-12 col-md-6 col-lg-4">
                                    <label for="grupo" class="form-label text-muted">Color:</label>

                                    <th:block th:if="${vehiculo.placa==null}">
                                        <select class="form-select custom-select" th:field="*{color}" required>
                                    </th:block>
                                    <th:block th:if="${vehiculo.placa!=null}">
                                        <select class="form-select custom-select" name="color" id="color" required>
                                    </th:block>

                                            <option value="0" disabled>--Seleccione--</option>
                                        <th:block  th:each="c :${color}">
                                            <option th:value="${c.dat}" th:selected="${vehiculo.placa!=null && vehiculo.color.dato==c.dat}" th:if="${c.act}" th:text="${c.dat}"></option>
                                        </th:block>
                                    </select>
                                </div>

                                <div class="mb-3 col-12 col-md-6 col-lg-4">
                                    <label for="grupo" class="form-label text-muted">Tipo:</label>

                                    <th:block th:if="${vehiculo.placa==null}">
                                        <select class="form-select custom-select" th:field="*{tipo_vehiculo}" required>
                                    </th:block>
                                    <th:block th:if="${vehiculo.placa!=null}">
                                        <select class="form-select custom-select" name="tipo_vehiculo" id="tipo_vehiculo" required>
                                    </th:block>

                                            <option value="0" disabled>--Seleccione--</option>
                                        <th:block th:each="t :${tipo_vehiculo}">
                                            <option th:value="${t.dat}" th:selected="${vehiculo.placa!=null && vehiculo.tipo_vehiculo.dato==t.dat}"  th:if="${t.act}" th:text="${t.dat}"></option>
                                        </th:block>
                                    </select>
                                </div>

                                <div class="mb-3 col-12 col-md-6 col-lg-4">
                                    <label for="grupo" class="form-label text-muted">Estado:</label>

                                    <th:block th:if="${vehiculo.placa==null}">
                                        <select class="form-select custom-select" th:field="*{estado}" required>
                                    </th:block>
                                    <th:block th:if="${vehiculo.placa!=null}">
                                        <select class="form-select custom-select" name="estado" id="estado" required>
                                    </th:block>

                                            <option value="0" disabled>--Seleccione--</option>
                                        <th:block th:each="e :${estado}">
                                            <option th:value="${e.dat}" th:selected="${vehiculo.placa!=null && vehiculo.estado.dato==e.dat}" th:if="${e.act}" th:text="${e.dat}"></option>
                                        </th:block>
                                    </select>
                                </div>

                                <div class="mb-3 col-12 col-md-6 col-lg-4">
                                    <label for="grupo" class="form-label text-muted">Marca:</label>

                                    <th:block th:if="${vehiculo.placa==null}">
                                        <select class="form-select custom-select" th:field="*{marca}" required>
                                    </th:block>
                                    <th:block th:if="${vehiculo.placa!=null}">
                                        <select class="form-select custom-select" name="marca" id="marca" required>
                                    </th:block>

                                            <option value="0" disabled>--Seleccione--</option>
                                        <th:block th:each="m :${marca}">
                                            <option th:value="${m.dat}" th:selected="${vehiculo.placa!=null && vehiculo.marca.dato==m.dat}" th:if="${m.act}" th:text="${m.dat}"></option>
                                        </th:block> 
                                    </select>
                                </div>

                                <div class="mb-3 col-12 col-md-6 col-lg-4">
                                    <label for="grupo" class="form-label text-muted">Módelo:</label>

                                    <th:block th:if="${vehiculo.placa==null}">
                                        <select class="form-select custom-select" th:field="*{modelo}" required>
                                    </th:block>
                                    <th:block th:if="${vehiculo.placa!=null}">
                                        <select class="form-select custom-select" name="modelo" id="modelo" required>
                                    </th:block>

                                            <option value="0" disabled>--Seleccione--</option>
                                        <th:block th:each="mo :${modelo}">
                                            <option th:value="${mo.dat}" th:selected="${vehiculo.placa!=null && vehiculo.modelo.dato==mo.dat}" th:if="${mo.act}" th:text="${mo.dat}"></option>
                                        </th:block> 
                                    </select>
                                </div>
                                
                                <div class="mb-3 col-12 col-md-6 col-lg-4">
                                    <label for="placa" class="form-label text-muted">Año de Fabricación:</label>
                                    <input type="number" th:field='*{anio_fabricacion}' placeholder=" ... " class="form-control" required aria-describedby="basic-addon4">        
                                    
                                </div>
                                
                                <div class="mb-3 col-12 col-md-6 col-lg-4">
                                    <label for="placa" class="form-label text-muted">Capacidad Máxima (Pasajeros):</label>
                                    <input type="number" th:field='*{capacidad_pasajeros}' placeholder=" ... " class="form-control" required aria-describedby="basic-addon4">        
                                    
                                </div>

                                <input  type="datetime" name="fecha_actualizacionn" style="display: none; width: 1px;height: 1px;" th:value="${#dates.format(vehiculo.fecha_actualizacion, 'yyyy-MM-dd HH:mm:ss')}">

                                <th:block th:if="${vehiculo.placa!=null && last_loc}">
                                    <div class="col-12  d-flex justify-content-center text-center ">
                                        <h6>Fecha Ultima Ubicación: <span id="date_last_loc"></span></h6>
                                    </div>
                                    <div class="col-12 d-flex justify-content-center">
                                        <div id="map" class="map-regular"></div>
                                    </div>
                                </th:block>

                                <div class="col-6 d-flex justify-content-start mt-2">
                                    <div class="form-check">
                                        <input class="form-check-input" type="checkbox"  th:attr="checked=${placa!=null?activo:'true'}" name="activo" id="activo">
                                            <label class="form-check-label text-muted" for="activo">Activo</label>
                                    </div>
                                </div>

                                <div class="col-6 d-flex justify-content-end mt-2">

                                    <button type="submit" class="btn btn-primary"> 
                                        <th:block th:if="${placa==null}"><b> <i class="fa fa-save"></i>
                                                Guardar</b>
                                        </th:block>
                                        <th:block th:if="${placa!=null}"><b> <i class="fa fa-pencil"></i>
                                                Editar</b>
                                        </th:block>
                                    </button>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
        <script th:src="@{/content/js/trp/svd_vhl574702.js}"></script>
        <script  th:if="${vehiculo.placa!=null  && last_loc }"  th:src="@{/content/js/lib/leaflet.js}"></script>
        <script th:src="@{/content/js/fetch_cache.js}"></script>
        <script  th:if="${vehiculo.placa!=null && last_loc }"  th:src="@{/content/js/trp/vhl544702lastloc.js}"></script>
    </th:block>
    <th:block th:unless="${trp_vehiculo_registro}" th:insert="~{error/403 :: error403}">
    </th:block>
</main>
