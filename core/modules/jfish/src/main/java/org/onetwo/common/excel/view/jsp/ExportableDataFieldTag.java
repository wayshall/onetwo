package org.onetwo.common.excel.view.jsp;

import javax.servlet.jsp.JspException;

import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.convert.ToBooleanConvertor;
import org.onetwo.common.web.view.jsp.datagrid.DataFieldTag;
import org.onetwo.common.web.view.jsp.grid.FieldTagBean;

@SuppressWarnings("serial")
public class ExportableDataFieldTag extends DataFieldTag{

	private boolean exportable = true;
	private String exportValue;
	
	@Override
	public FieldTagBean createComponent() {
		return new ExportableFieldTagBean();
	}

	protected void populateComponent() throws JspException{
		super.populateComponent();
		ExportableFieldTagBean comp = (ExportableFieldTagBean) component;
		comp.setExportable(exportable);
		comp.setExportValue(exportValue);
	}
	public void setExportable(boolean exportable) {
		this.exportable = exportable;
	}

	public void setExportable(String exportable) {
		if(StringUtils.isNotBlank(exportable) && !ToBooleanConvertor.FALSE_VALUE.equals(exportable)){
			this.exportable = true;
			this.exportValue = ToBooleanConvertor.TRUE_VALUE.equals(exportable)?"":exportable;
		}else{
			this.exportable = false;
		}
	}
	
}
