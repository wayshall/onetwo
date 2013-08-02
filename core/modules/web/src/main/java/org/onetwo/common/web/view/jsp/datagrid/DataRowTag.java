package org.onetwo.common.web.view.jsp.datagrid;

import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.jsp.JspException;

import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.web.view.jsp.datagrid.DataRowTagBean.CurrentRowData;
import org.onetwo.common.web.view.jsp.grid.BaseGridTag;
import org.onetwo.common.web.view.jsp.grid.GridTagBean;
import org.onetwo.common.web.view.jsp.grid.RowTagBean.RowType;

@SuppressWarnings("serial")
public class DataRowTag extends BaseGridTag<DataRowTagBean> {

	public static final String CURRENT_ROW_DATA = "current__row_data";
	
	private RowType type = RowType.row;
	private boolean renderHeader;

	
	@Override
	public DataRowTagBean createComponent() {
		return new DataRowTagBean(type);
	}
	
	protected void populateComponent() throws JspException{
		super.populateComponent();
		GridTagBean grid = getComponentFromRequest(getGridVarName(), GridTagBean.class);
		if(grid==null)
			throw new JspException("row tag must nested in a grid tag.");
		
		component.setRenderHeader(renderHeader);
		grid.addRow(component);
		
		if(component.isIterator()){
			Iterator<?> it = grid.getPage().getResult().iterator();
			component.setIterator(it);
			component.setDatas(new ArrayList<Object>());
		}
		
		setComponentIntoRequest(getRowVarName(), component);
	}

	@Override
	public int doStartTag() throws JspException {
		assertParentTag(DataGridTag.class);
		super.doStartTag();
		if(component.isIterator()){
			Iterator<?> it = component.getIterator();
			if(it.hasNext()){
				Object data = it.next();
				int index = 0;
				CurrentRowData cdata = new CurrentRowData(data, index);
				setComponentIntoRequest(CURRENT_ROW_DATA, cdata);
				
				if(StringUtils.isNotBlank(getName()))
					pageContext.getRequest().setAttribute(getName(), cdata.getOriginData());
				
				return EVAL_BODY_BUFFERED;
			}else{
				if(!component.isFieldTagCompletion()){
					return EVAL_BODY_BUFFERED;
				}
				clearComponentFromRequest(CURRENT_ROW_DATA);
				
				if(StringUtils.isNotBlank(getName()))
					pageContext.getRequest().removeAttribute(getName());
				
				return SKIP_BODY;
			}
		}else{
			return EVAL_BODY_BUFFERED;
		}
	}

	@Override
	public int doAfterBody() throws JspException {
		if(component.isIterator()){
			component.setFieldTagCompletion(true);
			CurrentRowData preData = getComponentFromRequest(CURRENT_ROW_DATA, CurrentRowData.class);
			if(preData!=null){
				component.getDatas().add(preData.getTranslateData());
			}
			Iterator<?> it = component.getIterator();
			if(it.hasNext()){
				Object data = it.next();
				int index = 0;
				if(preData!=null)
					index = preData.getIndex()+1;
				CurrentRowData cdata = new CurrentRowData(data, index);
				setComponentIntoRequest(CURRENT_ROW_DATA, cdata);
				
				if(StringUtils.isNotBlank(getName()))
					pageContext.getRequest().setAttribute(getName(), cdata.getOriginData());
				
				return EVAL_BODY_AGAIN;
			}
		}
		return super.doAfterBody();
	}

	@Override
	public int doEndTag() throws JspException {
		return EVAL_PAGE;
	}

	public void setType(String type) {
		this.type = RowType.valueOf(type);
	}

	public boolean isRenderHeader() {
		return renderHeader;
	}

	public void setRenderHeader(boolean renderHeader) {
		this.renderHeader = renderHeader;
	}

}
