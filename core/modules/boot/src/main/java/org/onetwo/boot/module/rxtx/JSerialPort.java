package org.onetwo.boot.module.rxtx;

import java.io.IOException;
import java.io.OutputStream;
import java.util.TooManyListenersException;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.file.FileUtils;
import org.onetwo.common.log.JFishLoggerFactory;
import org.slf4j.Logger;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

/**
 * 串口设备
 * 包含基本的连接（打开端口）、关闭和事件监听
 * 
 * @author weishao zeng <br/>
 */
public class JSerialPort {
	
	private final Logger logger = JFishLoggerFactory.getLogger(getClass());

	private CommPortIdentifier portIdentifier;
	private SerialPort serialPort;
	private volatile boolean connected;
//	private SerialPortEventPublisher publisher;

//	public JSerialPort(CommPortIdentifier portIdentifier, SerialPortEventPublisher publisher) {
	public JSerialPort(CommPortIdentifier portIdentifier) {
		super();
		this.portIdentifier = portIdentifier;
//		this.publisher = publisher;
	}

	/***
	 * 打开串口
	 * @param name
	 * @param timeoutInMillis
	 * @param baudrate
	 */
	public void open(String name, int timeoutInMillis, int baudrate) {
		CommPort commPort;
		try {
			commPort = this.portIdentifier.open(name, timeoutInMillis);
			this.connected = true;
			logger.info("已成功连接串口设备：{}", name);
		} catch (PortInUseException e) {
			throw new BaseException("端口已被占用!", e);
		}
		if (commPort instanceof SerialPort) {
			this.serialPort = (SerialPort) commPort;
		} else {
			close();
			throw new BaseException("端口不是串口：" + this.portIdentifier.getName());
		}

		configPort(serialPort, baudrate);
	}
	
	protected void configPort(SerialPort serialPort, int baudrate) {
		configBaudrate(this.serialPort, baudrate);
		
//		if (publisher!=null) {
//			addListener(this);
//		}
	}
	
	/***
	 * 向串口写入数据
	 * @param data
	 */
	public void write(byte[] data) {
		OutputStream output = null;
		try {
			output = this.serialPort.getOutputStream();
			output.write(data);
		} catch (IOException e) {
			throw new BaseException("写入串口失败:" + e.getMessage(), e);
		} finally {
			FileUtils.close(output);
		}
	}
	
	/***
	 * 移除事件监听器
	 * 关闭串口连接
	 */
	public void close() {
		this.connected = false;
		removeEventListener();
		this.serialPort.close();
		this.serialPort = null;
	}

	public boolean isConnected() {
		return connected;
	}

	/****
	 * 监听接收串口事件，重新包装和发布
	 */
//	@Override
	private void serialEvent(SerialPortEvent event) {
		JSerialEvent serialEvent = new JSerialEvent(this, event);
//		publisher.publish(serialEvent);
		
//		int eventType = event.getEventType();
//		switch (eventType) {
//
//		case SerialPortEvent.BI: // 10 通讯中断
//			// 重连？
//			this.close();
//			throw new RuntimeException("与串口设备通讯中断");
//
//		case SerialPortEvent.OE: // 7 溢位（溢出）错误
//		case SerialPortEvent.FE: // 9 帧错误
//		case SerialPortEvent.PE: // 8 奇偶校验错误
//		case SerialPortEvent.CD: // 6 载波检测
//		case SerialPortEvent.CTS: // 3 清除待发送数据
//		case SerialPortEvent.DSR: // 4 待发送数据准备好了
//		case SerialPortEvent.RI: // 5 振铃指示
//		case SerialPortEvent.OUTPUT_BUFFER_EMPTY: // 2 输出缓冲区已清空
//			System.out.println("收到串口事件：" + eventType);
//			break;
//
//		case SerialPortEvent.DATA_AVAILABLE: // 1 串口存在可用数据
//			InputStream in = null;
//			try {
//				in = this.serialPort.getInputStream();
//				int size = in.available();
//				byte[] data = new byte[size];
//				while (size>0) {
//					in.read(data);
//					System.out.println("data: " + LangUtils.toHex(data));
//					size = in.available();
//				}
//			} catch (Exception e) {
//				close();
//				throw new RuntimeException("读取数据错误：" + e.getMessage(), e);
//			} finally {
////				FileUtils.close(in);
//			}
//			break;
//		}
	}

	/****
	 * 添加原生的监听器
	 * @param lisenner
	 */
	public void addListener(SerialPortEventListener lisenner) {
		try {
			this.serialPort.addEventListener(lisenner);
			// 设置当有数据到达时唤醒监听接收线程
			this.serialPort.notifyOnDataAvailable(true);
			// 设置当通信中断时唤醒中断线程
			this.serialPort.notifyOnBreakInterrupt(true);
		} catch (TooManyListenersException e) {
			throw new RuntimeException("无法添加更多的串口监听器", e);
		}
	}

	public void removeEventListener() {
		this.serialPort.removeEventListener();
	}

	public SerialPort getSerialPort() {
		return serialPort;
	}

	/***
	 * 设置端口的波特率
	 * 
	 * @author weishao zeng
	 * @param serialPort
	 * @param baudrate
	 */
	static void configBaudrate(SerialPort serialPort, int baudrate) {
		try {
			serialPort.setSerialPortParams(baudrate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);
		} catch (UnsupportedCommOperationException e) {
			throw new RuntimeException("设置波特率出错：" + e.getMessage(), e);
		}
	}

}
