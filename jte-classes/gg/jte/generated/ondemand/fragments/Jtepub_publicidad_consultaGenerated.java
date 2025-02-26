package gg.jte.generated.ondemand.fragments;
import abreuapps.core.control.general.PublicidadDTO;
import java.util.List;
@SuppressWarnings("unchecked")
public final class Jtepub_publicidad_consultaGenerated {
	public static final String JTE_NAME = "fragments/pub_publicidad_consulta.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,3,3,3,3,9,9,17,17,27,27,28,28,28,28,28,28,28,28,28,29,29,29,30,30,30,31,31,31,32,32,32,34,34,36,36,36,36,38,38,38,38,40,40,40,41,41,41,3,4,5,6,7,7,7,7};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, boolean pub_publicidad_consulta, boolean pub_publicidad_registro, String msg, Boolean status, List<PublicidadDTO> publicidades) {
		jteOutput.writeContent("\n");
		gg.jte.generated.ondemand.shared.JteconsultaGenerated.render(jteOutput, jteHtmlInterceptor, "Publicidad", "pub_publicidad_registro", pub_publicidad_consulta, pub_publicidad_registro, msg, status, true, new gg.jte.html.HtmlContent() {
			public void writeTo(gg.jte.html.HtmlTemplateOutput jteOutput) {
				jteOutput.writeContent("\n        <thead class=\"bg-secondary text-white\">\n        <tr>\n            <th scope=\"col\">ID</th>\n            <th scope=\"col\">Título</th>\n            <th scope=\"col\">Empresa</th>\n            <th scope=\"col\">Activo</th>\n        </tr>\n        </thead>\n        <tbody>\n        ");
				for (var d:publicidades) {
					jteOutput.writeContent("\n            <tr");
					var __jte_html_attribute_0 = d.id();
					if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_0)) {
						jteOutput.writeContent(" data-id=\"");
						jteOutput.setContext("tr", "data-id");
						jteOutput.writeUserContent(__jte_html_attribute_0);
						jteOutput.setContext("tr", null);
						jteOutput.writeContent("\"");
					}
					jteOutput.writeContent(">\n                <th scope=\"row\">");
					jteOutput.setContext("th", null);
					jteOutput.writeUserContent(d.id());
					jteOutput.writeContent("</th>\n                <td>");
					jteOutput.setContext("td", null);
					jteOutput.writeUserContent(d.tit());
					jteOutput.writeContent("</td>\n                <td>");
					jteOutput.setContext("td", null);
					jteOutput.writeUserContent(d.empresa_dat());
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
				if (pub_publicidad_registro) {
					jteOutput.writeContent(" <script src=\"/content/js/pub/pub794851.js\"></script> ");
				}
				jteOutput.writeContent("\n        <script src=\"/content/js/pub/es_pub794851.js\"></script>\n    ");
			}
		});
		jteOutput.writeContent("\n");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		boolean pub_publicidad_consulta = (boolean)params.get("pub_publicidad_consulta");
		boolean pub_publicidad_registro = (boolean)params.get("pub_publicidad_registro");
		String msg = (String)params.get("msg");
		Boolean status = (Boolean)params.get("status");
		List<PublicidadDTO> publicidades = (List<PublicidadDTO>)params.get("publicidades");
		render(jteOutput, jteHtmlInterceptor, pub_publicidad_consulta, pub_publicidad_registro, msg, status, publicidades);
	}
}
