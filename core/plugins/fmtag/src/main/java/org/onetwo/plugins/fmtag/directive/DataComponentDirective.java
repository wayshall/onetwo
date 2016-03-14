package org.onetwo.plugins.fmtag.directive;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.onetwo.common.db.CrudEntityManager;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.ftl.DataComponent;
import org.onetwo.common.jackson.JsonMapper;
import org.onetwo.common.spring.SpringApplication;
import org.onetwo.common.spring.cache.JFishSimpleCacheManagerImpl;
import org.onetwo.common.spring.cache.SimpleCacheWrapper;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.Page;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.map.BaseMap;
import org.onetwo.common.web.view.ftl.AbstractDirective;
import org.onetwo.common.web.view.ftl.DirectivesUtils;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import freemarker.cache.TemplateCache;
import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class DataComponentDirective extends AbstractDirective implements TemplateDirectiveModel {

	public static final String DIRECTIVE_NAME = "dataComponent";

	public static final String DATASOURCE = "datasource";
	public static final String PARAMS = "params";
	public static final String TEMPLATE = "template";
	public static final String METHOD = "method";
	public static final String TYPE = "type";
	public static final String TYPE_SERVICE = "service";
	public static final String TYPE_SQL = "sql";
	public static final String CACHE = "cache";
	public static final String EXPIRE = "expire";
	
	public static final String VAR = "var";
	
	public static final int EXPIRE_AFTER_ACCESS = 60 * 30;
	
	private SimpleCacheWrapper dataComponentCache;
	
	public DataComponentDirective(){
		String cacheName = JFishSimpleCacheManagerImpl.FMT_DATA_COMPONENT_CACHE_NAME;
		CacheManager cacheManager = SpringApplication.getInstance().getBean(JFishSimpleCacheManagerImpl.class);
		Cache cache = null;
		if(cacheManager!=null && cacheManager.getCacheNames().contains(cacheName)){
			cache = cacheManager.getCache(cacheName);
		}else{
			logger.info(DIRECTIVE_NAME+" has not cache supported!");
		}
		dataComponentCache = new SimpleCacheWrapper(cache);
	}
	

	@Override
	public void render(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
		String ds = DirectivesUtils.getRequiredParameterByString(params, DATASOURCE);
		Assert.hasText(ds, "component atrribute[datasource] must have text; it must not be null, empty, or blank.");
		
		String paramsStr = DirectivesUtils.getParameterByString(params, PARAMS, null);
		String var = DirectivesUtils.getParameterByString(params, VAR, "__data__");
		String type = DirectivesUtils.getParameterByString(params, TYPE, TYPE_SERVICE);
		String methodName = DirectivesUtils.getParameterByString(params, METHOD, null);
		String template = DirectivesUtils.getParameterByString(params, TEMPLATE, null);
		boolean cache = DirectivesUtils.getParameterByBoolean(params, CACHE, false);
		
//		Object[] dataParams = null;

		Object result = null;
		
		if(cache && dataComponentCache.canCache()){//need Sync
			String key = null;
			key = LangUtils.appendNotBlank(ds, ".", methodName, ":", paramsStr);
			result = dataComponentCache.get(key);
			
			if(result==null){
				result = getDataFromDataSource(type, ds, methodName, paramsStr);

				int expire = DirectivesUtils.getParameterByInt(params, EXPIRE, EXPIRE_AFTER_ACCESS);
				dataComponentCache.put(key, result, expire);
			}
		}else{
			result = getDataFromDataSource(type, ds, methodName, paramsStr);
		}
		
		if(result==null)
			return ;

		
		DirectivesUtils.setVariable(env, var, result);
		if(StringUtils.isBlank(template)){
			if(body!=null)
				body.render(env.getOut());
		}else{
			String templatePath = TemplateCache.getFullTemplatePath(env, "/", template);
			env.include(templatePath, DirectivesUtils.ENCODING, true);
		}
	}
	
	protected Object getDataFromDataSource(String type, String ds, String methodName, String paramsStr){
		Object result = null;
		if(TYPE_SQL.equals(type)){//by named query
//			JFishDataDelegateService jem = SpringApplication.getInstance().getSpringHighestOrder(JFishDataDelegateServiceImpl.class);
//			if(StringUtils.isBlank(paramsStr)){
//				result = jem.findListByQName(ds);
//			}else{
//				Map dataParams = this.parseArgsAsMap(paramsStr);
//				if(dataParams==null)
//					throw new BaseException("it must be a json(map) paramters, check it: " +paramsStr );
//				result = jem.findListByQName(ds, dataParams);
//			}
			throw new UnsupportedOperationException();
		}else{
			Object service = SpringApplication.getInstance().getBean(ds);
			Assert.notNull(service, "can not find any service : " + ds);
			if(service instanceof DataComponent){//by interface
				Map dataParams = this.parseArgsAsMap(paramsStr);
				DataComponent dc = (DataComponent) service;
				result = dc.fetchData((BaseMap)dataParams);
			}else{
				if(StringUtils.isBlank(methodName)){
					Map dataParams = this.parseArgsAsMap(paramsStr);
					if(service instanceof CrudEntityManager && dataParams!=null){//try to invoke CrudEntityManager interface method 
						result = ((CrudEntityManager)service).findByProperties((Map)dataParams);
					}else{
						Assert.hasText(methodName, "component atrribute[method] must have text; it must not be null, empty, or blank.");
					}
				}else{
					MatchedMethod matchMethod = findMatchMethodBy(service.getClass(), methodName, paramsStr);
					if(matchMethod==null)
						throw new BaseException("can not match any method, check it. service:"+ds+", method: " + methodName+", params: " + paramsStr);

					result = ReflectUtils.invokeMethod(matchMethod.matchedMethod, service, (Object[])matchMethod.params);
				}
			}
		}
		return result;
	}
	
	private MatchedMethod findMatchMethodBy(Class<?> clazz, String methodName, String paramsStr){
		List<Method> methods = ReflectUtils.findPublicMethods(clazz, methodName);
		Object[] dataParams = null;
		for(Method method : methods){//match method by parameter type
			dataParams = this.parseArgs(paramsStr, method.getParameterTypes());
			if(ReflectUtils.matchParameters(method.getParameterTypes(), dataParams)){
				return new MatchedMethod(method, dataParams);
			}
		}
		return null;
	}
	
	static class MatchedMethod {
		public MatchedMethod(Method matchedMethod, Object[] params) {
			super();
			this.matchedMethod = matchedMethod;
			this.params = params;
		}
		Method matchedMethod;
		Object[] params;
	}

	private Map parseArgsAsMap(String paramsStr){
		if(StringUtils.isBlank(paramsStr))
			return null;
		Map dataParams = null;
		try {
			dataParams = JsonMapper.DEFAULT_MAPPER.fromJson(paramsStr, BaseMap.class);
		} catch (Exception e) {
//			throw new BaseException("it must be a map paramters: " +paramsStr );
			logger.error("parse params as map error : " + e.getMessage());
		}
		return dataParams;
	}
	
	private Object[] parseArgs(String paramsStr, Class[] argTypes){
		if(StringUtils.isBlank(paramsStr))
			return null;
		Object[] dataParams = null;
		if(LangUtils.isEmpty(argTypes))
			return null;
		List args = new ArrayList();
		String[] strs = StringUtils.split(paramsStr, ",");
		if(strs.length!=argTypes.length)
			throw new BaseException("the count of args is not match, excepted:"+strs.length+", actual: " + argTypes.length);
		Object obj = null;
		int index = 0;
		Class argType = null;
		for(String str : strs){
			argType = argTypes[index++];
			if(LangUtils.isMapClass(argType) || LangUtils.isMultipleObjectClass(argType)){
				obj = JsonMapper.DEFAULT_MAPPER.fromJson(str, argType);
			}else{
				obj = LangUtils.strCastTo(str.trim(), argType);
			}
			args.add(obj);
		}
		dataParams = args.toArray();
		return dataParams;
	}

	@Override
	public String getDirectiveName() {
		return DIRECTIVE_NAME;
	}
	
	public static void main(String[] args){
		DataComponentDirective dc = new DataComponentDirective();
		MatchedMethod m = dc.findMatchMethodBy(Page.class, "setAutoCount", "true");
		System.out.println("m: " + m);
		Page page = new Page();
		ReflectUtils.invokeMethod(m.matchedMethod, page, Boolean.FALSE);
		System.out.println(page.isAutoCount());
	}
	

}
