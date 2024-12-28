@template.header()
<nav class="sb-topnav navbar navbar-expand navbar-dark bg-dark align-center">
    <!-- Navbar Brand-->
    <a class="navbar-brand ps-3" href="/main/index">
        <img class="pb-2" width="32px" height="32px" src="/content/assets/img/Omsafooter.webp" />
        <span class="fs-4 fw-semibold">@appnombre</span>
    </a>
    <!-- Sidebar Toggle-->
    <button
        type="button"
        class="btn btn-link btn-sm order-1 order-lg-0 me-4 me-lg-0 toggle-btn text-white"
        onclick="toggleSidebar()"
    >
        <i class="fa fa-bars"></i>
    </button>
    <div class="d-none d-md-inline-block form-inline ms-auto me-0 me-md-3 my-2 my-md-0"></div>
    <h5 class="text-white fw-light mt-2 d-none d-md-block">@app_provincia</h5>
    <ul class="navbar-nav ms-auto ms-md-0 me-3 me-lg-4">
        <li class="nav-item dropdown">
            <a
                class="nav-link dropdown-toggle"
                id="navbarDropdown"
                href="#"
                role="button"
                data-bs-toggle="dropdown"
                aria-expanded="false"
            ><i class="fa fa-user fa-fw"></i></a>
            <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="navbarDropdown">
                <li><a class="dropdown-item config-user" href="#">Configuración</a></li>
                <li><hr class="dropdown-divider" /></li>
                <li>
                    <a class="dropdown-item" href="/auth/logout">Cerrar Sesión</a>
                </li>
            </ul>
        </li>
    </ul>
</nav>

@template.menu()
<div id="layoutSidenav_nav">
    <nav id="sidenavAccordion" class="bg-dark sidebar flex-shrink-0 p-3" data-bs-theme="dark">
        <div class="sb-sidenav-menu">
            <a class="d-flex align-items-center pb-3 mb-3 text-decoration-none text-white" href="/main/index">
                <img class="me-3" width="32px" height="32px" src="/content/assets/img/Omsafooter.webp" />
                <span class="fs-4 fw-semibold">@appnombre</span>
            </a>
            <ul class="nav list-unstyled ps-0">
                @if(trp_principal) 
                    <li class="mt-1">
                        <button
                            class="nav-link btn btn-toggle d-inline-flex align-items-center rounded border-0 collapsed"
                            type="button"
                            data-bs-toggle="collapse"
                            data-bs-target="#collapseLayouts4"
                            aria-expanded="false"
                            aria-controls="collapseLayouts4"
                        >
                            <i class="fa fa-car"></i>&MediumSpace; Transporte
                        </button>
                        <div
                            class="collapse"
                            id="collapseLayouts4"
                            aria-labelledby="headingFour"
                            data-bs-parent="#sidenavAccordion"
                        >
                            <ul class="nav btn-toggle-nav list-unstyled fw-normal pb-1 d-block">
                                @if(trp_vehiculo_consulta) 
                                    <li>
                                        <a class="nav-link acceso link-body-emphasis d-inline-flex text-decoration-none rounded" href="#" id="trp_vehiculo_consulta">
                                            &MediumSpace; Vehículos
                                        </a>
                                    </li>
                                @endif
                                @if(trp_rutas_consulta) 
                                    <li>
                                        <a class="nav-link acceso link-body-emphasis d-inline-flex text-decoration-none rounded" href="#" id="trp_rutas_consulta">
                                            &MediumSpace; Rutas
                                        </a>
                                    </li>
                                @endif
                                @if(trp_paradas_consulta) 
                                    <li>
                                        <a class="nav-link acceso link-body-emphasis d-inline-flex text-decoration-none rounded" href="#" id="trp_paradas_consulta">
                                            &MediumSpace; Paradas
                                        </a>
                                    </li>
                                @endif
                            </ul>
                        </div>
                    </li>
                @endif
                @if(con_principal)
                    <li>
                        <button
                            class="nav-link btn btn-toggle d-inline-flex align-items-center rounded border-0 collapsed"
                            type="button"
                            data-bs-toggle="collapse"
                            data-bs-target="#collapseLayouts1"
                            aria-expanded="false"
                            aria-controls="collapseLayouts1"
                            >
                            <i class="fa fa-calculator"></i>&MediumSpace; Contabilidad
                        </button>
                        <div
                            class="collapse"
                            id="collapseLayouts1"
                            aria-labelledby="headingOne"
                            data-bs-parent="#sidenavAccordion"
                            >
                            <ul class="nav btn-toggle-nav list-unstyled fw-normal pb-1 d-block">
                                
                                @if(con_registro_transacciones)
                                    <li>
                                        <a
                                            class="nav-link acceso  link-body-emphasis d-inline-flex text-decoration-none rounded"
                                            href="#"
                                            id="con_registro_transacciones"
                                            >
                                            &MediumSpace; Registro de Transacciones
                                        </a>
                                    </li>
                                @endif
                                
                                @if(con_conciliacion_bancaria)
                                    <li>
                                        <a
                                            class="nav-link acceso  link-body-emphasis d-inline-flex text-decoration-none rounded"
                                            href="#"
                                            id="con_conciliacion_bancaria"
                                            >
                                            &MediumSpace; Conciliación Bancaría
                                        </a>
                                    </li>
                                @endif   

                                @if(con_cuentas_bancarias)
                                    <li>
                                        <a
                                            class="nav-link acceso  link-body-emphasis d-inline-flex text-decoration-none rounded"
                                            href="#"
                                            id="con_cuentas_bancarias"
                                            >
                                            &MediumSpace; Cuentas Bancanrtías
                                        </a>
                                    </li>
                                @endif

                                @if(con_presupuesto)
                                    <li>
                                        <a
                                            class="nav-link acceso  link-body-emphasis d-inline-flex text-decoration-none rounded"
                                            href="#"
                                            id="con_presupuesto"
                                            >
                                            &MediumSpace; Gestión de Presupuesto
                                        </a>
                                    </li>
                                @endif

                                @if(con_impuestos)
                                    <li>
                                        <a class="nav-link acceso  link-body-emphasis d-inline-flex text-decoration-none rounded" href="#" id="con_impuestos">
                                            &MediumSpace; Gestión de Impuestos
                                        </a>
                                    </li>
                                @endif

                                @if(con_informes)
                                    <li>
                                        <a class="nav-link acceso  link-body-emphasis d-inline-flex text-decoration-none rounded" href="#" id="con_informes">
                                            &MediumSpace; Informes Financieros
                                        </a>
                                    </li>
                                @endif
                            </ul>
                        </div>
                    </li>
                @endif

                @if(cxp_principal)
                    <li>
                        <button
                            class="nav-link btn btn-toggle d-inline-flex align-items-center rounded border-0 collapsed"
                            type="button"
                            data-bs-toggle="collapse"
                            data-bs-target="#collapseLayouts2"
                            aria-expanded="false"
                            aria-controls="collapseLayouts2"
                            >
                            <i class="fa fa-list-alt"></i>&MediumSpace; Cuentas x Pagar
                        </button>
                        <div
                            class="collapse"
                            id="collapseLayouts2"
                            aria-labelledby="headingTwo"
                            data-bs-parent="#sidenavAccordion"
                            >
                            <ul class="nav btn-toggle-nav list-unstyled fw-normal pb-1 d-block">
                                
                                @if(cxp_registro_facturas)
                                    <li>
                                        <a class="nav-link acceso link-body-emphasis d-inline-flex text-decoration-none rounded " href="#" id="cxp_registro_facturas">
                                            &MediumSpace; Registro de Facturas
                                        </a>
                                    </li>
                                @endif

                                @if(cxp_control_pagos)
                                    <li>
                                        <a class="nav-link acceso link-body-emphasis d-inline-flex text-decoration-none rounded " href="#" id="cxp_control_pagos">
                                            &MediumSpace; Control de Pagos
                                        </a>
                                    </li>
                                @endif

                                @if(cxp_informes)
                                    <li>
                                        <a class="nav-link acceso link-body-emphasis d-inline-flex text-decoration-none rounded " href="#" id="cxp_informes">
                                            &MediumSpace; Informes y Análisis
                                        </a>
                                    </li>
                                @endif

                                @if(cxp_comunicacion_proveedores)
                                    <li>
                                        <a
                                            class="nav-link acceso link-body-emphasis d-inline-flex text-decoration-none rounded "
                                            href="#"
                                            id="cxp_comunicacion_proveedores"
                                            >
                                            &MediumSpace; Comunicación con Proveedores
                                        </a>
                                    </li>
                                @endif

                                @if(cxp_autorizaciones_aprobaciones)
                                    <li>
                                        <a
                                            class="nav-link acceso link-body-emphasis d-inline-flex text-decoration-none rounded "
                                            href="#"
                                            id="cxp_autorizaciones_aprobaciones"
                                            >
                                            &MediumSpace; Autorizaciones y Aprobaciones
                                        </a>
                                    </li>
                                @endif

                            </ul>
                        </div> 
                    </li>
                @endif

                @if(inv_principal)
                    <li class="me-1">                       
                        <button
                            class="nav-link btn btn-toggle d-inline-flex align-items-center rounded border-0 collapsed "
                            type="button"
                            data-bs-toggle="collapse"
                            data-bs-target="#collapseLayouts6"
                            aria-expanded="false"
                            aria-controls="collapseLayouts6"
                            >
                            <i class="fa fa-solid fa-archive"></i>&MediumSpace; Inventario
                        </button>
                        <div
                            class="collapse"
                            id="collapseLayouts6"
                            aria-labelledby="headingSix"
                            data-bs-parent="#sidenavAccordion"
                            >
                            <ul class="nav btn-toggle-nav list-unstyled fw-normal pb-1 d-block">
                                @if(inv_producto_consulta)
                                    <li>
                                        <a class="nav-link acceso link-body-emphasis text-decoration-none rounded " href="#" id="inv_producto_consulta">
                                            &MediumSpace; Productos
                                        </a>
                                    </li>
                                @endif
                            </ul>
                        </div>
                    </li>
                @endif

                @if(pub_principal)
                    <li class="me-1">                       
                        <button
                            class="nav-link btn btn-toggle d-inline-flex align-items-center rounded border-0 collapsed "
                            type="button"
                            data-bs-toggle="collapse"
                            data-bs-target="#collapseLayouts5"
                            aria-expanded="false"
                            aria-controls="collapseLayouts5"
                            >
                            <i class="fa fa-solid fa-bullhorn"></i>&MediumSpace; Publicidad
                        </button>
                        <div
                            class="collapse"
                            id="collapseLayouts5"
                            aria-labelledby="headingFifth"
                            data-bs-parent="#sidenavAccordion"
                            >
                            <ul class="nav btn-toggle-nav list-unstyled fw-normal pb-1 d-block">

                                @if(pub_publicidad_consulta)
                                    <li>
                                        <a class="nav-link acceso link-body-emphasis text-decoration-none rounded " href="#" id="pub_publicidad_consulta">
                                            &MediumSpace; Publicidad
                                        </a>
                                    </li>
                                @endif

                                @if(dat_gen_consulta_empresa)
                                    <li>
                                        <a class="nav-link acceso link-body-emphasis text-decoration-none rounded " href="#" id="dat_gen_consulta_empresa">
                                            &MediumSpace; Empresas
                                        </a>
                                    </li>
                                @endif

                                @if(pub_publicidad_reportes)
                                    <li>
                                        <a class="nav-link acceso link-body-emphasis text-decoration-none rounded " href="#" id="pub_publicidad_reportes">
                                            &MediumSpace; Reportes
                                        </a>
                                    </li>
                                @endif
                            </ul>
                        </div>
                    </li>
                @endif

                @if(sys_principal)
                    <li class="mb-1">                       
                        <button
                            class="nav-link btn btn-toggle d-inline-flex align-items-center rounded border-0 collapsed "
                            type="button"
                            data-bs-toggle="collapse"
                            data-bs-target="#collapseLayouts3"
                            aria-expanded="false"
                            aria-controls="collapseLayouts3"
                            >
                            <i class="fa fa-solid fa-gear"></i>&MediumSpace; Sistemas
                        </button>
                        <div
                            class="collapse"
                            id="collapseLayouts3"
                            aria-labelledby="headingThree"
                            data-bs-parent="#sidenavAccordion"
                            >
                            <ul class="nav btn-toggle-nav list-unstyled fw-normal pb-1 d-block">

                                @if(dat_gen_consulta_datos)
                                    <li>
                                        <a class="nav-link acceso link-body-emphasis text-decoration-none rounded " href="#" id="dat_gen_consulta_datos">
                                            &MediumSpace; Mantenimientos de Datos
                                        </a>
                                    </li>
                                @endif

                                @if(sys_configuracion)
                                    <li>
                                        <a class="nav-link acceso link-body-emphasis text-decoration-none rounded " href="#" id="sys_configuracion">
                                            &MediumSpace; Configuración
                                        </a>
                                    </li>
                                @endif

                                @if(usr_mgr_principal)
                                    <li>
                                        <a class="nav-link acceso link-body-emphasis text-decoration-none rounded " href="#" id="usr_mgr_principal">
                                            &MediumSpace; Usuarios
                                        </a>
                                    </li>
                                @endif

                            </ul>
                        </div>
                    </li>
                @endif

                <!-- Añadir otros menús siguiendo la misma estructura -->
            </ul>
        </div>

        <div class="sb-sidenav-footer position-fixed bottom-0 start-0 p-3">
            <div class="fs-6">Bienvenido:&MediumSpace;
                <a class="btn btn-secondary btn-sm config-user" href="#"><i class="fa fa-cog"></i></a>
                &nbsp;
                <a class="btn btn-danger btn-sm" href="/auth/logout"><i class="fa fa-sign-out"></i></a>
            </div>

            @if(datos_personales!=null)
                <span class="fs-6 fw-semibold mt-2">@datos_personales.getNombre() @datos_personales.getApellido()</span>
            @endif

        </div>
    </nav>
</div>

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
