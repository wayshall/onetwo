package org.onetwo.boot.module.rxtx;

import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.utils.LangUtils;
import org.springframework.util.Assert;

import gnu.io.SerialPortEventListener;


/**
 * 接口设备基础实现类
 * 进一步封装JSerialPort类
 * @author weishao zeng
 * <br/>
 */
public class SerialCommandExecutor {
	
//	protected final Logger logger = JFishLoggerFactory.getLogger(getClass());
	
	private SerialPortManager serialPortManager;
	private SerialConfig serialConfig;
	private JSerialPort port;
	
	public SerialCommandExecutor(SerialPortManager serialPortManager) {
		super();
		this.serialPortManager = serialPortManager;
	}

	/****
	 * 构造对象后，需要调用init方法初始化
	 */
	public void init() {
		this.getSerialConfig().checkConfig();
		port = serialPortManager.getSerialPort(getSerialConfig().getPort());
//		Assert.notNull(d.getTimeout(), "timeout不能为空！");
//		open();
	}
	
	public void open() {
		int timeout = getTimeout(getSerialConfig().getTimeout());
		Assert.notNull(getSerialConfig().getBaudrate(), "波特率参数不能为空！");
		port.open("weigh-"+getSerialConfig().getPort(), timeout, getSerialConfig().getBaudrate());
//		port.addListener(this);
	}
	

	/****
	 * 发送命令
	 * @author weishao zeng
	 * @param cmd
	 */
	public <T> T executeCmd(SeriaDeivceCommand<T> cmd, int reciveDataInSeconds) {
		try {
			this.open();
			this.port.addListener(new CommandSerialEventListenerAdaptor(port, cmd));
			byte[] data = LangUtils.hex2Bytes(cmd.getCommand());
			this.port.write(data);
			LangUtils.await(reciveDataInSeconds);
		} catch (Exception e) {
			throw new ServiceException("发送命令出错：" + cmd.getCommand(), e);
		} finally {
			this.port.close();
		}
		return cmd.decodeData();
	}

	private int getTimeout(Integer timeout) {
		int timeoutInMillis = 20000;
		if (timeout!=null) {
			timeoutInMillis = timeout * 1000;
		}
		return timeoutInMillis;
	}
	

	public void addListener(SerialPortEventListener lisenner) {
//		this.port.addListener(new WeighCommandSerialEventListenerAdaptor(port, cmd));
		this.port.addListener(lisenner);
	}

	public SerialConfig getSerialConfig() {
		return serialConfig;
	}

	public void setSerialConfig(SerialConfig serialConfig) {
		this.serialConfig = serialConfig;
	}

}
