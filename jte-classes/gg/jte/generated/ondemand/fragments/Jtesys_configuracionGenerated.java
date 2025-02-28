package gg.jte.generated.ondemand.fragments;
import abreuapps.core.control.general.ConfDTO;
import java.util.List;
@SuppressWarnings("unchecked")
public final class Jtesys_configuracionGenerated {
	public static final String JTE_NAME = "fragments/sys_configuracion.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,3,3,3,3,8,8,17,17,20,20,22,22,22,23,23,23,23,23,23,23,23,23,23,23,23,23,23,23,23,23,23,23,23,23,23,23,23,23,25,25,31,31,31,31,33,33,33,34,34,34,3,4,5,6,6,6,6};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, Boolean sys_configuracion, String msg, Boolean status, List<ConfDTO> conf) {
		jteOutput.writeContent("\n");
		gg.jte.generated.ondemand.shared.JteconsultaGenerated.render(jteOutput, jteHtmlInterceptor, "Configuración de Sistema", "", sys_configuracion, false, msg, status, false, new gg.jte.html.HtmlContent() {
			public void writeTo(gg.jte.html.HtmlTemplateOutput jteOutput) {
				jteOutput.writeContent("\n        <div class=\"row m-4 pb-4\">\n            <form id=\"form-guardar\">\n                ");
				for (var c: conf) {
					jteOutput.writeContent("\n                    <div class=\"col-auto mb-2 justify-content-start\">\n                        <label class=\"form-label\" >- ");
					jteOutput.setContext("label", null);
					jteOutput.writeUserContent(c.dsc());
					jteOutput.writeContent(":</label>\n                        <input type=\"text\" class=\"form-control\"");
					var __jte_html_attribute_0 = c.cod();
					if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_0)) {
						jteOutput.writeContent(" name=\"");
						jteOutput.setContext("input", "name");
						jteOutput.writeUserContent(__jte_html_attribute_0);
						jteOutput.setContext("input", null);
						jteOutput.writeContent("\"");
					}
					var __jte_html_attribute_1 = c.cod();
					if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_1)) {
						jteOutput.writeContent(" id=\"");
						jteOutput.setContext("input", "id");
						jteOutput.writeUserContent(__jte_html_attribute_1);
						jteOutput.setContext("input", null);
						jteOutput.writeContent("\"");
					}
					var __jte_html_attribute_2 = c.val();
					if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_2)) {
						jteOutput.writeContent(" value=\"");
						jteOutput.setContext("input", "value");
						jteOutput.writeUserContent(__jte_html_attribute_2);
						jteOutput.setContext("input", null);
						jteOutput.writeContent("\"");
					}
					jteOutput.writeContent(" />\n                    </div>\n                ");
				}
				jteOutput.writeContent("\n                <div class=\"col-12 mb-2 justify-content-center\">\n                    <button type=\"submit\" class=\"btn btn-primary\"><i class=\"fa fa-save\"></i> Guardar</button>\n                </div>\n            </form>\n        </div>\n    ");
			}
		}, new gg.jte.html.HtmlContent() {
			public void writeTo(gg.jte.html.HtmlTemplateOutput jteOutput) {
				jteOutput.writeContent("\n        <script src=\"/content/js/conf/svd_conf345612.js\"></script>\n    ");
			}
		});
		jteOutput.writeContent("\n");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		Boolean sys_configuracion = (Boolean)params.get("sys_configuracion");
		String msg = (String)params.get("msg");
		Boolean status = (Boolean)params.get("status");
		List<ConfDTO> conf = (List<ConfDTO>)params.get("conf");
		render(jteOutput, jteHtmlInterceptor, sys_configuracion, msg, status, conf);
	}
}
