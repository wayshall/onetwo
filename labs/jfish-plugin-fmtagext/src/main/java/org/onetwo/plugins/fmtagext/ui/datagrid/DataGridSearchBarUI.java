package org.onetwo.plugins.fmtagext.ui.datagrid;

import org.onetwo.plugins.fmtagext.ui.FmUIComponent;

public class DataGridSearchBarUI extends FmUIComponent {
	
	public DataGridSearchBarUI(DataGridUI datagrid) {
		super(datagrid, "ui-grid-search");
	}

	public DataGridUI getDatagrid() {
		return (DataGridUI)getParent();
	}

}
