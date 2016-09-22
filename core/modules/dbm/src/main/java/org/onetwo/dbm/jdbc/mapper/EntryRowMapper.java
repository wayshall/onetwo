package org.onetwo.dbm.jdbc.mapper;

import java.beans.PropertyDescriptor;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.dbm.jdbc.JdbcResultSetGetter;
import org.onetwo.dbm.mapping.DbmMappedField;
import org.onetwo.dbm.mapping.JFishMappedEntry;
import org.onetwo.dbm.utils.DbmUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.ResultSetWrappingSqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;

public class EntryRowMapper<T> implements RowMapper<T>{
	
	protected final Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	
	private JFishMappedEntry entry;
	private boolean debug = false;
//	private DbmTypeMapping sqlTypeMapping;
	private JdbcResultSetGetter jdbcResultSetGetter;
	
	public EntryRowMapper(JFishMappedEntry entry, JdbcResultSetGetter jdbcResultSetGetter) {
		super();
		this.entry = entry;
		this.jdbcResultSetGetter = jdbcResultSetGetter;
	}


	public EntryRowMapper(JFishMappedEntry entry, JdbcResultSetGetter jdbcResultSetGetter, boolean debug) {
		super();
		this.entry = entry;
		this.debug = debug;
		this.jdbcResultSetGetter = jdbcResultSetGetter;
	}


	@Override
	public T mapRow(ResultSet rs, int rowNum) throws SQLException {
		Assert.state(entry!=null, "no mapping entry!");

		ResultSetWrappingSqlRowSet resutSetWrapper = new ResultSetWrappingSqlRowSet(rs);
		SqlRowSetMetaData rsmd = resutSetWrapper.getMetaData();
		int columnCount = resutSetWrapper.getMetaData().getColumnCount();

		DbmMappedField field;
		String column = null;
		T entity = entry.newInstance();
		Object value = null;
		BeanWrapper bw = null;
		
		long start = 0;
		if(debug){
			start = System.currentTimeMillis();
		}
		for (int index = 1; index <= columnCount; index++) {
			value = null;
			column = DbmUtils.lookupColumnName(rsmd, index);
			/*if(!entry.containsColumn(column))
				continue;*/
			try {
				if(entry.containsColumn(column)){
					field = entry.getFieldByColumnName(column);
					value = getColumnValue(resutSetWrapper, index, field);
					if(value!=null){
						field.setValue(entity, value);
					}
				}else{
					String propName = toPropertyName(column);
					if(bw==null){
						bw = PropertyAccessorFactory.forBeanPropertyAccess(entity);
						bw.setAutoGrowNestedPaths(true);
					}
					if(!bw.isWritableProperty(propName))
						continue;
					value = getColumnValue(resutSetWrapper, index, bw.getPropertyDescriptor(propName), rsmd.getColumnType(index));
					bw.setPropertyValue(propName, value);
//					this.setRelatedProperty(rs, index, entity, propName);
				}
			} catch (Exception e) {
				LangUtils.throwBaseException(entry.getEntityClass() + " mapped field["+column+", "+value+"] error : " + e.getMessage(), e);
			}
		}	
		
		if(debug){
			long costTime = System.currentTimeMillis()-start;
			logger.info("===>>> mapp row cost time (milliseconds): " + costTime);
		}
		
		return entity;
	}
	
	protected String toPropertyName(final String column){
		/*String propName = column;
		if(propName.contains("___")){
			propName = propName.replace("___", ".");
		}
		propName = StringUtils.toCamel(propName, false);*/
		return StringUtils.toCamel(column, false);
	}
	
	/*protected void setRelatedProperty(ResultSet rs, int index, Object entity, String propName) throws SQLException{
		Object targetEntity = entity;
		int dot = propName.lastIndexOf(".");
		if(dot!=-1){
			String parentPath = propName.substring(0, dot);
			targetEntity = ReflectUtils.getExpr(entity, parentPath, true);
			propName = propName.substring(dot+1);
		}
		PropertyDescriptor prop = ReflectUtils.findProperty(targetEntity.getClass(), propName);
		if(prop==null)
			return ;
		Object value = getColumnValue(rs, index, prop.getPropertyType());
		ReflectUtils.setProperty(targetEntity, prop, value);
//		System.out.println("tareget:" + targetEntity);
	}*/

	public JFishMappedEntry getEntry() {
		return entry;
	}


	/*protected Object getColumnValue(ResultSet rs, int index, Class<?> type) throws SQLException {
		return JdbcUtils.getResultSetValue(rs, index, type);
	}*/
	


	protected Object getColumnValue(ResultSetWrappingSqlRowSet rs, int index, PropertyDescriptor pd, int sqlType) throws SQLException {
		return jdbcResultSetGetter.getColumnValue(rs, index, pd);
		/*JFishProperty jproperty = Intro.wrap(pd.getWriteMethod().getDeclaringClass()).getJFishProperty(pd.getName(), false);
		TypeHandler<?> typeHandler = sqlTypeMapping.getTypeHander(jproperty.getType(), sqlType);
		Object value = typeHandler.getResult(rs, index);
		return value;*/
	}

	/*protected Object getColumnValue(ResultSet rs, int index, PropertyDescriptor pd, int sqlType) throws SQLException {
		JFishProperty jproperty = Intro.wrap(pd.getWriteMethod().getDeclaringClass()).getJFishProperty(pd.getName(), false);
		JFishMappedProperty mappedProperty = new JFishMappedProperty(entry, jproperty);
		return getColumnValue(rs, index, mappedProperty);
	}*/


	protected Object getColumnValue(ResultSetWrappingSqlRowSet rs, int index, DbmMappedField field) throws SQLException {
		return jdbcResultSetGetter.getColumnValue(rs, index, field);
		/*TypeHandler<?> typeHandler = sqlTypeMapping.getTypeHander(field.getColumnType(), field.getColumn().getSqlType());
		Object value = typeHandler.getResult(rs, index);
		return value;*/
	}
	/*protected Object getColumnValue(ResultSet rs, int index, DbmMappedField field) throws SQLException {
		final Object value = JdbcUtils.getResultSetValue(rs, index, field.getColumnType());
//		JFishProperty jproperty = Intro.wrap(pd.getWriteMethod().getDeclaringClass()).getJFishProperty(pd.getName(), false);
		Object actualValue = DbmUtils.convertPropertyValue(field.getPropertyInfo(), value);
		return actualValue;
	}*/
}
