package org.onetwo.common.excel;

import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.interfaces.excel.ExcelValueParser;
import org.onetwo.common.utils.StringUtils;

/***
 * excel（多sheet）生成器
 * @author weishao
 *
 */
public class WorkbookExcelGeneratorImpl extends AbstractWorkbookExcelGenerator {
	
	private WorkbookData workbookData;
//	private Map<String, Object> context;

	public WorkbookExcelGeneratorImpl(WorkbookModel workbookModel, Map<String, Object> context){
		DefaultExcelValueParser excelValueParser = new DefaultExcelValueParser(context);
		for(VarModel var : workbookModel.getVars()){
			if(context.containsKey(var.getName()))
				throw new BaseException("var is exist: " + var.getName());
			excelValueParser.putVar(var.getName(), var.getValue());
		}
//		this.context = context;
		WorkbookListener workbookListener = null;
		if(StringUtils.isNotBlank(workbookModel.getListener()))
			workbookListener = (WorkbookListener)excelValueParser.parseValue(workbookModel.getListener(), null, workbookModel);
		if(workbookListener==null)
			workbookListener = WorkbookData.EMPTY_WORKBOOK_LISTENER;
		this.workbookData = new WorkbookData(workbookModel, new HSSFWorkbook(), excelValueParser, workbookListener);
		
	}
	@Override
	public void generateIt() {
		this.workbookData.getWorkbookListener().afterCreateWorkbook(getWorkbook());
		for(TemplateModel template : workbookData.getWorkbookModel().getSheets()){
			PoiExcelGenerator pg = DefaultExcelGeneratorFactory.createExcelGenerator(workbookData, template);
			pg.generateIt();
		}
	}

	@Override
	public Workbook getWorkbook() {
		return workbookData.getWorkbook();
	}

}
