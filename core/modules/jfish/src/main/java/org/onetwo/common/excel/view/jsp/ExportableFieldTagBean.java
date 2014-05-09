package org.onetwo.common.excel.view.jsp;

import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.web.view.jsp.grid.FieldTagBean;

public class ExportableFieldTagBean extends FieldTagBean {
	
	private boolean exportable;
	private String exportValue;

	public boolean isExportable() {
		return exportable;
	}

	public void setExportable(boolean exportable) {
		this.exportable = exportable;
	}

	public String getExportValue() {
		if (StringUtils.isBlank(exportValue)) {
			exportValue = getValue();
		}
		return exportValue;
	}

	public void setExportValue(String exportValue) {
		this.exportValue = exportValue;
	}
}
