package org.onetwo.common.web.view;

import org.onetwo.common.utils.StringUtils;
import org.springframework.web.util.HtmlUtils;

abstract public class HtmlElement {

	protected String id;
	protected String name;
	protected String title;
	protected String label;
	protected String cssStyle;
	protected String cssClass;
	protected String onclick;
	
	protected String attributes;
	

//	protected StringBuilder attributesBuf;

	public String getId() {
		if(StringUtils.isBlank(id) && StringUtils.isNotBlank(getName()))
			id = getName().replace('.', '_');
		return id;
	}

	public String getName() {
		/*if(StringUtils.isBlank(name)){
			return getClass().getSimpleName();
		}*/
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

	public void buildTagAttributesString(StringBuilder attributesBuf){
		buildAttributeTag(attributesBuf, "id", getId());
		buildAttributeTag(attributesBuf, "name", getName());
		buildAttributeTag(attributesBuf, "title", getTitle());
		buildAttributeTag(attributesBuf, "style", getCssStyle());
		buildAttributeTag(attributesBuf, "class", getCssClass());
		buildAttributeTag(attributesBuf, "onclick", getOnclick());
	}

	public void buildExtTagAttributesString(StringBuilder attributesBuf){
	}
	
	protected void buildAttributeTag(StringBuilder attributesBuf, String attr, Object val){
		String valStr = val==null?"":val.toString();
		if(StringUtils.isBlank(valStr))
			return ;
		valStr = HtmlUtils.htmlEscape(valStr);
		attributesBuf.append(attr).append("=\"").append(valStr).append("\"");
	}

	public String getAttributesHtml() {
		StringBuilder attributesBuf = new StringBuilder();
		buildAttributeTag(attributesBuf, "id", getId());
		buildAttributeTag(attributesBuf, "name", getName());
		buildAttributeTag(attributesBuf, "title", getTitle());
		buildAttributeTag(attributesBuf, "style", getCssStyle());
		buildAttributeTag(attributesBuf, "class", getCssClass());
		buildAttributeTag(attributesBuf, "onclick", getOnclick());
		if(StringUtils.isNotBlank(getTitle()))
			buildAttributeTag(attributesBuf, "data-toggle", "tooltip");
		
		this.buildExtTagAttributesString(attributesBuf);
		if(StringUtils.isNotBlank(attributes))
			attributesBuf.append(" ").append(attributes);
		return attributesBuf.toString();
	}

	public String getGridAttributesHtml() {
		StringBuilder attributesBuf = new StringBuilder();
		buildAttributeTag(attributesBuf, "style", getCssStyle());
		buildAttributeTag(attributesBuf, "class", getCssClass());
		buildAttributeTag(attributesBuf, "onclick", getOnclick());
		return attributesBuf.toString();
	}


}
