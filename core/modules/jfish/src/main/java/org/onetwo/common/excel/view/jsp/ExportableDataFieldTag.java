package org.onetwo.common.excel.view.jsp;

import javax.servlet.jsp.JspException;

import org.onetwo.common.web.view.jsp.datagrid.DataFieldTag;
import org.onetwo.common.web.view.jsp.grid.FieldTagBean;

@SuppressWarnings("serial")
public class ExportableDataFieldTag extends DataFieldTag{

	private boolean exportable = true;
	
	@Override
	public FieldTagBean createComponent() {
		return new ExportableFieldTagBean();
	}

	protected void populateComponent() throws JspException{
		super.populateComponent();
		ExportableFieldTagBean comp = (ExportableFieldTagBean) component;
		comp.setExportable(exportable);
	}
	public void setExportable(boolean exportable) {
		this.exportable = exportable;
	}
}
