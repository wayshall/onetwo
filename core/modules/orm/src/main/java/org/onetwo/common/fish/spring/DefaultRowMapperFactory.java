package org.onetwo.common.fish.spring;

import java.util.Collection;
import java.util.List;

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
	public RowMapper<?> createDefaultRowMapper(Class<?> type) {
		RowMapper<?> rowMapper = null;
		if(type==null || type==Object.class){
//			rowMapper = new SingleColumnRowMapper(Object.class);
			rowMapper = new UnknowTypeRowMapper();
		}else if(LangUtils.isBaseType(type) || LangUtils.isTimeClass(type)){
			//唯一，而且返回类型是简单类型，则返回单列的RowMapper
			rowMapper = new SingleColumnRowMapper(type);
		}else if(LangUtils.isMapClass(type)){
			rowMapper = new ColumnMapRowMapper();
		}else if(Object[].class==type){
			rowMapper = new ObjectArrayRowMapper();
		}else if(List.class.isAssignableFrom(type)){
			rowMapper = new ListRowMapper();
		}else if(Collection.class.isAssignableFrom(type)){
			rowMapper = new HashsetRowMapper();
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
