package org.onetwo.common.jfishdbm.mapping;

import org.onetwo.common.db.FileNamedQueryFactory;
import org.onetwo.common.spring.config.JFishPropertyPlaceholder;
import org.onetwo.common.spring.context.ApplicationConfigKeys;

public class DefaultDataBaseConfig implements DataBaseConfig {
	
	private int useBatchThreshold = 50;
	private int processSizePerBatch = 10000;
	private boolean batchEnabled = true;
	private boolean logSql = true;
	private boolean watchSqlFile = true;
	private String[] modelBasePackages;
	

	public DefaultDataBaseConfig(){
	}
	
	public DefaultDataBaseConfig(JFishPropertyPlaceholder configHolder){
		this.modelBasePackages = configHolder.getPropertiesWraper().getStringArray(ApplicationConfigKeys.BASE_PACKAGE, ",");
		this.watchSqlFile = configHolder.getPropertiesWraper().getBoolean(FileNamedQueryFactory.WATCH_SQL_FILE);
		this.logSql = configHolder.getPropertiesWraper().getBoolean(JFISHDB_LOG_SQL, true);
	}

	public DefaultDataBaseConfig(boolean batchEnabled, 
			int useBatchThreshold,
			int processSizePerBatch) {
		super();
		this.useBatchThreshold = useBatchThreshold;
		this.processSizePerBatch = processSizePerBatch;
		this.batchEnabled = batchEnabled;
	}

	@Override
	public int getUseBatchThreshold() {
		return useBatchThreshold;
	}

	@Override
	public boolean isBatchEnabled() {
		return batchEnabled;
	}

	public int getProcessSizePerBatch() {
		return processSizePerBatch;
	}


	public void setUseBatchThreshold(int useBatchThreshold) {
		this.useBatchThreshold = useBatchThreshold;
	}

	public boolean isLogSql() {
		return logSql;
	}

	public void setLogSql(boolean logSql) {
		this.logSql = logSql;
	}

	public boolean isWatchSqlFile() {
		return watchSqlFile;
	}

	public void setWatchSqlFile(boolean watchSqlFile) {
		this.watchSqlFile = watchSqlFile;
	}

	public String[] getModelBasePackages() {
		return modelBasePackages;
	}

	public void setModelBasePackages(String[] modelBasePackages) {
		this.modelBasePackages = modelBasePackages;
	}

	public void setProcessSizePerBatch(int processSizePerBatch) {
		this.processSizePerBatch = processSizePerBatch;
	}

	public void setBatchEnabled(boolean batchEnabled) {
		this.batchEnabled = batchEnabled;
	}

}
