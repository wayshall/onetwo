package org.onetwo.plugins.fmtag.directive;

import org.onetwo.common.ftl.directive.DirectivesUtils;

import freemarker.core.Environment;
import freemarker.ext.beans.BeanModel;

public final class DataDirectiveUtils {

	public static final String DG_CURRENT_ROW = "__FTL_DG_CURRENT_ROW__";
	
	private DataDirectiveUtils(){
	}

	public static String getCurrentRowName(String name){
		return DG_CURRENT_ROW + name;
	}
	
	public static void setDataGrid(Environment env, DataGrid val){
		env.setVariable(DataGridDirective.VAR_DATAGRID, DirectivesUtils.wrapAsBeanModel(val));
	}
	
	public static void setCurrentRow(Environment env, DataRow row){
		env.setVariable(getCurrentRowName(DG_CURRENT_ROW), DirectivesUtils.wrapAsBeanModel(row));
	}
	
	public static DataRow getCurrentRow(Environment env){
		BeanModel bm = (BeanModel)DirectivesUtils.getVariable(env, getCurrentRowName(DG_CURRENT_ROW), true);
		if(bm==null)
			return null;
		return (DataRow)bm.getWrappedObject();
	}
	
	public static DataGrid getDataGrid(Environment env, boolean required){
		BeanModel bm = null;
		if(required)
			bm = (BeanModel)DirectivesUtils.getRequiredVariable(env, DataGridDirective.VAR_DATAGRID, "no datagrid tag!");
		else
			bm = (BeanModel)DirectivesUtils.getVariable(env, DataGridDirective.VAR_DATAGRID, true);
		return (DataGrid)bm.getWrappedObject();
	}
	
}
