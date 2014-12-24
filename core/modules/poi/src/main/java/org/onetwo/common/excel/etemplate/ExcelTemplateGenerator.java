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
import org.onetwo.common.utils.ValueProvider;

public class ExcelTemplateGenerator {
	
	final private String templatePath;

	public ExcelTemplateGenerator(String templatePath) {
		super();
		this.templatePath = templatePath;
	}
	
	public void generate(final ETemplateContext context, String generatedPath){
		File destFile = FileUtils.copyFile(templatePath, generatedPath);
		System.out.println("dest: " + destFile);
		
		Workbook wb = readExcelTemplate(destFile);
		int sheetNumbs = wb.getNumberOfSheets();
		for (int index = 0; index < sheetNumbs; index++) {
			Sheet sheet = wb.getSheetAt(index);
			int rowNumbs = sheet.getPhysicalNumberOfRows();
			for (int rowIndex = 0; rowIndex < rowNumbs; rowIndex++) {
				Row row = sheet.getRow(rowIndex);
				int cellNumbs = row.getPhysicalNumberOfCells();
				for (int cellIndex = 0; cellIndex < cellNumbs; cellIndex++) {
					Cell cell = row.getCell(cellIndex);
					Object cellValue = ExcelUtils.getCellValue(cell);
					if(Expression.DOLOR.isExpresstion(cellValue.toString())){
						Expression.DOLOR.parseByProvider(cellValue.toString(), new ValueProvider() {
							
							@Override
							public String findString(String var) {
								Object val = context.get(var);
								return val==null?"":val.toString();
							}
						});
					}
						
					System.out.println("cellvalue:" + cellValue);
				}
			}
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
