@import abreuapps.core.control.general.Persona
@import java.util.Map

@param String app_nombre
@param Persona datos_personales
@param Map< String, Boolean > permisos
@param Map< String, String > conf


<div id="layoutSidenav_nav">
    <nav id="sidenavAccordion" class="bg-dark sidebar flex-shrink-0 p-3" data-bs-theme="dark">
        <div class="sb-sidenav-menu">
            <a class="d-flex align-items-center pb-3 mb-3 text-decoration-none text-white" href="/main/index">
                <img class="me-3" width="32px" height="32px" src="/content/assets/img/Omsafooter.webp" />
                <span class="fs-4 fw-semibold">${app_nombre}</span>
            </a>
            <ul class="nav list-unstyled ps-0">

                @if(permisos.getOrDefault("trp_principal",false)) 
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

                                @if(permisos.getOrDefault("trp_vehiculo_consulta",false)) 
                                    <li>
                                        <a class="nav-link acceso link-body-emphasis d-inline-flex text-decoration-none rounded" href="#" id="trp_vehiculo_consulta">
                                            &MediumSpace; Vehículos
                                        </a>
                                    </li>
                                @endif

                                @if(permisos.getOrDefault("trp_rutas_consulta",false)) 
                                    <li>
                                        <a class="nav-link acceso link-body-emphasis d-inline-flex text-decoration-none rounded" href="#" id="trp_rutas_consulta">
                                            &MediumSpace; Rutas
                                        </a>
                                    </li>
                                @endif

                                @if(permisos.getOrDefault("trp_paradas_consulta",false)) 
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

                @if(permisos.getOrDefault("con_principal",false))
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
                                
                                @if(permisos.getOrDefault("con_registro_transacciones",false))
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
                                
                                @if(permisos.getOrDefault("con_conciliacion_bancaria",false))
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

                                @if(permisos.getOrDefault("con_cuentas_bancarias",false))
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

                                @if(permisos.getOrDefault("con_presupuesto",false))
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

                                @if(permisos.getOrDefault("con_impuestos",false))
                                    <li>
                                        <a class="nav-link acceso  link-body-emphasis d-inline-flex text-decoration-none rounded" href="#" id="con_impuestos">
                                            &MediumSpace; Gestión de Impuestos
                                        </a>
                                    </li>
                                @endif

                                @if(permisos.getOrDefault("con_informes",false))
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

                @if(permisos.getOrDefault("cxp_principal",false))
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
                                
                                @if(permisos.getOrDefault("cxp_registro_facturas",false))
                                    <li>
                                        <a class="nav-link acceso link-body-emphasis d-inline-flex text-decoration-none rounded " href="#" id="cxp_registro_facturas">
                                            &MediumSpace; Registro de Facturas
                                        </a>
                                    </li>
                                @endif

                                @if(permisos.getOrDefault("cxp_control_pagos",false))
                                    <li>
                                        <a class="nav-link acceso link-body-emphasis d-inline-flex text-decoration-none rounded " href="#" id="cxp_control_pagos">
                                            &MediumSpace; Control de Pagos
                                        </a>
                                    </li>
                                @endif

                                @if(permisos.getOrDefault("cxp_informes",false))
                                    <li>
                                        <a class="nav-link acceso link-body-emphasis d-inline-flex text-decoration-none rounded " href="#" id="cxp_informes">
                                            &MediumSpace; Informes y Análisis
                                        </a>
                                    </li>
                                @endif

                                @if(permisos.getOrDefault("cxp_comunicacion_proveedores",false))
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

                                @if(permisos.getOrDefault("cxp_autorizaciones_aprobaciones",false))
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

                @if(permisos.getOrDefault("inv_principal",false))
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
                                
                                @if(permisos.getOrDefault("inv_producto_consulta",false))
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

                @if(permisos.getOrDefault("pub_principal",false))
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

                                @if(permisos.getOrDefault("pub_publicidad_consulta",false))
                                    <li>
                                        <a class="nav-link acceso link-body-emphasis text-decoration-none rounded " href="#" id="pub_publicidad_consulta">
                                            &MediumSpace; Publicidad
                                        </a>
                                    </li>
                                @endif

                                @if(permisos.getOrDefault("dat_gen_consulta_empresa",false))
                                    <li>
                                        <a class="nav-link acceso link-body-emphasis text-decoration-none rounded " href="#" id="dat_gen_consulta_empresa">
                                            &MediumSpace; Empresas
                                        </a>
                                    </li>
                                @endif

                                @if(permisos.getOrDefault("pub_publicidad_reportes",false))
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

                @if(permisos.getOrDefault("sys_principal",false))
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

                                @if(permisos.getOrDefault("dat_gen_consulta_datos",false))
                                    <li>
                                        <a class="nav-link acceso link-body-emphasis text-decoration-none rounded " href="#" id="dat_gen_consulta_datos">
                                            &MediumSpace; Mantenimientos de Datos
                                        </a>
                                    </li>
                                @endif

                                @if(permisos.getOrDefault("sys_configuracion",false))
                                    <li>
                                        <a class="nav-link acceso link-body-emphasis text-decoration-none rounded " href="#" id="sys_configuracion">
                                            &MediumSpace; Configuración
                                        </a>
                                    </li>
                                @endif

                                @if(permisos.getOrDefault("usr_mgr_principal",false))
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

            @if(! datos_personales.equals(null) )
                <span class="fs-6 fw-semibold mt-2">${datos_personales.getNombre()} ${datos_personales.getApellido()}</span>
            @endif

        </div>
    </nav>
</div>
