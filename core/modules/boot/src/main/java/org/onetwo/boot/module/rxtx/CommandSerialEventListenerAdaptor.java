package org.onetwo.boot.module.rxtx;

import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

/**
 * @author weishao zeng
 * <br/>
 */

public class CommandSerialEventListenerAdaptor implements SerialPortEventListener {

	private JSerialPort port;
	private SeriaDeivceCommand<?> command;
	
	public CommandSerialEventListenerAdaptor(JSerialPort port, SeriaDeivceCommand<?> command) {
		super();
		this.port = port;
		this.command = command;
	}

	@Override
	public void serialEvent(SerialPortEvent event) {
		JSerialEvent serialEvent = new JSerialEvent(port, event);
		command.onSerialEvent(serialEvent);
	}

}
