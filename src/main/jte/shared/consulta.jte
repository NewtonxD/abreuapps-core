@import gg.jte.Content

@param String titulo
@param String id_registro
@param boolean permiso_consulta
@param boolean permiso_registro
@param String msg
@param Boolean status
@param boolean table_content = true
@param Content content
@param Content scripts_content

<main id="content-page">
    @if(permiso_consulta)
        <div class="container-fluid px-4">
            <div class="row d-flex align-items-center justify-content-center mt-5 mb-5">
                <div class="card col-12 col-md-10 col-lg-8">
                    <div class="card-body mt-3 mb-3">

                        @template.shared.title_consulta(
                            titulo = titulo,
                            id = id_registro,
                            permiso = permiso_registro
                        )

                        @template.shared.message( msg = msg , status = status )

                        <br>

                        @if(table_content)
                            <div class="row">
                                <div class="table-responsive table-responsive-md">
                                    <table class="table table-hover mt-2" id="table">
                        @endif
                                        ${content}

                        @if(table_content)
                                    </table>
                                </div>
                            </div>
                        @endif

                    </div>
                </div>
            </div>
        </div>
        ${scripts_content}
    @else
        @template.error.403()
    @endif
</main>
