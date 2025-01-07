package gg.jte.generated.ondemand.fragments;
import abreuapps.core.control.general.Persona;
import java.util.Map;
@SuppressWarnings("unchecked")
public final class JtemenuGenerated {
	public static final String JTE_NAME = "shared/menu.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,3,3,3,3,14,14,14,14,18,18,38,38,44,44,46,46,52,52,54,54,60,60,65,65,67,67,87,87,97,97,99,99,109,109,111,111,121,121,123,123,133,133,135,135,141,141,143,143,149,149,153,153,155,155,175,175,181,181,183,183,189,189,191,191,197,197,199,199,209,209,211,211,221,221,226,226,228,228,248,248,254,254,259,259,261,261,281,281,287,287,289,289,295,295,297,297,303,303,307,307,309,309,329,329,335,335,337,337,343,343,345,345,351,351,357,357,359,370,370,371,371,371,371,371,371,372,372,377,377,377,3,4,5,6,6,6,6};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, String app_nombre, Persona datos_personales, Map< String, Boolean > permisos, Map< String, String > conf) {
		jteOutput.writeContent("\n\n<div id=\"layoutSidenav_nav\">\n    <nav id=\"sidenavAccordion\" class=\"bg-dark sidebar flex-shrink-0 p-3\" data-bs-theme=\"dark\">\n        <div class=\"sb-sidenav-menu\">\n            <a class=\"d-flex align-items-center pb-3 mb-3 text-decoration-none text-white\" href=\"/main/index\">\n                <img class=\"me-3\" width=\"32px\" height=\"32px\" src=\"/content/assets/img/Omsafooter.webp\" />\n                <span class=\"fs-4 fw-semibold\">");
		jteOutput.setContext("span", null);
		jteOutput.writeUserContent(app_nombre);
		jteOutput.writeContent("</span>\n            </a>\n            <ul class=\"nav list-unstyled ps-0\">\n\n                ");
		if (permisos.get("trp_principal")) {
			jteOutput.writeContent(" \n                    <li class=\"mt-1\">\n                        <button\n                            class=\"nav-link btn btn-toggle d-inline-flex align-items-center rounded border-0 collapsed\"\n                            type=\"button\"\n                            data-bs-toggle=\"collapse\"\n                            data-bs-target=\"#collapseLayouts4\"\n                            aria-expanded=\"false\"\n                            aria-controls=\"collapseLayouts4\"\n                        >\n                            <i class=\"fa fa-car\"></i>&MediumSpace; Transporte\n                        </button>\n                        <div\n                            class=\"collapse\"\n                            id=\"collapseLayouts4\"\n                            aria-labelledby=\"headingFour\"\n                            data-bs-parent=\"#sidenavAccordion\"\n                        >\n                            <ul class=\"nav btn-toggle-nav list-unstyled fw-normal pb-1 d-block\">\n\n                                ");
			if (permisos.get("trp_vehiculo_consulta")) {
				jteOutput.writeContent(" \n                                    <li>\n                                        <a class=\"nav-link acceso link-body-emphasis d-inline-flex text-decoration-none rounded\" href=\"#\" id=\"trp_vehiculo_consulta\">\n                                            &MediumSpace; Vehículos\n                                        </a>\n                                    </li>\n                                ");
			}
			jteOutput.writeContent("\n\n                                ");
			if (permisos.get("trp_rutas_consulta")) {
				jteOutput.writeContent(" \n                                    <li>\n                                        <a class=\"nav-link acceso link-body-emphasis d-inline-flex text-decoration-none rounded\" href=\"#\" id=\"trp_rutas_consulta\">\n                                            &MediumSpace; Rutas\n                                        </a>\n                                    </li>\n                                ");
			}
			jteOutput.writeContent("\n\n                                ");
			if (permisos.get("trp_paradas_consulta")) {
				jteOutput.writeContent(" \n                                    <li>\n                                        <a class=\"nav-link acceso link-body-emphasis d-inline-flex text-decoration-none rounded\" href=\"#\" id=\"trp_paradas_consulta\">\n                                            &MediumSpace; Paradas\n                                        </a>\n                                    </li>\n                                ");
			}
			jteOutput.writeContent("\n\n                            </ul>\n                        </div>\n                    </li>\n                ");
		}
		jteOutput.writeContent("\n\n                ");
		if (permisos.get("con_principal")) {
			jteOutput.writeContent("\n                    <li>\n                        <button\n                            class=\"nav-link btn btn-toggle d-inline-flex align-items-center rounded border-0 collapsed\"\n                            type=\"button\"\n                            data-bs-toggle=\"collapse\"\n                            data-bs-target=\"#collapseLayouts1\"\n                            aria-expanded=\"false\"\n                            aria-controls=\"collapseLayouts1\"\n                            >\n                            <i class=\"fa fa-calculator\"></i>&MediumSpace; Contabilidad\n                        </button>\n                        <div\n                            class=\"collapse\"\n                            id=\"collapseLayouts1\"\n                            aria-labelledby=\"headingOne\"\n                            data-bs-parent=\"#sidenavAccordion\"\n                            >\n                            <ul class=\"nav btn-toggle-nav list-unstyled fw-normal pb-1 d-block\">\n                                \n                                ");
			if (permisos.get("con_registro_transacciones")) {
				jteOutput.writeContent("\n                                    <li>\n                                        <a\n                                            class=\"nav-link acceso  link-body-emphasis d-inline-flex text-decoration-none rounded\"\n                                            href=\"#\"\n                                            id=\"con_registro_transacciones\"\n                                            >\n                                            &MediumSpace; Registro de Transacciones\n                                        </a>\n                                    </li>\n                                ");
			}
			jteOutput.writeContent("\n                                \n                                ");
			if (permisos.get("con_conciliacion_bancaria")) {
				jteOutput.writeContent("\n                                    <li>\n                                        <a\n                                            class=\"nav-link acceso  link-body-emphasis d-inline-flex text-decoration-none rounded\"\n                                            href=\"#\"\n                                            id=\"con_conciliacion_bancaria\"\n                                            >\n                                            &MediumSpace; Conciliación Bancaría\n                                        </a>\n                                    </li>\n                                ");
			}
			jteOutput.writeContent("   \n\n                                ");
			if (permisos.get("con_cuentas_bancarias")) {
				jteOutput.writeContent("\n                                    <li>\n                                        <a\n                                            class=\"nav-link acceso  link-body-emphasis d-inline-flex text-decoration-none rounded\"\n                                            href=\"#\"\n                                            id=\"con_cuentas_bancarias\"\n                                            >\n                                            &MediumSpace; Cuentas Bancanrtías\n                                        </a>\n                                    </li>\n                                ");
			}
			jteOutput.writeContent("\n\n                                ");
			if (permisos.get("con_presupuesto")) {
				jteOutput.writeContent("\n                                    <li>\n                                        <a\n                                            class=\"nav-link acceso  link-body-emphasis d-inline-flex text-decoration-none rounded\"\n                                            href=\"#\"\n                                            id=\"con_presupuesto\"\n                                            >\n                                            &MediumSpace; Gestión de Presupuesto\n                                        </a>\n                                    </li>\n                                ");
			}
			jteOutput.writeContent("\n\n                                ");
			if (permisos.get("con_impuestos")) {
				jteOutput.writeContent("\n                                    <li>\n                                        <a class=\"nav-link acceso  link-body-emphasis d-inline-flex text-decoration-none rounded\" href=\"#\" id=\"con_impuestos\">\n                                            &MediumSpace; Gestión de Impuestos\n                                        </a>\n                                    </li>\n                                ");
			}
			jteOutput.writeContent("\n\n                                ");
			if (permisos.get("con_informes")) {
				jteOutput.writeContent("\n                                    <li>\n                                        <a class=\"nav-link acceso  link-body-emphasis d-inline-flex text-decoration-none rounded\" href=\"#\" id=\"con_informes\">\n                                            &MediumSpace; Informes Financieros\n                                        </a>\n                                    </li>\n                                ");
			}
			jteOutput.writeContent("\n                            </ul>\n                        </div>\n                    </li>\n                ");
		}
		jteOutput.writeContent("\n\n                ");
		if (permisos.get("cxp_principal")) {
			jteOutput.writeContent("\n                    <li>\n                        <button\n                            class=\"nav-link btn btn-toggle d-inline-flex align-items-center rounded border-0 collapsed\"\n                            type=\"button\"\n                            data-bs-toggle=\"collapse\"\n                            data-bs-target=\"#collapseLayouts2\"\n                            aria-expanded=\"false\"\n                            aria-controls=\"collapseLayouts2\"\n                            >\n                            <i class=\"fa fa-list-alt\"></i>&MediumSpace; Cuentas x Pagar\n                        </button>\n                        <div\n                            class=\"collapse\"\n                            id=\"collapseLayouts2\"\n                            aria-labelledby=\"headingTwo\"\n                            data-bs-parent=\"#sidenavAccordion\"\n                            >\n                            <ul class=\"nav btn-toggle-nav list-unstyled fw-normal pb-1 d-block\">\n                                \n                                ");
			if (permisos.get("cxp_registro_facturas")) {
				jteOutput.writeContent("\n                                    <li>\n                                        <a class=\"nav-link acceso link-body-emphasis d-inline-flex text-decoration-none rounded \" href=\"#\" id=\"cxp_registro_facturas\">\n                                            &MediumSpace; Registro de Facturas\n                                        </a>\n                                    </li>\n                                ");
			}
			jteOutput.writeContent("\n\n                                ");
			if (permisos.get("cxp_control_pagos")) {
				jteOutput.writeContent("\n                                    <li>\n                                        <a class=\"nav-link acceso link-body-emphasis d-inline-flex text-decoration-none rounded \" href=\"#\" id=\"cxp_control_pagos\">\n                                            &MediumSpace; Control de Pagos\n                                        </a>\n                                    </li>\n                                ");
			}
			jteOutput.writeContent("\n\n                                ");
			if (permisos.get("cxp_informes")) {
				jteOutput.writeContent("\n                                    <li>\n                                        <a class=\"nav-link acceso link-body-emphasis d-inline-flex text-decoration-none rounded \" href=\"#\" id=\"cxp_informes\">\n                                            &MediumSpace; Informes y Análisis\n                                        </a>\n                                    </li>\n                                ");
			}
			jteOutput.writeContent("\n\n                                ");
			if (permisos.get("cxp_comunicacion_proveedores")) {
				jteOutput.writeContent("\n                                    <li>\n                                        <a\n                                            class=\"nav-link acceso link-body-emphasis d-inline-flex text-decoration-none rounded \"\n                                            href=\"#\"\n                                            id=\"cxp_comunicacion_proveedores\"\n                                            >\n                                            &MediumSpace; Comunicación con Proveedores\n                                        </a>\n                                    </li>\n                                ");
			}
			jteOutput.writeContent("\n\n                                ");
			if (permisos.get("cxp_autorizaciones_aprobaciones")) {
				jteOutput.writeContent("\n                                    <li>\n                                        <a\n                                            class=\"nav-link acceso link-body-emphasis d-inline-flex text-decoration-none rounded \"\n                                            href=\"#\"\n                                            id=\"cxp_autorizaciones_aprobaciones\"\n                                            >\n                                            &MediumSpace; Autorizaciones y Aprobaciones\n                                        </a>\n                                    </li>\n                                ");
			}
			jteOutput.writeContent("\n\n                            </ul>\n                        </div> \n                    </li>\n                ");
		}
		jteOutput.writeContent("\n\n                ");
		if (permisos.get("inv_principal")) {
			jteOutput.writeContent("\n                    <li class=\"me-1\">                       \n                        <button\n                            class=\"nav-link btn btn-toggle d-inline-flex align-items-center rounded border-0 collapsed \"\n                            type=\"button\"\n                            data-bs-toggle=\"collapse\"\n                            data-bs-target=\"#collapseLayouts6\"\n                            aria-expanded=\"false\"\n                            aria-controls=\"collapseLayouts6\"\n                            >\n                            <i class=\"fa fa-solid fa-archive\"></i>&MediumSpace; Inventario\n                        </button>\n                        <div\n                            class=\"collapse\"\n                            id=\"collapseLayouts6\"\n                            aria-labelledby=\"headingSix\"\n                            data-bs-parent=\"#sidenavAccordion\"\n                            >\n                            <ul class=\"nav btn-toggle-nav list-unstyled fw-normal pb-1 d-block\">\n                                \n                                ");
			if (permisos.get("inv_producto_consulta")) {
				jteOutput.writeContent("\n                                    <li>\n                                        <a class=\"nav-link acceso link-body-emphasis text-decoration-none rounded \" href=\"#\" id=\"inv_producto_consulta\">\n                                            &MediumSpace; Productos\n                                        </a>\n                                    </li>\n                                ");
			}
			jteOutput.writeContent("\n\n                            </ul>\n                        </div>\n                    </li>\n                ");
		}
		jteOutput.writeContent("\n\n                ");
		if (permisos.get("pub_principal")) {
			jteOutput.writeContent("\n                    <li class=\"me-1\">                       \n                        <button\n                            class=\"nav-link btn btn-toggle d-inline-flex align-items-center rounded border-0 collapsed \"\n                            type=\"button\"\n                            data-bs-toggle=\"collapse\"\n                            data-bs-target=\"#collapseLayouts5\"\n                            aria-expanded=\"false\"\n                            aria-controls=\"collapseLayouts5\"\n                            >\n                            <i class=\"fa fa-solid fa-bullhorn\"></i>&MediumSpace; Publicidad\n                        </button>\n                        <div\n                            class=\"collapse\"\n                            id=\"collapseLayouts5\"\n                            aria-labelledby=\"headingFifth\"\n                            data-bs-parent=\"#sidenavAccordion\"\n                            >\n                            <ul class=\"nav btn-toggle-nav list-unstyled fw-normal pb-1 d-block\">\n\n                                ");
			if (permisos.get("pub_publicidad_consulta")) {
				jteOutput.writeContent("\n                                    <li>\n                                        <a class=\"nav-link acceso link-body-emphasis text-decoration-none rounded \" href=\"#\" id=\"pub_publicidad_consulta\">\n                                            &MediumSpace; Publicidad\n                                        </a>\n                                    </li>\n                                ");
			}
			jteOutput.writeContent("\n\n                                ");
			if (permisos.get("dat_gen_consulta_empresa")) {
				jteOutput.writeContent("\n                                    <li>\n                                        <a class=\"nav-link acceso link-body-emphasis text-decoration-none rounded \" href=\"#\" id=\"dat_gen_consulta_empresa\">\n                                            &MediumSpace; Empresas\n                                        </a>\n                                    </li>\n                                ");
			}
			jteOutput.writeContent("\n\n                                ");
			if (permisos.get("pub_publicidad_reportes")) {
				jteOutput.writeContent("\n                                    <li>\n                                        <a class=\"nav-link acceso link-body-emphasis text-decoration-none rounded \" href=\"#\" id=\"pub_publicidad_reportes\">\n                                            &MediumSpace; Reportes\n                                        </a>\n                                    </li>\n                                ");
			}
			jteOutput.writeContent("\n                            </ul>\n                        </div>\n                    </li>\n                ");
		}
		jteOutput.writeContent("\n\n                ");
		if (permisos.get("sys_principal")) {
			jteOutput.writeContent("\n                    <li class=\"mb-1\">                       \n                        <button\n                            class=\"nav-link btn btn-toggle d-inline-flex align-items-center rounded border-0 collapsed \"\n                            type=\"button\"\n                            data-bs-toggle=\"collapse\"\n                            data-bs-target=\"#collapseLayouts3\"\n                            aria-expanded=\"false\"\n                            aria-controls=\"collapseLayouts3\"\n                            >\n                            <i class=\"fa fa-solid fa-gear\"></i>&MediumSpace; Sistemas\n                        </button>\n                        <div\n                            class=\"collapse\"\n                            id=\"collapseLayouts3\"\n                            aria-labelledby=\"headingThree\"\n                            data-bs-parent=\"#sidenavAccordion\"\n                            >\n                            <ul class=\"nav btn-toggle-nav list-unstyled fw-normal pb-1 d-block\">\n\n                                ");
			if (permisos.get("dat_gen_consulta_datos")) {
				jteOutput.writeContent("\n                                    <li>\n                                        <a class=\"nav-link acceso link-body-emphasis text-decoration-none rounded \" href=\"#\" id=\"dat_gen_consulta_datos\">\n                                            &MediumSpace; Mantenimientos de Datos\n                                        </a>\n                                    </li>\n                                ");
			}
			jteOutput.writeContent("\n\n                                ");
			if (permisos.get("sys_configuracion")) {
				jteOutput.writeContent("\n                                    <li>\n                                        <a class=\"nav-link acceso link-body-emphasis text-decoration-none rounded \" href=\"#\" id=\"sys_configuracion\">\n                                            &MediumSpace; Configuración\n                                        </a>\n                                    </li>\n                                ");
			}
			jteOutput.writeContent("\n\n                                ");
			if (permisos.get("usr_mgr_principal")) {
				jteOutput.writeContent("\n                                    <li>\n                                        <a class=\"nav-link acceso link-body-emphasis text-decoration-none rounded \" href=\"#\" id=\"usr_mgr_principal\">\n                                            &MediumSpace; Usuarios\n                                        </a>\n                                    </li>\n                                ");
			}
			jteOutput.writeContent("\n\n                            </ul>\n                        </div>\n                    </li>\n\n                ");
		}
		jteOutput.writeContent("\n\n                ");
		jteOutput.writeContent("\n            </ul>\n        </div>\n\n        <div class=\"sb-sidenav-footer position-fixed bottom-0 start-0 p-3\">\n            <div class=\"fs-6\">Bienvenido:&MediumSpace;\n                <a class=\"btn btn-secondary btn-sm config-user\" href=\"#\"><i class=\"fa fa-cog\"></i></a>\n                &nbsp;\n                <a class=\"btn btn-danger btn-sm\" href=\"/auth/logout\"><i class=\"fa fa-sign-out\"></i></a>\n            </div>\n\n            ");
		if (! datos_personales.equals(null) ) {
			jteOutput.writeContent("\n                <span class=\"fs-6 fw-semibold mt-2\">");
			jteOutput.setContext("span", null);
			jteOutput.writeUserContent(datos_personales.getNombre());
			jteOutput.writeContent(" ");
			jteOutput.setContext("span", null);
			jteOutput.writeUserContent(datos_personales.getApellido());
			jteOutput.writeContent("</span>\n            ");
		}
		jteOutput.writeContent("\n\n        </div>\n    </nav>\n</div>\n");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		String app_nombre = (String)params.get("app_nombre");
		Persona datos_personales = (Persona)params.get("datos_personales");
		Map< String, Boolean > permisos = (Map< String, Boolean >)params.get("permisos");
		Map< String, String > conf = (Map< String, String >)params.get("conf");
		render(jteOutput, jteHtmlInterceptor, app_nombre, datos_personales, permisos, conf);
	}
}
