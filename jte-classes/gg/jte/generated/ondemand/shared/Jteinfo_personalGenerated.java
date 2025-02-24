package gg.jte.generated.ondemand.shared;
import abreuapps.core.control.general.Persona;
import abreuapps.core.control.utils.DateUtils;
import abreuapps.core.control.general.DatoDTO;
import java.util.List;
@SuppressWarnings("unchecked")
public final class Jteinfo_personalGenerated {
	public static final String JTE_NAME = "shared/info_personal.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,2,3,5,5,5,5,12,12,12,14,14,14,14,14,14,14,14,14,15,15,23,23,24,24,24,24,24,24,24,24,24,25,25,25,25,25,25,25,25,25,26,26,27,27,27,27,27,27,27,27,27,28,28,33,33,33,33,33,33,33,33,33,38,38,38,38,38,38,38,38,38,43,43,43,43,43,43,43,43,43,52,52,53,53,55,55,56,56,56,56,56,56,56,56,56,56,56,56,57,57,59,59,60,60,60,60,60,60,60,60,60,60,60,60,61,61,62,62,62,62,62,62,62,62,62,62,62,62,63,63,65,65,67,67,68,68,102,102,103,103,104,104,104,104,104,104,104,104,104,104,104,104,105,105,106,106,109,109,109,5,6,7,8,9,9,9,9};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, Persona persona, DateUtils dateUtils, boolean update, List<DatoDTO> sexo, List<DatoDTO> sangre) {
		jteOutput.writeContent("\n<div id=\"info-dinamica-personal\" class=\"row\">\n    ");
		if (update) {
			jteOutput.writeContent("\n        <input  type=\"number\" name=\"id\" id=\"idPersona\" style=\"display: none; width: 1px;height: 1px;\" required>\n        <input  type=\"datetime\" name=\"fecha_actualizacionn\" style=\"display: none; width: 1px;height: 1px;\"");
			var __jte_html_attribute_0 = dateUtils.DateToFormato2(persona.getFecha_actualizacion());
			if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_0)) {
				jteOutput.writeContent(" value=\"");
				jteOutput.setContext("input", "value");
				jteOutput.writeUserContent(__jte_html_attribute_0);
				jteOutput.setContext("input", null);
				jteOutput.writeContent("\"");
			}
			jteOutput.writeContent(">\n    ");
		}
		jteOutput.writeContent("\n    <div class=\"col-12 mt-2 mb-2\">\n        <div class=\"alert alert-warning alert-cedula\" role=\"alert\" style=\"display:none;\">\n            <span>Cedula en uso. Verifique y vuelva a intentarlo.</span>\n        </div>\n    </div>\n    <div class=\"mb-3 col-12\">\n        <label for=\"cedula\" class=\"form-label text-muted\">Cédula:</label>\n        ");
		if (update) {
			jteOutput.writeContent("\n            <input type=\"text\" name=\"cedula\" id=\"cedula\"");
			var __jte_html_attribute_1 = persona.getCedula();
			if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_1)) {
				jteOutput.writeContent(" value=\"");
				jteOutput.setContext("input", "value");
				jteOutput.writeUserContent(__jte_html_attribute_1);
				jteOutput.setContext("input", null);
				jteOutput.writeContent("\"");
			}
			jteOutput.writeContent(" style=\"display: none; width: 1px;height: 1px;\" />\n            <input type=\"text\"");
			var __jte_html_attribute_2 = persona.getCedula();
			if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_2)) {
				jteOutput.writeContent(" value=\"");
				jteOutput.setContext("input", "value");
				jteOutput.writeUserContent(__jte_html_attribute_2);
				jteOutput.setContext("input", null);
				jteOutput.writeContent("\"");
			}
			jteOutput.writeContent(" name=\"ced\" disabled placeholder=\"XXX-XXXXXXX-X\" class=\"form-control cedula\" aria-describedby=\"cedula\" required>\n        ");
		} else {
			jteOutput.writeContent("\n            <input type=\"text\" name=\"cedula\" id=\"cedula\"");
			var __jte_html_attribute_3 = persona.getCedula();
			if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_3)) {
				jteOutput.writeContent(" value=\"");
				jteOutput.setContext("input", "value");
				jteOutput.writeUserContent(__jte_html_attribute_3);
				jteOutput.setContext("input", null);
				jteOutput.writeContent("\"");
			}
			jteOutput.writeContent(" placeholder=\"XXX-XXXXXXX-X\" class=\"form-control cedula\" aria-describedby=\"cedula\" required>\n        ");
		}
		jteOutput.writeContent("\n    </div>\n\n    <div class=\"mb-3 col-12 col-md-6\">\n        <label for=\"nombre\" class=\"form-label text-muted\">Nombre:</label>\n        <input type=\"text\" name=\"nombre\" id=\"nombre\"");
		var __jte_html_attribute_4 = persona.getNombre();
		if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_4)) {
			jteOutput.writeContent(" value=\"");
			jteOutput.setContext("input", "value");
			jteOutput.writeUserContent(__jte_html_attribute_4);
			jteOutput.setContext("input", null);
			jteOutput.writeContent("\"");
		}
		jteOutput.writeContent(" placeholder=\" ... \" class=\"form-control\" required aria-describedby=\"basic-addon4\">\n    </div>\n\n    <div class=\"mb-3 col-12 col-md-6\">\n        <label for=\"apellido\" class=\"form-label text-muted\">Apellido:</label>\n        <input type=\"text\"  name=\"apellido\" id=\"apellido\"");
		var __jte_html_attribute_5 = persona.getApellido();
		if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_5)) {
			jteOutput.writeContent(" value=\"");
			jteOutput.setContext("input", "value");
			jteOutput.writeUserContent(__jte_html_attribute_5);
			jteOutput.setContext("input", null);
			jteOutput.writeContent("\"");
		}
		jteOutput.writeContent(" placeholder=\" ... \" class=\"form-control\" required aria-describedby=\"basic-addon4\">\n    </div>\n\n    <div class=\"mb-3 col-12 col-md-6\">\n        <label for=\"apodo\" class=\"form-label text-muted\">Apodo:</label>\n        <input type=\"text\" name=\"apodo\" id=\"apodo\"");
		var __jte_html_attribute_6 = persona.getApodo();
		if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_6)) {
			jteOutput.writeContent(" value=\"");
			jteOutput.setContext("input", "value");
			jteOutput.writeUserContent(__jte_html_attribute_6);
			jteOutput.setContext("input", null);
			jteOutput.writeContent("\"");
		}
		jteOutput.writeContent("  placeholder=\" ... \" class=\"form-control\" aria-describedby=\"basic-addon4\">\n    </div>\n\n\n\n    <div class=\"mb-3 col-12 col-md-6\">\n        <label for=\"sexo\" class=\"form-label text-muted\" required>Sexo:</label>\n        <select class=\"form-select custom-select\" name=\"sexo\" id=\"sexo\">\n            <option value=\"0\" selected disabled>--Seleccione--</option>\n            ");
		for (var g : sexo) {
			jteOutput.writeContent("\n                ");
			if (g.act()) {
				jteOutput.writeContent("\n\n                    ");
				if (!update) {
					jteOutput.writeContent("\n                        <option");
					var __jte_html_attribute_7 = g.dat();
					if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_7)) {
						jteOutput.writeContent(" value=\"");
						jteOutput.setContext("option", "value");
						jteOutput.writeUserContent(__jte_html_attribute_7);
						jteOutput.setContext("option", null);
						jteOutput.writeContent("\"");
					}
					jteOutput.writeContent(">");
					jteOutput.setContext("option", null);
					jteOutput.writeUserContent(g.dat());
					jteOutput.writeContent("</option>\n                    ");
				} else {
					jteOutput.writeContent("\n\n                        ");
					if (g.dat().equals(persona.getSexo().getDato())) {
						jteOutput.writeContent("\n                            <option");
						var __jte_html_attribute_8 = g.dat();
						if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_8)) {
							jteOutput.writeContent(" value=\"");
							jteOutput.setContext("option", "value");
							jteOutput.writeUserContent(__jte_html_attribute_8);
							jteOutput.setContext("option", null);
							jteOutput.writeContent("\"");
						}
						jteOutput.writeContent(" selected>");
						jteOutput.setContext("option", null);
						jteOutput.writeUserContent(g.dat());
						jteOutput.writeContent("</option>\n                        ");
					} else {
						jteOutput.writeContent("\n                            <option");
						var __jte_html_attribute_9 = g.dat();
						if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_9)) {
							jteOutput.writeContent(" value=\"");
							jteOutput.setContext("option", "value");
							jteOutput.writeUserContent(__jte_html_attribute_9);
							jteOutput.setContext("option", null);
							jteOutput.writeContent("\"");
						}
						jteOutput.writeContent(">");
						jteOutput.setContext("option", null);
						jteOutput.writeUserContent(g.dat());
						jteOutput.writeContent("</option>\n                        ");
					}
					jteOutput.writeContent("\n\n                    ");
				}
				jteOutput.writeContent("\n\n                ");
			}
			jteOutput.writeContent("\n            ");
		}
		jteOutput.writeContent("\n        </select>\n    </div>\n\n\n    <div class=\"mb-3 col-12 col-md-6\">\n        <label for=\"numero_celular\" class=\"form-label text-muted\">Núm. Celular:</label>\n        <input type=\"tel\" name=\"numero_celular\" id=\"numero_celular\" placeholder=\"809-999-123\" class=\"form-control telefono\" aria-describedby=\"numero_celular\" required>\n    </div>\n\n    <div class=\"mb-3 col-12\">\n        <label for=\"direccion\" class=\"form-label text-muted\">Dirección:</label>\n        <textarea class=\"form-control\" name=\"direccion\" id=\"direccion\" rows=\"3\" required maxlength=\"255\" aria-describedby=\"direccion\"></textarea>\n    </div>\n\n    <div class=\"mb-3 col-12 col-md-6\">\n        <label for=\"nombre_emergencia\" class=\"form-label text-muted\">Nombre Contacto Emergencia:</label>\n        <input type=\"text\" name=\"nombre_emergencia\" id=\"nombre_emergencia\" class=\"form-control\" aria-describedby=\"nombre_emg\">\n    </div>\n\n    <div class=\"mb-3 col-12 col-md-6\">\n        <label for=\"numero_emergencia\" class=\"form-label text-muted\">Núm. Celular Emergencia:</label>\n        <input type=\"tel\" name=\"numero_emergencia\" id=\"numero_emergencia\" placeholder=\"809-999-123\" class=\"form-control telefono\" aria-describedby=\"numero_celular_emg\">\n    </div>\n\n    <div class=\"mb-3 col-12 col-md-6\">\n        <label for=\"fecha_nacimiento\" class=\"form-label text-muted\">Fecha de Nacimiento:</label>\n        <input type=\"date\" name=\"fecha_nacimiento\" id=\"fecha_nacimiento\" class=\"form-control\" aria-describedby=\"fecha_nacimiento\" required>\n    </div>\n\n    <div class=\"mb-3 col-12 col-md-6\">\n        <label for=\"tipo_sangre\" class=\"form-label text-muted\">Tipo de Sangre:</label>\n        <select class=\"form-select custom-select\" name=\"tipo_sangre\" id=\"tipo_sangre\" required>\n            <option value=\"0\" selected disabled>--Seleccione--</option>\n            ");
		for (var g : sangre) {
			jteOutput.writeContent("\n                ");
			if (g.act()) {
				jteOutput.writeContent("\n                    <option");
				var __jte_html_attribute_10 = g.dat();
				if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_10)) {
					jteOutput.writeContent(" value=\"");
					jteOutput.setContext("option", "value");
					jteOutput.writeUserContent(__jte_html_attribute_10);
					jteOutput.setContext("option", null);
					jteOutput.writeContent("\"");
				}
				jteOutput.writeContent(" >");
				jteOutput.setContext("option", null);
				jteOutput.writeUserContent(g.dat());
				jteOutput.writeContent("</option>\n                ");
			}
			jteOutput.writeContent("\n            ");
		}
		jteOutput.writeContent("\n        </select>\n    </div>\n</div>");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		Persona persona = (Persona)params.get("persona");
		DateUtils dateUtils = (DateUtils)params.get("dateUtils");
		boolean update = (boolean)params.get("update");
		List<DatoDTO> sexo = (List<DatoDTO>)params.get("sexo");
		List<DatoDTO> sangre = (List<DatoDTO>)params.get("sangre");
		render(jteOutput, jteHtmlInterceptor, persona, dateUtils, update, sexo, sangre);
	}
}
