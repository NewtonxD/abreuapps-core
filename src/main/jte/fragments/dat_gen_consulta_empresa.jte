@import abreuapps.core.control.general.DatoDTO
@import java.util.List

@param boolean dat_gen_consulta_empresa
@param boolean dat_gen_registro_empresa
@param String msg
@param Boolean status
@param List<DatoDTO> datos


<main id="content-page">
    @if(dat_gen_consulta_empresa)
        <div class="container-fluid px-4">
            <div class="row d-flex align-items-center justify-content-center mt-5 mb-5">
                <div class="card col-12 col-md-10 col-lg-8"> 
                    <div class="card-body mt-3 mb-3">
                        <div class="row">
                            <div
                                class="col-8 d-flex align-items-start justify-content-start"
                                >
                                <h4>
                                    Empresas
                                </h4>
                            </div>
                            <div
                                class="col-4 d-flex align-items-end justify-content-end"
                                >
                                @if(dat_gen_registro_empresa)
                                    <button
                                        type="button"
                                        class="btn btn-success"
                                        onclick="event.preventDefault();closeEventSource();cargar_contenido('dat_gen_registro_empresa');"
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
                                            <th scope="col">Empresa</th>
                                            <th scope="col">Descripción</th>
                                            <th scope="col">Estado</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        @for(var d : datos)
                                            <tr data-id=${d.dat()}>
                                                <th scope="row">${d.dat()}</th>
                                                <td>${d.dsc()}</td>
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
        <script src="/content/js/dtgnr/es_emp742520.js"></script>
        @if(dat_gen_registro_empresa)
            <script src="/content/js/dtgnr/emp742520.js"></script>
        @endif
        
    @else
        @template.error.403()
    @endif
</main>
