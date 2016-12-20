package org.onetwo.ext.poi.excel.generator;

import java.util.Map;

import org.onetwo.ext.poi.excel.data.WorkbookData;
import org.onetwo.ext.poi.excel.interfaces.TemplateGenerator;

/******
 * excel生成器工厂类
 * @author wayshall
 *
 */
abstract public class ExcelGenerators {
	
//	private static Map<String, WorkbookModel> TemplateModelCache = new ConcurrentHashMap<String, WorkbookModel>();
	private static final WorkbookGeneratorFactory FACTORY = new WorkbookGeneratorFactory();
	private static boolean devModel = false;

	public static void devModel(){
		devModel = true;
		FACTORY.setCacheTemplate(false);
	}
	public static void disabledDevModel(){
		devModel = false;
		FACTORY.setCacheTemplate(true);
	}

	public static boolean isDevModel() {
		return devModel;
	}
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
		return FACTORY.create(templatePath, context);
	}
	
	public static TemplateGenerator createWorkbookGenerator(String templatePath, Map<String, Object> context){
//		return createWorkbookGenerator(getWorkbookModel(templatePath), context);
		return FACTORY.create(templatePath, context);
	}
	
	public static TemplateGenerator createWorkbookGenerator(WorkbookModel workbook, Map<String, Object> context){
		TemplateGenerator generator = new WorkbookExcelGeneratorImpl(workbook, context);
		return generator;
	}

}
