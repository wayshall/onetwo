package org.onetwo.common.fish.orm;

public interface DataBaseConfig {


	/******
	 * 当保存或者删除接口参数是列表时，size大于临界值userBatchThreshold的将会自动转为调用jdbc batch
	 * 默认50
	 */
	public int getUserBatchThreshold();

	/*****
	 * 是否开启batch插入，默认开启
	 * @return
	 */
	public boolean isBatchEnabled();
	
	/****
	 * 每次批量处理时允许的最大数据量
	 * 默认1000
	 * @return
	 */
	public int getProcessSizePerBatch();

}