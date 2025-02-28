package gg.jte.generated.ondemand.fragments;
import abreuapps.core.control.transporte.VehiculoDTO;
import java.util.List;
@SuppressWarnings("unchecked")
public final class Jtetrp_vehiculo_consultaGenerated {
	public static final String JTE_NAME = "fragments/trp_vehiculo_consulta.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,3,3,3,3,9,9,17,17,28,28,29,29,29,29,29,29,29,29,29,30,30,30,31,31,31,32,32,32,33,33,33,34,34,34,36,36,38,38,38,38,40,40,40,40,42,42,42,42,42,3,4,5,6,7,7,7,7};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, Boolean trp_vehiculo_consulta, Boolean trp_vehiculo_registro, String msg, Boolean status, List<VehiculoDTO> vehiculos) {
		jteOutput.writeContent("\n");
		gg.jte.generated.ondemand.shared.JteconsultaGenerated.render(jteOutput, jteHtmlInterceptor, "Vehiculos", "trp_vehiculo_registro", trp_vehiculo_consulta, trp_vehiculo_registro, msg, status, true, new gg.jte.html.HtmlContent() {
			public void writeTo(gg.jte.html.HtmlTemplateOutput jteOutput) {
				jteOutput.writeContent("\n        <thead class=\"bg-secondary text-white\">\n        <tr>\n            <th scope=\"col\">Placa</th>\n            <th scope=\"col\">Marca y Módelo</th>\n            <th scope=\"col\">Color</th>\n            <th scope=\"col\">Estado</th>\n            <th scope=\"col\">Activo</th>\n        </tr>\n        </thead>\n        <tbody>\n        ");
				for (var d: vehiculos) {
					jteOutput.writeContent("\n            <tr");
					var __jte_html_attribute_0 = d.pl();
					if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_0)) {
						jteOutput.writeContent(" data-id=\"");
						jteOutput.setContext("tr", "data-id");
						jteOutput.writeUserContent(__jte_html_attribute_0);
						jteOutput.setContext("tr", null);
						jteOutput.writeContent("\"");
					}
					jteOutput.writeContent(">\n                <th scope=\"row\">");
					jteOutput.setContext("th", null);
					jteOutput.writeUserContent(d.pl());
					jteOutput.writeContent("</th>\n                <td>");
					jteOutput.setContext("td", null);
					jteOutput.writeUserContent(d.marca_dat() + ' ' + d.modelo_dat());
					jteOutput.writeContent("</td>\n                <td>");
					jteOutput.setContext("td", null);
					jteOutput.writeUserContent(d.color_dat());
					jteOutput.writeContent("</td>\n                <td>");
					jteOutput.setContext("td", null);
					jteOutput.writeUserContent(d.estado_dat());
					jteOutput.writeContent("</td>\n                <td>");
					jteOutput.setContext("td", null);
					jteOutput.writeUserContent(d.act()?"Activo":"Inactivo");
					jteOutput.writeContent("</td>\n            </tr>\n        ");
				}
				jteOutput.writeContent("\n        </tbody>\n    ");
			}
		}, new gg.jte.html.HtmlContent() {
			public void writeTo(gg.jte.html.HtmlTemplateOutput jteOutput) {
				jteOutput.writeContent("\n        <script src=\"/content/js/tb_init.js\"></script>\n        ");
				if (trp_vehiculo_registro) {
					jteOutput.writeContent(" <script src=\"/content/js/trp/vhl574702.js\"></script> ");
				}
				jteOutput.writeContent("\n        <script src=\"/content/js/trp/es_vhl574702.js\"></script>\n    ");
			}
		});
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		Boolean trp_vehiculo_consulta = (Boolean)params.get("trp_vehiculo_consulta");
		Boolean trp_vehiculo_registro = (Boolean)params.get("trp_vehiculo_registro");
		String msg = (String)params.get("msg");
		Boolean status = (Boolean)params.get("status");
		List<VehiculoDTO> vehiculos = (List<VehiculoDTO>)params.get("vehiculos");
		render(jteOutput, jteHtmlInterceptor, trp_vehiculo_consulta, trp_vehiculo_registro, msg, status, vehiculos);
	}
}
