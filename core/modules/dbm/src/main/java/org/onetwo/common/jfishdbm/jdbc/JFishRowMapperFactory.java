package org.onetwo.common.jfishdbm.jdbc;

import org.onetwo.common.jfishdbm.mapping.JFishMappedEntry;
import org.onetwo.common.jfishdbm.mapping.MappedEntryManager;
import org.springframework.jdbc.core.RowMapper;

public class JFishRowMapperFactory extends SimpleRowMapperFactory {

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
		}else{
			rowMapper = super.getBeanPropertyRowMapper(type);
		}
		return rowMapper;
	}
	

}
