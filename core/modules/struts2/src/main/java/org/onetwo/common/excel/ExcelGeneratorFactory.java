package org.onetwo.common.excel;

import java.util.HashMap;
import java.util.Map;

import org.onetwo.common.interfaces.excel.ExcelValueParser;

@SuppressWarnings("rawtypes")
abstract public class ExcelGeneratorFactory {
	
	private static Map<String, TemplateModel> TemplateModelCache = new HashMap<String, TemplateModel>();

	public static PoiExcelGenerator createExcelGenerator(TemplateModel template, Map context){
		PoiExcelGenerator generator = new POIExcelGeneratorImpl(template, context);
		return generator;
	}

	public static PoiExcelGenerator createWebExcelGenerator(TemplateModel template, Map context){
		ExcelValueParser parser = new WebExcelValueParser(context);
		PoiExcelGenerator generator = new POIExcelGeneratorImpl(template);
		generator.setExcelValueParser(parser);
		return generator;
	}
	
	public static PoiExcelGenerator createExcelGenerator(String templatePath, Map context){
		return createExcelGenerator(getTemplateModel(templatePath), context);
	}

	
	public static TemplateModel getTemplateModel(String path){
		return getTemplateModel(path, true);
	}
	
	public static TemplateModel getTemplateModel(String path, boolean fromCache){
		TemplateModel model = null;
		if(fromCache)
			model = TemplateModelCache.get(path);
		if(model==null){
			model = ExcelUtils.readTemplate(path);
			TemplateModelCache.put(path, model);
		}
		return model;
	}

}
