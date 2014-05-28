package org.onetwo.common.web.view.jsp.datagrid;

import javax.servlet.jsp.JspException;

import org.onetwo.common.utils.Page;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.convert.ToBooleanConvertor;
import org.onetwo.common.web.view.jsp.datagrid.DataRowTagBean.GridRowData;
import org.onetwo.common.web.view.jsp.grid.BaseGridTag;
import org.onetwo.common.web.view.jsp.grid.FieldTagBean;
import org.onetwo.common.web.view.jsp.grid.RowTagBean;

@SuppressWarnings("serial")
public class DataFieldTag extends BaseGridTag<FieldTagBean> {

	private String value;
	private int colspan;
//	String link;
	private String render;
	//改字段是否可以排序
	private boolean orderable = false;
	private String orderBy;
	private String dataFormat;

	
	
	private boolean searchable;
	private String searchFieldName;
	private String searchFieldType;
	private Object searchItems;
	private String searchItemLabel;
	private String searchItemValue;
	

	private String reserved;//备用
	
	private DataFieldValueListener dataFieldValueListener;
	
	@Override
	public FieldTagBean createComponent() {
		return new FieldTagBean();
	}

	@Override
	public int endTag() throws JspException {
//		assertParentTag(DataRowTag.class);
//		Deque<?> d = this.getTagStack();
		RowTagBean row = getComponentFromRequest(getRowVarName(), RowTagBean.class);
		if(row==null)
			throw new JspException("field tag must nested in a row tag.");

		row.addField(component);
		GridRowData cdata = getComponentFromRequest(DataRowTag.CURRENT_ROW_DATA, GridRowData.class);
		if(cdata!=null){
			/*Object dataFieldValue= null;
			if(!component.isAutoRender()){
				BodyContent bc = getBodyContent();
				if(bc!=null){
					dataFieldValue = cdata.putValue(component.getValue(), bc.getString(), dataFormat);
				}
	//			return EVAL_BODY_INCLUDE;
			}else{
				dataFieldValue = cdata.translateValue(component.getValue(), dataFormat);
			}*/
			Object dataFieldValue = cdata.translateFieldValue(this);
			notifyDataFieldValueListener(cdata, dataFieldValue);
		}
		return EVAL_PAGE;
	}
	
	/***
	 * 通知DataFieldValueListener
	 * @param rowData
	 * @param dataFieldValue
	 */
	private void notifyDataFieldValueListener(GridRowData rowData, Object dataFieldValue){
		if(this.dataFieldValueListener!=null)
			this.dataFieldValueListener.afterTranslateValue(rowData, this, dataFieldValue);
	}
	protected void populateComponent() throws JspException{
		super.populateComponent();
		component.setValue(value);
		component.setDataFormat(dataFormat);
		component.setColspan(colspan);
		component.setRender(render);
		component.setOrderable(orderable);
		component.setOrderBy(orderBy);
//		component.setOrdering(this.orderBy.equals(pageContext.getRequest().getParameter("orderBy")));
		component.setOrdering(StringUtils.equals(this.orderBy, pageContext.getRequest().getParameter("orderBy")));
		String order = pageContext.getRequest().getParameter("order");
		component.setOrderType(Page.DESC.equals(order)?Page.ASC:Page.DESC);
		
		component.setSearchFieldName(searchFieldName);
		component.setSearchable(searchable);
		component.setSearchFieldType(searchFieldType);
		component.setSearchItems(searchItems);
		component.setSearchItemLabel(searchItemLabel);
		component.setSearchItemValue(searchItemValue);
		
		component.setReserved(reserved);
		
	}
	
	
	@Override
	public int startTag() throws JspException {
		super.startTag();
		return component.isAutoRender()?SKIP_BODY:EVAL_BODY_BUFFERED;
	}

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

	public void setOrderable(String orderable) {
		if(StringUtils.isNotBlank(orderable) && !ToBooleanConvertor.FALSE_VALUE.equals(orderable)){
			this.orderable = true;
			if("true".equals(orderable))
				this.orderBy = null;
			else
				this.orderBy = orderable;
		}
	}

	public void setRender(String render) {
		this.render = render;
	}

	public void setDataFormat(String dataFormat) {
		this.dataFormat = dataFormat;
	}


	//search
	public void setSearch(String search) {
		if(StringUtils.isNotBlank(search) && !ToBooleanConvertor.FALSE_VALUE.equals(search)){
			this.searchable = true;
			if("true".equals(search))
				this.searchFieldName = null;
			else
				this.searchFieldName = search;
		}
	}

	public void setSearchFieldType(String searchFieldType) {
		this.searchFieldType = searchFieldType;
	}

	public void setSearchItems(Object searchItems) {
		this.searchItems = searchItems;
	}

	public void setSearchItemLabel(String searchItemLabel) {
		this.searchItemLabel = searchItemLabel;
	}

	public void setSearchItemValue(String searchItemValue) {
		this.searchItemValue = searchItemValue;
	}

	public void setReserved(String reserved) {
		this.reserved = reserved;
	}

}
