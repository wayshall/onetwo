package org.onetwo.plugins.fmtagext.directive;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import org.onetwo.common.jfishdbm.mapping.AbstractMappedField;
import org.onetwo.common.jfishdbm.mapping.JFishMappedEntry;
import org.onetwo.common.jfishdbm.mapping.JFishMappedField;
import org.onetwo.common.jfishdbm.mapping.JFishMappedFieldType;
import org.onetwo.common.spring.web.BaseController;
import org.onetwo.common.spring.web.utils.JFishWebUtils;
import org.onetwo.common.utils.ArrayUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.web.config.BaseSiteConfig;
import org.onetwo.common.web.view.ftl.DirectivesUtils;
import org.onetwo.plugins.fmtag.JFieldShowable;
import org.onetwo.plugins.fmtagext.service.JFishDataDelegateService;

import freemarker.core.Environment;
import freemarker.ext.beans.BeanModel;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

public class JFishEntryDirective extends FmtagextDirective {

	public static final String PARAMS_NAME = "name";
	/*****
	 * 默认为插件的lib目录
	 */
//	public static final String PARAMS_THEME = "theme";
	public static final String PARAMS_FORM = "form";
	public static final String PARAMS_METHOD = "method";
	public static final String PARAMS_ACTION = "action";
	public static final String PARAMS_EXCLUDEFIELDS = "excludeFields";
	public static final String PARAMS_TEMPLATE = "template";
	public static final String PARAMS_TYPE = "type";
	public static final String PARAMS_FORM_BUTTONS = "formButtons";

	public static final String VAR_JENTRYDO = "__jentryDO__";
	public static final String VAR_ENTRY_DATA = "__entryData__";
	public static final String DEFAULT_TEMPLATE = "lib/default/jentry.ftl";
//	public static final String DEFAULT_THEME = "lib";
	
//	private SimpleCacheWrapper jentryDOCache;
	
	public JFishEntryDirective(){
	}

	@Override
	public void doInternalRender(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
		String template = DirectivesUtils.getParameterByString(params, PARAMS_TEMPLATE, getPluginTemplatePath(DEFAULT_TEMPLATE));
		String paramsName = DirectivesUtils.getRequiredParameterByString(params, PARAMS_NAME);

//		String theme = DirectivesUtils.getParameterByString(params, PARAMS_THEME, getPluginTemplatePath(DEFAULT_THEME));
		String action = DirectivesUtils.getParameterByString(params, PARAMS_ACTION, "");
		String method = DirectivesUtils.getParameterByString(params, PARAMS_METHOD, "");
		String formButtons = DirectivesUtils.getParameterByString(params, PARAMS_FORM_BUTTONS, ":submit|:back");
		String typeStr = DirectivesUtils.getRequiredParameterByString(params, PARAMS_TYPE);
		boolean form = DirectivesUtils.getParameterByBoolean(params, PARAMS_FORM, true);
		
		
		Object entity = null;
		
		entity = env.getVariable(paramsName);
		JFishMappedEntry entry = null;
		if(BeanModel.class.isInstance(entity)){
			entity = ((BeanModel)entity).getWrappedObject();
			JFishDataDelegateService jdataDelegate = getHighestBean(JFishDataDelegateService.class);
			if(entity!=null && entity.getClass()!=Object.class){
				entry = jdataDelegate.getJFishMappedEntry(entity.getClass());
			}
//			paramsName = StringUtils.getClassShortName(entity);
		}else{
//			throw new IllegalArgumentException("jentry error name: " + paramsName);
		}

		

		JEntryDO entryDo = null;
		EntryData entryData = null;
		
		JFieldShowable jtype = null;
		try {
			jtype = JFieldShowable.valueOf(typeStr);
		} catch (Exception e) {
			jtype = JFieldShowable.show;
		}
		
		if(entry!=null){
			entryDo = JEntryDO.create(entry);
			entryDo.setType(jtype);
			Object id = entry.getId(entity);
			entryData = new EntryData();
			entryData.setIdName(entry.getIdentifyField().getName());
			entryData.setIdValue(id);
			entryData.setEntity(entity);
			
			if(StringUtils.isBlank(action)){
				BaseController<?> controller = JFishWebUtils.currentTypeController(BaseController.class);
				if(controller!=null){
					if(entryDo.isCreate()){
						action = controller.getUrlMeta().getCreateAction();
						method = "post";
					}else if(entryDo.isUpdate()){
						action = controller.getUrlMeta().getUpdateAction(id);
						method = "put";
					}else{
					}
				}
			}
			DirectivesUtils.setObjectVariable(env, VAR_ENTRY_DATA, entryData);
		}else{
			entryDo = new JEntryDO();
			entryDo.setType(jtype);
		}
		entryDo.setForm(form);
		
		action = StringUtils.appendStartWith(action, "/");

		entryDo.setFormAction(BaseSiteConfig.getInstance().getBaseURL()+action);
		entryDo.setFormMethod(method);
//		entryDo.setTheme(theme);
//		entryDo.setEntity(entity);
		entryDo.setFormButtons(formButtons);
		
		if(entry!=null){
			String excludeFieldsStr = DirectivesUtils.getParameterByString(params, PARAMS_EXCLUDEFIELDS, "");
			String[] excludeFields = StringUtils.split(excludeFieldsStr, ",");
			
			Collection<AbstractMappedField> fields = entry.getFields(JFishMappedFieldType.FIELD);
			JFieldDO jfieldDO = null;
			for(JFishMappedField field : fields){
				if(ArrayUtils.contains(excludeFields, field.getName()))
					continue;
				jfieldDO = JFieldDO.create(entryDo, field);
				if(!jfieldDO.isShowable())
					continue;
				if(jfieldDO.getShowOrder()==0)
					jfieldDO.setShowOrder(entryDo.getFields().size()+1);
				entryDo.addField(jfieldDO);
			}
		}
		entryDo.setName(paramsName);
		DirectivesUtils.setObjectVariable(env, VAR_JENTRYDO, entryDo);
		
		DirectivesUtils.setParamsToHtmlElement(params, entryDo, PARAMS_NAME);
		String bodyContetnt = this.getBodyContent(body);
		entryDo.setBodyContent(bodyContetnt);
		
		entryDo.sortFields();
		env.include(template, DirectivesUtils.ENCODING, true);
	}

	@Override
	public String getDirectiveName() {
		return "jentry";
	}

}
