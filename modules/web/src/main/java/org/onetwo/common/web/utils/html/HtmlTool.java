package org.onetwo.common.web.utils.html;

public class HtmlTool {

	private static final HtmlTool instance = new HtmlTool();

	public static HtmlTool getInstance() {
		return instance;
	}

	private HtmlTool(){
	}
	
	protected HtmlFilterConfigHandler htmlFilter = new HtmlFilterConfigHandler();

	public String filterHtml(String text) {
		return this.filterHtml(text, true, true);
	}

	public String filterEnHtml(String text) {
		return this.filterHtml(text, false, true);
	}

	public String filterHtml(String text, Boolean isFilterSpace, Boolean isFilteLine) {
		for (HtmlFilterConfig bbcode : htmlFilter.getFilterConfigs()) {
			text = text.replaceAll(bbcode.getRegex(), bbcode.getReplace());
		}
		if (isFilterSpace) {
			text = text.replaceAll(" ", "");
			text = text.replaceAll("　", "");
			text = text.replaceAll("\\s+", "");
		}
		if (isFilteLine) {
			text = text.replaceAll("\\n+", "");
		}
		return text;
	}

}
