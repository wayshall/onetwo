package org.onetwo.common.excel;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.onetwo.common.excel.ListRowMapper.StringListRowMapper;
import org.onetwo.common.excel.utils.ExcelUtils;
import org.onetwo.common.excel.utils.TheFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@SuppressWarnings("unchecked")
public abstract class WorkbookReaderFactory {
	
	abstract public static class AbstractCellValueConvertor<T> implements CellValueConvertor {
		protected Logger logger = LoggerFactory.getLogger(this.getClass());
		protected T defaultValue;
		
		public AbstractCellValueConvertor(T defaultValue) {
			this.defaultValue = defaultValue;
		}

		public Object convert(Cell cell) {
			if(cell==null)
				return defaultValue;
			try {
				Object val = doConvert(cell);
				return val;
			} catch (Exception e) {
				logger.error("conver cell value error, row: "+cell.getRowIndex());
				return defaultValue;
			}
		}

		protected T defaulltValueIfNull(T value){
			return value==null?defaultValue:value;
		}
		
		protected String getAsString(Cell cell){
			cell.setCellType(Cell.CELL_TYPE_STRING);
			return StringUtils.trimToEmpty(cell.getStringCellValue());
		}
		
		protected String getStringValue(Cell cell){
			String strValue = StringUtils.trimToEmpty(cell.getStringCellValue());
			return strValue;
		}
		abstract protected Object doConvert(Cell cell);
	}
	
	public static class IntegerConvertor extends AbstractCellValueConvertor<Integer> {

		public IntegerConvertor(Integer defaultValue) {
			super(defaultValue);
		}

		@Override
		public Integer doConvert(Cell cell) {
			int type = cell.getCellType();
			Integer value = null;
			if(Cell.CELL_TYPE_STRING==type){
				String strValue = getStringValue(cell);
				if(StringUtils.isBlank(strValue))
					return defaultValue;
				value = Integer.parseInt(strValue);
			}else if(Cell.CELL_TYPE_NUMERIC==type){
				Double dvalue = cell.getNumericCellValue();
				value = dvalue.intValue();
			}else if(Cell.CELL_TYPE_FORMULA==type){
				CellValue cv = ExcelUtils.getFormulaCellValue(cell);
				value = cv==null?defaultValue:(int)cv.getNumberValue();
			}else{
				String strValue = getAsString(cell);
				if(StringUtils.isBlank(strValue))
					return defaultValue;
				value = Integer.parseInt(strValue);
			}
			return value;
		}
		
	}
	
	public static class LongConvertor extends AbstractCellValueConvertor<Long> {

		public LongConvertor(Long defaultValue) {
			super(defaultValue);
		}

		@Override
		public Long doConvert(Cell cell) {
			int type = cell.getCellType();
			Long value = null;
			if(Cell.CELL_TYPE_STRING==type){
				value = ((Number)Double.parseDouble(getStringValue(cell))).longValue();
			}else if(Cell.CELL_TYPE_NUMERIC==type){
				Double dvalue = cell.getNumericCellValue();
				value = dvalue.longValue();
			}else if(Cell.CELL_TYPE_FORMULA==type){
				CellValue cv = ExcelUtils.getFormulaCellValue(cell);
				value = cv==null?defaultValue:(long)cv.getNumberValue();
			}else{
				String strValue = getAsString(cell);
				if(StringUtils.isBlank(strValue))
					return defaultValue;
				value = Long.parseLong(strValue);
			}
			return value;
		}
		
	}
	
	public static class DoubleConvertor extends AbstractCellValueConvertor<Double> {

		public DoubleConvertor(Double defaultValue) {
			super(defaultValue);
		}

		@Override
		public Double doConvert(Cell cell) {
			int type = cell.getCellType();
			Double value = null;
			if(Cell.CELL_TYPE_STRING==type){
				value = Double.parseDouble(getStringValue(cell));
			}else if(Cell.CELL_TYPE_NUMERIC==type){
				value = cell.getNumericCellValue();
			}else if(Cell.CELL_TYPE_FORMULA==type){
				CellValue cv = ExcelUtils.getFormulaCellValue(cell);
				value = cv==null?defaultValue:cv.getNumberValue();
			}else{
				String strValue = getAsString(cell);
				if(StringUtils.isBlank(strValue))
					return defaultValue;
				value = Double.parseDouble(strValue);
			}
			return value;
		}
		
	}

	public static class StringConvertor extends AbstractCellValueConvertor<String> {

		public StringConvertor(String defaultValue) {
			super(defaultValue);
		}

		@Override
		public String doConvert(Cell cell) {
			int type = cell.getCellType();
			String value = null;
			if(Cell.CELL_TYPE_STRING==type){
				value = getStringValue(cell);
			}else if(Cell.CELL_TYPE_NUMERIC==type){
				Double dvalue = cell.getNumericCellValue();
				if(dvalue!=null){
					value = String.valueOf(dvalue.longValue());
				}
			}else if(Cell.CELL_TYPE_FORMULA==type){
				CellValue cv = ExcelUtils.getFormulaCellValue(cell);
				value = cv==null?defaultValue:cv.getStringValue();
			}else if(Cell.CELL_TYPE_BOOLEAN==type){
				boolean bvalue = cell.getBooleanCellValue();
				value = String.valueOf(bvalue);
			}else if(Cell.CELL_TYPE_BLANK==type){
				value = "";
			}
			return value;
		}
		
	}
	public static class DateConvertor extends AbstractCellValueConvertor<Date> {

		public DateConvertor(Date defaultValue) {
			super(defaultValue);
		}

		@Override
		public Date doConvert(Cell cell) {
			int type = cell.getCellType();
			Date value = null;
			if(Cell.CELL_TYPE_STRING==type){
				value = TheFunction.getInstance().parseDateTime(getStringValue(cell));
			}else if(Cell.CELL_TYPE_NUMERIC==type){
				value = cell.getDateCellValue();
			}else if(Cell.CELL_TYPE_FORMULA==type){
				CellValue cv = ExcelUtils.getFormulaCellValue(cell);
				value = cv==null?defaultValue:TheFunction.getInstance().parseDateTime(cv.getStringValue());//Types.convertValue(cv.getStringValue(), Date.class);
			}else {
				String strValue = getAsString(cell);
				if(StringUtils.isBlank(strValue))
					return defaultValue;
				value = TheFunction.getInstance().parseDateTime(strValue);
			}
			return value;
		}
		
	}

	public static Map<String, CellValueConvertor> convertors;
	
	static{
		convertors = new HashMap<String, CellValueConvertor>();
		convertors.put(int.class.getSimpleName(), new IntegerConvertor(0));
		convertors.put(Integer.class.getSimpleName().toLowerCase(), new IntegerConvertor(null));
		convertors.put(long.class.getSimpleName().toLowerCase(), new LongConvertor(0L));
		convertors.put(Long.class.getSimpleName().toLowerCase(), new LongConvertor(null));
		convertors.put(double.class.getSimpleName().toLowerCase(), new DoubleConvertor(0.0));
		convertors.put(Double.class.getSimpleName().toLowerCase(), new DoubleConvertor(null));
		convertors.put(float.class.getSimpleName().toLowerCase(), new DoubleConvertor(0.0));
		convertors.put(Float.class.getSimpleName().toLowerCase(), new DoubleConvertor(null));
		convertors.put(String.class.getSimpleName().toLowerCase(), new StringConvertor(null));
		convertors.put(Date.class.getSimpleName().toLowerCase(), new DateConvertor(null));
	}

	private static Map<String, WorkbookReader> WORKBOOK_CACHES = new ConcurrentHashMap<String, WorkbookReader>();
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
	public static WorkbookReader getWorkbookReader(Class clazz, int dataRowStartIndex, Object...propertyMapper){
		WorkbookReader wb = new DefaultRowMapperWorkbookReader(new BeanRowMapper(dataRowStartIndex, clazz, ExcelUtils.asMap(propertyMapper), convertors));
		return wb;
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
