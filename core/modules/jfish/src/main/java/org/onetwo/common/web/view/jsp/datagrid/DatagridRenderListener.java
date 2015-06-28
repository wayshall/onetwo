package org.onetwo.common.web.view.jsp.datagrid;

import org.onetwo.common.web.view.jsp.grid.GridTagBean;

public interface DatagridRenderListener {
	
//	public GridTagBean createGridTagBean(DataGridTag tag);
	public void prepareRender(DataGridTag tag, GridTagBean tagBean);

}
