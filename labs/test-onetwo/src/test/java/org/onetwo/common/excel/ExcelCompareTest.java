package org.onetwo.common.excel;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.ss.usermodel.Row;
import org.junit.Test;
import org.onetwo.common.utils.CUtils;
import org.onetwo.common.utils.FileUtils;
import org.onetwo.plugins.batch.utils.BatchUtils;
import org.onetwo.plugins.batch.utils.SimpleFlatFileItemReader;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class ExcelCompareTest {

	private String basePath = "org/onetwo/common/excel/";
	
	private String path1 = basePath + "bus_copy.xls";
	private String path2 = basePath + "bus_origin.xls";
	
//	@Test
	public void testDate(){
		BeanRowMapper<HashMap> bm = BeanRowMapper.map(4, HashMap.class, 1, "carno", 2, "cardno");
		Map<String, List<?>> map1 = WorkbookReaderFactory.createWorkbookByMapper(bm).readData(path1, 2, 1);
		List<HashMap<String, Object>> copyData = (List<HashMap<String, Object>>)map1.entrySet().iterator().next().getValue();
		Map<String, List<?>> map2 = WorkbookReaderFactory.createWorkbookByMapper(bm).readData(path2, 2, 1);
		List<HashMap<String, Object>> originData = (List<HashMap<String, Object>>)map2.entrySet().iterator().next().getValue();
		System.out.println("size1: " + copyData.size());
		System.out.println("size2: " + originData.size());
		
//		Set<HashMap<String, Object>> diff = Sets.difference(new HashSet<HashMap<String, Object>>(data1), new HashSet<HashMap<String, Object>>(data2));
//		Map<String, List<?>> data2 = WorkbookReaderFactory.getWorkbookReader(HashMap.class).readData(path2, 2, 1);
		Collection<HashMap<String, Object>> diff = CUtils.difference(copyData, originData, "carno", "cardno");
		System.out.println("copyData diff originData: " + diff.size());
		System.out.println("copyData diff originData: " + diff);
		Collection<HashMap<String, Object>> diff2 = CUtils.difference(originData, copyData, "carno", "cardno");
		System.out.println("originData diff copyData: " + diff2.size());
		System.out.println("originData diff copyData: " + diff2);
	}
	
	@Test
	public void testMonth(){
		path1 = basePath + "bus_copy_month.xls";
		path2 = basePath + "bus_origin_month.xls";
		
		BeanRowMapper<HashMap> bm = new BeanRowMapper<HashMap>(4, HashMap.class, 1, "carno", 2, "cardno"){

			protected void afterMapOneDataRow(HashMap data, Row row, int rowIndex){
				data.put("rowIndex", rowIndex);
			}
		};
		Map<String, List<?>> map1 = WorkbookReaderFactory.createWorkbookByMapper(bm).readData(path1, 0, 1);
		List<HashMap<String, Object>> copyData = (List<HashMap<String, Object>>)map1.entrySet().iterator().next().getValue();
		Map<String, List<?>> map2 = WorkbookReaderFactory.createWorkbookByMapper(bm).readData(path2, 0, 1);
		List<HashMap<String, Object>> originData = (List<HashMap<String, Object>>)map2.entrySet().iterator().next().getValue();
		System.out.println("size1: " + copyData.size());
		System.out.println("size2: " + originData.size());
		
//		Set<HashMap<String, Object>> diff = Sets.difference(new HashSet<HashMap<String, Object>>(data1), new HashSet<HashMap<String, Object>>(data2));
//		Map<String, List<?>> data2 = WorkbookReaderFactory.getWorkbookReader(HashMap.class).readData(path2, 2, 1);
		Collection<HashMap<String, Object>> diff = CUtils.difference(copyData, originData, "carno", "cardno");
		System.out.println("copyData diff originData: " + diff.size());
		System.out.println("copyData diff originData: " + diff);
		Collection<HashMap<String, Object>> diff2 = CUtils.difference(originData, copyData, "carno", "cardno");
		System.out.println("originData diff copyData: " + diff2.size());
		System.out.println("originData diff copyData: " + diff2);
	}


	public static class TransactionMapper implements FieldSetMapper<HashMap<String, Object>> {

		@Override
		public HashMap<String, Object> mapFieldSet(FieldSet fieldSet) throws BindException {
			HashMap<String, Object> row = new HashMap<String, Object>();
			row.put("lineno", fieldSet.readString(1));
			row.put("carno", fieldSet.readString(3));
			row.put("cardno", fieldSet.readString(4));
			return row;
		}
		
	}
	@Test
	public void testMonthTxt(){
		path1 = basePath + "db2014-4.txt";
		path2 = basePath + "em2014-4.xls";
		
		path1 = FileUtils.getResourcePath(path1);
		SimpleFlatFileItemReader<HashMap<String, Object>> reader = BatchUtils.newFlatFileItemReader(path1, new TransactionMapper());
		reader.fastOpen();
		List<HashMap<String, Object>> dbdatas = reader.readList();
		
		BeanRowMapper<HashMap> bm = new BeanRowMapper<HashMap>(4, HashMap.class, 1, "carno", 2, "cardno"){

			protected void afterMapOneDataRow(HashMap data, Row row, int rowIndex){
				data.put("rowIndex", rowIndex);
			}
		};
		Map<String, List<?>> map1 = WorkbookReaderFactory.createWorkbookByMapper(bm).readData(path2, 0, 2);
		Iterator<Entry<String, List<?>>> it = map1.entrySet().iterator();
		List<HashMap<String, Object>> sheet1 = (List<HashMap<String, Object>>)it.next().getValue();
		List<HashMap<String, Object>> sheet2 = (List<HashMap<String, Object>>)it.next().getValue();
		System.out.println("sheet1: " + sheet1.size());
		System.out.println("sheet2: " + sheet2.size());
		sheet1.addAll(sheet2);
		
//		Set<HashMap<String, Object>> diff = Sets.difference(new HashSet<HashMap<String, Object>>(data1), new HashSet<HashMap<String, Object>>(data2));
//		Map<String, List<?>> data2 = WorkbookReaderFactory.getWorkbookReader(HashMap.class).readData(path2, 2, 1);
		Collection<HashMap<String, Object>> diff = CUtils.difference(dbdatas, sheet1, "carno", "cardno");
		System.out.println("dbdatas diff originData: " + diff.size());
		System.out.println("dbdatas diff originData: " + diff);
		Collection<HashMap<String, Object>> diff2 = CUtils.difference(sheet1, dbdatas, "carno", "cardno");
		System.out.println("originData diff dbdatas: " + diff2.size());
		System.out.println("originData diff dbdatas: " + diff2);
	}
}
