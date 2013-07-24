package org.onetwo.plugins.fmtagext.ui;

import org.onetwo.plugins.fmtagext.ui.aglybuilder.EntityFormUIBuilder;
import org.onetwo.plugins.fmtagext.ui.aglybuilder.EntityGridUIBuilder;
import org.onetwo.plugins.fmtagext.ui.aglybuilder.FieldUIBuilder;
import org.onetwo.plugins.fmtagext.ui.aglybuilder.ShowEntityGridUIBuilder;
import org.onetwo.plugins.fmtagext.ui.datagrid.DataGridColumnUI;
import org.onetwo.plugins.fmtagext.ui.datagrid.DataGridUI;
import org.onetwo.plugins.fmtagext.ui.form.FormUI;

public class BaseFieldUIBuilder implements FieldUIBuilder {

	@Override
	public void buildListGridField(EntityGridUIBuilder gridBuilder, DataGridUI datagrid, JFieldViewObject viewObj) {
//		gridBuilder.buildColumn(viewObj.getLabel(), viewObj.getShowValue(), viewObj.getOrder());
		if(viewObj==null)
			return ;
		DataGridColumnUI dgc = new DataGridColumnUI(viewObj.getLabel(), viewObj.getShowValue());
		dgc.setValueFormat(viewObj.getFormat());
		gridBuilder.buildDataGridColumn(viewObj.getLabel(), dgc);
	}

	@Override
	public void buildShowGridField(ShowEntityGridUIBuilder gridBuilder, DataGridUI datagrid, JFieldViewObject viewObj) {
		if(viewObj==null)
			return ;
		DataGridColumnUI label = new DataGridColumnUI(viewObj.getName(), viewObj.getLabel());
		label.setValueFormat(viewObj.getFormat());
		
		DataGridColumnUI value = new DataGridColumnUI(viewObj.getName(), viewObj.getShowValue());
		value.setValueFormat(viewObj.getFormat());
		
		gridBuilder.buildRow(label, value);
	}

	@Override
	public void buildFormField(EntityFormUIBuilder formBuilder, FormUI form, JFieldViewObject viewObj) {
		UITypeMapper.getInstance().getFormFIeldUIBuilder(viewObj.getFormui()).buildFormField(formBuilder, form, viewObj);
	}
	
}
