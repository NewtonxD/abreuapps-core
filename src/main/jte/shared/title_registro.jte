@param String titulo
@param String permiso
@param boolean update
@param boolean backButton

<div class="row">
    <div class="col-12 d-flex align-items-start justify-content-start">
        @if(backButton)
            <button type="button" class="btn btn-sm btn-secondary"
                    onclick="$(function () {
                            event.preventDefault();
                            cargar_contenido('${permiso}');
                        });">
                <b><i class="fa fa-chevron-left"></i></b>
            </button>
        @endif&nbsp;&nbsp;
        <h4>
            ${titulo}
            <i class="fa fa-${!update?"plus":"pencil"}"></i>
        </h4>
    </div>
</div>