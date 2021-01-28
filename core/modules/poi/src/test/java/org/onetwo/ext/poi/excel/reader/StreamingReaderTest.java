package org.onetwo.ext.poi.excel.reader;
/**
 * @author weishao zeng
 * <br/>
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;
import org.onetwo.ext.poi.utils.ExcelUtils;

import com.monitorjbl.xlsx.StreamingReader;

public class StreamingReaderTest {
	
	@Test
	public void testStream() throws Exception {
		InputStream is = new FileInputStream(new File("/Users/way/mydev/java/cloud-micro/odysseus/bff-tobacco/doc/需求文档/智能派单/番禺-工作日历20201230(1).xls"));
		WorkbookReaderFactory.streamReader().readSheet(0).to(1)
				.row(1).toEnd().onData((dataModel, row, index) -> {
					Cell cell = row.getCell(0);
					ExcelUtils.setCellTypeAsString(cell);
					short dfs = cell.getCellStyle().getDataFormat();
					System.out.println("rwo["+index+"]: " + dfs + ", "+cell.getStringCellValue());
				})
			.endSheet()
			.from(is, null);
	}
	
	@Test
	public void test() throws Exception {
		InputStream is = new FileInputStream(new File("/Users/way/mydev/java/cloud-micro/odysseus/bff-tobacco/doc/需求文档/智能派单/番禺-工作日历20201230(1).xls"));
		Workbook workbook = StreamingReader.builder()
		        .rowCacheSize(100)    // number of rows to keep in memory (defaults to 10)
		        .bufferSize(4096)     // buffer size to use when reading InputStream to file (defaults to 1024)
		        .open(is); 
		for (Sheet sheet : workbook){
			System.out.println(sheet.getSheetName());
			for (Row r : sheet) {
				for (Cell c : r) {
					System.out.println(c.getStringCellValue());
			    }
			}
		}
	}

}
