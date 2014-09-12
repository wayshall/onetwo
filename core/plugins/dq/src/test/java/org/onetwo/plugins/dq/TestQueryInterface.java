package org.onetwo.plugins.dq;

import java.util.List;

import org.onetwo.common.utils.Page;
import org.onetwo.plugins.dq.annotations.Name;

public interface TestQueryInterface {

	public void findPage(Page<?> page, String name);

	public List<TestBean> findList(String name);

	public TestBean findOne(String name);
	public Integer countUser(String name); 

	public int batchInsert(List<TestBean> datas);
	public int batchInsertWithNamed(@Name("data") List<TestBean> datas);
}
