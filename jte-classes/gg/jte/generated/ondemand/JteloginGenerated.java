package gg.jte.generated.ondemand;
@SuppressWarnings("unchecked")
public final class JteloginGenerated {
	public static final String JTE_NAME = "login.jte";
	public static final int[] JTE_LINE_INFO = {0,0,0,0,0,15,15,15,15,15,17,17,17,37,37,37,46,46,46,46,46,46,46,46,46,49,49,49,57,57,57,57,57,57,57,57,57,60,60,60,62,62,64,64,64,66,66,67,67,69,69,69,71,71,75,78,78,78,93,93,93,0,1,2,3,3,3,3};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, String error_msg, String success_msg, String app_nombre, abreuapps.core.control.utils.Localizer localize) {
		jteOutput.writeContent("\n<!DOCTYPE html>\n<html lang=\"es\">\n<head>\n    <meta charset=\"utf-8\" />\n    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\" />\n    <meta\n        name=\"viewport\"\n        content=\"width=device-width, initial-scale=1, shrink-to-fit=no\"\n        />\n    <link rel=\"icon\" type=\"image/x-icon\" href=\"/content/favicon.ico\">\n    <meta name=\"description\" content=\"Login Sistema ERP ");
		jteOutput.setContext("meta", "content");
		jteOutput.writeUserContent(app_nombre);
		jteOutput.setContext("meta", null);
		jteOutput.writeContent("\" />\n    <meta name=\"author\" content=\"Carlos Isaac Abreu Pérez\" />\n    <title>Login ERP - ");
		jteOutput.setContext("title", null);
		jteOutput.writeUserContent(app_nombre);
		jteOutput.writeContent("</title>\n    <link href=\"/content/css/styles.css\" rel=\"stylesheet\" />\n    <link href=\"/content/css/bootstrap.min.css\" rel=\"stylesheet\" />\n</head>\n<body class=\"secondary-color fondo-bonito\">\n\n    <div id=\"layoutAuthentication\" class=\"h-100\">\n\n        <div id=\"layoutAuthentication_content\" class=\"h-100\">\n\n            <main class=\"h-100 d-flex align-items-center justify-content-center\">\n\n                <div class=\"container\"> \n                \n                   <div class=\"row h-100 d-flex align-items-center justify-content-center\">\n                        <div class=\"col-10 col-lg-5\">\n                            <div class=\"card shadow-lg border-0 rounded-lg mt-5\">\n                                <form method=\"post\">\n                                    <div class=\"card-body\">\n                                        <div class=\"d-flex justify-content-between align-middle py-3 mb-3\">\n                                            <h2 class=\"ms-2\">Login<br/><span>");
		jteOutput.setContext("span", null);
		jteOutput.writeUserContent(app_nombre);
		jteOutput.writeContent("</span></h2>\n                                            <img  width=\"64px\" height=\"64px\" class=\"me-4\" src=\"/content/assets/img/Omsafooter.webp\" alt=\"OMSA\"/>\n                                        </div>\n                                        <div class=\"form-floating mb-3\">\n                                            <input\n                                                class=\"form-control\"\n                                                id=\"username\"\n                                                name=\"username\"\n                                                type=\"text\"\n                                               ");
		var __jte_html_attribute_0 = localize.apply("username");
		if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_0)) {
			jteOutput.writeContent(" placeholder=\"");
			jteOutput.setContext("input", "placeholder");
			jteOutput.writeUserContent(__jte_html_attribute_0);
			jteOutput.setContext("input", null);
			jteOutput.writeContent("\"");
		}
		jteOutput.writeContent("\n                                                required=\"required\"\n                                                />\n                                            <label for=\"username\" class=\"text-muted\">");
		jteOutput.setContext("label", null);
		jteOutput.writeUserContent(localize.apply("username"));
		jteOutput.writeContent("</label>\n                                        </div>\n                                        <div class=\"form-floating mb-3\">\n                                            <input\n                                                class=\"form-control\"\n                                                id=\"password\"\n                                                name=\"password\"\n                                                type=\"password\"\n                                               ");
		var __jte_html_attribute_1 = localize.apply("password");
		if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_1)) {
			jteOutput.writeContent(" placeholder=\"");
			jteOutput.setContext("input", "placeholder");
			jteOutput.writeUserContent(__jte_html_attribute_1);
			jteOutput.setContext("input", null);
			jteOutput.writeContent("\"");
		}
		jteOutput.writeContent("\n                                                required=\"required\"\n                                                />\n                                            <label for=\"password\" class=\"text-muted\">");
		jteOutput.setContext("label", null);
		jteOutput.writeUserContent(localize.apply("password"));
		jteOutput.writeContent("</label>\n                                        </div>\n                                        ");
		if (error_msg != null) {
			jteOutput.writeContent("\n                                            <div class=\"alert alert-danger\">\n                                                <span>");
			jteOutput.setContext("span", null);
			jteOutput.writeUserContent(error_msg);
			jteOutput.writeContent("</span>\n                                            </div>\n                                        ");
		}
		jteOutput.writeContent("\n                                        ");
		if (success_msg != null) {
			jteOutput.writeContent(" \n                                            <div class=\"alert alert-success\">\n                                                <span>");
			jteOutput.setContext("span", null);
			jteOutput.writeUserContent(success_msg);
			jteOutput.writeContent("</span>\n                                            </div>\n                                        ");
		}
		jteOutput.writeContent("\n                                        <div\n                                            class=\"d-flex py-3 align-items-center justify-content-end mb-0\"\n                                            >\n                                            ");
		jteOutput.writeContent("\n                                            <button type=\"submit\" class=\"btn btn-primary\">\n                                                <b>\n                                                    ");
		jteOutput.setContext("b", null);
		jteOutput.writeUserContent(localize.apply("login"));
		jteOutput.writeContent("\n                                                </b>\n                                            </button>\n                                        </div>\n                                    </div>\n                                </form>\n                            </div>\n                        </div>\n                    </div>\n                </div>\n            </main>\n        </div>\n    </div>           \n</body>\n</html>\n");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		String error_msg = (String)params.get("error_msg");
		String success_msg = (String)params.get("success_msg");
		String app_nombre = (String)params.get("app_nombre");
		abreuapps.core.control.utils.Localizer localize = (abreuapps.core.control.utils.Localizer)params.get("localize");
		render(jteOutput, jteHtmlInterceptor, error_msg, success_msg, app_nombre, localize);
	}
}
