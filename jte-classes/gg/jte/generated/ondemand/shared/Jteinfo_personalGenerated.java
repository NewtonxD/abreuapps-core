package gg.jte.generated.ondemand.shared;
import abreuapps.core.control.general.Persona;
import abreuapps.core.control.utils.DateUtils;
import abreuapps.core.control.general.DatoDTO;
import java.util.List;
@SuppressWarnings("unchecked")
public final class Jteinfo_personalGenerated {
	public static final String JTE_NAME = "shared/info_personal.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,2,3,5,5,5,5,12,12,12,13,13,13,13,13,13,13,13,13,14,14,14,14,14,14,14,14,14,15,15,17,17,25,25,26,26,26,26,26,26,26,26,26,27,27,27,27,27,27,27,27,27,28,28,29,29,29,29,29,29,29,29,29,30,30,35,35,35,35,35,35,35,35,35,40,40,40,40,40,40,40,40,40,45,45,45,45,45,45,45,45,45,54,54,55,55,57,57,58,58,58,58,58,58,58,58,58,58,58,58,59,59,61,61,62,62,62,62,62,62,62,62,62,62,62,62,63,63,64,64,64,64,64,64,64,64,64,64,64,64,65,65,67,67,69,69,70,70,77,77,77,77,77,77,77,77,77,82,82,82,87,87,87,87,87,87,87,87,87,92,92,92,92,92,92,92,92,92,97,97,98,98,98,98,98,98,98,98,98,99,99,101,101,108,108,109,109,110,110,111,111,111,111,111,111,111,111,111,111,111,111,112,112,114,114,115,115,115,115,115,115,115,115,115,115,115,115,116,116,117,117,117,117,117,117,117,117,117,117,117,117,118,118,120,120,121,121,122,122,125,125,125,5,6,7,8,9,9,9,9};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, Persona persona, DateUtils dateUtils, boolean update, List<DatoDTO> sexo, List<DatoDTO> sangre) {
		jteOutput.writeContent("\n<div id=\"info-dinamica-personal\" class=\"row\">\n    ");
		if (update) {
			jteOutput.writeContent("\n        <input  type=\"number\" name=\"id\" id=\"idPersona\"");
			var __jte_html_attribute_0 = persona.getId();
			if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_0)) {
				jteOutput.writeContent(" value=\"");
				jteOutput.setContext("input", "value");
				jteOutput.writeUserContent(__jte_html_attribute_0);
				jteOutput.setContext("input", null);
				jteOutput.writeContent("\"");
			}
			jteOutput.writeContent(" style=\"display: none; width: 1px;height: 1px;\">\n        <input  type=\"datetime\" name=\"fecha_actualizacionn\" style=\"display: none; width: 1px;height: 1px;\"");
			var __jte_html_attribute_1 = dateUtils.DateToFormato2(persona.getFecha_actualizacion());
			if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_1)) {
				jteOutput.writeContent(" value=\"");
				jteOutput.setContext("input", "value");
				jteOutput.writeUserContent(__jte_html_attribute_1);
				jteOutput.setContext("input", null);
				jteOutput.writeContent("\"");
			}
			jteOutput.writeContent(">\n    ");
		} else {
			jteOutput.writeContent("\n        <input  type=\"number\" name=\"id\" id=\"idPersona\" value=\"0\" style=\"display: none; width: 1px;height: 1px;\">\n    ");
		}
		jteOutput.writeContent("\n    <div class=\"col-12 mt-2 mb-2\">\n        <div class=\"alert alert-warning alert-cedula\" role=\"alert\" style=\"display:none;\">\n            <span>Cedula en uso. Verifique y vuelva a intentarlo.</span>\n        </div>\n    </div>\n    <div class=\"mb-3 col-12\">\n        <label for=\"cedula\" class=\"form-label text-muted\">Cédula:</label>\n        ");
		if (update) {
			jteOutput.writeContent("\n            <input type=\"text\" name=\"cedula\" id=\"cedula\"");
			var __jte_html_attribute_2 = persona.getCedula();
			if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_2)) {
				jteOutput.writeContent(" value=\"");
				jteOutput.setContext("input", "value");
				jteOutput.writeUserContent(__jte_html_attribute_2);
				jteOutput.setContext("input", null);
				jteOutput.writeContent("\"");
			}
			jteOutput.writeContent(" style=\"display: none; width: 1px;height: 1px;\" />\n            <input type=\"text\"");
			var __jte_html_attribute_3 = persona.getCedula();
			if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_3)) {
				jteOutput.writeContent(" value=\"");
				jteOutput.setContext("input", "value");
				jteOutput.writeUserContent(__jte_html_attribute_3);
				jteOutput.setContext("input", null);
				jteOutput.writeContent("\"");
			}
			jteOutput.writeContent(" name=\"ced\" disabled placeholder=\"XXX-XXXXXXX-X\" class=\"form-control cedula\" aria-describedby=\"cedula\" required>\n        ");
		} else {
			jteOutput.writeContent("\n            <input type=\"text\" name=\"cedula\" id=\"cedula\"");
			var __jte_html_attribute_4 = persona.getCedula();
			if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_4)) {
				jteOutput.writeContent(" value=\"");
				jteOutput.setContext("input", "value");
				jteOutput.writeUserContent(__jte_html_attribute_4);
				jteOutput.setContext("input", null);
				jteOutput.writeContent("\"");
			}
			jteOutput.writeContent(" placeholder=\"XXX-XXXXXXX-X\" class=\"form-control cedula\" aria-describedby=\"cedula\" required>\n        ");
		}
		jteOutput.writeContent("\n    </div>\n\n    <div class=\"mb-3 col-12 col-md-6\">\n        <label for=\"nombre\" class=\"form-label text-muted\">Nombre:</label>\n        <input type=\"text\" name=\"nombre\" id=\"nombre\"");
		var __jte_html_attribute_5 = persona.getNombre();
		if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_5)) {
			jteOutput.writeContent(" value=\"");
			jteOutput.setContext("input", "value");
			jteOutput.writeUserContent(__jte_html_attribute_5);
			jteOutput.setContext("input", null);
			jteOutput.writeContent("\"");
		}
		jteOutput.writeContent(" placeholder=\" ... \" class=\"form-control\" required aria-describedby=\"basic-addon4\">\n    </div>\n\n    <div class=\"mb-3 col-12 col-md-6\">\n        <label for=\"apellido\" class=\"form-label text-muted\">Apellido:</label>\n        <input type=\"text\"  name=\"apellido\" id=\"apellido\"");
		var __jte_html_attribute_6 = persona.getApellido();
		if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_6)) {
			jteOutput.writeContent(" value=\"");
			jteOutput.setContext("input", "value");
			jteOutput.writeUserContent(__jte_html_attribute_6);
			jteOutput.setContext("input", null);
			jteOutput.writeContent("\"");
		}
		jteOutput.writeContent(" placeholder=\" ... \" class=\"form-control\" required aria-describedby=\"basic-addon4\">\n    </div>\n\n    <div class=\"mb-3 col-12 col-md-6\">\n        <label for=\"apodo\" class=\"form-label text-muted\">Apodo:</label>\n        <input type=\"text\" name=\"apodo\" id=\"apodo\"");
		var __jte_html_attribute_7 = persona.getApodo();
		if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_7)) {
			jteOutput.writeContent(" value=\"");
			jteOutput.setContext("input", "value");
			jteOutput.writeUserContent(__jte_html_attribute_7);
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
					var __jte_html_attribute_8 = g.dat();
					if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_8)) {
						jteOutput.writeContent(" value=\"");
						jteOutput.setContext("option", "value");
						jteOutput.writeUserContent(__jte_html_attribute_8);
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
						var __jte_html_attribute_9 = g.dat();
						if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_9)) {
							jteOutput.writeContent(" value=\"");
							jteOutput.setContext("option", "value");
							jteOutput.writeUserContent(__jte_html_attribute_9);
							jteOutput.setContext("option", null);
							jteOutput.writeContent("\"");
						}
						jteOutput.writeContent(" selected>");
						jteOutput.setContext("option", null);
						jteOutput.writeUserContent(g.dat());
						jteOutput.writeContent("</option>\n                        ");
					} else {
						jteOutput.writeContent("\n                            <option");
						var __jte_html_attribute_10 = g.dat();
						if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_10)) {
							jteOutput.writeContent(" value=\"");
							jteOutput.setContext("option", "value");
							jteOutput.writeUserContent(__jte_html_attribute_10);
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
		jteOutput.writeContent("\n        </select>\n    </div>\n\n\n    <div class=\"mb-3 col-12 col-md-6\">\n        <label for=\"numero_celular\" class=\"form-label text-muted\">Núm. Celular:</label>\n        <input type=\"tel\" name=\"numero_celular\" id=\"numero_celular\"");
		var __jte_html_attribute_11 = persona.getNumero_celular();
		if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_11)) {
			jteOutput.writeContent(" value=\"");
			jteOutput.setContext("input", "value");
			jteOutput.writeUserContent(__jte_html_attribute_11);
			jteOutput.setContext("input", null);
			jteOutput.writeContent("\"");
		}
		jteOutput.writeContent(" placeholder=\"809-999-123\" class=\"form-control telefono\" aria-describedby=\"numero_celular\" required>\n    </div>\n\n    <div class=\"mb-3 col-12\">\n        <label for=\"direccion\" class=\"form-label text-muted\">Dirección:</label>\n        <textarea class=\"form-control\" name=\"direccion\" id=\"direccion\" rows=\"3\" required maxlength=\"255\" aria-describedby=\"direccion\">");
		jteOutput.setContext("textarea", null);
		jteOutput.writeUserContent(persona.getDireccion());
		jteOutput.writeContent("</textarea>\n    </div>\n\n    <div class=\"mb-3 col-12 col-md-6\">\n        <label for=\"nombre_emergencia\" class=\"form-label text-muted\">Nombre Contacto Emergencia:</label>\n        <input type=\"text\" name=\"nombre_emergencia\"");
		var __jte_html_attribute_12 = persona.getNombre_emergencia();
		if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_12)) {
			jteOutput.writeContent(" value=\"");
			jteOutput.setContext("input", "value");
			jteOutput.writeUserContent(__jte_html_attribute_12);
			jteOutput.setContext("input", null);
			jteOutput.writeContent("\"");
		}
		jteOutput.writeContent(" id=\"nombre_emergencia\" class=\"form-control\" aria-describedby=\"nombre_emg\">\n    </div>\n\n    <div class=\"mb-3 col-12 col-md-6\">\n        <label for=\"numero_emergencia\" class=\"form-label text-muted\">Núm. Celular Emergencia:</label>\n        <input type=\"tel\" name=\"numero_emergencia\"");
		var __jte_html_attribute_13 = persona.getNumero_emergencia();
		if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_13)) {
			jteOutput.writeContent(" value=\"");
			jteOutput.setContext("input", "value");
			jteOutput.writeUserContent(__jte_html_attribute_13);
			jteOutput.setContext("input", null);
			jteOutput.writeContent("\"");
		}
		jteOutput.writeContent(" id=\"numero_emergencia\" placeholder=\"809-999-123\" class=\"form-control telefono\" aria-describedby=\"numero_celular_emg\">\n    </div>\n\n    <div class=\"mb-3 col-12 col-md-6\">\n        <label for=\"fecha_nacimiento\" class=\"form-label text-muted\">Fecha de Nacimiento:</label>\n        ");
		if (update) {
			jteOutput.writeContent("\n            <input type=\"date\" name=\"fecha_nacimiento\"");
			var __jte_html_attribute_14 = dateUtils.dateFormat.format(persona.getFecha_nacimiento());
			if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_14)) {
				jteOutput.writeContent(" value=\"");
				jteOutput.setContext("input", "value");
				jteOutput.writeUserContent(__jte_html_attribute_14);
				jteOutput.setContext("input", null);
				jteOutput.writeContent("\"");
			}
			jteOutput.writeContent(" id=\"fecha_nacimiento\" class=\"form-control\" aria-describedby=\"fecha_nacimiento\" required>\n        ");
		} else {
			jteOutput.writeContent("\n            <input type=\"date\" name=\"fecha_nacimiento\" id=\"fecha_nacimiento\" class=\"form-control\" aria-describedby=\"fecha_nacimiento\" required>\n        ");
		}
		jteOutput.writeContent("\n    </div>\n\n    <div class=\"mb-3 col-12 col-md-6\">\n        <label for=\"tipo_sangre\" class=\"form-label text-muted\">Tipo de Sangre:</label>\n        <select class=\"form-select custom-select\" name=\"tipo_sangre\" id=\"tipo_sangre\" required>\n            <option value=\"0\" selected disabled>--Seleccione--</option>\n            ");
		for (var g : sangre) {
			jteOutput.writeContent("\n                ");
			if (g.act()) {
				jteOutput.writeContent("\n                    ");
				if (!update) {
					jteOutput.writeContent("\n                        <option");
					var __jte_html_attribute_15 = g.dat();
					if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_15)) {
						jteOutput.writeContent(" value=\"");
						jteOutput.setContext("option", "value");
						jteOutput.writeUserContent(__jte_html_attribute_15);
						jteOutput.setContext("option", null);
						jteOutput.writeContent("\"");
					}
					jteOutput.writeContent(">");
					jteOutput.setContext("option", null);
					jteOutput.writeUserContent(g.dat());
					jteOutput.writeContent("</option>\n                    ");
				} else {
					jteOutput.writeContent("\n\n                        ");
					if (g.dat().equals(persona.getTipo_sangre().getDato())) {
						jteOutput.writeContent("\n                            <option");
						var __jte_html_attribute_16 = g.dat();
						if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_16)) {
							jteOutput.writeContent(" value=\"");
							jteOutput.setContext("option", "value");
							jteOutput.writeUserContent(__jte_html_attribute_16);
							jteOutput.setContext("option", null);
							jteOutput.writeContent("\"");
						}
						jteOutput.writeContent(" selected>");
						jteOutput.setContext("option", null);
						jteOutput.writeUserContent(g.dat());
						jteOutput.writeContent("</option>\n                        ");
					} else {
						jteOutput.writeContent("\n                            <option");
						var __jte_html_attribute_17 = g.dat();
						if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_17)) {
							jteOutput.writeContent(" value=\"");
							jteOutput.setContext("option", "value");
							jteOutput.writeUserContent(__jte_html_attribute_17);
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
				jteOutput.writeContent("\n                ");
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
