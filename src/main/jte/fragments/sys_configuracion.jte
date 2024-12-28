
@template.content()
<main  id="content-page">

    <th:block th:if="${sys_configuracion}">
        <div class="container-fluid px-4">
            <div class="row d-flex align-items-center justify-content-center mt-5 mb-5">
                <div class="card col-12 col-md-10 col-lg-8">
                    <div class="card-body mt-3 mb-3">
                        <form id="form-guardar">
                        <div class="row">

                            <div class="col-10 d-flex align-items-center justify-content-start">
                                <h4 class="me-4">
                                    Configuraciones del Sistema&nbsp;<i class="fa fa-cog"></i>
                                </h4>
                                <button type="submit" class="btn btn-primary"><i class="fa fa-save"></i></button>
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
                        <div class="row m-4 pb-4">
                            <div th:each="c: ${conf}" class="col-auto mb-2 justify-content-start">
                                <label class="form-label" th:text="${'- '+c.dsc+': '}"></label>
                                <input type="text" class="form-control" th:name="${c.cod}" th:id="${c.cod}" th:value="${c.val}" />
                            </div>
                        </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
        <script th:src="@{/content/js/conf/svd_conf345612.js}"></script>
    </th:block>

    <th:block th:unless="${sys_configuracion}" th:insert="~{error/403 :: error403}">
    </th:block>
</main>
