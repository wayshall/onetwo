package org.onetwo.boot.module.rxtx;
/**
 * @author weishao zeng
 * <br/>
 */

public interface SerialPortEventPublisher {
	
	void publish(JSerialEvent serialEvent);

}
