package org.onetwo.boot.jdbc;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;

public class JdbcContextConfig {
	
	@Autowired
	private DataSource dataSource;
	

}
