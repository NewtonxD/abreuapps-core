
@template.content()
<main id="content-page">
    @if(@dat_gen_registro_datos)
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
                                </button>
                                &nbsp;&nbsp;<h4>Datos Generales&nbsp;
                                    <th:block th:if="${!update}">
                                        <i class="fa fa-plus"></i>
                                    </th:block>
                                    <th:block th:if="${update}">
                                        <i class="fa fa-pencil"></i>
                                    </th:block>
                                </h4>
                            </div>
                        </div>
                        <form class="form-group" id="form-guardar">
                            <div class="row">
                                <th:block th:if="${!update}">
                                    <div class="mb-3 col-12 col-md-6">
                                        <label for="nom-dato" class="form-label text-muted">Nombre del Dato:</label>
                                        <input type="text" id="nom-dato" name="dato" placeholder=" ... " class="form-control" required aria-describedby="basic-addon4">        
                                    </div>

                                    <div class="mb-3 col-12 col-md-6">
                                        <label for="grupo" class="form-label text-muted">Grupo:</label>
                                        <select class="form-select custom-select" id="dato_padre" name="dato_padre" required>
                                            <option value="0" selected disabled>--Seleccione--</option>
                                            <option th:value="${g.dat}" th:each="g :${grupos}" th:if="${g.act}" th:text="${g.dat}"></option>
                                        </select>
                                    </div>
                                </th:block>

                                <th:block th:if="${update}">
                                    <input  type="datetime" name="fecha_actualizacionn" style="display: none; width: 1px;height: 1px;" th:value="${#dates.format(dato.fecha_actualizacion, 'yyyy-MM-dd HH:mm:ss')}">
                                    <input  type="text" name="dato_padre" style="display: none; width: 1px;height: 1px;" th:value="${dato.dato_padre}" required>
                                    <input type="text" style="display: none; width: 1px;height: 1px;" th:value="${dato.dato}" name="dato" required>

                                <div class="mb-3 col-12 col-md-6">
                                    <label for="nom-dato" class="form-label text-muted">Nombre del Dato:</label>
                                    <input type="text" id="nom-dato" disabled th:value="${dato.dato}" placeholder=" ... " class="form-control" aria-describedby="basic-addon4">
                                </div>

                                <div class="mb-3 col-12 col-md-6">
                                    <label for="grupo" class="form-label text-muted">Grupo:</label>
                                    <select class="form-select custom-select" disabled >
                                        <th:block th:if="${dato.dato_padre==null}">
                                            <option value="0" selected disabled>--Seleccione--</option>
                                        </th:block>
                                        <th:block th:if="${dato.dato_padre!=null}">
                                            <option th:value="${dato.dato_padre}" th:text="${dato.dato_padre}"></option>
                                        </th:block>
                                    </select>        
                                </div>
                                </th:block>

                                <div class="mb-3 col-12">
                                    <label for="descripcion" class="form-label text-muted">Descripción:</label>
                                    <textarea class="form-control" id="descripcion" name="descripcion" rows="3" th:text="${update?dato.descripcion:''}" required maxlength="255"></textarea>
                                </div>
                                <div class="col-6 d-flex justify-content-start">
                                    <div class="form-check">

                                        <input class="form-check-input" type="checkbox"  th:attr="checked=${update?dato.activo:true}" name="activo" id="activo">

                                            <label class="form-check-label text-muted" for="activo">Activo</label>
                                    </div>
                                </div>
                                <div class="col-6 d-flex justify-content-end">

                                    <button type="submit" class="btn btn-primary"> 
                                        <th:block th:if="${!update}"><b> <i class="fa fa-save"></i>
                                            Guardar</b>
                                        </th:block>
                                        <th:block th:if="${update}"><b> <i class="fa fa-pencil"></i>
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
        <script src="/content/js/dtgnr/svd_dt742520.js"></script>

    @else
        @template("error/403.jte", "error403")
    @endif
</main>