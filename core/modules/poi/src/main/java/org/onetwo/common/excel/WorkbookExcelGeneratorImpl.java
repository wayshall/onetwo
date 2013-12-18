package org.onetwo.common.excel;

import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;

public class WorkbookExcelGeneratorImpl extends AbstractWorkbookExcelGenerator {
	
	private WorkbookModel workbookModel;
	private Workbook workbook;
	private Map<String, Object> context;

	public WorkbookExcelGeneratorImpl(WorkbookModel workbookModel, Map<String, Object> context){
		this.workbookModel = workbookModel;
		this.context = context;
	}
	@Override
	public void generateIt() {
		this.workbook = new HSSFWorkbook();
		for(TemplateModel template : workbookModel.getSheets()){
			PoiExcelGenerator pg = DefaultExcelGeneratorFactory.createExcelGenerator(workbook, template, context);
			pg.generateIt();
		}
	}

	@Override
	public Workbook getWorkbook() {
		return workbook;
	}

}
