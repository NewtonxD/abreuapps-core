package gg.jte.generated.ondemand.fragments;
import abreuapps.core.control.transporte.Ruta;
import abreuapps.core.control.utils.DateUtils;
@SuppressWarnings("unchecked")
public final class Jtetrp_rutas_registroGenerated {
	public static final String JTE_NAME = "fragments/trp_rutas_registro.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,3,3,3,3,7,7,7,9,16,16,19,19,20,20,20,20,20,20,20,20,20,21,21,21,21,21,21,21,21,21,22,22,23,23,23,23,23,23,23,23,23,23,23,23,23,23,23,23,23,23,23,23,23,28,28,28,28,28,28,28,28,28,33,33,33,33,33,33,33,33,33,38,38,38,38,48,48,48,48,48,51,51,51,51,57,57,57,61,61,61,3,4,5,5,5,5};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, Boolean trp_rutas_registro, DateUtils dateUtils, Ruta ruta) {
		jteOutput.writeContent("\n");
		boolean update = ruta.getRuta()!=null;
		jteOutput.writeContent("\n\n");
		gg.jte.generated.ondemand.shared.JteregistroGenerated.render(jteOutput, jteHtmlInterceptor, "Rutas", "trp_rutas_consulta", trp_rutas_registro, update, new gg.jte.html.HtmlContent() {
			public void writeTo(gg.jte.html.HtmlTemplateOutput jteOutput) {
				jteOutput.writeContent("\n        <div class=\"mb-3 col-12 col-lg-8\">\n            <label for=\"ruta\" class=\"form-label text-muted\">Nombre de la Ruta:</label>\n            ");
				if (update) {
					jteOutput.writeContent("\n                <input  type=\"datetime\" name=\"fecha_actualizacionn\" style=\"display: none; width: 1px;height: 1px;\"");
					var __jte_html_attribute_0 = dateUtils.DateToFormato2(ruta.getFecha_actualizacion());
					if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_0)) {
						jteOutput.writeContent(" value=\"");
						jteOutput.setContext("input", "value");
						jteOutput.writeUserContent(__jte_html_attribute_0);
						jteOutput.setContext("input", null);
						jteOutput.writeContent("\"");
					}
					jteOutput.writeContent(">\n                <input type=\"text\" style=\"display: none; width: 1px;height: 1px;\" id=\"Ruta\" name=\"ruta\"");
					var __jte_html_attribute_1 = ruta.getRuta();
					if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_1)) {
						jteOutput.writeContent(" value=\"");
						jteOutput.setContext("input", "value");
						jteOutput.writeUserContent(__jte_html_attribute_1);
						jteOutput.setContext("input", null);
						jteOutput.writeContent("\"");
					}
					jteOutput.writeContent(">\n            ");
				}
				jteOutput.writeContent("\n            <input type=\"text\"");
				var __jte_html_attribute_2 = update?"":"ruta";
				if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_2)) {
					jteOutput.writeContent(" name=\"");
					jteOutput.setContext("input", "name");
					jteOutput.writeUserContent(__jte_html_attribute_2);
					jteOutput.setContext("input", null);
					jteOutput.writeContent("\"");
				}
				var __jte_html_attribute_3 = ruta.getRuta();
				if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_3)) {
					jteOutput.writeContent(" value=\"");
					jteOutput.setContext("input", "value");
					jteOutput.writeUserContent(__jte_html_attribute_3);
					jteOutput.setContext("input", null);
					jteOutput.writeContent("\"");
				}
				var __jte_html_attribute_4 = update;
				if (__jte_html_attribute_4) {
				jteOutput.writeContent(" disabled");
				}
				jteOutput.writeContent(" placeholder=\" ... \" class=\"form-control\" required aria-describedby=\"basic-addon4\">\n        </div>\n\n        <div class=\"mb-3 col-12 col-lg-6\">\n            <label for=\"localizacion_inicial\" class=\"form-label text-muted\">Lugar donde inicia:</label>\n            <input type=\"text\" name=\"localizacion_inicial\"");
				var __jte_html_attribute_5 = ruta.getLocalizacion_inicial();
				if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_5)) {
					jteOutput.writeContent(" value=\"");
					jteOutput.setContext("input", "value");
					jteOutput.writeUserContent(__jte_html_attribute_5);
					jteOutput.setContext("input", null);
					jteOutput.writeContent("\"");
				}
				jteOutput.writeContent(" placeholder=\" ... \" class=\"form-control\" required aria-describedby=\"basic-addon4\">\n        </div>\n\n        <div class=\"mb-3 col-12 col-lg-6\">\n            <label for=\"localizacion_final\" class=\"form-label text-muted\">Lugar donde termina:</label>\n            <input type=\"text\" name=\"localizacion_final\"");
				var __jte_html_attribute_6 = ruta.getLocalizacion_final();
				if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_6)) {
					jteOutput.writeContent(" value=\"");
					jteOutput.setContext("input", "value");
					jteOutput.writeUserContent(__jte_html_attribute_6);
					jteOutput.setContext("input", null);
					jteOutput.writeContent("\"");
				}
				jteOutput.writeContent(" placeholder=\" ... \" class=\"form-control\" required aria-describedby=\"basic-addon4\">\n        </div>\n\n        <div class=\"col-12 text-start \">\n            <h5>Ubicación:</h5>\n            ");
				if (!update) {
					jteOutput.writeContent(" <label for=\"\" class=\"form-label text-muted\">Coloque las líneas de la ruta sobre la calle</label> ");
				}
				jteOutput.writeContent("\n        </div>\n        <div class=\"mb-3 col-12 d-flex justify-content-center\">\n            <div id=\"map\" class=\"map-regular\"></div>\n        </div>\n\n        <button type=\"button\" id=\"getPolylineData\"  style=\"display: none; width: 1px;height: 1px;\" ></button>\n\n        <div class=\"col-6 d-flex justify-content-start\">\n            <div class=\"form-check\">\n                <input class=\"form-check-input\" type=\"checkbox\" ");
				var __jte_html_attribute_7 =  !update || ruta.isActivo() ;
				if (__jte_html_attribute_7) {
				jteOutput.writeContent(" checked");
				}
				jteOutput.writeContent(" name=\"activo\" id=\"activo\">\n                <label class=\"form-check-label text-muted\" for=\"activo\">Activo</label>\n            </div>\n        </div>");
			}
		}, new gg.jte.html.HtmlContent() {
			public void writeTo(gg.jte.html.HtmlTemplateOutput jteOutput) {
				jteOutput.writeContent("\n        <script src=\"/content/js/lib/leaflet.js\"></script>\n        <script src=\"/content/js/lib/leaflet-geoman.min.js\"></script>\n        <script src=\"/content/js/fetch_cache.js\"></script>\n        <script src=\"/content/js/trp/rta5984502loc.js\"></script>\n        <script src=\"/content/js/trp/svd_rta5984502.js\"></script>\n    ");
			}
		}, true, true);
		jteOutput.writeContent("\n\n\n\n");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		Boolean trp_rutas_registro = (Boolean)params.get("trp_rutas_registro");
		DateUtils dateUtils = (DateUtils)params.get("dateUtils");
		Ruta ruta = (Ruta)params.get("ruta");
		render(jteOutput, jteHtmlInterceptor, trp_rutas_registro, dateUtils, ruta);
	}
}
