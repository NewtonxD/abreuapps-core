package gg.jte.generated.ondemand.shared;
import gg.jte.Content;
@SuppressWarnings("unchecked")
public final class JteconsultaGenerated {
	public static final String JTE_NAME = "shared/consulta.jte";
	public static final int[] JTE_LINE_INFO = {0,0,2,2,2,2,13,13,13,19,23,25,25,29,29,33,33,34,34,34,36,36,40,40,46,46,46,47,47,48,48,49,49,51,51,51,2,3,4,5,6,7,8,9,10,10,10,10};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, String titulo, String id_registro, boolean permiso_consulta, boolean permiso_registro, String msg, Boolean status, boolean table_content, Content content, Content scripts_content) {
		jteOutput.writeContent("\n<main id=\"content-page\">\n    ");
		if (permiso_consulta) {
			jteOutput.writeContent("\n        <div class=\"container-fluid px-4\">\n            <div class=\"row d-flex align-items-center justify-content-center mt-5 mb-5\">\n                <div class=\"card col-12 col-md-10 col-lg-8\">\n                    <div class=\"card-body mt-3 mb-3\">\n\n                        ");
			gg.jte.generated.ondemand.shared.Jtetitle_consultaGenerated.render(jteOutput, jteHtmlInterceptor, titulo, id_registro, permiso_registro);
			jteOutput.writeContent("\n\n                        ");
			gg.jte.generated.ondemand.shared.JtemessageGenerated.render(jteOutput, jteHtmlInterceptor, msg, status);
			jteOutput.writeContent("\n\n                        <br>\n\n                        ");
			if (table_content) {
				jteOutput.writeContent("\n                            <div class=\"row\">\n                                <div class=\"table-responsive table-responsive-md\">\n                                    <table class=\"table table-hover mt-2\" id=\"table\">\n                        ");
			}
			jteOutput.writeContent("\n                                        ");
			jteOutput.setContext("table", null);
			jteOutput.writeUserContent(content);
			jteOutput.writeContent("\n\n                        ");
			if (table_content) {
				jteOutput.writeContent("\n                                    </table>\n                                </div>\n                            </div>\n                        ");
			}
			jteOutput.writeContent("\n\n                    </div>\n                </div>\n            </div>\n        </div>\n        ");
			jteOutput.setContext("main", null);
			jteOutput.writeUserContent(scripts_content);
			jteOutput.writeContent("\n    ");
		} else {
			jteOutput.writeContent("\n        ");
			gg.jte.generated.ondemand.error.Jte403Generated.render(jteOutput, jteHtmlInterceptor);
			jteOutput.writeContent("\n    ");
		}
		jteOutput.writeContent("\n</main>\n");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		String titulo = (String)params.get("titulo");
		String id_registro = (String)params.get("id_registro");
		boolean permiso_consulta = (boolean)params.get("permiso_consulta");
		boolean permiso_registro = (boolean)params.get("permiso_registro");
		String msg = (String)params.get("msg");
		Boolean status = (Boolean)params.get("status");
		boolean table_content = (boolean)params.getOrDefault("table_content", true);
		Content content = (Content)params.get("content");
		Content scripts_content = (Content)params.get("scripts_content");
		render(jteOutput, jteHtmlInterceptor, titulo, id_registro, permiso_consulta, permiso_registro, msg, status, table_content, content, scripts_content);
	}
}
