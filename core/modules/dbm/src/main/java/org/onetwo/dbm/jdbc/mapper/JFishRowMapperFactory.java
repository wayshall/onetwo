package org.onetwo.dbm.jdbc.mapper;

import org.onetwo.common.db.dquery.DynamicMethod;
import org.onetwo.common.db.dquery.NamedQueryInvokeContext;
import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.common.utils.Assert;
import org.onetwo.dbm.annotation.DbmCascadeResult;
import org.onetwo.dbm.annotation.DbmRowMapper;
import org.onetwo.dbm.jdbc.JdbcResultSetGetter;
import org.onetwo.dbm.mapping.JFishMappedEntry;
import org.onetwo.dbm.mapping.MappedEntryManager;
import org.springframework.jdbc.core.RowMapper;

public class JFishRowMapperFactory extends JdbcDaoRowMapperFactory {

	private MappedEntryManager mappedEntryManager;
	private JdbcResultSetGetter jdbcResultSetGetter;
	
	public JFishRowMapperFactory(MappedEntryManager mappedEntryManager, JdbcResultSetGetter jdbcResultSetGetter) {
		super();
		this.mappedEntryManager = mappedEntryManager;
		this.jdbcResultSetGetter = jdbcResultSetGetter;
	}

	public MappedEntryManager getMappedEntryManager() {
		return mappedEntryManager;
	}

	public void setMappedEntryManager(MappedEntryManager mappedEntryManager) {
		this.mappedEntryManager = mappedEntryManager;
	}

	@SuppressWarnings("unchecked")
	protected RowMapper<?> getBeanPropertyRowMapper(Class<?> type) {
		RowMapper<?> rowMapper = null;
		if(getMappedEntryManager().isSupportedMappedEntry(type)){
			JFishMappedEntry entry = this.getMappedEntryManager().getEntry(type);
			rowMapper = new EntryRowMapper<>(entry, this.jdbcResultSetGetter);
			return rowMapper;
		}else if(type.getAnnotation(DbmRowMapper.class)!=null){
			DbmRowMapper dbmRowMapper = type.getAnnotation(DbmRowMapper.class);
			if(dbmRowMapper.value()==Void.class){
				return new DbmBeanPropertyRowMapper<>(this.jdbcResultSetGetter,  type);
			}else{
				Assert.isAssignable(RowMapper.class, dbmRowMapper.value());
				Class<? extends RowMapper<?>> rowMapperClass = (Class<? extends RowMapper<?>>)dbmRowMapper.value();
				return ReflectUtils.newInstance(rowMapperClass, type);
			}
		}else{
//			rowMapper = super.getBeanPropertyRowMapper(type);
			rowMapper = new DbmBeanPropertyRowMapper<>(this.jdbcResultSetGetter,  type);
		}
		return rowMapper;
	}
	
	@Override
	public RowMapper<?> createRowMapper(NamedQueryInvokeContext invokeContext) {
		DynamicMethod dmethod = invokeContext.getDynamicMethod();
		DbmCascadeResult dbmCascadeResult = dmethod.getMethod().getAnnotation(DbmCascadeResult.class);
		if(dbmCascadeResult==null){
			return super.createRowMapper(invokeContext);
		}
		DbmNestedBeanRowMapper<?> rowMapper = new DbmNestedBeanRowMapper<>(jdbcResultSetGetter, dmethod.getResultClass(), dbmCascadeResult);
		return rowMapper;
	}
	
}
