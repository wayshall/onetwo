package org.onetwo.plugins.fmtagext.ui.datagrid;

import org.onetwo.plugins.fmtagext.ui.UI.TemplateKeys;

public class DataGridHeaderRowUI extends DataGridRowUI {

	
	public DataGridHeaderRowUI(DataGridUI parent) {
		super(parent);
	}

	@Override
	public String getTemplate() {
		return TemplateKeys.UI_GRID_HEADER_ROW;
	}


}
