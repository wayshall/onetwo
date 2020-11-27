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

import com.monitorjbl.xlsx.StreamingReader;

public class StreamingReaderTest {
	
	@Test
	public void test() throws Exception {
		InputStream is = new FileInputStream(new File("F:\\资料\\广州烟商\\2月度销售统计表导入模版.xlsx"));
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
