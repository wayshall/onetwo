package org.onetwo.boot.module.rxtx;

import java.io.InputStream;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.utils.LangUtils;
import org.slf4j.Logger;

import gnu.io.SerialPortEvent;

/**
 * @author weishao zeng
 * <br/>
 */

public class SimpleSerialPortEventListener {
	
	protected final Logger logger = JFishLoggerFactory.getLogger(getClass());

//	@EventListener
	public void onSerialEvent(JSerialEvent serialEvent) {
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
			logger.info("收到串口事件：{}", eventType);
			break;

		case SerialPortEvent.DATA_AVAILABLE: // 1 串口存在可用数据
			onDataAvailable(port);
			break;
		default: 
			logger.error("收到无法识别的串口事件：" + eventType);
			break;
		}
	}
	
	protected void onDataAvailable(JSerialPort port) {
		InputStream in = null;
		try {
			in = port.getSerialPort().getInputStream();
			int size = in.available();
			byte[] data = new byte[size];
			while (size>0) {
				in.read(data);
				receiveData(data);
				size = in.available();
			}
		} catch (Exception e) {
			port.close();
			throw new BaseException("读取数据错误：" + e.getMessage(), e);
		} finally {
//			FileUtils.close(in);
		}
	}
	
	protected void receiveData(byte[] data) {
		logger.info("receiveData: ", LangUtils.toHex(data));
	}
	
}
