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


public class ExcelTemplateEngineer {
	
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private Map<String, ETRowDirective> rowDirectives = Maps.newHashMap();

	public ExcelTemplateEngineer() {
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

	public Object getCellValue(Cell cell){
		return ExcelUtils.getCellValue(cell);
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
		ExcelUtils.parseCommonRow(row, sheetContext.getValueProvider());
		return row.getRowNum();
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
//			logger.info("find mergedRegion, first row:{}, last row:{} " + cellRange.getFirstRow(), cellRange.getLastRow());
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
	
	public void generate(File templateFile, String generatedPath, final ETemplateContext context){
		try {
			ExcelUtils.makeDirs(generatedPath, true);
			generate(templateFile, new FileOutputStream(generatedPath), context);
		} catch (FileNotFoundException e) {
			throw new ExcelException("write workbook to ["+generatedPath+"] error: " + e.getMessage());
		}
	}
	
	public void generate(File templateFile, OutputStream out, final ETemplateContext context){
//		File destFile = FileUtils.copyFile(templatePath, generatedPath);
//		System.out.println("dest: " + destFile);
		
		Workbook wb = readExcelTemplate(templateFile);
		int sheetNumbs = wb.getNumberOfSheets();
		
		ExcelTemplateValueProvider provider = new ExcelTemplateValueProvider(context);
		for (int index = 0; index < sheetNumbs; index++) {
			Sheet sheet = wb.getSheetAt(index);
			ETSheetContext directiveContext = new ETSheetContext(this, provider, context);
			directiveContext.setSheet(sheet);
			parseSheet(directiveContext);
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

}
