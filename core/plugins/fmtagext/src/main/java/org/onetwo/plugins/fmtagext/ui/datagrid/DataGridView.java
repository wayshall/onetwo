package org.onetwo.plugins.fmtagext.ui.datagrid;

import org.onetwo.common.utils.Page;
import org.onetwo.plugins.fmtagext.ui.AbstractDataComponent;

public class DataGridView extends AbstractDataComponent {

	private Page<?> data;
	
	public DataGridView(DataGridUI dataGrid, Page<?> page) {
		super(dataGrid);
		this.data = page;
	}

	@Override
	public Page<?> getData() {
		return data;
	}

}
