package org.onetwo.common.web.view.jsp.grid;

import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.Page;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.map.CasualMap;
import org.onetwo.common.web.view.HtmlElement;
import org.onetwo.common.web.view.jsp.TagUtils;

public class FieldTagBean extends HtmlElement {
	public static enum RenderType {
		auto,
		checkbox,
		radio,
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
	

	private String reserved;//备用
	

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
	
	
	public String getGridLabelHtml(){
		FieldTagBean fieldBean = this;
		String linkText = "";
		if(fieldBean.isOrderable()){
			linkText = "<a href='";
			linkText += fieldBean.appendOrderBy(fieldBean.getRowTagBean().getGridTagBean().getActionWithQueryString()) +"' ";

			if(fieldBean.getGrid().isFormPagination()){
				linkText += " data-method='post'";
			}else{
				linkText += " data-method='get'";
			}
			if(fieldBean.getGrid().isAjaxSupported()){
				linkText += " remote=true ajaxName=" + fieldBean.getRowTagBean().getGridTagBean().getAjaxZoneName();
			}
			linkText += ">";
			linkText +=fieldBean.getLabel();
			
			if (fieldBean.isOrdering() && fieldBean.getOrderType()==":desc"){
				linkText += "↑";
			}else if(fieldBean.isOrdering() && fieldBean.getOrderType()==":asc"){
				linkText += "↓";
			}
			
			linkText += "</a>";
		}else{
			linkText = fieldBean.getLabel();
		}
		return linkText;
	}

	public String getOrderByString(){
		if(!isOrderable())
			return "";
		StringBuilder sb = new StringBuilder();
		sb.append("order=").append(orderType).append("&orderBy=").append(getOrderBy());
		return sb.toString();
	}
	
	public String appendOrderBy(String action){
//		String paramStr = action;
		int paramStart = action.indexOf('?');
		String str = "";
		if(paramStart!=-1){
			String paramStr = action.substring(paramStart+1);
			action = action.substring(0, paramStart);
			CasualMap params = new CasualMap(paramStr).filter("order", "orderBy");
			str = TagUtils.appendParamString(action, params.toParamString());
			str = TagUtils.appendParamString(str, getOrderByString());
		}else{
			str = action + "?" +getOrderByString();
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
	
	public boolean isRadio(){
		return RenderType.radio==render;
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


	public String getReserved() {
		return reserved;
	}

	public void setReserved(String reserved) {
		this.reserved = reserved;
	}


	public String getGridTdAttribute() {
		if(colspan<=1)
			return LangUtils.EMPTY_STRING;
		StringBuilder attributesBuf = new StringBuilder();
		buildAttributeTag(attributesBuf, "colspan", getColspan());
		return attributesBuf.toString();
	}
}
