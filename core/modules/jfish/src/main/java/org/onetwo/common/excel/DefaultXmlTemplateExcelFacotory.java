package org.onetwo.common.excel;

import java.util.Map;

import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.exception.SystemErrorCode.ServiceErrorCode;
import org.onetwo.common.interfaces.TemplateGenerator;
import org.onetwo.common.interfaces.XmlTemplateGeneratorFactory;
import org.onetwo.common.spring.SpringApplication;
import org.onetwo.common.spring.cache.JFishSimpleCacheManagerImpl;
import org.onetwo.common.spring.cache.SimpleCacheWrapper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;

/*****
 * 功能与DefaultExcelGeneratorFactory类似，
 * 把bean配置到spring context，提供spring支持的excel生成器工厂。
 * @author wayshall
 *
 */
public class DefaultXmlTemplateExcelFacotory implements XmlTemplateGeneratorFactory, InitializingBean {
	private static final String DEFAULT_BASE_TEMPLATE_DIR = "/WEB-INF/excel/";
	
//	private Map<String, TemplateModel> TemplateModelCache = new HashMap<String, TemplateModel>();
	private SimpleCacheWrapper cache;
	private String baseTemplateDir = DEFAULT_BASE_TEMPLATE_DIR;
	private boolean cacheTemplate;
	
	public DefaultXmlTemplateExcelFacotory(){;
	}
	
	
	@Override
	public void afterPropertiesSet() throws Exception {
		JFishSimpleCacheManagerImpl cacheManager = SpringApplication.getInstance().getBean(JFishSimpleCacheManagerImpl.class);
		cache = cacheManager.getExcelTemplateCache();
	}


	protected TemplateModel getTemplateModel(String path){
		return getTemplateModel(path, cacheTemplate);
	}
	
	protected TemplateModel getTemplateModel(String path, boolean checkCache){
		TemplateModel model = null;
		if(checkCache)
			model = cache.get(path);
		
		if(model==null){
			Resource resource = SpringApplication.getInstance().getAppContext().getResource(getFullTemplatePath(path));
			if(resource==null || !resource.isReadable())
				throw new ServiceException("can not find valid excel template: " + path, ServiceErrorCode.RESOURCE_NOT_FOUND);
			model = ExcelUtils.readTemplate(resource);
			
			if(checkCache)
				cache.put(path, model);
		}
		
		return model;
	}
	
	private String getFullTemplatePath(String path){
		return getBaseTemplateDir() + path;
	}
	
	@Override
	public TemplateGenerator create(String template, Map<?, ?> context) {
		return createExcelGenerator(getTemplateModel(template), context);
	}
	
	public TemplateGenerator createExcelGenerator(TemplateModel template, Map<?, ?> context){
		PoiExcelGenerator generator = new POIExcelGeneratorImpl(template, context);
		return generator;
	}

	public String getBaseTemplateDir() {
		return baseTemplateDir;
	}

	public void setBaseTemplateDir(String baseTemplateDir) {
		this.baseTemplateDir = baseTemplateDir;
	}
	public boolean isCacheTemplate() {
		return cacheTemplate;
	}
	public void setCacheTemplate(boolean cacheTemplate) {
		this.cacheTemplate = cacheTemplate;
	}

}
