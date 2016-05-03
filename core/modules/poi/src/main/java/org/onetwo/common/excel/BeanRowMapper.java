package org.onetwo.common.excel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.onetwo.common.excel.exception.ExcelException;
import org.onetwo.common.excel.utils.ExcelUtils;
import org.springframework.beans.BeanWrapper;

import com.google.common.collect.Maps;

public class BeanRowMapper<T> extends AbstractRowMapper<T> {
	
	private static enum MapperType {
//		DEFAULT,
		NAME,
		CELL_INDEX
	}
	
	public static <R> BeanRowMapper<R> map(Class<R> clazz, Object...propertyMapper){
		BeanRowMapper<R> rowMapper = new BeanRowMapper<R>(clazz, propertyMapper);
		return rowMapper;
	}
	
	public static <R> BeanRowMapper<R> map(int dataRowStartIndex, Class<R> clazz, Object...propertyMapper){
		BeanRowMapper<R> rowMapper = new BeanRowMapper<R>(dataRowStartIndex, clazz, propertyMapper);
		return rowMapper;
	}

	private Class<T> clazz;
	private Map<Object, String> propertyMapper = Maps.newHashMap();
	private MapperType mapperType;
	private boolean autoGetCellValue = true;
	//0-based
	private int dataRowStartIndex = 1;
	

	public BeanRowMapper(Class<T> clazz, Object...propertyMapper) {
		this(1, clazz, propertyMapper);
	}

	public BeanRowMapper(Class<T> clazz, Map<String, CellValueConvertor> convertors) {
		this(1, clazz, convertors);
	}
	public BeanRowMapper(int dataRowStartIndex, Class<T> clazz, Object...propertyMapper) {
		this(dataRowStartIndex, clazz, WorkbookReaderFactory.convertors);
		if(!ExcelUtils.isEmpty(propertyMapper)){
			this.propertyMapper = ExcelUtils.asMap(propertyMapper);
		}
		this.dataRowStartIndex = dataRowStartIndex;
	}
	
	public BeanRowMapper(int dataRowStartIndex, Class<T> clazz, Map<String, CellValueConvertor> convertors) {
		super(convertors);
		this.clazz = clazz;
		this.dataRowStartIndex = dataRowStartIndex;
	}

	@Override
	public int getDataRowStartIndex() {
		return dataRowStartIndex;
	}
	
	public void setDataRowStartIndex(int dataRowStartIndex) {
		this.dataRowStartIndex = dataRowStartIndex;
	}

	public BeanRowMapper<T> autoGetCellValue(boolean autoGetCellValue) {
		this.autoGetCellValue = autoGetCellValue;
		return this;
	}

	public BeanRowMapper<T> map(Object key, String property){
		this.propertyMapper.put(key, property);
		return this;
	}

	@Override
	public List<String> mapTitleRow(int sheetIndex, Sheet sheet){
		List<String> titleNames = super.mapTitleRow(sheetIndex, sheet);
		this.autoMapperType(titleNames);
		return titleNames;
	}
	

	protected List<String> mapTitleRow(Sheet sheet){
		Row row = sheet.getRow(getTitleRowIndex());
		int cellCount = row.getPhysicalNumberOfCells();
		List<String> rowValues = new ArrayList<String>();
		
		Cell cell = null;
		Object cellValue = null;
		for(int i=0; i<cellCount; i++){
			cell = row.getCell(i);
			cellValue = ExcelUtils.getCellValue(cell);
			String label = ExcelUtils.trimToEmpty(cellValue);
			if(ExcelUtils.isBlank(label)){
				//if title is empty (region column), get the previous title name
				label = rowValues.get(i-1);
			}
			rowValues.add(label);
		}
		return rowValues;
	}
	
	private MapperType autoMapperType(List<String> titleNames){
		if(ExcelUtils.isEmpty(propertyMapper)){
			mapperType = MapperType.NAME;
			for(String name : titleNames)
				this.propertyMapper.put(name, name);
			return mapperType;
		}
		Object key = this.propertyMapper.keySet().iterator().next();
		if(key instanceof Number){
			mapperType = MapperType.CELL_INDEX;
		}else if(key instanceof String){
			mapperType = MapperType.NAME;
		}
		return mapperType;
	}

	/*@Override
	public String getMapperName() {
		return BeanRowMapper.class.getSimpleName()+"."+clazz.getName();
	}*/

	public void setClazz(Class<T> clazz) {
		this.clazz = clazz;
	}

	protected T newBean(){
		return ExcelUtils.newInstance(clazz);
	}
	

	@Override
	public T mapDataRow(List<String> names, Row row, int rowIndex) {
		if(isIgnoreRow(row))
			return null;
//		int cellCount = row.getPhysicalNumberOfCells();
		int cellCount = row.getLastCellNum();
		
		T bean = newBean();
		BeanWrapper bw = ExcelUtils.newBeanWrapper(bean);
		
		String titleLable = null;
		String propertyName;
		Object cellValue;
		for(int cellIndex=0; cellIndex<cellCount; cellIndex++){
			Cell cell = null;
			switch (mapperType) {
				case NAME://列名映射到对象属性
					if(!ExcelUtils.isEmpty(names) && cellIndex<names.size())
						titleLable = names.get(cellIndex);
					
					if(cellIndex<=cellCount){
						cell = row.getCell(cellIndex);
					}
					if(cell==null)
						continue;
					if(!this.propertyMapper.containsKey(titleLable))
						continue;
					
					cellValue = getCellValue(cell, titleLable, cellIndex);
					propertyName = this.propertyMapper.get(titleLable);
					this.setBeanProperty(bw, propertyName, cell, cellValue);
					break;
	
				case CELL_INDEX://列数映射到对象属性
					if(!this.propertyMapper.containsKey(cellIndex))
						continue;

					if(cellIndex<=cellCount){
						cell = row.getCell(cellIndex);
					}
					cellValue = getCellValue(cell, titleLable, cellIndex);
					propertyName = this.propertyMapper.get(cellIndex);
					this.setBeanProperty(bw, propertyName, cell, cellValue);
					break;
					
				default:
					throw new UnsupportedOperationException("maptype:"+mapperType);
			}
			
			/*if(LangUtils.isNotEmpty(names) && i<names.size())
				name = names.get(i);
//			if(name==null)
//				throw new ServiceException("no title " + names.get(i));
			if(i<=cellCount){
				cell = row.getCell(i);
			}
			if(cell==null)
				continue;
			Object cellValue = getCellValue(cell, name, i);
			String propertyName = getPropertyName(cell, name, i);
			this.setBeanProperty(bw, propertyName, cell, cellValue);*/
		}
		this.afterMapOneDataRow(bean, row, rowIndex);
		return bean;
	}
	
	protected void afterMapOneDataRow(T data, Row row, int rowIndex){
	}
	
	/***
	 * 默认忽略空行
	 * @param row
	 * @return
	 */
	protected boolean isIgnoreRow(Row row){
		return row==null;
	}

	/***
	 * 自定义获取对应列的值
	 * @param cell
	 * @param titleName
	 * @param cellINdex
	 * @return
	 */
	protected Object getCellValue(Cell cell, String titleName, int cellINdex){
		return null;
	}
	
	/***
	 * 如果cellValue为null，则尝试再次自动处理
	 * @param bw
	 * @param name
	 * @param cell
	 * @param cellValue
	 */
	protected void setBeanProperty(BeanWrapper bw, String name, Cell cell, Object cellValue){
		if(ExcelUtils.isBlank(name))
			return ;
		Object value = null;
		try {
			if(cellValue==null && autoGetCellValue){
				CellValueConvertor convertor = this.getCellValueConvertor(name);
				if(convertor==null){
					Class<?> type = bw.getPropertyType(name);
					if(type!=null)
						convertor = this.getCellValueConvertor(type.getSimpleName());
				}
				if(convertor!=null)
					value = convertor.convert(cell);
				else
					value = ExcelUtils.getCellValue(cell);
			}else{
				value = cellValue;
			}
			if(value!=null && (!String.class.isInstance(value) || !ExcelUtils.isBlank((String)value)))
				bw.setPropertyValue(name, value);
		} catch (Exception e) {
			throw new ExcelException("row:"+cell.getRowIndex()+",set property["+name+"] error, value: "+value, e);
		}
	}

	@Override
	public String getMapperName() {
		return clazz.getName();
	}

	
}
