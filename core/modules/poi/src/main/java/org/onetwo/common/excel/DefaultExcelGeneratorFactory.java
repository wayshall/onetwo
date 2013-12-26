package org.onetwo.common.excel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.onetwo.common.interfaces.TemplateGenerator;

/******
 * excel生成器工厂类
 * @author wayshall
 *
 */
//@SuppressWarnings("rawtypes")
abstract public class DefaultExcelGeneratorFactory {
	
	private static Map<String, WorkbookModel> TemplateModelCache = new ConcurrentHashMap<String, WorkbookModel>();

	public static PoiExcelGenerator createExcelGenerator(TemplateModel template, Map<String, Object> context){
		PoiExcelGenerator generator = new POIExcelGeneratorImpl(template, context);
		return generator;
	}

	public static PoiExcelGenerator createExcelGenerator(WorkbookData workbook, TemplateModel template){
		PoiExcelGenerator generator = new POIExcelGeneratorImpl(workbook, template);
		return generator;
	}
	
	public static PoiExcelGenerator createWebExcelGenerator(TemplateModel template, Map<String, Object> context){
//		ExcelValueParser parser = new DefaultExcelValueParser(context);
		PoiExcelGenerator generator = new POIExcelGeneratorImpl(template);
//		generator.setExcelValueParser(parser);
		return generator;
	}
	
	public static PoiExcelGenerator createExcelGenerator(String templatePath, Map<String, Object> context){
		return createExcelGenerator(getTemplateModel(templatePath, true), context);
	}
	
	public static PoiExcelGenerator createExcelGenerator(String templatePath, boolean cacheTemplate, Map<String, Object> context){
		return createExcelGenerator(getTemplateModel(templatePath, cacheTemplate), context);
	}

	
	public static TemplateGenerator createWorkbookGenerator(String templatePath, Map<String, Object> context){
		return createWorkbookGenerator(getWorkbookModel(templatePath), context);
	}
	
	public static TemplateGenerator createWorkbookGenerator(String templatePath, boolean cacheTemplate, Map<String, Object> context){
		return createWorkbookGenerator(getWorkbookModel(templatePath, cacheTemplate), context);
	}
	public static TemplateGenerator createWorkbookGenerator(WorkbookModel workbook, Map<String, Object> context){
		TemplateGenerator generator = new WorkbookExcelGeneratorImpl(workbook, context);
		return generator;
	}
	
	public static TemplateModel getTemplateModel(String path, boolean fromCache){
		return getWorkbookModel(path, fromCache).getSheet(0);
	}
	
	public static WorkbookModel getWorkbookModel(String path){
		return getWorkbookModel(path, true);
	}
	
	public static WorkbookModel getWorkbookModel(String path, boolean fromCache){
		WorkbookModel model = null;
		if(fromCache)
			model = TemplateModelCache.get(path);
		if(model==null){
			model = ExcelUtils.readAsWorkbookModel(path);
//			model.getFreezer().freezing();
			TemplateModelCache.put(path, model);
		}
		return model;
	}

}
