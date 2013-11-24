package org.onetwo.common.ds;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import javax.sql.DataSource;

import org.onetwo.common.fish.utils.ContextHolder;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class JFishMultipleDatasource implements DataSource, InitializingBean, ApplicationContextAware {
	
	public static final String DEFAULT_MASTER_NAME = DataSourceSwitcherInfo.DEFAULT_INFO.getCurrentDatasourceName();
	
	private Map<String, DataSource> datasources;
	private ContextHolder contextHolder;
	private String masterName;
	private DataSource masterDatasource;
	private boolean masterSlave = true;
	
	private ApplicationContext applicationContext;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		if(StringUtils.isBlank(masterName))
			masterName = DEFAULT_MASTER_NAME;
		
		Assert.notEmpty(datasources, "datasources can not be empty.");
		if(masterDatasource==null){
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
		
		DataSourceSwitcherInfo switcher = contextHolder.getContextAttribute(DataSourceSwitcherInfo.CURRENT_DATASOURCE_KEY);
		DataSource ds = null;
		if(datasources!=null && switcher!=null && datasources.containsKey(switcher.getCurrentDatasourceName())){
			ds = datasources.get(switcher.getCurrentDatasourceName());
		}else{
			ds = masterDatasource;
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

	public boolean isWrapperFor(Class<?> arg0) throws SQLException {
		return getCurrentDatasource().isWrapperFor(arg0);
	}

	public void setLogWriter(PrintWriter arg0) throws SQLException {
		getCurrentDatasource().setLogWriter(arg0);
	}

	public void setLoginTimeout(int arg0) throws SQLException {
		getCurrentDatasource().setLoginTimeout(arg0);
	}

	public <T> T unwrap(Class<T> arg0) throws SQLException {
		return getCurrentDatasource().unwrap(arg0);
	}


}
