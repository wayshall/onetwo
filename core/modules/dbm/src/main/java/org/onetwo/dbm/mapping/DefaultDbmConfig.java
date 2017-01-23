package org.onetwo.dbm.mapping;


public class DefaultDbmConfig implements DbmConfig {
	/*public static final String JFISH_BASE_PACKAGES = "jfish.base.packages";
	public static final String JFISH_DBM_SQL_LOG = "jfish.dbm.sql.log";
	public static final String JFISH_DBM_SQL_WATCH = "jfish.dbm.sql.watch";*/
	/***
	 * the threshold of useBatchOptimize
	 */
	private int useBatchThreshold = 50;
	private int processSizePerBatch = 10000;
	/****
	 * whether use jdbc batch to optimeze insert or update list
	 */
	private boolean useBatchOptimize = true;
	private boolean logSql = true;
	private boolean watchSqlFile = true;
//	private String[] modelPackagesToScan;
	

	public DefaultDbmConfig(){
	}

	public DefaultDbmConfig(boolean batchEnabled, 
			int useBatchThreshold,
			int processSizePerBatch) {
		super();
		this.useBatchThreshold = useBatchThreshold;
		this.processSizePerBatch = processSizePerBatch;
		this.useBatchOptimize = batchEnabled;
	}

	/*public String[] getModelPackagesToScan() {
		return modelPackagesToScan;
	}

	public void setModelPackagesToScan(String... modelPackagesToScan) {
		this.modelPackagesToScan = modelPackagesToScan;
	}*/

	@Override
	public int getUseBatchThreshold() {
		return useBatchThreshold;
	}

	@Override
	public boolean isBatchEnabled() {
		return useBatchOptimize;
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

	public void setProcessSizePerBatch(int processSizePerBatch) {
		this.processSizePerBatch = processSizePerBatch;
	}

	public void setBatchEnabled(boolean batchEnabled) {
		this.useBatchOptimize = batchEnabled;
	}

}
