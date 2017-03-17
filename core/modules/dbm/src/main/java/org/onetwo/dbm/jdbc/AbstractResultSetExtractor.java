package org.onetwo.dbm.jdbc;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.Assert;

public class AbstractResultSetExtractor<T> {
	protected final RowMapper<T> rowMapper;

	public AbstractResultSetExtractor(RowMapper<T> rowMapper) {
		super();
		Assert.notNull(rowMapper, "RowMapper is required");
		this.rowMapper = rowMapper;
	}

	public RowMapper<T> getRowMapper() {
		return rowMapper;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((rowMapper == null) ? 0 : rowMapper.hashCode());
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
		AbstractResultSetExtractor<?> other = (AbstractResultSetExtractor<?>) obj;
		if (rowMapper == null) {
			if (other.rowMapper != null)
				return false;
		} else if (!rowMapper.equals(other.rowMapper))
			return false;
		return true;
	}
	
}
