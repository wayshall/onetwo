package org.onetwo.ext.poi.excel.etemplate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.onetwo.ext.poi.excel.etemplate.ETSheetContext.ETRowContext;
import org.onetwo.ext.poi.excel.etemplate.directive.ETRowDirective;
import org.onetwo.ext.poi.excel.etemplate.directive.ForeachRowDirective;
import org.onetwo.ext.poi.excel.exception.ExcelException;
import org.onetwo.ext.poi.utils.ExcelUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;


public class DefaultExcelTemplateEngineer implements ExcelTemplateEngineer {
	
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private Map<String, ETRowDirective> rowDirectives = Maps.newHashMap();

	public DefaultExcelTemplateEngineer() {
		super();
		addDirective(new ForeachRowDirective());
	}

	final void addDirective(ETRowDirective directive){
		addDirective(directive, false);
	}
	final void addDirective(ETRowDirective directive, boolean override){
		if(rowDirectives.containsKey(directive.getName())){
			if(override){
				rowDirectives.put(directive.getName(), directive);
			}else{
				throw new RuntimeException("directive already exists: " + directive.getName());
			}
		}else{
			rowDirectives.put(directive.getName(), directive);
		}
	}


	protected Object parseCellValue(Cell cell, ETSheetContext sheetContext){
//		return ExcelUtils.getCellValue(cell);
		if(cell==null)
			return null;
		
		CellType type = cell.getCellType();
		Object value = null;
//		if (cell.getRowIndex()==3 && cell.getColumnIndex()==9) {
//			System.out.println("test");
//		}
		
		if(CellType.STRING==type){
			ExcelTemplateValueProvider provider = sheetContext.getValueProvider();
//			value = StringUtils.cleanInvisibleUnicode(cell.getStringCellValue().trim());
			String cellText = cell.getStringCellValue().trim();
			if(provider.isExpresstion(cellText)){
				value = provider.parseCellValue(cellText);
//				Object newCellValue = provider.parseValue(cellText);
//				provider.setCellValue(cell, newCellValue);
			}
		}else if(CellType.NUMERIC==type){
			boolean isDateCell = DateUtil.isCellDateFormatted(cell);
			if (isDateCell) {
				value = cell.getDateCellValue();
			} else {
				value = cell.getNumericCellValue();
			}
		}else if(CellType.FORMULA==type){
//			String cellFormula = cell.getCellFormula(); 
//			sheetContext.getFormulaEvaluator().clearAllCachedResultValues();
//			CellValue cellValue = sheetContext.getFormulaEvaluator().evaluate(cell);
//			value = cellValue.formatAsString();
		}else if(CellType.BOOLEAN==type){
			value = cell.getBooleanCellValue();
		}else if(CellType.BLANK==type){
			value = "";
		}
		return value;
	}
	
	public int parseRow(ETSheetContext sheetContext, Row row){
		for(ETRowDirective d : rowDirectives.values()){
			ETRowContext rowContext = sheetContext.new ETRowContext(row);
			if(d.isMatch(rowContext) ){
				logger.info("match directive[{}], executing...", d.getName());
				try {
					if(d.excecute(rowContext))
						return rowContext.getLastRownumbAfterExecuteTag();
				} catch (Exception e) {
					throw new RuntimeException("execute directive["+d.getName()+"] error:"+e.getMessage(), e);
				}
			}
		}
		parseCommonRow(row, sheetContext);
//		ExcelUtils.copyRow(sheetContext.getSheet(), newRow, repeateRow.getRow());
		return row.getRowNum();
	}
	

	protected void parseCommonRow(Row row, ETSheetContext sheetContext){
		ExcelTemplateValueProvider provider = sheetContext.getValueProvider();
		int cellNumbs = row.getPhysicalNumberOfCells();
		for (int cellIndex = 0; cellIndex < cellNumbs; cellIndex++) {
			Cell cell = row.getCell(cellIndex);
			Object cellValue = parseCellValue(cell, sheetContext);
			if(cellValue==null)
				continue;

//			String cellText = cellValue.toString();
//			if(provider.isExpresstion(cellText)){
//				Object newCellValue = provider.parseCellValue(cellText);
////				Object newCellValue = provider.parseValue(cellText);
//				provider.setCellValue(cell, newCellValue);
//			}
			provider.setCellValue(cell, cellValue);
		}
	}
	
	

	/*protected int parse(final Row row, final ExcelTemplateValueProvider provider){
		RowForeachDirectiveModel forModel = rowForeachDirective.matchStart(row);
		if(forModel!=null){
			Row nextRow = row.getSheet().getRow(row.getRowNum()+1);
			if(rowForeachDirective.matchEnd(forModel, nextRow)){
				nextRow = row.getSheet().getRow(forModel.getEndRow().getRowNum()+1);
				rowForeachDirective.excecute(this, forModel, provider);
				
				int rownumb = nextRow.getRowNum()-1;
				return rownumb;
			}
			throw new BaseException("not match end tag for : " + forModel.getDirectiveStart());
		}else{
			int cellNumbs = row.getPhysicalNumberOfCells();
			for (int cellIndex = 0; cellIndex < cellNumbs; cellIndex++) {
				Cell cell = row.getCell(cellIndex);
				Object cellValue = getCellValue(cell);
				if(cellValue==null)
					continue;
				
				Object newCellValue = provider.parseCellValue(cell, provider);
				ExcelUtils.setCellValue(cell, newCellValue);
			}
		}
		return row.getRowNum();
	}*/

	protected void parseSheet(ETSheetContext sheetContext){
		Sheet sheet = sheetContext.getSheet();
		final ExcelTemplateValueProvider provider = sheetContext.getValueProvider();
//		int rowNumbs = sheet.getPhysicalNumberOfRows();
		List<CellRangeAddress> cellRangeList = Lists.newArrayList();
		for(int i=0; i< sheet.getNumMergedRegions(); i++){
			CellRangeAddress cellRange = sheet.getMergedRegion(i);
			cellRangeList.add(cellRange);
			if(logger.isDebugEnabled()){
				logger.debug("find mergedRegion, first row:{}, last row:{}, firstCol: {}, lastCol: {} ",
								cellRange.getFirstRow(), cellRange.getLastRow(),
								cellRange.getFirstColumn(), cellRange.getLastColumn());
			}
		}
		provider.setCellRangeList(cellRangeList);
		
		for (int rowIndex = 0; rowIndex < sheet.getPhysicalNumberOfRows(); rowIndex++) {
			Row row = sheet.getRow(rowIndex);
			if(row==null)
				continue;
			rowIndex = parseRow(sheetContext, row);
		}

		/*for(int i=0; i< sheet.getNumMergedRegions(); i++){
			CellRangeAddress cellRange = sheet.getMergedRegion(i);
			logger.info("==find mergedRegion, first row:{}, last row:{} " + cellRange.getFirstRow(), cellRange.getLastRow());
		}*/
	}
	
	@Override
	public void generate(File templateFile, String generatedPath, final ETemplateContext context){
		try {
			ExcelUtils.makeDirs(generatedPath, true);
			generate(templateFile, new FileOutputStream(generatedPath), context);
		} catch (FileNotFoundException e) {
			throw new ExcelException("write workbook to ["+generatedPath+"] error: " + e.getMessage());
		}
	}
	
	@Override
	public void generate(File templateFile, OutputStream out, final ETemplateContext context){
//		File destFile = FileUtils.copyFile(templatePath, generatedPath);
//		System.out.println("dest: " + destFile);
		
		Workbook wb = readExcelTemplate(templateFile);
		FormulaEvaluator formulaEvaluator = null;
		if (context.isEvaluateFormula()) {
			formulaEvaluator = wb.getCreationHelper().createFormulaEvaluator();
		}
		
		int sheetNumbs = wb.getNumberOfSheets();
		
		OgnlExcelTemplateValueProvider provider = new OgnlExcelTemplateValueProvider(context);
		for (int index = 0; index < sheetNumbs; index++) {
			Sheet sheet = wb.getSheetAt(index);
			ETSheetContext directiveContext = new ETSheetContext(this, provider, context);
			directiveContext.setFormulaEvaluator(formulaEvaluator);
			directiveContext.setSheet(sheet);
			parseSheet(directiveContext);
		}
//		formulaEvaluator.clearAllCachedResultValues();
//		formulaEvaluator.evaluateAll();
		if (context.isEvaluateFormula()) {
			wb.setForceFormulaRecalculation(true);
		}
		try {
			wb.write(out);
		} catch (Exception e) {
			throw new ExcelException("write workbook error: " + e.getMessage());
		}
	}
	
	public Workbook readExcelTemplate(File destFile){
		InputStream in;
		try {
			in = new FileInputStream(destFile);
		} catch (FileNotFoundException e) {
			throw new ExcelException("create file inpustream error: " + e.getMessage(), e);
		}
		Workbook wb = ExcelUtils.createWorkbook(in);
		return wb;
	}

	@Override
	public void generate(String templateFile, String generatedPath, ETemplateContext context) {
		generate(new File(templateFile), generatedPath, context);
	}

}
