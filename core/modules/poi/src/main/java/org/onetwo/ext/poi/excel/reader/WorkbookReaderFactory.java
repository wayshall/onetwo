package org.onetwo.ext.poi.excel.reader;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.onetwo.ext.poi.excel.generator.CellValueConvertor;
import org.onetwo.ext.poi.excel.reader.ListRowMapper.StringListRowMapper;
import org.onetwo.ext.poi.excel.stream.ExcelStreamReader;
import org.onetwo.ext.poi.excel.stream.ExcelStreamReaderBuilder;
import org.onetwo.ext.poi.excel.stream.MonitorExcelStreamReader;
import org.onetwo.ext.poi.excel.stream.SheetStreamReaderBuilder.SheetStreamReader;
import org.onetwo.ext.poi.utils.ExcelUtils;
import org.onetwo.ext.poi.utils.TheFunction;
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
			ExcelUtils.setCellTypeAsString(cell);
			return StringUtils.trimToEmpty(cell.getStringCellValue());
		}
		
		protected String getStringValue(Cell cell){
			String strValue = StringUtils.trimToEmpty(cell.getStringCellValue());
//			strValue = org.onetwo.common.utils.StringUtils.cleanInvisibleUnicode(strValue);
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
			CellType type = cell.getCellType();
			Integer value = null;
			if(CellType.STRING==type){
				String strValue = getStringValue(cell);
				if(StringUtils.isBlank(strValue))
					return defaultValue;
				value = Integer.parseInt(strValue);
			}else if(CellType.NUMERIC==type){
				Double dvalue = cell.getNumericCellValue();
				value = dvalue.intValue();
			}else if(CellType.FORMULA==type){
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
			CellType type = cell.getCellType();
			Long value = null;
			if(CellType.STRING==type){
				value = ((Number)Double.parseDouble(getStringValue(cell))).longValue();
			}else if(CellType.NUMERIC==type){
				Double dvalue = cell.getNumericCellValue();
				value = dvalue.longValue();
			}else if(CellType.FORMULA==type){
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
			CellType type = cell.getCellType();
			Double value = null;
			if(CellType.STRING==type){
				value = Double.parseDouble(getStringValue(cell));
			}else if(CellType.NUMERIC==type){
				value = cell.getNumericCellValue();
			}else if(CellType.FORMULA==type){
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
			CellType type = cell.getCellType();
			String value = null;
			if(CellType.STRING==type){
				value = getStringValue(cell);
			}else if(CellType.NUMERIC==type){
				Double dvalue = cell.getNumericCellValue();
				if(dvalue!=null){
					value = String.valueOf(dvalue.longValue());
				}
			}else if(CellType.FORMULA==type){
				CellValue cv = ExcelUtils.getFormulaCellValue(cell);
				value = cv==null?defaultValue:cv.getStringValue();
			}else if(CellType.BOOLEAN==type){
				boolean bvalue = cell.getBooleanCellValue();
				value = String.valueOf(bvalue);
			}else if(CellType.BLANK==type){
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
			CellType type = cell.getCellType();
			Date value = null;
			if(CellType.STRING==type){
				value = TheFunction.getInstance().parseDateTime(getStringValue(cell));
			}else if(CellType.NUMERIC==type){
				value = cell.getDateCellValue();
			}else if(CellType.FORMULA==type){
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

	/*private static Map<String, WorkbookReader> WORKBOOK_CACHES = new ConcurrentHashMap<String, WorkbookReader>();
	static {
		WorkbookReader listWb = new DefaultRowMapperWorkbookReader(new ListRowMapper());
//		listWb.setRowMapper(new ListRowMapper());
		WORKBOOK_CACHES.put(ListRowMapper.class.getName(), listWb);

		WorkbookReader stringWb = new DefaultRowMapperWorkbookReader(new StringListRowMapper());
//		stringWb.setRowMapper(new StringListRowMapper());
		WORKBOOK_CACHES.put(StringListRowMapper.class.getName(), stringWb);
	}*/
	
	public static ExcelStreamReaderBuilder streamReader() {
		return new ExcelStreamReaderBuilder();
	}
	
	public static ExcelStreamReaderBuilder streamReader(int bufferSize) {
		return streamReader(bufferSize, bufferSize/10);
	}
	
	public static ExcelStreamReaderBuilder streamReader(int bufferSize, int rowCacheSize) {
		return new ExcelStreamReaderBuilder() {
			protected ExcelStreamReader build(List<SheetStreamReader<?>> sheetReaders) {
				ExcelStreamReader reader = new MonitorExcelStreamReader(sheetReaders, bufferSize, rowCacheSize);
				return reader;
			}
		};
	}

	public static WorkbookReader getWorkbookReader(){
		return getWorkbookReader(HashMap.class);
	}

	public static WorkbookReader getStringListWorkbookReader(){
		return getWorkbookReader(StringListRowMapper.class);
	}
	public static WorkbookReader getListWorkbookReader(){
		return getWorkbookReader(ListRowMapper.class);
	}
	
	/*public static WorkbookReader getCacheWorkbookReader(String key){
		return WORKBOOK_CACHES.get(key);
	}*/
	
	/****
	 * 创建excel读取器
	 * @author weishao zeng
	 * @param clazz 每一行excel数据映射的对象
	 * @param dataRowStartIndex 数据行的索引，若第一行为标题行，第二行开始为数据行，则此参数为1
	 * @param propertyMapper
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static WorkbookReader createWorkbookReader(Class clazz, int dataRowStartIndex, Object...propertyMapper){
		WorkbookReader wb = new DefaultRowMapperWorkbookReader(new BeanRowMapper(dataRowStartIndex, clazz, ExcelUtils.asMap(propertyMapper), convertors));
		return wb;
	}

	@SuppressWarnings("rawtypes")
	public static WorkbookReader getWorkbookReader(Class clazz){
		WorkbookReader wb = new DefaultRowMapperWorkbookReader(new BeanRowMapper(clazz, convertors));
//		wb.setRowMapper(new BeanRowMapper(clazz, convertors));
//		WORKBOOK_CACHES.put(key, wb);
		return wb;
	}

	public static WorkbookReader getWorkbookByMapper(SheetRowMapper<?> rowMapper){
		WorkbookReader wb = new DefaultRowMapperWorkbookReader(rowMapper);
		return wb;
	}

	public static WorkbookReader createWorkbookByMapper(SheetRowMapper<?> rowMapper){
		WorkbookReader wb = new DefaultRowMapperWorkbookReader(rowMapper);
		return wb;
	}

	public static ExcelReader createDefaultExcelReader(){
		ExcelReader wb = new DefaultPOIExcelReader();
		return wb;
	}

	public static Map<String, CellValueConvertor> getConvertors() {
		return convertors;
	}
}
