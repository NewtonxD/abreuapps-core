package gg.jte.generated.ondemand.fragments;
import abreuapps.core.control.transporte.LogVehiculo;
import java.util.List;
@SuppressWarnings("unchecked")
public final class JtegeneralGenerated {
	public static final String JTE_NAME = "fragments/general.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,3,3,3,3,14,14,14,14,22,22,22,48,48,50,50,50,50,50,50,50,50,50,51,51,51,53,53,55,55,57,57,59,59,62,62,62,63,63,63,64,64,64,65,65,65,68,68,82,82,82,3,4,5,5,5,5};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, int active_views, int today_views, List<LogVehiculo> vhl_log) {
		jteOutput.writeContent("\n<main class=\"main-content\" id=\"content-page\">\n    <div class=\"container-fluid px-4\">\n        <div class=\"row d-flex justify-content-center mt-2\">\n            <div class=\"col-12 col-sm-6 col-lg-4 mt-1 mb-1\">\n                <div class=\"card\">\n                    <div class=\"card-body text-center row align-items-center\">\n                        <div class=\"col-12 col-lg-6\"><h3 class=\"pt-2 pb-2\">Clientes Activos</h3></div>\n                        <div class=\"col-12 col-lg-6\"><h1 id=\"clientes_activos\" >");
		jteOutput.setContext("h1", null);
		jteOutput.writeUserContent(active_views);
		jteOutput.writeContent("</h1></div>\n                    </div>\n                </div>\n            </div>\n            <div class=\"col-12 col-sm-6 col-lg-4 mt-1 mb-1\">\n                <div class=\"card\">\n                    <div class=\"card-body text-center row align-items-center\">\n                        <div class=\"col-12 col-lg-6\"><h3 class=\"pt-2 pb-2\">Visitas Hoy</h3></div>\n                        <div class=\"col-12 col-lg-6\"><h1 id=\"views_hoy\">");
		jteOutput.setContext("h1", null);
		jteOutput.writeUserContent(today_views);
		jteOutput.writeContent("</h1></div>\n                    </div>\n                </div>\n            </div>\n        </div>\n        <div class=\"row d-flex justify-content-center mt-2\">\n            <div class=\"col-12  col-lg-8 mt-1 mb-1\">\n                <div class=\"card\">\n                    <div class=\"card-body text-center\">\n                        <h4 class=\"pt-2 pb-1\">Eventos del Transporte</h4>\n                        <div class=\"table-responsive table-responsive-md\">\n                            <table\n                                class=\"table table-hover mt-2\"\n                                id=\"table\"\n                                >\n                                <thead class=\"bg-secondary text-white\">\n                                    <tr>\n                                        <th scope=\"col\">ID</th>\n                                        <th scope=\"col\"> </th>\n                                        <th scope=\"col\">Placa</th>\n                                        <th scope=\"col\">Ruta</th>\n                                        <th scope=\"col\">Estado</th>\n                                        <th scope=\"col\">Fecha</th>\n                                    </tr>\n                                </thead>\n                                <tbody>\n                                    ");
		for (var d : vhl_log) {
			jteOutput.writeContent("\n\n                                        <tr");
			var __jte_html_attribute_0 = d.getId();
			if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_0)) {
				jteOutput.writeContent(" data-id=\"");
				jteOutput.setContext("tr", "data-id");
				jteOutput.writeUserContent(__jte_html_attribute_0);
				jteOutput.setContext("tr", null);
				jteOutput.writeContent("\"");
			}
			jteOutput.writeContent(">\n                                            <th scope=\"row\">");
			jteOutput.setContext("th", null);
			jteOutput.writeUserContent(d.getId());
			jteOutput.writeContent("</th>\n                                            <td>\n                                                ");
			if (d.getEstado_new()=="Averiado") {
				jteOutput.writeContent("\n                                                    <img src=\"/content/assets/img/car-crash-icon.webp\" alt=\"alt\" height=\"32\" width=\"32\"/>\n                                                ");
			}
			jteOutput.writeContent("\n        \n                                                ");
			if (d.isSystem_change()) {
				jteOutput.writeContent("\n                                                    <img src=\"/content/assets/img/bot-icon.webp\" alt=\"alt\" height=\"32\" width=\"32\"/>\n                                                ");
			}
			jteOutput.writeContent("\n                                            </td>\n\n                                            <td>");
			jteOutput.setContext("td", null);
			jteOutput.writeUserContent(d.getPlaca());
			jteOutput.writeContent("</td>\n                                            <td>");
			jteOutput.setContext("td", null);
			jteOutput.writeUserContent(d.getRuta_new());
			jteOutput.writeContent("</td>\n                                            <td>");
			jteOutput.setContext("td", null);
			jteOutput.writeUserContent((d.isSystem_change() ? "Sistema - ":"" ) + d.getEstado_new());
			jteOutput.writeContent("</td>\n                                            <td>");
			jteOutput.setContext("td", null);
			jteOutput.writeUserContent(String.valueOf(d.getFecha_registro()));
			jteOutput.writeContent("</td>\n                                        </tr>\n\n                                    ");
		}
		jteOutput.writeContent("\n\n                                </tbody>\n                            </table>\n                        </div>\n                        <script src=\"/content/js/es_dash6085603.js\"></script>\n                        <script src=\"/content/js/tb_init_dashboard.js\"></script>\n                    </div>\n                </div>\n            </div>\n        </div>\n    </div>\n</main>\n>\n");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		int active_views = (int)params.get("active_views");
		int today_views = (int)params.get("today_views");
		List<LogVehiculo> vhl_log = (List<LogVehiculo>)params.get("vhl_log");
		render(jteOutput, jteHtmlInterceptor, active_views, today_views, vhl_log);
	}
}
