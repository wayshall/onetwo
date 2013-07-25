package org.onetwo.common.web.view.jsp.grid;

import org.onetwo.common.web.view.HtmlElement;

public class FieldTagBean extends HtmlElement {

	private String value;
	private int colspan;
//	String link;
	private boolean autoRender = true;
	//改字段是否可以排序
	private boolean order = false;
	
	//该字段在本次渲染里是否排序
	private boolean ordering = false;
	private boolean search = false;
	
	public String getValue() {
		return value;
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
		return autoRender;
	}
	public void setAutoRender(boolean autoRender) {
		this.autoRender = autoRender;
	}
	public boolean isOrder() {
		return order;
	}
	public void setOrder(boolean order) {
		this.order = order;
	}
	public boolean isOrdering() {
		return ordering;
	}
	public void setOrdering(boolean ordering) {
		this.ordering = ordering;
	}
	public boolean isSearch() {
		return search;
	}
	public void setSearch(boolean search) {
		this.search = search;
	}
	
}
