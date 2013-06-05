package org.onetwo.common.fish.spring;

import org.onetwo.common.fish.orm.JFishMappedEntry;
import org.onetwo.common.fish.orm.MappedEntryManager;
import org.onetwo.common.utils.LangUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SingleColumnRowMapper;

public class DefaultRowMapperFactory implements RowMapperFactory {

	private MappedEntryManager mappedEntryManager;
	
	public DefaultRowMapperFactory(MappedEntryManager mappedEntryManager) {
		super();
		this.mappedEntryManager = mappedEntryManager;
	}

	@Override
	public <T> RowMapper<T> createDefaultRowMapper(Class<T> type) {
		RowMapper<T> rowMapper = null;
		if(type==null || type==Object.class){
//			rowMapper = new SingleColumnRowMapper(Object.class);
			rowMapper = (RowMapper<T>)new UnknowTypeRowMapper();
		}else if(LangUtils.isBaseType(type) || LangUtils.isTimeClass(type)){
			//唯一，而且返回类型是简单类型，则返回单列的RowMapper
			rowMapper = new SingleColumnRowMapper<T>(type);
		}else if(LangUtils.isMapClass(type)){
			rowMapper = (RowMapper<T>)new ColumnMapRowMapper();
		}else{
			JFishMappedEntry entry = this.getMappedEntryManager().findEntry(type);
			if(entry!=null)
				rowMapper = new EntryRowMapper(entry);
			else
				rowMapper = getBeanPropertyRowMapper(type);
		}
		return rowMapper;
	}

	public MappedEntryManager getMappedEntryManager() {
		return mappedEntryManager;
	}

	public void setMappedEntryManager(MappedEntryManager mappedEntryManager) {
		this.mappedEntryManager = mappedEntryManager;
	}

	protected <T> RowMapper<T> getBeanPropertyRowMapper(Class<T> entityClass) {
		return new BeanPropertyRowMapper(entityClass);
	}
	

}
