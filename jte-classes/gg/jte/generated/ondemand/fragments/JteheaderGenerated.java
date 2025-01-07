package gg.jte.generated.ondemand.fragments;
@SuppressWarnings("unchecked")
public final class JteheaderGenerated {
	public static final String JTE_NAME = "shared/header.jte";
	public static final int[] JTE_LINE_INFO = {1,1,1,1,1,4,4,7,7,7,9,18,39,39,39,1,1,1,1};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, String app_nombre) {
		jteOutput.writeContent("\n<nav class=\"sb-topnav navbar navbar-expand navbar-dark bg-dark align-center\">\n    ");
		jteOutput.writeContent("\n    <a class=\"navbar-brand ps-3\" href=\"/main/index\">\n        <img class=\"pb-2\" width=\"32px\" height=\"32px\" src=\"/content/assets/img/Omsafooter.webp\" />\n        <span class=\"fs-4 fw-semibold\">");
		jteOutput.setContext("span", null);
		jteOutput.writeUserContent(app_nombre);
		jteOutput.writeContent(" ERP</span>\n    </a>\n    ");
		jteOutput.writeContent("\n    <button\n        type=\"button\"\n        class=\"btn btn-link btn-sm order-1 order-lg-0 me-4 me-lg-0 toggle-btn text-white\"\n        onclick=\"toggleSidebar()\"\n    >\n        <i class=\"fa fa-bars\"></i>\n    </button>\n    <div class=\"d-none d-md-inline-block form-inline ms-auto me-0 me-md-3 my-2 my-md-0\"></div>\n    ");
		jteOutput.writeContent("\n    <ul class=\"navbar-nav ms-auto ms-md-0 me-3 me-lg-4\">\n        <li class=\"nav-item dropdown\">\n            <a\n                class=\"nav-link dropdown-toggle\"\n                id=\"navbarDropdown\"\n                href=\"#\"\n                role=\"button\"\n                data-bs-toggle=\"dropdown\"\n                aria-expanded=\"false\"\n            ><i class=\"fa fa-user fa-fw\"></i></a>\n            <ul class=\"dropdown-menu dropdown-menu-end\" aria-labelledby=\"navbarDropdown\">\n                <li><a class=\"dropdown-item config-user\" href=\"#\">Configuración</a></li>\n                <li><hr class=\"dropdown-divider\" /></li>\n                <li>\n                    <a class=\"dropdown-item\" href=\"/auth/logout\">Cerrar Sesión</a>\n                </li>\n            </ul>\n        </li>\n    </ul>\n</nav>\n");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		String app_nombre = (String)params.get("app_nombre");
		render(jteOutput, jteHtmlInterceptor, app_nombre);
	}
}
