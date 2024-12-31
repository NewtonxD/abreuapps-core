@import abreuapps.core.control.general.PublicidadDTO
@import java.util.List

@param boolean pub_publicidad_consulta
@param boolean pub_publicidad_registro
@param String msg
@param Boolean status
@param List<PublicidadDTO> publicidades


<main id="content-page">
    @if(pub_publicidad_consulta)
        <div class="container-fluid px-4">
            <div class="row d-flex align-items-center justify-content-center mt-5 mb-5">
                <div class="card col-12 col-md-10 col-lg-8"> 
                    <div class="card-body mt-3 mb-3">
                        <div class="row">
                            <div
                                class="col-8 d-flex align-items-start justify-content-start"
                            >
                                <h4>
                                    Publicidad&nbsp;
                                </h4>
                            </div>
                            <div
                                class="col-4 d-flex align-items-end justify-content-end"
                                >
                                @if(pub_publicidad_registro)
                                    <button
                                        type="button"
                                        class="btn btn-success"
                                        onclick="
                                            event.preventDefault();
                                            closeEventSource();
                                            cargar_contenido('pub_publicidad_registro');"
                                        >
                                        <b><i class="fa fa-plus"></i></b>
                                    </button>
                                @endif
                            </div>
                        </div>
                        <div class="row">
                            <div class="col mt-2 mb-2">
                                @if(msg!=null)
                                    <div
                                            class="alert ${status ? "alert-success" : "alert-warning"} alert-dismissible fade show"
                                            role="alert"
                                    >
                                        <b><span>${msg}</span></b>
                                        <button
                                                type="button"
                                                class="btn-close"
                                                data-bs-dismiss="alert"
                                                aria-label="Close"
                                        ></button>
                                    </div>
                                @endif
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
                                            <th scope="col">ID</th>
                                            <th scope="col">Título</th>
                                            <th scope="col">Empresa</th>
                                            <th scope="col">Activo</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        @for(var d:publicidades)
                                            <tr data-id="${d.id()}">
                                                <th scope="row">${d.id()}</th>
                                                <td>${d.tit()}</td>
                                                <td>${d.empresa_dat()}</td>
                                                <td>${d.act()?"Activo":"Inactivo"}</td>
                                            </tr>
                                        @endfor
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <script src="/content/js/tb_init.js"></script>
        @if(pub_publicidad_registro) <script src="/content/js/pub/pub794851.js"></script> @endif
        <script src="/content/js/pub/es_pub794851.js"></script>
    @else
        @template.error.403()
    @endif
</main>
