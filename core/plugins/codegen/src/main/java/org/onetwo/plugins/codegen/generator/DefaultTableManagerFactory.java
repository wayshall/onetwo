package org.onetwo.plugins.codegen.generator;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;

import org.onetwo.common.spring.config.JFishPropertyPlaceholder;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.plugins.codegen.model.entity.DatabaseEntity;
import org.springframework.beans.factory.annotation.Autowired;

public class DefaultTableManagerFactory {
	
	public static final long DEFAULT_DB = -1;
	
	private Map<Long, DefaultTableManager> tableManagerCaches = new ConcurrentHashMap<Long, DefaultTableManager>();;
	
	@Autowired
	private DefaultDataSourceFactory dataSourceFactory;

	@Autowired
	private TableComponentFacotry tableComponentFacotry;
	
//	@Resource
//	private JFishDaoImplementor JFishDaoImplementor;
	@Resource
	private DataSource dataSource;
	
	private DefaultTableManager defaultTableManager;
	private DatabaseEntity defaultDataBase;


	@Autowired
	private JFishPropertyPlaceholder configHolder;


	public DefaultTableManagerFactory() {
		super();
	}

	@PostConstruct
	public void init(){
		this.defaultDataBase = createDefaultDb();
		DefaultTableManager tm = new DefaultTableManager();
		tm.setDataBase(this.defaultDataBase.getDataBase());
		tm.setDataSource(dataSource);
		tm.setTableComponentFacotry(tableComponentFacotry);
		
		this.defaultTableManager = tm;
		
	}

	private DatabaseEntity createDefaultDb(){
		DatabaseEntity db = new DatabaseEntity();
		
		String driverClass = this.configHolder.getPropertiesWraper().getProperty("jdbc.driverClass");
		
		String jdbcUrl = this.configHolder.getPropertiesWraper().getProperty("jdbc.url");
		if(StringUtils.isBlank(jdbcUrl))
			jdbcUrl = this.configHolder.getPropertiesWraper().getProperty("jdbc.jdbcUrl");
		
		String userName = this.configHolder.getPropertiesWraper().getProperty("jdbc.username");
		if(StringUtils.isBlank(userName))
			userName = this.configHolder.getPropertiesWraper().getProperty("jdbc.user");
		
		String password = this.configHolder.getPropertiesWraper().getProperty("jdbc.password");;
		
		db.setId(DefaultTableManagerFactory.DEFAULT_DB);
		db.setDriverClass(driverClass);
		db.setJdbcUrl(jdbcUrl);
		db.setUsername(userName);
		db.setPassword(password);
		db.setLabel("webapp");
		return db;
	}
	
	
	public DefaultTableManager createTableManager(DatabaseEntity db){
		DefaultTableManager tm = tableManagerCaches.get(db.getId());
		if(tm!=null)
			return tm;
		DataSource ds = dataSourceFactory.create(db);
		tm = new DefaultTableManager();
		tm.setDataSource(ds);
		tm.setDataBase(db.getDataBase());
		tm.setTableComponentFacotry(tableComponentFacotry);
		tableManagerCaches.put(db.getId(), tm);
		return tm;
	}

	public void setDataSourceFactory(DefaultDataSourceFactory dataSourceFactory) {
		this.dataSourceFactory = dataSourceFactory;
	}

	public void setTableComponentFacotry(TableComponentFacotry tableComponentFacotry) {
		this.tableComponentFacotry = tableComponentFacotry;
	}
	public DefaultTableManager getDefaultTableManager() {
		return defaultTableManager;
	}

	public DatabaseEntity getDefaultDataBase() {
		return defaultDataBase;
	}


}
