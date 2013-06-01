package org.onetwo.plugins.fmtagext.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;

abstract public class AbstractHtmlElement {

	protected String id;
	protected String name;
	protected String title;
	protected String cssStyle;
	protected String cssClass;
	protected String onclick;
	
	protected Map<String, String> attributes;

	public String getId() {
		if(StringUtils.isBlank(id)){
			id = getName();
		}
		if(id!=null && id.contains(".")){
			id = id.replace('.', '_');
		}
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

	public Map<String, String> getAttributes() {
		if(attributes==null){
			attributes = LangUtils.newHashMap();
		}
		return attributes;
	}

	public String getAttributesString() {
		if(LangUtils.isEmpty(attributes))
			return "";
		StringBuilder str = new StringBuilder();
		for(Entry<String, String> entry : attributes.entrySet()){
			str.append(entry.getKey()).append("=").append(entry.getValue()).append(" ");
		}
		return str.toString();
	}

	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}
	
	public void copyTo(AbstractHtmlElement html){
		html.id = id;
		html.name = name;
		html.onclick = onclick;
		html.title = title;
		html.cssClass = cssClass;
		html.cssStyle = cssStyle;
		if(attributes!=null)
			html.attributes = new HashMap<String, String>(attributes);
	}
}
