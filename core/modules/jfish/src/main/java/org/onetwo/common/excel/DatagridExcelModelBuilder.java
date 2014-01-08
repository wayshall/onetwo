package org.onetwo.common.excel;

import org.onetwo.common.web.view.jsp.datagrid.AbstractDatagridRenderListener;
import org.onetwo.common.web.view.jsp.datagrid.DataGridTag;
import org.onetwo.common.web.view.jsp.grid.GridTagBean;

public class DatagridExcelModelBuilder extends AbstractDatagridRenderListener {

	@Override
	public void afterRender(DataGridTag tag, GridTagBean tagBean) {
		WorkbookModel workbook = new WorkbookModel();
	}
	

}
