package org.onetwo.boot.module.artemis.consumer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.onetwo.boot.module.activemq.ActivemqProperties;
import org.onetwo.common.utils.LangUtils;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author wayshall
 * <br/>
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes=ArtemisConsumeApplication.class,
//				properties={"spring.activemq.in-memory=false", "spring.activemq.pool.enabled=false", "spring.activemq.broker-url=tcp://localhost:61616"},
				properties={
//							"spring.activemq.in-memory=true", "spring.activemq.pool.enabled=false", 
							"spring.activemq.in-memory=false", 
							"spring.activemq.pool.enabled=false", 
							"spring.activemq.packages.trustAll=true",
							"spring.main.allow-bean-definition-overriding=true",
							"spring.artemis.mode=native", 
							"spring.artemis.host=localhost", 
							"spring.artemis.port=61616", 
							"spring.artemis.user=admin", 
							"spring.artemis.password=admin", 
							ActivemqProperties.CONVERTER_KEY+ "=jackson2",
//							org.onetwo.boot.core.config.BootJFishConfig.ZIFISH_CONFIG_PREFIX+ ".activemq.embedded.enabled=true", org.onetwo.boot.core.config.BootJFishConfig.ZIFISH_CONFIG_PREFIX+ ".activemq.jdbcStore.enabled=true"
//							org.onetwo.boot.core.config.BootJFishConfig.ZIFISH_CONFIG_PREFIX+ ".jms.converter=jackson2",
							org.onetwo.boot.core.config.BootJFishConfig.ZIFISH_CONFIG_PREFIX+ ".activemq.type=artemis",
							org.onetwo.boot.core.config.BootJFishConfig.ZIFISH_CONFIG_PREFIX+ ".activemq.enabled=true",
							org.onetwo.boot.core.config.BootJFishConfig.ZIFISH_CONFIG_PREFIX+ ".activemq.topic.subscriptionDurable=true",
							org.onetwo.boot.core.config.BootJFishConfig.ZIFISH_CONFIG_PREFIX+ ".activemq.topic.subscriptionShared=true"
							}
)
public class ArtemisConsumeTest {

	@Test
	public void testForConsume() throws InterruptedException {
		LangUtils.CONSOLE.exitIf("exit");
	}

}
