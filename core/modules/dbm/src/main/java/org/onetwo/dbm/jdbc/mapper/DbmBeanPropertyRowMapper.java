package org.onetwo.dbm.jdbc.mapper;

import java.beans.PropertyDescriptor;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.dbm.jdbc.JdbcResultSetGetter;
import org.onetwo.dbm.jdbc.JdbcUtils;
import org.onetwo.dbm.utils.DbmUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.NotWritablePropertyException;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.ResultSetWrappingSqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

public class DbmBeanPropertyRowMapper<T> implements RowMapper<T> {
	final protected Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	
	protected ConversionService conversionService = new DefaultConversionService();
//	private DbmTypeMapping sqlTypeMapping;
	protected JdbcResultSetGetter jdbcResultSetGetter;
	
	
	protected boolean primitivesDefaultedForNullValue = true;
	protected Class<T> mappedClass;
	protected Set<String> mappedProperties;
	protected Map<String, PropertyDescriptor> mappedFields;

	/*public DbmBeanPropertyRowMapper() {
		super();
	}*/

	public DbmBeanPropertyRowMapper(JdbcResultSetGetter jdbcResultSetGetter, Class<T> mappedClass) {
		this.mappedClass = mappedClass;
		this.jdbcResultSetGetter = jdbcResultSetGetter;
		this.initialize(mappedClass);
	}


	public ConversionService getConversionService() {
		return conversionService;
	}

	public void setConversionService(ConversionService conversionService) {
		this.conversionService = conversionService;
	}

	protected void initialize(Class<T> mappedClass) {
		this.mappedClass = mappedClass;
		this.mappedFields = new HashMap<String, PropertyDescriptor>();
		this.mappedProperties = new HashSet<String>();
		PropertyDescriptor[] pds = BeanUtils.getPropertyDescriptors(mappedClass);
		for (PropertyDescriptor pd : pds) {
			if (pd.getWriteMethod() != null) {
				this.mappedFields.put(JdbcUtils.lowerCaseName(pd.getName()), pd);
				String underscoredName = underscoreName(pd.getName());
				if (!JdbcUtils.lowerCaseName(pd.getName()).equals(underscoredName)) {
					this.mappedFields.put(underscoredName, pd);
				}
				this.mappedProperties.add(pd.getName());
			}
		}
	}

	/**
	 * Convert a name in camelCase to an underscored name in lower case.
	 * Any upper case letters are converted to lower case with a preceding underscore.
	 * @param name the original name
	 * @return the converted name
	 * @since 4.2
	 * @see #lowerCaseName
	 */
	protected String underscoreName(String name) {
		return JdbcUtils.underscoreName(name);
	}

	protected void initBeanWrapper(BeanWrapper bw) {
		ConversionService cs = getConversionService();
		if (cs != null) {
			bw.setConversionService(cs);
		}
	}
	
	@Override
	public T mapRow(ResultSet rs, int rowNumber) throws SQLException {
		Assert.state(this.mappedClass != null, "Mapped class was not specified");
		T mappedObject = BeanUtils.instantiate(this.mappedClass);
		BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(mappedObject);
		initBeanWrapper(bw);

		ResultSetWrappingSqlRowSet resutSetWrapper = new ResultSetWrappingSqlRowSet(rs);
		SqlRowSetMetaData rsmd = resutSetWrapper.getMetaData();
		int columnCount = resutSetWrapper.getMetaData().getColumnCount();

		for (int index = 1; index <= columnCount; index++) {
			String column = DbmUtils.lookupColumnName(rsmd, index);
//			String field = lowerCaseName(column.replaceAll(" ", ""));
			String field = JdbcUtils.lowerCaseName(column);
			PropertyDescriptor pd = this.mappedFields.get(field);
			if (pd != null) {
				try {
					Object value = getColumnValue(resutSetWrapper, index, pd);
					if (rowNumber == 0 && logger.isDebugEnabled()) {
						logger.debug("Mapping column '" + column + "' to property '" + pd.getName() +
								"' of type [" + ClassUtils.getQualifiedName(pd.getPropertyType()) + "]");
					}
					try {
						bw.setPropertyValue(pd.getName(), value);
					}
					catch (TypeMismatchException ex) {
						if (value == null && this.primitivesDefaultedForNullValue) {
							if (logger.isDebugEnabled()) {
								logger.debug("Intercepted TypeMismatchException for row " + rowNumber +
										" and column '" + column + "' with null value when setting property '" +
										pd.getName() + "' of type [" +
										ClassUtils.getQualifiedName(pd.getPropertyType()) +
										"] on object: " + mappedObject, ex);
							}
						}
						else {
							throw ex;
						}
					}
				}
				catch (NotWritablePropertyException ex) {
					throw new DataRetrievalFailureException(
							"Unable to map column '" + column + "' to property '" + pd.getName() + "'", ex);
				}
			}
			else {
				// No PropertyDescriptor found
				if (rowNumber == 0 && logger.isDebugEnabled()) {
					logger.debug("No property found for column '" + column + "' mapped to field '" + field + "'");
				}
			}
		}

		return mappedObject;
	}
	
	protected Object getColumnValue(ResultSetWrappingSqlRowSet rs, int index, PropertyDescriptor pd) throws SQLException {
		return jdbcResultSetGetter.getColumnValue(rs, index, pd);
		/*JFishProperty jproperty = Intro.wrap(pd.getWriteMethod().getDeclaringClass()).getJFishProperty(pd.getName(), false);
		TypeHandler<?> typeHandler = sqlTypeMapping.getTypeHander(jproperty.getType(), sqlType);
		Object value = typeHandler.getResult(rs, index);
		return value;*/
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((mappedClass == null) ? 0 : mappedClass.hashCode());
		result = prime * result
				+ ((mappedFields == null) ? 0 : mappedFields.hashCode());
		result = prime
				* result
				+ ((mappedProperties == null) ? 0 : mappedProperties.hashCode());
		result = prime * result
				+ (primitivesDefaultedForNullValue ? 1231 : 1237);
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DbmBeanPropertyRowMapper<?> other = (DbmBeanPropertyRowMapper<?>) obj;
		if (mappedClass == null) {
			if (other.mappedClass != null)
				return false;
		} else if (!mappedClass.equals(other.mappedClass))
			return false;
		if (mappedFields == null) {
			if (other.mappedFields != null)
				return false;
		} else if (!mappedFields.equals(other.mappedFields))
			return false;
		if (mappedProperties == null) {
			if (other.mappedProperties != null)
				return false;
		} else if (!mappedProperties.equals(other.mappedProperties))
			return false;
		if (primitivesDefaultedForNullValue != other.primitivesDefaultedForNullValue)
			return false;
		return true;
	}

}
