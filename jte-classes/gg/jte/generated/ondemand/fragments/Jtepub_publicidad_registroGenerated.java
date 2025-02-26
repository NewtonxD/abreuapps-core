package gg.jte.generated.ondemand.fragments;
import abreuapps.core.control.general.Publicidad;
import abreuapps.core.control.utils.DateUtils;
import java.util.List;
import abreuapps.core.control.general.DatoDTO;
@SuppressWarnings("unchecked")
public final class Jtepub_publicidad_registroGenerated {
	public static final String JTE_NAME = "fragments/pub_publicidad_registro.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,2,3,5,5,5,5,11,11,11,14,21,21,22,22,23,23,23,23,23,23,23,23,23,24,24,24,24,24,24,24,24,24,25,25,30,30,30,30,30,30,30,30,30,37,37,38,38,40,40,41,41,41,41,41,41,41,41,41,41,41,41,42,42,44,44,45,45,45,45,45,45,45,45,45,45,45,45,46,46,47,47,47,47,47,47,47,47,47,47,47,47,48,48,50,50,52,52,53,53,60,60,61,61,61,61,61,61,61,61,61,62,62,64,64,73,73,74,74,74,74,74,74,74,74,74,75,75,77,77,82,82,82,82,82,82,82,82,82,87,87,90,90,91,91,91,91,91,91,91,91,91,99,99,99,104,104,104,104,104,108,108,108,108,111,111,113,113,114,114,114,115,115,115,5,6,7,8,8,8,8};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, Boolean pub_publicidad_registro, DateUtils dateUtils, Publicidad publicidad, List<DatoDTO> empresas) {
		jteOutput.writeContent("\n\n");
		boolean update =  (publicidad.getId()!=null) ;
		jteOutput.writeContent("\n\n\n");
		gg.jte.generated.ondemand.shared.JteregistroGenerated.render(jteOutput, jteHtmlInterceptor, "Publicidad", "pub_publicidad_consulta", pub_publicidad_registro, update, new gg.jte.html.HtmlContent() {
			public void writeTo(gg.jte.html.HtmlTemplateOutput jteOutput) {
				jteOutput.writeContent("\n        ");
				if (update) {
					jteOutput.writeContent("\n            <input type=\"text\" style=\"display: none; width: 1px;height: 1px;\" id=\"idPublicidad\" name=\"id\"");
					var __jte_html_attribute_0 = publicidad.getId();
					if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_0)) {
						jteOutput.writeContent(" value=\"");
						jteOutput.setContext("input", "value");
						jteOutput.writeUserContent(__jte_html_attribute_0);
						jteOutput.setContext("input", null);
						jteOutput.writeContent("\"");
					}
					jteOutput.writeContent(" >\n            <input  type=\"datetime\" name=\"fecha_actualizacionn\" style=\"display: none; width: 1px;height: 1px;\"");
					var __jte_html_attribute_1 = dateUtils.DateToFormato2(publicidad.getFecha_actualizacion());
					if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_1)) {
						jteOutput.writeContent(" value=\"");
						jteOutput.setContext("input", "value");
						jteOutput.writeUserContent(__jte_html_attribute_1);
						jteOutput.setContext("input", null);
						jteOutput.writeContent("\"");
					}
					jteOutput.writeContent(">\n        ");
				}
				jteOutput.writeContent("\n\n\n        <div class=\"mb-3 col-12 col-md-6\">\n            <label for=\"nom-dato\" class=\"form-label text-muted\">Título:</label>\n            <input type=\"text\" name=\"titulo\" id=\"titulo\"");
				var __jte_html_attribute_2 = publicidad.getTitulo();
				if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_2)) {
					jteOutput.writeContent(" value=\"");
					jteOutput.setContext("input", "value");
					jteOutput.writeUserContent(__jte_html_attribute_2);
					jteOutput.setContext("input", null);
					jteOutput.writeContent("\"");
				}
				jteOutput.writeContent(" required class=\"form-control\" placeholder=\"...\"/>\n        </div>\n\n        <div class=\"mb-3 col-12 col-md-6\">\n            <label for=\"nom-dato\" class=\"form-label text-muted\">Empresa:</label>\n            <select required class=\"form-select custom-select\" name=\"empresa\" id=\"empresa\">\n                <option value=\"0\" selected disabled>--Seleccione--</option>\n                ");
				for (var g : empresas ) {
					jteOutput.writeContent("\n                    ");
					if (g.act()) {
						jteOutput.writeContent("\n\n                        ");
						if (!update) {
							jteOutput.writeContent("\n                            <option");
							var __jte_html_attribute_3 = g.dat();
							if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_3)) {
								jteOutput.writeContent(" value=\"");
								jteOutput.setContext("option", "value");
								jteOutput.writeUserContent(__jte_html_attribute_3);
								jteOutput.setContext("option", null);
								jteOutput.writeContent("\"");
							}
							jteOutput.writeContent(">");
							jteOutput.setContext("option", null);
							jteOutput.writeUserContent(g.dat());
							jteOutput.writeContent("</option>\n                        ");
						} else {
							jteOutput.writeContent("\n\n                            ");
							if ( g.dat().equals(publicidad.getEmpresa().getDato()) ) {
								jteOutput.writeContent("\n                                <option");
								var __jte_html_attribute_4 = g.dat();
								if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_4)) {
									jteOutput.writeContent(" value=\"");
									jteOutput.setContext("option", "value");
									jteOutput.writeUserContent(__jte_html_attribute_4);
									jteOutput.setContext("option", null);
									jteOutput.writeContent("\"");
								}
								jteOutput.writeContent(" selected >");
								jteOutput.setContext("option", null);
								jteOutput.writeUserContent(g.dat());
								jteOutput.writeContent("</option>\n                            ");
							} else {
								jteOutput.writeContent("\n                                <option");
								var __jte_html_attribute_5 = g.dat();
								if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_5)) {
									jteOutput.writeContent(" value=\"");
									jteOutput.setContext("option", "value");
									jteOutput.writeUserContent(__jte_html_attribute_5);
									jteOutput.setContext("option", null);
									jteOutput.writeContent("\"");
								}
								jteOutput.writeContent(">");
								jteOutput.setContext("option", null);
								jteOutput.writeUserContent(g.dat());
								jteOutput.writeContent("</option>\n                            ");
							}
							jteOutput.writeContent("\n\n                        ");
						}
						jteOutput.writeContent("\n\n                    ");
					}
					jteOutput.writeContent("\n                ");
				}
				jteOutput.writeContent("\n            </select>\n        </div>\n\n        <div class=\"mb-3 col-12 col-md-5\">\n            <label for=\"descripcion\" class=\"form-label text-muted\">Fecha Inicial:</label>\n\n            ");
				if (update) {
					jteOutput.writeContent("\n                <input required class=\"form-control\"");
					var __jte_html_attribute_6 = dateUtils.dateFormat.format(publicidad.getFecha_inicio());
					if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_6)) {
						jteOutput.writeContent(" value=\"");
						jteOutput.setContext("input", "value");
						jteOutput.writeUserContent(__jte_html_attribute_6);
						jteOutput.setContext("input", null);
						jteOutput.writeContent("\"");
					}
					jteOutput.writeContent(" name=\"fecha_inicio\" id=\"startDate\" type=\"date\"/>\n            ");
				} else {
					jteOutput.writeContent("\n                <input required class=\"form-control\" name=\"fecha_inicio\" id=\"startDate\" type=\"date\"/>\n            ");
				}
				jteOutput.writeContent("\n\n            <div class=\"alert alert-warning\" role=\"alert\" id=\"dateDifference\" style=\"display: none;\" ></div>\n        </div>\n        <div class=\"mb-3 mt-3 col-12 col-md-2 text-center\">\n            <p class=\"text-wrap m-0\">Total de Dias <br>de Vigencia: <span id=\"dias\">0</span></p>\n        </div>\n        <div class=\"mb-3 col-12 col-md-5\">\n            <label for=\"descripcion\" class=\"form-label text-muted\">Fecha Final:</label>\n            ");
				if (update) {
					jteOutput.writeContent("\n                <input required name=\"fecha_fin\"");
					var __jte_html_attribute_7 = dateUtils.dateFormat.format(publicidad.getFecha_fin());
					if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_7)) {
						jteOutput.writeContent(" value=\"");
						jteOutput.setContext("input", "value");
						jteOutput.writeUserContent(__jte_html_attribute_7);
						jteOutput.setContext("input", null);
						jteOutput.writeContent("\"");
					}
					jteOutput.writeContent("  class=\"form-control\" id=\"endDate\" type=\"date\"/>\n            ");
				} else {
					jteOutput.writeContent("\n                <input required name=\"fecha_fin\" class=\"form-control\" id=\"endDate\" type=\"date\"/>\n            ");
				}
				jteOutput.writeContent("\n        </div>\n\n        <div class=\"mb-3 col-12\">\n            <label for=\"nom-dato\" class=\"form-label text-muted\">Link de Acceso:</label>\n            <input type=\"text\" required name=\"link_destino\"");
				var __jte_html_attribute_8 = publicidad.getLink_destino();
				if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_8)) {
					jteOutput.writeContent(" value=\"");
					jteOutput.setContext("input", "value");
					jteOutput.writeUserContent(__jte_html_attribute_8);
					jteOutput.setContext("input", null);
					jteOutput.writeContent("\"");
				}
				jteOutput.writeContent(" placeholder=\"https://www.ejemplo.com\" class=\"form-control\"/>\n        </div>\n\n        <div class=\"mb-3 col-12\">\n            <label for=\"nom-dato\" class=\"form-label text-muted\">Video o Imagen:</label>\n            ");
				if (!update) {
					jteOutput.writeContent("\n                <div class=\"alert alert-warning\" role=\"alert\" id=\"fileInfo\" style=\"display: none;\" ></div>\n                <input type=\"file\" class=\"form-control\" id=\"fileInput\" required accept=\"video/mp4,image/jpeg,image/webp,image/png\">\n            ");
				}
				jteOutput.writeContent("\n            <input type=\"text\" style=\"display: none;\"");
				var __jte_html_attribute_9 = publicidad.getImagen_video_direccion();
				if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_9)) {
					jteOutput.writeContent(" value=\"");
					jteOutput.setContext("input", "value");
					jteOutput.writeUserContent(__jte_html_attribute_9);
					jteOutput.setContext("input", null);
					jteOutput.writeContent("\"");
				}
				jteOutput.writeContent(" name=\"imagen_video_direccion\" id=\"archivo\" class=\"form-control\"/>\n            <div id=\"previewContainer\" class=\"d-flex justify-content-center\" style=\"display: none;\">\n                <div id=\"preview\" class=\"mt-2\"></div>\n            </div>\n        </div>\n\n        <div class=\"mb-3 col-12\">\n            <label for=\"descripcion\" class=\"form-label text-muted\">Descripción:</label>\n            <textarea class=\"form-control\" name=\"descripcion\" rows=\"2\" maxlength=\"255\">");
				jteOutput.setContext("textarea", null);
				jteOutput.writeUserContent(publicidad.getDescripcion());
				jteOutput.writeContent("</textarea>\n        </div>\n\n        <div class=\"col-6 d-flex justify-content-start\">\n            <div class=\"form-check\">\n                <input class=\"form-check-input\" type=\"checkbox\" ");
				var __jte_html_attribute_10 =  !update || publicidad.isActivo() ;
				if (__jte_html_attribute_10) {
				jteOutput.writeContent(" checked");
				}
				jteOutput.writeContent(" name=\"activo\" id=\"activo\">\n                <label class=\"form-check-label text-muted\" for=\"activo\">Activo</label>\n            </div>\n        </div>\n    ");
			}
		}, new gg.jte.html.HtmlContent() {
			public void writeTo(gg.jte.html.HtmlTemplateOutput jteOutput) {
				jteOutput.writeContent("\n        <script src=\"/content/js/pub/pub804952.js\"></script>\n        <script src=\"/content/js/pub/svd_pub794851.js\"></script>\n        ");
				if (update) {
					jteOutput.writeContent("\n            <script src=\"/content/js/pub/pub825153.js\"></script>\n        ");
				}
				jteOutput.writeContent("\n    ");
			}
		}, true, true);
		jteOutput.writeContent("\n");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		Boolean pub_publicidad_registro = (Boolean)params.get("pub_publicidad_registro");
		DateUtils dateUtils = (DateUtils)params.get("dateUtils");
		Publicidad publicidad = (Publicidad)params.get("publicidad");
		List<DatoDTO> empresas = (List<DatoDTO>)params.get("empresas");
		render(jteOutput, jteHtmlInterceptor, pub_publicidad_registro, dateUtils, publicidad, empresas);
	}
}
