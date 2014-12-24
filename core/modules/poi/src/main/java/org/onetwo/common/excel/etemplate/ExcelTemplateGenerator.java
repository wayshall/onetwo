package org.onetwo.common.excel.etemplate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.onetwo.common.excel.ExcelUtils;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.utils.Expression;
import org.onetwo.common.utils.FileUtils;

public class ExcelTemplateGenerator {
	
	final private String templatePath;
//	private ExcelVisitor visitor;
//	private List<ExcelObject> nodes;
	private final Expression expression = Expression.DOLOR;

	public ExcelTemplateGenerator(String templatePath) {
		super();
		this.templatePath = templatePath;
	}
	

	public void parse(Cell cell, final ExcelTemplateValueProvider provider, String generatedPath){
		Object cellValue = ExcelUtils.getCellValue(cell);
		if(cellValue==null)
			return ;
		System.out.println("cellvalue:" + cellValue);
		if(expression.isExpresstion(cellValue.toString())){
			final String text = expression.parseByProvider(cellValue.toString(), provider);
			System.out.println("parse:" + text);
		}
	}

	public void parse(Row row, final ExcelTemplateValueProvider provider, String generatedPath){
		int cellNumbs = row.getPhysicalNumberOfCells();
		for (int cellIndex = 0; cellIndex < cellNumbs; cellIndex++) {
			Cell cell = row.getCell(cellIndex);
			parse(cell, provider, generatedPath);
		}
	}

	public void parse(Sheet sheet, final ExcelTemplateValueProvider provider, String generatedPath){
		int rowNumbs = sheet.getPhysicalNumberOfRows();
		for (int rowIndex = 0; rowIndex < rowNumbs; rowIndex++) {
			Row row = sheet.getRow(rowIndex);
			parse(row, provider, generatedPath);
		}
	}
	
	public void generate(final ETemplateContext context, String generatedPath){
		File destFile = FileUtils.copyFile(templatePath, generatedPath);
		System.out.println("dest: " + destFile);
		
		Workbook wb = readExcelTemplate(destFile);
		int sheetNumbs = wb.getNumberOfSheets();
		
		ExcelTemplateValueProvider provider = new ExcelTemplateValueProvider(context);
		for (int index = 0; index < sheetNumbs; index++) {
			Sheet sheet = wb.getSheetAt(index);
			parse(sheet, provider, generatedPath);
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
