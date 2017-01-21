/**
 * Copyright (c) 2005-2012 springside.org.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package org.onetwo.common.spring.test;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.junit.Before;
import org.onetwo.common.spring.Springs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

public abstract class SpringBaseJUnitTestCase extends AbstractTransactionalJUnit4SpringContextTests {

	protected DataSource dataSource;
	
	@Resource
	protected ApplicationContext applicationContext;

	@Before
	public void setupOnApplication(){
		Springs.initApplication(applicationContext);
	}
	
	@Override
	@Autowired
	public void setDataSource(DataSource dataSource) {
		super.setDataSource(dataSource);
		this.dataSource = dataSource;
	}

}
