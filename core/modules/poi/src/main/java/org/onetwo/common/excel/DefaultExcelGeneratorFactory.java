package org.onetwo.common.excel;

import java.util.Map;

import org.onetwo.common.excel.data.WorkbookData;
import org.onetwo.common.excel.interfaces.TemplateGenerator;

/******
 * excel生成器工厂类
 * @author wayshall
 *
 */
//@SuppressWarnings("rawtypes")
abstract public class DefaultExcelGeneratorFactory {
	
//	private static Map<String, WorkbookModel> TemplateModelCache = new ConcurrentHashMap<String, WorkbookModel>();
	private static final WorkbookGeneratorFactory WF = new WorkbookGeneratorFactory();

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
	
	public static TemplateGenerator createExcelGenerator(String templatePath, Map<String, Object> context){
		return WF.create(templatePath, context);
	}
	
	public static TemplateGenerator createWorkbookGenerator(String templatePath, Map<String, Object> context){
//		return createWorkbookGenerator(getWorkbookModel(templatePath), context);
		return WF.create(templatePath, context);
	}
	
	public static TemplateGenerator createWorkbookGenerator(WorkbookModel workbook, Map<String, Object> context){
		TemplateGenerator generator = new WorkbookExcelGeneratorImpl(workbook, context);
		return generator;
	}
	/*
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
	}*/

}
