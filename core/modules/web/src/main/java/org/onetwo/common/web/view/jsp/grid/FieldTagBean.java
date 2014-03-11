package org.onetwo.common.web.view.jsp.grid;

import org.onetwo.common.utils.Page;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.web.view.HtmlElement;

public class FieldTagBean extends HtmlElement {
	public static enum RenderType {
		auto,
		checkbox,
		html
	}

	private String value;
	private String dataFormat;
	private int colspan = 1;
//	String link;
	private RenderType render;
	//改字段是否可以排序
	private boolean orderable = false;
	
	//该字段在本次渲染里是否排序
	private boolean ordering = false;
	
	private String orderType = Page.DESC;
	private String orderBy;
	
	private String bodyContent;
	
	private RowTagBean rowTagBean;
	
	
	//search
	private boolean searchable;
	private String searchFieldName;
	private String searchFieldType;
	private Object searchItems;
	private String searchItemLabel;
	private String searchItemValue;
	

	

	/*public void render(Writer out){
		try {
			out.write(bodyContent);
		} catch (IOException e) {
			throw new BaseException("render field["+value+"] error.");
		}
	}*/
	
	public String getValue() {
		if(value==null)
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
		return RenderType.auto==render;
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
	public String getBodyContent() {
		return bodyContent;
	}
	public void setBodyContent(String bodyContent) {
		this.bodyContent = bodyContent;
	}

	public RenderType getRender() {
		return render;
	}

	public void setRender(String render) {
		if(StringUtils.isBlank(render)){
			this.render = RenderType.auto;
			return ;
		}
		try{
			this.render = RenderType.valueOf(render);
		}catch(Exception e){
			this.render = RenderType.auto;
		}
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
		return RenderType.checkbox==render;
	}
	
	public boolean isHtmlRender(){
		return RenderType.html==render;
	}
	
	public GridTagBean getGrid(){
		return getRowTagBean().getGridTagBean();
	}

	
	//search
	public boolean isSearchable() {
		return searchable;
	}

	public void setSearchable(boolean searchable) {
		this.searchable = searchable;
	}

	public String getSearchFieldType() {
		return searchFieldType;
	}

	public void setSearchFieldType(String searchFieldType) {
		this.searchFieldType = searchFieldType;
	}

	public Object getSearchItems() {
		return searchItems;
	}

	public void setSearchItems(Object searchItems) {
		this.searchItems = searchItems;
	}

	public String getSearchItemLabel() {
		return searchItemLabel;
	}

	public void setSearchItemLabel(String searchItemLabel) {
		this.searchItemLabel = searchItemLabel;
	}

	public String getSearchItemValue() {
		return searchItemValue;
	}

	public void setSearchItemValue(String searchItemValue) {
		this.searchItemValue = searchItemValue;
	}

	public String getSearchFieldName() {
		if(StringUtils.isBlank(searchFieldName))
			return getName();
		return searchFieldName;
	}

	public void setSearchFieldName(String searchFieldName) {
		this.searchFieldName = searchFieldName;
	}

	public String getDataFormat() {
		return dataFormat;
	}

	public void setDataFormat(String dataFormat) {
		this.dataFormat = dataFormat;
	}

}
