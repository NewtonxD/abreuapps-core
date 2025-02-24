package gg.jte.generated.ondemand.fragments;
import abreuapps.core.control.general.DatoDTO;
import java.util.List;
@SuppressWarnings("unchecked")
public final class Jtedat_gen_consulta_datosGenerated {
	public static final String JTE_NAME = "fragments/dat_gen_consulta_datos.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,3,3,3,3,10,10,18,18,28,28,29,29,29,29,29,29,29,29,29,30,30,30,31,31,31,32,32,32,33,33,33,35,35,37,37,37,37,40,40,42,42,43,43,43,44,44,44,3,4,5,6,7,7,7,7};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, Boolean dat_gen_consulta_datos, Boolean dat_gen_registro_datos, String msg, Boolean status, List<DatoDTO> datos) {
		jteOutput.writeContent("\n\n");
		gg.jte.generated.ondemand.shared.JteconsultaGenerated.render(jteOutput, jteHtmlInterceptor, "Datos Generales", "dat_gen_registro_datos", dat_gen_consulta_datos, dat_gen_registro_datos, msg, status, true, new gg.jte.html.HtmlContent() {
			public void writeTo(gg.jte.html.HtmlTemplateOutput jteOutput) {
				jteOutput.writeContent("\n        <thead class=\"bg-secondary text-white\">\n            <tr>\n                <th scope=\"col\">Dato</th>\n                <th scope=\"col\">Descripción</th>\n                <th scope=\"col\">Grupo</th>\n                <th scope=\"col\">Estado</th>\n            </tr>\n        </thead>\n        <tbody>\n            ");
				for (var d : datos) {
					jteOutput.writeContent("\n                <tr");
					var __jte_html_attribute_0 = d.dat();
					if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_0)) {
						jteOutput.writeContent(" data-id=\"");
						jteOutput.setContext("tr", "data-id");
						jteOutput.writeUserContent(__jte_html_attribute_0);
						jteOutput.setContext("tr", null);
						jteOutput.writeContent("\"");
					}
					jteOutput.writeContent(">\n                    <th scope=\"row\">");
					jteOutput.setContext("th", null);
					jteOutput.writeUserContent(d.dat());
					jteOutput.writeContent("</th>\n                    <td>");
					jteOutput.setContext("td", null);
					jteOutput.writeUserContent(d.dsc());
					jteOutput.writeContent("</td>\n                    <td>");
					jteOutput.setContext("td", null);
					jteOutput.writeUserContent(d.fat_dat());
					jteOutput.writeContent("</td>\n                    <td>");
					jteOutput.setContext("td", null);
					jteOutput.writeUserContent(d.act()?"Activo":"Inactivo");
					jteOutput.writeContent("</td>\n                </tr>\n            ");
				}
				jteOutput.writeContent("\n        </tbody>\n    ");
			}
		}, new gg.jte.html.HtmlContent() {
			public void writeTo(gg.jte.html.HtmlTemplateOutput jteOutput) {
				jteOutput.writeContent("\n        <script src=\"/content/js/tb_init.js\"></script>\n        <script src=\"/content/js/dtgnr/es_dt742520.js\"></script>\n        ");
				if (dat_gen_registro_datos) {
					jteOutput.writeContent("\n            <script src=\"/content/js/dtgnr/dt742520.js\"></script>\n        ");
				}
				jteOutput.writeContent("\n    ");
			}
		});
		jteOutput.writeContent("\n");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		Boolean dat_gen_consulta_datos = (Boolean)params.get("dat_gen_consulta_datos");
		Boolean dat_gen_registro_datos = (Boolean)params.get("dat_gen_registro_datos");
		String msg = (String)params.get("msg");
		Boolean status = (Boolean)params.get("status");
		List<DatoDTO> datos = (List<DatoDTO>)params.get("datos");
		render(jteOutput, jteHtmlInterceptor, dat_gen_consulta_datos, dat_gen_registro_datos, msg, status, datos);
	}
}
