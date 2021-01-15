package org.onetwo.boot.module.rxtx;
/**
 * @author weishao zeng
 * <br/>
 */

import java.util.Map;

import org.junit.Test;
import org.onetwo.boot.module.rxtx.weigh.GetAllWeighTestCommand;
import org.onetwo.boot.module.rxtx.weigh.ModuleWeighInfo;
import org.onetwo.common.utils.LangUtils;

public class SerialCommandExecutorTest {
	SerialPortManager serialPortManager = new SerialPortManager();

	@Test
	public void testGetAllWeighCommand() {
		serialPortManager.init();
		
		SerialCommandExecutor reader = new SerialCommandExecutor(serialPortManager);
		SerialConfig readerProps = new SerialConfig();
		readerProps.setPort("COM2");
		readerProps.setBaudrate(115200);
		reader.setSerialConfig(readerProps);
		reader.init();
		
		GetAllWeighTestCommand cmd = new GetAllWeighTestCommand();
		Map<String, ModuleWeighInfo> weighList = reader.executeCmd(cmd, 4);
		System.out.println("weighList: " + weighList);
		
//		LangUtils.CONSOLE.exitIf("no");
	}

	@Test
	public void testRfid() {
		serialPortManager.init();
		
		SerialCommandExecutor reader = new SerialCommandExecutor(serialPortManager);
		SerialConfig readerProps = new SerialConfig();
		readerProps.setPort("COM2");
		readerProps.setBaudrate(115200);
		reader.setSerialConfig(readerProps);
		reader.init();
		
		reader.open();
		LangUtils.CONSOLE.exitIf("no");
	}
}
