package org.onetwo.plugins.fmtag.directive;

import java.io.IOException;
import java.util.Map;

import org.onetwo.common.profiling.UtilTimerStack;
import org.onetwo.common.web.view.ftl.DirectivesUtils;
import org.onetwo.plugins.fmtag.directive.DataRow.RowType;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

@SuppressWarnings("rawtypes")
public class DataRowDirective extends FmtagBaseDirective implements TemplateDirectiveModel {

	public static final String DIRECTIVE_NAME = "row";
	public static final String PARAMS_TYPE = "type";
	
	@Override
	public void doInternalRender(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
		DataGrid datagrid = DataDirectiveUtils.getDataGrid(env, true);
		if(datagrid.isDebug()){
			UtilTimerStack.push(DIRECTIVE_NAME);
		}
		String typeStr = DirectivesUtils.getParameter(params, PARAMS_TYPE, RowType.row.toString());
		RowType type = RowType.valueOf(typeStr);
		
		DataRow row = new DataRow(type);
		DirectivesUtils.setParamsToHtmlElement(params, row);
		
		row.type = type;
		datagrid.addRow(row);
		DataDirectiveUtils.setCurrentRow(env, row);
		body.render(env.getOut());
		if(datagrid.isDebug()){
			UtilTimerStack.pop(DIRECTIVE_NAME);
		}
	}

	@Override
	public String getDirectiveName() {
		return DIRECTIVE_NAME;
	}

}
