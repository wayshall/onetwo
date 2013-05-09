package org.onetwo.common.ejb.jms;

import org.junit.Before;
import org.junit.Test;
import org.onetwo.common.utils.DateUtil;
import org.onetwo.common.utils.propconf.AppConfig;
import org.onetwo.common.utils.propconf.PropConfig;

public class JMSSendTest {
	
	private PropConfig config;
	
	@Before
	public void setup(){
		config = AppConfig.create("jndi-jms.properties");
	}
	
	@Test
	public void testJMS(){
	}
	
	public static void main(String[] args){
		PropConfig config = AppConfig.create("jndi-jms.properties");
		MessageProcessor processor = MessageFactory.createSender(config);
		processor.send("exit22-"+DateUtil.nowString());
		processor.destroy();
	}

}
