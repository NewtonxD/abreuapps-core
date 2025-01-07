<main id="content-page">
    <div class="container-fluid px-4">

        <div class="row d-flex align-items-center justify-content-center mt-5 mb-5">
            <div class="card col-12 col-md-10 col-lg-8">
                <div class="card-header blanco-obligao row">
                    <div class="col-10">
                        <h3 class="mt-3">(CNT) Cuentas Bancarías</h3>
                    </div>
                    <div class="col-2 mb-2 d-flex align-items-end justify-content-end">
                        <button type="button" class="btn btn-secondary"><b><i class="fa-regular fa-rectangle-list"></i> Consulta</b></button>
                    </div>
                </div>
                <div class="card-body">
                    <form class="form-group">
                        <div class="row d-flex align-items-end justify-content-end">
                            <div class="mb-3 col-12 col-sm-6 col-lg-4">
                                <label for="fecha" class="form-label text-muted">Fecha:</label>
                                <input type="date" class="form-control" id="fecha">
                            </div>
                        </div>
                        <div class="row">    
                            <div class="mb-3 col-12 col-sm-6">
                                <label for="inputBanco" class="form-label text-muted">Banco:</label>
                                <select id="inputBanco" class="form-select">
                                    <option value="0" selected disable>--Seleccione--</option>
                                </select>
                            </div>

                            <div class="mb-3 col-12 col-sm-6">
                                <label for="inputCuenta" class="form-label text-muted"># Cuenta:</label>
                                <input type="text" class="form-control" required placeholder="" aria-label="Cuenta Contable" aria-describedby="basic-addon2">   
                            </div>

                            <div class="mb-3 col-12 col-sm-6">
                                <label for="inputCuenta" class="form-label text-muted"># Regional Cuenta:</label>
                                <input type="text" class="form-control" placeholder="" aria-label="Cuenta Contable" aria-describedby="basic-addon2">   
                            </div>

                            <div class="mb-3 col-12 col-sm-6">
                                <label for="inputDescCuenta" class="form-label text-muted">Descripción Cuenta:</label>
                                <input type="text" class="form-control" required placeholder="" aria-label="Descripcion Cuenta" aria-describedby="basic-addon2">   
                            </div>

                            <div class="mb-3 col-12 col-md-4">
                                <label for="tipo-cuenta" class="form-label text-muted">Tipo de Cuenta:</label>
                                <select class="form-select" id="tipo-cuenta" name="tipo-cuenta">
                                  <option value="0" selected disable>--Seleccione--</option>
                                  <option value="corriente">Corriente</option>
                                  <option value="ahorro">Ahorro</option>
                                </select>
                              </div>

                            <div class="mb-3 col-12 col-md-4">
                                <label for="tipo-cuenta" class="form-label text-muted">Moneda:</label>
                                <select class="form-select" id="tipo-cuenta" name="tipo-cuenta">
                                  <option value="0" selected disable>--Seleccione--</option>
                                  <option value="1">DOP Peso Dominicano</option>
                                  <option value="2">USD Dolar Estadounidense</option>
                                  <option value="3">EUR Euro</option>
                                </select>
                              </div>

                            <div class="mb-3 col-12 col-md-4">
                                <label for="monto" class="form-label text-muted">Monto Inicial:</label>
                                <input type="number" class="form-control" id="monto" name="monto" step="0.01" min="0" value="0.00" required/>
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

