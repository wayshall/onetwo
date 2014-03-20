package org.onetwo.common.excel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;

public class ExcelCompareTest {

	private String basePath = "org/onetwo/common/excel/";
	
	private String path1 = basePath + "bus_origin.xls";
	private String path2 = basePath + "bus_origin.xls";
	
	@Test
	public void test(){
		Map<String, List<?>> map1 = WorkbookReaderFactory.getWorkbookReader(HashMap.class).readData(path1, 2, 1);
		List<HashMap<String, Object>> data1 = (List<HashMap<String, Object>>)map1.entrySet().iterator().next().getValue();
		for(HashMap<String, Object> row : data1){
			System.out.println("row: " + row);
		}
//		Map<String, List<?>> data2 = WorkbookReaderFactory.getWorkbookReader(HashMap.class).readData(path2, 2, 1);
	}

}
