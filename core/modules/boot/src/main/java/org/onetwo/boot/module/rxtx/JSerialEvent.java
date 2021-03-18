package org.onetwo.boot.module.rxtx;

import gnu.io.SerialPortEvent;

/**
 * @author weishao zeng
 * <br/>
 */

public class JSerialEvent {

	final private JSerialPort source;
	final private SerialPortEvent event;
	
	public JSerialEvent(JSerialPort source, SerialPortEvent event) {
		super();
		this.source = source;
		this.event = event;
	}

	public JSerialPort getSource() {
		return source;
	}

	public SerialPortEvent getEvent() {
		return event;
	}
	
}
