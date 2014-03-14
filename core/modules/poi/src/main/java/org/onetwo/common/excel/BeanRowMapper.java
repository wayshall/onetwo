package org.onetwo.common.excel;

import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.onetwo.common.excel.ExcelReader.CellValueConvertor;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.StringUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;

import com.google.common.collect.Maps;

public class BeanRowMapper<T> extends AbstractRowMapper<T> {
	
	private static enum MapperType {
		DEFAULT,
		NAME,
		CELL_INDEX
	}
	
	public static <R> BeanRowMapper<R> map(Class<R> clazz, Object...propertyMapper){
		BeanRowMapper<R> rowMapper = new BeanRowMapper<R>(clazz, propertyMapper);
		return rowMapper;
	}

	private Class<T> clazz;
	private Map<Object, String> propertyMapper = Maps.newHashMap();
	private MapperType mapperType;
	private boolean autoGetCellValue = true;
	
	public BeanRowMapper(Class<T> clazz, Object...propertyMapper) {
		this(clazz, WorkbookReaderFactory.convertors);
		if(!LangUtils.isEmpty(propertyMapper)){
			this.propertyMapper = LangUtils.asMap(propertyMapper);
		}
	}
	
	public BeanRowMapper(Class<T> clazz, Map<String, CellValueConvertor> convertors) {
		super(convertors);
		this.clazz = clazz;
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
		this.autoMapperType();
		return this.mapTitleRow(sheet);
	}
	
	private MapperType autoMapperType(){
		mapperType = MapperType.DEFAULT;
		if(LangUtils.isEmpty(propertyMapper))
			return mapperType;
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
		return ReflectUtils.newInstance(clazz);
	}

	@Override
	public T mapDataRow(List<String> names, Row row, int rowIndex) {
		if(isIgnoreRow(row))
			return null;
		int cellCount = row.getPhysicalNumberOfCells();
		
		T bean = newBean();
		BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(bean);
		bw.setAutoGrowNestedPaths(true);
		
		String name = null;
		for(int i=0; i<names.size(); i++){
			Cell cell = null;
			if(LangUtils.isNotEmpty(names))
				name = names.get(i);
//			if(name==null)
//				throw new ServiceException("no title " + names.get(i));
			if(i<=cellCount){
				cell = row.getCell(i);
			}
			Object cellValue = getCellValue(cell, name, i);
			String propertyName = getPropertyName(cell, name, i);
			this.setBeanProperty(bw, propertyName, cell, cellValue);
		}
		return bean;
	}
	
	protected boolean isIgnoreRow(Row row){
		return false;
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
		if(StringUtils.isBlank(name))
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
			if(value!=null)
				bw.setPropertyValue(name, value);
		} catch (Exception e) {
			throw new BaseException("set property["+name+"] error, value: "+value, e);
		}
	}
	
	protected String getPropertyName(Cell cell, String name, int cellINdex){
		String propertyName = null;
		switch (mapperType) {
			case NAME:
				propertyName = this.propertyMapper.get(name);
				break;
				
			case CELL_INDEX:
				propertyName = this.propertyMapper.get(cellINdex);
				break;
	
			default:
				propertyName = name;
				break;
		}
		return propertyName;
	}

	@Override
	public String getMapperName() {
		return clazz.getName();
	}

	
}
