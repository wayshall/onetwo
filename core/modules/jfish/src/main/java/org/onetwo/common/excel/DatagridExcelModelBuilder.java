package org.onetwo.common.excel;

import org.onetwo.common.web.view.jsp.datagrid.AbstractDatagridRenderListener;
import org.onetwo.common.web.view.jsp.datagrid.DataGridTag;
import org.onetwo.common.web.view.jsp.grid.GridTagBean;
import org.onetwo.common.web.view.jsp.grid.RowTagBean;

public class DatagridExcelModelBuilder extends AbstractDatagridRenderListener {

	@Override
	public void afterRender(DataGridTag tag, GridTagBean tagBean) {
		WorkbookModel workbook = new WorkbookModel();
		TemplateModel template = new TemplateModel();
		template.setLabel(tagBean.getLabel());
		template.setName(tagBean.getName());
		for(RowTagBean rowTag : tagBean.getRows()){
//			template.
		}
		workbook.addSheet(template);
	}
	

}
