package gg.jte.generated.ondemand.fragments;
import abreuapps.core.control.general.DatoDTO;
import abreuapps.core.control.general.Dato;
import abreuapps.core.control.utils.DateUtils;
import java.util.List;
@SuppressWarnings("unchecked")
public final class Jtedat_gen_registro_datosGenerated {
	public static final String JTE_NAME = "fragments/dat_gen_registro_datos.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,2,3,5,5,5,5,11,11,18,18,19,19,28,28,29,29,30,30,30,30,30,30,30,30,30,30,30,30,31,31,32,32,35,35,36,36,36,36,36,36,36,36,36,37,37,37,37,37,37,37,37,37,38,38,38,38,38,38,38,38,38,42,42,42,42,42,42,42,42,42,48,48,50,50,51,51,51,51,51,51,51,51,51,51,51,51,52,52,56,56,60,60,60,64,64,64,64,64,68,68,68,68,70,70,70,70,70,5,6,7,8,9,9,9,9};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, Boolean dat_gen_registro_datos, Boolean update, DateUtils dateUtils, List<DatoDTO> grupos, Dato dato) {
		jteOutput.writeContent("\n");
		gg.jte.generated.ondemand.shared.JteregistroGenerated.render(jteOutput, jteHtmlInterceptor, "Datos Generales", "dat_gen_consulta_datos", dat_gen_registro_datos, update, new gg.jte.html.HtmlContent() {
			public void writeTo(gg.jte.html.HtmlTemplateOutput jteOutput) {
				jteOutput.writeContent("\n        ");
				if (!update) {
					jteOutput.writeContent("\n            <div class=\"mb-3 col-12 col-md-6\">\n                <label for=\"nom-dato\" class=\"form-label text-muted\">Nombre del Dato:</label>\n                <input type=\"text\" id=\"nom-dato\" name=\"dato\" placeholder=\" ... \" class=\"form-control\" required aria-describedby=\"basic-addon4\">\n            </div>\n            <div class=\"mb-3 col-12 col-md-6\">\n                <label for=\"grupo\" class=\"form-label text-muted\">Grupo:</label>\n                <select class=\"form-select custom-select\" id=\"dato_padre\" name=\"dato_padre\" required>\n                    <option value=\"0\" selected disabled>--Seleccione--</option>\n                    ");
					for (var g : grupos) {
						jteOutput.writeContent("\n                        ");
						if (g.act()) {
							jteOutput.writeContent("\n                            <option");
							var __jte_html_attribute_0 = g.dat();
							if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_0)) {
								jteOutput.writeContent(" value=\"");
								jteOutput.setContext("option", "value");
								jteOutput.writeUserContent(__jte_html_attribute_0);
								jteOutput.setContext("option", null);
								jteOutput.writeContent("\"");
							}
							jteOutput.writeContent(" >");
							jteOutput.setContext("option", null);
							jteOutput.writeUserContent(g.dat());
							jteOutput.writeContent("</option>\n                        ");
						}
						jteOutput.writeContent("\n                    ");
					}
					jteOutput.writeContent("\n                </select>\n            </div>\n        ");
				} else {
					jteOutput.writeContent("\n            <input  type=\"datetime\" name=\"fecha_actualizacionn\" style=\"display: none; width: 1px;height: 1px;\"");
					var __jte_html_attribute_1 = dateUtils.DateToFormato2(dato.getFecha_actualizacion());
					if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_1)) {
						jteOutput.writeContent(" value=\"");
						jteOutput.setContext("input", "value");
						jteOutput.writeUserContent(__jte_html_attribute_1);
						jteOutput.setContext("input", null);
						jteOutput.writeContent("\"");
					}
					jteOutput.writeContent(">\n            <input  type=\"text\" name=\"dato_padre\" style=\"display: none; width: 1px;height: 1px;\"");
					var __jte_html_attribute_2 = dato.getDato_padre();
					if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_2)) {
						jteOutput.writeContent(" value=\"");
						jteOutput.setContext("input", "value");
						jteOutput.writeUserContent(__jte_html_attribute_2);
						jteOutput.setContext("input", null);
						jteOutput.writeContent("\"");
					}
					jteOutput.writeContent(" required>\n            <input type=\"text\" style=\"display: none; width: 1px;height: 1px;\"");
					var __jte_html_attribute_3 = dato.getDato();
					if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_3)) {
						jteOutput.writeContent(" value=\"");
						jteOutput.setContext("input", "value");
						jteOutput.writeUserContent(__jte_html_attribute_3);
						jteOutput.setContext("input", null);
						jteOutput.writeContent("\"");
					}
					jteOutput.writeContent(" name=\"dato\" required>\n\n            <div class=\"mb-3 col-12 col-md-6\">\n                <label for=\"nom-dato\" class=\"form-label text-muted\">Nombre del Dato:</label>\n                <input type=\"text\" id=\"nom-dato\" disabled");
					var __jte_html_attribute_4 = dato.getDato();
					if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_4)) {
						jteOutput.writeContent(" value=\"");
						jteOutput.setContext("input", "value");
						jteOutput.writeUserContent(__jte_html_attribute_4);
						jteOutput.setContext("input", null);
						jteOutput.writeContent("\"");
					}
					jteOutput.writeContent(" placeholder=\" ... \" class=\"form-control\" aria-describedby=\"basic-addon4\">\n            </div>\n\n            <div class=\"mb-3 col-12 col-md-6\">\n                <label for=\"grupo\" class=\"form-label text-muted\">Grupo:</label>\n                <select class=\"form-select custom-select\" disabled >\n                    ");
					if (dato.getDato_padre().equals(null)) {
						jteOutput.writeContent("\n                        <option value=\"0\" selected disabled>--Seleccione--</option>\n                    ");
					} else {
						jteOutput.writeContent("\n                        <option");
						var __jte_html_attribute_5 = dato.getDato_padre();
						if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_5)) {
							jteOutput.writeContent(" value=\"");
							jteOutput.setContext("option", "value");
							jteOutput.writeUserContent(__jte_html_attribute_5);
							jteOutput.setContext("option", null);
							jteOutput.writeContent("\"");
						}
						jteOutput.writeContent(">");
						jteOutput.setContext("option", null);
						jteOutput.writeUserContent(dato.getDato_padre());
						jteOutput.writeContent("</option>\n                    ");
					}
					jteOutput.writeContent("\n                </select>\n            </div>\n\n        ");
				}
				jteOutput.writeContent("\n\n        <div class=\"mb-3 col-12\">\n            <label for=\"descripcion\" class=\"form-label text-muted\">Descripción:</label>\n            <textarea class=\"form-control\" id=\"descripcion\" name=\"descripcion\" rows=\"3\" required maxlength=\"255\">");
				jteOutput.setContext("textarea", null);
				jteOutput.writeUserContent(update?dato.getDescripcion():"");
				jteOutput.writeContent("</textarea>\n        </div>\n        <div class=\"col-6 d-flex justify-content-start\">\n            <div class=\"form-check\">\n                <input class=\"form-check-input\" type=\"checkbox\" ");
				var __jte_html_attribute_6 = !update || dato.isActivo();
				if (__jte_html_attribute_6) {
				jteOutput.writeContent(" checked");
				}
				jteOutput.writeContent(" name=\"activo\" id=\"activo\">\n                <label class=\"form-check-label text-muted\" for=\"activo\">Activo</label>\n            </div>\n        </div>\n    ");
			}
		}, new gg.jte.html.HtmlContent() {
			public void writeTo(gg.jte.html.HtmlTemplateOutput jteOutput) {
				jteOutput.writeContent("\n        <script src=\"/content/js/dtgnr/svd_dt742520.js\"></script>\n    ");
			}
		}, true, true);
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		Boolean dat_gen_registro_datos = (Boolean)params.get("dat_gen_registro_datos");
		Boolean update = (Boolean)params.get("update");
		DateUtils dateUtils = (DateUtils)params.get("dateUtils");
		List<DatoDTO> grupos = (List<DatoDTO>)params.get("grupos");
		Dato dato = (Dato)params.get("dato");
		render(jteOutput, jteHtmlInterceptor, dat_gen_registro_datos, update, dateUtils, grupos, dato);
	}
}
