package gg.jte.generated.ondemand.shared;
@SuppressWarnings("unchecked")
public final class Jtetitle_consultaGenerated {
	public static final String JTE_NAME = "shared/title_consulta.jte";
	public static final int[] JTE_LINE_INFO = {0,0,0,0,0,7,7,7,7,11,11,18,18,18,18,21,21,23,23,23,0,1,2,2,2,2};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, String titulo, String id, boolean permiso) {
		jteOutput.writeContent("\n<div class=\"row\">\n    <div class=\"col-8 d-flex align-items-start justify-content-start\">\n        <h4>\n            ");
		jteOutput.setContext("h4", null);
		jteOutput.writeUserContent(titulo);
		jteOutput.writeContent("&nbsp;\n        </h4>\n    </div>\n    <div class=\"col-4 d-flex align-items-end justify-content-end\">\n        ");
		if (permiso) {
			jteOutput.writeContent("\n            <button\n                    type=\"button\"\n                    class=\"btn btn-success\"\n                    onclick=\"\n                        event.preventDefault();\n                        closeEventSource();\n                        cargar_contenido('");
			jteOutput.setContext("button", "onclick");
			jteOutput.writeUserContent(id);
			jteOutput.setContext("button", null);
			jteOutput.writeContent("');\"\n            ><b><i class=\"fa fa-plus\"></i></b>\n            </button>\n        ");
		}
		jteOutput.writeContent("\n    </div>\n</div>");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		String titulo = (String)params.get("titulo");
		String id = (String)params.get("id");
		boolean permiso = (boolean)params.get("permiso");
		render(jteOutput, jteHtmlInterceptor, titulo, id, permiso);
	}
}
