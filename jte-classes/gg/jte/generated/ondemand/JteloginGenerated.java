package gg.jte.generated.ondemand;
@SuppressWarnings("unchecked")
public final class JteloginGenerated {
	public static final String JTE_NAME = "login.jte";
	public static final int[] JTE_LINE_INFO = {0,0,0,0,0,14,14,14,14,14,16,16,16,36,36,36,65,65,67,67,67,69,69,71,71,73,73,73,75,75,80,98,98,98,0,1,2,2,2,2};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, String error_msg, String success_msg, String app_nombre) {
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
		jteOutput.writeContent("</span></h2>\n                                            <img  width=\"64px\" height=\"64px\" class=\"me-4\" src=\"/content/assets/img/Omsafooter.webp\" alt=\"OMSA\"/>\n                                        </div>\n                                        <div class=\"form-floating mb-3\">\n                                            <input\n                                                class=\"form-control\"\n                                                id=\"username\"\n                                                name=\"username\"\n                                                type=\"text\"\n                                                placeholder=\"Nombre de Usuario\"\n                                                required=\"required\"\n                                                />\n                                            <label for=\"username\" class=\"text-muted\"\n                                                   >Nombre de Usuario</label\n                                            >\n                                        </div>\n                                        <div class=\"form-floating mb-3\">\n                                            <input\n                                                class=\"form-control\"\n                                                id=\"password\"\n                                                name=\"password\"\n                                                type=\"password\"\n                                                placeholder=\"Password\"\n                                                required=\"required\"\n                                                />\n                                            <label for=\"password\" class=\"text-muted\"\n                                                   >Contraseña</label\n                                            >\n                                        </div>\n                                        ");
		if (error_msg != null) {
			jteOutput.writeContent(" \n                                            <div class=\"alert alert-danger\">\n                                                <span>");
			jteOutput.setContext("span", null);
			jteOutput.writeUserContent(error_msg);
			jteOutput.writeContent("</span>\n                                            </div>\n                                        ");
		}
		jteOutput.writeContent("\n\n                                        ");
		if (success_msg != null) {
			jteOutput.writeContent(" \n                                            <div class=\"alert alert-success\">\n                                                <span>");
			jteOutput.setContext("span", null);
			jteOutput.writeUserContent(success_msg);
			jteOutput.writeContent("</span>\n                                            </div>\n                                        ");
		}
		jteOutput.writeContent("\n\n                                        <div\n                                            class=\"d-flex py-3 align-items-center justify-content-end mb-0\"\n                                            >\n                                            ");
		jteOutput.writeContent("\n                                            <button type=\"submit\" class=\"btn btn-primary\">\n                                                <b>\n                                                    Iniciar Sesión\n                                                </b>\n                                            </button>\n                                        </div>\n                                    </div>\n                                </form>\n                            </div>\n                        </div>\n                    </div>\n                </div>\n            </main>\n        </div>\n    </div>           \n</body>\n</html>\n");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		String error_msg = (String)params.get("error_msg");
		String success_msg = (String)params.get("success_msg");
		String app_nombre = (String)params.get("app_nombre");
		render(jteOutput, jteHtmlInterceptor, error_msg, success_msg, app_nombre);
	}
}
