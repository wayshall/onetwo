package org.onetwo.plugins.fmtagext.ui.aglybuilder;

import org.onetwo.plugins.fmtagext.ui.JFieldViewObject;
import org.onetwo.plugins.fmtagext.ui.datagrid.DataGridUI;


public interface FieldUIBuilder extends FormFIeldUIBuilder {

	public void buildListGridField(EntityGridUIBuilder gridBuilder, DataGridUI datagrid, JFieldViewObject viewObj);
	public void buildShowGridField(ShowEntityGridUIBuilder gridBuilder, DataGridUI datagrid, JFieldViewObject viewObj);

//	public void buildFormField(EntityFormUIBuilder formBuilder, FormUI form, JFieldViewObject viewObj);
	
}
