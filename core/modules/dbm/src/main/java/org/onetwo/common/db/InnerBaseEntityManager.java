package org.onetwo.common.db;

import java.util.List;

import org.onetwo.common.db.sqlext.SelectExtQuery;
import org.onetwo.common.utils.Page;

public interface InnerBaseEntityManager extends BaseEntityManager {

	public <T> List<T> select(SelectExtQuery extQuery);
	public <T> T selectUnique(SelectExtQuery extQuery);
	public <T> T selectOne(SelectExtQuery extQuery);
	public void selectPage(Page<?> page, SelectExtQuery extQuery);
	

	public <T> List<T> findList(JFishQueryValue queryValue);
	public <T> T findUnique(JFishQueryValue queryValue);
}
