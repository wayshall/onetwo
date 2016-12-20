package org.onetwo.ext.poi.excel.generator;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.onetwo.ext.poi.excel.interfaces.XmlTemplateGeneratorFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

public class WorkbookGeneratorFactory extends AbstractWorkbookTemplateFactory implements XmlTemplateGeneratorFactory, ModelGeneratorFactory {
	
	private Map<String, WorkbookModel> workbookModelCache = new ConcurrentHashMap<String, WorkbookModel>();
	
	public WorkbookGeneratorFactory(){
		super(true);
	}

	protected WorkbookModel getWorkbookModel(String path, boolean fromCache){
		WorkbookModel model = null;
		if(fromCache)
			model = workbookModelCache.get(path);
		if(model==null){
			model = readAsWorkbookModel(getResource(path));
			workbookModelCache.put(path, model);
		}
		return model;
	}
	
	protected Resource getResource(String path){
		Resource res = new ClassPathResource(path);
		if(!res.isReadable() || !res.exists()){
			res = new FileSystemResource(path);
		}
		return res;
	}
	
}


