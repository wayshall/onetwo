package org.onetwo.dbm.jdbc.mapper;

import java.beans.PropertyDescriptor;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Map.Entry;

import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.reflect.ClassIntroManager;
import org.onetwo.common.reflect.Intro;
import org.onetwo.common.utils.JFishProperty;
import org.onetwo.dbm.annotation.DbmCascadeResult;
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

import com.google.common.collect.Maps;

public class DbmNestedBeanRowMapper<T> implements RowMapper<T> {
	final protected Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	
	protected ConversionService conversionService = new DefaultConversionService();

	protected Class<T> mappedClass;
//	private DbmTypeMapping sqlTypeMapping;
	protected JdbcResultSetGetter jdbcResultSetGetter;
	protected PropertySetter propertySetter;

	/*public DbmBeanPropertyRowMapper() {
		super();
	}*/

	public DbmNestedBeanRowMapper(JdbcResultSetGetter jdbcResultSetGetter, Class<T> mappedClass) {
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

	
	
	class PropertySetter {
		private String idField = "id";
		private String columnPrefix = "";
		private Intro<?> classIntro;
		private Map<String, JFishProperty> simpleFields = Maps.newHashMap();
		private Map<String, PropertySetter> complexFields = Maps.newHashMap();
		
		public PropertySetter(String idField, String columnPrefix,
				Class<?> mappedClass) {
			super();
			this.idField = idField;
			this.columnPrefix = columnPrefix;
			this.classIntro = ClassIntroManager.getInstance().getIntro(mappedClass);
		}

		protected void initialize(Class<?> mappedClass) {
			for (PropertyDescriptor pd : classIntro.getProperties()) {
				if(pd.getWriteMethod()==null){
					continue;
				}
				JFishProperty jproperty = classIntro.getJFishProperty(pd.getName(), false);
				if(jproperty.hasAnnotation(DbmCascadeResult.class)){
					DbmCascadeResult result = jproperty.getAnnotation(DbmCascadeResult.class);
					PropertySetter setter = new PropertySetter(result.idField(), appendPrefix(result.columnPrefix()), jproperty.getType());
					complexFields.put(jproperty.getName(), setter);
				}else{
					this.simpleFields.put(JdbcUtils.lowerCaseName(pd.getName()), jproperty);
				}
			}
		}
		
		protected String appendPrefix(String name){
			return columnPrefix + name;
		}
		
		public Object mapResult(Map<String, Integer> names, ResultSetWrappingSqlRowSet resutSetWrapper){
			Object id = null;
			Object mappedObject = classIntro.newInstance();
			BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(mappedObject);
			initBeanWrapper(bw);
			
			for(Entry<String, JFishProperty> entry : simpleFields.entrySet()){
				JFishProperty jproperty = entry.getValue();
				String field = JdbcUtils.lowerCaseName(appendPrefix(entry.getKey()));
				if(names.containsKey(field)){
					int index = names.get(field);
					setPropertyValue(bw, resutSetWrapper, index, jproperty);
					
				}else if(names.containsKey(JdbcUtils.underscoreName(field))){
					int index = names.get(field);
					setPropertyValue(bw, resutSetWrapper, index, jproperty);
				}
			}
			return mappedObject;
		}

		protected void setPropertyValue(BeanWrapper bw, ResultSetWrappingSqlRowSet resutSetWrapper, int index, JFishProperty jproperty) {
			Object value = getColumnValue(resutSetWrapper, index, jproperty.getPropertyDescriptor());
			bw.setPropertyValue(jproperty.getName(), value);
		}
	}


	protected Object getColumnValue(ResultSetWrappingSqlRowSet rs, int index, PropertyDescriptor pd) {
		return jdbcResultSetGetter.getColumnValue(rs, index, pd);
	}
	
	@Override
	public T mapRow(ResultSet rs, int rowNum) throws SQLException {
		Assert.state(this.mappedClass != null, "Mapped class was not specified");
		T mappedObject = BeanUtils.instantiate(this.mappedClass);
		BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(mappedObject);
		initBeanWrapper(bw);

		ResultSetWrappingSqlRowSet resutSetWrapper = new ResultSetWrappingSqlRowSet(rs);
		SqlRowSetMetaData rsmd = resutSetWrapper.getMetaData();
		int columnCount = resutSetWrapper.getMetaData().getColumnCount();
		Map<String, Integer> names = DbmUtils.lookupColumnNames(rsmd);
		
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

	protected void initBeanWrapper(BeanWrapper bw) {
		ConversionService cs = getConversionService();
		if (cs != null) {
			bw.setConversionService(cs);
		}
	}

}
