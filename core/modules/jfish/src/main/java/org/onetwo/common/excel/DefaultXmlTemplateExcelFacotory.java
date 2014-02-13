package org.onetwo.common.excel;

import java.util.Map;

import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.exception.SystemErrorCode.ServiceErrorCode;
import org.onetwo.common.interfaces.TemplateGenerator;
import org.onetwo.common.interfaces.XmlTemplateGeneratorFactory;
import org.onetwo.common.spring.SpringApplication;
import org.onetwo.common.spring.cache.JFishSimpleCacheManagerImpl;
import org.onetwo.common.spring.cache.SimpleCacheWrapper;
import org.onetwo.common.web.config.BaseSiteConfig;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;

/*****
 * 功能与DefaultExcelGeneratorFactory类似，
 * 把bean配置到spring context，提供spring支持的excel生成器工厂。
 * @author wayshall
 *
 */
public class DefaultXmlTemplateExcelFacotory implements XmlTemplateGeneratorFactory, ModelGeneratorFactory, InitializingBean {
	
//	private static final String DEFAULT_BASE_TEMPLATE_DIR = "/WEB-INF/excel";
//	private Map<String, TemplateModel> TemplateModelCache = new HashMap<String, TemplateModel>();
	private SimpleCacheWrapper cache;
//	private String baseTemplateDir = DEFAULT_BASE_TEMPLATE_DIR;
	private String baseTemplateDir;
	private boolean cacheTemplate = BaseSiteConfig.getInstance().isProduct();
	private SpringApplication springApplication;
	
	public DefaultXmlTemplateExcelFacotory(){;
	}
	
	
	@Override
	public void afterPropertiesSet() throws Exception {
		this.springApplication = SpringApplication.getInstance();
		JFishSimpleCacheManagerImpl cacheManager = springApplication.getBean(JFishSimpleCacheManagerImpl.class);
		cache = cacheManager.getExcelTemplateCache();
	}


	protected WorkbookModel getWorkbookModel(String path){
		return getWorkbookModel(path, cacheTemplate);
	}
	
	protected WorkbookModel getWorkbookModel(String templatePath, boolean checkCache){
		WorkbookModel model = null;
//		String path = baseTemplateDir + templatePath;
		String path = templatePath;
		if(checkCache)
			model = cache.get(path);
		
		if(model==null){
			Resource resource = getResource(path);
			if(resource==null)
				throw new ServiceException("can not find valid excel template: " + path, ServiceErrorCode.RESOURCE_NOT_FOUND);
			model = ExcelUtils.readAsWorkbookModel(resource);
			
			if(checkCache)
				cache.put(path, model);
		}
		
		return model;
	}
	
	private String getFullTemplatePath(String path){
		return (getBaseTemplateDir()==null?"":getBaseTemplateDir()) + path;
	}
	
	@Override
	public TemplateGenerator create(String template, Map<String, Object> context) {
		return create(getWorkbookModel(template), context);
	}
	
	@Override
	public TemplateGenerator create(WorkbookModel workbook, Map<String, Object> context){
		TemplateGenerator generator = new WorkbookExcelGeneratorImpl(workbook, context);
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


	@Override
	public boolean checkTemplate(String template) {
		return getResource(template)!=null;
	}

	public Resource getResource(String template) {
		String path = getFullTemplatePath(template);
		Resource resource = springApplication.getAppContext().getResource(path);
		if(resource==null)
			return null;
		return resource;
	}

}
