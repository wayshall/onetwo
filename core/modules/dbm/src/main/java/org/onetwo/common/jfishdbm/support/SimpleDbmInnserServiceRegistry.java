package org.onetwo.common.jfishdbm.support;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang3.ArrayUtils;
import org.onetwo.common.db.DataBase;
import org.onetwo.common.db.filter.annotation.DataQueryFilterListener;
import org.onetwo.common.db.sql.SequenceNameManager;
import org.onetwo.common.db.sqlext.SQLSymbolManager;
import org.onetwo.common.jfishdbm.dialet.AbstractDBDialect.DBMeta;
import org.onetwo.common.jfishdbm.dialet.DBDialect;
import org.onetwo.common.jfishdbm.dialet.DbmetaFetcher;
import org.onetwo.common.jfishdbm.dialet.DefaultDatabaseDialetManager;
import org.onetwo.common.jfishdbm.dialet.MySQLDialect;
import org.onetwo.common.jfishdbm.dialet.OracleDialect;
import org.onetwo.common.jfishdbm.jdbc.mapper.JFishRowMapperFactory;
import org.onetwo.common.jfishdbm.jdbc.mapper.RowMapperFactory;
import org.onetwo.common.jfishdbm.jpa.JFishSequenceNameManager;
import org.onetwo.common.jfishdbm.jpa.JPAMappedEntryBuilder;
import org.onetwo.common.jfishdbm.mapping.DataBaseConfig;
import org.onetwo.common.jfishdbm.mapping.DefaultDataBaseConfig;
import org.onetwo.common.jfishdbm.mapping.EntityValidator;
import org.onetwo.common.jfishdbm.mapping.JFishMappedEntryBuilder;
import org.onetwo.common.jfishdbm.mapping.MappedEntryBuilder;
import org.onetwo.common.jfishdbm.mapping.MappedEntryManager;
import org.onetwo.common.jfishdbm.mapping.MutilMappedEntryManager;
import org.onetwo.common.jfishdbm.query.JFishSQLSymbolManagerImpl;
import org.onetwo.common.utils.LangUtils;
import org.springframework.util.Assert;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;

public class SimpleDbmInnserServiceRegistry {
	
	final private Map<String, Object> services = Maps.newConcurrentMap();

	private DBDialect dialect;
	private MappedEntryManager mappedEntryManager;
	private SQLSymbolManager sqlSymbolManager;
	private SequenceNameManager sequenceNameManager;
	private DefaultDatabaseDialetManager databaseDialetManager;
	protected DataBaseConfig dataBaseConfig;
	private RowMapperFactory rowMapperFactory;
	private EntityValidator entityValidator;
	
	public void initialize(DataSource dataSource, String[] packagesToScan){
		if(dataBaseConfig==null){
			dataBaseConfig = new DefaultDataBaseConfig();
		}
		if(databaseDialetManager==null){
			this.databaseDialetManager = new DefaultDatabaseDialetManager();
			this.databaseDialetManager.register(DataBase.MySQL.getName(), new MySQLDialect());
			this.databaseDialetManager.register(DataBase.Oracle.getName(), new OracleDialect());
		}
		
		
//		super.initDao();
		if(this.dialect==null){
			DBMeta dbmeta = DbmetaFetcher.create(dataSource).getDBMeta();
//			this.dialect = JFishSpringUtils.getMatchDBDiaclet(applicationContext, dbmeta);
			this.dialect = this.databaseDialetManager.getRegistered(dbmeta.getDbName());
			if (this.dialect == null) {
				throw new IllegalArgumentException("'dialect' is required");
			}
//			LangUtils.cast(dialect, InnerDBDialet.class).setDbmeta(dbmeta);
			this.dialect.getDbmeta().setVersion(dbmeta.getVersion());
			this.dialect.initialize();
		}
		
		//mappedEntryManager
		if(mappedEntryManager==null){
			MutilMappedEntryManager _mappedEntryManager = new MutilMappedEntryManager();
//			this.mappedEntryManager.initialize();
			
			List<MappedEntryBuilder> builders = LangUtils.newArrayList();
			MappedEntryBuilder builder = new JFishMappedEntryBuilder(this);
			builder.initialize();
			builders.add(builder);
			
			builder = new JPAMappedEntryBuilder(this);
			builder.initialize();
			builders.add(builder);
//			this.mappedEntryBuilders = ImmutableList.copyOf(builders);
			_mappedEntryManager.setMappedEntryBuilder(ImmutableList.copyOf(builders));
			this.mappedEntryManager = _mappedEntryManager;
			
		}
		if(ArrayUtils.isNotEmpty(packagesToScan)){
			mappedEntryManager.scanPackages(packagesToScan);
		}
		
		//init sql symbol
		if(sqlSymbolManager==null){
			JFishSQLSymbolManagerImpl newSqlSymbolManager = JFishSQLSymbolManagerImpl.create();
//			newSqlSymbolManager.setDialect(dialect);
			newSqlSymbolManager.setMappedEntryManager(mappedEntryManager);
			newSqlSymbolManager.setListeners(Arrays.asList(new DataQueryFilterListener()));
			this.sqlSymbolManager = newSqlSymbolManager;
		}
		
//		this.mappedEntryManager = SpringUtils.getHighestOrder(applicationContext, MappedEntryManager.class);
		this.rowMapperFactory = new JFishRowMapperFactory(mappedEntryManager);

		if(this.sequenceNameManager==null){
			this.sequenceNameManager = new JFishSequenceNameManager();
		}
	}
	

	public DBDialect getDialect() {
		return dialect;
	}



	public MappedEntryManager getMappedEntryManager() {
		return mappedEntryManager;
	}

	public SQLSymbolManager getSqlSymbolManager() {
		return sqlSymbolManager;
	}

	public SequenceNameManager getSequenceNameManager() {
		return sequenceNameManager;
	}

	public DefaultDatabaseDialetManager getDatabaseDialetManager() {
		return databaseDialetManager;
	}

	public DataBaseConfig getDataBaseConfig() {
		return dataBaseConfig;
	}

	public RowMapperFactory getRowMapperFactory() {
		return rowMapperFactory;
	}

	public <T> T getService(Class<T> clazz) {
		Assert.notNull(clazz);
		return clazz.cast(getService(clazz.getName()));
	}

	public <T> T getService(String name) {
		Assert.hasText(name);
		return (T) services.get(name);
	}

	public <T> SimpleDbmInnserServiceRegistry register(T service) {
		return register(service.getClass().getName(), service);
	}

	public <T> SimpleDbmInnserServiceRegistry register(String name, T service) {
		Assert.hasText(name);
		Assert.notNull(service);
		services.put(name, service);
		return this;
	}

	public EntityValidator getEntityValidator() {
		return entityValidator;
	}


	public void setEntityValidator(EntityValidator entityValidator) {
		this.entityValidator = entityValidator;
	}
	

}
