package org.onetwo.common.ftl.directive;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.onetwo.common.ftl.directive.OverrideDirective.OverrideBodyWraper;
import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.spring.ftl.JFishFreeMarkerConfigurer;
import org.onetwo.common.utils.ArrayUtils;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.Page;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.web.view.HtmlElement;
import org.slf4j.Logger;
import org.springframework.web.servlet.support.RequestContext;

import freemarker.core.Environment;
import freemarker.ext.beans.ArrayModel;
import freemarker.ext.beans.BeanModel;
import freemarker.ext.beans.BooleanModel;
import freemarker.ext.beans.CollectionModel;
import freemarker.ext.beans.SimpleMapModel;
import freemarker.ext.beans.StringModel;
import freemarker.ext.servlet.HttpRequestHashModel;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateNumberModel;

@SuppressWarnings({"unchecked", "rawtypes"})
public class DirectivesUtils {

	public static final String ENCODING = "UTF-8";
	public static final String REQUEST_CONTEX = "request";
	public static final String REQUEST = "Request";

	public static final String OVERRIDE_BODY = "__FTL_EXTENDS_OVERRIDE_BODY__";
//	public static final String ALREADY_RENDER_BODY = "__FTL_EXTENDS_ALREADY_RENDER_BODY__";
//	public static final String CUURENT_OVERRIDE_BODY = "__FTL_EXTENDS_CUURENT_OVERRIDE_BODY__";

	private static final Logger logger = MyLoggerFactory.getLogger(DirectivesUtils.class);

	private DirectivesUtils(){
	}
	
	public static Page<?> toPage(Object dsValue){
		Page page = null;
		if(dsValue==null){
			page = Page.create();
		}else if (dsValue instanceof Page) {
			page = (Page) dsValue;
		}else {
			List list = null;
			if(SimpleMapModel.class.isInstance(dsValue)){
				Map dataMap = (Map)((SimpleMapModel) dsValue).getWrappedObject();
				list = new ArrayList();
				for(Entry entry : (Set<Entry>)dataMap.entrySet()){
//					list.add(KVEntry.create(entry));
					list.add(DirectivesUtils.wrapAsBeanModel(entry));
				}
			}else{
				list = LangUtils.asList(dsValue);
			}
			if (list == null)
				list = Collections.EMPTY_LIST;
			page = new Page();
			page.setResult(list);
			page.setTotalCount(list.size());
			page.setPageSize(list.size());
		}
		return page;
	}
	
	public static void render(String tag, Environment env, TemplateDirectiveBody body){
		if(body==null || env==null)
			return ;
		try {
			body.render(env.getOut());
		} catch (Exception e) {
			LangUtils.throwBaseException("render tempalte["+tag+"] error : "+e.getMessage(), e);
		} 
	}
	
	public static TemplateModel getVariable(Environment env, String name, boolean throwIfError){
		TemplateModel model = null;
		try {
			model = env.getVariable(name);
		} catch (Exception e) {
			if(throwIfError)
				LangUtils.throwBaseException("get variable["+name+"] error: " + e.getMessage(), e);
		}
		return model;
	}
	

	public static TemplateModel getRequiredVariable(Environment env, String name){
		return getRequiredVariable(env, name, null);
	}
	
	public static RequestContext getRequestContext(Environment env){
		StringModel val = (StringModel) getVariable(env, REQUEST_CONTEX, true);
		return (RequestContext)val.getWrappedObject();
	}
	
	public static HttpServletRequest getRequest(Environment env){
		HttpRequestHashModel req = (HttpRequestHashModel) getVariable(env, REQUEST, true);
		return req.getRequest();
	}
	
	public static void setParamsTo(Map params, Object obj){
		Assert.notNull(obj);
		PropertyDescriptor[] pds = ReflectUtils.desribProperties(obj.getClass());
		for(PropertyDescriptor pd : pds){
			Object val = params.get(pd.getName());
			if(val!=null){
				ReflectUtils.invokeMethod(pd.getWriteMethod(), obj);
			}
		}
	}
	
	public static TemplateModel getRequiredVariable(Environment env, String name, String msg){
		TemplateModel model = getVariable(env, name, true);
		if(model==null){
			if(StringUtils.isBlank(msg)){
				msg = "the variable["+name+"] can not be null!";
				LangUtils.throwBaseException(msg);
			}
		}
		return model;
	}
	
	public static String getOverrideBodyName(String name){
		Assert.hasText(name);
		return OVERRIDE_BODY + name;
	}
	
	public static OverrideBodyWraper getOverrideBody(Environment env, String name){
		return (OverrideBodyWraper)getVariable(env, getOverrideBodyName(name), false);
	}
	
	public static void setOverrideBody(Environment env, String name, OverrideBodyWraper body){
		env.setVariable(getOverrideBodyName(name), body);
	}
	
	public static void setVariable(Environment env, String name, Object val){
		if(val==null)
			return ;
		TemplateModel model = null;
		if(TemplateModel.class.isInstance(val)){
			model = (TemplateModel) val;
		}else if(Map.class.isInstance(val)){
			model = new SimpleMapModel((Map)val, JFishFreeMarkerConfigurer.INSTANCE);
		}else if(Collection.class.isInstance(val)){
			model = new CollectionModel((Collection)val, JFishFreeMarkerConfigurer.INSTANCE);
		}else if(LangUtils.isArray(val)){
			model = new ArrayModel(val, JFishFreeMarkerConfigurer.INSTANCE);
		}else if(String.class.isInstance(val)){
			model = new StringModel(val, JFishFreeMarkerConfigurer.INSTANCE);
		}else{
			model = wrapAsBeanModel(val);
		}
		env.setVariable(name, model);
	}
	
	public static BeanModel wrapAsBeanModel(Object obj){
		if(obj==null)
			return null;
		BeanModel m = null;
		try {
//			BeansWrapper bw = (BeansWrapper)ObjectWrapper.BEANS_WRAPPER;
			m = new BeanModel(obj, JFishFreeMarkerConfigurer.INSTANCE);
		} catch (Exception e) {
			LangUtils.throwBaseException("wrap bean error : " + obj.getClass(), e);
		}
		
		return m;
	}


	public static void setVariable(Environment env, String name, TemplateModel val){
		env.setVariable(name, val);
	}
	
	public static void setObjectVariable(Environment env, String name, Object val){
		if(TemplateModel.class.isInstance(val)){
			env.setVariable(name, (TemplateModel)val);
		}else if(String.class.isInstance(val)){
			env.setVariable(name, new SimpleScalar(val.toString()));
		}else{
			env.setVariable(name, wrapAsBeanModel(val));
		}
	}
	
	public static Object getObjectVariable(Environment env, String name){
		Object val = null;
		try {
			BeanModel model = (BeanModel)env.getVariable(name);
			val = model.getWrappedObject();
		} catch (TemplateModelException e) {
			return null;
		} catch(Exception e){
			logger.error("getObjectVariable error : " + e.getMessage(), e);
		}
		return val;
	}

	public static String getRequiredParameterByString(Map params, String name){
		TemplateModel val = getParameter(params, name, true);
		return val.toString();
	}
	public static String getParameterByString(Map params, String name, String def){
		TemplateModel attr = getParameter(params, name, false);
		if(attr!=null)
			return attr.toString();
		return def;
	}
	
	public static boolean getParameterByBoolean(Map params, String name, boolean def){
		TemplateModel attr = getParameter(params, name, false);
		if(attr!=null){
			try {
				if(BooleanModel.class.isInstance(attr)){
					return ((BooleanModel)attr).getAsBoolean();
				}else{
					return Boolean.valueOf(attr.toString());
				}
			} catch (Exception e) {
				return def;
			}
		}
		return def;
	}
	public static int getParameterByInt(Map params, String name, int def){
		TemplateModel attr = getParameter(params, name, false);
		if(attr!=null){
			try {
				if(TemplateNumberModel.class.isInstance(attr)){
					return ((TemplateNumberModel)attr).getAsNumber().intValue();
				}else{
					return Integer.parseInt(attr.toString());
				}
			} catch (Exception e) {
				return def;
			}
		}
		return def;
	}

	public static TemplateModel getRequiredParameter(Map params, String name){
		return getParameter(params, name, true);
	}
	
	public static String getParameter(Map params, String name, String defVal){
		TemplateModel val = getParameter(params, name, false);
		if(val==null)
			return defVal;
		return val.toString();
	}
	
	public static <T> T getParameter(Map params, String name, boolean throwIfNotExist){
		if(!params.containsKey(name)){
			if(throwIfNotExist)
				throw LangUtils.asBaseException("freemarker template error : the param["+name+"] has not be given.");
			else
				return null;
		}
		
		T val = (T)params.get(name);
		return val;
		/*if(val!=null)
			return val;
		else
			throw LangUtils.asBaseException("the param["+name+"] can not be null.");*/
	}
	

	public static void setParamsToHtmlElement(Map params, HtmlElement html, String...excludeFields){
		String prop = "";
		String def = null;
		
		if(!ArrayUtils.contains(excludeFields, "name")){
			prop = DirectivesUtils.getParameterByString(params, "name", def);
			if(StringUtils.isNotBlank(prop))
				html.setName(prop);
		}

		if(!ArrayUtils.contains(excludeFields, "id")){
			prop = DirectivesUtils.getParameterByString(params, "id", def);
			if(StringUtils.isNotBlank(prop))
				html.setId(prop);
		}

		if(!ArrayUtils.contains(excludeFields, "title")){
			prop = DirectivesUtils.getParameterByString(params, "title", def);
			if(StringUtils.isNotBlank(prop))
				html.setTitle(prop);
		}

		if(!ArrayUtils.contains(excludeFields, "cssStyle")){
			prop = DirectivesUtils.getParameterByString(params, "cssStyle", def);
			if(StringUtils.isNotBlank(prop))
				html.setCssStyle(prop);
		}

		if(!ArrayUtils.contains(excludeFields, "cssClass")){
			prop = DirectivesUtils.getParameterByString(params, "cssClass", def);
			if(StringUtils.isNotBlank(prop))
				html.setCssClass(prop);
		}

		if(!ArrayUtils.contains(excludeFields, "onclick")){
			prop = DirectivesUtils.getParameterByString(params, "onclick", def);
			if(StringUtils.isNotBlank(prop))
				html.setOnclick(prop);
		}
		
		if(!ArrayUtils.contains(excludeFields, "label")){
			prop = DirectivesUtils.getParameterByString(params, "label", def);
			if(StringUtils.isNotBlank(prop))
				html.setLabel(prop);
		}

		if(!ArrayUtils.contains(excludeFields, "attributes")){
			prop = DirectivesUtils.getParameterByString(params, "attributes", def);
			if(StringUtils.isNotBlank(prop))
				html.setAttributes(prop);
		}
	}
	
}
