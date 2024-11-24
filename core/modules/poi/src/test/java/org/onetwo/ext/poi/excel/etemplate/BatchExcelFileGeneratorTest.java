package org.onetwo.ext.poi.excel.etemplate;
/**
 * @author wayshall
 * <br/>
 */

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.ext.poi.excel.reader.HashMapRowMapper;
import org.onetwo.ext.poi.excel.reader.WorkbookReader;
import org.onetwo.ext.poi.excel.reader.WorkbookReaderFactory;

public class BatchExcelFileGeneratorTest {
	DefaultBatchExcelFileGenerator bg = new DefaultBatchExcelFileGenerator();
	String templateFilePath = "/Users/way/mydev/work-doc/lp/template.xlsx";
	String dataFilePath = "/Users/way/mydev/work-doc/lp/data.xlsx";
	String outDir = "/Users/way/mydev/work-doc/lp/batch";
	
	@Test
	public void testBatchGenerate() {
		SimpleBatchExceGenerator g = SimpleBatchExceGenerator.create()
													.templateFilePath(templateFilePath)
													.dataFilePath(dataFilePath)
													.dataFileTitleRowIndex(1)
													.dataFileDataStartRowIndex(3)
													.outDirPath(outDir)
													.dataFileSheetIndex(5)
													.keyName("槽段编号");
		g.generate(3);
		
//		bg.batchGenerate(new File(templateFilePath), new File(dataFilePath), context, new File(outDirPath));
	}

	@Test
	public void testDataFileRead() {
		WorkbookReader reader = WorkbookReaderFactory.createWorkbookByMapper(new HashMapRowMapper(1, 3));
		
		Map<String, List<Object>> sheetMap = reader.readData(dataFilePath, 5, 1);
		System.out.println("sheetMap: " + sheetMap.keySet());
		
		bg.setTemplateFilePath(templateFilePath);
		bg.generate(outDir, () -> {
			List<Map<String, Object>> dataList = LangUtils.getFirst(sheetMap);
			return dataList.subList(0, 3);
//			return dataList;
		});
	}
}
