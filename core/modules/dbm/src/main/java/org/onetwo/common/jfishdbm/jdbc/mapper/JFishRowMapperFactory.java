package org.onetwo.common.jfishdbm.jdbc.mapper;

import org.onetwo.common.jfishdbm.annotation.DbmRowMapper;
import org.onetwo.common.jfishdbm.mapping.JFishMappedEntry;
import org.onetwo.common.jfishdbm.mapping.MappedEntryManager;
import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.common.utils.Assert;
import org.springframework.jdbc.core.RowMapper;

public class JFishRowMapperFactory extends JdbcDaoRowMapperFactory {

	private MappedEntryManager mappedEntryManager;
	
	public JFishRowMapperFactory(MappedEntryManager mappedEntryManager) {
		super();
		this.mappedEntryManager = mappedEntryManager;
	}

	public MappedEntryManager getMappedEntryManager() {
		return mappedEntryManager;
	}

	public void setMappedEntryManager(MappedEntryManager mappedEntryManager) {
		this.mappedEntryManager = mappedEntryManager;
	}

	protected RowMapper<?> getBeanPropertyRowMapper(Class<?> type) {
		RowMapper<?> rowMapper = null;
		if(getMappedEntryManager().isSupportedMappedEntry(type)){
			JFishMappedEntry entry = this.getMappedEntryManager().getEntry(type);
			rowMapper = new EntryRowMapper<>(entry);
			return rowMapper;
		}else if(type.getAnnotation(DbmRowMapper.class)!=null){
			DbmRowMapper dbmRowMapper = type.getAnnotation(DbmRowMapper.class);
			if(dbmRowMapper.value()==Void.class){
				return new DbmBeanPropertyRowMapper<>(type);
			}else{
				Assert.isAssignable(RowMapper.class, dbmRowMapper.value());
				Class<? extends RowMapper<?>> rowMapperClass = (Class<? extends RowMapper<?>>)dbmRowMapper.value();
				return ReflectUtils.newInstance(rowMapperClass, type);
			}
		}else{
			rowMapper = super.getBeanPropertyRowMapper(type);
		}
		return rowMapper;
	}
	
	
	

}
