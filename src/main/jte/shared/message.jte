@param String msg
@param Boolean status

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