package org.onetwo.plugins.dq;

import java.util.List;

import org.onetwo.common.utils.Page;

public interface TestQueryInterface {

	public Page<TestBean> findPage(Page<?> page, String name);

	public List<TestBean> findList(String name);

	public TestBean findOne(String name);
	public Integer countUser(String name);
}
