package gg.jte.generated.ondemand.fragments;
import abreuapps.core.control.transporte.Parada;
import abreuapps.core.control.utils.DateUtils;
@SuppressWarnings("unchecked")
public final class Jtetrp_paradas_registroGenerated {
	public static final String JTE_NAME = "fragments/trp_paradas_registro.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,3,3,3,3,7,7,7,9,16,16,17,17,18,18,18,18,18,18,18,18,18,19,19,19,19,19,19,19,19,19,20,20,24,24,24,29,29,31,31,39,39,39,39,39,39,39,39,39,44,44,44,44,44,44,44,44,44,49,49,49,49,49,53,53,53,53,58,58,58,59,59,59,3,4,5,5,5,5};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, Boolean trp_paradas_registro, DateUtils dateUtils, Parada parada) {
		jteOutput.writeContent("\n");
		boolean update = parada.getId()!=null;
		jteOutput.writeContent("\n\n");
		gg.jte.generated.ondemand.shared.JteregistroGenerated.render(jteOutput, jteHtmlInterceptor, "Paradas", "trp_paradas_consulta", trp_paradas_registro, update, new gg.jte.html.HtmlContent() {
			public void writeTo(gg.jte.html.HtmlTemplateOutput jteOutput) {
				jteOutput.writeContent("\n        ");
				if (update) {
					jteOutput.writeContent("\n            <input  type=\"datetime\" name=\"fecha_actualizacionn\" style=\"display: none; width: 1px;height: 1px;\"");
					var __jte_html_attribute_0 = dateUtils.DateToFormato2(parada.getFecha_actualizacion());
					if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_0)) {
						jteOutput.writeContent(" value=\"");
						jteOutput.setContext("input", "value");
						jteOutput.writeUserContent(__jte_html_attribute_0);
						jteOutput.setContext("input", null);
						jteOutput.writeContent("\"");
					}
					jteOutput.writeContent(">\n            <input type=\"text\" style=\"display: none; width: 1px;height: 1px;\" id=\"idParada\" name=\"id\"");
					var __jte_html_attribute_1 = parada.getId();
					if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_1)) {
						jteOutput.writeContent(" value=\"");
						jteOutput.setContext("input", "value");
						jteOutput.writeUserContent(__jte_html_attribute_1);
						jteOutput.setContext("input", null);
						jteOutput.writeContent("\"");
					}
					jteOutput.writeContent(">\n        ");
				}
				jteOutput.writeContent("\n\n        <div class=\"mb-3 col-12\">\n            <label for=\"descripcion\" class=\"form-label text-muted\">Descripción:</label>\n            <textarea class=\"form-control\" name=\"descripción\" rows=\"2\" required maxlength=\"255\">");
				jteOutput.setContext("textarea", null);
				jteOutput.writeUserContent(parada.getDescripción());
				jteOutput.writeContent("</textarea>\n        </div>\n\n        <div class=\"col-12 text-start \">\n            <h5>Ubicación:</h5>\n            ");
				if (!update) {
					jteOutput.writeContent("\n                <label for=\"\" class=\"form-label text-muted\">Trate de colocar la parada del lado de la acera que corresponde. Nunca encima de la calle.</label>\n            ");
				}
				jteOutput.writeContent("\n        </div>\n        <div class=\"mb-3 col-12 d-flex justify-content-center\">\n            <div id=\"map\" class=\"map-regular\"></div>\n        </div>\n\n        <div class=\"col-6 justify-content-center mb-3\">\n            <label for=\"latitud\" class=\"form-label text-muted\">Latitud:</label>\n            <input type=\"number\" class=\"form-control\" name=\"latitud\"");
				var __jte_html_attribute_2 = parada.getLatitud();
				if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_2)) {
					jteOutput.writeContent(" value=\"");
					jteOutput.setContext("input", "value");
					jteOutput.writeUserContent(__jte_html_attribute_2);
					jteOutput.setContext("input", null);
					jteOutput.writeContent("\"");
				}
				jteOutput.writeContent(" step=\".00000000000000001\" required />\n        </div>\n\n        <div class=\"col-6 justify-content-center mb-3\">\n            <label for=\"longitud\" class=\"form-label text-muted\">Longitud:</label>\n            <input type=\"number\" class=\"form-control\" name=\"longitud\"");
				var __jte_html_attribute_3 = parada.getLongitud();
				if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_3)) {
					jteOutput.writeContent(" value=\"");
					jteOutput.setContext("input", "value");
					jteOutput.writeUserContent(__jte_html_attribute_3);
					jteOutput.setContext("input", null);
					jteOutput.writeContent("\"");
				}
				jteOutput.writeContent(" step=\".00000000000000001\" required />\n        </div>\n\n        <div class=\"col-6 d-flex justify-content-start\">\n            <div class=\"form-check\">\n                <input class=\"form-check-input\" type=\"checkbox\" ");
				var __jte_html_attribute_4 =  !update || parada.isActivo() ;
				if (__jte_html_attribute_4) {
				jteOutput.writeContent(" checked");
				}
				jteOutput.writeContent(" name=\"activo\" id=\"activo\">\n                <label class=\"form-check-label text-muted\" for=\"activo\">Activo</label>\n            </div>\n        </div>\n    ");
			}
		}, new gg.jte.html.HtmlContent() {
			public void writeTo(gg.jte.html.HtmlTemplateOutput jteOutput) {
				jteOutput.writeContent("\n        <script src=\"/content/js/trp/svd_pda574702.js\"></script>\n        <script src=\"/content/js/lib/leaflet.js\"></script>\n        <script src=\"/content/js/fetch_cache.js\"></script>\n        <script src=\"/content/js/trp/pda544702loc.js\"></script>\n    ");
			}
		}, true, true);
		jteOutput.writeContent("\n");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		Boolean trp_paradas_registro = (Boolean)params.get("trp_paradas_registro");
		DateUtils dateUtils = (DateUtils)params.get("dateUtils");
		Parada parada = (Parada)params.get("parada");
		render(jteOutput, jteHtmlInterceptor, trp_paradas_registro, dateUtils, parada);
	}
}
