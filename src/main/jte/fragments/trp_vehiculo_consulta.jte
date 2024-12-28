
@template.content()
<main  id="content-page">
    <th:block th:if="${trp_vehiculo_consulta}">
        <div class="container-fluid px-4">
            <div class="row d-flex align-items-center justify-content-center mt-5 mb-5">
                <div class="card col-12 col-md-10 col-lg-8"> 
                    <div class="card-body mt-3 mb-3">
                        <div class="row">
                            <div
                            class="col-8 d-flex align-items-start justify-content-start"
                            >
                            <h4>
                                Vehículos&nbsp;
                            </h4>
                        </div>
                        <div
                            class="col-4 d-flex align-items-end justify-content-end"
                            >
                            <button th:if="${trp_vehiculo_registro}"
                                type="button"
                                class="btn btn-success"
                                onclick="event.preventDefault();closeEventSource();cargar_contenido('trp_vehiculo_registro');"
                                >
                                <b><i class="fa fa-plus"></i></b>
                            </button>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col mt-2 mb-2">
                            <th:block th:if="${msg!=null}">
                                <div
                                    th:class="'alert '+${status ? 'alert-success' : 'alert-warning'}+' alert-dismissible fade show'"
                                    role="alert"
                                    >
                                    <b><span th:text="${msg}"></span></b>
                                    <button
                                        type="button"
                                        class="btn-close"
                                        data-bs-dismiss="alert"
                                        aria-label="Close"
                                        ></button>
                                </div>
                            </th:block>
                        </div>
                    </div>
                    <br>
                        <div class="row">
                            <div class="table-responsive table-responsive-md">
                                <table
                                    class="table table-hover mt-2"
                                    id="table"
                                    >
                                    <thead class="bg-secondary text-white">
                                        <tr>
                                            <th scope="col">Placa</th>
                                            <th scope="col">Marca y Módelo</th>
                                            <th scope="col">Color</th>
                                            <th scope="col">Estado</th>
                                            <th scope="col">Activo</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr th:each="d: ${vehiculos}" th:attr="data-id=${d.pl}">
                                            <th scope="row" th:text="${d.pl}"></th>
                                            <td th:text="${d.marca_dat + ' ' + d.modelo_dat}"></td>
                                            <td th:text="${d.color_dat}"></td>
                                            <td th:text="${d.estado_dat}"></td>
                                            <td th:text="${d.act?'Activo':'Inactivo'}"></td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <script th:src="@{/content/js/tb_init.js}"></script>
        <script th:if="${trp_vehiculo_registro}"  th:src="@{/content/js/trp/vhl574702.js}"></script>
        <script th:src="@{/content/js/trp/es_vhl574702.js}"></script>
</th:block>
<th:block th:unless="${trp_vehiculo_consulta}" th:insert="~{error/403 :: error403}">
</th:block>
</main>