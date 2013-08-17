package org.onetwo.plugin.hibernate;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.onetwo.common.db.BaseEntityManager;
import org.onetwo.common.hibernate.HibernateEntityManagerImpl;
import org.onetwo.common.jdbc.JdbcDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@Configuration
@ComponentScan(basePackageClasses=HibernatePlugin.class)
public class HibernatePluginContext {
	
	@Resource
	private DataSource dataSource;
	
	@Bean
	public JdbcDao jdbcDao(){
		JdbcDao jdbcDao = new JdbcDao();
		jdbcDao.setDataSource(dataSource);
		return jdbcDao;
	}
	
	@Bean
	public BaseEntityManager baseEntityManager(){
		return new HibernateEntityManagerImpl();
	}
	
}
