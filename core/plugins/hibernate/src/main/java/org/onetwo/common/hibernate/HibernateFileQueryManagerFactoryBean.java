package org.onetwo.common.hibernate;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.onetwo.common.db.BaseEntityManager;
import org.onetwo.common.db.FileNamedQueryFactory;
import org.onetwo.common.db.FileNamedQueryFactoryListener;
import org.onetwo.common.hibernate.sql.HibernateFileQueryManagerImpl;
import org.onetwo.common.hibernate.sql.HibernateNamedInfo;
import org.onetwo.common.jdbc.DataBase;
import org.onetwo.common.jdbc.JdbcUtils;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.spring.config.JFishPropertyPlaceholder;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class HibernateFileQueryManagerFactoryBean implements FactoryBean<FileNamedQueryFactory<?>>, InitializingBean {


	@Resource
	private ApplicationContext applicationContext;
	
	private DataSource dataSource;
	@Autowired
	private JFishPropertyPlaceholder configHolder;
	
	private FileNamedQueryFactory<HibernateNamedInfo> fileNamedQueryFactory;
	
	@Resource
	private BaseEntityManager baseEntityManager;
	
	@Override
	public void afterPropertiesSet() throws Exception {
//		Assert.notNull(appConfig, "appConfig can not be null.");
		boolean watchSqlFile = configHolder.getPropertiesWraper().getBoolean(FileNamedQueryFactory.WATCH_SQL_FILE);
		DataBase db = JdbcUtils.getDataBase(dataSource);
		FileNamedQueryFactoryListener listener = SpringUtils.getBean(applicationContext, FileNamedQueryFactoryListener.class);
		FileNamedQueryFactory<HibernateNamedInfo> fq = new HibernateFileQueryManagerImpl(db, watchSqlFile, baseEntityManager, listener);
		fq.initQeuryFactory(baseEntityManager);
		this.fileNamedQueryFactory = fq;
	}

	@Override
	public FileNamedQueryFactory<?> getObject() throws Exception {
		return fileNamedQueryFactory;
	}

	@Override
	public Class<?> getObjectType() {
		return HibernateFileQueryManagerImpl.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

}
