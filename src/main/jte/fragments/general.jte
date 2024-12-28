@template.content()
<main class="main-content" id="content-page">
    <div class="container-fluid px-4">
        <div class="row d-flex justify-content-center mt-2">
            <div class="col-12 col-sm-6 col-lg-4 mt-1 mb-1">
                <div class="card">
                    <div class="card-body text-center row align-items-center">
                        <div class="col-12 col-lg-6"><h3 class="pt-2 pb-2">Clientes Activos</h3></div>
                        <div class="col-12 col-lg-6"><h1 id="clientes_activos" >@total_active_views</h1></div>
                    </div>
                </div>
            </div>
            <div class="col-12 col-sm-6 col-lg-4 mt-1 mb-1">
                <div class="card">
                    <div class="card-body text-center row align-items-center">
                        <div class="col-12 col-lg-6"><h3 class="pt-2 pb-2">Visitas Hoy</h3></div>
                        <div class="col-12 col-lg-6"><h1 id="views_hoy">@total_views_today</h1></div>
                    </div>
                </div>
            </div>
        </div>
        <div class="row d-flex justify-content-center mt-2">
            <div class="col-12  col-lg-8 mt-1 mb-1">
                <div class="card">
                    <div class="card-body text-center">
                        <h4 class="pt-2 pb-1">Eventos del Transporte</h4>
                        <div class="table-responsive table-responsive-md">
                            <table
                                class="table table-hover mt-2"
                                id="table"
                                >
                                <thead class="bg-secondary text-white">
                                    <tr>
                                        <th scope="col">ID</th>
                                        <th scope="col"> </th>
                                        <th scope="col">Placa</th>
                                        <th scope="col">Ruta</th>
                                        <th scope="col">Estado</th>
                                        <th scope="col">Fecha</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    @for(var d : vhl_log)

                                        <tr data-id="${d.id}">
                                            <th scope="row">${d.id}</th>
                                            <td>
                                                @if(d.estado_new=="Averiado")
                                                    <img src="/content/assets/img/car-crash-icon.webp" alt="alt" height="32" width="32"/>
                                                @endif
        
                                                @if(d.system_change)
                                                    <img src="/content/assets/img/bot-icon.webp" alt="alt" height="32" width="32"/>
                                                @endif
                                            </td>

                                            <td>${d.placa}</td>
                                            <td>${d.ruta_new}</td>
                                            <td>${(d.system_change?'Sistema - ':'')+d.estado_new}</td>
                                            <td>${d.fecha_registro}</td>
                                        </tr>

                                    @endfor

                                </tbody>
                            </table>
                        </div>
                        <script src="/content/js/es_dash6085603.js"></script>
                        <script src="/content/js/tb_init_dashboard.js"></script>
                    </div>
                </div>
            </div>
        </div>
    </div>
</main>


@template.footer()
<footer class="py-4 bg-light mt-auto">
    <div class="container-fluid px-4">
        <div class="d-flex align-items-center justify-content-between small">
            <div class="text-muted">Copyright &copy; Abreu Apps 2024</div>
            <div>
                <a href="#" class="text-muted">Políticas de Privacidad</a>
                &middot;
                <a href="#" class="text-muted">Terminos &amp; Condiciones</a>
            </div>
        </div>
    </div>
</footer>
