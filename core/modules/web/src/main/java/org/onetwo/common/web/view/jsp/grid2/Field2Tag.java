package org.onetwo.common.web.view.jsp.grid2;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;

import org.onetwo.common.utils.Page;
import org.onetwo.common.web.view.jsp.grid.BaseGridTag;
import org.onetwo.common.web.view.jsp.grid.FieldTagBean;
import org.onetwo.common.web.view.jsp.grid.RowTagBean;
import org.onetwo.common.web.view.jsp.grid2.Row2TagBean.CurrentRowData;

@SuppressWarnings("serial")
public class Field2Tag extends BaseGridTag<FieldTagBean> {

	private String value;
	private int colspan;
//	String link;
	private String render;
	//改字段是否可以排序
	private boolean orderable = false;
	private String dataFormat;
	
	@Override
	public FieldTagBean createComponent() {
		return new FieldTagBean();
	}

	@Override
	public int doEndTag() throws JspException {
		RowTagBean row = getComponentFromRequest(getRowVarName(), RowTagBean.class);
		if(row==null)
			throw new JspException("field tag must nested in a row tag.");

		CurrentRowData cdata = getComponentFromRequest(Row2Tag.CURRENT_ROW_DATA, CurrentRowData.class);
		if(!component.isAutoRender()){
			BodyContent bc = getBodyContent();
			if(bc!=null){
				cdata.putValue(component.getValue(), bc.getString(), dataFormat);
			}
//			return EVAL_BODY_INCLUDE;
		}else{
			cdata.translateValue(component.getValue(), dataFormat);
		}
		row.addField(component);
		return EVAL_PAGE;
	}
	protected void populateComponent() throws JspException{
		super.populateComponent();
		component.setValue(value);
		component.setColspan(colspan);
		component.setRender(render);
		component.setOrderable(orderable);
		component.setOrdering(getName().equals(pageContext.getRequest().getParameter("orderBy")));
		String order = pageContext.getRequest().getParameter("order");
		component.setOrderType(Page.DESC.equals(order)?Page.ASC:Page.DESC);
	}
	
	@Override
	public int doStartTag() throws JspException {
		super.doStartTag();
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

	public boolean isOrderable() {
		return orderable;
	}

	public void setOrderable(boolean orderable) {
		this.orderable = orderable;
	}

	public String getRender() {
		return render;
	}

	public void setRender(String render) {
		this.render = render;
	}

	public void setDataFormat(String dataFormat) {
		this.dataFormat = dataFormat;
	}

}
