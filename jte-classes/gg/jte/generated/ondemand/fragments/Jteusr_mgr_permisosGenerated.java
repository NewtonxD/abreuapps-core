package gg.jte.generated.ondemand.fragments;
import abreuapps.core.control.usuario.Usuario;
@SuppressWarnings("unchecked")
public final class Jteusr_mgr_permisosGenerated {
	public static final String JTE_NAME = "fragments/usr_mgr_permisos.jte";
	public static final int[] JTE_LINE_INFO = {0,0,2,2,2,2,5,5,13,13,20,20,20,20,20,20,20,20,20,41,41,41,41,44,44,44,46,46,46,2,3,3,3,3};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, boolean usr_mgr_registro, Usuario usuario) {
		jteOutput.writeContent("\n");
		gg.jte.generated.ondemand.shared.JteregistroGenerated.render(jteOutput, jteHtmlInterceptor, "Editar Permisos [ "+usuario.getUsername()+" ]", "usr_mgr_principal", usr_mgr_registro, true, new gg.jte.html.HtmlContent() {
			public void writeTo(gg.jte.html.HtmlTemplateOutput jteOutput) {
				jteOutput.writeContent("\n        <div class=\"row me-4 pe-4  d-flex justify-content-end\">\n            <div class=\"mb-3 col-auto\">\n                <button type=\"button\" class=\"btn btn-primary\" onclick=\"saveUsuarioAccess()\" id=\"BtnSave\"><b><i class=\"fa fa-save\"></i>&nbsp;Guardar</b></button>\n            </div>\n        </div>\n        <div class=\"row m-4  d-flex justify-content-center\">\n            <input type=\"hidden\" name=\"idUsuario\"");
				var __jte_html_attribute_0 = usuario.getUsername();
				if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_0)) {
					jteOutput.writeContent(" value=\"");
					jteOutput.setContext("input", "value");
					jteOutput.writeUserContent(__jte_html_attribute_0);
					jteOutput.setContext("input", null);
					jteOutput.writeContent("\"");
				}
				jteOutput.writeContent(" id=\"idUsuario\"/>\n            <div class=\"mb-3 col-auto\">\n                <button type=\"button\" class=\"btn btn-primary\" id=\"BtnMrkAll\" onclick=\"checkAll()\"><b><i class=\"fa fa-check-square\"></i>&nbsp;Marcar Todos</b></button>\n            </div>\n            <div class=\"mb-3 col-auto\">\n                <button type=\"button\" class=\"btn btn-danger\" id=\"BtnMrkNone\" onclick=\"checkNone()\"><b><i class=\"fa fa-square-o\"></i>&nbsp;Desmarcar Todos</b></button>\n            </div>\n            <div class=\"mb-3 col-auto\">\n                <input type=\"text\" id=\"idUsuarioCopy\" class=\"form-control\"/>\n            </div>\n            <div class=\"mb-3 col-auto\">\n                <button type=\"button\" class=\"btn btn-secondary\" id=\"BtnCopy\" onclick=\"usuarioCopyAccess()\"><b><i class=\"fa fa-users\"></i>&nbsp;Copiar</b></button>\n            </div>\n        </div>\n\n        <div class=\"container\">\n\n        </div>\n        <div class=\"row mt-4 mb-4\">\n            <div class=\"col-12 special-access\"></div>\n        </div>\n    ");
			}
		}, new gg.jte.html.HtmlContent() {
			public void writeTo(gg.jte.html.HtmlTemplateOutput jteOutput) {
				jteOutput.writeContent("\n        <script src=\"/content/js/usrmgr/acc812653.js\"></script>\n        <script src=\"/content/js/lib/tree.js\"></script>\n    ");
			}
		}, false, true);
		jteOutput.writeContent("\n\n");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		boolean usr_mgr_registro = (boolean)params.get("usr_mgr_registro");
		Usuario usuario = (Usuario)params.get("usuario");
		render(jteOutput, jteHtmlInterceptor, usr_mgr_registro, usuario);
	}
}
