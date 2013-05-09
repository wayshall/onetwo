package org.onetwo.plugins.fmtagext.ui;


public class HtmlWrapperUI extends HtmlUI {

	private final UIComponent component;
	public HtmlWrapperUI(String htmlTagName, UIComponent component) {
		super(htmlTagName);
		this.component = component;
	}


	@Override
	public void onRender(UIRender render) {
		render.renderString("<"+getHtml()+">");
		render.renderUIComponent(component);
		render.renderString("</"+getHtml()+">");
	}

}
