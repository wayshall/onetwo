package org.onetwo.common.db.builder;

import java.util.List;

import org.onetwo.common.utils.Page;

public interface EMQueryBuilder extends QueryBuilder {

	public <T> T one();

	public <T> List<T> list();
	
	public <T> void page(Page<T> page);
	
	public int execute();
}
