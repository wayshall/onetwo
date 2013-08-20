package org.onetwo.plugin.hibernate;

import java.util.List;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.onetwo.common.db.BaseEntityManager;
import org.onetwo.common.db.sqlext.DefaultSQLDialetImpl;
import org.onetwo.common.db.sqlext.DefaultSQLSymbolManagerImpl;
import org.onetwo.common.db.sqlext.ExtQueryListener;
import org.onetwo.common.db.sqlext.SQLSymbolManager;
import org.onetwo.common.hibernate.HibernateEntityManagerImpl;
import org.onetwo.common.jdbc.JdbcDao;
import org.onetwo.common.spring.SpringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@Configuration
@ComponentScan(basePackageClasses=HibernatePlugin.class)
public class HibernatePluginContext  {
	
	@Resource
	private DataSource dataSource;
	
	@Resource
	private ApplicationContext applicationContext;
	
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
	
	@Bean
	public SQLSymbolManager SqlSymbolManager(){
		List<ExtQueryListener> listeners = SpringUtils.getBeans(applicationContext, ExtQueryListener.class);
		DefaultSQLSymbolManagerImpl hibernate = new DefaultSQLSymbolManagerImpl(new DefaultSQLDialetImpl());
		hibernate.setListeners(listeners);
		return hibernate;
	}

	
	
	
}
