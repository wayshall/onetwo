package org.onetwo.boot.module.poi;

import org.onetwo.common.exception.ServiceException;
import org.onetwo.ext.poi.excel.generator.AbstractWorkbookTemplateFactory;
import org.onetwo.ext.poi.excel.generator.ModelGeneratorFactory;
import org.onetwo.ext.poi.excel.generator.WorkbookModel;
import org.onetwo.ext.poi.excel.interfaces.XmlTemplateGeneratorFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

/*****
 * 功能与DefaultExcelGeneratorFactory类似，
 * 把bean配置到spring context，提供spring支持的excel生成器工厂。
 * @author wayshall
 *
 */
public class DefaultXmlTemplateExcelFacotory extends AbstractWorkbookTemplateFactory implements XmlTemplateGeneratorFactory, ModelGeneratorFactory, InitializingBean {
	
//	private static final String DEFAULT_BASE_TEMPLATE_DIR = "/WEB-INF/excel";
//	private Map<String, TemplateModel> TemplateModelCache = new HashMap<String, TemplateModel>();
	private Cache<String, WorkbookModel> modelCaches;
//	private String baseTemplateDir = DEFAULT_BASE_TEMPLATE_DIR;
	private String baseTemplateDir = "classpath:";
	@Autowired
	private ApplicationContext appContext;
	
	public DefaultXmlTemplateExcelFacotory(){
		super(true);
	}
	
	
	@Override
	public void afterPropertiesSet() throws Exception {
		this.modelCaches = CacheBuilder.newBuilder()
										.maximumSize(100)
										.build();
	}


	protected WorkbookModel getWorkbookModel(String path){
		return getWorkbookModel(path, cacheTemplate);
	}
	
	protected WorkbookModel getWorkbookModel(String templatePath, boolean checkCache){
		WorkbookModel model = null;
//		String path = baseTemplateDir + templatePath;
		String path = templatePath;
		if(checkCache)
			model = modelCaches.getIfPresent(path);
		
		if(model==null){
			Resource resource = getResource(path);
			if(resource==null){
				throw new ServiceException("can not find valid excel template: " + path);
			}
			model = readAsWorkbookModel(resource);
			
			if(checkCache){
				modelCaches.put(path, model);
			}
		}
		
		return model;
	}
	
	private String getFullTemplatePath(String path){
		return (getBaseTemplateDir()==null?"":getBaseTemplateDir()) + path;
	}
	
	public String getBaseTemplateDir() {
		baseTemplateDir = "classpath:";
		return baseTemplateDir;
	}

	public void setBaseTemplateDir(String baseTemplateDir) {
		this.baseTemplateDir = baseTemplateDir;
	}

	@Override
	public boolean checkTemplate(String template) {
		Resource res = getResource(template);
		return res!=null && res.exists();
	}

	public Resource getResource(String template) {
		String path = getFullTemplatePath(template);
		Resource resource = appContext.getResource(path);
		return resource;
	}

}
