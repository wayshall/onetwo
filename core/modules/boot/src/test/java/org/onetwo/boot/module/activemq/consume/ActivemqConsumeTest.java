package org.onetwo.boot.module.activemq.consume;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.onetwo.common.utils.LangUtils;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author wayshall
 * <br/>
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes=ActivemqConsumeApplication.class,
//				properties={"spring.activemq.in-memory=false", "spring.activemq.pool.enabled=false", "spring.activemq.broker-url=tcp://localhost:61616"},
				properties={
//							"spring.activemq.in-memory=true", "spring.activemq.pool.enabled=false", 
							"spring.activemq.in-memory=false", "spring.activemq.pool.enabled=false", "spring.activemq.broker-url=tcp://localhost:61616", 
							"spring.activemq.packages.trustAll=true",
//							org.onetwo.boot.core.config.BootJFishConfig.ZIFISH_CONFIG_PREFIX+ ".activemq.messageConverter=jackson2",
//							org.onetwo.boot.core.config.BootJFishConfig.ZIFISH_CONFIG_PREFIX+ ".activemq.embedded.enabled=true", org.onetwo.boot.core.config.BootJFishConfig.ZIFISH_CONFIG_PREFIX+ ".activemq.jdbcStore.enabled=true"
							org.onetwo.boot.core.config.BootJFishConfig.ZIFISH_CONFIG_PREFIX+ ".activemq.enabled=true"
							}
)
public class ActivemqConsumeTest {

	@Test
	public void testForConsume() throws InterruptedException {
		LangUtils.CONSOLE.exitIf("exit");
	}

}
