package org.onetwo.common.excel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.onetwo.common.interfaces.TemplateGenerator;
import org.onetwo.common.interfaces.XmlTemplateGeneratorFactory;

public class WorkbookGeneratorFactory implements XmlTemplateGeneratorFactory, ModelGeneratorFactory {
	
	private Map<String, WorkbookModel> workbookModelCache = new ConcurrentHashMap<String, WorkbookModel>();
	
	@Override
	public TemplateGenerator create(String template, Map<String, Object> context) {
//		return DefaultExcelGeneratorFactory.createWorkbookGenerator(template, context);
		return create(getWorkbookModel(template, true), context);
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
			model = ExcelUtils.readAsWorkbookModel(path);
			workbookModelCache.put(path, model);
		}
		return model;
	}
}
