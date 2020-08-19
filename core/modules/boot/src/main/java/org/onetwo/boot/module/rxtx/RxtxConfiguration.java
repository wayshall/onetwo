package org.onetwo.boot.module.rxtx;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import gnu.io.CommPortIdentifier;

/**
 * @author weishao zeng
 * <br/>
 */
@Configuration
@ConditionalOnClass(CommPortIdentifier.class)
public class RxtxConfiguration {
	
	@Bean
	@ConditionalOnMissingBean(SerialPortManager.class)
	public SerialPortManager serialPortManager(SerialPortEventPublisher serialPortEventPublisher) {
		SerialPortManager mgr = new SerialPortManager(serialPortEventPublisher);
		return mgr;
	}
	
	@Bean
	@ConditionalOnMissingBean(SerialPortEventPublisher.class)
	public SerialPortEventPublisher serialPortEventPublisher() {
		SerialPortEventPublisher publisher = new SpringSerialPortEventPublisher();
		return publisher;
	}

}
