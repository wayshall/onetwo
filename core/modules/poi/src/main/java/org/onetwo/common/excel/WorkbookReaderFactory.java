package org.onetwo.common.excel;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.onetwo.common.excel.ExcelReader.CellValueConvertor;
import org.onetwo.common.excel.ListRowMapper.StringListRowMapper;
import org.onetwo.common.utils.DateUtil;


@SuppressWarnings("unchecked")
public abstract class WorkbookReaderFactory {
	
	public static class IntegerConvertor implements CellValueConvertor{

		@Override
		public Integer convert(Cell cell) {
			if(cell==null)
				return null;
			int type = cell.getCellType();
			Integer value = null;
			if(Cell.CELL_TYPE_STRING==type){
				value = Integer.parseInt(cell.getStringCellValue());
			}else if(Cell.CELL_TYPE_NUMERIC==type){
				Double dvalue = cell.getNumericCellValue();
				value = dvalue.intValue();
			}
			return value;
		}
		
	}
	
	public static class LongConvertor implements CellValueConvertor{

		@Override
		public Long convert(Cell cell) {
			if(cell==null)
				return null;
			int type = cell.getCellType();
			Long value = null;
			if(Cell.CELL_TYPE_STRING==type){
				value = ((Number)Double.parseDouble(cell.getStringCellValue())).longValue();
			}else if(Cell.CELL_TYPE_NUMERIC==type){
				Double dvalue = cell.getNumericCellValue();
				value = dvalue.longValue();
			}
			return value;
		}
		
	}
	
	public static class DoubleConvertor implements CellValueConvertor{

		@Override
		public Double convert(Cell cell) {
			if(cell==null)
				return null;
			int type = cell.getCellType();
			Double value = null;
			if(Cell.CELL_TYPE_STRING==type){
				value = Double.parseDouble(cell.getStringCellValue());
			}else if(Cell.CELL_TYPE_NUMERIC==type){
				value = cell.getNumericCellValue();
			}
			return value;
		}
		
	}

	public static class StringConvertor implements CellValueConvertor{

		@Override
		public String convert(Cell cell) {
			if(cell==null)
				return null;
			int type = cell.getCellType();
			String value = null;
			if(Cell.CELL_TYPE_STRING==type){
				value = cell.getStringCellValue();
			}else if(Cell.CELL_TYPE_NUMERIC==type){
				Double dvalue = cell.getNumericCellValue();
				if(dvalue!=null){
					value = String.valueOf(dvalue.longValue());
				}
			}else if(Cell.CELL_TYPE_FORMULA==type){
				value = cell.getCellFormula().toString();
			}else if(Cell.CELL_TYPE_BOOLEAN==type){
				boolean bvalue = cell.getBooleanCellValue();
				value = String.valueOf(bvalue);
			}else if(Cell.CELL_TYPE_BLANK==type){
				value = "";
			}
			return value;
		}
		
	}
	public static class DateConvertor implements CellValueConvertor{

		@Override
		public Date convert(Cell cell) {
			if(cell==null)
				return null;
			int type = cell.getCellType();
			Date value = null;
			if(Cell.CELL_TYPE_STRING==type){
				value = DateUtil.parse(cell.getStringCellValue());
			}else if(Cell.CELL_TYPE_NUMERIC==type){
				value = cell.getDateCellValue();
			}else {
				value = null;
			}
			return value;
		}
		
	}

	public static Map<String, CellValueConvertor> convertors;
	
	static{
		convertors = new HashMap<String, CellValueConvertor>();
		convertors.put("int", new IntegerConvertor());
		convertors.put("integer", new IntegerConvertor());
		convertors.put("long", new LongConvertor());
		convertors.put("double", new DoubleConvertor());
		convertors.put("float", new DoubleConvertor());
		convertors.put("string", new StringConvertor());
		convertors.put("date", new DateConvertor());
	}

	public static Map<String, WorkbookReader> WORKBOOK_CACHES = new HashMap<String, WorkbookReader>();
	static {
		WorkbookReader listWb = new DefaultRowMapperWorkbookReader(new ListRowMapper());
//		listWb.setRowMapper(new ListRowMapper());
		WORKBOOK_CACHES.put(ListRowMapper.class.getName(), listWb);

		WorkbookReader stringWb = new DefaultRowMapperWorkbookReader(new StringListRowMapper());
//		stringWb.setRowMapper(new StringListRowMapper());
		WORKBOOK_CACHES.put(StringListRowMapper.class.getName(), stringWb);
	}

	public static WorkbookReader getWorkbookReader(){
		return getWorkbookReader(HashMap.class);
	}

	public static WorkbookReader getStringListWorkbookReader(){
		return getCacheWorkbookReader(StringListRowMapper.class.getName());
	}
	public static WorkbookReader getListWorkbookReader(){
		return getCacheWorkbookReader(ListRowMapper.class.getName());
	}
	
	public static WorkbookReader getCacheWorkbookReader(String key){
		return WORKBOOK_CACHES.get(key);
	}

	@SuppressWarnings("rawtypes")
	public static WorkbookReader getWorkbookReader(Class clazz){
		String key = clazz.getName();
		WorkbookReader wb = (WorkbookReader)getCacheWorkbookReader(key);
		if(wb!=null)
			return wb;
		wb = new DefaultRowMapperWorkbookReader(new BeanRowMapper(clazz, convertors));
//		wb.setRowMapper(new BeanRowMapper(clazz, convertors));
		WORKBOOK_CACHES.put(key, wb);
		return wb;
	}

	/*public static WorkbookReader getWorkbookByMapper(Class<? extends SSFRowMapper<?>> rowMapperClass){
		String key = rowMapperClass.getName();
		WorkbookReader wb = WORKBOOK_CACHES.get(key);
		if(wb!=null)
			return wb;
		wb = new DefaultWorkbookReader();
		wb.setRowMapper(ReflectUtils.newInstance(rowMapperClass));
		WORKBOOK_CACHES.put(key, wb);
		return wb;
	}*/

	public static WorkbookReader getWorkbookByMapper(SSFRowMapper<?> rowMapper){
		WorkbookReader wb = new DefaultRowMapperWorkbookReader(rowMapper);
		return wb;
	}

	public static WorkbookReader createWorkbookByMapper(SSFRowMapper<?> rowMapper){
		WorkbookReader wb = new DefaultRowMapperWorkbookReader(rowMapper);
		return wb;
	}

	public static ExcelReader createDefaultExcelReader(){
		ExcelReader wb = new DefaultPOIExcelReader();
		return wb;
	}
}
