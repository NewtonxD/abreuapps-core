
@template.content()
<main  id="content-page">
    <div class="container-fluid px-4">
        <div class="row d-flex align-items-center justify-content-center mt-5 mb-5">
            <div class="card col-12 col-md-10 col-lg-8">
                <div class="card-header blanco-obligao row">
                    <div class="col-10">
                        <h3 class="mt-3">(CNT) Gestión de Presupuesto</h3>
                    </div>
                    <div class="col-2 mb-2 d-flex align-items-end justify-content-end">
                        <button type="button" class="btn btn-secondary"><b><i class="fa-regular fa-rectangle-list"></i> Consulta</b></button>
                    </div>
                </div>
                <div class="card-body mt-3 mb-3">
                    <form class="form-group">
                        <div class="row d-flex align-items-end justify-content-end">
                            <div class="mb-3 col-12 col-sm-6 col-lg-4">
                                <label for="fecha" class="form-label text-muted">Fecha:</label>
                                <input type="date" class="form-control" id="fecha">
                            </div>
                        </div>
                        <div class="row">  
                            <div class="mb-3 col-12 col-sm-6">
                                <label for="inputPeriodo" class="form-label text-muted">Periodo Contable:</label>
                                <div class="input-group">
                                    <input type="text" class="form-control" required disabled placeholder="" aria-label="Periodo Contable" aria-describedby="basic-addon2">
                                    <button type="button" class="input-group-text" id="basic-addon2"><i class="fa-regular fa-calendar"></i></button>
                                </div>
                            </div>

                            <div class="mb-3 col-12 col-sm-6">
                                <label for="inputCategoria" class="form-label text-muted">Categoria:</label>
                                <select id="inputCategoria" class="form-select">
                                    <option value="0" selected disable>--Seleccione--</option>
                                </select>
                            </div>

                            <div class="mb-3 col-12 col-sm-6">
                                <label for="inputPresupuesto" class="form-label text-muted" >Presupuesto Asignado:</label>
                                <input type="number" step="0.01" value="0.00" class="form-control" id="inputSaldoBanco">
                            </div>
                            <div class="mb-3 col-12 col-md-6">
                                <label for="tipo-cuenta" class="form-label text-muted">Moneda:</label>
                                <select class="form-select" id="tipo-cuenta" name="tipo-cuenta">
                                  <option value="0" selected disable>--Seleccione--</option>
                                  <option value="1">DOP Peso Dominicano</option>
                                  <option value="2">USD Dolar Estadounidense</option>
                                  <option value="3">EUR Euro</option>
                                </select>
                              </div>

                        <div class="col-12 d-flex justify-content-end">
                            <button type="submit" class="btn btn-outline-success"><b><i class="fas fa-save"></i> Guardar</b></button>
                        </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
    <script>$(document).ready(function() {
        var date = new Date();

        var day = date.getDate();
        var month = date.getMonth() + 1;
        var year = date.getFullYear();

        if (month < 10) month = "0" + month;
        if (day < 10) day = "0" + day;

        var today = year + "-" + month + "-" + day ;       
        $("#fecha").attr("value", today);
    });</script>
</main>
