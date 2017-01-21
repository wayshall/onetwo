package org.onetwo.common.web.view.ftl;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.profiling.UtilTimerStack;
import org.onetwo.common.spring.Springs;
import org.onetwo.common.utils.LangUtils;
import org.slf4j.Logger;

import freemarker.core.Environment;
import freemarker.ext.beans.BeanModel;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

@SuppressWarnings("rawtypes")
abstract public class AbstractDirective implements TemplateDirectiveModel {

	public static final String PARAMS_PROFILE = "profile";
	
	protected final Logger logger = MyLoggerFactory.getLogger(this.getClass());
	
	abstract public String getDirectiveName();
	
	protected <T> T getBean(Class<T> beanClass){
		return (T)Springs.getInstance().getBean(beanClass);
	}
	
	protected <T> T getHighestBean(Class<T> beanClass){
		return (T)Springs.getInstance().getSpringHighestOrder(beanClass);
	}
	
	protected String getRequiredParameterByString(Map params, String name){
		return DirectivesUtils.getRequiredParameterByString(params, name);
	}

	protected <T> T getParameter(Map params, String name, boolean throwIfNotExist){
		return DirectivesUtils.getParameter(params, name, throwIfNotExist);
	}
	

	protected <T> T getVariable(String varName, Environment env) throws TemplateModelException{
		Object value = env.getVariable(varName);
		if (value instanceof BeanModel) {
			value = ((BeanModel) value).getWrappedObject();
		}
		return (T)value;
	}

	protected void setObjectVariable(Environment env, String name, Object val){
		DirectivesUtils.setObjectVariable(env, name, val);
	}
	
	protected String getBodyContent(TemplateDirectiveBody body){
		StringWriter sw = null;
		String bodyContent = "";
		try {
//			body.render(env.getOut());
			if(body!=null){
				sw = new StringWriter();
				body.render(sw);
				bodyContent = sw.toString();
			}
		} catch (Exception e) {
			LangUtils.throwBaseException("render error : " + e.getMessage(), e);
//			e.printStackTrace();
		} finally{
			LangUtils.closeIO(sw);
		}
		return bodyContent;
	}
	
	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
		if(params.containsKey(PARAMS_PROFILE)){
			boolean profile = DirectivesUtils.getParameterByBoolean(params, PARAMS_PROFILE, false);
			UtilTimerStack.active(profile);
		}
		String name = DirectivesUtils.getParameterByString(params, "name", getDirectiveName());
		UtilTimerStack.push(name);
		
		this.render(env, params, loopVars, body);
		
		UtilTimerStack.pop(name);
	}

	abstract public void render(Environment env, Map<?, ?> params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException ;
}
