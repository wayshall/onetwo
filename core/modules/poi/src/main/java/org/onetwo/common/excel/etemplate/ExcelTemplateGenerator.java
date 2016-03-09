package org.onetwo.common.excel.etemplate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.onetwo.common.excel.ExcelUtils;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.file.FileUtils;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.utils.LangUtils;
import org.slf4j.Logger;

public class ExcelTemplateGenerator {
	
	protected final Logger logger = JFishLoggerFactory.logger(this.getClass());
	
//	final private String templatePath;
	final private File templateFile;
//	private ExcelVisitor visitor;
//	private List<ExcelObject> nodes;
//	private final Expression expression = Expression.DOLOR;
	
	private RowForeachDirective rowForeachDirective = new RowForeachDirective();

	public ExcelTemplateGenerator(String templatePath) {
		super();
		this.templateFile = new File(templatePath);
	}

	public ExcelTemplateGenerator(File templateFile) {
		super();
		this.templateFile = templateFile;
	}


	/*public Object parseCellValue(Cell cell, final ExcelTemplateValueProvider provider){
		Object cellValue = ExcelUtils.getCellValue(cell);
		if(cellValue==null)
			return null;
		String cellText = cellValue.toString();
		if(expression.isExpresstion(cellText)){
			final String text = expression.parseByProvider(cellText, provider);
//			ExcelUtils.setCellValue(cell, text);
			if(provider.isDebug())
				logger.info("parse [{}] as [{}]", cellText, text);
			return text;
		}else{
			return cellValue;
		}
	}*/
	
	public Object getCellValue(Cell cell){
		return ExcelUtils.getCellValue(cell);
	}
	

	public int parse(final Row row, final ExcelTemplateValueProvider provider){
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
	}

	public void parse(Sheet sheet, final ExcelTemplateValueProvider provider){
//		int rowNumbs = sheet.getPhysicalNumberOfRows();
		List<CellRangeAddress> cellRangeList = LangUtils.newArrayList();
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
			rowIndex = parse(row, provider);
		}

		/*for(int i=0; i< sheet.getNumMergedRegions(); i++){
			CellRangeAddress cellRange = sheet.getMergedRegion(i);
			logger.info("==find mergedRegion, first row:{}, last row:{} " + cellRange.getFirstRow(), cellRange.getLastRow());
		}*/
	}
	
	public void generate(final ETemplateContext context, String generatedPath){
		try {
			FileUtils.makeDirs(generatedPath, true);
			generate(context, new FileOutputStream(generatedPath));
		} catch (FileNotFoundException e) {
			throw new BaseException("write workbook to ["+generatedPath+"] error: " + e.getMessage());
		}
	}
	
	public void generate(final ETemplateContext context, OutputStream out){
//		File destFile = FileUtils.copyFile(templatePath, generatedPath);
//		System.out.println("dest: " + destFile);
		
		Workbook wb = readExcelTemplate(templateFile);
		int sheetNumbs = wb.getNumberOfSheets();
		
		ExcelTemplateValueProvider provider = new ExcelTemplateValueProvider(context);
		for (int index = 0; index < sheetNumbs; index++) {
			Sheet sheet = wb.getSheetAt(index);
			parse(sheet, provider);
		}
		try {
			wb.write(out);
		} catch (Exception e) {
			throw new BaseException("write workbook error: " + e.getMessage());
		}
	}
	
	public Workbook readExcelTemplate(File destFile){
		InputStream in;
		try {
			in = new FileInputStream(destFile);
		} catch (FileNotFoundException e) {
			throw new BaseException("create file inpustream error: " + e.getMessage(), e);
		}
		Workbook wb = ExcelUtils.createWorkbook(in);
		return wb;
	}

}
