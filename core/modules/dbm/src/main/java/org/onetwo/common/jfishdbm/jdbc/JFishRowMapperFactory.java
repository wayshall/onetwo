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
		JFishMappedEntry entry = this.getMappedEntryManager().findEntry(type);
		if(entry!=null)
			rowMapper = new EntryRowMapper<>(entry);
		else
			rowMapper = super.getBeanPropertyRowMapper(type);
		return rowMapper;
	}
	

}
