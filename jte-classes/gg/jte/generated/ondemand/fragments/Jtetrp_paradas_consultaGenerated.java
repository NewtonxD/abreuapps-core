package gg.jte.generated.ondemand.fragments;
import abreuapps.core.control.transporte.ParadaDTO;
import java.util.List;
@SuppressWarnings("unchecked")
public final class Jtetrp_paradas_consultaGenerated {
	public static final String JTE_NAME = "fragments/trp_paradas_consulta.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,3,3,3,3,9,9,17,17,26,26,27,27,27,27,27,27,27,27,27,28,28,28,29,29,29,30,30,30,32,32,34,34,34,34,36,36,36,36,38,38,38,39,39,39,3,4,5,6,7,7,7,7};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, Boolean trp_paradas_consulta, Boolean trp_paradas_registro, String msg, Boolean status, List<ParadaDTO> paradas) {
		jteOutput.writeContent("\n");
		gg.jte.generated.ondemand.shared.JteconsultaGenerated.render(jteOutput, jteHtmlInterceptor, "Paradas", "trp_paradas_registro", trp_paradas_consulta, trp_paradas_registro, msg, status, true, new gg.jte.html.HtmlContent() {
			public void writeTo(gg.jte.html.HtmlTemplateOutput jteOutput) {
				jteOutput.writeContent("\n        <thead class=\"bg-secondary text-white\">\n        <tr>\n            <th scope=\"col\">ID</th>\n            <th scope=\"col\">Descripción</th>\n            <th scope=\"col\">Activo</th>\n        </tr>\n        </thead>\n        <tbody>\n            ");
				for (var d: paradas) {
					jteOutput.writeContent("\n                <tr");
					var __jte_html_attribute_0 = d.id();
					if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_0)) {
						jteOutput.writeContent(" data-id=\"");
						jteOutput.setContext("tr", "data-id");
						jteOutput.writeUserContent(__jte_html_attribute_0);
						jteOutput.setContext("tr", null);
						jteOutput.writeContent("\"");
					}
					jteOutput.writeContent(">\n                    <th scope=\"row\">");
					jteOutput.setContext("th", null);
					jteOutput.writeUserContent(d.id());
					jteOutput.writeContent("</th>\n                    <td>");
					jteOutput.setContext("td", null);
					jteOutput.writeUserContent(d.dsc());
					jteOutput.writeContent("</td>\n                    <td>");
					jteOutput.setContext("td", null);
					jteOutput.writeUserContent(d.act()?"Activo":"Inactivo");
					jteOutput.writeContent("</td>\n                </tr>\n            ");
				}
				jteOutput.writeContent("\n        </tbody>\n    ");
			}
		}, new gg.jte.html.HtmlContent() {
			public void writeTo(gg.jte.html.HtmlTemplateOutput jteOutput) {
				jteOutput.writeContent("\n        <script src=\"/content/js/tb_init.js\"></script>\n        ");
				if (trp_paradas_registro) {
					jteOutput.writeContent(" <script src=\"/content/js/trp/pda574702.js\"></script> ");
				}
				jteOutput.writeContent("\n        <script src=\"/content/js/trp/es_pda574702.js\"></script>\n    ");
			}
		});
		jteOutput.writeContent("\n");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		Boolean trp_paradas_consulta = (Boolean)params.get("trp_paradas_consulta");
		Boolean trp_paradas_registro = (Boolean)params.get("trp_paradas_registro");
		String msg = (String)params.get("msg");
		Boolean status = (Boolean)params.get("status");
		List<ParadaDTO> paradas = (List<ParadaDTO>)params.get("paradas");
		render(jteOutput, jteHtmlInterceptor, trp_paradas_consulta, trp_paradas_registro, msg, status, paradas);
	}
}
