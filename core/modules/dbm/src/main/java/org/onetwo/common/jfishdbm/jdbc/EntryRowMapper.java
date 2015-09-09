package org.onetwo.common.jfishdbm.jdbc;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.onetwo.common.jfishdbm.mapping.JFishMappedEntry;
import org.onetwo.common.jfishdbm.mapping.JFishMappedField;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;

public class EntryRowMapper<T> implements RowMapper<T>{
	
	protected final Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	
	private JFishMappedEntry entry;
	private boolean debug = false;
	
	public EntryRowMapper(JFishMappedEntry entry) {
		super();
		this.entry = entry;
	}


	public EntryRowMapper(JFishMappedEntry entry, boolean debug) {
		super();
		this.entry = entry;
		this.debug = debug;
	}


	@Override
	public T mapRow(ResultSet rs, int rowNum) throws SQLException {
		Assert.state(entry!=null, "no mapping entry!");
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount();

		JFishMappedField field;
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
			column = JdbcUtils.lookupColumnName(rsmd, index);
			/*if(!entry.containsColumn(column))
				continue;*/
			try {
				if(entry.containsColumn(column)){
					field = entry.getFieldByColumnName(column);
					value = getColumnValue(rs, index, field.getColumnType());
					if(value!=null){
						field.setColumnValue(entity, value);
					}
				}else{
					String propName = toPropertyName(column);
					if(bw==null){
						bw = PropertyAccessorFactory.forBeanPropertyAccess(entity);
						bw.setAutoGrowNestedPaths(true);
					}
					if(!bw.isWritableProperty(propName))
						continue;
					value = getColumnValue(rs, index, bw.getPropertyType(propName));
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


	protected Object getColumnValue(ResultSet rs, int index, Class<?> type) throws SQLException {
		return JdbcUtils.getResultSetValue(rs, index, type);
	}

}
