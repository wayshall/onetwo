package org.onetwo.plugins.dq;

import java.util.List;

import org.onetwo.common.utils.Page;

public interface TestQueryInterface {

	public Page<TestBean> findPage(String name);

	public List<TestBean> findList(String name);

	public TestBean findOne(String name);
}
