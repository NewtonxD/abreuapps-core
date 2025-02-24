package gg.jte.generated.ondemand.shared;
@SuppressWarnings("unchecked")
public final class Jtetitle_registroGenerated {
	public static final String JTE_NAME = "shared/title_registro.jte";
	public static final int[] JTE_LINE_INFO = {0,0,0,0,0,7,7,7,11,11,11,11,15,15,17,17,17,18,18,18,18,21,21,21,0,1,2,3,3,3,3};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, String titulo, String permiso, boolean update, boolean backButton) {
		jteOutput.writeContent("\n<div class=\"row\">\n    <div class=\"col-12 d-flex align-items-start justify-content-start\">\n        ");
		if (backButton) {
			jteOutput.writeContent("\n            <button type=\"button\" class=\"btn btn-sm btn-secondary\"\n                    onclick=\"$(function () {\n                            event.preventDefault();\n                            cargar_contenido('");
			jteOutput.setContext("button", "onclick");
			jteOutput.writeUserContent(permiso);
			jteOutput.setContext("button", null);
			jteOutput.writeContent("');\n                        });\">\n                <b><i class=\"fa fa-chevron-left\"></i></b>\n            </button>\n        ");
		}
		jteOutput.writeContent("&nbsp;&nbsp;\n        <h4>\n            ");
		jteOutput.setContext("h4", null);
		jteOutput.writeUserContent(titulo);
		jteOutput.writeContent("\n            <i class=\"fa fa-");
		jteOutput.setContext("i", "class");
		jteOutput.writeUserContent(!update?"plus":"pencil");
		jteOutput.setContext("i", null);
		jteOutput.writeContent("\"></i>\n        </h4>\n    </div>\n</div>");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		String titulo = (String)params.get("titulo");
		String permiso = (String)params.get("permiso");
		boolean update = (boolean)params.get("update");
		boolean backButton = (boolean)params.get("backButton");
		render(jteOutput, jteHtmlInterceptor, titulo, permiso, update, backButton);
	}
}
