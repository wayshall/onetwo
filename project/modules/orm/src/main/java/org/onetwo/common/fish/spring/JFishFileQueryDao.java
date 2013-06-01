package org.onetwo.common.fish.spring;

import java.util.List;

import org.onetwo.common.db.ParamValues.PlaceHolder;
import org.onetwo.common.fish.JFishQuery;
import org.onetwo.common.utils.Page;
import org.springframework.jdbc.core.RowMapper;

public interface JFishFileQueryDao {

	public JFishQuery createJFishQueryByQName(String queryName, Object... args);

	public JFishQuery createJFishQueryByQName(String queryName, PlaceHolder type, Object... args);

	public JFishQuery createCountJFishQueryByQName(String queryName, Object... args);

	public <T> List<T> findListByQName(String queryName, Object... params);

	public <T> T findUniqueByQName(String queryName, Object... params);

	public <T> Page<T> findPageByQName(String queryName, Page<T> page, Object... params);
	
	public <T> Page<T> findPageByQName(String queryName, RowMapper<T> rowMapper, Page<T> page, Object... params);

}