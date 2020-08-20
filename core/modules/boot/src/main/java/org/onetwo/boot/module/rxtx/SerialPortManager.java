package org.onetwo.boot.module.rxtx;

import java.util.Enumeration;
import java.util.Map;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.log.JFishLoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import com.google.common.collect.Maps;

import gnu.io.CommPortIdentifier;

/**
 * @author weishao zeng
 * <br/>
 */

public class SerialPortManager implements InitializingBean {

	private Map<String, CommPortIdentifier> portMap;
	
	private SerialPortEventPublisher serialPortEventPublisher;
	
	public SerialPortManager() {
		this(null);
	}
	public SerialPortManager(SerialPortEventPublisher serialPortEventPublisher) {
		super();
		this.serialPortEventPublisher = serialPortEventPublisher;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		this.init();
	}

	public void init() {
		this.portMap = scanPorts();
	}
	
	public JSerialPort getSerialPort(String portName) {
		portName = portName.toUpperCase();
		CommPortIdentifier portIdentifier = getCommPortIdentifier(portName);
		if (portIdentifier==null) {
			throw new BaseException("serial port not found: " + portName);
		}
		JSerialPort serialPort = createJSerialPort(portIdentifier);
		return serialPort;
	}
	
	protected JSerialPort createJSerialPort(CommPortIdentifier portIdentifier) {
		return new JSerialPort(portIdentifier, serialPortEventPublisher);
	}
	
	public CommPortIdentifier getCommPortIdentifier(String portName) {
		return portMap.get(portName);
	}

	
    @SuppressWarnings("unchecked")
	public static final Map<String, CommPortIdentifier> scanPorts() {
        //获得当前可用串口
        Enumeration<CommPortIdentifier> identifiers = CommPortIdentifier.getPortIdentifiers();//获得所有串口

        Map<String, CommPortIdentifier> portMap = Maps.newHashMap();
        //串口名字添加到List并返回    
        while (identifiers.hasMoreElements()) {
        	CommPortIdentifier port = identifiers.nextElement();
        	portMap.put(port.getName().toUpperCase(), port);
        	JFishLoggerFactory.getCommonLogger().info("scaned port: {}", port.getName());
        }
        return portMap;
    }

	public SerialPortEventPublisher getSerialPortEventPublisher() {
		return serialPortEventPublisher;
	}
    
}
