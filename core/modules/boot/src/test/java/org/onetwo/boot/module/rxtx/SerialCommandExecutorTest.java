package org.onetwo.boot.module.rxtx;
/**
 * @author weishao zeng
 * <br/>
 */

import java.util.Map;

import org.junit.Test;
import org.onetwo.boot.module.rxtx.weigh.GetAllWeighCommand;
import org.onetwo.boot.module.rxtx.weigh.ModuleWeighInfo;

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
		
		GetAllWeighCommand cmd = new GetAllWeighCommand();
		Map<String, ModuleWeighInfo> weighList = reader.executeCmd(cmd, 4);
		System.out.println("weighList: " + weighList);
		
//		LangUtils.CONSOLE.exitIf("no");
	}
}
