package org.onetwo.boot.module.rxtx;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

/**
 * @author weishao zeng
 * <br/>
 */

public class SpringSerialPortEventPublisher implements SerialPortEventPublisher {
	@Autowired
	private ApplicationContext applicationContext;

	@Override
	public void publish(JSerialEvent event) {
		applicationContext.publishEvent(event);
	}

}
