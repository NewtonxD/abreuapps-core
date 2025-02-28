package gg.jte.generated.ondemand.fragments;
import abreuapps.core.control.transporte.Vehiculo;
import abreuapps.core.control.utils.DateUtils;
import abreuapps.core.control.general.DatoDTO;
import java.util.List;
@SuppressWarnings("unchecked")
public final class Jtetrp_vehiculo_registroGenerated {
	public static final String JTE_NAME = "fragments/trp_vehiculo_registro.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,2,3,6,6,6,6,18,18,18,20,27,27,30,30,31,31,31,31,31,31,31,31,31,32,32,33,33,33,33,33,33,33,33,33,33,33,33,33,33,33,33,33,33,33,33,33,40,40,41,41,42,42,44,44,44,44,44,44,44,44,44,45,45,45,45,45,46,46,46,47,47,48,48,48,48,48,48,48,48,48,48,48,48,49,49,50,50,51,51,59,59,60,60,62,62,63,63,63,63,63,63,63,63,63,64,64,64,64,64,65,65,65,66,66,67,67,67,67,67,67,67,67,67,67,67,67,68,68,70,70,71,71,79,79,80,80,81,81,83,83,83,83,83,83,83,83,83,84,84,84,84,84,85,85,85,86,86,87,87,87,87,87,87,87,87,87,87,87,87,88,88,89,89,90,90,98,98,99,99,101,101,103,103,103,103,103,103,103,103,103,104,104,104,104,104,105,105,105,106,106,107,107,107,107,107,107,107,107,107,107,107,107,108,108,110,110,111,111,119,119,120,120,122,122,124,124,124,124,124,124,124,124,124,125,125,125,125,125,126,126,126,127,127,128,128,128,128,128,128,128,128,128,128,128,128,129,129,131,131,132,132,138,138,138,138,138,138,138,138,138,143,143,143,143,143,143,143,143,143,147,147,148,148,148,148,148,148,148,148,148,149,149,151,151,158,158,162,162,162,162,162,166,166,166,166,168,168,172,172,173,173,173,174,174,174,6,7,8,9,10,11,12,13,14,14,14,14};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, Boolean trp_vehiculo_registro, DateUtils dateUtils, Vehiculo vehiculo, Boolean last_loc, List<DatoDTO> color, List<DatoDTO> tipo_vehiculo, List<DatoDTO> estado, List<DatoDTO> marca, List<DatoDTO> modelo) {
		jteOutput.writeContent("\n\n\n");
		boolean update = vehiculo.getPlaca()!=null;
		jteOutput.writeContent("\n\n");
		gg.jte.generated.ondemand.shared.JteregistroGenerated.render(jteOutput, jteHtmlInterceptor, "Vehiculo", "trp_vehiculo_consulta", trp_vehiculo_registro, update, new gg.jte.html.HtmlContent() {
			public void writeTo(gg.jte.html.HtmlTemplateOutput jteOutput) {
				jteOutput.writeContent("\n    <div class=\"mb-3 col-12 col-md-6 col-lg-4\">\n        <label for=\"placa\" class=\"form-label text-muted\">Placa:</label>\n        ");
				if (update) {
					jteOutput.writeContent("\n            <input type=\"text\" style=\"display: none; width: 1px;height: 1px;\" id=\"Placa\" name=\"placa\"");
					var __jte_html_attribute_0 = vehiculo.getPlaca();
					if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_0)) {
						jteOutput.writeContent(" value=\"");
						jteOutput.setContext("input", "value");
						jteOutput.writeUserContent(__jte_html_attribute_0);
						jteOutput.setContext("input", null);
						jteOutput.writeContent("\"");
					}
					jteOutput.writeContent(">\n        ");
				}
				jteOutput.writeContent("\n        <input type=\"text\"");
				var __jte_html_attribute_1 = !update?"placa":"";
				if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_1)) {
					jteOutput.writeContent(" name=\"");
					jteOutput.setContext("input", "name");
					jteOutput.writeUserContent(__jte_html_attribute_1);
					jteOutput.setContext("input", null);
					jteOutput.writeContent("\"");
				}
				var __jte_html_attribute_2 = vehiculo.getPlaca();
				if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_2)) {
					jteOutput.writeContent(" value=\"");
					jteOutput.setContext("input", "value");
					jteOutput.writeUserContent(__jte_html_attribute_2);
					jteOutput.setContext("input", null);
					jteOutput.writeContent("\"");
				}
				var __jte_html_attribute_3 = update;
				if (__jte_html_attribute_3) {
				jteOutput.writeContent(" disabled");
				}
				jteOutput.writeContent(" placeholder=\" ... \" class=\"form-control\" required aria-describedby=\"basic-addon4\">\n    </div>\n\n    <div class=\"mb-3 col-12 col-md-6 col-lg-4\">\n        <label for=\"grupo\" class=\"form-label text-muted\">Color:</label>\n        <select class=\"form-select custom-select\" name=\"color\" id=\"color\" required>\n            <option value=\"0\" disabled>--Seleccione--</option>\n            ");
				for (var c : color) {
					jteOutput.writeContent("\n                ");
					if (c.act()) {
						jteOutput.writeContent("\n                    ");
						if (update) {
							jteOutput.writeContent("\n                        <option\n                               ");
							var __jte_html_attribute_4 = c.dat();
							if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_4)) {
								jteOutput.writeContent(" value=\"");
								jteOutput.setContext("option", "value");
								jteOutput.writeUserContent(__jte_html_attribute_4);
								jteOutput.setContext("option", null);
								jteOutput.writeContent("\"");
							}
							jteOutput.writeContent("\n                               ");
							var __jte_html_attribute_5 = vehiculo.getColor().getDato().equals(c.dat());
							if (__jte_html_attribute_5) {
							jteOutput.writeContent(" selected");
							}
							jteOutput.writeContent("\n                        >");
							jteOutput.setContext("option", null);
							jteOutput.writeUserContent(c.dat());
							jteOutput.writeContent("</option>\n                    ");
						} else {
							jteOutput.writeContent("\n                        <option");
							var __jte_html_attribute_6 = c.dat();
							if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_6)) {
								jteOutput.writeContent(" value=\"");
								jteOutput.setContext("option", "value");
								jteOutput.writeUserContent(__jte_html_attribute_6);
								jteOutput.setContext("option", null);
								jteOutput.writeContent("\"");
							}
							jteOutput.writeContent(">");
							jteOutput.setContext("option", null);
							jteOutput.writeUserContent(c.dat());
							jteOutput.writeContent("</option>\n                    ");
						}
						jteOutput.writeContent("\n                ");
					}
					jteOutput.writeContent("\n            ");
				}
				jteOutput.writeContent("\n        </select>\n    </div>\n\n    <div class=\"mb-3 col-12 col-md-6 col-lg-4\">\n        <label for=\"grupo\" class=\"form-label text-muted\">Tipo:</label>\n        <select class=\"form-select custom-select\" name=\"tipo_vehiculo\" id=\"tipo_vehiculo\" required>\n            <option value=\"0\" disabled>--Seleccione--</option>\n            ");
				for (var t:tipo_vehiculo) {
					jteOutput.writeContent("\n                ");
					if (t.act()) {
						jteOutput.writeContent("\n\n                    ");
						if (update) {
							jteOutput.writeContent("\n                        <option");
							var __jte_html_attribute_7 = t.dat();
							if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_7)) {
								jteOutput.writeContent(" value=\"");
								jteOutput.setContext("option", "value");
								jteOutput.writeUserContent(__jte_html_attribute_7);
								jteOutput.setContext("option", null);
								jteOutput.writeContent("\"");
							}
							jteOutput.writeContent("\n                               ");
							var __jte_html_attribute_8 =  vehiculo.getTipo_vehiculo().getDato().equals(t.dat()) ;
							if (__jte_html_attribute_8) {
							jteOutput.writeContent(" selected");
							}
							jteOutput.writeContent("\n                        >");
							jteOutput.setContext("option", null);
							jteOutput.writeUserContent(t.dat());
							jteOutput.writeContent("</option>\n                    ");
						} else {
							jteOutput.writeContent("\n                        <option");
							var __jte_html_attribute_9 = t.dat();
							if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_9)) {
								jteOutput.writeContent(" value=\"");
								jteOutput.setContext("option", "value");
								jteOutput.writeUserContent(__jte_html_attribute_9);
								jteOutput.setContext("option", null);
								jteOutput.writeContent("\"");
							}
							jteOutput.writeContent(">");
							jteOutput.setContext("option", null);
							jteOutput.writeUserContent(t.dat());
							jteOutput.writeContent("</option>\n                    ");
						}
						jteOutput.writeContent("\n\n                ");
					}
					jteOutput.writeContent("\n            ");
				}
				jteOutput.writeContent("\n        </select>\n    </div>\n\n    <div class=\"mb-3 col-12 col-md-6 col-lg-4\">\n        <label for=\"grupo\" class=\"form-label text-muted\">Estado:</label>\n        <select class=\"form-select custom-select\" name=\"estado\" id=\"estado\" required>\n            <option value=\"0\" disabled>--Seleccione--</option>\n            ");
				for (var e:estado) {
					jteOutput.writeContent("\n                ");
					if (e.act()) {
						jteOutput.writeContent("\n                    ");
						if (update) {
							jteOutput.writeContent("\n                        <option\n                           ");
							var __jte_html_attribute_10 = e.dat();
							if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_10)) {
								jteOutput.writeContent(" value=\"");
								jteOutput.setContext("option", "value");
								jteOutput.writeUserContent(__jte_html_attribute_10);
								jteOutput.setContext("option", null);
								jteOutput.writeContent("\"");
							}
							jteOutput.writeContent("\n                           ");
							var __jte_html_attribute_11 =  vehiculo.getEstado().getDato().equals(e.dat()) ;
							if (__jte_html_attribute_11) {
							jteOutput.writeContent(" selected");
							}
							jteOutput.writeContent("\n                        >");
							jteOutput.setContext("option", null);
							jteOutput.writeUserContent(e.dat());
							jteOutput.writeContent("</option>\n                    ");
						} else {
							jteOutput.writeContent("\n                        <option");
							var __jte_html_attribute_12 = e.dat();
							if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_12)) {
								jteOutput.writeContent(" value=\"");
								jteOutput.setContext("option", "value");
								jteOutput.writeUserContent(__jte_html_attribute_12);
								jteOutput.setContext("option", null);
								jteOutput.writeContent("\"");
							}
							jteOutput.writeContent(">");
							jteOutput.setContext("option", null);
							jteOutput.writeUserContent(e.dat());
							jteOutput.writeContent("</option>\n                    ");
						}
						jteOutput.writeContent("\n                ");
					}
					jteOutput.writeContent("\n            ");
				}
				jteOutput.writeContent("\n        </select>\n    </div>\n\n    <div class=\"mb-3 col-12 col-md-6 col-lg-4\">\n        <label for=\"grupo\" class=\"form-label text-muted\">Marca:</label>\n        <select class=\"form-select custom-select\" name=\"marca\" id=\"marca\" required>\n            <option value=\"0\" disabled>--Seleccione--</option>\n            ");
				for (var m:marca) {
					jteOutput.writeContent("\n                ");
					if (m.act()) {
						jteOutput.writeContent("\n\n                    ");
						if (update) {
							jteOutput.writeContent("\n                        <option\n                           ");
							var __jte_html_attribute_13 = m.dat();
							if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_13)) {
								jteOutput.writeContent(" value=\"");
								jteOutput.setContext("option", "value");
								jteOutput.writeUserContent(__jte_html_attribute_13);
								jteOutput.setContext("option", null);
								jteOutput.writeContent("\"");
							}
							jteOutput.writeContent("\n                           ");
							var __jte_html_attribute_14 =  vehiculo.getMarca().getDato().equals(m.dat()) ;
							if (__jte_html_attribute_14) {
							jteOutput.writeContent(" selected");
							}
							jteOutput.writeContent("\n                        >");
							jteOutput.setContext("option", null);
							jteOutput.writeUserContent(m.dat());
							jteOutput.writeContent("</option>\n                    ");
						} else {
							jteOutput.writeContent("\n                        <option");
							var __jte_html_attribute_15 = m.dat();
							if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_15)) {
								jteOutput.writeContent(" value=\"");
								jteOutput.setContext("option", "value");
								jteOutput.writeUserContent(__jte_html_attribute_15);
								jteOutput.setContext("option", null);
								jteOutput.writeContent("\"");
							}
							jteOutput.writeContent(">");
							jteOutput.setContext("option", null);
							jteOutput.writeUserContent(m.dat());
							jteOutput.writeContent("</option>\n                    ");
						}
						jteOutput.writeContent("\n\n                ");
					}
					jteOutput.writeContent("\n            ");
				}
				jteOutput.writeContent("\n        </select>\n    </div>\n\n    <div class=\"mb-3 col-12 col-md-6 col-lg-4\">\n        <label for=\"grupo\" class=\"form-label text-muted\">Módelo:</label>\n        <select class=\"form-select custom-select\" name=\"modelo\" id=\"modelo\" required>\n            <option value=\"0\" disabled>--Seleccione--</option>\n            ");
				for (var m:modelo) {
					jteOutput.writeContent("\n                ");
					if (m.act()) {
						jteOutput.writeContent("\n\n                    ");
						if (update) {
							jteOutput.writeContent("\n                        <option\n                           ");
							var __jte_html_attribute_16 = m.dat();
							if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_16)) {
								jteOutput.writeContent(" value=\"");
								jteOutput.setContext("option", "value");
								jteOutput.writeUserContent(__jte_html_attribute_16);
								jteOutput.setContext("option", null);
								jteOutput.writeContent("\"");
							}
							jteOutput.writeContent("\n                           ");
							var __jte_html_attribute_17 =  vehiculo.getModelo().getDato().equals(m.dat()) ;
							if (__jte_html_attribute_17) {
							jteOutput.writeContent(" selected");
							}
							jteOutput.writeContent("\n                        >");
							jteOutput.setContext("option", null);
							jteOutput.writeUserContent(m.dat());
							jteOutput.writeContent("</option>\n                    ");
						} else {
							jteOutput.writeContent("\n                        <option");
							var __jte_html_attribute_18 = m.dat();
							if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_18)) {
								jteOutput.writeContent(" value=\"");
								jteOutput.setContext("option", "value");
								jteOutput.writeUserContent(__jte_html_attribute_18);
								jteOutput.setContext("option", null);
								jteOutput.writeContent("\"");
							}
							jteOutput.writeContent(">");
							jteOutput.setContext("option", null);
							jteOutput.writeUserContent(m.dat());
							jteOutput.writeContent("</option>\n                    ");
						}
						jteOutput.writeContent("\n\n                ");
					}
					jteOutput.writeContent("\n            ");
				}
				jteOutput.writeContent("\n        </select>\n    </div>\n\n    <div class=\"mb-3 col-12 col-md-6 col-lg-4\">\n        <label for=\"placa\" class=\"form-label text-muted\">Año de Fabricación:</label>\n        <input type=\"number\" name='anio_fabricacion'");
				var __jte_html_attribute_19 = vehiculo.getAnio_fabricacion();
				if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_19)) {
					jteOutput.writeContent(" value=\"");
					jteOutput.setContext("input", "value");
					jteOutput.writeUserContent(__jte_html_attribute_19);
					jteOutput.setContext("input", null);
					jteOutput.writeContent("\"");
				}
				jteOutput.writeContent(" placeholder=\" ... \" class=\"form-control\" required aria-describedby=\"basic-addon4\">\n    </div>\n\n    <div class=\"mb-3 col-12 col-md-6 col-lg-4\">\n        <label for=\"placa\" class=\"form-label text-muted\">Capacidad Máxima (Pasajeros):</label>\n        <input type=\"number\" name='capacidad_pasajeros'");
				var __jte_html_attribute_20 = vehiculo.getCapacidad_pasajeros();
				if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_20)) {
					jteOutput.writeContent(" value=\"");
					jteOutput.setContext("input", "value");
					jteOutput.writeUserContent(__jte_html_attribute_20);
					jteOutput.setContext("input", null);
					jteOutput.writeContent("\"");
				}
				jteOutput.writeContent(" placeholder=\" ... \" class=\"form-control\" required aria-describedby=\"basic-addon4\">\n\n    </div>\n\n    ");
				if (update) {
					jteOutput.writeContent("\n        <input  type=\"datetime\" name=\"fecha_actualizacionn\" style=\"display: none; width: 1px;height: 1px;\"");
					var __jte_html_attribute_21 = dateUtils.DateToFormato2(vehiculo.getFecha_actualizacion());
					if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_21)) {
						jteOutput.writeContent(" value=\"");
						jteOutput.setContext("input", "value");
						jteOutput.writeUserContent(__jte_html_attribute_21);
						jteOutput.setContext("input", null);
						jteOutput.writeContent("\"");
					}
					jteOutput.writeContent(">\n    ");
				}
				jteOutput.writeContent("\n\n    ");
				if (update && last_loc) {
					jteOutput.writeContent("\n        <div class=\"col-12  d-flex justify-content-center text-center \">\n            <h6>Fecha Ultima Ubicación: <span id=\"date_last_loc\"></span></h6>\n        </div>\n        <div class=\"col-12 d-flex justify-content-center pb-4\">\n            <div id=\"map\" class=\"map-regular\"></div>\n        </div>\n    ");
				}
				jteOutput.writeContent("\n\n    <div class=\"col-6 d-flex justify-content-start mt-2\">\n        <div class=\"form-check\">\n            <input class=\"form-check-input\" type=\"checkbox\" ");
				var __jte_html_attribute_22 =  !update || vehiculo.isActivo() ;
				if (__jte_html_attribute_22) {
				jteOutput.writeContent(" checked");
				}
				jteOutput.writeContent(" name=\"activo\" id=\"activo\">\n            <label class=\"form-check-label text-muted\" for=\"activo\">Activo</label>\n        </div>\n    </div>\n    ");
			}
		}, new gg.jte.html.HtmlContent() {
			public void writeTo(gg.jte.html.HtmlTemplateOutput jteOutput) {
				jteOutput.writeContent("\n    <script src=\"/content/js/trp/svd_vhl574702.js\"></script>\n    ");
				if (update && last_loc) {
					jteOutput.writeContent("\n        <script src=\"/content/js/lib/leaflet.js\"></script>\n        <script src=\"/content/js/fetch_cache.js\"></script>\n        <script src=\"/content/js/trp/vhl544702lastloc.js\"></script>\n    ");
				}
				jteOutput.writeContent("\n    ");
			}
		}, true, true);
		jteOutput.writeContent("\n");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		Boolean trp_vehiculo_registro = (Boolean)params.get("trp_vehiculo_registro");
		DateUtils dateUtils = (DateUtils)params.get("dateUtils");
		Vehiculo vehiculo = (Vehiculo)params.get("vehiculo");
		Boolean last_loc = (Boolean)params.get("last_loc");
		List<DatoDTO> color = (List<DatoDTO>)params.get("color");
		List<DatoDTO> tipo_vehiculo = (List<DatoDTO>)params.get("tipo_vehiculo");
		List<DatoDTO> estado = (List<DatoDTO>)params.get("estado");
		List<DatoDTO> marca = (List<DatoDTO>)params.get("marca");
		List<DatoDTO> modelo = (List<DatoDTO>)params.get("modelo");
		render(jteOutput, jteHtmlInterceptor, trp_vehiculo_registro, dateUtils, vehiculo, last_loc, color, tipo_vehiculo, estado, marca, modelo);
	}
}
