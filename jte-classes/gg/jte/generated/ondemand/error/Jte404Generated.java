package gg.jte.generated.ondemand.error;
@SuppressWarnings("unchecked")
public final class Jte404Generated {
	public static final String JTE_NAME = "error/404.jte";
	public static final int[] JTE_LINE_INFO = {37,37,37,37,37,37,37,37,37,37,37,37};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor) {
		jteOutput.writeContent("<!DOCTYPE html>\n<html lang=\"es\">\n    <head>\n        <meta charset=\"utf-8\" />\n        <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\" />\n        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1, shrink-to-fit=no\" />\n        <meta name=\"description\" content=\"\" />\n        <meta name=\"author\" content=\"\" />\n        <title>404 Error - Proyecto</title>\n        <link rel=\"icon\" type=\"image/x-icon\" href=\"/content/favicon.ico\">\n        <link href=\"/content/css/styles.css\" rel=\"stylesheet\" />\n        <link href=\"/content/css/bootstrap.min.css\" rel=\"stylesheet\" />\n        <link rel=\"stylesheet\" href=\"/content/css/font-awesome.min.css\" />\n    </head>\n    <body>\n        <div id=\"layoutError\">\n            <div id=\"layoutError_content\">\n                <main>\n                    <div class=\"container\">\n                        <div class=\"row justify-content-center\">\n                            <div class=\"col-lg-6\">\n                                <div class=\"text-center mt-4\">\n                                    <img class=\"mb-4 img-error\" width=\"250\" height=\"250\" src=\"/content/assets/img/error-404-monochrome.svg\" />\n                                    <p class=\"lead\">La dirección URL no fue encontrada en el servidor</p>\n                                    <a href=\"/auth/login\">\n                                        <i class=\"fa fa-arrow-left me-1\"></i>\n                                        Volver a Inicio\n                                    </a>\n                                </div>\n                            </div>\n                        </div>\n                    </div>\n                </main>\n            </div>\n        </div>\n    </body>\n</html>\n");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		render(jteOutput, jteHtmlInterceptor);
	}
}
