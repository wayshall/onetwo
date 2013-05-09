package org.onetwo.plugins.fmtagext.ui;

import org.onetwo.plugins.fmtagext.ui.aglybuilder.EntityGridUIBuilder;
import org.onetwo.plugins.fmtagext.ui.aglybuilder.EntityUIBuilder;
import org.onetwo.plugins.fmtagext.ui.datagrid.DataGridColumnUI;
import org.onetwo.plugins.fmtagext.ui.datagrid.DataGridUI;


public interface DataGridUIBuilder extends EntityUIBuilder<DataGridUI> {
	
	public EntityGridUIBuilder buildCheckbox();
	
	public EntityGridUIBuilder buildColumn(Object headerColumn, UIComponent... uiComponents);
	public EntityGridUIBuilder buildDataGridColumn(Object headerColumn, DataGridColumnUI operation);

	public EntityGridUIBuilder buildForm(String method, String action);
	public EntityGridUIBuilder buildToolbarMenus(UIComponent... toolbarMenus);

	public EntityGridUIBuilder buildHeaderCssStyle(String... headerCssStyle);

	public EntityGridUIBuilder buildHeaderCssClass(String... headerCssClass);

//	public EntityGridUIBuilder buildEnityFields(String... excludeFields);
	
}
