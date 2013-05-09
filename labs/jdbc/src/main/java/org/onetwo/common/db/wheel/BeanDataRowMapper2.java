package org.onetwo.common.db.wheel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.MyUtils;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.StringUtils;

public class BeanDataRowMapper2 implements DataRowMapper {
	
	private Class<?> beanClass;
	
	private BeanDataRowMapper2(Class<?> beanClass) {
		this.beanClass = beanClass;
	}

	@Override
	public Object mapDataRow(ResultSet rs, Map<String, Integer> columNames) throws SQLException {
		Object bean = newInstance();
		Object value;
		String propName;
		for(Map.Entry<String, Integer> col : columNames.entrySet()){
			propName = getPropertyName(col.getKey());
			try {
				int sqlType = DBUtils.getColumnSqlType(rs, col.getValue()+1);
				value = DBUtils.getValueByFieldFromResultSet(col.getKey(), SqlTypeFactory.getJavaType(sqlType), rs);
				MyUtils.setValue(bean, propName, value);
				
			} catch (Exception e) {
				LangUtils.throwBaseException("map row error. propName: " + propName, e);
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
	
	public static DBValueHanlder getValueHandler(Class<?> clazz){
		return DBUtils.getValueHandler(clazz);
	}

}
