package org.onetwo.common.db.wheel;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.MyUtils;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.StringUtils;

@SuppressWarnings("unchecked")
public class BeanDataRowMapper implements DataRowMapper, WheelAware {
	
	private Class<?> beanClass;
	
	private Wheel wheel;
	
	public BeanDataRowMapper(Class<?> beanClass) {
		this.beanClass = beanClass;
	}

	@Override
	public Object mapDataRow(ResultSet rs, Map<String, Integer> columNames) throws SQLException {
		Object bean = newInstance();
		String propName;
		Object value;
		if(bean instanceof Map){
			Map map = (Map) bean;
			for(Map.Entry<String, Integer> col : columNames.entrySet()){
				propName = getPropertyName(col.getKey());
				try {
					int sqlType = DBUtils.getColumnSqlType(rs, col.getValue()+1);
//					value = DBUtils.getValueByFieldFromResultSet(col.getKey(), JDBC.inst().wheel().getJavaType(sqlType), rs);
					Class toType = wheel.getJavaType(sqlType);
					value = getValueHandler(toType).getValue(rs, col.getKey(), toType);
					map.put(propName, value);
				} catch (Exception e) {
					LangUtils.throwBaseException("map row error. propName: " + propName, e);
				}
			}
		}else{
			Map<String, Field> fieldMap = ReflectUtils.toMap(ReflectUtils.findFieldsFilterStatic(beanClass), "name");
			Field field;
			for(Map.Entry<String, Integer> col : columNames.entrySet()){
				propName = getPropertyName(col.getKey());
				if(!fieldMap.containsKey(propName))
					continue;
				field = fieldMap.get(propName);
				try {
					value = getValueHandler(field.getType()).getValue(rs, col.getKey(), field.getType());
//					value = DBUtils.getValueByFieldFromResultSet(col.getKey(), field.getType(), rs);
					MyUtils.setValue(bean, propName, value);
				} catch (Exception e) {
					LangUtils.throwBaseException("map row error. propName: " + propName, e);
				}
			}
		}
		return bean;
	}
	
	protected String getPropertyName(String name){
		return StringUtils.toPropertyName(name);
	}
	
	protected Object newInstance(){
		return ReflectUtils.newInstance(beanClass);
	}
	
	public DBValueHanlder getValueHandler(Class<?> clazz){
//		return DBUtils.getValueHandler(clazz);
		return wheel.getValueHandler(clazz);
	}

	@Override
	public void setWheel(Wheel wheel) {
		this.wheel = wheel;
	}

}
