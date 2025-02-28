package gg.jte.generated.ondemand.fragments;
import abreuapps.core.control.usuario.Usuario;
import abreuapps.core.control.general.Persona;
import abreuapps.core.control.utils.DateUtils;
import abreuapps.core.control.general.DatoDTO;
import java.util.List;
@SuppressWarnings("unchecked")
public final class Jteusr_mgr_registroGenerated {
	public static final String JTE_NAME = "fragments/usr_mgr_registro.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,2,3,4,6,6,6,6,16,16,24,24,26,26,26,26,26,26,26,26,26,27,27,27,27,27,27,27,27,27,28,28,28,28,28,28,28,28,28,29,29,29,29,29,29,29,29,29,30,30,39,39,43,43,46,46,58,58,59,59,59,59,59,59,59,59,59,60,60,60,60,60,60,60,60,60,61,61,64,64,64,64,64,64,64,64,64,69,69,69,69,69,69,69,69,69,74,74,74,74,74,86,86,98,98,106,106,109,109,109,109,111,111,113,113,115,115,116,116,116,116,116,6,7,8,9,10,11,12,14,14,14,14};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, Boolean usr_mgr_registro, DateUtils dateUtils, Boolean configuracion, Usuario user, Persona persona, List<DatoDTO> sexo, List<DatoDTO> sangre, Boolean update) {
		jteOutput.writeContent("\n");
		gg.jte.generated.ondemand.shared.JteregistroGenerated.render(jteOutput, jteHtmlInterceptor, (configuracion ? "Editar Mi" : "" ) +" Usuario", "usr_mgr_principal", usr_mgr_registro, update, new gg.jte.html.HtmlContent() {
			public void writeTo(gg.jte.html.HtmlTemplateOutput jteOutput) {
				jteOutput.writeContent("\n        <section class=\"mt-3 mb-3 me-4 ms-4\">\n            <input type=\"text\" id=\"original_usuario\"");
				var __jte_html_attribute_0 = user.getUsername();
				if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_0)) {
					jteOutput.writeContent(" value=\"");
					jteOutput.setContext("input", "value");
					jteOutput.writeUserContent(__jte_html_attribute_0);
					jteOutput.setContext("input", null);
					jteOutput.writeContent("\"");
				}
				jteOutput.writeContent(" style=\"display: none; width: 1px;height: 1px;\" >\n            <input type=\"text\" id=\"original_correo\"");
				var __jte_html_attribute_1 = user.getCorreo();
				if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_1)) {
					jteOutput.writeContent(" value=\"");
					jteOutput.setContext("input", "value");
					jteOutput.writeUserContent(__jte_html_attribute_1);
					jteOutput.setContext("input", null);
					jteOutput.writeContent("\"");
				}
				jteOutput.writeContent(" style=\"display: none; width: 1px;height: 1px;\"  >\n            <input type=\"text\" id=\"original_cedula\"");
				var __jte_html_attribute_2 = persona.getCedula();
				if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_2)) {
					jteOutput.writeContent(" value=\"");
					jteOutput.setContext("input", "value");
					jteOutput.writeUserContent(__jte_html_attribute_2);
					jteOutput.setContext("input", null);
					jteOutput.writeContent("\"");
				}
				jteOutput.writeContent(" style=\"display: none; width: 1px;height: 1px;\" >\n            <input type=\"text\" id=\"update\"");
				var __jte_html_attribute_3 = update;
				if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_3)) {
					jteOutput.writeContent(" value=\"");
					jteOutput.setContext("input", "value");
					jteOutput.writeUserContent(__jte_html_attribute_3);
					jteOutput.setContext("input", null);
					jteOutput.writeContent("\"");
				}
				jteOutput.writeContent(" style=\"display: none; width: 1px;height: 1px;\" >\n            ");
				if (update) {
					jteOutput.writeContent("\n                <article class=\"pt-2\">\n                    <div class=\"row\">\n                        <div class=\"mb-3 col-12 col-md-6 col-lg-4 d-flex justify-content-center\">\n                            <button type=\"button\" class=\"btn btn-danger\" id=\"BtnResetPwd\"><b><i class=\"fa fa-rotate-right\"></i>&nbsp;Reestablecer Clave</b></button>\n                        </div>\n                        <div class=\"mb-3 col-12 col-md-6 col-lg-4 d-flex justify-content-center\">\n                            <button type=\"button\" class=\"btn btn-primary\" id=\"BtnCloseSess\"><b><i class=\"fa fa-sign-out\"></i>&nbsp;Cerrar Sesión</b></button>\n                        </div>\n                        ");
					if (!configuracion) {
						jteOutput.writeContent("\n                            <div class=\"mb-3 col-12 col-md-6 col-lg-4 d-flex justify-content-center\">\n                                <button type=\"button\" class=\"btn btn-success\" id=\"BtnEditAcc\"><b><i class=\"fa fa-users\"></i>&nbsp;Editar Permisos</b></button>\n                            </div>\n                        ");
					}
					jteOutput.writeContent("\n                    </div>\n                </article>\n            ");
				}
				jteOutput.writeContent("\n            <article class=\"pt-2\">\n                <h5>Información del Usuario</h5>\n                <div class=\"row\" id=\"inf_usuario\">\n                    <div class=\"col-12 mt-2 mb-2\">\n                        <div class=\"alert alert-warning alert-correo\" role=\"alert\" style=\"display:none;\">\n                            <span>Correo en uso. Verifique y vuelva a intentarlo.</span>\n                        </div>\n                        <div class=\"alert alert-warning alert-usuario\" role=\"alert\" style=\"display:none;\">\n                            <span>Usuario en uso. Verifique y vuelva a intentarlo.</span>\n                        </div>\n                    </div>\n                    ");
				if (update) {
					jteOutput.writeContent("\n                        <input  type=\"datetime\" name=\"fecha_actualizacionn\" style=\"display: none; width: 1px;height: 1px;\"");
					var __jte_html_attribute_4 = dateUtils.DateToFormato2(user.getFecha_actualizacion());
					if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_4)) {
						jteOutput.writeContent(" value=\"");
						jteOutput.setContext("input", "value");
						jteOutput.writeUserContent(__jte_html_attribute_4);
						jteOutput.setContext("input", null);
						jteOutput.writeContent("\"");
					}
					jteOutput.writeContent(">\n                        <input  type=\"number\" class=\"idusuario\" name=\"id\" id=\"id\"");
					var __jte_html_attribute_5 = user.getId();
					if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_5)) {
						jteOutput.writeContent(" value=\"");
						jteOutput.setContext("input", "value");
						jteOutput.writeUserContent(__jte_html_attribute_5);
						jteOutput.setContext("input", null);
						jteOutput.writeContent("\"");
					}
					jteOutput.writeContent(" style=\"display: none; width: 1px;height: 1px;\">\n                    ");
				}
				jteOutput.writeContent("\n                    <div class=\"mb-3 col-12 col-md-6\">\n                        <label for=\"usuario\" class=\"form-label text-muted\">Usuario:</label>\n                        <input type=\"text\" name=\"username\" id=\"username\"");
				var __jte_html_attribute_6 = user.getUsername();
				if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_6)) {
					jteOutput.writeContent(" value=\"");
					jteOutput.setContext("input", "value");
					jteOutput.writeUserContent(__jte_html_attribute_6);
					jteOutput.setContext("input", null);
					jteOutput.writeContent("\"");
				}
				jteOutput.writeContent(" placeholder=\" ... \" class=\"form-control\" required aria-describedby=\"basic-addon4\">\n                    </div>\n\n                    <div class=\"mb-3 col-12 col-md-6\">\n                        <label for=\"correo\" class=\"form-label text-muted\">Correo:</label>\n                        <input type=\"email\" name=\"correo\" id=\"correo\"");
				var __jte_html_attribute_7 = user.getCorreo();
				if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_7)) {
					jteOutput.writeContent(" value=\"");
					jteOutput.setContext("input", "value");
					jteOutput.writeUserContent(__jte_html_attribute_7);
					jteOutput.setContext("input", null);
					jteOutput.writeContent("\"");
				}
				jteOutput.writeContent(" placeholder=\" ... \" class=\"form-control\" required aria-describedby=\"basic-addon4\">\n                    </div>\n\n                    <div class=\"col-12 d-flex justify-content-start\">\n                        <div class=\"form-check\">\n                            <input class=\"form-check-input\" type=\"checkbox\" ");
				var __jte_html_attribute_8 =  !update || user.isActivo() ;
				if (__jte_html_attribute_8) {
				jteOutput.writeContent(" checked");
				}
				jteOutput.writeContent(" name=\"activo\" id=\"activo\">\n                            <label class=\"form-check-label text-muted\" for=\"activo\">Activo</label>\n                        </div>\n                    </div>\n\n\n                </div>\n\n            </article>\n            <article class=\"pt-2\">\n                <h5>Información Personal &nbsp;<button type=\"button\" class=\"btn collapsed\" data-bs-toggle=\"collapse\" data-bs-target=\"#inf_personal\"><i class=\"fa fa-angle-down\"></i></button></h5>\n                <div class=\"row collapse\" id=\"inf_personal\">\n                    ");
				gg.jte.generated.ondemand.shared.Jteinfo_personalGenerated.render(jteOutput, jteHtmlInterceptor, persona, dateUtils, update, sexo, sangre);
				jteOutput.writeContent("\n                </div>\n            </article>\n        </section>\n\n        <div class=\"row pt-2 pb-2 me-4 ms-4  d-flex justify-content-end align-items-center\">\n            <div class=\"col-12 mt-2 mb-2\">\n                <div class=\"alert alert-warning alert-pwd\" role=\"alert\" style=\"display:none;\">\n                    <span>Contraseña incorrecta. Verifique e intentelo de nuevo.</span>\n                </div>\n            </div>\n            <div class=\"col-12 col-md-6 d-flex justify-content-end\">\n                ");
				if (configuracion) {
					jteOutput.writeContent("\n                    <input\n                            class=\"form-control\"\n                            id=\"password\"\n                            type=\"password\"\n                            placeholder=\"Contraseña\"\n                            required=\"required\"\n                    />\n                ");
				}
				jteOutput.writeContent("\n            </div>\n        </div>\n    ");
			}
		}, new gg.jte.html.HtmlContent() {
			public void writeTo(gg.jte.html.HtmlTemplateOutput jteOutput) {
				jteOutput.writeContent("\n        <script src=\"/content/js/lib/input-formats.js\"></script>\n        ");
				if (configuracion) {
					jteOutput.writeContent("\n            <script src=\"/content/js/usrmgr/svd_usr802562.js\"></script>\n        ");
				} else {
					jteOutput.writeContent("\n            <script src=\"/content/js/usrmgr/svd_usr782360.js\"></script>\n        ");
				}
				jteOutput.writeContent("\n    ");
			}
		}, true, !configuracion);
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		Boolean usr_mgr_registro = (Boolean)params.get("usr_mgr_registro");
		DateUtils dateUtils = (DateUtils)params.get("dateUtils");
		Boolean configuracion = (Boolean)params.get("configuracion");
		Usuario user = (Usuario)params.get("user");
		Persona persona = (Persona)params.get("persona");
		List<DatoDTO> sexo = (List<DatoDTO>)params.get("sexo");
		List<DatoDTO> sangre = (List<DatoDTO>)params.get("sangre");
		Boolean update = (Boolean)params.get("update");
		render(jteOutput, jteHtmlInterceptor, usr_mgr_registro, dateUtils, configuracion, user, persona, sexo, sangre, update);
	}
}
