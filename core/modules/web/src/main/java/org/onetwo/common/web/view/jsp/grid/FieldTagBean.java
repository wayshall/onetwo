package org.onetwo.common.web.view.jsp.grid;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.jsp.tagext.BodyContent;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.utils.Page;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.web.view.HtmlElement;

public class FieldTagBean extends HtmlElement {

	private String value;
	private int colspan = 1;
//	String link;
	private String render;
	//改字段是否可以排序
	private boolean orderable = false;
	
	//该字段在本次渲染里是否排序
	private boolean ordering = false;
	
	private String orderType = Page.DESC;
	private String orderBy;
	
	private BodyContent bodyContent;
	
	private RowTagBean rowTagBean;
	
	
	public void render(Writer out){
		try {
			bodyContent.writeOut(out);
		} catch (IOException e) {
			throw new BaseException("render field["+value+"] error.");
		}
	}
	
	public String getValue() {
		if(StringUtils.isBlank(value))
			value = getName();
		return value;
	}

	public String getOrderByString(){
		if(!isOrderable())
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
	
	public void setValue(String value) {
		this.value = value;
	}
	public int getColspan() {
		return colspan;
	}
	public void setColspan(int colspan) {
		this.colspan = colspan;
	}
	public boolean isAutoRender() {
		return StringUtils.isBlank(render);
	}
	public boolean isOrdering() {
		return ordering;
	}
	public void setOrdering(boolean ordering) {
		this.ordering = ordering;
	}
	public boolean isOrderable() {
		return orderable;
	}
	public void setOrderable(boolean orderable) {
		this.orderable = orderable;
	}
	public BodyContent getBodyContent() {
		return bodyContent;
	}
	public void setBodyContent(BodyContent bodyContent) {
		this.bodyContent = bodyContent;
	}

	public String getRender() {
		return render;
	}

	public void setRender(String render) {
		this.render = render;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getOrderBy() {
		if(StringUtils.isBlank(orderBy)){
			orderBy = getName();
		}
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public RowTagBean getRowTagBean() {
		return rowTagBean;
	}

	public void setRowTagBean(RowTagBean rowTagBean) {
		this.rowTagBean = rowTagBean;
	}
	
	public boolean isCheckbox(){
		return "checkbox".equalsIgnoreCase(render);
	}
}
