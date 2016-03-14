package org.onetwo.plugins.fmtagext.directive;

import java.io.IOException;
import java.util.Map;

import org.onetwo.common.fish.exception.JFishException;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.web.view.ftl.DirectiveRender;
import org.onetwo.common.web.view.ftl.DirectivesUtils;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

public class JFishFieldDirective extends FmtagextDirective {
	public static final String PARAMS_NAME = "name";
	public static final String PARAMS_FORMAT = "format";
	/********
	 * 例如input_text, input_checkbox,textarea
	 * ':'开头表示自定义模板
	 * 模板目录由entry的theme决定
	 */
	public static final String PARAMS_FORM_TAG = "formTag";
	public static final String PARAMS_AUTORENDER = "autoRender";
	
	public static final String PARAMS_DATA_PROVIDER = "dataProvider";
	public static final String PARAMS_DATA_KEY = "dataKey";
	public static final String PARAMS_DATA_VALUE = "dataValue";

	@Override
	public void doInternalRender(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
		String name = DirectivesUtils.getRequiredParameterByString(params, PARAMS_NAME);
		String format = DirectivesUtils.getParameterByString(params, PARAMS_FORMAT, "");
		String formTag = DirectivesUtils.getParameterByString(params, PARAMS_FORM_TAG, "");
		int showOrder = DirectivesUtils.getParameterByInt(params, "showOrder", 0);
		boolean autoRender = DirectivesUtils.getParameterByBoolean(params, PARAMS_AUTORENDER, true);
		
		JEntryDO jentryDo = (JEntryDO)DirectivesUtils.getObjectVariable(env, JFishEntryDirective.VAR_JENTRYDO);
		if(jentryDo==null)
			throw new JFishException("@jfield must has a parent directive @jentry .");
		JFieldDO jfield = jentryDo.getField(name);
		if(jfield==null){
			jfield = new JFieldDO(jentryDo, name);
			jentryDo.addField(jfield);
		}
		if(showOrder==0){
			showOrder = jentryDo.getFields().size()+1;
		}
		jfield.setShowOrder(showOrder);
		jfield.setFormat(format);
		DirectivesUtils.setParamsToHtmlElement(params, jfield);
		if(StringUtils.isNotBlank(formTag)){
			jfield.setFormTag(formTag);
		}
		if(!autoRender){
			jfield.setRender(new DirectiveRender(jfield.getName(), env, body));
		}
		
		if(jfield.isSelect()){
			TemplateModel dataProvider = DirectivesUtils.getParameter(params, PARAMS_DATA_PROVIDER, true);
			String dataKey = DirectivesUtils.getParameterByString(params, PARAMS_DATA_KEY, "key");
			String dataValue = DirectivesUtils.getParameterByString(params, PARAMS_DATA_VALUE, "value");
			jfield.setDataProvider(dataProvider);
			jfield.setDataKey(dataKey);
			jfield.setDataValue(dataValue);
		}
	}

	@Override
	public String getDirectiveName() {
		return "jfield";
	}

}
