package org.onetwo.common.fish.orm;

public interface DataBaseConfig {


	/******
	 * 当保存接口参数是列表时，size大于临界值userBatchThreshold的将会自动转为调用jdbc batch
	 */
	public int getUserBatchThreshold();

	/*****
	 * 是否开启batch插入，默认开启
	 * @return
	 */
	public boolean isBatchEnabled();
	
	/****
	 * 
	 * @return
	 */
	public int getBatchSizeForUpdate();

}