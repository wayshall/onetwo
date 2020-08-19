package org.onetwo.boot.module.rxtx;
/**
 * @author weishao zeng
 * <br/>
 */

public class SimpleSerialPortEventPublisher implements SerialPortEventPublisher {
	
	private SimpleSerialPortEventListener listener;
	
	public SimpleSerialPortEventPublisher() {
		this.listener = new SimpleSerialPortEventListener();
	}

	@Override
	public void publish(JSerialEvent serialEvent) {
		listener.onSerialEvent(serialEvent);
	}

}
