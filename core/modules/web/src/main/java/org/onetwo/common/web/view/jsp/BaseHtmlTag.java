package org.onetwo.common.web.view.jsp;

import javax.servlet.jsp.JspException;

import org.onetwo.common.web.view.HtmlElement;

@SuppressWarnings("serial")
abstract public class BaseHtmlTag<T extends HtmlElement> extends AbstractBodyTag {
	

	protected String id;
	protected String name;
	protected String title;
	protected String label;
	protected String cssStyle;
	protected String cssClass;
	protected String onclick;
	
	protected T component;
	
	
	abstract public T createComponent();
	
	
	
	@Override
	public int doStartTag() throws JspException {
		component = createComponent();
		this.populateComponent();
		return EVAL_BODY_BUFFERED;
	}
	
	protected void populateComponent() throws JspException{
		component.setId(id);
		component.setName(name);
		component.setTitle(title);
		component.setLabel(label);
		component.setCssClass(cssClass);
		component.setCssStyle(cssStyle);
		component.setOnclick(onclick);
	}

	@Override
	public int doEndTag() throws JspException {
		return super.doEndTag();
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getCssStyle() {
		return cssStyle;
	}

	public void setCssStyle(String cssStyle) {
		this.cssStyle = cssStyle;
	}

	public String getCssClass() {
		return cssClass;
	}

	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}

	public String getOnclick() {
		return onclick;
	}

	public void setOnclick(String onclick) {
		this.onclick = onclick;
	}
	
	
}
