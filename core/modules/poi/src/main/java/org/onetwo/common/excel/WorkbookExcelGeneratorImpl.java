package org.onetwo.common.excel;

import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.onetwo.common.interfaces.excel.ExcelValueParser;
import org.onetwo.common.utils.StringUtils;

public class WorkbookExcelGeneratorImpl extends AbstractWorkbookExcelGenerator {
	
	private WorkbookModel workbookModel;
	private WorkbookData workbookData;
//	private Map<String, Object> context;

	public WorkbookExcelGeneratorImpl(WorkbookModel workbookModel, Map<String, Object> context){
		this.workbookModel = workbookModel;
		ExcelValueParser excelValueParser = new DefaultExcelValueParser(context);
//		this.context = context;
		WorkbookListener workbookListener = null;
		if(StringUtils.isNotBlank(workbookModel.getListener()))
			workbookListener = (WorkbookListener)excelValueParser.parseValue(workbookModel.getListener(), null, workbookModel);
		if(workbookListener==null)
			workbookListener = WorkbookData.EMPTY_WORKBOOK_LISTENER;
		this.workbookData = new WorkbookData(new HSSFWorkbook(), excelValueParser, workbookListener);
	}
	@Override
	public void generateIt() {
		this.workbookData.getWorkbookListener().afterCreateWorkbook(getWorkbook());
		for(TemplateModel template : workbookModel.getSheets()){
			PoiExcelGenerator pg = DefaultExcelGeneratorFactory.createExcelGenerator(workbookData, template);
			pg.generateIt();
		}
	}

	@Override
	public Workbook getWorkbook() {
		return workbookData.getWorkbook();
	}

}
