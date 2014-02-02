package org.onetwo.common.excel.view.jsp;

import org.onetwo.common.web.view.jsp.grid.FieldTagBean;

public class ExportableFieldTagBean extends FieldTagBean {
	
	private boolean exportable;

	public boolean isExportable() {
		return exportable;
	}

	public void setExportable(boolean exportable) {
		this.exportable = exportable;
	}
}
