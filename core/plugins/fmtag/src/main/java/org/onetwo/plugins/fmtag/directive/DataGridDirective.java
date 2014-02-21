package org.onetwo.plugins.fmtag.directive;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.onetwo.common.ftl.directive.DirectivesUtils;
import org.onetwo.common.profiling.UtilTimerStack;
import org.onetwo.common.utils.Page;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.map.CasualMap;
import org.onetwo.common.web.utils.RequestUtils;
import org.onetwo.plugins.fmtag.FmtagWebPlugin;
import org.springframework.web.servlet.support.RequestContext;

import freemarker.core.Environment;
import freemarker.ext.beans.BeanModel;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

@SuppressWarnings("rawtypes")
public class DataGridDirective extends FmtagBaseDirective implements TemplateDirectiveModel {
	
	public static final String DIRECTIVE_NAME = "grid";
	public static final String VAR_DATAGRID = "__dg__";

	public static final String PARAMS_NAME = "name";
	public static final String PARAMS_METHOD = "method";
	public static final String PARAMS_DATASOURCE = "datasource";
	public static final String PARAMS_ACTION = "action";
	public static final String PARAMS_CSS_CLASS = "cssClass";
	public static final String PARAMS_CSS_STYLE = "cssStyle";
	public static final String PARAMS_TEMPLATE = "template";
	public static final String PARAMS_TOOLBAR= "toolbar";
	public static final String PARAMS_PAGINATION = "pagination";
	/*********
	 * 默认常见的lib/dg目录
	 */
	public static final String PARAMS_THEME = "theme";

	public static final String DEFAULT_TEMPLATE = "lib/dg_bstrp/datagrid.ftl";
	public static final String DEFAULT_PAGINATION = "number";

	@Override
	public void doInternalRender(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
		boolean debug = DirectivesUtils.getParameterByString(params, "debug", "false").equals("true");
		if (debug) {
			UtilTimerStack.setActive(true);
			UtilTimerStack.push(DIRECTIVE_NAME);
		}
		
		DataGrid dg = this.buildDataGrid(debug, env, params);
		DataDirectiveUtils.setDataGrid(env, dg);

		this.doBeforeRenderInnerBody(dg, env, params);
		dg.bodyContent = getBodyContent(body);
		this.doAfterRenderInnerBody(dg, env, params);

		dg.sortFieldsByShowOrder();
		String template = DirectivesUtils.getParameterByString(params, PARAMS_TEMPLATE, FmtagWebPlugin.getTemplatePath(DEFAULT_TEMPLATE));
		this.renderTempalte(env, params, dg, template);
	}
	
	protected DataGrid buildDataGrid(boolean debug, Environment env, Map params) throws TemplateModelException{
		String name = DirectivesUtils.getRequiredParameterByString(params, PARAMS_NAME);
		RequestContext request = DirectivesUtils.getRequestContext(env);
		String datasource = DirectivesUtils.getParameterByString(params, PARAMS_DATASOURCE, name);
		String pagination = DirectivesUtils.getParameterByString(params, PARAMS_PAGINATION, "");
		String method = DirectivesUtils.getParameterByString(params, PARAMS_METHOD, "delete");
		String toolbar = DirectivesUtils.getParameterByString(params, PARAMS_TOOLBAR, "");
		String cssClass = DirectivesUtils.getParameterByString(params, PARAMS_CSS_CLASS, "table");
		String cssStyle = DirectivesUtils.getParameterByString(params, PARAMS_CSS_STYLE, "");
		
		String themeDir = DirectivesUtils.getParameterByString(params, PARAMS_THEME, getSelfConfig().getTemplatePath("lib/dg"));
		String action = getActionString(env, params);
		
		DataGrid dg = new DataGrid();
		dg.debug = debug;
		dg.dataSource = datasource;
		dg.formMethod = method;
		dg.action = action;
		dg.toolbar = toolbar;
		dg.requestUri = request.getRequestUri();
		dg.themeDir = themeDir;
		dg.setCssClass(cssClass);
		dg.setCssStyle(cssStyle);
		dg.setPagination(pagination);
		DirectivesUtils.setParamsToHtmlElement(params, dg);

		dg.page = getDataFromDatasource(datasource, env, params);
		
		return dg;
	}
	
	protected Page getDataFromDatasource(String datasource, Environment env, Map params) throws TemplateModelException{
		Object dsValue = env.getVariable(datasource);
		if (dsValue instanceof BeanModel) {
			dsValue = ((BeanModel) dsValue).getWrappedObject();
		}
		Page page = DirectivesUtils.toPage(dsValue);
		
		return page;
	}
	
	protected void doBeforeRenderInnerBody(DataGrid dg, Environment env, Map params){
	}
	
	protected void doAfterRenderInnerBody(DataGrid dg, Environment env, Map params){
	}
	
	protected void renderTempalte(Environment env, Map params, DataGrid dg, String template) throws IOException, TemplateException {
		String pname = "render template : " + template;
		if (dg.debug) {
			UtilTimerStack.push(pname);
		}
		env.include(template, DirectivesUtils.ENCODING, true);

		if (dg.debug) {
			UtilTimerStack.pop(pname);
			UtilTimerStack.pop(DIRECTIVE_NAME);
		}
	}

	protected String getActionString(Environment env, Map params) {
		String action = DirectivesUtils.getParameterByString(params, PARAMS_ACTION, "");
		RequestContext request = DirectivesUtils.getRequestContext(env);
		if (StringUtils.isBlank(action))
			return request.getRequestUri();
		if(!action.startsWith(":")){
			return action;
		}
		String surl = request.getRequestUri();
		String[] symbols = StringUtils.split(action, "|");
		int index = 0;
		for (String symbol : symbols) {
			if (StringUtils.isBlank(symbol))
				continue;
			String qstr = this.processUrlSymbol(symbol, env);
			if (StringUtils.isNotBlank(qstr)) {
				if (index == 0)
					surl += "?";
				else
					surl += "&";
				surl += qstr;
				index++;
			}
		}

		return surl;
	}

	protected String processUrlSymbol(String symbol, Environment env) {
		HttpServletRequest request = DirectivesUtils.getRequest(env);
		String str = null;
		if (symbol.equals(":qstr")) {
			str = request.getQueryString();
			if(StringUtils.isBlank(str))
				return "";
			CasualMap params = new CasualMap(str);
			params.filter("pageNo", "order", "orderBy");
			str = params.toParamString();
		} else if (symbol.equals(":post2get")) {
			str = RequestUtils.getPostParametersWithout(request, "pageNo", "order", "orderBy").toParamString();
		}
		return str;
	}

	@Override
	public String getDirectiveName() {
		return DIRECTIVE_NAME;
	}

}
