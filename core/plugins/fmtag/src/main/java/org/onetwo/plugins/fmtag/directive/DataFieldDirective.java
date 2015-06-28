package org.onetwo.plugins.fmtag.directive;

import java.io.IOException;
import java.util.Map;

import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.Page;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.web.view.ftl.DirectivesUtils;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

@SuppressWarnings("rawtypes")
public class DataFieldDirective extends FmtagBaseDirective implements TemplateDirectiveModel {

	public static final String DIRECTIVE_NAME = "field";
	
	@Override
	public void doInternalRender(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
//		DataGridAdaptor datagrid = DirectivesUtils.getDataGrid(env, true);
		DataGrid datagrid = DataDirectiveUtils.getDataGrid(env, true);

		int colspan = DirectivesUtils.getParameterByInt(params, "colspan", 1);
		String type = DirectivesUtils.getParameterByString(params, "type", "");
		String label = DirectivesUtils.getParameterByString(params, "label", "");
		String name = DirectivesUtils.getParameterByString(params, "name", "");
		String autoRender = DirectivesUtils.getParameterByString(params, "autoRender", "true");
		String render = DirectivesUtils.getParameterByString(params, "render", type);
		String canOrderby = DirectivesUtils.getParameterByString(params, "orderBy", "false");
		String format = DirectivesUtils.getParameterByString(params, "format", "");
		String value = DirectivesUtils.getParameterByString(params, "value", name);
		String canSearch = DirectivesUtils.getParameterByString(params, "search", "false");

		String orderType = DirectivesUtils.getRequest(env).getParameter("order");
		String orderBy = DirectivesUtils.getRequest(env).getParameter("orderBy");
		
		int showOrder = DirectivesUtils.getParameterByInt(params, "showOrder", 0);
		
		if(StringUtils.isBlank(orderType)){
			orderType = Page.DESC;
		}else if(Page.DESC.equals(orderType)){
			orderType = Page.ASC;
		}else{
			orderType = Page.DESC;
		}

		if(StringUtils.isBlank(name)){
			LangUtils.throwBaseException("data field properties error : " + name + ", " + label);
		}
		
		DataRow row = DataDirectiveUtils.getCurrentRow(env);
		if(row==null){
			row = datagrid.getIteratorRow();
		}
		
		DataField field = row.getField(name);
		if(field==null){
			field = new DataField(name, label);
			field.setShowOrder(row.getFields().size()+1);
			row.addField(field);
		}
		field.colspan=colspan;
		field.env = env;
		field.body = body;
		field.autoRender = "true".equals(autoRender);
		field.render = render;
		field.order = "true".equals(canOrderby);
		field.search = "true".equals(canSearch);
		field.setFormat(format);
		if(showOrder!=0){
			field.setShowOrder(showOrder);
		}
		if(field.search){
			datagrid.addSearchField(field);
		}
		field.orderType = orderType;
		field.ordering = field.getName().equals(orderBy);
		field.value = value;
		field.setType(type);
		if(StringUtils.isNotBlank(render)){
			field.autoRender = false;
			/*if(StringUtils.isBlank(field.render)){
				field.render = type;
			}*/
		}
		DirectivesUtils.setParamsToHtmlElement(params, field);
		Assert.hasText(field.getValue(), "attribute value must be has text!");
		
		/*if(row==null){
			datagrid.getIteratorRow().addField(field);
			if(!datagrid.rows.contains(row)){
				row.renderHeader = true;
				datagrid.rows.add(row);
			}
		}else{
			row.addField(field);
		}*/

		
		
		/*if(datagrid.isDebug()){
			UtilTimerStack.pop(pname);
		}*/
	}

	@Override
	public String getDirectiveName() {
		return DIRECTIVE_NAME;
	}

}
