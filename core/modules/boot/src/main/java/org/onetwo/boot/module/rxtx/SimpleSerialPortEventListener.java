package org.onetwo.boot.module.rxtx;

import java.io.InputStream;

import org.onetwo.common.utils.LangUtils;
import org.springframework.context.event.EventListener;

import gnu.io.SerialPortEvent;

/**
 * @author weishao zeng
 * <br/>
 */

public class SimpleSerialPortEventListener {

	@EventListener
	public void serialEvent(JSerialEvent serialEvent) {
		int eventType = serialEvent.getEvent().getEventType();
		
		JSerialPort port = serialEvent.getSource();
		
		switch (eventType) {

		case SerialPortEvent.BI: // 10 通讯中断
			// 重连？
			port.close();
			throw new RuntimeException("与串口设备通讯中断");

		case SerialPortEvent.OE: // 7 溢位（溢出）错误
		case SerialPortEvent.FE: // 9 帧错误
		case SerialPortEvent.PE: // 8 奇偶校验错误
		case SerialPortEvent.CD: // 6 载波检测
		case SerialPortEvent.CTS: // 3 清除待发送数据
		case SerialPortEvent.DSR: // 4 待发送数据准备好了
		case SerialPortEvent.RI: // 5 振铃指示
		case SerialPortEvent.OUTPUT_BUFFER_EMPTY: // 2 输出缓冲区已清空
			System.out.println("收到串口事件：" + eventType);
			break;

		case SerialPortEvent.DATA_AVAILABLE: // 1 串口存在可用数据
			InputStream in = null;
			try {
				in = port.getSerialPort().getInputStream();
				int size = in.available();
				byte[] data = new byte[size];
				while (size>0) {
					in.read(data);
					System.out.println("data: " + LangUtils.toHex(data));
					size = in.available();
				}
			} catch (Exception e) {
				port.close();
				throw new RuntimeException("读取数据错误：" + e.getMessage(), e);
			} finally {
//				FileUtils.close(in);
			}
			break;
		}
	}
	
}
