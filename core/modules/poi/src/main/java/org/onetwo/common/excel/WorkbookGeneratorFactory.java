package org.onetwo.common.excel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.onetwo.common.interfaces.TemplateGenerator;
import org.onetwo.common.interfaces.XmlTemplateGeneratorFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class WorkbookGeneratorFactory implements XmlTemplateGeneratorFactory, ModelGeneratorFactory {
	
	private Map<String, WorkbookModel> workbookModelCache = new ConcurrentHashMap<String, WorkbookModel>();
	private boolean cacheTemplate = true;
	
	@Override
	public TemplateGenerator create(String template, Map<String, Object> context) {
//		return DefaultExcelGeneratorFactory.createWorkbookGenerator(template, context);
		return create(getWorkbookModel(template, cacheTemplate), context);
	}
	
	@Override
	public TemplateGenerator create(WorkbookModel workbook, Map<String, Object> context){
		TemplateGenerator generator = new WorkbookExcelGeneratorImpl(workbook, context);
		return generator;
	}

	protected WorkbookModel getWorkbookModel(String path, boolean fromCache){
		WorkbookModel model = null;
		if(fromCache)
			model = workbookModelCache.get(path);
		if(model==null){
			model = ExcelUtils.readAsWorkbookModel(getResource(path));
			workbookModelCache.put(path, model);
		}
		return model;
	}


	@Override
	public boolean checkTemplate(String template) {
		return getWorkbookModel(template, cacheTemplate)!=null;
	}

	public void setCacheTemplate(boolean cacheTemplate) {
		this.cacheTemplate = cacheTemplate;
	}
	
	protected Resource getResource(String path){
		Resource res = new ClassPathResource(path);
		if(!res.isReadable()){
			return null;
		}
		return res;
	}
	
}


