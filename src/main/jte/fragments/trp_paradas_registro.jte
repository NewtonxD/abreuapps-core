
@template.content()
<main  id="content-page">
    <th:block th:if="${trp_paradas_registro}">
        <div class="container-fluid px-4">
            <div class="row d-flex align-items-center justify-content-center mt-5 mb-5">
                <div class="card col-12 col-md-10 col-lg-8">
                    <div class="card-body mt-3 mb-3">
                        <div class="row mb-3">
                            <div class="col-12 d-flex align-items-start justify-content-start">
                                <button type="submit" class="btn btn-sm btn-secondary"
                                        onclick="$(function () {
                                                event.preventDefault();
                                                cargar_contenido('trp_paradas_consulta');
                                            });">
                                    <b><i class="fa fa-chevron-left"></i></b>
                                </button>
                                &nbsp;&nbsp;<h4>Paradas&nbsp;
                                    <th:block th:if="${id==null}">
                                        <i class="fa fa-plus"></i>
                                    </th:block>
                                    <th:block th:if="${id!=null}">
                                        <i class="fa fa-pencil"></i>
                                    </th:block>
                                </h4>
                            </div>
                        </div>


                        <form class="form-group" id="form-guardar" th:object="${parada}">
                            <div class="row">
                                <input type="text" style="display: none; width: 1px;height: 1px;" th:if="${parada.id!=null}" id="idParada" name="id" th:value="${parada.id}" placeholder=" ... " class="form-control" aria-describedby="basic-addon4">

                                <div class="mb-3 col-12">
                                    <label for="descripcion" class="form-label text-muted">Descripción o texto identificativo:</label>
                                    <textarea class="form-control" th:field="*{descripción}" rows="2" required maxlength="255"></textarea>
                                </div>

                                <input  type="datetime" name="fecha_actualizacionn" style="display: none; width: 1px;height: 1px;" th:value="${#dates.format(parada.fecha_actualizacion, 'yyyy-MM-dd HH:mm:ss')}">

                                <div class="col-12 text-start ">
                                    <h5>Ubicación:</h5>
                                    <label for="" th:if="${parada.id==null}" class="form-label text-muted">Trate de colocar la parada del lado de la acera que corresponde. Nunca encima de la calle.</label>
                                </div>
                                <div class="mb-3 col-12 d-flex justify-content-center">
                                    <div id="map" class="map-regular"></div>
                                </div>

                                <div class="col-6 justify-content-center mb-3">
                                    <label for="latitud" class="form-label text-muted">Latitud:</label>
                                    <input type="number" class="form-control" th:field="*{latitud}"  step=".00000000000000001" required />
                                </div>

                                <div class="col-6 justify-content-center mb-3">
                                    <label for="longitud" class="form-label text-muted">Longitud:</label>
                                    <input type="number" class="form-control" th:field="*{longitud}" step=".00000000000000001" required />
                                </div>

                                <div class="col-6 d-flex justify-content-start">
                                    <div class="form-check">
                                        <input class="form-check-input" type="checkbox"  th:attr="checked=${id!=null?activo:'true'}" name="activo" id="activo">
                                            <label class="form-check-label text-muted" for="activo">Activo</label>
                                    </div>
                                </div>

                                <div class="col-6 d-flex justify-content-end">

                                    <button type="submit" class="btn btn-primary"> 
                                        <th:block th:if="${id==null}"><b> <i class="fa fa-save"></i>
                                                Guardar</b>
                                        </th:block>
                                        <th:block th:if="${id!=null}"><b> <i class="fa fa-pencil"></i>
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
        <script th:src="@{/content/js/trp/svd_pda574702.js}"></script>
        <script th:src="@{/content/js/lib/leaflet.js}"></script>
        <script th:src="@{/content/js/fetch_cache.js}"></script>
        <script th:src="@{/content/js/trp/pda544702loc.js}"></script>
    </th:block>
    <th:block th:unless="${trp_paradas_registro}" th:insert="~{error/403 :: error403}">
    </th:block>
</main>
