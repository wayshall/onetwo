package org.onetwo.plugins.fmtag.directive;

import java.util.Collections;
import java.util.List;

import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.Page;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.web.view.ftl.DirectivesUtils;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;

public class DataField extends AbstractJFieldView {

	String value;
	int colspan;
//	String link;
	boolean autoRender = true;
	String render;
	//改字段是否可以排序
	boolean order = false;
	//该字段在本次渲染里是否排序
	boolean ordering = false;
	boolean search = false;
	
	//表格显示时的顺序
//	private int showOrder;
//	private boolean showable = true;
	
	String orderType = Page.DESC;
	String orderBy;
//	String format;
	private String type;
	private List<String> typeList = Collections.EMPTY_LIST;
	
	Environment env;
	TemplateDirectiveBody body;
	
	public DataField(String name, String label) {
		super();
		this.name = name;
		if(StringUtils.isNotBlank(label))
			this.label = label;
	}
	public int getColspan() {
		return colspan;
	}
	/*public String getLink() {
		return link;
	}*/
	public boolean isAutoRender() {
		return autoRender;
	}
	public String getRender() {
		return render;
	}
	public String getValue() {
		if(StringUtils.isBlank(value) && StringUtils.isNotBlank(getName())){
			value = getName();
		}
		return value;
	}
	
	public void render(){
		DirectivesUtils.render(name, env, body);
	}
	public boolean isOrder() {
		return order;
	}
	public String getOrderType() {
		return orderType;
	}
	public String getOrderBy() {
		if(StringUtils.isBlank(orderBy)){
			orderBy = getName();
		}
		return orderBy;
	}
	
	public String getOrderByString(){
		if(!isOrder())
			return "";
		StringBuilder sb = new StringBuilder();
		sb.append("order=").append(orderType).append("&orderBy=").append(getOrderBy());
		return sb.toString();
	}
	
	public String appendOrderBy(String action){
		String str = action;
		if(action.indexOf('?')!=-1){
			str += "&"+getOrderByString();
		}else{
			str += "?"+getOrderByString();
		}
		return str;
	}
	public boolean isOrdering() {
		return ordering;
	}
	public String getType() {
		return type;
	}
	
	public void setType(String type){
		int indx = type.indexOf(":");
		if(indx<1){
			this.type = type;
		}else{
			this.type = type.substring(0, indx);
			String str = type.substring(indx+1);
			this.typeList = LangUtils.asList(StringUtils.split(str, ","));
		}
	}
	public boolean isSearch() {
		return search;
	}
	
	public List<String> getTypeList() {
		return typeList;
	}
	public void setSearch(boolean search) {
		this.search = search;
	}
	
	public String getGridAttributesHtml() {
		StringBuilder attributesBuf = new StringBuilder();
		if(getColspan()!=0)
			buildAttributeTag(attributesBuf, "colspan", getColspan());
		buildAttributeTag(attributesBuf, "style", getCssStyle());
		buildAttributeTag(attributesBuf, "class", getCssClass());
		buildAttributeTag(attributesBuf, "onclick", getOnclick());
		return attributesBuf.toString();
	}
}
