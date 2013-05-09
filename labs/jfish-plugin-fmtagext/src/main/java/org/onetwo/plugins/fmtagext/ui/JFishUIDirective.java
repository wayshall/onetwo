package org.onetwo.plugins.fmtagext.ui;

import java.io.IOException;
import java.util.Map;

import org.onetwo.common.fish.exception.JFishException;
import org.onetwo.common.ftl.directive.AbstractDirective;

import freemarker.core.Environment;
import freemarker.ext.beans.BeanModel;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

public class JFishUIDirective extends AbstractDirective {
//	public static final String ENCODING = UIRender.ENCODING;
//	public static final String THIS_COMPONENT = UIRender.THIS_COMPONENT;
	public static final String BODY_CONTENT = "__bodyContent__";
	
//	private Map<String, Class<?>> uiComponents = LangUtils.newHashMap();

	@Override
	public String getDirectiveName() {
		return "jfishui";
	}

	@Override
	public void render(Environment env, Map<?, ?> params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
//		String name = this.getRequiredParameterByString(params, "name");
		Object componentObject = getParameter(params, "component", true);
		
		if(SimpleScalar.class.isInstance(componentObject)){
			componentObject = env.getVariable(componentObject.toString());
		}
		BeanModel beanModel = (BeanModel) componentObject;
		Object component = beanModel.getWrappedObject();
		
		String bodyContent = getBodyContent(body);
		setObjectVariable(env, BODY_CONTENT, bodyContent);

		Object dataObject = getParameter(params, "data", false);
		if(ShowableUI.class.isInstance(component)){
			if(!((ShowableUI)component).isUIShowable(dataObject)){
				return ;
			}
		}
		this.renderUIComponent(env, dataObject, body, component);

	}
	
	protected void renderUIComponent(Environment env, Object dataObject, TemplateDirectiveBody body, Object component) throws IOException, TemplateException{
		if(RendableUI.class.isInstance(component)){
			UIRender render = new UIRender(env, body, dataObject);
			Object oldThis = render.getThis();
			render.setThis(component);
			((RendableUI)component).onRender(render);
			render.setThis(oldThis);
			
		}else if(UIComponent.class.isInstance(component)){
			UIRender render = new UIRender(env, body, dataObject);
			render.renderUIComponent((UIComponent)component);
			
		}else{
			throw new JFishException("unsuppported component class : " + component);
		}
		
	}
	
/*
	protected Page<?> getPageData(String varName, Environment env) throws TemplateModelException{
		Object dsValue = getVariable(varName, env);
		Page<?> page = DirectivesUtils.toPage(dsValue);
		
		return page;
	}*/

}
