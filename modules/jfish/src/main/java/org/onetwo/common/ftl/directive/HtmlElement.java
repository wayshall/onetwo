package org.onetwo.common.ftl.directive;

import org.onetwo.common.utils.StringUtils;

abstract public class HtmlElement {

	protected String id;
	protected String name;
	protected String title;
	protected String label;
	protected String cssStyle;
	protected String cssClass;
	protected String onclick;
	
	protected String attributes;

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getCssStyle() {
		return cssStyle;
	}

	public String getCssClass() {
		return cssClass;
	}

	public String getOnclick() {
		return onclick;
	}

	public String getAttributes() {
		return attributes;
	}

	public String getTitle() {
		return title;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setCssStyle(String cssStyle) {
		this.cssStyle = cssStyle;
	}

	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}

	public void setOnclick(String onclick) {
		this.onclick = onclick;
	}

	public void setAttributes(String attributes) {
		this.attributes = attributes;
	}

	public String getLabel() {
		if(StringUtils.isBlank(label))
			label = getName();
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
}
