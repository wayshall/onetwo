package org.onetwo.dbm.mapping;

import org.onetwo.dbm.spring.EnableDbmAttributes;

public interface DbmConfig {

	public boolean isEnableSessionCache();
	public EnableDbmAttributes getEnableDbmAttributes();
	public void onEnableDbmAttributes(EnableDbmAttributes attributes);

	/******
	 * 当保存或者删除接口参数是列表时，size大于临界值userBatchThreshold的将会自动转为调用jdbc batch
	 * 默认50
	 */
	public int getUseBatchThreshold();

	/*****
	 * 是否开启batch插入，默认开启
	 * @return
	 */
	public boolean isUseBatchOptimize();
	
	/****
	 * 批量处理时，每次提交的数据量
	 * 默认10000
	 * @return
	 */
	public int getProcessSizePerBatch();
	
	public boolean isLogSql();
	
	public boolean isWatchSqlFile();
	
	/****
	 * package to scan only for model
	 * @return
	 */
//	public String[] getModelPackagesToScan();
	
	public String getDataSource();

}