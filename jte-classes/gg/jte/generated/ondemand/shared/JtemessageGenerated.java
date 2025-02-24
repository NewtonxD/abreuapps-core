package gg.jte.generated.ondemand.shared;
@SuppressWarnings("unchecked")
public final class JtemessageGenerated {
	public static final String JTE_NAME = "shared/message.jte";
	public static final int[] JTE_LINE_INFO = {0,0,0,0,0,5,5,5,7,7,7,7,10,10,10,18,18,20,20,20,0,1,1,1,1};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, String msg, Boolean status) {
		jteOutput.writeContent("\n<div class=\"row\">\n    <div class=\"col mt-2 mb-2\">\n        ");
		if (msg!=null) {
			jteOutput.writeContent("\n            <div\n                    class=\"alert ");
			jteOutput.setContext("div", "class");
			jteOutput.writeUserContent(status ? "alert-success" : "alert-warning");
			jteOutput.setContext("div", null);
			jteOutput.writeContent(" alert-dismissible fade show\"\n                    role=\"alert\"\n            >\n                <b><span>");
			jteOutput.setContext("span", null);
			jteOutput.writeUserContent(msg);
			jteOutput.writeContent("</span></b>\n                <button\n                        type=\"button\"\n                        class=\"btn-close\"\n                        data-bs-dismiss=\"alert\"\n                        aria-label=\"Close\"\n                ></button>\n            </div>\n        ");
		}
		jteOutput.writeContent("\n    </div>\n</div>");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		String msg = (String)params.get("msg");
		Boolean status = (Boolean)params.get("status");
		render(jteOutput, jteHtmlInterceptor, msg, status);
	}
}
