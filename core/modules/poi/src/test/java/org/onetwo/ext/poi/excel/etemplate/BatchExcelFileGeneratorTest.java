package org.onetwo.ext.poi.excel.etemplate;
/**
 * @author wayshall
 * <br/>
 */

import java.io.File;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.junit.Test;
import org.onetwo.ext.poi.excel.reader.HashMapRowMapper;
import org.onetwo.ext.poi.excel.reader.WorkbookReader;
import org.onetwo.ext.poi.excel.reader.WorkbookReaderFactory;
import org.onetwo.ext.poi.utils.ExcelUtils;

public class BatchExcelFileGeneratorTest {
	DefaultBatchExcelFileGenerator bg = new DefaultBatchExcelFileGenerator();
	
	@Test
	public void testBatchGenerate() {
		String templateFilePath = "";
		String dataFilePath = "";
		ETemplateContext context = ETemplateContext.newContext();
		String outDirPath = "";
		
//		bg.batchGenerate(new File(templateFilePath), new File(dataFilePath), context, new File(outDirPath));
	}

	@Test
	public void testDataFileRead() {
		String dataFilePath = "/Users/way/mydev/work-doc/lp/施工记录11.7.xlsx";
		WorkbookReader reader = WorkbookReaderFactory.createWorkbookByMapper(new HashMapRowMapper(3) {
			public List<String> mapTitleRow(Sheet sheet) {
				Row titleRow = sheet.getRow(1);
				List<String> list = ExcelUtils.getRowValues(titleRow);
				return list;
			}
		});
		
		Map<String, List<Object>> sheetMap = reader.readData(dataFilePath, 5, 1);
		System.out.println("sheetMap: " + sheetMap.keySet());
	}
}
