package gg.jte.generated.ondemand.fragments;
import abreuapps.core.control.usuario.UsuarioDTO;
import java.util.List;
@SuppressWarnings("unchecked")
public final class Jteusr_mgr_principalGenerated {
	public static final String JTE_NAME = "fragments/usr_mgr_principal.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,3,3,3,3,9,9,18,18,28,28,29,29,29,29,29,29,29,29,29,30,30,30,31,31,31,32,32,32,33,33,33,35,35,37,37,37,37,39,39,39,39,41,41,41,43,43,43,3,4,5,6,7,7,7,7};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, Boolean usr_mgr_principal, Boolean usr_mgr_registro, String msg, Boolean status, List<UsuarioDTO> usuarios) {
		jteOutput.writeContent("\n");
		gg.jte.generated.ondemand.shared.JteconsultaGenerated.render(jteOutput, jteHtmlInterceptor, "Usuarios", "usr_mgr_registro", usr_mgr_principal, usr_mgr_registro, msg, status, true, new gg.jte.html.HtmlContent() {
			public void writeTo(gg.jte.html.HtmlTemplateOutput jteOutput) {
				jteOutput.writeContent("\n        <thead class=\"bg-secondary text-white\">\n        <tr>\n            <th scope=\"col\">Usuario</th>\n            <th scope=\"col\">Correo</th>\n            <th scope=\"col\">Clave Vencida</th>\n            <th scope=\"col\">Estado</th>\n        </tr>\n        </thead>\n        <tbody>\n        ");
				for (var u: usuarios) {
					jteOutput.writeContent("\n            <tr");
					var __jte_html_attribute_0 = u.usr();
					if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_0)) {
						jteOutput.writeContent(" data-id=\"");
						jteOutput.setContext("tr", "data-id");
						jteOutput.writeUserContent(__jte_html_attribute_0);
						jteOutput.setContext("tr", null);
						jteOutput.writeContent("\"");
					}
					jteOutput.writeContent(">\n                <th>");
					jteOutput.setContext("th", null);
					jteOutput.writeUserContent(u.usr());
					jteOutput.writeContent("</th>\n                <td>");
					jteOutput.setContext("td", null);
					jteOutput.writeUserContent(u.mail());
					jteOutput.writeContent("</td>\n                <td>");
					jteOutput.setContext("td", null);
					jteOutput.writeUserContent(u.pwd_chg()?"Si":"No");
					jteOutput.writeContent("</td>\n                <td>");
					jteOutput.setContext("td", null);
					jteOutput.writeUserContent(u.act()?"Activo":"Inactivo");
					jteOutput.writeContent("</td>\n            </tr>\n        ");
				}
				jteOutput.writeContent("\n        </tbody>\n    ");
			}
		}, new gg.jte.html.HtmlContent() {
			public void writeTo(gg.jte.html.HtmlTemplateOutput jteOutput) {
				jteOutput.writeContent("\n        <script src=\"/content/js/tb_init.js\"></script>\n        ");
				if (usr_mgr_registro) {
					jteOutput.writeContent(" <script src=\"/content/js/usrmgr/usr782360.js\"></script> ");
				}
				jteOutput.writeContent("\n        <script src=\"/content/js/usrmgr/es_usr782360.js\"></script>\n    ");
			}
		});
		jteOutput.writeContent("\n\n");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		Boolean usr_mgr_principal = (Boolean)params.get("usr_mgr_principal");
		Boolean usr_mgr_registro = (Boolean)params.get("usr_mgr_registro");
		String msg = (String)params.get("msg");
		Boolean status = (Boolean)params.get("status");
		List<UsuarioDTO> usuarios = (List<UsuarioDTO>)params.get("usuarios");
		render(jteOutput, jteHtmlInterceptor, usr_mgr_principal, usr_mgr_registro, msg, status, usuarios);
	}
}
