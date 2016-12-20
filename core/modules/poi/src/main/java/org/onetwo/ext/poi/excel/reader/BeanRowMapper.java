package org.onetwo.ext.poi.excel.reader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.onetwo.ext.poi.excel.exception.ExcelException;
import org.onetwo.ext.poi.excel.generator.CellValueConvertor;
import org.onetwo.ext.poi.utils.ExcelUtils;
import org.springframework.beans.BeanWrapper;

import com.google.common.collect.Maps;

public class BeanRowMapper<T> extends AbstractRowMapper<T> {
	
	private static enum MapperType {
//		DEFAULT,
		NAME,
		CELL_INDEX
	}
	
	public static <R> BeanRowMapper<R> newMapper(Class<R> clazz, Object...propertyMapper){
		BeanRowMapper<R> rowMapper = new BeanRowMapper<R>(clazz, propertyMapper);
		return rowMapper;
	}
	
	public static <R> BeanRowMapper<R> newMapper(int dataRowStartIndex, Class<R> clazz, Object...propertyMapper){
		BeanRowMapper<R> rowMapper = new BeanRowMapper<R>(dataRowStartIndex, clazz, propertyMapper);
		return rowMapper;
	}

	private Class<T> clazz;
	private Map<Object, String> propertyMapper = Maps.newHashMap();
	private MapperType mapperType;
	private boolean autoGetCellValue = true;
	//0-based
	private int dataRowStartIndex = 1;
	
	/******
	 * 
	 * @param clazz
	 * @param propertyMapper cellIndex1 | fieldName1OfMappedClass, titleLabel1, cellIndex2 | fieldName2OfMappedClass, titleLabel2
	 */
	public BeanRowMapper(Class<T> clazz, Object...propertyMapper) {
		this(1, clazz, propertyMapper);
	}

	public BeanRowMapper(Class<T> clazz, Map<Object, String> propertyMapper, Map<String, CellValueConvertor> convertors) {
		this(1, clazz, propertyMapper, convertors);
	}
	public BeanRowMapper(int dataRowStartIndex, Class<T> clazz, Object...propertyMapper) {
		this(dataRowStartIndex, clazz, null, WorkbookReaderFactory.convertors);
		if(!ExcelUtils.isEmpty(propertyMapper)){
			this.propertyMapper = ExcelUtils.asMap(propertyMapper);
		}
		this.dataRowStartIndex = dataRowStartIndex;
	}
	
	public BeanRowMapper(int dataRowStartIndex, Class<T> clazz, Map<Object, String> propertyMapper, Map<String, CellValueConvertor> convertors) {
		super(convertors);
		this.clazz = clazz;
		this.dataRowStartIndex = dataRowStartIndex;
		if(propertyMapper!=null){
			this.propertyMapper = propertyMapper;
		}
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

	public BeanRowMapper<T> map(Object cellTitleName, String beanProperty){
		this.propertyMapper.put(cellTitleName, beanProperty);
		return this;
	}

	@Override
	public List<String> mapTitleRow(Sheet sheet){
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
		//默认按名称映射，title和property一一对应
		if(ExcelUtils.isEmpty(propertyMapper)){
			mapperType = MapperType.NAME;
			for(String name : titleNames)
				this.propertyMapper.put(name, name);
			return mapperType;
		}
		//由映射的第一值决定是名称映射还是索引映射
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
		
		for(int cellIndex=0; cellIndex<cellCount; cellIndex++){
			this.mapDataObjectField(names, row, bw, cellCount, cellIndex);
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
	
	protected void mapDataObjectField(List<String> names, Row row, BeanWrapper bw, int cellCount, int cellIndex){
		String titleLable = null;
		String propertyName;
		Object cellValue;
		Cell cell = null;
		switch (mapperType) {
			case NAME://列名映射到对象属性
				if(!ExcelUtils.isEmpty(names) && cellIndex<names.size())
					titleLable = names.get(cellIndex);
				
				if(cellIndex<=cellCount){
					cell = row.getCell(cellIndex);
				}
				if(cell==null)
					return ;
				if(!this.propertyMapper.containsKey(titleLable)){
//					continue;
					throw new RuntimeException("no field mapping for title name: " + titleLable);
				}
				
				cellValue = getCellValue(cell, titleLable, cellIndex);
				propertyName = this.propertyMapper.get(titleLable);
				this.setBeanProperty(bw, propertyName, cell, cellValue);
				break;

			case CELL_INDEX://列数映射到对象属性
				if(!this.propertyMapper.containsKey(cellIndex))
					return;

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
			/*if(value!=null && (!String.class.isInstance(value) || !ExcelUtils.isBlank((String)value)))
				bw.setPropertyValue(name, value);*/
			this.setBeanProperty(bw, name, value);
		} catch (Exception e) {
			throw new ExcelException("row:"+cell.getRowIndex()+",set property["+name+"] error, value: "+value, e);
		}
	}

	protected void setBeanProperty(BeanWrapper bw, String name, Object value){
		if(!bw.isWritableProperty(name))
			return ;
		if(value!=null && (!String.class.isInstance(value) || !ExcelUtils.isBlank((String)value)))
			bw.setPropertyValue(name, value);
	}

	@Override
	public String getMapperName() {
		return clazz.getName();
	}

	
}
