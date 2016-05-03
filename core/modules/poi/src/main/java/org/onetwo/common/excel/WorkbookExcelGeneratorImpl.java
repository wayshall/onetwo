package org.onetwo.common.excel;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.onetwo.common.excel.data.WorkbookData;

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
//		this.context = context;
//		Object data = context.get("reportData0");
		WorkbookListener workbookListener = null;
		if(StringUtils.isNotBlank(workbookModel.getListener()))
			workbookListener = (WorkbookListener)excelValueParser.parseValue(workbookModel.getListener(), workbookModel, context);
		if(workbookListener==null)
			workbookListener = WorkbookData.EMPTY_WORKBOOK_LISTENER;
		Workbook workbook = PoiModel.FORMAT_XLSX.equalsIgnoreCase(workbookModel.getFormat())?new XSSFWorkbook():new HSSFWorkbook();
		this.workbookData = new WorkbookData(workbookModel, workbook, excelValueParser, workbookListener);
		this.workbookData.initData();
	}
	@Override
	public int generateIt() {
		this.workbookData.getWorkbookListener().afterCreateWorkbook(getWorkbook());
		int total = 0;
		for(TemplateModel template : workbookData.getWorkbookModel().getSheets()){
			PoiExcelGenerator pg = DefaultExcelGeneratorFactory.createExcelGenerator(workbookData, template);
			total += pg.generateIt();
		}
		return total;
	}

	@Override
	public Workbook getWorkbook() {
		return workbookData.getWorkbook();
	}
	@Override
	public WorkbookData getWorkbookData() {
		return workbookData;
	}

}
