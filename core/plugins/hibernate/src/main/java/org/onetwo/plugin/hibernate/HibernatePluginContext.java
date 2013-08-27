package org.onetwo.plugin.hibernate;

import java.util.List;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.onetwo.common.db.BaseEntityManager;
import org.onetwo.common.db.sqlext.ExtQueryListener;
import org.onetwo.common.db.sqlext.SQLSymbolManager;
import org.onetwo.common.db.sqlext.SQLSymbolManagerFactory;
import org.onetwo.common.hibernate.HibernateEntityManagerImpl;
import org.onetwo.common.hibernate.listener.TimestampEventListener;
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
	private SessionFactory sessionFactory; 
	
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
		HibernateEntityManagerImpl em = new HibernateEntityManagerImpl();
		em.setSessionFactory(sessionFactory);
		return em;
	}
	
	@Bean
	public SQLSymbolManager SqlSymbolManager(){
		SQLSymbolManager symbolManager = SpringUtils.getBean(applicationContext, SQLSymbolManager.class);
		if(symbolManager==null){
			symbolManager = SQLSymbolManagerFactory.getInstance().get(baseEntityManager().getEntityManagerProvider());
			List<ExtQueryListener> listeners = SpringUtils.getBeans(applicationContext, ExtQueryListener.class);
			symbolManager.setListeners(listeners);
		}
		return symbolManager;
	}

	@Bean
	public TimestampEventListener timestampEventListener(){
		return new TimestampEventListener();
	}
	
	
}
