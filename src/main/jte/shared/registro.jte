@import gg.jte.Content

@param String titulo
@param String id_consulta
@param boolean permiso_registro
@param boolean update
@param Content content
@param Content scripts_content

<main id="content-page">
    @if(permiso_registro)
        <div class="container-fluid px-4">
            <div class="row d-flex align-items-center justify-content-center mt-5 mb-5">
                <div class="card col-12 col-md-10 col-lg-8">
                    <div class="card-body mt-3 mb-3">

                        @template.shared.title_registro(
                            titulo = titulo,
                            permiso = id_consulta,
                            update = update
                        )
                        <form class="form-group" id="form-guardar">
                            <div class="row">
                                ${content}

                                <div class="col-6 d-flex justify-content-end">

                                    <button type="submit" class="btn btn-primary">
                                        @if(!update)
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
        ${scripts_content}
    @else
        @template.error.403()
    @endif
</main>
