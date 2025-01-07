package gg.jte.generated.ondemand;
import java.util.List;
import abreuapps.core.control.transporte.LogVehiculo;
import abreuapps.core.control.general.Persona;
import java.util.Map;
@SuppressWarnings("unchecked")
public final class JteindexGenerated {
	public static final String JTE_NAME = "index.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,2,3,5,5,5,5,23,23,23,23,23,25,25,25,41,41,41,44,44,46,51,57,61,71,71,71,5,6,7,8,9,10,11,12,12,12,12};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, String server_ip, String app_nombre, List<LogVehiculo> vhl_log, int active_views, int today_views, Persona datos_personales, Map< String, Boolean > permisos, Map< String, String > conf) {
		jteOutput.writeContent("\n<!DOCTYPE html>\n<html lang=\"es\">\n    <head>\n        <meta charset=\"utf-8\" />\n        <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\" />\n        <meta\n            name=\"viewport\"\n            content=\"width=device-width, initial-scale=1, shrink-to-fit=no\"\n        />\n        <meta name=\"description\" content=\"Sistema ERP ");
		jteOutput.setContext("meta", "content");
		jteOutput.writeUserContent(app_nombre);
		jteOutput.setContext("meta", null);
		jteOutput.writeContent("\" />\n        <meta name=\"author\" content=\"Carlos Isaac Abreu Pérez\" />\n        <title>");
		jteOutput.setContext("title", null);
		jteOutput.writeUserContent(app_nombre);
		jteOutput.writeContent(" ERP</title>\n        <link rel=\"icon\" type=\"image/x-icon\" href=\"/content/favicon.ico\">\n        <link href=\"/content/css/jquery.dataTables.min.css\" rel=\"stylesheet\"/>\n        <link href=\"/content/css/styles.css\" rel=\"stylesheet\" />\n        <link href=\"/content/css/treestyle.css\" rel=\"stylesheet\" />\n        <link rel=\"stylesheet\" href=\"/content/css/font-awesome.min.css\"/>\n        <link href=\"/content/css/bootstrap.min.css\" rel=\"stylesheet\" />\n        <link rel=\"stylesheet\" href=\"/content/css/leaflet.css\"/>\n        <link href=\"/content/css/leaflet-geoman.css\" rel=\"stylesheet\" />\n    </head>\n\n    <body class=\"fondo-bonito-paginas\">\n        <script src=\"/content/js/lib/jquery-3.6.4.min.js\"></script>\n        <script src=\"/content/js/lib/jquery.dataTables.min.js\"></script>\n        <script src=\"/content/js/lib/bootstrap.bundle.min.js\"></script>\n        <script type=\"text/javascript\">\n            const SERVER_IP = '");
		jteOutput.setContext("script", null);
		jteOutput.writeUserContent(server_ip);
		jteOutput.writeContent("';\n        </script>\n        <script src=\"/content/js/global_vars.js\"></script>\n        ");
		gg.jte.generated.ondemand.fragments.JteheaderGenerated.render(jteOutput, jteHtmlInterceptor, app_nombre);
		jteOutput.writeContent("\n        <div id=\"layoutSidenav\">\n            ");
		gg.jte.generated.ondemand.fragments.JtemenuGenerated.render(jteOutput, jteHtmlInterceptor, app_nombre, datos_personales, permisos, conf);
		jteOutput.writeContent("\n            <div id=\"layoutSidenav_content\">\n                <div aria-live=\"polite\" aria-atomic=\"true\" class=\"position-relative\">\n                    <div id=\"toast-container\" class=\"toast-container top-0 end-0 p-3\"></div>\n                </div>\n                <main>\n                    ");
		gg.jte.generated.ondemand.fragments.JtegeneralGenerated.render(jteOutput, jteHtmlInterceptor, active_views, today_views, vhl_log);
		jteOutput.writeContent("\n                </main>\n            </div>\n            <div class=\"overlay\" onclick=\"hideSidebar()\"></div>\n        </div>\n        <script src=\"/content/js/checkstatus.js\"></script>\n        <script src=\"/content/js/scripts.js\"></script>\n        <script src=\"/content/js/access.js\"></script>\n        <script src=\"/content/js/context-loading.js\"></script>\n    </body>\n</html>");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		String server_ip = (String)params.get("server_ip");
		String app_nombre = (String)params.get("app_nombre");
		List<LogVehiculo> vhl_log = (List<LogVehiculo>)params.get("vhl_log");
		int active_views = (int)params.get("active_views");
		int today_views = (int)params.get("today_views");
		Persona datos_personales = (Persona)params.get("datos_personales");
		Map< String, Boolean > permisos = (Map< String, Boolean >)params.get("permisos");
		Map< String, String > conf = (Map< String, String >)params.get("conf");
		render(jteOutput, jteHtmlInterceptor, server_ip, app_nombre, vhl_log, active_views, today_views, datos_personales, permisos, conf);
	}
}
