package org.onetwo.common.spring.ftl;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.LangUtils;
import org.slf4j.Logger;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.ui.freemarker.SpringTemplateLoader;

import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.core.Environment;
import freemarker.ext.beans.ArrayModel;
import freemarker.ext.beans.BeanModel;
import freemarker.ext.beans.BeansWrapper;
import freemarker.ext.beans.CollectionModel;
import freemarker.ext.beans.DateModel;
import freemarker.ext.beans.SimpleMapModel;
import freemarker.ext.beans.StringModel;
import freemarker.template.Configuration;
import freemarker.template.SimpleNumber;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateBooleanModel;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateNumberModel;

final public class FtlUtils {

	private static final Logger logger = JFishLoggerFactory.getLogger(FtlUtils.class);
	public static final BeansWrapper BEAN_WRAPPER = new BeansWrapper(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
	
	public static final String CONFIG_CLASSIC_COMPATIBLE = "classic_compatible";

	static {
		BEAN_WRAPPER.setSimpleMapWrapper(true);
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
	
	public static TemplateModel getRequiredVariable(Environment env, String name, String msg){
		TemplateModel model = getVariable(env, name, true);
		if(model==null){
			if(StringUtils.isBlank(msg)){
				throw new BaseException("the variable["+name+"] can not be null!");
			}
		}
		return model;
	}
	public static TemplateModel getVariable(Environment env, String name, boolean throwIfError){
		TemplateModel model = null;
		try {
			model = env.getVariable(name);
		} catch (Exception e) {
			if(throwIfError)
				throw new BaseException("get variable["+name+"] error: " + e.getMessage(), e);
		}
		return model;
	}


	public static void setVariable(Environment env, String name, Object val){
		if(val==null)
			return ;
		TemplateModel model = wrapAsModel(val);
		env.setVariable(name, model);
	}

	public static TemplateModel wrapAsModel(Object val){
		if(val==null)
			return null;
		TemplateModel model = null;
		if(TemplateModel.class.isInstance(val)){
			model = (TemplateModel) val;
		}else if(Map.class.isInstance(val)){
			model = new SimpleMapModel((Map)val, BEAN_WRAPPER);
		}else if(Collection.class.isInstance(val)){
			model = new CollectionModel((Collection)val, BEAN_WRAPPER);
		}else if(LangUtils.isArray(val)){
			model = new ArrayModel(val, BEAN_WRAPPER);
		}else if(String.class.isInstance(val)){
			model = new StringModel(val, BEAN_WRAPPER);
		}else if(Number.class.isInstance(val)){
			model = new SimpleNumber((Number)val);
		}else if(Date.class.isInstance(val)){
			model = new DateModel((Date)val, BEAN_WRAPPER);
		}else{
			model = wrapAsBeanModel(val);
		}
		return model;
	}
	
	public static BeanModel wrapAsBeanModel(Object obj){
		if(obj==null)
			return null;
		BeanModel m = null;
		try {
//			BeansWrapper bw = (BeansWrapper)ObjectWrapper.BEANS_WRAPPER;
			m = new BeanModel(obj, BEAN_WRAPPER);
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

	public static String getRequiredParameterByString(Map<?, ?> params, String name){
		TemplateModel val = getParameter(params, name, true);
		return val.toString();
	}
	public static String getParameterByString(Map<?, ?> params, String name, String def){
		TemplateModel attr = getParameter(params, name, false);
		if(attr!=null)
			return attr.toString();
		return def;
	}
	
	public static boolean getParameterByBoolean(Map<?, ?> params, String name, boolean def){
		TemplateModel attr = getParameter(params, name, false);
		if(attr!=null){
			try {
				if(TemplateBooleanModel.class.isInstance(attr)){
					return ((TemplateBooleanModel)attr).getAsBoolean();
				}else{
					return Boolean.valueOf(attr.toString());
				}
			} catch (Exception e) {
				return def;
			}
		}
		return def;
	}
	public static int getParameterByInt(Map<?, ?> params, String name, int def){
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

	public static TemplateModel getRequiredParameter(Map<?, ?> params, String name){
		return getParameter(params, name, true);
	}
	
	public static String getParameter(Map<?, ?> params, String name, String defVal){
		TemplateModel val = getParameter(params, name, false);
		if(val==null)
			return defVal;
		return val.toString();
	}
	
	public static <T> T getParameter(Map<?, ?> params, String name, boolean throwIfNotExist){
		if(!params.containsKey(name)){
			if(throwIfNotExist)
				throw new BaseException("freemarker template error : the param["+name+"] has not be given.");
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
	
	public static TemplateLoader getTemplateLoader(ResourceLoader resourceLoader, String... templateLoaderPaths) {
		Assert.notEmpty(templateLoaderPaths);
		List<TemplateLoader> loaders = LangUtils.newArrayList(templateLoaderPaths.length);
		for(String path :templateLoaderPaths){
			TemplateLoader loader = getTemplateLoaderForPath(resourceLoader, path);
			loaders.add(loader);
		}
		return loaders.size()==1?loaders.get(0):getMultiTemplateLoader(loaders);
	}
	
	public static TemplateLoader getTemplateLoaderForPath(ResourceLoader resourceLoader, String templateLoaderPath) {
		try {
			Resource path = resourceLoader.getResource(templateLoaderPath);
			File file = path.getFile();  // will fail if not resolvable in the file system
			if (logger.isDebugEnabled()) {
				logger.debug(
						"Template loader path [" + path + "] resolved to file path [" + file.getAbsolutePath() + "]");
			}
			return new FileTemplateLoader(file);
		}
		catch (IOException ex) {
			if (logger.isDebugEnabled()) {
				logger.debug("Cannot resolve template loader path [" + templateLoaderPath +
						"] to [java.io.File]: using SpringTemplateLoader as fallback", ex);
			}
			return new SpringTemplateLoader(resourceLoader, templateLoaderPath);
		}
		
	}

	public static TemplateLoader getMultiTemplateLoader(List<TemplateLoader> templateLoaders) {
		Assert.notEmpty(templateLoaders);
		TemplateLoader[] loaders = templateLoaders.toArray(new TemplateLoader[templateLoaders.size()]);
		return new MultiTemplateLoader(loaders);
	}
	
	private FtlUtils(){
	}

}
