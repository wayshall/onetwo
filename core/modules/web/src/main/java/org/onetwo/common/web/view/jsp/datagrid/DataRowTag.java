package org.onetwo.common.web.view.jsp.datagrid;

import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.jsp.JspException;

import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.web.view.jsp.datagrid.DataRowTagBean.GridRowData;
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
			component.setDatas(new ArrayList<GridRowData>(grid.getPage().getResult().size()));
		}
		
		setComponentIntoRequest(getRowVarName(), component);
	}
	
	private void setDataIntoRequest(GridRowData cdata){
		if(StringUtils.isNotBlank(component.getName())){
			pageContext.getRequest().setAttribute(component.getName(), cdata.getOriginData());
			pageContext.getRequest().setAttribute(component.getName()+"Row", cdata);
		}
	}

	@Override
	public int startTag() throws JspException {
		assertParentTag(DataGridTag.class);
		super.startTag();
		if(component.isIterator()){
			Iterator<?> it = component.getIterator();
			if(it.hasNext()){
				Object data = it.next();
				int index = 0;
				GridRowData cdata = GridRowData.create(component, data, index);
				setComponentIntoRequest(CURRENT_ROW_DATA, cdata);
				
				this.setDataIntoRequest(cdata);
				
				return EVAL_BODY_BUFFERED;
			}else{
				if(!component.isFieldTagCompletion()){
					return EVAL_BODY_BUFFERED;
				}
//				clearComponentFromRequest(CURRENT_ROW_DATA);
				
				
				return SKIP_BODY;
			}
		}else{
			GridRowData cdata = GridRowData.create(component, LangUtils.newHashMap(), 0);
			setComponentIntoRequest(CURRENT_ROW_DATA, cdata);
			
			component.setCurrentRowData(cdata);
			
			return EVAL_BODY_BUFFERED;
		}
	}


	@Override
	public int endTag() throws Exception {
		clearComponentFromRequest(CURRENT_ROW_DATA);
		if(StringUtils.isNotBlank(component.getName()))
			pageContext.getRequest().removeAttribute(component.getName());
		return super.endTag();
	}

	@Override
	public int doAfterBody() throws JspException {
		if(component.isIterator()){
			component.setFieldTagCompletion(true);
			GridRowData preData = getComponentFromRequest(CURRENT_ROW_DATA, GridRowData.class);
			if(preData!=null){
				component.addRowData(preData);//preData.getTranslateData()
			}
			Iterator<?> it = component.getIterator();
			if(it.hasNext()){
				Object data = it.next();
				int index = 0;
				if(preData!=null)
					index = preData.getIndex()+1;
				GridRowData cdata = GridRowData.create(component, data, index);
				setComponentIntoRequest(CURRENT_ROW_DATA, cdata);
				
				this.setDataIntoRequest(cdata);
				
				return EVAL_BODY_AGAIN;
			}
		}
		return super.doAfterBody();
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
