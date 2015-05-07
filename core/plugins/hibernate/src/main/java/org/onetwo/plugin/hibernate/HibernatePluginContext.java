package org.onetwo.plugin.hibernate;

import java.util.List;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.onetwo.common.db.BaseEntityManager;
import org.onetwo.common.db.DataQueryFilterListener;
import org.onetwo.common.db.FileNamedQueryFactory;
import org.onetwo.common.db.FileNamedQueryFactoryListener;
import org.onetwo.common.db.sqlext.DefaultSQLDialetImpl;
import org.onetwo.common.db.sqlext.ExtQueryListener;
import org.onetwo.common.db.sqlext.SQLSymbolManager;
import org.onetwo.common.hibernate.HibernateEntityManagerImpl;
import org.onetwo.common.hibernate.HibernateUtils;
import org.onetwo.common.hibernate.TableGeneratorService;
import org.onetwo.common.hibernate.TableGeneratorServiceImpl;
import org.onetwo.common.hibernate.listener.TimestampEventListener;
import org.onetwo.common.hibernate.msf.JFishMultipleSessionFactory;
import org.onetwo.common.hibernate.sql.HibernateFileQueryManagerImpl;
import org.onetwo.common.hibernate.sql.HibernateSQLSymbolManagerImpl;
import org.onetwo.common.jdbc.DataBase;
import org.onetwo.common.jdbc.JdbcDao;
import org.onetwo.common.jdbc.JdbcUtils;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.spring.config.JFishPropertyPlaceholder;
import org.onetwo.common.spring.sql.JFishNamedFileQueryInfo;
import org.onetwo.common.spring.sql.JFishNamedSqlFileManager;
import org.onetwo.common.spring.sql.StringTemplateLoaderFileSqlParser;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;


@Configuration
public class HibernatePluginContext implements InitializingBean  {
	
//	@Resource
	private DataSource dataSource;
	
	private SessionFactory sessionFactory; 
	
	@Resource
	private ApplicationContext applicationContext;

	@Autowired
	private JFishPropertyPlaceholder configHolder;
	
	
	@Override
	public void afterPropertiesSet() throws Exception {
		this.dataSource = SpringUtils.getBean(applicationContext, DataSource.class);
		SessionFactory sf = SpringUtils.getBean(applicationContext, JFishMultipleSessionFactory.class);
		if(sf==null)
			sf = SpringUtils.getBean(applicationContext, SessionFactory.class);
		HibernateUtils.initSessionFactory(sf);
		this.sessionFactory = sf;
	}

	@Bean
	public TableGeneratorService tableGeneratorService(){
		return new TableGeneratorServiceImpl();
	}
	@Bean
	public JdbcDao jdbcDao(){
		JdbcDao jdbcDao = new JdbcDao();
		jdbcDao.setDataSource(dataSource);
		return jdbcDao;
	}
	
	@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE)
	public BaseEntityManager baseEntityManager(){
		HibernateEntityManagerImpl em = new HibernateEntityManagerImpl();
		em.setSessionFactory(sessionFactory);
//		em.setDataSource(dataSource);
		em.setSqlSymbolManager(sqlSymbolManager());
		em.setFileNamedQueryFactory(fileNamedQueryFactory());
		return em;
	}
	
	@Bean
	public SQLSymbolManager sqlSymbolManager(){
//		SQLSymbolManager symbolManager = SpringUtils.getBean(applicationContext, SQLSymbolManager.class);
		SQLSymbolManager symbolManager = new HibernateSQLSymbolManagerImpl(new DefaultSQLDialetImpl());//SQLSymbolManagerFactory.getInstance().get(EntityManagerProvider.Hibernate);
		List<ExtQueryListener> listeners = SpringUtils.getBeans(applicationContext, ExtQueryListener.class);
		symbolManager.setListeners(listeners);
		return symbolManager;
	}
	
	@Bean
	public ExtQueryListener dataQueryFilterListener(){
		return new DataQueryFilterListener();
	}
	
	/*@Bean
	public HibernateFileQueryManagerFactoryBean fileQueryManagerFactoryBean(){
		HibernateFileQueryManagerFactoryBean fb = new HibernateFileQueryManagerFactoryBean();
		fb.setDataSource(dataSource);
		return fb;
	}*/
	@Bean
	public FileNamedQueryFactory<JFishNamedFileQueryInfo> fileNamedQueryFactory(){
//		Assert.notNull(appConfig, "appConfig can not be null.");
//		boolean watchSqlFile = configHolder.getPropertiesWraper().getBoolean(FileNamedQueryFactory.WATCH_SQL_FILE);
//		DataBase db = JdbcUtils.getDataBase(dataSource);
		FileNamedQueryFactoryListener listener = SpringUtils.getBean(applicationContext, FileNamedQueryFactoryListener.class);
		FileNamedQueryFactory<JFishNamedFileQueryInfo> fq = new HibernateFileQueryManagerImpl(sqlFileManager(), listener);
		fq.initQeuryFactory(baseEntityManager());
		return fq;
	}
	
	@Bean
	public JFishNamedSqlFileManager<JFishNamedFileQueryInfo> sqlFileManager() {
		boolean watchSqlFile = configHolder.getPropertiesWraper().getBoolean(FileNamedQueryFactory.WATCH_SQL_FILE);
		DataBase db = JdbcUtils.getDataBase(dataSource);
//		FileNamedQueryFactoryListener listener = SpringUtils.getBean(applicationContext, FileNamedQueryFactoryListener.class);
		StringTemplateLoaderFileSqlParser<JFishNamedFileQueryInfo> listener = new StringTemplateLoaderFileSqlParser<JFishNamedFileQueryInfo>();
		JFishNamedSqlFileManager<JFishNamedFileQueryInfo> sqlfileMgr = JFishNamedSqlFileManager.createDefaultJFishNamedSqlFileManager(db, watchSqlFile, listener);
		return sqlfileMgr;
	}

	@Bean
	public TimestampEventListener timestampEventListener(){
		return new TimestampEventListener();
	}
	
	@Bean
	public Module hibernateModule(){
		return new Hibernate4Module();
	}
	
}
