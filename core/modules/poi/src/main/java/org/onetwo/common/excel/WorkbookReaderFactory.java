package org.onetwo.common.excel;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.onetwo.common.excel.ListRowMapper.StringListRowMapper;
import org.onetwo.common.utils.DateUtil;
import org.onetwo.common.utils.convert.Types;


@SuppressWarnings("unchecked")
public abstract class WorkbookReaderFactory {
	
	abstract public static class AbstractCellValueConvertor implements CellValueConvertor {

		public Object convert(Cell cell) {
			if(cell==null)
				return null;
			Object val = doConvert(cell);
			return val;
		}
		
		protected String getAsString(Cell cell){
			cell.setCellType(Cell.CELL_TYPE_STRING);
			return cell.getStringCellValue();
		}
		abstract protected Object doConvert(Cell cell);
	}
	
	public static class IntegerConvertor extends AbstractCellValueConvertor {

		@Override
		public Integer doConvert(Cell cell) {
			int type = cell.getCellType();
			Integer value = null;
			if(Cell.CELL_TYPE_STRING==type){
				value = Integer.parseInt(cell.getStringCellValue());
			}else if(Cell.CELL_TYPE_NUMERIC==type){
				Double dvalue = cell.getNumericCellValue();
				value = dvalue.intValue();
			}else if(Cell.CELL_TYPE_FORMULA==type){
				CellValue cv = ExcelUtils.getFormulaCellValue(cell);
				value = cv==null?null:(int)cv.getNumberValue();
			}else{
				value = Integer.parseInt(getAsString(cell));
			}
			return value;
		}
		
	}
	
	public static class LongConvertor extends AbstractCellValueConvertor {

		@Override
		public Long doConvert(Cell cell) {
			int type = cell.getCellType();
			Long value = null;
			if(Cell.CELL_TYPE_STRING==type){
				value = ((Number)Double.parseDouble(cell.getStringCellValue())).longValue();
			}else if(Cell.CELL_TYPE_NUMERIC==type){
				Double dvalue = cell.getNumericCellValue();
				value = dvalue.longValue();
			}else if(Cell.CELL_TYPE_FORMULA==type){
				CellValue cv = ExcelUtils.getFormulaCellValue(cell);
				value = cv==null?null:(long)cv.getNumberValue();
			}else{
				value = Long.parseLong(getAsString(cell));
			}
			return value;
		}
		
	}
	
	public static class DoubleConvertor extends AbstractCellValueConvertor {

		@Override
		public Double doConvert(Cell cell) {
			int type = cell.getCellType();
			Double value = null;
			if(Cell.CELL_TYPE_STRING==type){
				value = Double.parseDouble(cell.getStringCellValue());
			}else if(Cell.CELL_TYPE_NUMERIC==type){
				value = cell.getNumericCellValue();
			}else if(Cell.CELL_TYPE_FORMULA==type){
				CellValue cv = ExcelUtils.getFormulaCellValue(cell);
				value = cv==null?null:cv.getNumberValue();
			}else{
				value = Double.parseDouble(getAsString(cell));
			}
			return value;
		}
		
	}

	public static class StringConvertor extends AbstractCellValueConvertor {

		@Override
		public String doConvert(Cell cell) {
			int type = cell.getCellType();
			String value = null;
			if(Cell.CELL_TYPE_STRING==type){
				value = cell.getStringCellValue().trim();
			}else if(Cell.CELL_TYPE_NUMERIC==type){
				Double dvalue = cell.getNumericCellValue();
				if(dvalue!=null){
					value = String.valueOf(dvalue.longValue());
				}
			}else if(Cell.CELL_TYPE_FORMULA==type){
				CellValue cv = ExcelUtils.getFormulaCellValue(cell);
				value = cv==null?null:cv.getStringValue();
			}else if(Cell.CELL_TYPE_BOOLEAN==type){
				boolean bvalue = cell.getBooleanCellValue();
				value = String.valueOf(bvalue);
			}else if(Cell.CELL_TYPE_BLANK==type){
				value = "";
			}
			return value;
		}
		
	}
	public static class DateConvertor extends AbstractCellValueConvertor {

		@Override
		public Date doConvert(Cell cell) {
			int type = cell.getCellType();
			Date value = null;
			if(Cell.CELL_TYPE_STRING==type){
				value = DateUtil.parse(cell.getStringCellValue());
			}else if(Cell.CELL_TYPE_NUMERIC==type){
				value = cell.getDateCellValue();
			}else if(Cell.CELL_TYPE_FORMULA==type){
				CellValue cv = ExcelUtils.getFormulaCellValue(cell);
				value = cv==null?null:Types.convertValue(cv.getStringValue(), Date.class);
			}else {
				value = DateUtil.parse(getAsString(cell));
			}
			return value;
		}
		
	}

	public static Map<String, CellValueConvertor> convertors;
	
	static{
		convertors = new HashMap<String, CellValueConvertor>();
		convertors.put(int.class.getSimpleName(), new IntegerConvertor());
		convertors.put(Integer.class.getSimpleName().toLowerCase(), new IntegerConvertor());
		convertors.put(Long.class.getSimpleName().toLowerCase(), new LongConvertor());
		convertors.put(Double.class.getSimpleName().toLowerCase(), new DoubleConvertor());
		convertors.put(Float.class.getSimpleName().toLowerCase(), new DoubleConvertor());
		convertors.put(String.class.getSimpleName().toLowerCase(), new StringConvertor());
		convertors.put(Date.class.getSimpleName().toLowerCase(), new DateConvertor());
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
