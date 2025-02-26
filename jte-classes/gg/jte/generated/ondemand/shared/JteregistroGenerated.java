package gg.jte.generated.ondemand.shared;
import gg.jte.Content;
@SuppressWarnings("unchecked")
public final class JteregistroGenerated {
	public static final String JTE_NAME = "shared/registro.jte";
	public static final int[] JTE_LINE_INFO = {0,0,2,2,2,2,12,12,12,18,23,26,26,26,28,28,32,32,34,34,36,36,39,39,47,47,47,48,48,49,49,50,50,52,52,52,2,3,4,5,6,7,8,9,9,9,9};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, String titulo, String id_consulta, boolean permiso_registro, boolean update, Content content, Content scripts_content, boolean saveButton, boolean backButton) {
		jteOutput.writeContent("\n<main id=\"content-page\">\n    ");
		if (permiso_registro) {
			jteOutput.writeContent("\n        <div class=\"container-fluid px-4\">\n            <div class=\"row d-flex align-items-center justify-content-center mt-5 mb-5\">\n                <div class=\"card col-12 col-md-10 col-lg-8\">\n                    <div class=\"card-body mt-3 mb-3\">\n\n                        ");
			gg.jte.generated.ondemand.shared.Jtetitle_registroGenerated.render(jteOutput, jteHtmlInterceptor, titulo, id_consulta, update, backButton);
			jteOutput.writeContent("\n                        <form class=\"form-group\" id=\"form-guardar\">\n                            <div class=\"row mt-4\">\n                                ");
			jteOutput.setContext("div", null);
			jteOutput.writeUserContent(content);
			jteOutput.writeContent("\n\n                                ");
			if (saveButton) {
				jteOutput.writeContent("\n                                    <div class=\"col-6 d-flex justify-content-end\">\n\n                                        <button type=\"submit\" class=\"btn btn-primary\" id=\"btn_guardar\">\n                                            ");
				if (!update) {
					jteOutput.writeContent("\n                                                <b><i class=\"fa fa-save\"></i> Guardar</b>\n                                            ");
				} else {
					jteOutput.writeContent("\n                                                <b><i class=\"fa fa-pencil\"></i> Editar</b>\n                                            ");
				}
				jteOutput.writeContent("\n                                        </button>\n                                    </div>\n                                ");
			}
			jteOutput.writeContent("\n\n                            </div>\n                        </form>\n                    </div>\n                </div>\n            </div>\n        </div>\n        ");
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
		String id_consulta = (String)params.get("id_consulta");
		boolean permiso_registro = (boolean)params.get("permiso_registro");
		boolean update = (boolean)params.get("update");
		Content content = (Content)params.get("content");
		Content scripts_content = (Content)params.get("scripts_content");
		boolean saveButton = (boolean)params.getOrDefault("saveButton", true);
		boolean backButton = (boolean)params.getOrDefault("backButton", true);
		render(jteOutput, jteHtmlInterceptor, titulo, id_consulta, permiso_registro, update, content, scripts_content, saveButton, backButton);
	}
}
