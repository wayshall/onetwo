package org.onetwo.common.ds;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Map;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.onetwo.common.spring.SpringApplication;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.Ordered;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

public class JFishMultipleDatasource implements DataSource, Ordered, InitializingBean, ApplicationContextAware {

//	public static final String DATASOURCE_KEY = "DataSource";
	public static final String DEFAULT_MASTER_NAME = SwitcherInfo.DEFAULT_SWITCHER_NAME;// + DATASOURCE_KEY;
	
	private Map<String, DataSource> datasources;
	private ContextHolder contextHolder;
	private String masterName;
	private DataSource masterDatasource;
	private boolean masterSlave = true;
	
	private ApplicationContext applicationContext;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notEmpty(datasources, "datasources can not be empty.");
		if(masterDatasource==null){
			if(StringUtils.isBlank(masterName))
				masterName = DEFAULT_MASTER_NAME;
//			Assert.hasText(masterName, "masterName can not be empty.");
			this.masterDatasource = datasources.get(masterName);
		}
		Assert.notNull(masterDatasource, "master datasource can not be null: " + masterName);
		
		if(contextHolder==null){
			contextHolder = SpringUtils.getBean(applicationContext, ContextHolder.class);
		}
		
		Assert.notNull(contextHolder, "contextHolder can not be null.");
	}

	public DataSource getCurrentDatasource(){
		if(!masterSlave)
			return masterDatasource;
		
		SwitcherInfo switcher = contextHolder.getContextAttribute(SwitcherInfo.CURRENT_SWITCHER_INFO);
		DataSource ds = masterDatasource;
		if(datasources!=null && switcher!=null && StringUtils.isNotBlank(switcher.getCurrentSwitcherName())){
			switch (switcher.getType()) {
			case TransactionManager:
				PlatformTransactionManager pt = (PlatformTransactionManager)SpringApplication.getInstance().getBean(switcher.getCurrentSwitcherName());
				if(HibernateTransactionManager.class.isInstance(pt)){
					ds = ((HibernateTransactionManager)pt).getDataSource();
				}else if(DataSourceTransactionManager.class.isInstance(pt)){
					ds = ((DataSourceTransactionManager)pt).getDataSource();
				}else{
					throw new UnsupportedOperationException("TransactionManager: " + switcher.getCurrentSwitcherName());
				}
				break;

			default:
				String dsName = switcher.getCurrentSwitcherName();// + DATASOURCE_KEY;
				if(datasources.containsKey(dsName))
					ds = datasources.get(dsName);
				break;
			}
			
		}
		return ds;
	}
	
	public void setMasterDatasource(DataSource masterDatasource) {
		this.masterDatasource = masterDatasource;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	public void setMasterSlave(boolean masterSlave) {
		this.masterSlave = masterSlave;
	}

	public void setMasterName(String masterName) {
		this.masterName = masterName;
	}

	public Map<String, DataSource> getDatasources() {
		return datasources;
	}

	public void setDatasources(Map<String, DataSource> datasources) {
		this.datasources = datasources;
	}

	public ContextHolder getContextHolder() {
		return contextHolder;
	}

	public void setContextHolder(ContextHolder contextHolder) {
		this.contextHolder = contextHolder;
	}

	public Connection getConnection() throws SQLException {
		return getCurrentDatasource().getConnection();
	}

	public Connection getConnection(String username, String password)
			throws SQLException {
		return getCurrentDatasource().getConnection(username, password);
	}

	public PrintWriter getLogWriter() throws SQLException {
		return getCurrentDatasource().getLogWriter();
	}

	public int getLoginTimeout() throws SQLException {
		return getCurrentDatasource().getLoginTimeout();
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T unwrap(Class<T> iface) throws SQLException {
		if (iface.isInstance(this)) {
			return (T) this;
		}
		return getCurrentDatasource().unwrap(iface);
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return (iface.isInstance(this) || getCurrentDatasource().isWrapperFor(iface));
	}
	public void setLogWriter(PrintWriter arg0) throws SQLException {
		getCurrentDatasource().setLogWriter(arg0);
	}

	public void setLoginTimeout(int arg0) throws SQLException {
		getCurrentDatasource().setLoginTimeout(arg0);
	}


	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE;
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		return getCurrentDatasource().getParentLogger();
	}


}
