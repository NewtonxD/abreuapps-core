package gg.jte.generated.ondemand;
@SuppressWarnings("unchecked")
public final class JtemapaGenerated {
	public static final String JTE_NAME = "mapa.jte";
	public static final int[] JTE_LINE_INFO = {0,0,0,0,0,23,23,23,23,44,44,45,45,46,46,47,47,47,58,58,58,0,1,2,3,4,4,4,4};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, String server_ip, String base_dir, String base_lat, String base_lng, String app_provincia) {
		jteOutput.writeContent("\n<!DOCTYPE html>\n<html lang=\"es\">\n    <head>\n        <meta charset=\"utf-8\" />\n        <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\" />\n        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1, shrink-to-fit=no\" />\n        <meta name=\"description\" content=\"OMSA - Mapa\" />\n        <meta name=\"author\" content=\"Carlos Isaac Abreu Pérez\" />\n        <title>OMSA @app_provincia - Mapa</title>\n        <link rel=\"icon\" type=\"image/x-icon\" href=\"/content/favicon.ico\" />\n        <link rel=\"stylesheet\" href=\"/content/css/bootstrap.min.css\" media=\"none\" onload=\"if(media!='all')media='all'\" />\n        <link href=\"/content/css/mapa_estilos.css\" rel=\"stylesheet\" />\n        <link rel=\"stylesheet\" href=\"/content/css/leaflet.css\" />\n    </head>\n    <body>\n        <div id=\"loading\">\n            <img width=\"64px\" height=\"64px\" src=\"/content/assets/img/Omsafooter.webp\" alt=\"OMSA\" />\n            <h1>");
		jteOutput.setContext("h1", null);
		jteOutput.writeUserContent(app_provincia);
		jteOutput.writeContent("</h1>\n            <span id=\"loader\" style=\"margin-top: 4px;\"></span>\n        </div>\n        <div aria-live=\"polite\" aria-atomic=\"true\" class=\"position-relative\">\n            <div id=\"toast-container\" class=\"toast-container top-0 end-0 p-3\"></div>\n        </div>\n        <div id=\"adContainer\" class=\"ad-container\">\n            <div id=\"adContent\" class=\"ad-content\"></div>\n            <button id=\"closeAd\" class=\"close-btn\" style=\"display: none;\"><b>x</b></button>\n        </div>\n        <div id=\"map\"></div>\n        <script>\n            window.addEventListener('load', function () {\n                setTimeout(function () {\n                    document.getElementById('loading').style.display = 'none';\n                }, 1500);\n            });\n        </script>\n        <script src=\"/content/js/lib/jquery-3.6.4.min.js\"></script>\n        <script src=\"/content/js/lib/bootstrap.bundle.min.js\"></script>\n        <script type=\"text/javascript\">\n            const SERVER_IP = '");
		jteOutput.writeUnsafeContent(server_ip);
		jteOutput.writeContent("';\n            const BASE_LAT = ");
		jteOutput.writeUnsafeContent(base_lat);
		jteOutput.writeContent(";\n            const BASE_LNG = ");
		jteOutput.writeUnsafeContent(base_lng);
		jteOutput.writeContent(";\n            const BASE_DIR = '");
		jteOutput.setContext("script", null);
		jteOutput.writeUserContent(base_dir);
		jteOutput.writeContent("';\n        </script>\n        <script src=\"/content/js/global_vars.js\"></script>\n        <script async src=\"/content/js/checkstatus.js\"></script>\n        <script async src=\"/content/js/ads.js\"></script>\n        <script src=\"/content/js/lib/leaflet.js\"></script>\n        <script src=\"/content/js/lib/leaflet.Marker.SlideTo.js\"></script>\n        <script src=\"/content/js/fetch_cache.js\"></script>\n        <script defer src=\"/content/js/trp/map574702.js\"></script>\n    </body>\n</html>\n");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		String server_ip = (String)params.get("server_ip");
		String base_dir = (String)params.get("base_dir");
		String base_lat = (String)params.get("base_lat");
		String base_lng = (String)params.get("base_lng");
		String app_provincia = (String)params.get("app_provincia");
		render(jteOutput, jteHtmlInterceptor, server_ip, base_dir, base_lat, base_lng, app_provincia);
	}
}
