package org.onetwo.boot.module.rxtx;

public interface SeriaDeivceCommand<T> {

	/***
	 * 指令
	 * @author weishao zeng
	 * @return
	 */
	String getCommand();
	
	/***
	 * 收到数据
	 * @author weishao zeng
	 * @param serialEvent
	 */
	void onSerialEvent(JSerialEvent serialEvent);
	
	T decodeData();
	
	void clearData();
	
}
