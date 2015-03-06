package org.onetwo.common.web.view;

import java.util.Map;
import java.util.Map.Entry;

import org.onetwo.common.utils.LangUtils;
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

	private Map<String, Object> dynamicAttributes = LangUtils.newHashMap();
	

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
		return StringUtils.emptyIfNull(cssStyle);
	}

	public String getCssClass() {
		return StringUtils.emptyIfNull(cssClass);
	}

	public String getOnclick() {
		return onclick;
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
	
	protected StringBuilder buildAttributeTag(StringBuilder attributesBuf, String attr, Object val){
		String valStr = val==null?"":val.toString();
		if(StringUtils.isBlank(valStr))
			return attributesBuf;
		valStr = HtmlUtils.htmlEscape(valStr);
		attributesBuf.append(attr).append("=\"").append(valStr).append("\"");
		return attributesBuf;
	}

	public String getAttributesHtml() {
		StringBuilder attributesBuf = new StringBuilder();
		buildAttributeTag(attributesBuf, "id", getId());
		buildAttributeTag(attributesBuf, "name", getName());
		buildAttributeTag(attributesBuf, "title", getTitle());
//		buildAttributeTag(attributesBuf, "style", getCssStyle());
//		buildAttributeTag(attributesBuf, "class", getCssClass());
		buildAttributeTag(attributesBuf, "onclick", getOnclick());
		if(StringUtils.isNotBlank(getTitle()))
			buildAttributeTag(attributesBuf, "data-toggle", "tooltip");
		
		this.buildExtTagAttributesString(attributesBuf);
		attributesBuf.append(" ").append(getDynamicAttributesHtml());
		return attributesBuf.toString();
	}

	public String getGridAttributesHtml() {
		StringBuilder attributesBuf = new StringBuilder();
		buildAttributeTag(attributesBuf, "title", getTitle());
//		buildAttributeTag(attributesBuf, "style", getCssStyle());
//		buildAttributeTag(attributesBuf, "class", getCssClass());
		buildAttributeTag(attributesBuf, "onclick", getOnclick());
		attributesBuf.append(" ").append(getDynamicAttributesHtml());
		return attributesBuf.toString();
	}

	public Map<String, Object> getDynamicAttributes() {
		return dynamicAttributes;
	}

	public void setDynamicAttributes(Map<String, Object> dynamicAttributes) {
		this.dynamicAttributes = dynamicAttributes;
	}

	public String getDynamicAttributesHtml() {
		StringBuilder attributesBuf = new StringBuilder();
		for(Entry<String, Object> entry : this.dynamicAttributes.entrySet()){
			buildAttributeTag(attributesBuf, entry.getKey(), entry.getValue());
		}
		return attributesBuf.toString();
	}

}
