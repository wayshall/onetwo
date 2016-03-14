package org.onetwo.common.jfishdbm.jdbc.mapper;

import java.beans.PropertyDescriptor;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.onetwo.common.jfishdbm.utils.DbmUtils;
import org.onetwo.common.reflect.Intro;
import org.onetwo.common.utils.JFishProperty;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.support.JdbcUtils;

public class DbmBeanPropertyRowMapper<T> extends BeanPropertyRowMapper<T> {
	

	public DbmBeanPropertyRowMapper() {
		super();
	}

	public DbmBeanPropertyRowMapper(Class<T> mappedClass,
			boolean checkFullyPopulated) {
		super(mappedClass, checkFullyPopulated);
	}

	public DbmBeanPropertyRowMapper(Class<T> mappedClass) {
		super(mappedClass);
	}

	protected Object getColumnValue(ResultSet rs, int index, PropertyDescriptor pd) throws SQLException {
		final Object value = JdbcUtils.getResultSetValue(rs, index, pd.getPropertyType());
		JFishProperty jproperty = Intro.wrap(pd.getWriteMethod().getDeclaringClass()).getJFishProperty(pd.getName(), false);
		Object actualValue = DbmUtils.convertPropertyValue(jproperty, value);
		return actualValue;
	}

}
