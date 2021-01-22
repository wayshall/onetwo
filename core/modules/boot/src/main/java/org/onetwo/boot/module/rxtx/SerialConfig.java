package org.onetwo.boot.module.rxtx;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.utils.StringUtils;

import lombok.Data;

/**
 * @author weishao zeng
 * <br/>
 */
@Data
public class SerialConfig {
//	String readerName = "COM3:115200";
	/***
	 * 端口名称
	 */
	String portLabel;
	/***
	 * 端口
	 */
	String port;
	/***
	 * 默认波特率 115200
	 */
	Integer baudrate = 115200;
	int timeout = 2000;
	
	public String getReaderName() {
		return port + ":" + baudrate;
	}
	
	public void checkConfig() {
		if (StringUtils.isBlank(port)) {
			throw new BaseException("没有配置串口端口：" + port);
		}
		if (baudrate==null) {
			throw new BaseException("没有配置串口波特率：" + baudrate);
		}
	}
	
	public String getPortLabel() {
		if (StringUtils.isBlank(portLabel)) {
			return getPort();
		}
		return portLabel;
	}
}
