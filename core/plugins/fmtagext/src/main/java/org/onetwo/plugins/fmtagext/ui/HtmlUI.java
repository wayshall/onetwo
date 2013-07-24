package org.onetwo.plugins.fmtagext.ui;

public class HtmlUI implements RendableUI {
	
	private final String html;
	
	public HtmlUI(String html) {
		super();
		this.html = html;
	}

	@Override
	public void onRender(UIRender render) {
		render.renderString(html);
	}

	public String getHtml() {
		return html;
	}

}
