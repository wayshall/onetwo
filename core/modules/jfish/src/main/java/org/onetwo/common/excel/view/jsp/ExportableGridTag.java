package org.onetwo.common.excel.view.jsp;

import javax.servlet.jsp.JspException;

import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.convert.ToBooleanConvertor;
import org.onetwo.common.web.view.jsp.datagrid.DataGridTag;
import org.onetwo.common.web.view.jsp.grid.GridTagBean;

@SuppressWarnings("serial")
public class ExportableGridTag extends DataGridTag {

	private boolean exportable;
	private String exportDataSource;
	

	@Override
	public GridTagBean createComponent() {
		GridTagBean gbean = new ExportableGridTagBean();
		return gbean;
	}

	@Override
	protected void populateComponent() throws JspException {
		super.populateComponent();
		ExportableGridTagBean comp = (ExportableGridTagBean) component;
		comp.setExportable(exportable);
		comp.setExportDataSource(exportDataSource);
	}



	public void setExportable(String exportable) {
		if(StringUtils.isNotBlank(exportable) && !ToBooleanConvertor.FALSE_VALUE.equals(exportable)){
			this.exportable = true;
			this.exportDataSource = exportable;
		}else{
			this.exportable = false;
		}
	}
}
