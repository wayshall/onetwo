package org.onetwo.common.excel;

import java.beans.PropertyDescriptor;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.onetwo.common.excel.ExcelReader.CellValueConvertor;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.StringUtils;

public class BeanRowMapper<T> extends AbstractRowMapper<T> {

	private Class<T> clazz;
	
	public BeanRowMapper(Class<T> clazz) {
		this(clazz, WorkbookReaderFactory.convertors);
	}
	
	public BeanRowMapper(Class<T> clazz, Map<String, CellValueConvertor> convertors) {
		super(convertors);
		this.clazz = clazz;
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
		int cellCount = row.getPhysicalNumberOfCells();
		
		T bean = newBean();
		String name = null;
		for(int i=0; i<names.size(); i++){
			Cell cell = null;
			name = names.get(i);
			if(name==null)
				throw new ServiceException("no title " + names.get(i));
			if(i<=cellCount){
				cell = row.getCell(i);
			}
			this.setBeanProperty(bean, getPropertyName(name), cell);
		}
		return bean;
	}
	
	protected String getPropertyName(String name){
		return name;
	}
	
	protected void setBeanProperty(Object bean, String name, Cell cell){
		CellValueConvertor convertor = null;
		Object parentObj = bean;
		Object value = null;
		try {
			String[] paths = StringUtils.split(name, ".");
			String targetProperty = name;
			int index = 0;
			for(String path : paths){
				if(index==(paths.length-1)){
					targetProperty = path;
					break;
				}
				Object pathObj = null;
				try {
					pathObj = PropertyUtils.getProperty(parentObj, path);
				} catch (Exception e) {
					throw new BaseException("setBeanProperty error", e);
				}
				if(pathObj==null){
					PropertyDescriptor pd = ReflectUtils.getPropertyDescriptor(parentObj, path);
					if(pd!=null){
						pathObj = ReflectUtils.newInstance(pd.getPropertyType());
						PropertyUtils.setProperty(parentObj, path, pathObj);
					}
				}
				if(pathObj!=null)
					parentObj = pathObj;
				index++;
			}
			PropertyDescriptor pd = ReflectUtils.getPropertyDescriptor(parentObj, targetProperty);
			if(pd!=null){
				Class<?> pType = pd.getPropertyType();
				convertor = this.getCellValueConvertor(pType.getName());
				if(convertor==null)
					convertor = this.getCellValueConvertor(pType.getSimpleName());
			}
			if(convertor!=null)
				value = convertor.convert(cell);
			else
				value = ExcelUtils.getCellValue(cell);
			PropertyUtils.setProperty(parentObj, targetProperty, value);
		} catch (Exception e) {
			throw new ServiceException("setBeanProperty error : class["+bean+"], property[" + name+"], value["+value+"]", e);
		}
	}

	@Override
	public String getMapperName() {
		return clazz.getName();
	}

	
}
