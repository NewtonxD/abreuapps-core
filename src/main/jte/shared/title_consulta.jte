@param String titulo
@param String id
@param boolean permiso

<div class="row">
    <div class="col-8 d-flex align-items-start justify-content-start">
        <h4>
            ${titulo}&nbsp;
        </h4>
    </div>
    <div class="col-4 d-flex align-items-end justify-content-end">
        @if(permiso)
            <button
                    type="button"
                    class="btn btn-success"
                    onclick="
                        event.preventDefault();
                        closeEventSource();
                        cargar_contenido('${id}');"
            ><b><i class="fa fa-plus"></i></b>
            </button>
        @endif
    </div>
</div>