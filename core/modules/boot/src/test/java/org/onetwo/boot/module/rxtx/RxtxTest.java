package org.onetwo.boot.module.rxtx;

import org.onetwo.common.utils.LangUtils;

/**
 * @author weishao zeng
 * <br/>
 */

public class RxtxTest {

	public static void main(String[] args) {
		SerialPortManager mgr = new SerialPortManager(new SimpleSerialPortEventPublisher());
		mgr.init();
		JSerialPort serialPort = mgr.getSerialPort("COM2");
		serialPort.open("test", 2000, 115200);
		byte[] data = LangUtils.hex2Bytes("00 05 02 05 0C".replace(" ", ""));
		serialPort.write(data);
		
		LangUtils.CONSOLE.waitIf("go", in -> {
			serialPort.write(data);
		});
		LangUtils.CONSOLE.exitIf("exit");
	}

}
