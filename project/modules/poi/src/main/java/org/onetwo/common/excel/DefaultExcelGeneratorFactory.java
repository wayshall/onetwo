package org.onetwo.common.excel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.onetwo.common.interfaces.excel.ExcelValueParser;

/******
 * excel生成器工厂类
 * @author wayshall
 *
 */
@SuppressWarnings("rawtypes")
abstract public class DefaultExcelGeneratorFactory {
	
	private static Map<String, TemplateModel> TemplateModelCache = new ConcurrentHashMap<String, TemplateModel>();

	public static PoiExcelGenerator createExcelGenerator(TemplateModel template, Map context){
		PoiExcelGenerator generator = new POIExcelGeneratorImpl(template, context);
		return generator;
	}

	public static PoiExcelGenerator createWebExcelGenerator(TemplateModel template, Map context){
		ExcelValueParser parser = new DefaultExcelValueParser(context);
		PoiExcelGenerator generator = new POIExcelGeneratorImpl(template);
		generator.setExcelValueParser(parser);
		return generator;
	}
	
	public static PoiExcelGenerator createExcelGenerator(String templatePath, Map context){
		return createExcelGenerator(getTemplateModel(templatePath), context);
	}
	
	public static PoiExcelGenerator createExcelGenerator(String templatePath, boolean cacheTemplate, Map context){
		return createExcelGenerator(getTemplateModel(templatePath, cacheTemplate), context);
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
